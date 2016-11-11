/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servers;

import GreenhouseAPI.Greenhouse;
import GreenhouseAPI.IGreenhouse;
import java.nio.channels.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Morten
 */
public class SCADA {

    private Greenhouse greenhouse;
    private IGreenhouse iGreenhouse;
    public SCADA() throws RemoteException{
    }

    public static void main(String[] args) throws RemoteException {
        SCADA scada = new SCADA();
        scada.initialize();
    }

    public void initialize() throws RemoteException{
        startServer();
        clientConnect();
    }
            
    public void clientConnect() {
        String host = JOptionPane.showInputDialog("Server name?", "localhost");
        Registry registry;

        try {
            registry = LocateRegistry.getRegistry(host, IGreenhouse.REGISTRY_PORT_MES);
            iGreenhouse = (IGreenhouse) registry.lookup(IGreenhouse.OBJECT_NAME);
        } catch (RemoteException | NotBoundException e) {

            throw new Error("Error" + e);
        }

    }

    public boolean startServer() throws RemoteException {

        try {
            Registry registry = LocateRegistry.createRegistry(IGreenhouse.REGISTRY_PORT_SCADA);
            registry.bind(IGreenhouse.OBJECT_NAME, (Remote) new Greenhouse());

        } catch (AlreadyBoundException | RemoteException e) {
            throw new Error("Error when creating server: " + e);
        } catch (java.rmi.AlreadyBoundException ex) {
            Logger.getLogger(Greenhouse.class.getName()).log(Level.SEVERE, null, ex);

        }
        System.out.println("Server running with registry on port " + IGreenhouse.REGISTRY_PORT_SCADA);
        return true;
    }
}
