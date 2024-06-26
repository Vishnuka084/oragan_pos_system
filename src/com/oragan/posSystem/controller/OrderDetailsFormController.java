package com.oragan.posSystem.controller;

import com.oragan.posSystem.db.DBConnection;

import com.oragan.posSystem.entity.Customer;
import com.oragan.posSystem.entity.Order;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import java.sql.*;


public class OrderDetailsFormController {
    public AnchorPane context;
    public TableView<Order> tblOrder;
    public TableColumn<Order,String>  colOrderID;
    public TableColumn<Order,String>  colCustomerID;
    public TableColumn<Order,String>  ColCustomerName;
    public TableColumn<Order,String>  colOrderDate;
    public TableColumn<Order,String>  colOrderTotal;
    public TableColumn<Order,Order> colOption;
    public ComboBox<String> cmbOrderID;
    public String cmbCustomerName;
    public String cmbOrderIDs;
    public TextField txtSearchField;

    private ObservableList<String> orderIds = FXCollections.observableArrayList();

    private ObservableList<Order> orderList = FXCollections.observableArrayList();

    public void initialize() {
        // Initialize columns
        colOrderID.setCellValueFactory(new PropertyValueFactory<>("orderID"));
        colOrderDate.setCellValueFactory(new PropertyValueFactory<>("orderDate"));
        colOrderTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        colCustomerID.setCellValueFactory(new PropertyValueFactory<>("customer_Id"));

        // Fetch data and populate the table
        fetchOrdersFromDatabase();
    }

    private void fetchOrdersFromDatabase() {
        orderList.clear();

        String query = "SELECT OrderID, OrderDate, Total, CustomerID FROM 'Order'";

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String orderID = rs.getString("OrderID");
                Date orderDate = rs.getDate("OrderDate");
                double total = rs.getDouble("Total");
                String customerID = rs.getString("CustomerID");

                Order order = new Order(orderID, orderDate, total, customerID);
                orderList.add(order);
                System.out.println(order);
            }

            // Set items in the table view
            tblOrder.setItems(orderList);

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace(); // Handle or log exception
        }
    }


    public void txtSearchFieldOnAction(ActionEvent actionEvent) {
        String searchText = txtSearchField.getText().trim();
        if (!searchText.isEmpty()) {
            filterTableBySearch(searchText);
        } else {
            tblOrder.setItems(orderList); // Reset to show all orders if search field is cleared
        }
    }

    private void filterTableBySearch(String searchText) {
        ObservableList<Order> filteredList = FXCollections.observableArrayList();
        for (Order order : orderList) {
            if (order.getOrderID().toLowerCase().contains(searchText.toLowerCase())) {
                filteredList.add(order);
            }
        }
        tblOrder.setItems(filteredList);
    }

    public void cmbSearchByOrderOnAction(ActionEvent actionEvent) {
        String selectedOrderID = cmbOrderID.getValue();
        if (selectedOrderID != null && !selectedOrderID.isEmpty()) {
            filterTableByOrderID(selectedOrderID);
        } else {
            tblOrder.setItems(orderList); // Reset to show all orders if no order ID is selected
        }
    }

    private void filterTableByOrderID(String orderID) {
        ObservableList<Order> filteredList = FXCollections.observableArrayList();
        for (Order order : orderList) {
            if (order.getOrderID().equalsIgnoreCase(orderID)) {
                filteredList.add(order);
            }
        }
        tblOrder.setItems(filteredList);
    }
}



