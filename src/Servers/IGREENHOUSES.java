package Servers;

import GreenhouseAPI.Greenhouse;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;

/**
 * Created by madsn on 11-11-2016.
 */
public interface IGREENHOUSES extends Remote {

    Map<String, Greenhouse> getGreenhouses() throws RemoteException;
}
