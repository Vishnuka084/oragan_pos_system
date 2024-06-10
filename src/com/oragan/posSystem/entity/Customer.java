
package com.oragan.posSystem.entity;

public class Customer {

    private String Customer_Id;
    private String Customer_name;
    private String Address;
    private String contact_number;


    public Customer() {
    }

    public Customer(String customer_Id, String customer_name, String address, String contact_number) {
        Customer_Id = customer_Id;
        Customer_name = customer_name;
        Address = address;
        this.contact_number = contact_number;
    }

    public String getCustomer_Id() {
        return Customer_Id;
    }

    public void setCustomer_Id(String customer_Id) {
        Customer_Id = customer_Id;
    }

    public String getCustomer_name() {
        return Customer_name;
    }

    public void setCustomer_name(String customer_name) {
        Customer_name = customer_name;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getContact_number() {
        return contact_number;
    }

    public void setContact_number(String contact_number) {
        this.contact_number = contact_number;
    }
}
