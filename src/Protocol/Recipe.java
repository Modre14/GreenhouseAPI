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
public class Recipe implements Serializable {

    String id;
    int temp;
    int minTemp;
    int maxTemp;
    int waterTime;
    double irrigationPrDay;
    int redLight;
    int blueLight;
    int days;
    double hoursDay;

    /**
     * @param id
     * @param temp
     * @param minTemp
     * @param maxTemp
     * @param waterTime is the number of seconds the waterpump is on
     * @param irriationPrDay
     * @param redLight is the value of redLight
     * @param blueLight is the value of blueLight
     * @param days is the amount of before harvest
     * @param hoursDay
     */
    public Recipe(String id, int temp, int minTemp, int maxTemp, int waterTime, int irriationPrDay, int redLight, int blueLight, int days, double hoursDay) {
        this.id = id;
        this.temp = temp;
        this.maxTemp = maxTemp;
        this.minTemp = minTemp;
        this.waterTime = waterTime;
        this.irrigationPrDay = irriationPrDay;
        this.redLight = redLight;
        this.blueLight = blueLight;
        this.days = days;
        this.hoursDay = hoursDay;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Protocol{" + "id=" + id + ", Temp=" + temp + ", minTemp=" + minTemp + ", maxTemp=" + maxTemp + ", waterTime=" + waterTime + ", redLight=" + redLight + ", blueLight=" + blueLight + ", days=" + days + '}';
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

    public int getWaterTime() {
        return waterTime;
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

    public double getHoursDay() {
        return hoursDay;
    }

    public void setRedLight(int redLight) {
        this.redLight = redLight;
    }

    public void setBlueLight(int blueLight) {
        this.blueLight = blueLight;
    }

}
