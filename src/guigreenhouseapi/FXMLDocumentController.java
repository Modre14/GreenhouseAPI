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
    private String temp2;
    private String levelOfMoist;
    private String waterLevel;
    private String plantHeight;

    @FXML
    private Pane GreenhouseData;
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
    @FXML
    private ProgressBar thermometerIndicator;
    @FXML
    private TextField tempInside;

    public FXMLDocumentController() throws RemoteException {

    }
    private PLCConnection con;

    private IGreenhouse api;

    @FXML

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

//Starts a thread foreach greenhouse conecction 
        for (int j = 0; j < IP.size(); j++) {
            System.out.println(j);
            con = new UDPConnection(5000, (String) IP.get(j));
            System.out.println(IP.get(j));
            System.out.println(con);
            try {
                api = new Greenhouse(con);
            } catch (RemoteException ex) {
                Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            }

            switch (j) {
                case 0:
                    Thread t0 = new Thread(() -> {
                        try {

                            System.out.println(api);
                            update();

                        } catch (InterruptedException ex) {
                            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    );
                    t0.start();

                    break;
                case 1:
                    Thread t1 = new Thread(() -> {
                        try {
                            System.out.println(api);
                            update();

                        } catch (InterruptedException ex) {
                            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    );
                    t1.start();

                    break;

                case 2:

                    Thread t2 = new Thread(() -> {
                        try {
                            System.out.println(api);
                            update();

                        } catch (InterruptedException ex) {
                            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    );
                    t2.start();

                    break;
                case 3:
                    Thread t3 = new Thread(() -> {
                        try {
                            System.out.println(api);
                            update();

                        } catch (InterruptedException ex) {
                            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    );
                    t3.start();

                    break;
                default:
                    System.out.println("");
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
        System.out.println(listOfGreenhouse.getValue());
        for (int i = 0; i < IP.size(); i++) {
            System.out.println(IP.get(i));
            if (listOfGreenhouse.getValue().equals(IP.get(i))) {
                temp1 = api.ReadTemp1() - 253;
                tempInside.setText(String.valueOf(temp1));
                thermometerIndicator.setProgress((temp1) / 100.0 * 2.0);

                System.out.println("working");
            }
        }

    }

}
