/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GreenhouseAPI;

import PLCCommunication.*;
import Recipe.Order;
import SCADA.SQLConnection;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;
import java.util.BitSet;

/**
 * API to communicate to the PLC
 *
 * @author Steffen Skov
 */
public class Greenhouse extends UnicastRemoteObject implements IGreenhouse, ICommands, Serializable {

    private PLCConnection conn;
    private Message mess;
    private int blueLight;
    private double lightIntensity;
    private int days;
    private String IP;
    private int lastLog = 0;
    private Alarm alarm;
    private int lastIrrigation;

    public int getAlarm() {
        int currentTemp = (int) ReadTemp1();
        if (currentTemp > getOrder().getRecipe().getMaxTemp()) {
            return Alarm.MAXTEMP;
        } else if (currentTemp < getOrder().getRecipe().getMinTemp()) {
            return Alarm.MINTEMP;
        }
        return Alarm.OFF;
    }

    private Order order;
    int fanSpeed = 0;

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
        this.lastLog = 0;
        this.lastIrrigation = 0;
        try {
            SQLConnection.execute("INSERT INTO batchlog (Product, Greenhouse) Values('" + getOrder().getRecipe().getId() + "', '" + this.IP + "')");
            ResultSet rs = SQLConnection.execute("SELECT LAST_INSERT_ID()");
            rs.next();
            this.order.setBatch(rs.getInt(1));
            SQLConnection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * Create greenhouse API
     *
     * @param IP IP connection
     */
    public Greenhouse(String IP) throws RemoteException {
        this.IP = IP;
        this.conn = new UDPConnection(5000, IP);

    }

    /**
     * Setpoint for temperature inside Greenhouse CMD: 1
     *
     * @param kelvin : temperature in kelvin (273 > T > 303)
     * @return true if processed
     */
    public boolean SetTemperature(int kelvin) {
        mess = new Message(TEMP_SETPOINT);
        if (kelvin > 273 && kelvin < 303) // 0 - 30 grader celcius
        {
            System.out.println("Set temperature setpoint to " + kelvin);
            mess.setData(kelvin - 273);
            conn.addMessage(mess);
            if (conn.send()) {
                return true;
            } else {
                return false;
            }
        }
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
        mess = new Message(MOIST_SETPOINT);
        if (moist > 10 && moist < 90) {
            mess.setData(moist);
            conn.addMessage(mess);
            if (conn.send()) {
                return true;
            } else {
                return false;
            }
        }
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
        System.out.println("Set red light to " + level);
        mess = new Message(REDLIGHT_SETPOINT);
        if (level >= 0 && level <= 100) {
            mess.setData(level);
            blueLight = 100 - level;
            conn.addMessage(mess);
            if (conn.send()) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    /**
     * Setpoint for blue light inside Greenhouse CMD: 4
     *
     * @param level in percent
     * @return true if processed
     */
    public boolean SetBlueLight(int level) {
        System.out.println("Set blue light to " + level);
        mess = new Message(BLUELIGHT_SETPOINT);
        if (level >= 0 && level <= 100) {
            mess.setData(level);
            blueLight = level;
            conn.addMessage(mess);
            if (conn.send()) {
                return true;
            } else {
                return false;
            }
        }
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
            mess = new Message(ADDWATER);
            mess.setData(sec);
            conn.addMessage(mess);
            if (conn.send()) {
                return true;
            } else {
                return false;
            }
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
        System.out.println("Read greenhouse temperature ");
        mess = new Message(READ_GREENHOUSE_TEMP);
        double temp = 0.0;
        mess.setData(); //None data
        conn.addMessage(mess);
        conn.addMessage(mess);
        if (conn.send()) {
            if (mess.getResultData() != null) {
                temp = (double) (mess.getResultData())[0];
                System.out.println(temp);
            } else {
                temp = 19.99; // return a dummy value
            }
        }
        System.out.println("Temperature is: " + temp + "celcius");
        return temp;
    }

    /**
     * Read tempature outside the Greenhouse CMD: 10
     *
     * @return Temperature in kelvin
     */
    public double ReadTemp2() {
        System.out.println("Read outdoor temperature ");
        mess = new Message(READ_OUTDOOR_TEMP);
        double temp2 = 0.0;
        mess.setData(); //None data
        conn.addMessage(mess);
        if (conn.send()) {
            if (mess.getResultData() != null) {
                temp2 = (double) (mess.getResultData())[0];
            } else {
                temp2 = 19.99; // return a dummy value
            }
        }
        System.out.println("Temperature is: " + temp2);
        return temp2 + 273.0;

    }

    /**
     * Read relative moisture inside the Greenhouse CMD: 11
     *
     * @return Moisture in %
     */
    public double ReadMoist() {
        System.out.println("Read outdoor temperature ");
        mess = new Message(READ_MOISTURE);
        double moist = 0.0;
        mess.setData(); //None data
        conn.addMessage(mess);
        if (conn.send()) {
            if (mess.getResultData() != null) {
                moist = (double) (mess.getResultData())[0];
            } else {
                moist = 1.0; // return a dummy value
            }                               // In real world moist will never be so low
        }
        System.out.println("Moisture is: " + moist + " %");
        return moist;
    }

    /**
     * Read level of water in the Greenhouse CMD: 17
     *
     * @return Level in millimeter [0 < level < 250]
     */
    public double ReadWaterLevel() {
        System.out.println("Read water level ");
        mess = new Message(READ_WATER_LEVEL);
        double level = 0.0; // level
        mess.setData(); //None data
        conn.addMessage(mess);
        if (conn.send()) {
            if (mess.getResultData() != null) {
                level = (mess.getResultData())[0] * 10.0;
            } else {
                level = 1000.0; // return a dummy value
            }
        }
        System.out.println("Water level is: " + level);
        return level;
    }

    /**
     * NOT IMPLEMENTED IN THE GREENHOUSE Read higths of the plants CMD: 12
     *
     * @return Higths (cm?)
     */
    public double ReadPlantHeight() {
        System.out.println("Read height of plants");
        mess = new Message(READ_PLANT_HEIGHT);
        double level = 0.0; // level
        mess.setData(); //None data
        conn.addMessage(mess);
        if (conn.send()) {
            if (mess.getResultData() != null) {
                level = (mess.getResultData())[0];
            } else {
                level = 1000.0; // return a dummy value
            }
        }
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
        mess = new Message(READ_ALL_ALARMS);
        BitSet alarms = new BitSet(32);

        mess.setData(); //None data
        conn.addMessage(mess);
        if (conn.send()) {
            alarms = fillBitSet(mess.getResultData());
            System.out.println(alarms.toString());
        }

        System.out.println("Alarm state is:                            " + alarms);
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
        mess = new Message(RESET_ALARMS);
        errorNum--;
        if (errorNum >= 0 && errorNum < 64) // 0 - 30 grader celcius
        {
            System.out.println("Reset alarm " + errorNum + 1);
            mess.setData(errorNum);
            conn.addMessage(mess);
            if (conn.send()) {
                return true;
            } else {
                return false;
            }
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
        mess = new Message(GET_STATUS);
        mess.setData(); //None data
        conn.addMessage(mess);
        if (conn.send()) {
            result = mess.getResultData();
        }
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
        System.out.println("Set fan speed " + speed);
        fanSpeed = speed;

        mess = new Message(SET_FAN_SPEED);
        if (speed >= 0 && speed <= 2) {
            mess.setData(speed);
            conn.addMessage(mess);
            if (conn.send()) {
                return true;
            } else {
                return false;
            }
        }
        return false;

    }

    @Override
    public void setLightIntensity(double level) {

        if (level < 0) {
            level = 0;
        } else if (level > 99) {
            level = 100;
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

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    @Override
    public int getFanspeed() {
        return fanSpeed;
    }

    public int getLastLog() {
        return lastLog;
    }

    public void log() {
        lastLog++;
        String values = "'" + getOrder().getBatch() + "', '" + getOrder().getRecipe().getRedLight() + "', '" + getOrder().getRecipe().getBlueLight() + "', '" + getLightIntensity() + "', '" + ReadTemp1() + "', '" + (ReadTemp2() - 273) + "', '" + ReadWaterLevel() + "', '"+ ReadMoist() + "', '" + getFanspeed() + "'";
        System.out.println("Information gathered!");
        SQLConnection.execute("INSERT INTO `" + IP + "` (`Batch`, `RedLight`, `BlueLight`, `LightIntensity`, `InsideTemp`, `OutsideTemp`, `WaterLevel`, `WaterLevel`, `FanRunning`) Values(" + values + ")");
        SQLConnection.close();
        System.out.println("Logging sucessful!");

    }

    @Override
    public int getLastWatering() {
        return lastIrrigation;
    }

    @Override
    public void waterGreenhouse() {
        System.out.println("                                                                                    THIS IS WATER");

        double irrigation = 24.0 / getOrder().getRecipe().getIrrigationsPrDay();

        if (lastIrrigation == 0) {

            lastIrrigation = getOrder().getSecondsElapsed();
            AddWater(getOrder().getRecipe().getWaterTime());

        } else if (lastIrrigation + (irrigation * 3600) < getOrder().getSecondsElapsed()) {
            lastIrrigation = getOrder().getSecondsElapsed();
            AddWater(getOrder().getRecipe().getWaterTime());

        }
    }

    @Override
    public void changeLightInGreenhouse() {
        //set the light
        double maxLight = getOrder().getRecipe().getHoursDay() / 2.0;
        double time = (getOrder().getSecondsElapsed() / 3600.0) % 24.0;
        System.out.println("                                                    LIGHT");
        if (time < maxLight) {
            setLightIntensity((maxLight + (time - maxLight)) / maxLight * 100);
            System.out.println("                                                    LIGHTINTENSITY:      " + lightIntensity);
        } else {

            setLightIntensity((maxLight - (time - maxLight)) / maxLight * 100);

            System.out.println("                                                    LIGHTINTENSITY2:      " + lightIntensity);
        }

        getAlarm();
        SetBlueLight((int) (getOrder().getRecipe().getBlueLight() * getLightIntensity() / 100));

        SetRedLight((int) (getOrder().getRecipe().getRedLight() * getLightIntensity() / 100));

    }

}
