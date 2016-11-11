/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package guigreenhouseapi;

import GreenhouseAPI.Greenhouse;
import GreenhouseAPI.IGreenhouse;
import PLCCommunication.PLCConnection;
import PLCCommunication.UDPConnection;
import Servers.SCADA;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

/**
 *
 * @author Morten
 */
public class FXMLDocumentController extends Thread implements Initializable {

    private final ObservableList IP = FXCollections.observableArrayList(
            "192.168.0.10", "192.168.0.20", "192.168.0.30", "192.168.0.40");
    
    private double temp1;
    private double temp2;
    private String levelOfMoist;
    private double waterLevelValue;
    @FXML

    private TextField waterLevel;

    private TextField Temp_outside;
    @FXML
    private TextField Level_of_moist;
    private TextField Water_level;
    @FXML

    private Button getGreenhouseDataButton;
    @FXML
    private ChoiceBox<String> listOfGreenhouse;
    @FXML
    private TextField tempInside;
    @FXML
    private ProgressBar waterlevelIndicator;
    @FXML
    private ProgressBar lightIndicator;
    @FXML
    private ProgressBar thermometerIndicatorIn;
    @FXML
    private ProgressBar thermometerIndicatorOut;
    @FXML
    private TextField tempOutside;
    @FXML
    private TextField tempSpinner;
    @FXML
    private TextField levelOfMoistSpinner;
    @FXML
    private Button startProduction;
    @FXML
    private ChoiceBox<?> listOfOrders;

    public FXMLDocumentController() throws RemoteException {

    }
    private SCADA scada;
    private PLCConnection con;

    private IGreenhouse api;

    private ArrayList<IGreenhouse> greenhouseArray;

    private void handleButtonAction(ActionEvent event) throws RemoteException {
        
        System.out.println("You clicked me!");

        try {

//            Temp_inside.setText(String.valueOf(api.ReadTemp1()));
            Temp_outside.setText(String.valueOf(api.ReadTemp2()));
            Level_of_moist.setText(String.valueOf(api.ReadMoist()));
            Water_level.setText(String.valueOf(api.ReadWaterLevel()));
        } catch (RemoteException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void update() throws InterruptedException {
        Thread.sleep(2000);
//        while (true) {
//            try {
//                
//                System.out.println(api.ReadTemp1());
//                System.out.println(this.api);
////                Temp_inside.setText(String.valueOf(api.ReadTemp1()));
////                Temp_outside.setText(String.valueOf(api.ReadTemp2()));
////                Level_of_moist.setText(String.valueOf(api.ReadMoist()));
////                Water_level.setText(String.valueOf(api.ReadWaterLevel()));
////                Height_of_plants.setText(String.valueOf(api.ReadPlantHeight()));
//            } catch (RemoteException ex) {
//                Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
//            }
//
//            Thread.sleep(50000);
//        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        listOfGreenhouse.setItems(IP);

        greenhouseArray = new ArrayList<>();
//Starts a thread foreach greenhouse conecction 
        for (int j = 0; j < IP.size(); j++) {
            con = new UDPConnection(5000, (String) IP.get(j));
            System.out.println(IP.get(j));
            System.out.println(con);

            try {
                greenhouseArray.add(new Greenhouse(con));

            } catch (RemoteException ex) {
                Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

//        try {
//            api.startServer();
////
////        try {
////            startUpdateThread();
////        } catch (InvocationTargetException ex) {
////            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
////        } catch (InterruptedException ex) {
////            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
////        }
//        } catch (RemoteException ex) {
//            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (InterruptedException ex) {
//            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

    @FXML
    private void getGreenhouseData(ActionEvent event) throws RemoteException, InvocationTargetException, InterruptedException {

        for (int i = 0; i < IP.size(); i++) {

            if (listOfGreenhouse.getValue().equals(IP.get(i))) {
                
                api = greenhouseArray.get(i);
                temp1 = api.ReadTemp1() - 253;
                tempInside.setText(String.valueOf(temp1));
                thermometerIndicatorIn.setProgress((temp1) / 50.0);
                temp2 = api.ReadTemp2() - 253;
                tempOutside.setText(String.valueOf(temp2));
                thermometerIndicatorOut.setProgress(temp2 / 50.0);
                waterLevelValue = api.ReadWaterLevel() / 10;
                waterLevel.setText(String.valueOf(waterLevelValue));
                waterlevelIndicator.setProgress(waterLevelValue / 25.0);

            }
        }

    }

}
