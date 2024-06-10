/**
 * authour By Pamindu Nawodya
 * Date:6/11/2024
 * Time:1:27 AM
 * Project Name:oragan_pos_system
 */
package com.oragan.posSystem.dto;

public class CustomerDTO {

    private String Customer_Id;
    private String Customer_name;
    private String Address;
    private String contact_number;

    public CustomerDTO() {
    }


    public CustomerDTO(String customer_Id, String customer_name, String address, String contact_number) {
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

    @Override
    public String toString() {
        return "CustomerDTO{" +
                "Customer_Id='" + Customer_Id + '\'' +
                ", Customer_name='" + Customer_name + '\'' +
                ", Address='" + Address + '\'' +
                ", contact_number='" + contact_number + '\'' +
                '}';
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
