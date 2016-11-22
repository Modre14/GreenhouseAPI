/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MES;

import GreenhouseAPI.IGreenhouse;
import SCADA.ISCADA;
import java.lang.reflect.Array;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author Morten
 */
public class RMI_Client {

    public void clientConnect() {
        String host = JOptionPane.showInputDialog("Server name?", "localhost");
        Registry registry;

        try {
            registry = LocateRegistry.getRegistry(host, ISCADA.REGISTRY_PORT_SCADA);
            ISCADA scada = (ISCADA) registry.lookup(ISCADA.OBJECT_NAME);
        } catch (RemoteException | NotBoundException e) {

            throw new Error("Error" + e);
        }

    }

    public void getInfoFromSCADA() throws RemoteException {

        System.out.println(SCADA.SCADA.getInstance().sendInfoToMES().toString());
    }

    public ArrayList sendDataToSCADA(ArrayList info) throws RemoteException {
        return info;

    }

}
