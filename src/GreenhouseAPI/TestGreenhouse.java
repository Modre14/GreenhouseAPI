/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GreenhouseAPI;

import MES.RMI_Config;
import PLCCommunication.*;
import java.nio.channels.AlreadyBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * API tester
 *
 * @author sps Testing
 */
public class TestGreenhouse {

    PLCConnection con = new UDPConnection(5000, "192.168.0.10");
    IGreenhouse api = new Greenhouse(con);

    private TestGreenhouse() throws RemoteException {

    }

    private void red() throws RemoteException {

        api.startServer();
    }

    public static void main(String[] args) throws RemoteException, java.rmi.AlreadyBoundException {
        TestGreenhouse t = new TestGreenhouse();
        t.red();
//        PLCConnection con = new UDPConnection(1025, "localhost"); 
//        PLCConnection con = new SerialConnection("COM4");
//        SerialConnection.getPortList("COM1");
//        int i = 0;
//        while (i != 200) {
//            api.SetRedLight(i);
//            i++;
//            System.out.println("hell");
//            //this
//        }
//
//        api.SetBlueLight(100);
//        //api.SetTemperature(273 + 25);
//        api.SetFanSpeed(1);
//        //double outdoorTemperature; 
//        //while (true)
//        //   outdoorTemperature = api.ReadTemp2();
//
////        System.exit(3);
    }

}
