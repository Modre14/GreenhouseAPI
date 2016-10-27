/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GreenhouseAPI;

import MES.RMI_Config;
import PLCCommunication.*;
import java.io.IOException;
import java.nio.channels.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.BitSet;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javax.swing.JOptionPane;

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

    public static void main(String[] args) throws RemoteException, java.rmi.AlreadyBoundException {

        TestGreenhouse t = new TestGreenhouse();

        t.startServer();

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

    /**
     * Starts a server on the SCADA system
     */
    private void startServer() throws java.rmi.AlreadyBoundException {
        try {
            Registry registry = LocateRegistry.createRegistry(RMI_Config.REGISTRY_PORT);
            registry.bind(RMI_Config.OBJECT_NAME, (Remote) new Greenhouse());
        } catch (AlreadyBoundException | RemoteException e) {
            throw new Error("Error when creating server: " + e);
        }
        System.out.println("Server running with registry on port " + RMI_Config.REGISTRY_PORT);
    }

}
