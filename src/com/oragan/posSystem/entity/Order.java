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
    private String Customer_name;
    private String Customer_Id;
    private String Status;

    public Order() {
    }

    public Order(String orderID, Date orderDate, double total, String customer_name, String customer_Id, String status) {
        OrderID = orderID;
        OrderDate = orderDate;
        Total = total;
        Customer_name = customer_name;
        Customer_Id = customer_Id;
        Status = status;
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

    public String getCustomer_name() {
        return Customer_name;
    }

    public void setCustomer_name(String customer_name) {
        Customer_name = customer_name;
    }

    public String getCustomer_Id() {
        return Customer_Id;
    }

    public void setCustomer_Id(String customer_Id) {
        Customer_Id = customer_Id;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

}
