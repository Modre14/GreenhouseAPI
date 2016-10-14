/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MES;

import GreenhouseAPI.Greenhouse;
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
    
        private static void startServer() {
		try {
			Registry registry = LocateRegistry.createRegistry(RMI_Config.REGISTRY_PORT);
		//	registry.bind(RMI_Config.OBJECT_NAME, (Remote) "her skal vi gøre noget");
		} catch (AlreadyBoundException | RemoteException e) {
			throw new Error("Error when creating server: "+e);
		}
        System.out.println("Server running with registry on port "+ RMI_Config.REGISTRY_PORT);
	}
}
