/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SCADA;

import GreenhouseAPI.IGreenhouse;
import java.util.Map;

/**
 *
 * @author Morten
 */
public interface ISCADA {

    IGreenhouse getGreenhouse(String IP);

    Map<String, IGreenhouse> getGreenhouseList();

    public boolean startServer();
    
}
