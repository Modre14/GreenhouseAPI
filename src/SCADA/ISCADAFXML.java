/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SCADA;

import GreenhouseAPI.IGreenhouse;
import Recipe.Order;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Map;

/**
 *
 * @author Morten
 */
public interface ISCADAFXML extends Remote {

    IGreenhouse getGreenhouse(String IP) throws RemoteException;

    Map<String, IGreenhouse> getGreenhouseList() throws RemoteException;

    public boolean startServer() throws RemoteException;

    public void automate() throws RemoteException;

    public void removeOrder(Order order) throws RemoteException;

    public String getGreenhouseError() throws RemoteException;

    public void setGreenhouseError(String s) throws RemoteException;

    /**
     * Sends info to the MES system The return value //
     */
    //    String sendInfoToMES() throws RemoteException;
    //
    ArrayList getOrders() throws RemoteException;

    /**
     * Gets order information from MES
     *
     * @return
     * @throws RemoteException //
     */
//    ArrayList receiveInfo(ArrayList info) throws RemoteException;
//
//    ArrayList receiveOrder(ArrayList order) throws RemoteException;
}
