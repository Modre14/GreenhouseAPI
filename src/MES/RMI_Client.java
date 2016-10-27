/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MES;

import GreenhouseAPI.Greenhouse;
import GreenhouseAPI.IGreenhouse;
import java.nio.channels.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import javax.swing.JOptionPane;

/**
 *
 * @author Morten
 */
public class RMI_Client {

    IGreenhouse greenhouse;

    public static void main(String[] args) throws RemoteException {
        RMI_Client c = new RMI_Client();
        c.client();
        c.info();
    }

    public void client() {
        String host = JOptionPane.showInputDialog("Server name?", "localhost");
        Registry registry;

        try {
            registry = LocateRegistry.getRegistry(host, RMI_Config.REGISTRY_PORT);
            greenhouse = (IGreenhouse) registry.lookup(RMI_Config.OBJECT_NAME);
        } catch (RemoteException | NotBoundException e) {

            throw new Error("Error" + e);
        }

    }

    private void info() throws RemoteException {

        System.out.println(greenhouse.getInfo().toString());
    }

}
