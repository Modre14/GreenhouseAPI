/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SCADA;

import GreenhouseAPI.Alarm;
import GreenhouseAPI.Greenhouse;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import GreenhouseAPI.IGreenhouse;
import GreenhouseAPI.SimulatedGreenhouse;
import Protocol.Order;

import java.io.Serializable;
import java.nio.channels.AlreadyBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Morten
 */
public class SCADA extends UnicastRemoteObject implements ISCADA, Serializable {

    private static Map<String, IGreenhouse> ghlist;
    private static ISCADA instance = null;
    private IGreenhouse greenhouse;
    private ArrayList<Order> orderList = new ArrayList<>();
    private String greenhouseError = "";

    public SCADA() throws RemoteException {

        ghlist = new HashMap<>();

    }

    public void removeOrder(Order order) {
        orderList.remove(order);
    }

    public static ISCADA getInstance() throws RemoteException {

        if (instance == null) {
            instance = new SCADA();
            for (int i = 0; i < SCADA_CONFIG.IP_ADRESSES.length; i++) {

                ghlist.put(SCADA_CONFIG.IP_ADRESSES[i], new Greenhouse(SCADA_CONFIG.IP_ADRESSES[i]));
                System.out.println(ghlist);
            }
            instance.automate();

        }

        return instance;
    }

    @Override
    public Map<String, IGreenhouse> getGreenhouseList() throws RemoteException {
        System.out.println("Given list");
        return ghlist;
    }

    @Override
    public IGreenhouse getGreenhouse(String IP) throws RemoteException {

        return ghlist.get(IP);
    }

    public boolean startServer() {
        try {
            Registry registry = LocateRegistry.createRegistry(ISCADA.REGISTRY_PORT_SCADA);
            registry.bind(ISCADA.OBJECT_NAME, instance);

        } catch (AlreadyBoundException | RemoteException e) {
            throw new Error("Error when creating server: " + e);
        } catch (java.rmi.AlreadyBoundException ex) {
            Logger.getLogger(SCADA.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Server running with registry on port " + ISCADA.REGISTRY_PORT_SCADA);
        return true;
    }

    @Override
    public void receiveInfo(ArrayList info) throws RemoteException {
        orderList = info;
        System.out.println(info.get(0));
        System.out.println(info.get(1));
    }

    @Override
    public ArrayList<Order> getOrders() throws RemoteException {
        return orderList;
    }

    public void automate() throws RemoteException {
        new Thread(() -> {

            //simulate 24 hours
            while (true) {

                try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException ex) {
                    Logger.getLogger(SCADA.class.getName()).log(Level.SEVERE, null, ex);
                }
                //timeStamp
                double hours = 0;
                for (Map.Entry<String, IGreenhouse> ghl : ghlist.entrySet()) {

                    try {
                        IGreenhouse gh = ghl.getValue();

                        //add water
                        if (gh.getOrder() != null && gh.getOrder().getRecipe().getDays() - (gh.getOrder().getSecondsElapsed() / 3600 / 24) > 0) {
                            Date d = new Date();

                            gh.changeLightInGreenhouse();
                            gh.waterGreenhouse();

                            if (gh.getOrder().getSecondsElapsed() >= (60 * (gh.getLastLog() + 1))) {
                                gh.log();
                            }

                            //add water;
                            if (gh.getAlarm() > Alarm.OFF) {
                                String s = String.format("%02d", (int) Math.floor(gh.getOrder().getSecondsElapsed() / 3600) % 24) + ":" + String.format("%02d", (int) Math.floor(gh.getOrder().getSecondsElapsed() / 60 % 60));
                                if (gh.getAlarm() == Alarm.MINTEMP) {

                                    greenhouseError = greenhouseError + "\n" + " Time: " + s + "  Temprature is under minimum on greenhouse: " + ghl.getKey();

                                } else if (gh.getAlarm() == Alarm.MAXTEMP) {
                                    greenhouseError = greenhouseError + "\n" + " Time: " + s + "  Temprature is over maximum on greenhouse: " + ghl.getKey();
                                }
                            }

                        }

                    } catch (Exception e) {
                    }
                }
            }
        }).start();
    }

    public String getGreenhouseError() {
        return greenhouseError;
    }

    public void setGreenhouseError(String s) {
        this.greenhouseError = s;
    }

}
