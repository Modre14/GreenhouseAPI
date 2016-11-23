/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MES;

import GreenhouseAPI.Greenhouse;
import Protocol.Ordre;
import Protocol.Protocol;
import SCADA.ISCADA;
import SCADA.SCADA;
import java.nio.channels.AlreadyBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Morten
 */
public class MES {

    private ArrayList<Protocol> protocolArray = new ArrayList<Protocol>();;
    ERP_Connect obj2;
    RMI_Client c;
    private ISCADA scada;
    private ArrayList ordreList = new ArrayList();
    private ArrayList<Ordre> ordres = new ArrayList<Ordre>();

    public static void main(String[] args) throws RemoteException {
        MES m = new MES();
        m.makeProtocols();
        m.ERPConnect();
        m.gennerateOrdres();
//        m.SCADAConnect();
//        m.startServer();
    }

    private void makeProtocols() {
        Protocol p1 = new Protocol("2014001", 21, 16, 26, 0, 92, 8, 20);
        protocolArray.add(p1);
        Protocol p2 = new Protocol("2014002", 21, 16, 26, 0, 92, 8, 20);
        protocolArray.add(p2);
        Protocol p3 = new Protocol("2014101", 15, 10, 20, 0, 92, 8, 50);
        protocolArray.add(p3);
        Protocol p4 = new Protocol("2014102", 15, 10, 20, 0, 92, 8, 25);
        protocolArray.add(p4);
        Protocol p5 = new Protocol("2014201", 15, 10, 20, 0, 92, 8, 64);
        protocolArray.add(p5);
        Protocol p6 = new Protocol("2014202", 15, 10, 20, 0, 92, 8, 42);
        protocolArray.add(p6);
        Protocol p7 = new Protocol("2014203", 15, 10, 20, 0, 92, 8, 52);
        protocolArray.add(p7);

    }

    private void gennerateOrdres() {

        ordreList = obj2.getOrdreList();

        for (int i = 0; i < ordreList.size(); i++) {
            String line = (String) ordreList.get(i).toString();
//            System.out.println(line);
            String[] tokens = line.split(",");
            for (int j = 0; j <protocolArray.size(); j++) {
                if (protocolArray.get(j).getId().equals(tokens[0])) {
                    System.out.print(protocolArray.get(j).getId());
                    System.out.println(":   " + j);
                    
                    //Start Date
                    String startDateConvert = tokens[3];
                    Date startDate = new Date();
                    String[] dateTokens = startDateConvert.split("-");
                    
                    
                    String sdate = dateTokens[2];
                    String[] startDateTokens = sdate.split(" ");
    
                    
                    startDate.setYear(Integer.valueOf(dateTokens[0])-1900);
                    startDate.setMonth(Integer.valueOf(dateTokens[1]));
                    startDate.setDate(Integer.valueOf(startDateTokens[0]));
                    
                    
                    //end Date
                    String endDateConvert = tokens[4];
                    Date endDate = new Date();
                    String[] edateTokens = endDateConvert.split("-");
                    
         
                    String edate = dateTokens[2];
                    String[] endDateTokens = edate.split(" ");
                    
                    
                    endDate.setYear(Integer.valueOf(edateTokens[0])-1900);
                    endDate.setMonth(Integer.valueOf(edateTokens[1]));
                    endDate.setDate(Integer.valueOf(endDateTokens[0]));
                    
                    
                    Double q = Double.valueOf(tokens[2]);
                    int quantity = (int) (q + 0);
                    Ordre ordre = new Ordre(tokens[1], protocolArray.get(j), startDate, endDate, quantity);
                    System.out.println("Name: "+tokens[1] + " Protocol " + protocolArray.get(j)+" Start: " + startDate + " End: " + endDate + " quantity: " + quantity);
                    ordres.add(ordre);
                    System.out.println(ordres.size());
                }
            }

//            System.out.println(protocolArray);
        }

//        ordres.add(protocolArray.equals(tokens[0]), "name", date, date, 1);
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
