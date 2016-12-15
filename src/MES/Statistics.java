package MES;/**
 * Created by madsn on 14-12-2016.
 */

import GreenhouseAPI.Greenhouse;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.chart.LineChart;
import org.jfree.*;

import SCADA.SQLConnection;
import javafx.application.Application;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class Statistics extends Application {

    public TableView<Batch> batchTable;
    public Tab logTab;
    public TableView<Log> logTable;
    public Label LogLabelBatch;
    public Label LogLabelGreenhouse;
    public Label LogLabelDate;
    public Button showLogButton;
    public Tab statisticsTab;
    public LineChart<Integer, Integer> statisticGraph;
    private ObservableList<Log> logList = FXCollections.observableArrayList();
    private ObservableList<Batch> batchList = FXCollections.observableArrayList();
    private List<TableColumn> columns = new ArrayList<>();
    private Batch selectedBatch;

    public static void main(String[] args) {
        launch(args);
    }

    public static class Log{

        private SimpleIntegerProperty RedLight;
        private SimpleIntegerProperty BlueLight;
        private SimpleIntegerProperty LightIntensity;
        private SimpleIntegerProperty InsideTemp;
        private SimpleIntegerProperty OutsideTemp;
        private SimpleIntegerProperty WaterLevel;
        private SimpleIntegerProperty FanRunning;

        public Log(int redLight, int blueLight, int lightIntensity, int insideTemp, int outsideTemp, int waterLevel, int fanRunning) {
            RedLight = new SimpleIntegerProperty(redLight);
            BlueLight = new SimpleIntegerProperty(blueLight);
            LightIntensity = new SimpleIntegerProperty(lightIntensity);
            InsideTemp = new SimpleIntegerProperty(insideTemp);
            OutsideTemp = new SimpleIntegerProperty(outsideTemp);
            WaterLevel = new SimpleIntegerProperty(waterLevel);
            FanRunning = new SimpleIntegerProperty(fanRunning);
        }

        public Integer getRedLight() {
            return RedLight.get();
        }

        public Integer getBlueLight() {
            return BlueLight.get();
        }

        public Integer getLightIntensity() {
            return LightIntensity.get();
        }

        public Integer getInsideTemp() {
            return InsideTemp.get();
        }

        public Integer getOutsideTemp() {
            return OutsideTemp.get();
        }

        public Integer getWaterLevel() {
            return WaterLevel.get();
        }

        public Integer getFanRunning() {
            return FanRunning.get();
        }

    }

    public static class Batch {
        private SimpleIntegerProperty Id;
        private SimpleIntegerProperty Product;
        private SimpleStringProperty Greenhouse;
        private SimpleStringProperty TimeStamp;

        public Batch(int id, int product, String greenhouse, String timeStamp){
            Id = new SimpleIntegerProperty(id);
            Product = new SimpleIntegerProperty(product);
            Greenhouse = new SimpleStringProperty(greenhouse);
            TimeStamp = new SimpleStringProperty(timeStamp);
        }

        public int getId() {
            return Id.get();
        }

        public int getProduct() {
            return Product.get();
        }

        public String getGreenhouse() {
            return Greenhouse.get();
        }

        public String getTimeStamp() {
            return TimeStamp.get();
        }
    }

    @Override
    public void start(Stage stage) throws IOException, SQLException {
        Parent root = FXMLLoader.load(getClass().getResource("Statistics.fxml"));
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();



    }

    public void generateStatistics(ActionEvent actionEvent) {
        statisticsTab.setDisable(false);
        statisticsTab.getTabPane().getSelectionModel().select(statisticsTab);

        // InsideTemperature
        Number[] insideTemps = new Number[logList.size()];
        List<Number> insideTempsList = new ArrayList<>();
        for (int i = 0; i < logList.size(); i++){
            insideTemps[i] = (logList.get(i).getInsideTemp());
            insideTempsList.add(logList.get(i).getInsideTemp());
        }
        double s = org.jfree.data.statistics.Statistics.getStdDev(insideTemps);

        double m = org.jfree.data.statistics.Statistics.calculateMedian(insideTempsList);

        double q1 = org.jfree.data.statistics.BoxAndWhiskerCalculator.calculateQ1(insideTempsList);

        double q3 = org.jfree.data.statistics.BoxAndWhiskerCalculator.calculateQ3(insideTempsList);

        double iqr = q3-q1;

        double aMinus = q1 - 1.5 * iqr;
        double aPlus = q1 + 1.5 * iqr;






        // OutsideTemperature
        Number[] outsideTemps = new Number[logList.size()];
        for (int i = 0; i < logList.size(); i++){
            outsideTemps[i] = (logList.get(i).getOutsideTemp());
        }
        //double s = org.jfree.data.statistics.Statistics.getStdDev(outsideTemps);


        statisticGraph.setTitle("Batch ID: " + selectedBatch.getId() + " [" + selectedBatch.getGreenhouse() + "]");





    }

    public void getBatches(ActionEvent actionEvent) throws SQLException {
        showLogButton.setDisable(false);

        batchList.clear();
        columns.clear();
        batchTable.getItems().clear();
        batchTable.getColumns().clear();

        ResultSet rs = SCADA.SQLConnection.execute("SELECT * FROM batchlog");
        ResultSetMetaData rsmd = rs.getMetaData();
        for (int i = 1; i <= rsmd.getColumnCount(); i++){
            TableColumn tc = new TableColumn(rsmd.getColumnName(i));
            tc.setCellValueFactory(
                    new PropertyValueFactory<Log, Integer>(rsmd.getColumnName(i)));
            columns.add(tc);
        }

        while (rs.next()){
        batchList.add(new Batch(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getString(4)));
    }
        batchTable.setItems(batchList);
        for (TableColumn t : columns){

            batchTable.getColumns().add(t);

        }
    }

    public void showLog(ActionEvent actionEvent) throws Exception {
        if (batchTable.getSelectionModel().getSelectedIndex() == -1){
            return;
        }

        logTab.setDisable(false);
        logTab.getTabPane().getSelectionModel().select(logTab);

        columns.clear();
        logList.clear();
        logTable.getItems().clear();
        logTable.getColumns().clear();

        selectedBatch = batchTable.getSelectionModel().getSelectedItem();
        int id = selectedBatch.getId();
        String greenhouse = selectedBatch.getGreenhouse();
        String timestamp = selectedBatch.getTimeStamp();

        LogLabelGreenhouse.setText(greenhouse);
        LogLabelDate.setText(timestamp);
        LogLabelBatch.setText(String.valueOf(id));

        ResultSet rs = SQLConnection.execute("SELECT * FROM `" + greenhouse + "` WHERE Batch = " + id);
        ResultSetMetaData rsmd = rs.getMetaData();
        for (int i = 3; i < rsmd.getColumnCount(); i++){
            TableColumn tc = new TableColumn(rsmd.getColumnName(i));
            tc.setCellValueFactory(
                    new PropertyValueFactory<Log, Integer>(rsmd.getColumnName(i)));
            columns.add(tc);
        }

        while (rs.next()){
            logList.add(new Log(rs.getInt(3), rs.getInt(4), rs.getInt(5), rs.getInt(6), rs.getInt(7), rs.getInt(8), rs.getInt(9)));
            System.out.println(rs.getInt(3));
        }

        logTable.setItems(logList);
        for (TableColumn t : columns){

            logTable.getColumns().add(t);

        }





        

    }
}
