/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MES;

import GreenhouseAPI.Greenhouse;
import Protocol.Protocol;
import SCADA.ISCADA;
import SCADA.SCADA;
import java.nio.channels.AlreadyBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Morten
 */
public class MES {

    private List<Protocol> protocolArray;
    ERP_Connect obj2;
    RMI_Client c;
    private ISCADA scada;
    private ArrayList ordreList = new ArrayList();

    public static void main(String[] args) throws RemoteException {
        MES m = new MES();
        m.makeProtocols();
        m.ERPConnect();
        m.SCADAConnect();
//        m.startServer();
    }

    private void makeProtocols() {
        protocolArray = new ArrayList<Protocol>();
        Protocol p1 = new Protocol("2014001", 21, 16, 26, 0, 92, 8, 20);
        protocolArray.add(p1);
        Protocol p2 = new Protocol("2014002", 21, 16, 26, 0, 92, 8, 20);
        protocolArray.add(p2);
        Protocol p3 = new Protocol("2014101", 15, 10, 20, 0, 92, 8, 50);
        protocolArray.add(p3);
        Protocol p4 = new Protocol("2014102", 15, 10, 20, 0, 92, 8, 25);
        protocolArray.add(p4);
        Protocol p5 = new Protocol("201420", 15, 10, 20, 0, 92, 8, 64);
        protocolArray.add(p5);
        Protocol p6 = new Protocol("2014202", 15, 10, 20, 0, 92, 8, 42);
        protocolArray.add(p6);
        Protocol p7 = new Protocol("2014203", 15, 10, 20, 0, 92, 8, 52);
        protocolArray.add(p7);

    }

    private void ERPConnect() throws RemoteException {
        obj2 = new ERP_Connect();
        obj2.getConnection();
        obj2.getDataFromERP();

    }

    private void SCADAConnect() throws RemoteException {
        c = new RMI_Client();
        c.clientConnect();
        ordreList = obj2.getOrdreList();
        c.getInfoFromSCADA();
        c.sendDataToSCADA(ordreList);

    }

    public boolean startServer() throws RemoteException {

        try {
            Registry registry = LocateRegistry.createRegistry(ISCADA.REGISTRY_PORT_MES);
            registry.bind(ISCADA.OBJECT_NAME, (Remote) new SCADA());

        } catch (AlreadyBoundException | RemoteException e) {
            throw new Error("Error when creating server: " + e);
        } catch (java.rmi.AlreadyBoundException ex) {
            Logger.getLogger(Greenhouse.class.getName()).log(Level.SEVERE, null, ex);

        }
        System.out.println("Server running with registry on port " + ISCADA.REGISTRY_PORT_MES);
        return true;
    }

}
