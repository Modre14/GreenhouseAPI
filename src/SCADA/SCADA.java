/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SCADA;

import GreenhouseAPI.Alarm;
import GreenhouseAPI.Greenhouse;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

import GreenhouseAPI.IGreenhouse;
import GreenhouseAPI.SimulatedGreenhouse;
import Recipe.Order;

import java.io.Serializable;
import java.nio.channels.AlreadyBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Morten
 */
public class SCADA extends UnicastRemoteObject implements ISCADAFXML, Serializable, ISCADA {

    private static Map<String, IGreenhouse> ghlist;
    private static ISCADAFXML instance = null;
    private IGreenhouse greenhouse;
    private ArrayList<Order> orderList = new ArrayList<>();
    private String greenhouseError = "";

    public SCADA() throws RemoteException {

        ghlist = new HashMap<>();

    }

    public void removeOrder(Order order) {
        orderList.remove(order);
    }

    public static ISCADAFXML getInstance() throws RemoteException {

        if (instance == null) {
            instance = new SCADA();
            for (int i = 0; i < SCADA_CONFIG.IP_ADRESSES.length; i++) {

                ghlist.put(SCADA_CONFIG.IP_ADRESSES[i], new Greenhouse(SCADA_CONFIG.IP_ADRESSES[i]));

            }
            instance.automate();

        }

        return instance;
    }

    public Map<String, IGreenhouse> getGreenhouseList() throws RemoteException {

        return ghlist;
    }

    public IGreenhouse getGreenhouse(String IP) throws RemoteException {

        return ghlist.get(IP);
    }

    public boolean startServer() {
        try {
            Registry registry = LocateRegistry.createRegistry(ISCADA.REGISTRY_PORT_SCADA);
            registry.bind(ISCADA.OBJECT_NAME, instance);

        } catch (AlreadyBoundException e) {
            throw new Error("Error when creating server: " + e);
        } catch (RemoteException ex) {
            Logger.getLogger(SCADA.class.getName()).log(Level.SEVERE, null, ex);
        } catch (java.rmi.AlreadyBoundException ex) {
            Logger.getLogger(SCADA.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Server running with registry on port " + ISCADA.REGISTRY_PORT_SCADA);
        return true;
    }

    @Override
    public void receiveInfo(ArrayList info) throws RemoteException {
        orderList = info;
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

                        if (gh.getOrder() != null && gh.getOrder().getRecipe().getDays() - (gh.getOrder().getSecondsElapsed() / 3600 / 24) > 0) {
                            Date d = new Date();
                            //changeLight
                            gh.changeLightInGreenhouse();
                            //add water
                            gh.waterGreenhouse();

                            if (gh.getOrder().getSecondsElapsed() >= (60 * (gh.getLastLog() + 1))) {
                                gh.log();
                            }
                            if (gh.getAlarm() != Alarm.OFF) {

                                String s = String.format("%02d", (int) Math.floor(gh.getOrder().getSecondsElapsed() / 3600) % 24) + ":" + String.format("%02d", (int) Math.floor(gh.getOrder().getSecondsElapsed() / 60 % 60));
                                if (gh.getAlarm() == Alarm.MINTEMP) {

                                    greenhouseError = greenhouseError + "\n" + " Time: " + s + "  Temprature " + gh.ReadTemp1() + "°C is under minimum on greenhouse: " + ghl.getKey();

                                } else if (gh.getAlarm() == Alarm.MAXTEMP) {
                                    greenhouseError = greenhouseError + "\n" + " Time: " + s + "  Temprature " + gh.ReadTemp1() + " is over maximum on greenhouse: " + ghl.getKey();
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
