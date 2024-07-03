
package com.oragan.posSystem.controller;

import com.oragan.posSystem.db.DBConnection;
import com.oragan.posSystem.entity.Customer;
import com.oragan.posSystem.entity.Item;
import com.oragan.posSystem.entity.Order;
import com.oragan.posSystem.entity.OrderItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import java.sql.*;


public class OrderDetailsFormController {
    public TableView tblOrder;
    public TableColumn colOrderID;
    public TableColumn colCustomerID;
    public TableColumn colOrderDate;
    public TableColumn colOrderTotal;
    public String cmbCustomerName;
    public String cmbOrderIDs;
    public TableColumn colCustomerName;
    public TableColumn colStatus;
    private ObservableList<Order> orderList = FXCollections.observableArrayList();

    public void initialize() {
        // Initialize columns
        colOrderID.setCellValueFactory(new PropertyValueFactory<>("orderID"));
        colOrderDate.setCellValueFactory(new PropertyValueFactory<>("orderDate"));
        colOrderTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        colCustomerID.setCellValueFactory(new PropertyValueFactory<>("customer_Id"));
        colCustomerName.setCellValueFactory(new PropertyValueFactory<>("Customer_name"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("Status"));

        // Fetch data and populate the table
        fetchOrdersFromDatabase();
    }

    private void fetchOrdersFromDatabase() {
        orderList.clear();

        String query = "SELECT OrderID, OrderDate, Total, Customer_name, Customer_Id, Status FROM 'Order'";

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String orderID = rs.getString("OrderID");
                Date orderDate = rs.getDate("OrderDate");
                double total = rs.getDouble("Total");
                String customerID = rs.getString("Customer_Id");
                String customerName = rs.getString("Customer_name");
                String status = rs.getString("Status");

                Order order = new Order(orderID, orderDate, total, customerID,customerName,status);
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

    }

    public void cmbSearchByOrderOnAction(ActionEvent actionEvent) {
    }
}



