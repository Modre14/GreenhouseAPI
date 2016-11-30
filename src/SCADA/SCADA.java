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
import Protocol.Ordre;
import java.io.Serializable;
import java.nio.channels.AlreadyBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
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
    private ArrayList<Ordre> ordreList = new ArrayList<Ordre>();

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
        ordreList = info;
        System.out.println(info.get(0));
        System.out.println(info.get(1));
    }

    @Override
    public ArrayList getOrdres() throws RemoteException {
       return ordreList;
    }

}
