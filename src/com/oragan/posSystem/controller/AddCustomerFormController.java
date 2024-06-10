/**
 * authour By Pamindu Nawodya
 * Date:6/1/2024
 * Time:10:14 PM
 * Project Name:oragan_pos_system
 */
package com.oragan.posSystem.controller;

import com.oragan.posSystem.db.DBConnection;
import com.oragan.posSystem.entity.Customer;
import javafx.event.ActionEvent;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.sql.*;

public class AddCustomerFormController {


    public AnchorPane addCustomerFormContext;
    public TextField txtCustomerId;
    public TextField txtCustomerName;
    public TextField txtCustomerAddress;
    public TextField txtContactNumber;

    public void btnClearTxtFldOnAction(ActionEvent actionEvent) {
        txtCustomerId.clear();
        txtCustomerName.clear();
        txtCustomerAddress.clear();
        txtContactNumber.clear();
    }

    public void btnCustomerAddOnAction(ActionEvent actionEvent) {
       /* String customerID=txtCustomerId.getText();
        String customerName=txtCustomerName.getText();
        String customerAddress=txtCustomerAddress.getText();
        String customerSalary=txtContactNumber.getText();

        Customer c1=new Customer(
                customerID,
                customerName,
                customerAddress,customerSalary
        );*/

        String newCustomerId = generateNewCustomerId();
        if (newCustomerId == null) {
            System.out.println("Error generating new customer ID.");
            return;
        }


        String sql = "INSERT INTO customers(Customer_Id, customer_name, address, contact_number) VALUES(?,?,?,?)";
        try {
            // Get connection from the DBConnection singleton
            Connection conn = DBConnection.getInstance().getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, newCustomerId);
                pstmt.setString(2, txtCustomerName.getText());
                pstmt.setString(3, txtCustomerAddress.getText());
                pstmt.setString(4, txtContactNumber.getText());
                pstmt.executeUpdate();
                System.out.println("customer data add sucessfully");
            }
        } catch ( ClassNotFoundException | SQLException e) {
            System.out.println("Error adding customer to the database: " + e.getMessage());
        }
    }

    //Auto Genarate ID Function

    private String generateNewCustomerId() {
        String sql = "SELECT Customer_Id FROM customers ORDER BY Customer_Id DESC LIMIT 1";
        try {
            Connection conn = DBConnection.getInstance().getConnection();
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                if (rs.next()) {
                    String lastId = rs.getString("Customer_Id");
                    if (lastId != null && lastId.length() > 1 && lastId.startsWith("C")) {
                        String numericPartStr = lastId.substring(1);
                        try {
                            int numericPart = Integer.parseInt(numericPartStr) + 1;
                            return String.format("C%02d", numericPart);
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid numeric part in the last customer ID: " + numericPartStr);
                            return null;
                        }
                    } else {
                        System.out.println("Invalid format of the last customer ID: " + lastId);
                        return null;
                    }
                } else {
                    // If no records found, start with C01
                    return "C01";
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Error fetching last customer ID: " + e.getMessage());
            return null;
        }
    }



    }




