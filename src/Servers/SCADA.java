/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servers;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import GreenhouseAPI.Greenhouse;
import GreenhouseAPI.IGreenhouse;
/**
 *
 * @author Morten
 */
public class SCADA {

    public static void main(String[] args) throws RemoteException {
        Map<String, IGreenhouse> ghmap = new HashMap<>();

        for (int i = 0; i < SERVER_CONFIG.IP_ADRESSES.length; i++) {
            ghmap.put(SERVER_CONFIG.IP_ADRESSES[i], new Greenhouse(SERVER_CONFIG.IP_ADRESSES[i]));
        }
        try {

            Registry registry = LocateRegistry.createRegistry(SERVER_CONFIG.REGISTRY_PORT);
            registry.bind(SERVER_CONFIG.REMOTE_OBJECT_NAME, ghmap);
        } catch (RemoteException | java.rmi.AlreadyBoundException e) {
            throw new Error("Error creating server: "+e);
        }
        System.out.println("Server running with registry on port "+SERVER_CONFIG.REGISTRY_PORT);
        }
    }


}
