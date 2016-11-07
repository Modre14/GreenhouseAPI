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
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.rmi.RemoteException;
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
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

/**
 *
 * @author Morten
 */
public class FXMLDocumentController extends Thread implements Initializable {

    private final ObservableList IP = FXCollections.observableArrayList(
            "192.168.0.10", "192.168.0.20", "192.168.0.30", "192.168.0.40");

    private String temp1;
    private String temp2;
    private String levelOfMoist;
    private String waterLevel;
    private String plantHeight;

    @FXML
    private Pane GreenhouseData;
    @FXML
    private TextField Temp_inside;
    @FXML
    private TextField Temp_outside;
    @FXML
    private TextField Level_of_moist;
    @FXML
    private TextField Water_level;
    @FXML
    private TextField Height_of_plants;
    @FXML
    private Button refreshButton;
    @FXML
    private TextField tempSpinner;
    @FXML
    private TextField redLightSpinner;
    @FXML
    private TextField blueLightSpinner;
    @FXML
    private TextField levelOfMoistSpinner;

    @FXML
    private Button getGreenhouseDataButton;
    @FXML
    private ChoiceBox<String> listOfGreenhouse;
  

    public FXMLDocumentController() throws RemoteException {

    }
    private PLCConnection con;

    private IGreenhouse api;

    @FXML

    private void handleButtonAction(ActionEvent event) throws RemoteException {

        System.out.println("You clicked me!");

        try {

            Temp_inside.setText(String.valueOf(api.ReadTemp1()));
            Temp_outside.setText(String.valueOf(api.ReadTemp2()));
            Level_of_moist.setText(String.valueOf(api.ReadMoist()));
            Water_level.setText(String.valueOf(api.ReadWaterLevel()));
            Height_of_plants.setText(String.valueOf(api.ReadPlantHeight()));
        } catch (RemoteException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    private void update() throws InterruptedException {
        while (true) {
            try {


                System.out.println(api.ReadTemp1());

//                Temp_inside.setText(String.valueOf(api.ReadTemp1()));
//                Temp_outside.setText(String.valueOf(api.ReadTemp2()));
//                Level_of_moist.setText(String.valueOf(api.ReadMoist()));
//                Water_level.setText(String.valueOf(api.ReadWaterLevel()));
//                Height_of_plants.setText(String.valueOf(api.ReadPlantHeight()));

            } catch (RemoteException ex) {
                Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            }

            Thread.sleep(50000);
        }
    }



    @Override
    public void initialize(URL url, ResourceBundle rb) {
        listOfGreenhouse.setItems(IP);

        //Starts a thread foreach greenhouse conecction 
        for (int j = 0; j < IP.size(); j++) {
            System.out.println(j);
            con = new UDPConnection(5000, (String) IP.get(j));
            System.out.println(IP.get(j));
            System.out.println(con);
            Thread t = new Thread(() -> {
                try {
                    update();
                } catch (InterruptedException ex) {
                    Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                }
            });

            try {
                api = new Greenhouse(con);
            } catch (RemoteException ex) {
                Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            }

            t.start();
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

//        startUpdateThread();
    }

    private void clearDate() {

        temp1 = null;
        temp2 = null;
        waterLevel = null;
        levelOfMoist = null;
    }

    private String createInfoString() {
        return "";
    }

}
