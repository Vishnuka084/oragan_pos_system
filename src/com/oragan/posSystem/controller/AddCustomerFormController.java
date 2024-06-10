package com.oragan.posSystem.controller;
import com.oragan.posSystem.db.DBConnection;
import javafx.event.ActionEvent;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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

        String sql = "INSERT INTO customers(Customer_Id, customer_name, address, contact_number) VALUES(?,?,?,?)";
        try {
            // Get connection from the DBConnection singleton
            Connection conn = DBConnection.getInstance().getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, txtCustomerId.getText());
                pstmt.setString(2, txtCustomerName.getText());
                pstmt.setString(3, txtCustomerAddress.getText());
                pstmt.setString(4, txtContactNumber.getText());
                pstmt.executeUpdate();
                System.out.println("data add sucessfully");
            }
        } catch ( ClassNotFoundException | SQLException e) {
            System.out.println("Error adding customer to the database: " + e.getMessage());
        }
    }
    }




