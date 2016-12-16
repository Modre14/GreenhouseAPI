package MES;/**
 * Created by madsn on 14-12-2016.
 */

import GreenhouseAPI.Greenhouse;
import com.mysql.jdbc.exceptions.MySQLSyntaxErrorException;
import com.sun.org.apache.bcel.internal.generic.ExceptionThrower;
import com.sun.org.apache.bcel.internal.generic.SIPUSH;
import javafx.beans.property.SimpleDoubleProperty;
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
import java.util.*;

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
    public TableView statisticTable;
    private ObservableList<Log> logList = FXCollections.observableArrayList();
    private ObservableList<Batch> batchList = FXCollections.observableArrayList();
    private ObservableList<Statistic> statisticList = FXCollections.observableArrayList();
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
        private SimpleIntegerProperty Moisture;
        private SimpleIntegerProperty FanRunning;

        public Log(int redLight, int blueLight, int lightIntensity, int insideTemp, int outsideTemp, int waterLevel, int moisture, int fanRunning) {
            RedLight = new SimpleIntegerProperty(redLight);
            BlueLight = new SimpleIntegerProperty(blueLight);
            LightIntensity = new SimpleIntegerProperty(lightIntensity);
            InsideTemp = new SimpleIntegerProperty(insideTemp);
            OutsideTemp = new SimpleIntegerProperty(outsideTemp);
            WaterLevel = new SimpleIntegerProperty(waterLevel);
            Moisture = new SimpleIntegerProperty(moisture);
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

        public Integer getMoisture() { return Moisture.get(); }

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

    public static class Statistic{
        private SimpleStringProperty Name;
        private SimpleDoubleProperty stdDev;
        private SimpleDoubleProperty Mean;
        private SimpleDoubleProperty Median;
        private SimpleDoubleProperty Q1;
        private SimpleDoubleProperty Q3;
        private SimpleDoubleProperty IQR;
        private SimpleDoubleProperty Aminus;
        private SimpleDoubleProperty Aplus;

        public Statistic(String name){
            Name = new SimpleStringProperty(name);
        }

        public void setValues(double[] values){
            this.stdDev = new SimpleDoubleProperty(values[0]);
            this.Mean = new SimpleDoubleProperty(values[1]);
            this.Median = new SimpleDoubleProperty(values[2]);
            this.Q1 = new SimpleDoubleProperty(values[3]);
            this.Q3 = new SimpleDoubleProperty(values[4]);
            this.IQR = new SimpleDoubleProperty(values[5]);
            this.Aminus = new SimpleDoubleProperty(values[6]);
            this.Aplus = new SimpleDoubleProperty(values[7]);
        }

        public double[] getValues(){
            double[] returnList = new double[8];
            returnList[0] = getStdDev();
            returnList[1] = getMean();
            returnList[2] = getMedian();
            returnList[3] = getQ1();
            returnList[4] = getQ3();
            returnList[5] = getIQR();
            returnList[6] = getAminus();
            returnList[7] = getAplus();

            return returnList;
        }

        public String getName() {
            return Name.get();
        }


        public double getStdDev() {
            return stdDev.get();
        }


        public double getMedian() {
            return Median.get();
        }


        public double getMean() {
            return Mean.get();
        }


        public double getQ1() {
            return Q1.get();
        }


        public double getQ3() {
            return Q3.get();
        }


        public double getIQR() {
            return IQR.get();
        }


        public double getAminus() {
            return Aminus.get();
        }

        public double getAplus() {
            return Aplus.get();
        }

    }

    @Override
    public void start(Stage stage) throws IOException, SQLException {
        Parent root = FXMLLoader.load(getClass().getResource("Statistics.fxml"));
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();



    }

    public void generateStatistics(ActionEvent actionEvent) throws SQLException {
        statisticsTab.setDisable(false);
        statisticsTab.getTabPane().getSelectionModel().select(statisticsTab);

        columns.clear();
        statisticList.clear();
        statisticTable.getColumns().clear();
        statisticTable.getItems().clear();

        Statistic insideTempStatistic = new Statistic("InsideTemp");
        Statistic outsideTempStatistic = new Statistic("OutsideTemp");
        Statistic waterLevelStatistic = new Statistic("WaterLevel");
        Statistic moistureStatistic = new Statistic("Moisture");

        //Inside Temperature
        Number[] values = new Number[logList.size()];
        for (int i = 0; i < logList.size(); i++){
            values[i] = (logList.get(i).getInsideTemp());
        }
        insideTempStatistic.setValues(doTheMath(values));
        statisticList.add(insideTempStatistic);

        //OutsideTemperature
        for (int i = 0; i < logList.size(); i++){
            values[i] = (logList.get(i).getOutsideTemp());
        }
        outsideTempStatistic.setValues(doTheMath(values));
        statisticList.add(outsideTempStatistic);

        //WaterLevel
        for (int i = 0; i < logList.size(); i++){
            values[i] = (logList.get(i).getWaterLevel());
        }
        waterLevelStatistic.setValues(doTheMath(values));
        statisticList.add(waterLevelStatistic);

        //Moisture
        for (int i = 0; i < logList.size(); i++){
            values[i] = (logList.get(i).getMoisture());
        }
        moistureStatistic.setValues(doTheMath(values));
        statisticList.add(moistureStatistic);

        String statisticnName = selectedBatch.getId() + "x" + selectedBatch.getGreenhouse();

        String query = "CREATE TABLE `" + statisticnName + "`" +
                " (" +
                " Reading varchar(255)," +
                " StdDev real," +
                " Mean real," +
                " Median real," +
                " Q1 real," +
                " Q3 real," +
                " IQR real," +
                " Aminus real," +
                " Aplus real" +
                ");";

        try{
            SQLConnection.execute(query, "greenhousestatistics");
            SQLConnection.execute("DELETE FROM `" + statisticnName + "`", "greenhousestatistics");

                for (Statistic s: statisticList) {
                    query = "INSERT INTO `" + statisticnName + "` Values ('" + s.getName() + "'";
                    for (double value : s.getValues()){
                        query += " , '" + value + "'";
                    }
                    query += ");";
                    SQLConnection.execute(query, "greenhousestatistics");
                }
                SQLConnection.close();


        } catch (Exception e) {
            System.out.println(e);
        }

        ResultSet rs = SQLConnection.execute("SELECT * FROM `" + statisticnName + "`", "greenhousestatistics");
        ResultSetMetaData rsmd = rs.getMetaData();

        TableColumn tc = new TableColumn("Reading");
        tc.setCellValueFactory(
                new PropertyValueFactory<Statistic, String>("Name"));
        columns.add(tc);

        for (int i = 1; i < rsmd.getColumnCount(); i++){
            tc = new TableColumn(rsmd.getColumnName(i+1));
            tc.setCellValueFactory(
                    new PropertyValueFactory<Statistic, Integer>(rsmd.getColumnName(i+1)));
            columns.add(tc);
        }

        SQLConnection.close();

        statisticTable.setItems(statisticList);
        for (TableColumn t : columns){

            statisticTable.getColumns().add(t);

        }
    }

    public double[] doTheMath(Number[] values){
        Arrays.sort(values);
        List<Number> valueList = new ArrayList<>();
        for (Number n : values){
            valueList.add(n);
        }

        double[] returnList = new double[8];

        double s = org.jfree.data.statistics.Statistics.getStdDev(values);
        returnList[0] = s;
        double m = org.jfree.data.statistics.Statistics.calculateMedian(valueList);
        returnList[1] = m;
        double mean = org.jfree.data.statistics.Statistics.calculateMean(values);
        returnList[2] = mean;
        double q1 = org.jfree.data.statistics.BoxAndWhiskerCalculator.calculateQ1(valueList);
        returnList[3] = q1;
        double q3 = org.jfree.data.statistics.BoxAndWhiskerCalculator.calculateQ3(valueList);
        returnList[4] = q3;
        double iqr = q3-q1;
        returnList[5] = iqr;
        double aMinus = q1 - 1.5 * iqr;
        returnList[6] = aMinus;
        double aPlus = q1 + 1.5 * iqr;
        returnList[7] = aPlus;

        return returnList;
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
        SQLConnection.close();
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
            logList.add(new Log(rs.getInt(3), rs.getInt(4), rs.getInt(5), rs.getInt(6), rs.getInt(7), rs.getInt(8), rs.getInt(9), rs.getInt(10)));
            System.out.println(rs.getInt(3));
        }

        logTable.setItems(logList);
        for (TableColumn t : columns){

            logTable.getColumns().add(t);

        }
        SQLConnection.close();
    }
}
