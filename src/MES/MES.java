/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MES;

import GreenhouseAPI.Greenhouse;
import Protocol.Order;
import Protocol.Recipe;
import SCADA.ISCADA;
import SCADA.SCADA;
import java.io.Serializable;
import java.nio.channels.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Morten
 */
public class MES {

    private ArrayList<Recipe> recipeArray = new ArrayList<Recipe>();
    ERP_Connect obj2;
    RMI_Client c;
    private ISCADA scada;
    private ArrayList orderList = new ArrayList();
    private ArrayList<Order> orders = new ArrayList<Order>();

    public static void main(String[] args) throws RemoteException {
        MES m = new MES();
        m.makeRecipes();
        m.ERPConnect();
        m.generateOrders();

        m.sendOrdersToScada();

//        m.startServer();
    }

    private void makeRecipes() {
        Recipe p1 = new Recipe("2014001", 23, 16, 26, 15, 2, 92, 8, 20, 16);
        recipeArray.add(p1);
        Recipe p2 = new Recipe("2014002", 23, 16, 26, 15, 2, 92, 8, 20, 16);
        recipeArray.add(p2);
        Recipe p3 = new Recipe("2014101", 15, 10, 20, 15, 2, 92, 8, 50, 16);
        recipeArray.add(p3);
        Recipe p4 = new Recipe("2014102", 15, 10, 20, 15, 2, 92, 8, 25, 16);
        recipeArray.add(p4);
        Recipe p5 = new Recipe("2014201", 15, 10, 20, 15, 2, 92, 8, 64, 16);
        recipeArray.add(p5);
        Recipe p6 = new Recipe("2014202", 15, 10, 20, 15, 2, 92, 8, 42, 16);
        recipeArray.add(p6);
        Recipe p7 = new Recipe("2014203", 15, 10, 20, 15, 2, 92, 8, 52, 16);
        recipeArray.add(p7);

    }

    private void generateOrders() {

        orderList = obj2.getOrderList();

        for (int i = 0; i < orderList.size(); i++) {
            String line = (String) orderList.get(i).toString();
//            System.out.println(line);
            String[] tokens = line.split(",");
            for (int j = 0; j < recipeArray.size(); j++) {
                if (recipeArray.get(j).getId().equals(tokens[0])) {
                    System.out.print(recipeArray.get(j).getId());
                    System.out.println(":   " + j);

                    //Start Date
                    String startDateConvert = tokens[3];
                    Date startDate = new Date();
                    String[] dateTokens = startDateConvert.split("-");

                    String sdate = dateTokens[2];
                    String[] startDateTokens = sdate.split(" ");

//                    startDate.setYear(Integer.valueOf(dateTokens[0]) - 1900);
//                    startDate.setMonth(Integer.valueOf(dateTokens[1]));
//                    startDate.setDate(Integer.valueOf(startDateTokens[0]));
                    //end Date
                    String endDateConvert = tokens[4];
                    Date endDate = new Date();
                    String[] edateTokens = endDateConvert.split("-");

                    String edate = dateTokens[2];
                    String[] endDateTokens = edate.split(" ");

                    endDate.setYear(Integer.valueOf(edateTokens[0]) - 1900);
                    endDate.setMonth(Integer.valueOf(edateTokens[1]));
                    endDate.setDate(Integer.valueOf(endDateTokens[0]));

                    Double q = Double.valueOf(tokens[2]);
                    int quantity = (int) (q + 0);
                    Order ordre = new Order(tokens[1], recipeArray.get(j), startDate, endDate, quantity);
                    System.out.println("Name: " + tokens[1] + " Protocol " + recipeArray.get(j) + " Start: " + startDate + " End: " + endDate + " quantity: " + quantity);
                    orders.add(ordre);
                    System.out.println(orders);

                }

            }

//            System.out.println(recipeArray);
        }

//        ordres.add(recipeArray.equals(tokens[0]), "name", date, date, 1);
        System.out.println(orders.get(0));
        System.out.println(orders.get(1));
        System.out.println(orders.get(2));
        System.out.println(orders.get(3));
        System.out.println(orders.get(4));
        System.out.println(orders.get(5));
        System.out.println(orders.get(6));
        System.out.println(orders.get(7));
    }

    private void ERPConnect() throws RemoteException {
        obj2 = new ERP_Connect();
        obj2.getConnection();
        obj2.getDataFromERP();

    }

    public void sendOrdersToScada() throws RemoteException {
        String host = JOptionPane.showInputDialog("Server name?", "localhost");
        Registry registry;
        Date date = new Date();

        Order ordre = new Order("Blomster", recipeArray.get(0), date, date, 30);
        orders.add(ordre);
        try {
            registry = LocateRegistry.getRegistry(host, ISCADA.REGISTRY_PORT_SCADA);
            ISCADA scada = (ISCADA) registry.lookup(ISCADA.OBJECT_NAME);
            scada.receiveInfo(orders);
        } catch (RemoteException | NotBoundException e) {

            throw new Error("Error" + e);
        }

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
