/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SCADA;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

import GreenhouseAPI.Greenhouse;
import GreenhouseAPI.IGreenhouse;
import java.nio.channels.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Morten
 */
public class SCADA implements ISCADA {

    private static Map<String, IGreenhouse> ghlist;
    private static ISCADA instance = null;
    private IGreenhouse greenhouse;

    protected SCADA() {
        ghlist = new HashMap<>();

    }

    public static ISCADA getInstance() throws RemoteException {

        if (instance == null) {
            instance = new SCADA();
            for (int i = 0; i < SCADA_CONFIG.IP_ADRESSES.length; i++) {
                ghlist.put(SCADA_CONFIG.IP_ADRESSES[i], new Greenhouse(SCADA_CONFIG.IP_ADRESSES[i]));
            }
        }

        return instance;
    }

    @Override
    public Map<String, IGreenhouse> getGreenhouseList() {
        System.out.println("Given list");
        return ghlist;
    }

    @Override
    public IGreenhouse getGreenhouse(String IP) {

        return ghlist.get(IP);
    }

    public boolean startServer() {
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

    public void getOrdres() throws RemoteException {
        String host = JOptionPane.showInputDialog("Server name?", "localhost");
        Registry registry;

        try {
            registry = LocateRegistry.getRegistry(host, IGreenhouse.REGISTRY_PORT_MES);
            greenhouse = (IGreenhouse) registry.lookup(IGreenhouse.OBJECT_NAME);
        } catch (RemoteException | NotBoundException e) {

            throw new Error("Error" + e);
        }
        
    }

}
