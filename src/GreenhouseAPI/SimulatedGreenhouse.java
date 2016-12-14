/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GreenhouseAPI;

import PLCCommunication.ICommands;
import PLCCommunication.Message;
import PLCCommunication.PLCConnection;
import PLCCommunication.UDPConnection;
import Protocol.Order;
import SCADA.SQLConnection;

import javax.xml.transform.Result;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.sql.*;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import static jdk.nashorn.internal.objects.NativeError.printStackTrace;

/**
 *
 * @author Morten
 */
public class SimulatedGreenhouse implements IGreenhouse, ICommands, Serializable {

    private int maxValue = 30;

    private double currentValue;
    private String conn;
    private Message mess;
    private int blueLight;
    private double lightIntensity;
    private int days;
    private int daysCompleted;
    private Order order;
    private double water;
    private int moist;
    private int lastLog = 0;
    String IP;

    double temp = 15.0;
    double temp2 = 15.0;
    int fanSpeed = 0;

    /**
     * Create greenhouse API
     *
     * @param IP IP
     */
    public SimulatedGreenhouse(String IP) throws RemoteException {
        this.conn = IP;
        this.IP = IP;

    }

    public boolean SetTemperature(int kelvin) {

        new Thread(() -> {
            Random generator = new Random();
            while (true) {

                double valD = 0;
                int r = generator.nextInt(2) + 1;

                int neg = generator.nextInt(2);

                if (temp > (kelvin + 4)) {

                    fanSpeed = 2;
                } else if (temp > kelvin) {
                    fanSpeed = 1;
                } else {
                    fanSpeed = 0;
                }

                switch (neg) {
                    case 0:
                        valD = r / 5.0;
                        if (water > 0) {
                            water = water - 0.2;
                            System.out.println("WATER------------------------------------");
                        }else{
                            water =0;
                        }

                        break;
                    case 1:
                        valD = ((r / (10.0)) * (1 + fanSpeed)) * (-1);
                        break;

                }
                temp = temp + valD;

                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException ex) {
                    Logger.getLogger(SimulatedGreenhouse.class.getName()).log(Level.SEVERE, null, ex);
                }

//                System.out.print("temp: " + temp + "");
//                System.out.println("|   |" + neg + "    Fan speed: " + fanSpeed);
            }
        }).start();

        return false;
    }

    /**
     * Setpoint for moisture inside Greenhouse CMD:2
     *
     * @param moist in % ( 10 > M > 90 )
     * @return true if processed
     */
    public boolean SetMoisture(int moist) {
        System.out.println("Set moisture level to " + moist);

        return false;
    }

    /**
     * Setpoint for red light inside Greenhouse CMD:3
     *
     * @param level in percent
     * @return true if processed
     * @throws java.rmi.RemoteException
     */
    @Override

    public boolean SetRedLight(int level) {
//        System.out.println("Set red light to " + level);

        return false;
    }

    /**
     * Setpoint for blue light inside Greenhouse CMD: 4
     *
     * @param level in percent
     * @return true if processed
     */
    public boolean SetBlueLight(int level) {
//        System.out.println("Set blue light to " + level);

        return false;
    }

    /**
     * Add water for some seconds. Pump is stopped if height of water is
     * exceeded CMD: 6
     *
     * @param sec : Second to turn on the pump {0 <= sec < 120}
     * @return true if processed
     */
    public boolean AddWater(int sec) {
        if (sec >= 0 && sec < 120) {
            water = sec;
            System.out.println(water + " THIS IS WATER");
        }
        return false;
    }

    /**
     * NOT IMPLEMENTED Add Fertiliser for some seconds. Pump is stopped if
     * height of water is exceeded CMD: 7
     *
     * @param sec : Secord to turn on the pump
     * @return true if processed
     */
    public boolean AddFertiliser(int sec) {
        return true;
    }

    /**
     * NOT IMPLEMENTED Add CO2 for some seconds. Pump is stopped if height of
     * water is exceeded CMD: 8
     *
     * @param sec : Secord to turn on the valve
     * @return true if processed
     */
    public boolean AddCO2(int sec) {
        return true;
    }

    /**
     * Read tempature inside the Greenhouse CMD:9
     *
     * @return Temperature in kelvin
     */
    public double ReadTemp1() {
//        System.out.println("Read greenhouse temperature ");

//        System.out.println("Temperature is: " + temp + "celcius");
        return temp;
    }

    /**
     * Read tempature outside the Greenhouse CMD: 10
     *
     * @return Temperature in kelvin
     */
    public double ReadTemp2() {
//        System.out.println("Read outdoor temperature ");
//
//        System.out.println("");
//        System.out.println("Temperature is: " + temp2);
        return temp2 + 273.0;

    }

    /**
     * Read relative moisture inside the Greenhouse CMD: 11
     *
     * @return Moisture in %
     */
    public double ReadMoist() {
        double moist = 40.0;
//        System.out.println("Moisture is: " + moist + " %");
        return moist;
    }

    /**
     * Read level of water in the Greenhouse CMD: 17
     *
     * @return Level in millimeter [0 < level < 250]
     */
    public double ReadWaterLevel() {
//        System.out.println("Read water level ");

//        double level = water; // level

//        System.out.println("Water level is: " + level);
        return water;
    }

    /**
     * NOT IMPLEMENTED IN THE GREENHOUSE Read higths of the plants CMD: 12
     *
     * @return Higths (cm?)
     */
    public double ReadPlantHeight() {
        System.out.println("Read height of plants");

        double level = 0.0; // level

        System.out.println("Plant height is: " + level);
        return level;
    }

    /**
     * Read all alarms one bits pr. alarm. CMD: 13
     *
     * @return Alarms as BitSet
     */
    public BitSet ReadErrors() {
        System.out.println("Get all alarms ");

        BitSet alarms = new BitSet(32);

        System.out.println("Alarm state is: " + alarms);
        return alarms;
    }

    private BitSet fillBitSet(byte[] al) {
        BitSet alarms = new BitSet(32);
        if (true) {
            if (al != null && al.length == 4) {
                for (int i = 0; i < 4; i++) {
                    for (int b = 0; b < 8; b++) {
                        int ib = (al[i] >> b) & 0x1;
                        Boolean bit;
                        if (ib == 1) {
                            bit = true;
                        } else {
                            bit = false;
                        }
                        alarms.set(i * 8 + b, bit);
                    }
                }
            }
        }
        System.out.println("Alarms in set state: " + alarms);
        return alarms;
    }

    /**
     * Reset one alarm CMD: 14
     *
     * @param errorNum
     * @return Done
     */
    public boolean ResetError(int errorNum) {

        errorNum--;
        if (errorNum >= 0 && errorNum < 64) // 0 - 30 grader celcius
        {
            System.out.println("Reset alarm " + errorNum + 1);

        }
        return false;
    }

    /**
     * Get all values as a byte array CMD: 15
     *
     * @return All values
     */
    public byte[] GetStatus() {
        byte[] result = new byte[100];
        System.out.println("Get status ");

        System.out.println("State is: " + result);
        return result;
    }

    /**
     * Set Fane speed CMD: 16
     *
     * @param speed : {OFF (0), LOW (1), HIGH(2)};
     * @return Done
     */
    public boolean SetFanSpeed(int speed) {
//        System.out.println("Set fan speed " + speed);
        switch (speed) {
            case 0:
                fanSpeed = 0;
                break;
            case 1:
                fanSpeed = 1;
                break;
            case 2:
                fanSpeed = 2;
                break;
        }
        return false;

    }

    @Override
    public void setLightIntensity(double level) {

        if (level < 0.0) {
            level = 0.0;
            System.out.println("<100");
        } else if (level > 99.0) {
            level = 100.0;
            System.out.println("> 100");
        }
        lightIntensity = level;
    }

    @Override
    public double getLightIntensity() {
        return lightIntensity;
    }

    @Override
    public int getBlueLight() {
        return blueLight;
    }

    @Override
    public void setOrder(Order order) {
        this.order = order;
        this.lastLog = 0;
        try {
            SQLConnection.execute("INSERT INTO batchlog (Product, Greenhouse) Values('"+ getOrder().getRecipe().getId() +"', '" + this.IP + "')");
            ResultSet rs = SQLConnection.execute("SELECT LAST_INSERT_ID");
            SQLConnection.close();
            rs.next();
            this.order.setBatch(rs.getInt(1));
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public Order getOrder() {
        return order;
    }

    @Override
    public int getFanspeed() {
        return fanSpeed;
    }

    @Override

    public int getAlarm() {
        System.out.println(ReadTemp1());
        if (ReadTemp1() > getOrder().getRecipe().getMaxTemp()) {
            System.out.println("                                                                AlarmMAX");
            return Alarm.MAXTEMP;
        } else if (ReadTemp1() < getOrder().getRecipe().getMinTemp()) {
            System.out.println("                                                                AlarmMIN");
            return Alarm.MINTEMP;
        }
        System.out.println("                                                                    NONE");
        return Alarm.OFF;
    }

    public int getLastLog(){
        return lastLog;
    }

    public void log(){
        lastLog++;
        String values = "'" + getOrder().getBatch() + "', '" + (100 - getBlueLight()) + "', '" + getBlueLight() + "', '" + getLightIntensity() + "', '" + ReadTemp1() + "', '" + ReadTemp2() + "', '" + ReadWaterLevel() + "', '" + getFanspeed() + "'";
        System.out.println("Information gathered!");
        SQLConnection.execute("INSERT INTO `" + IP + "` (`Batch`, `RedLight`, `BlueLight`, `LightIntensity`, `InsideTemp`, `OutsideTemp`, `WaterLevel`, `FanRunning`) Values(" + values + ")");
        SQLConnection.close();
        System.out.println("Logging sucessful!");

    }

}
