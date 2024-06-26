package com.oragan.posSystem.controller;

import com.oragan.posSystem.db.DBConnection;
import com.oragan.posSystem.entity.Customer;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.sql.*;

public class AddCustomerFormController {

    public AnchorPane addCustomerFormContext;
    public TextField txtCustomerId;
    public TextField txtCustomerName;
    public TextField txtCustomerAddress;
    public TextField txtContactNumber;
    private TableView<Customer> tblCustomer;
    private CustomerFormController customerFormController;
    private Customer customerToUpdate;  // Track the customer being updated

    public void initialize() {
        // If customerToUpdate is not set, it's an add operation
        if (customerToUpdate == null) {
            setNewCustomerId();
        }
    }

    public void initForUpdate(TableView<Customer> tblCustomer, CustomerFormController customerFormController, Customer customer) {
        this.tblCustomer = tblCustomer;
        this.customerFormController = customerFormController;
        this.customerToUpdate = customer;

        // Pre-fill the form fields with customer data
        txtCustomerId.setText(customer.getCustomer_Id());
        txtCustomerName.setText(customer.getCustomer_name());
        txtCustomerAddress.setText(customer.getAddress());
        txtContactNumber.setText(customer.getContact_number());
        txtCustomerId.setEditable(false);  // Disable editing of ID during update
    }

    public void init(TableView<Customer> tblCustomer, CustomerFormController customerFormController) {
        this.tblCustomer = tblCustomer;
        this.customerFormController = customerFormController;
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
        txtCustomerName.clear();
        txtCustomerAddress.clear();
        txtContactNumber.clear();
        resetFieldStyles();

        if (customerToUpdate == null) {
            setNewCustomerId();
        } else {
            txtCustomerId.setText(customerToUpdate.getCustomer_Id());
        }
    }

    public void btnCustomerAddOnAction(ActionEvent actionEvent) {
        if (!validateInput()) {
            return;
        }

        if (customerToUpdate != null) {
            updateCustomer();
        } else {
            addNewCustomer();
        }
    }

    private void addNewCustomer() {
        String newCustomerId = txtCustomerId.getText();
        String sql = "INSERT INTO customers(Customer_Id, customer_name, address, contact_number) VALUES(?,?,?,?)";
        try {
            Connection conn = DBConnection.getInstance().getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, newCustomerId);
                pstmt.setString(2, txtCustomerName.getText());
                pstmt.setString(3, txtCustomerAddress.getText());
                pstmt.setString(4, txtContactNumber.getText());
                pstmt.executeUpdate();
                showAlert(Alert.AlertType.INFORMATION, "Success", "Customer added successfully.");
                customerFormController.refreshCustomerData();
                btnClearTxtFldOnAction(null);
                setNewCustomerId();
            }
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Error adding customer to the database: " + e.getMessage());
        }
    }

    private void updateCustomer() {
        String sql = "UPDATE customers SET customer_name=?, address=?, contact_number=? WHERE Customer_Id=?";
        try {
            Connection conn = DBConnection.getInstance().getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, txtCustomerName.getText());
                pstmt.setString(2, txtCustomerAddress.getText());
                pstmt.setString(3, txtContactNumber.getText());
                pstmt.setString(4, txtCustomerId.getText());
                pstmt.executeUpdate();
                showAlert(Alert.AlertType.INFORMATION, "Success", "Customer updated successfully.");
                customerFormController.refreshCustomerData();
                ((Stage) addCustomerFormContext.getScene().getWindow()).close();
            }
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Error updating customer in the database: " + e.getMessage());
        }
    }

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
                    return "C001";
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Error fetching last customer ID: " + e.getMessage());
            return null;
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean validateInput() {
        boolean valid = true;
        resetFieldStyles();

        if (!txtCustomerName.getText().matches("^[A-Z][a-zA-Z]*$")) {
            txtCustomerName.setStyle("-fx-border-color: red;");
            Tooltip tooltip = new Tooltip("Name should start with a capital letter and contain only letters.");
            Tooltip.install(txtCustomerName, tooltip);
            valid = false;
        } else {
            txtCustomerName.setStyle("-fx-border-color: #2aff2a;");
        }

        if (!txtCustomerAddress.getText().matches("^[A-Z][a-zA-Z0-9\\s]*$")) {
            txtCustomerAddress.setStyle("-fx-border-color: red;");
            Tooltip tooltip = new Tooltip("Address should start with a capital letter and contain only letters and numbers.");
            Tooltip.install(txtCustomerAddress, tooltip);
            valid = false;
        } else {
            txtCustomerAddress.setStyle("-fx-border-color: #2aff2a;");
        }

        if (!txtContactNumber.getText().matches("^\\+94[0-9]+$")) {
            txtContactNumber.setStyle("-fx-border-color: red;");
            Tooltip tooltip = new Tooltip("Phone number should start with +94 and contain only numbers.");
            Tooltip.install(txtContactNumber, tooltip);
            valid = false;
        } else {
            txtContactNumber.setStyle("-fx-border-color: #2aff2a;");
        }

        System.out.println("Hello Ravindu");

        if (!valid) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Please correct the input fields.");
        }

        return valid;
    }

    private void resetFieldStyles() {
        txtCustomerName.setStyle(null);
        txtCustomerAddress.setStyle(null);
        txtContactNumber.setStyle(null);
    }
}
