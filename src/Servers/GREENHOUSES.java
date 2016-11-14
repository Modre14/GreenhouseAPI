package Servers;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by madsn on 11-11-2016.
 */
public class GREENHOUSES extends UnicastRemoteObject implements Serializable {

    public GREENHOUSES() throws RemoteException {
        super();

    }
}
