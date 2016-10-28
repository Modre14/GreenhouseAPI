/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MES;

import GreenhouseAPI.IGreenhouse;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import javax.swing.JOptionPane;

/**
 *
 * @author Morten
 */
public class RMI_Client {

    private IGreenhouse greenhouse;

    public void clientConnect() {
        String host = JOptionPane.showInputDialog("Server name?", "localhost");
        Registry registry;

        try {
            registry = LocateRegistry.getRegistry(host, IGreenhouse.REGISTRY_PORT);
            greenhouse = (IGreenhouse) registry.lookup(IGreenhouse.OBJECT_NAME);
        } catch (RemoteException | NotBoundException e) {

            throw new Error("Error" + e);
        }

    }

    public void getInfoFromSCADA() throws RemoteException {

        System.out.println(greenhouse.sendInfoToMES().toString());
    }

    public void sendDataToSCADA(String string) throws RemoteException {

        greenhouse.receiveInfo(string);
    }

}
