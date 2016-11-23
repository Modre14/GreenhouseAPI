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

    String id;
    int Temp;
    int minTemp;
    int maxTemp;
    int waterFlow;
    int redLight;
    int blueLight;
    int days;

    /**
     * @param waterFlow is the number of seconds the waterpump is on
     * @param redLight is the value of redLight
     * @param blueLight is the value of blueLight
     * @param days is the amount of before harvest
     */
    public Protocol(String id, int Temp, int minTemp, int maxTemp, int waterFlow, int redLight, int blueLight, int days) {
        this.id = id;
        this.Temp = Temp;
        this.waterFlow = waterFlow;
        this.redLight = redLight;
        this.blueLight = blueLight;
        this.days = days;
    }

    public String getId() {
        return id;
    }

}
