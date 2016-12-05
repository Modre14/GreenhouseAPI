/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SCADA;

import GreenhouseAPI.Greenhouse;
import java.rmi.RemoteException;
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
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Morten
 */
public class SCADA extends UnicastRemoteObject implements ISCADA, ISCADAHMI, Serializable {

    private static Map<String, IGreenhouse> ghlist;
    private static ISCADA instance = null;
    private IGreenhouse greenhouse;
    private ArrayList<Order> orderList = new ArrayList<>();

    public SCADA() throws RemoteException {

        ghlist = new HashMap<>();

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
            while (true) {
                //timeStamp
                for (Map.Entry<String, IGreenhouse> ghl : ghlist.entrySet()) {
                    IGreenhouse gh = ghl.getValue();
                    try {
                        if (gh.getOrder() != null) {
                            System.out.println(ghl.getKey() + " : Checked");
                            if (gh.ReadMoist() < gh.getOrder().getProtocol().getWaterFlow()) {
                                gh.AddWater(5);
                               
                            }
                            if (gh.ReadTemp1() > gh.getOrder().getProtocol().getMaxTemp()) {
                                gh.SetFanSpeed(2);
                            }else  {
                                gh.SetFanSpeed(0);
                            }
                        }
                    } catch (RemoteException ex) {
                        Logger.getLogger(SCADA.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }

                try {
                    TimeUnit.SECONDS.sleep(60);
                } catch (InterruptedException ex) {
                    Logger.getLogger(SCADA.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }).start();
    }
}
