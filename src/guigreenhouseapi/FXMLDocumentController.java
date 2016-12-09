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
import java.util.Date;
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
import javafx.scene.input.KeyEvent;
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
    private TextField Water_level;
    @FXML
    private ComboBox<String> listOfGreenhouse;
    @FXML
    private TextField tempInside;
    @FXML
    private ProgressBar waterlevelIndicator;
    @FXML
    private ProgressBar thermometerIndicatorIn;
    @FXML
    private ProgressBar thermometerIndicatorOut;
    @FXML
    private TextField tempOutside;
    @FXML
    private Button stopProductionButton;
    @FXML
    private CheckBox stopProductionCheckBox;
    @FXML
    private Slider lightSlider;
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
    @FXML
    private TextField daysLeftTextField;
    @FXML
    private TextField timerTextField;
    @FXML
    private ComboBox<String> listOfGreenhouse3;
    @FXML
    private TextField Order_TextField;
    @FXML
    private TextField Quantity_TextField;
    @FXML
    private TextField Temp_TextField;
    @FXML
    private TextField MaxTemp_TextField;
    @FXML
    private TextField MinTemp_TextField;
    @FXML
    private TextField BlueLight_TextField;
    @FXML
    private TextField RedLight_TextField;
    @FXML
    private TextField IrrDay_TextField;
    @FXML
    private TextField WaterTime_TextField;
    @FXML
    private TextField HoursDay_TextField;
    @FXML
    private TextField Days_TextField;
    
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
            
            if (scada.getGreenhouse(gh.getKey()).getOrder() != null) {
                Date d = new Date();
                
                l3.add(scada.getGreenhouse(gh.getKey()).getOrder().getName());

                if (gh.getValue().getOrder().getRecipe().getDays() - gh.getValue().getOrder().getSecondsElapsed() / 3600 / 24 <= 0) {
                    l2.add("Complete");
                } else {
                    l2.add(gh.getValue().getOrder().getRecipe().getDays() - gh.getValue().getOrder().getSecondsElapsed() / 3600 / 24);
                    
                }
            } else {
                l2.add("None");
                ArrayList<Order> orders = SCADA.getInstance().getOrders();
                
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
            listOfGreenhouse3.setItems((ObservableList<String>) l);
            
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
        }, 1000, 1000);
        
        Timer fan = new java.util.Timer();
        
        fan.schedule(new TimerTask() {
            public void run() {
                Platform.runLater(new Runnable() {
                    public void run() {
//                        System.out.println("hello");
                        try {
                            if (gh.getFanspeed() != 0) {
                                
                                fanImg.setRotate(fanImg.getRotate() + 15);
                            }
                        } catch (Exception e) {
                        }
                    }
                });
            }
        }, 200, 200);
        
    }
    
    @FXML
    private void getGreenhouseData(ActionEvent event) throws RemoteException, InvocationTargetException, InterruptedException {
        
        gh = scada.getGreenhouse(listOfGreenhouse.getValue());
        lightSlider.setValue(gh.getOrder().getRecipe().getBlueLight());
//
//        lightSlider.setValue(gh.getBlueLight());
//        amountOfLghtSlider.setValue(gh.getLightIntensity());
//        lightIndicator.setProgress(50 / 100.0);
////        gh.SetRedLight(56);
////        gh.SetBlueLight(50);
////        disableCheckAndButton();
//        gh.setDays(50);a
//        gh.setDaysCompleted(6);
        updateOverview();
        updateLight();
        
    }
    
    @FXML
    private void stopProduction(ActionEvent event) throws RemoteException {
        gh = scada.getGreenhouse(listOfGreenhouse.getValue());
        gh.SetRedLight(0);
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
        amountOfLghtProgress.progressProperty().set(gh.getLightIntensity() / 100.0);
        
        gh.getOrder().getRecipe().setBlueLight((int) lightSlider.getValue());
        gh.getOrder().getRecipe().setRedLight(100 - gh.getOrder().getRecipe().getBlueLight());
//        gh.setLightIntensity((int) gh.getLightIntensity());
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
        lightSlider.setValue(o.getRecipe().getBlueLight());
//        amountOfLghtSlider.setValue(o.getRecipe().getLightIntensity());
        gh.SetTemperature(o.getRecipe().getTemp());
        System.out.println("Temp:  " + o.getRecipe().getTemp());
//        gh.setDays(o.getRecipe().getDays());
        System.out.println(gh.SetTemperature(o.getRecipe().getTemp()));
        System.out.println(gh.SetBlueLight(o.getRecipe().getBlueLight()));
//        
        gh.SetRedLight(o.getRecipe().getRedLight());
//        gh.setLightIntensity(o.getRecipe().getLightIntensity());
        Date d = new Date();
        gh.getOrder().setStartDate(d);
        updateOverview();
        System.out.println(gh.getOrder().getSecondsElapsed());
        
    }
    
    private void updateValues() throws RemoteException {
        
        try {
            
            try {
                temp1 = gh.ReadTemp1();
                temp1 = (double) Math.round(temp1 * 100.0) / 100.0;
                
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
        levelOfMoistLabel.setText(String.valueOf(gh.ReadMoist()) + "%");
        
        waterLevelValue = gh.ReadWaterLevel() / 10;
        waterLevel.setText(String.valueOf(waterLevelValue));
        waterlevelIndicator.setProgress(waterLevelValue / 25.0);

        if (gh.getOrder().getRecipe().getDays() - gh.getOrder().getSecondsElapsed() / 3600 / 24 <= 0) {
            daysLeftTextField.setText("Complete");
            timerTextField.setText("00:00");
            updateOverview();

        } else {
            daysLeftTextField.setText(String.valueOf(gh.getOrder().getRecipe().getDays() - gh.getOrder().getSecondsElapsed() / 3600 / 24));
            timerTextField.setText(String.format("%02d", (int) Math.floor(gh.getOrder().getSecondsElapsed() / 3600) % 24) + ":" + String.format("%02d", (int) Math.floor(gh.getOrder().getSecondsElapsed() / 60 % 60)));
        }

        
        updateLight();
        
    }
    
    @FXML
    private void changeRecipe() throws RemoteException {
        gh = scada.getGreenhouse(listOfGreenhouse3.getValue());
        Order_TextField.setText(gh.getOrder().getName());
        Quantity_TextField.setText(String.valueOf(gh.getOrder().getQuantity()));
        Temp_TextField.setText(String.valueOf(gh.getOrder().getRecipe().getTemp()));
        MaxTemp_TextField.setText(String.valueOf(gh.getOrder().getRecipe().getMaxTemp()));
        MinTemp_TextField.setText(String.valueOf(gh.getOrder().getRecipe().getMinTemp()));
        BlueLight_TextField.setText(String.valueOf(gh.getOrder().getRecipe().getBlueLight()));
        RedLight_TextField.setText(String.valueOf(gh.getOrder().getRecipe().getRedLight()));
        IrrDay_TextField.setText(String.valueOf(gh.getOrder().getRecipe().getIrrigationsPrDay()));
        WaterTime_TextField.setText(String.valueOf(gh.getOrder().getRecipe().getWaterTime()));
        HoursDay_TextField.setText(String.valueOf(String.valueOf(gh.getOrder().getRecipe().getHoursDay())));
        Days_TextField.setText(String.valueOf(gh.getOrder().getRecipe().getDays()));
    }

    @FXML
    private void changeButton(ActionEvent event) throws RemoteException {
        gh = scada.getGreenhouse(listOfGreenhouse3.getValue());
        gh.getOrder().getRecipe().setBlueLight(Integer.parseInt(BlueLight_TextField.getText()));
        gh.getOrder().getRecipe().setRedLight(Integer.parseInt(RedLight_TextField.getText()));

//        lightSlider.setValue(Integer.parseInt(BlueLight_TextField.getText()));
        gh.getOrder().getRecipe().setMaxTemp(Integer.parseInt(MaxTemp_TextField.getText()));
        gh.getOrder().getRecipe().setMinTemp(Integer.parseInt(MinTemp_TextField.getText()));
        gh.getOrder().getRecipe().setHoursDay(Double.parseDouble(HoursDay_TextField.getText()));
        gh.getOrder().getRecipe().setDays(Integer.parseInt(Days_TextField.getText()));
        gh.getOrder().getRecipe().setIrrigationsPrDay(Double.parseDouble(IrrDay_TextField.getText()));
        gh.getOrder().getRecipe().setWaterTime(Integer.parseInt(WaterTime_TextField.getText()));
        gh.getOrder().setQuantity(Integer.parseInt(Quantity_TextField.getText()));
        gh.getOrder().getRecipe().setTemp(Integer.parseInt(Temp_TextField.getText()));

    }

    @FXML
    private void checkLightValue(KeyEvent event) {
 if(event.getSource().toString().toLowerCase().contains("bluelight_textfield")){
           int value = Integer.parseInt(BlueLight_TextField.getText());
                RedLight_TextField.setText(String.valueOf(100 - value)); 
        }else if(event.getSource().toString().toLowerCase().contains("redlight_textfield")){
            int value = Integer.parseInt(RedLight_TextField.getText());
                BlueLight_TextField.setText(String.valueOf(100 - value));
        }  
    }

    
       
  
    
}
