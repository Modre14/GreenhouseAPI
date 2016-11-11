/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servers;

import GreenhouseAPI.Greenhouse;
import GreenhouseAPI.IGreenhouse;
import PLCCommunication.PLCConnection;
import PLCCommunication.UDPConnection;
import java.nio.channels.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import static javafx.collections.FXCollections.observableArrayList;
import javafx.collections.ObservableList;
import javax.swing.JOptionPane;

/**
 *
 * @author Morten
 */
public class SCADA {

    private Greenhouse greenhouse;
    private IGreenhouse iGreenhouse;
    private final List IP = observableArrayList("192.168.0.10", "192.168.0.20", "192.168.0.30", "192.168.0.40");
    private ArrayList<IGreenhouse> greenhouseArray;

    IGreenhouse api;

    public ArrayList<IGreenhouse> getGreenhouseArray() {
        return greenhouseArray;
    }

    public List getIP() {
        return IP;
    }

    public SCADA() throws RemoteException {
       
    }

    public static void main(String[] args) throws RemoteException {


    }

    public void initialize() throws RemoteException {
        greenhouseArray = new ArrayList<>();
        startServer();
//        clientConnect();
        for (int i = 0; i < IP.size(); i++) {
            PLCConnection con = new UDPConnection(5000, (String) IP.get(i));
            IGreenhouse api = new Greenhouse(con);
            greenhouseArray.add(api);   
        }

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
