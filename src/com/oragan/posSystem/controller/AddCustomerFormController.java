package com.oragan.posSystem.controller;

import com.oragan.posSystem.db.DBConnection;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.sql.*;

public class AddCustomerFormController {

    public AnchorPane addCustomerFormContext;
    public TextField txtCustomerId;
    public TextField txtCustomerName;
    public TextField txtCustomerAddress;
    public TextField txtContactNumber;

    public void initialize() {
        setNewCustomerId();
    }

    private void setNewCustomerId() {
        String newCustomerId = generateNewCustomerId();
        if (newCustomerId != null) {
            txtCustomerId.setText(newCustomerId);
        } else {
            System.out.println("Error generating new customer ID.");
        }
    }

    public void btnClearTxtFldOnAction(ActionEvent actionEvent) {
        txtCustomerId.clear();
        txtCustomerName.clear();
        txtCustomerAddress.clear();
        txtContactNumber.clear();
    }

    public void btnCustomerAddOnAction(ActionEvent actionEvent) {
        String newCustomerId = txtCustomerId.getText(); // Use the ID set by initialize method
        if (newCustomerId == null || newCustomerId.isEmpty()) {
            System.out.println("Error: Customer ID is missing.");
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
                System.out.println("Customer data added successfully");

                // Show success alert
                showAlert(Alert.AlertType.INFORMATION, "Success", "Customer added successfully.");

                // Reset the form
                btnClearTxtFldOnAction(null);
                setNewCustomerId(); // Generate a new customer ID for the next entry
            }
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Error adding customer to the database: " + e.getMessage());
        }
    }

    // Auto Generate ID Function
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
                            return String.format("C%03d", numericPart);
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid numeric part in the last customer ID: " + numericPartStr);
                            return null;
                        }
                    } else {
                        System.out.println("Invalid format of the last customer ID: " + lastId);
                        return null;
                    }
                } else {
                    // If no records found, start with C001
                    return "C001";
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Error fetching last customer ID: " + e.getMessage());
            return null;
        }
    }

    // Show alert method
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
