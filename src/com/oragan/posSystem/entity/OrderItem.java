/**
 * authour By Pamindu Nawodya
 * Date:6/24/2024
 * Time:7:02 PM
 * Project Name:oragan_pos_system
 */
package com.oragan.posSystem.entity;

import java.nio.charset.Charset;

public class OrderItem {


    private String OrderID;
    private String Item_code;
    private  String Item_name;
    private Integer Quantity;
    private double price;
    private double total;
    private String status;

    public OrderItem() {
    }

    public OrderItem(String orderID, String item_code, String item_name, Integer quantity, double price, double total, String status) {
        OrderID = orderID;
        Item_code = item_code;
        Item_name = item_name;
        Quantity = quantity;
        this.price = price;
        this.total = total;
        this.status = status;
    }

    public String getOrderID() {
        return OrderID;
    }

    public void setOrderID(String orderID) {
        OrderID = orderID;
    }

    public String getItem_code() {
        return Item_code;
    }

    public void setItem_code(String item_code) {
        Item_code = item_code;
    }

    public String getItem_name() {
        return Item_name;
    }

    public void setItem_name(String item_name) {
        Item_name = item_name;
    }

    public Integer getQuantity() {
        return Quantity;
    }

    public void setQuantity(Integer quantity) {
        Quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


}
