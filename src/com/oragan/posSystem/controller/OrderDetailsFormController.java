
package com.oragan.posSystem.controller;

import com.oragan.posSystem.db.DBConnection;
import com.oragan.posSystem.entity.Order;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.sql.*;


public class OrderDetailsFormController {
    public TableView<Order> tblOrder;
    public TableColumn<Order, String> colOrderID;
    public TableColumn<Order, String> colCustomerID;
    public TableColumn<Order, Date> colOrderDate;
    public TableColumn<Order, Double> colOrderTotal;
    public TableColumn<Order, String> colCustomerName;
    public TableColumn<Order, String> colStatus;
    public TableColumn<Order, Order> colOptions;
    public String cmbCustomerName;
    public String cmbOrderIDs;
    public ComboBox<String> cmbOrderID;
    public TextField txtSerchFieldOrder;
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
        initializeTableColumns();

        txtSerchFieldOrder.textProperty().addListener((observable, oldValue, newValue) -> filterCustomerList(newValue));
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

                Order order = new Order(orderID, orderDate, total, customerID, customerName, status);
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
        filterCustomerList(txtSerchFieldOrder.getText());
    }

    public void cmbSearchByOrderOnAction(ActionEvent actionEvent) {
        String selectedValue = cmbOrderID.getValue();
        if ("Customer Name".equals(selectedValue)) {
            txtSerchFieldOrder.setPromptText("Enter customer  name");
            txtSerchFieldOrder.clear();
        }
        filterCustomerList(txtSerchFieldOrder.getText());
    }

    private void filterCustomerList(String searchQuery) {
        if (searchQuery == null || searchQuery.isEmpty()) {
            tblOrder.setItems(orderList);  // Display all orders if search query is empty
        } else {
            ObservableList<Order> filteredList = FXCollections.observableArrayList();
            String selectedValue = cmbOrderID.getValue();
            if ("Customer Name".equals(selectedValue)) {
                for (Order order : orderList) {
                    if (order.getCustomer_name().toLowerCase().contains(searchQuery.toLowerCase())) {
                        filteredList.add(order);
                    }
                }
            } else if ("Order ID".equals(selectedValue)) {
                for (Order order : orderList) {
                    if (order.getOrderID().toLowerCase().contains(searchQuery.toLowerCase())) {
                        filteredList.add(order);
                    }
                }
            }
            tblOrder.setItems(filteredList);
        }
    }

    private void initializeTableColumns() {
        colCustomerID.setCellValueFactory(new PropertyValueFactory<>("customer_Id"));
        colCustomerName.setCellValueFactory(new PropertyValueFactory<>("customer_name"));
        colOrderID.setCellValueFactory(new PropertyValueFactory<>("orderID"));
        colOrderTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colOrderDate.setCellValueFactory(new PropertyValueFactory<>("orderDate"));

        colOptions.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        colOptions.setCellFactory(param -> new TableCell<Order, Order>() {

            private final Button deleteButton = new Button();

            @Override
            protected void updateItem(Order order, boolean empty) {
                super.updateItem(order, empty);

                if (order == null) {
                    setGraphic(null);
                    return;
                }

                // Load the delete icon image
                Image deleteImage = new Image(getClass().getResourceAsStream("/com/oragan/posSystem/assets/icons8-delete-120.png"));
                if (deleteImage != null) {
                    ImageView deleteImageView = new ImageView(deleteImage);
                    deleteImageView.setFitWidth(20); // Adjust the size as needed
                    deleteImageView.setFitHeight(20); // Adjust the size as needed
                    deleteButton.setGraphic(deleteImageView);
                }

                HBox hBox = new HBox(deleteButton);
                hBox.setAlignment(Pos.CENTER);
                hBox.setSpacing(10);
                setGraphic(hBox);

                deleteButton.setOnAction(event -> {
                    // Handle the delete action
                    handleDeleteOrder(order);
                });
            }
        });

        tblOrder.setItems(orderList);
    }


    private void handleDeleteOrder(Order order) {
        // Remove the order from the orderList
        orderList.remove(order);

        // Delete the order from the database
        String query = "DELETE FROM 'OrderItem' WHERE OrderID = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, order.getOrderID());
            ps.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace(); // Handle or log exception
        }
    }





}


