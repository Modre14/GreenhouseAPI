/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SCADA;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 *
 * @author Morten
 */
public interface ISCADA extends Remote {

    public static int REGISTRY_PORT_SCADA = 54323;
    public static int REGISTRY_PORT_MES = 54322;
    public static String OBJECT_NAME = "server";
//


    /**
     * Sends info to the MES system The return value //
     */
    //    String sendInfoToMES() throws RemoteException;
    //

    /**
     * Gets order information from MES
     *
     * @return
     * @throws RemoteException //
     */
//    ArrayList receiveInfo(ArrayList info) throws RemoteException;
//
//    ArrayList receiveOrder(ArrayList order) throws RemoteException;
    void receiveInfo(ArrayList info) throws RemoteException;
}
