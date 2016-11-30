/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GreenhouseAPI;

import PLCCommunication.*;
import java.rmi.RemoteException;

/**
 * API tester
 *
 * @author sps Testing
 */
public class TestGreenhouse {

    IGreenhouse api = new Greenhouse("192.168.0.10");

    private TestGreenhouse() throws RemoteException {

    }

    private void red() throws RemoteException {
        api.SetBlueLight(55);
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
