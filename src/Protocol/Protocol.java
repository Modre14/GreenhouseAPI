/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Protocol;

import java.io.Serializable;

/**
 *
 * @author Morten
 */
public class Protocol implements Serializable {

    String id;
    int temp;
    int minTemp;
    int maxTemp;
    int waterFlow;
    int redLight;
    int blueLight;
    int days;
    int lightIntensity;
    /**
     * @param waterFlow is the number of seconds the waterpump is on
     * @param redLight is the value of redLight
     * @param blueLight is the value of blueLight
     * @param days is the amount of before harvest
     */
    public Protocol(String id, int temp, int minTemp, int maxTemp, int waterFlow, int redLight, int blueLight, int days, int lightIntensity) {
        this.id = id;
        this.temp = temp;
        this.waterFlow = waterFlow;
        this.redLight = redLight;
        this.blueLight = blueLight;
        this.days = days;
        this.lightIntensity = lightIntensity;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Protocol{" + "id=" + id + ", Temp=" + temp + ", minTemp=" + minTemp + ", maxTemp=" + maxTemp + ", waterFlow=" + waterFlow + ", redLight=" + redLight + ", blueLight=" + blueLight + ", days=" + days + '}';
    }

    public int getTemp() {
        return temp;
    }

    public int getMinTemp() {
        return minTemp;
    }

    public int getMaxTemp() {
        return maxTemp;
    }

    public int getWaterFlow() {
        return waterFlow;
    }

    public int getRedLight() {
        return redLight;
    }

    public int getBlueLight() {
        return blueLight;
    }

    public int getDays() {
        return days;
    }

    public int getLightIntensity() {
        return lightIntensity;
    }

}
