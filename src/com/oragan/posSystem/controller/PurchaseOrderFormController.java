package com.oragan.posSystem.controller;

import com.oragan.posSystem.db.DBConnection;
import com.oragan.posSystem.entity.Customer;
import com.oragan.posSystem.entity.Item;
import com.oragan.posSystem.entity.OrderItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class PurchaseOrderFormController {
    public ComboBox<String> cmbCustomerID;
    public TextField txtCustomerName;
    public TextField txtContactNumber;
    public ComboBox<String> cmbItemCode;
    public TextField txtItemName;
    public TextField txtQtyOnHand;
    public TextField txtCustomerAddress;
    public TextField txtPrice;
    public Label lblOrderID;
    public TableColumn colItemCode;
    public TableColumn colItemName;
    public TableColumn colUnitPrice;
    public TableColumn colQuantity;
    public TableColumn colTotal;
    public TextField txtQuantity;
    public TableView tblCart;
    public TextField txtTotal;
    private List<Customer> customers;
    private List<Item> items;
    private ObservableList<OrderItem> cartItems;

    public void initialize() {
        customers = getAllCustomers();
        for (Customer customer : customers) {
            cmbCustomerID.getItems().add(customer.getCustomer_Id());
        }
        if (!customers.isEmpty()) {
            cmbCustomerID.getSelectionModel().selectFirst();
            setCustomerDetails(customers.get(0).getCustomer_Id());
        }

        items = getAllItems();
        for (Item item : items) {
            cmbItemCode.getItems().add(item.getItem_code());
        }
        if (!items.isEmpty()) {
            cmbItemCode.getSelectionModel().selectFirst();
            setItemDetails(items.get(0).getItem_code());
        }

        //generate  order Id
        try {
            String orderId = generateOrderId();
            lblOrderID.setText(orderId);
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Error generating order ID: " + e.getMessage());
        }

        // Initialize cart table
        cartItems = FXCollections.observableArrayList();
        tblCart.setItems(cartItems);
        colItemCode.setCellValueFactory(new PropertyValueFactory<>("item_code"));
        colItemName.setCellValueFactory(new PropertyValueFactory<>("item_name"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));

    }

    private String generateOrderId() throws SQLException, ClassNotFoundException {
        String query = "SELECT OrderID FROM 'Order' ORDER BY OrderID DESC LIMIT 1";

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                String lastOrderId = rs.getString("OrderID");
                int lastIdNumber = Integer.parseInt(lastOrderId.replace("ORD-", ""));
                int newIdNumber = lastIdNumber + 1;
                return String.format("ORD-%06d", newIdNumber);
            } else {
                // If no orders exist, start with ORD-000001
                return "ORD-000001";
            }
        }
    }



    private void setItemDetails(String Item_code) {
        for (Item item : items) {
            if (item.getItem_code().equals(Item_code)) {
                txtItemName.setText(item.getItem_name());
                txtPrice.setText(String.valueOf(item.getPrice()));
                txtQtyOnHand.setText(String.valueOf(item.getQty()));
                break;
            }
        }
    }

    private List<Item> getAllItems() {
        List<Item> itemList = new ArrayList<>();
        String query = "SELECT * FROM items";

        try {
            DBConnection dbConnection = DBConnection.getInstance();
            if (dbConnection.isConnectionValid()) {
                try (Connection conn = dbConnection.getConnection();
                     PreparedStatement ps = conn.prepareStatement(query);
                     ResultSet rs = ps.executeQuery()) {

                    while (rs.next()) {
                        Item item = new Item();
                        item.setItem_code(rs.getString("Item_code"));
                        item.setItem_name(rs.getString("Item_name"));
                        item.setPrice(rs.getDouble("price"));
                        item.setQty(rs.getInt("qty"));
                        itemList.add(item);
                    }
                }
            } else {
                System.err.println("Database connection is not valid.");
            }
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Error loading items: " + e.getMessage());
        }

        return itemList;
    }

    private void setCustomerDetails(String customer_id) {
        for (Customer customer : customers) {
            if (customer.getCustomer_Id().equals(customer_id)) {
                txtCustomerName.setText(customer.getCustomer_name());
                txtCustomerAddress.setText(customer.getAddress());
                txtContactNumber.setText(customer.getContact_number());
                break;
            }
        }
    }

    private List<Customer> getAllCustomers() {
        List<Customer> customerList = new ArrayList<>();
        String query = "SELECT * FROM customers";

        try {
            DBConnection dbConnection = DBConnection.getInstance();
            if (dbConnection.isConnectionValid()) {
                try (Connection conn = dbConnection.getConnection();
                     PreparedStatement ps = conn.prepareStatement(query);
                     ResultSet rs = ps.executeQuery()) {

                    while (rs.next()) {
                        Customer customer = new Customer();
                        customer.setCustomer_Id(rs.getString("customer_id"));
                        customer.setCustomer_name(rs.getString("customer_name"));
                        customer.setAddress(rs.getString("address"));
                        customer.setContact_number(rs.getString("contact_number"));
                        customerList.add(customer);
                    }
                }
            } else {
                System.err.println("Database connection is not valid.");
            }
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Error loading customers: " + e.getMessage());
        }

        return customerList;
    }

    public void cmbCustomerIDOnAction(ActionEvent actionEvent) {
        String selectedCustomerId = cmbCustomerID.getSelectionModel().getSelectedItem();
        setCustomerDetails(selectedCustomerId);
    }

    public void cmbItemCodeOnAction(ActionEvent actionEvent) {
        String selectedItemCode = cmbItemCode.getSelectionModel().getSelectedItem();
        setItemDetails(selectedItemCode);
    }

    public void btnAddCartOnAction(ActionEvent actionEvent) {
        String itemCode = cmbItemCode.getSelectionModel().getSelectedItem();
        String itemName = txtItemName.getText();
        int quantity = Integer.parseInt(txtQuantity.getText());
        double price = Double.parseDouble(txtPrice.getText());
        String orderId = lblOrderID.getText();
        double total = quantity * price;

        OrderItem selectedItem = new OrderItem();
        selectedItem.setItem_code(itemCode);
        selectedItem.setItem_name(itemName);
        selectedItem.setQuantity(quantity);
        selectedItem.setPrice(price);
        selectedItem.setTotal(total);

        cartItems.add(selectedItem);
        tblCart.refresh();
        updateTotal();

    }
    public void  updateTotal(){
        double total=0;
        for(OrderItem orderitem:cartItems){
            total+=orderitem.getTotal();
        }
        txtTotal.setText(String.valueOf(total));
    }


    public void btnPlaceOrderOnAction(ActionEvent actionEvent) {
        String orderId = lblOrderID.getText();
        String orderDate = getCurrentDateTime(); // Implement getCurrentDateTime() to get current date and time as a string
        double total = Double.parseDouble(txtTotal.getText()); // Get total from txtTotal
        String customerId = cmbCustomerID.getSelectionModel().getSelectedItem(); // Assuming CustomerID is an integer

        String insertOrderQuery = "INSERT INTO 'Order' (OrderID, OrderDate, Total, CustomerID) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(insertOrderQuery)) {

            ps.setString(1, orderId);
            ps.setString(2, orderDate);
            ps.setDouble(3, total);
            ps.setString(4, customerId);

            int rowsInserted = ps.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Order saved successfully.");
                saveOrderItems(orderId); // Call method to save order items
                clearForm(); // Optional: Clear form after saving
            } else {
                System.err.println("Failed to save order.");
            }
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Error saving order: " + e.getMessage());
        }

    }

    //get Current date and time
    private String getCurrentDateTime() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = currentDateTime.format(formatter);
        return formattedDateTime;

    }

    private void saveOrderItems(String orderId) {

        String insertOrderItemQuery = "INSERT INTO 'OrderItem' (OrderID, Item_code, Item_name, Quantity, Price, Total) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(insertOrderItemQuery)) {

            for (OrderItem orderItem : cartItems) {
                ps.setString(1, orderId);
                ps.setString(2, orderItem.getItem_code());
                ps.setString(3, orderItem.getItem_name());
                ps.setInt(4, orderItem.getQuantity());
                ps.setDouble(5, orderItem.getPrice());
                ps.setDouble(6, orderItem.getTotal());
                ps.addBatch();
            }

            int[] rowsInserted = ps.executeBatch();
            System.out.println("Inserted " + rowsInserted.length + " order items.");

        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Error saving order items: " + e.getMessage());
        }
    }

    private void clearForm() {
        // Clear UI fields as needed
        txtTotal.clear();
        cartItems.clear();
        tblCart.refresh();
        // Clear other form fields as needed
    }
}
