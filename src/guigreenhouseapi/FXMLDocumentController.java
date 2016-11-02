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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

/**
 *
 * @author Morten
 */
public class FXMLDocumentController extends Thread implements Initializable {

    private final ObservableList IP = FXCollections.observableArrayList(
            "192.168.0.10", "192.168.0.20", "192.168.0.30", "192.168.0.40");

    @FXML
    private Pane GreenhouseData;
    @FXML
    private TextField Temp_indside;
    @FXML
    private TextField Temp_outside;
    @FXML
    private TextField Level_of_moist;
    @FXML
    private TextField Water_level;
    @FXML
    private TextField Hight_of_plants;
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
    private TextField tempSpinner1;
    @FXML
    private TextField redLightSpinner1;
    @FXML
    private TextField blueLightSpinner1;
    @FXML
    private TextField levelOfMoistSpinner1;
    @FXML
    private Button getGreenhouseDataButton1;
    @FXML
    private Pane GreenhouseData1;
    @FXML
    private TextField Temp_indside1;
    @FXML
    private TextField Temp_outside1;
    @FXML
    private TextField Level_of_moist1;
    @FXML
    private TextField Water_level1;
    @FXML
    private TextField Hight_of_plants1;
    @FXML
    private Button refreshButton1;
    @FXML
    private TextField tempSpinner2;
    @FXML
    private TextField redLightSpinner2;
    @FXML
    private TextField blueLightSpinner2;
    @FXML
    private TextField levelOfMoistSpinner2;
    @FXML
    private Button getGreenhouseDataButton2;
    @FXML
    private Pane GreenhouseData2;
    @FXML
    private TextField Temp_indside2;
    @FXML
    private TextField Temp_outside2;
    @FXML
    private TextField Level_of_moist2;
    @FXML
    private TextField Water_level2;
    @FXML
    private TextField Hight_of_plants2;
    @FXML
    private Button refreshButton2;
    @FXML
    private TextField tempSpinner3;
    @FXML
    private TextField redLightSpinner3;
    @FXML
    private TextField blueLightSpinner3;
    @FXML
    private TextField levelOfMoistSpinner3;
    @FXML
    private Button getGreenhouseDataButton3;
    @FXML
    private Pane GreenhouseData3;
    @FXML
    private TextField Temp_indside3;
    @FXML
    private TextField Temp_outside3;
    @FXML
    private TextField Level_of_moist3;
    @FXML
    private TextField Water_level3;
    @FXML
    private TextField Hight_of_plants3;
    @FXML
    private Button refreshButton3;

    public FXMLDocumentController() throws RemoteException {

    }
    private PLCConnection con;

    private IGreenhouse api;

    @FXML

    private void handleButtonAction(ActionEvent event) throws RemoteException {

        System.out.println("You clicked me!");

        try {
            Temp_indside.setText(String.valueOf(api.ReadTemp1()));
            Temp_outside.setText(String.valueOf(api.ReadTemp2()));
            Level_of_moist.setText(String.valueOf(api.ReadMoist()));
            Water_level.setText(String.valueOf(api.ReadWaterLevel()));
            Hight_of_plants.setText(String.valueOf(api.ReadPlantHeight()));
        } catch (RemoteException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private int i = 0;

    private void update() throws InterruptedException {
        while (true) {
            try {

                System.out.println("2");
                Temp_indside.setText(String.valueOf(api.ReadTemp1() + i));
//                Temp_outside.setText(String.valueOf(api.ReadTemp2()));
//                Level_of_moist.setText(String.valueOf(api.ReadMoist()));
//                Water_level.setText(String.valueOf(api.ReadWaterLevel()));
//                Hight_of_plants.setText(String.valueOf(api.ReadPlantHeight()));
                i++;
            } catch (RemoteException ex) {
                Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            }
            Thread.sleep(5000);
        }
    }

    private void startUpdateThread() throws InvocationTargetException, InterruptedException {

        System.out.println("1");
        Thread t = new Thread(() -> {
            try {
                update();
            } catch (InterruptedException ex) {
                Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        t.start();

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
//
//        try {
//            startUpdateThread();
//        } catch (InvocationTargetException ex) {
//            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (InterruptedException ex) {
//            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
//        }

    }

    @FXML
    private void getGreenhouseData(ActionEvent event) throws RemoteException, InvocationTargetException, InterruptedException {
        System.out.println(IP.get(0));
        con = new UDPConnection(5000, (String) IP.get(0));
        api = new Greenhouse(con);
        startUpdateThread();
    }

}
