/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GreenhouseAPI;

import GreenhouseAPI.Greenhouse;
import MES.RMI_Config;
import java.nio.channels.AlreadyBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 *
 * @author Morten
 */
public class RMI_Server {
    public static void main(String[] args) throws java.rmi.AlreadyBoundException {
        startServer();
    }
        
        private static void startServer() throws java.rmi.AlreadyBoundException {
		try {
			Registry registry = LocateRegistry.createRegistry(RMI_Config.REGISTRY_PORT);
			registry.bind(RMI_Config.OBJECT_NAME, (Remote) new Greenhouse());
		} catch (AlreadyBoundException | RemoteException e) {
			throw new Error("Error when creating server: "+e);
		}
        System.out.println("Server running with registry on port "+ RMI_Config.REGISTRY_PORT);
	}
}
