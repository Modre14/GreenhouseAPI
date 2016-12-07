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

    private Recipe recipe;
    private String name;
    private Date startDate;
    private Date endDate;
    private int quantity;
    private int daysCompleted;

    public Order(String name, Recipe recipe, Date startDate, Date endDate, int quantity) {

        this.name = name;
        this.recipe = recipe;
        this.startDate = startDate;
        this.endDate = endDate;
        this.quantity = quantity;
        this.daysCompleted = 0;
    }

    @Override
    public String toString() {
        return name + "\t - " + recipe + " Start date: " + startDate + " EndDate: " + endDate + " quantyty: " + quantity;
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
        return (int) ((d.getTime() - getStartDate().getTime()) / 1000);
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

}
