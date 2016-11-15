/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SCADA;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.Map;

import GreenhouseAPI.Greenhouse;
import GreenhouseAPI.IGreenhouse;
/**
 *
 * @author Morten
 */
public class SCADA {

    private static Map<String, IGreenhouse> ghlist;
    private static SCADA instance = null;
    protected SCADA() {
        ghlist = new HashMap<>();
        
    }

    public static SCADA getInstance() throws RemoteException { 

        if (instance == null){
            instance = new SCADA();
            for (int i = 0; i < SCADA_CONFIG.IP_ADRESSES.length; i++ ) {
                ghlist.put(SCADA_CONFIG.IP_ADRESSES[i], new Greenhouse(SCADA_CONFIG.IP_ADRESSES[i]));
            }
        }
        
        return instance;
    }

    public Map<String, IGreenhouse> getGreenhouses(){
        System.out.println("Given list");
        return ghlist;
    }

}
