package com.oragan.posSystem.entity;

public class OrderItem {

    private String OrderID;
    private String Item_code;
    private  String Item_name;
    private Integer Quantity;
    private double price;
    private double total;

    public OrderItem() {
    }

    public OrderItem(String orderID, String item_code, String item_name, Integer quantity, double price, double total) {
        OrderID = orderID;
        Item_code = item_code;
        Item_name = item_name;
        Quantity = quantity;
        this.price = price;
        this.total = total;
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

    @Override
    public String toString() {
        return "OrderItem{" +
                "OrderID='" + OrderID + '\'' +
                ", Item_code='" + Item_code + '\'' +
                ", Item_name='" + Item_name + '\'' +
                ", Quantity=" + Quantity +
                ", price=" + price +
                ", total=" + total +
                '}';
    }
}
