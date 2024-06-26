/**
 * authour By Pamindu Nawodya
 * Date:6/24/2024
 * Time:10:18 PM
 * Project Name:oragan_pos_system
 */
package com.oragan.posSystem.entity;

import java.sql.Date;

public class Order {

    private String OrderID;
    private Date OrderDate;
    private double Total;
    private String Customer_Id;


    public Order(String orderID) {
        OrderID = orderID;
    }

    public Order(String orderID, Date orderDate, double total, String customer_Id) {
        OrderID = orderID;
        OrderDate = orderDate;
        Total = total;
        Customer_Id = customer_Id;
    }


    public String getOrderID() {
        return OrderID;
    }

    public void setOrderID(String orderID) {
        OrderID = orderID;
    }

    public Date getOrderDate() {
        return OrderDate;
    }

    public void setOrderDate(Date orderDate) {
        OrderDate = orderDate;
    }

    public double getTotal() {
        return Total;
    }

    public void setTotal(double total) {
        Total = total;
    }

    public String getCustomer_Id() {
        return Customer_Id;
    }

    public void setCustomer_Id(String customer_Id) {
        Customer_Id = customer_Id;
    }

    @Override
    public String toString() {
        return "Order{" +
                "OrderID='" + OrderID + '\'' +
                ", OrderDate=" + OrderDate +
                ", Total=" + Total +
                ", Customer_Id='" + Customer_Id + '\'' +
                '}';
    }

}
