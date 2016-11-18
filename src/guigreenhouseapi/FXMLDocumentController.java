/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package guigreenhouseapi;

import GreenhouseAPI.IGreenhouse;
import PLCCommunication.PLCConnection;
import SCADA.ISCADA;
import SCADA.SCADA;

import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import static javafx.collections.FXCollections.observableArrayList;
import static javafx.collections.FXCollections.observableList;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.input.DragEvent;
import javafx.scene.input.InputMethodEvent;
import static javafx.collections.FXCollections.observableArrayList;
import static javafx.collections.FXCollections.observableList;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import static javafx.scene.paint.Color.color;
import javafx.scene.shape.Circle;

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
        for (Map.Entry<String, IGreenhouse> gh : SCADA.getInstance().getGreenhouseList().entrySet()) {
            l1.add(gh.getKey());
            l2.add(scada.getGreenhouse(gh.getKey()).getDaysRemaining());
        }
        listOfGreenhouses.setItems((ObservableList<String>) l1);
        greenhouseStatus.setItems((ObservableList< String>) l2);

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        try {
            scada = SCADA.getInstance();
            scada.startServer();
            List l = FXCollections.observableArrayList();

            for (Map.Entry<String, IGreenhouse> gh : SCADA.getInstance().getGreenhouseList().entrySet()) {
                System.out.println(gh.getKey());
                l.add(gh.getKey());
            }
            listOfGreenhouse.setItems((ObservableList<String>) l);
            updateOverview();
        } catch (RemoteException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    private void getGreenhouseData(ActionEvent event) throws RemoteException, InvocationTargetException, InterruptedException {

        gh = scada.getGreenhouse(listOfGreenhouse.getValue());

        temp1 = gh.ReadTemp1() - 273;
        tempInside.setText(String.valueOf(temp1));
        thermometerIndicatorIn.setProgress((temp1) / 50.0);
        temp2 = gh.ReadTemp2() - 273;
        tempOutside.setText(String.valueOf(temp2));
        thermometerIndicatorOut.setProgress(temp2 / 50.0);
        waterLevelValue = gh.ReadWaterLevel() / 10;
        waterLevel.setText(String.valueOf(waterLevelValue));
        waterlevelIndicator.setProgress(waterLevelValue / 25.0);
        lightIndicator.setProgress(50 / 100.0);
//        gh.SetRedLight(56);
//        gh.SetBlueLight(50);
//        disableCheckAndButton();
        lightSlider.setValue(gh.getBlueLight());
        amountOfLghtSlider.setValue(gh.getLightIntensity());
        gh.setDays(50);
        gh.setDaysCompleted(6);
        updateOverview();
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

}
