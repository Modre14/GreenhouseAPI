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
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javax.swing.event.ChangeEvent;

/**
 *
 * @author Morten
 */
public class FXMLDocumentController extends Thread implements Initializable {

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
    private Spinner<Integer> tempSpinner;
    @FXML
    private Spinner<Integer> redLightSpinner;
    @FXML
    private Spinner<Integer> blueLightSpinner;
    @FXML
    private Spinner<Integer> levelOfMoistSpinner;
    @FXML
    private Button addWaterButton;
    @FXML
    private Button setValuesButton;

    public FXMLDocumentController() throws RemoteException {
    }
    private PLCConnection con = new UDPConnection(5000, "192.168.0.10");
    private IGreenhouse api = new Greenhouse(con);

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

    private void update() throws InterruptedException {
        int i = 1;
        while (true) {
            try {

                System.out.println("2");
                Temp_indside.setText(String.valueOf(api.ReadTemp1() + i));
                Temp_outside.setText(String.valueOf(api.ReadTemp2()));
                Level_of_moist.setText(String.valueOf(api.ReadMoist()));
                Water_level.setText(String.valueOf(api.ReadWaterLevel()));
                Hight_of_plants.setText(String.valueOf(api.ReadPlantHeight()));
                i++;
            } catch (RemoteException ex) {
                Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            }
            Thread.sleep(100000);
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
    private void setValues(ActionEvent event) throws RemoteException {
        api.SetTemperature(tempSpinner.getValue()+273);
        api.SetRedLight(redLightSpinner.getValue());
        api.SetBlueLight(blueLightSpinner.getValue());
        api.SetMoisture(levelOfMoistSpinner.getValue());
    }

}
