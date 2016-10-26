/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GreenhouseAPI;

import MES.RMI_Config;
import PLCCommunication.*;
import com.sun.org.apache.xalan.internal.utils.FeatureManager;
import java.io.IOException;
import java.rmi.NotBoundException;
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
    private IGreenhouse greenhouse;
    IGreenhouse api = new Greenhouse(con);
    
    private TestGreenhouse() throws RemoteException{

		String host = JOptionPane.showInputDialog("Server name?" , "localhost");
		Registry registry;

		try {
			registry = LocateRegistry.getRegistry(host, RMI_Config.REGISTRY_PORT);
			greenhouse = (IGreenhouse)registry.lookup(RMI_Config.OBJECT_NAME);
		} catch(RemoteException | NotBoundException e) {

			throw new Error("Error" + e);
		}

	}
    
    private void red() throws RemoteException {
        api.SetRedLight(10);
        
    
}
    
    
 public static void main(String[] args) throws RemoteException {

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
        
    

   
    



