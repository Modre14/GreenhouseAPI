/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SCADA;

import GreenhouseAPI.IGreenhouse;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Map;

/**
 *
 * @author Morten
 */
public interface ISCADA extends  Remote {

    public static int REGISTRY_PORT_SCADA = 54323;
    public static int REGISTRY_PORT_MES = 54322;
    public static String OBJECT_NAME = "server";
//
    IGreenhouse getGreenhouse(String IP) throws RemoteException;

    Map<String, IGreenhouse> getGreenhouseList() throws RemoteException;

    public boolean startServer() throws RemoteException;

    /**
     * Sends info to the MES system The return value
//     */
//    String sendInfoToMES() throws RemoteException;
//
    ArrayList getOrders() throws RemoteException;

    /**
     * Gets order information from MES
     *
     * @return
     * @throws RemoteException
//     */
//    ArrayList receiveInfo(ArrayList info) throws RemoteException;
//
//    ArrayList receiveOrdre(ArrayList ordre) throws RemoteException;

    
    	void receiveInfo(ArrayList info) throws RemoteException;
}
