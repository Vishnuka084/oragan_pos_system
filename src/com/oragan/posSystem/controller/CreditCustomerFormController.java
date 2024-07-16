/**
 * authour By Pamindu Nawodya
 * Date:7/13/2024
 * Time:5:29 PM
 * Project Name:oragan_pos_system
 */
package com.oragan.posSystem.controller;

import com.oragan.posSystem.db.DBConnection;
import com.oragan.posSystem.entity.Order;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CreditCustomerFormController {


    public TableColumn <Order,String> colName;
    public TableColumn <Order,String> colId;
    public TableView <Order>CustomerNametbl;
    public TextField txtCustomerName;
    private ObservableList<Order> creditCustomerList = FXCollections.observableArrayList();


    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("Customer_Id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("Customer_name"));

        CustomerNametbl.setItems(creditCustomerList);
        txtCustomerName.textProperty().addListener((observable, oldValue, newValue) -> filterCustomerList(newValue));
    }


    public void searchCustomerByName(ActionEvent actionEvent) {
        String searchName = txtCustomerName.getText().trim().toLowerCase();
        if (searchName.isEmpty()) {
            // If search field is empty, display all credit customers
            CustomerNametbl.setItems(creditCustomerList);
        } else {
            // Filter customers by name
            ObservableList<Order> filteredList = FXCollections.observableArrayList();
            for (Order order : creditCustomerList) {
                if (order.getCustomer_name().toLowerCase().contains(searchName)) {
                    filteredList.add(order);
                }
            }
            CustomerNametbl.setItems(filteredList);
        }
    }

    private void filterCustomerList(String searchName) {
        if (searchName == null || searchName.trim().isEmpty()) {
            // If search field is empty, display all credit customers
            CustomerNametbl.setItems(creditCustomerList);
        } else {
            // Filter customers by name
            ObservableList<Order> filteredList = FXCollections.observableArrayList();
            for (Order order : creditCustomerList) {
                if (order.getCustomer_name().toLowerCase().contains(searchName.toLowerCase().trim())) {
                    filteredList.add(order);
                }
            }
            CustomerNametbl.setItems(filteredList);
        }
    }




    public void FetchCreditCustomers() {
        creditCustomerList.clear(); // Clear existing data
        try (Connection conn = DBConnection.getInstance().getConnection()) {
            // SQL query to fetch customer names where creditOrDebit is 'Credit'
            String query = "SELECT Customer_name, Customer_Id FROM `Order` WHERE Credit_Or_Debit = 'Credit'";

            try (PreparedStatement ps = conn.prepareStatement(query)) {
                // Execute the query
                ResultSet rs = ps.executeQuery();

                // Process the result set
                while (rs.next()) {
                    String customerName = rs.getString("Customer_name");
                    String customerId = rs.getString("Customer_Id");

                    // Check for empty or null values before adding to the list
                    if (customerName != null && !customerName.trim().isEmpty() &&
                            customerId != null && !customerId.trim().isEmpty()) {
                        creditCustomerList.add(new Order(customerId, customerName));
                    }
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            // Handle the error as needed
        }
        CustomerNametbl.setItems(creditCustomerList);
    }

}

