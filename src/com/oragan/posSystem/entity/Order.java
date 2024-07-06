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
    private double Total;
    private String Customer_name;
    private String Customer_Id;
    private String Status;
    private String Current_Date;
    private String Issue_Date;

    public Order() {
    }

    public Order(String orderID, double total, String customer_name, String customer_Id, String status, String current_Date, String issue_Date) {
        OrderID = orderID;
        Total = total;
        Customer_name = customer_name;
        Customer_Id = customer_Id;
        Status = status;
        Current_Date = current_Date;
        Issue_Date = issue_Date;
    }

    public String getOrderID() {
        return OrderID;
    }

    public void setOrderID(String orderID) {
        OrderID = orderID;
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

    public String getCurrent_Date() {
        return Current_Date;
    }

    public void setCurrent_Date(String current_Date) {
        Current_Date = current_Date;
    }

    public String getIssue_Date() {
        return Issue_Date;
    }

    public void setIssue_Date(String issue_Date) {
        Issue_Date = issue_Date;
    }
}
