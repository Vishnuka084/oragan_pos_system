/**
 * authour By Pamindu Nawodya
 * Date:6/10/2024
 * Time:4:07 PM
 * Project Name:oragan_pos_system
 */
package com.oragan.posSystem.entity;

public class Item {

    private String Item_code;
    private String Item_name;
    private double price;
    private int qty;

    public Item() {
    }

    public Item(String item_code, String item_name, double price, int qty) {
        Item_code = item_code;
        Item_name = item_name;
        this.price = price;
        this.qty = qty;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }
}
