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
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

/**
 *
 * @author Morten
 */
public class FXMLDocumentController extends Thread implements Initializable {

    @FXML
    private Button button;
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

    public FXMLDocumentController() throws RemoteException {
    }
    private PLCConnection con = new UDPConnection(5000, "192.168.0.10");
    private IGreenhouse api = new Greenhouse(con);

    @FXML
    private Label label;

    @FXML
    private void handleButtonAction(ActionEvent event) throws RemoteException {

        System.out.println("You clicked me!");
        label.setText("Hello World!");

        Temp_indside.setText(String.valueOf(api.ReadTemp1()));
        Temp_outside.setText(String.valueOf(api.ReadTemp2()));
        Level_of_moist.setText(String.valueOf(api.ReadMoist()));
        Water_level.setText(String.valueOf(api.ReadWaterLevel()));
        Hight_of_plants.setText(String.valueOf(api.ReadPlantHeight()));

    }

    private void update() throws InvocationTargetException, InterruptedException{ 


        System.out.println("1");
        Thread t = new Thread(() -> {
            try {
                System.out.println("2");
                Temp_indside.setText(String.valueOf(api.ReadTemp1()));
                Temp_outside.setText(String.valueOf(api.ReadTemp2()));
                Level_of_moist.setText(String.valueOf(api.ReadMoist()));
                Water_level.setText(String.valueOf(api.ReadWaterLevel()));
                Hight_of_plants.setText(String.valueOf(api.ReadPlantHeight()));
            } catch (RemoteException ex) {
                Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            }

        });
        t.start();
        Thread.sleep(1000);
        
    }

    @Override
    public void initialize(URL url, ResourceBundle rb
    ) {
        try {
            // TODO
            update();

        } catch (InvocationTargetException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
