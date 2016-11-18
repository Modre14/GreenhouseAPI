/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SCADA;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

import GreenhouseAPI.Greenhouse;
import GreenhouseAPI.IGreenhouse;

/**
 *
 * @author Morten
 */
public class SCADA implements ISCADA {

    private static Map<String, IGreenhouse> ghlist;
    private static ISCADA instance = null;

    protected SCADA() {
        ghlist = new HashMap<>();

    }

    public static ISCADA getInstance() throws RemoteException {

        if (instance == null) {
            instance = new SCADA();
            for (int i = 0; i < SCADA_CONFIG.IP_ADRESSES.length; i++) {
                ghlist.put(SCADA_CONFIG.IP_ADRESSES[i], new Greenhouse(SCADA_CONFIG.IP_ADRESSES[i]));
            }
        }

        return instance;
    }

    @Override
    public Map<String, IGreenhouse> getGreenhouseList() {
        System.out.println("Given list");
        return ghlist;
    }

    @Override
    public IGreenhouse getGreenhouse(String IP) {

        return ghlist.get(IP);
    }

}
