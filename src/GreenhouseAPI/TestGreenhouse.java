/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GreenhouseAPI;

import PLCCommunication.*;
import java.util.BitSet;

/**
 * API tester
 *
 * @author sps
 *Testing
 */
public class TestGreenhouse {

    public static void main(String[] args) {
//        PLCConnection con = new UDPConnection(1025, "localhost"); 
        PLCConnection con = new UDPConnection(5000, "192.168.0.10");
//        PLCConnection con = new SerialConnection("COM4");
//        SerialConnection.getPortList("COM1");

        IGreenhouse api = new Greenhouse(con);
        int i = 0;
        while (i != 500) {            
            api.SetRedLight(i);
            i++;
            
        }
        
        
        api.SetBlueLight(i);
        //api.SetTemperature(273 + 25);
        api.SetFanSpeed(1);
        //double outdoorTemperature; 
        //while (true)
        //   outdoorTemperature = api.ReadTemp2();

//        System.exit(3);
    }

}
