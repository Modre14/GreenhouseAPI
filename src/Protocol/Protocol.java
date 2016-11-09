/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Protocol;

/**
 *
 * @author Morten
 */
public class Protocol {
    String name;
    int day;
    int night;
    int dayTemp;
    int nightTemp;
    int waterFlow;
    int redLight;
    int blueLight;
    int days;

    /**
     * @param day is the number of hours with light
     * @param night is the number of hours with darkness
     * @param dayTemp is is the temperature  during the day
     * @param nightTemp is the temperature during the day 
     * @param waterFlow is the nuber of seconds the waterpum is on
     * @param redLight is the value of redLight
     * @param blueLight is the value of blueLight
     * @param days is the amount of before harvest 
     */
    public Protocol(String name, int day, int dayTemp, int waterFlow, int redLight, int blueLight, int days) {
        this.name = name;
        this.day = day;
        this.dayTemp = dayTemp;
        this.waterFlow = waterFlow;
        this.redLight = redLight;
        this.blueLight = blueLight;
        this.days = days;
    }

}
