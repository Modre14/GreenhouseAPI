/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package guigreenhouseapi;

import GreenhouseAPI.IGreenhouse;
import PLCCommunication.PLCConnection;
import Protocol.Order;
import SCADA.ISCADA;
import SCADA.ISCADAHMI;
import SCADA.SCADA;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 *
 * @author Morten
 */
public class FXMLDocumentController extends Thread implements Initializable {

    public ListView listOfOrders;
    public ComboBox<String> listOfGreenhouse2;
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
    private ComboBox<String> listOfGreenhouse;
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
    private Button stopProductionButton;
    @FXML
    private CheckBox stopProductionCheckBox;
    @FXML
    private Slider lightSlider;
    @FXML
    private Slider amountOfLghtSlider;
    @FXML
    private Circle colorIndicator;
    @FXML
    private ListView<String> listOfGreenhouses;
    @FXML
    private ListView<String> greenhouseStatus;
    @FXML
    private ListView<String> greenhouseOrders;
    @FXML
    private ImageView fanImg;
    @FXML
    private ProgressBar amountOfLghtProgress;
    @FXML
    private Label levelOfMoistLabel;

    public FXMLDocumentController() throws RemoteException {

    }
    private PLCConnection con;

    private IGreenhouse gh;
    private ISCADA scada;

    private ArrayList<IGreenhouse> greenhouseArray;

    private void updateOverview() throws RemoteException {
        List l1 = FXCollections.observableArrayList();
        List l2 = FXCollections.observableArrayList();
        List l3 = FXCollections.observableArrayList();
        List l4 = FXCollections.observableArrayList();
        for (Map.Entry<String, IGreenhouse> gh : SCADA.getInstance().getGreenhouseList().entrySet()) {
            l1.add(gh.getKey());
            l2.add(scada.getGreenhouse(gh.getKey()).getDaysRemaining());
            if (scada.getGreenhouse(gh.getKey()).getOrder() != null) {
                l3.add(scada.getGreenhouse(gh.getKey()).getOrder().getName());
            } else {
                l3.add("None");
            }

        }

        ArrayList<Order> orders = SCADA.getInstance().getOrders();

        listOfGreenhouses.setItems((ObservableList<String>) l1);
        greenhouseStatus.setItems((ObservableList< String>) l2);
        greenhouseOrders.setItems((ObservableList<String>) l3);

        for (Order order : orders) {
            l4.add(order.toString());
        }

        listOfOrders.setItems((ObservableList) l4);

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        try {
            scada = SCADA.getInstance();

            List l = FXCollections.observableArrayList();

            for (Map.Entry<String, IGreenhouse> gh : SCADA.getInstance().getGreenhouseList().entrySet()) {
                System.out.println(gh.getKey());
                l.add(gh.getKey());

            }

            listOfGreenhouse.setItems((ObservableList<String>) l);
            listOfGreenhouse2.setItems((ObservableList) l);

            updateOverview();

        } catch (RemoteException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            scada.startServer();
        } catch (RemoteException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
        Timer timer = new java.util.Timer();

        timer.schedule(new TimerTask() {
            public void run() {
                Platform.runLater(new Runnable() {
                    public void run() {
//                        System.out.println("hello");
                        try {
                            updateValues();
                        } catch (Exception e) {
                        }
                    }
                });
            }
        }, 5000, 5000);
    }

    @FXML
    private void getGreenhouseData(ActionEvent event) throws RemoteException, InvocationTargetException, InterruptedException {

        gh = scada.getGreenhouse(listOfGreenhouse.getValue());
        gh.SetTemperature(293);

        updateOverview();

//        waterLevelValue = gh.ReadWaterLevel() / 10;
//        waterLevel.setText(String.valueOf(waterLevelValue));
//        waterlevelIndicator.setProgress(waterLevelValue / 25.0);
//        lightIndicator.setProgress(50 / 100.0);
////        gh.SetRedLight(56);
////        gh.SetBlueLight(50);
////        disableCheckAndButton();
//        lightSlider.setValue(gh.getBlueLight());
//        amountOfLghtSlider.setValue(gh.getLightIntensity());
//        gh.setDays(50);
//        gh.setDaysCompleted(6);
    }

    @FXML
    private void stopProduction(ActionEvent event) throws RemoteException {
        gh = scada.getGreenhouse(listOfGreenhouse.getValue());
        gh.SetBlueLight(0);
        gh.SetFanSpeed(0);
        gh.SetRedLight(0);
        gh.SetMoisture(0);
        disableCheckAndButton();
        lightSlider.setValue(0);
        amountOfLghtSlider.setValue(0);
        updateLight();
    }

    @FXML
    private void stopProductionCheck(ActionEvent event) {
        if (stopProductionCheckBox.isSelected()) {
            stopProductionButton.setDisable(false);
        } else {
            stopProductionButton.setDisable(true);
        }
    }

    private void disableCheckAndButton() {
        stopProductionButton.setDisable(true);
        stopProductionCheckBox.setSelected(false);

    }

    @FXML
    private void updateLight() throws RemoteException {
        gh = scada.getGreenhouse(listOfGreenhouse.getValue());
        gh.SetRedLight((int) ((100 - lightSlider.getValue()) * amountOfLghtSlider.getValue() / 100));
        gh.SetBlueLight((int) (lightSlider.getValue() * amountOfLghtSlider.getValue() / 100));
        gh.setLightIntensity((int) amountOfLghtSlider.getValue());
        int colorRed = (int) (255 / 100 * (100 - lightSlider.getValue()));
        int colorBlue = (int) (255 / 100 * lightSlider.getValue());
        colorIndicator.setFill(Color.web("rgb(" + colorRed + ",0," + colorBlue + ")"));
    }

    @FXML
    private void getOrderList() {
        try {
            updateOverview();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void addOrderButton() throws RemoteException {
        gh = scada.getGreenhouse(listOfGreenhouse2.getValue());
        gh.setOrder((Order) scada.getOrders().get(listOfOrders.getSelectionModel().getSelectedIndex()));
        Order o = gh.getOrder();
        gh.setDays( o.getProtocol().getDays());
       
        updateOverview();
    }

    private void updateValues() {

        try {

            try {
                temp1 = gh.ReadTemp1() - 273;
                tempInside.setText(String.valueOf(temp1));
                thermometerIndicatorIn.setProgress(temp1 / 50.0);
            } catch (RemoteException ex) {
                Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            }

            temp2 = gh.ReadTemp2() - 273;
            tempOutside.setText(String.valueOf(temp2));
            thermometerIndicatorOut.setProgress(temp2 / 50.0);
        } catch (RemoteException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
