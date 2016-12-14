/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Protocol;

import java.io.Serializable;
import java.sql.*;
import java.util.Date;

/**
 *
 * @author Morten
 */
public class Order implements Serializable {

    private Recipe recipe;
    private String name;
    private Date startDate;
    private Date endDate;
    private Date orderStarted;
    private int quantity;
    private int batch;

   /**
    *
    * @param name
    * @param recipe
    * @param startDate
    * @param endDate
    * @param quantity
    */
    public Order(String name, Recipe recipe, Date startDate, Date endDate, int quantity) {

        this.name = name;
        this.recipe = recipe;
        this.startDate = startDate;
        this.endDate = endDate;
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return name + "\t - " + recipe + " Start date: " + startDate + " EndDate: " + endDate + " quantity: " + quantity;
    }

    public String getName() {
        return name;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getSecondsElapsed() {
        Date d = new Date();
        return (int) (((d.getTime() - orderStarted.getTime()) / 1000*10));
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setBatch(int batch){
        this.batch = batch;

    }

    public int getBatch(){
        return this.batch;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setOrderStarted (Date d){
        this.orderStarted = d;
    }

}
