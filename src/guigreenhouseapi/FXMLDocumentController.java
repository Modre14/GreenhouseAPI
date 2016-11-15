/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package guigreenhouseapi;

import GreenhouseAPI.IGreenhouse;
import PLCCommunication.PLCConnection;
import SCADA.SCADA;

import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;

/**
 *
 * @author Morten
 */
public class FXMLDocumentController extends Thread implements Initializable {

    private double temp1;
    private double temp2;
    private double levelOfMoist;
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

    @Override
    public void initialize(URL url, ResourceBundle rb){

        Map<String, IGreenhouse> ghlist  = null;
        try {
            SCADA.getInstance();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        for (Map.Entry<String, IGreenhouse> gh : ghlist.entrySet()){
                //System.out.println(gh.getKey());
            }

       
    }

    @FXML
    private void getGreenhouseData(ActionEvent event) throws RemoteException, InvocationTargetException, InterruptedException {

//        for (int i = 0; i < scada.getGreenhouseArray().size(); i++) {
//
//            if (listOfGreenhouse.getValue().equals(scada.getGreenhouseArray().get(i))) {
//
//                api = scada.getGreenhouseArray().get(i);
//                temp1 = api.ReadTemp1() - 253;
//                tempInside.setText(String.valueOf(temp1));
//                thermometerIndicatorIn.setProgress((temp1) / 50.0);
//                temp2 = api.ReadTemp2() - 253;
//                tempOutside.setText(String.valueOf(temp2));
//                thermometerIndicatorOut.setProgress(temp2 / 50.0);
//                waterLevelValue = api.ReadWaterLevel() / 10;
//                waterLevel.setText(String.valueOf(waterLevelValue));
//                waterlevelIndicator.setProgress(waterLevelValue / 25.0);
//
//            }
//        }

    }

}
