/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Protocol;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Morten
 */
public class Order implements Serializable {

    private Protocol protocol;
    private String name;
    private Date startDate;
    private Date endDate;
    private int quantity;

    public Order(String name, Protocol protocol, Date startDate, Date endDate, int quantity) {

        this.name = name;
        this.protocol = protocol;
        this.startDate = startDate;
        this.endDate = endDate;
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Navn:" + name + " Protocol: " + protocol + " Start date: " + startDate + " EndDate: " + endDate + " quantity: " + quantity;
    }

}
