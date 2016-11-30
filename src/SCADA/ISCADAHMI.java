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
public interface ISCADAHMI extends Remote {

    IGreenhouse getGreenhouse(String IP) throws RemoteException;

    Map<String, IGreenhouse> getGreenhouseList() throws RemoteException;

    public boolean startServer() throws RemoteException;

}
