/**
 * authour By Pamindu Nawodya
 * Date:6/11/2024
 * Time:1:27 AM
 * Project Name:oragan_pos_system
 */
package com.oragan.posSystem.dto;

public class ItemDTO {
    private String Item_code;
    private String Item_name;
    private double price;
    private int qty;

    public ItemDTO() {
    }

    public ItemDTO(String item_code, String item_name, double price, int qty) {
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


    @Override
    public String toString() {
        return "ItemDTO{" +
                "Item_code='" + Item_code + '\'' +
                ", Item_name='" + Item_name + '\'' +
                ", price=" + price +
                ", qty=" + qty +
                '}';
    }
}
