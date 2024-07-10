package com.oragan.posSystem.controller;

import com.oragan.posSystem.db.DBConnection;
import com.oragan.posSystem.entity.Customer;
import com.oragan.posSystem.entity.Item;
import com.oragan.posSystem.entity.OrderItem;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public TableColumn colStatus;
    public Button btnOrderHold;
    public TableColumn <OrderItem,OrderItem>colOptions;
    private List<Customer> customers;
    private List<Item> items;
    private ObservableList<OrderItem> cartItems;
    

    public void initialize() {

        customers = getAllCustomers();
        for (Customer customer : customers) {
            cmbCustomerID.getItems().add(customer.getCustomer_Id());
        }
        cmbCustomerID.getSelectionModel();
        setCustomerDetails(null); // or handle it as per your logic

        // Add a listener to handle selection changes
        cmbCustomerID.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                // Handle the case where no customer is selected
                clearCustomerDetails();
            } else {
                setCustomerDetails(newValue);
            }
        });
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
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        initializeTableColumns();




    }

    private void clearCustomerDetails() {
        txtCustomerName.clear();
        txtCustomerAddress.clear();
        txtContactNumber.clear();

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

    //status



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
        String newStatus = btnOrderHold.getText().equals("Hold") ? "Hold" : "Confirm";
        int quantity = Integer.parseInt(txtQuantity.getText());
        double price = Double.parseDouble(txtPrice.getText());
        double total = quantity * price;

        // Validate if quantity is available
        int qtyOnHand = Integer.parseInt(txtQtyOnHand.getText());
        if (quantity > qtyOnHand) {
            showAlert(Alert.AlertType.ERROR, "Error", "Insufficient quantity available.");
            return;
        }

        // Create the OrderItem object
        OrderItem selectedItem = new OrderItem();
        selectedItem.setItem_code(itemCode);
        selectedItem.setItem_name(itemName);
        selectedItem.setQuantity(quantity);
        selectedItem.setPrice(price);
        selectedItem.setTotal(total);
        selectedItem.setStatus(newStatus);

        // Update the quantity preview in the text field
        txtQtyOnHand.setText(String.valueOf(qtyOnHand - quantity));

        // Add item to cart
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
        double total = Double.parseDouble(txtTotal.getText());
        String customerId = cmbCustomerID.getSelectionModel().getSelectedItem();
        String customername = txtCustomerName.getText();
        String Status=btnOrderHold.getText();
        String currentDate = getCurrentDateTime();
        String issueDate = getCurrentDateTime();

        String insertOrderQuery = "INSERT INTO 'Order' (OrderID, Total, Customer_name, Customer_Id, Status, Current_Date, Issue_Date) VALUES (?, ?, ?, ? ,?, ? ,?)";
        String updateItemQuantityQuery = "UPDATE items SET qty = qty - ? WHERE Item_code = ?";
        String deleteOrderItemsQuery = "DELETE FROM OrderItem WHERE OrderID = ?";

        try (Connection conn = DBConnection.getInstance().getConnection()) {
            // Start transaction
            conn.setAutoCommit(false);

            try (PreparedStatement psOrder = conn.prepareStatement(insertOrderQuery);
                 PreparedStatement psUpdateItem = conn.prepareStatement(updateItemQuantityQuery);
                 PreparedStatement psDeleteOrderItems = conn.prepareStatement(deleteOrderItemsQuery) ) {

                // Insert order
                psOrder.setString(1, orderId);
                psOrder.setDouble(2, total);
                if (customerId != null && !customerId.isEmpty()) {
                    psOrder.setString(3, customerId);
                } else {
                    psOrder.setNull(3, java.sql.Types.VARCHAR);
                }

                if (customername != null && !customername.isEmpty()) {
                    psOrder.setString(4, customername);
                } else {
                    psOrder.setNull(4, java.sql.Types.VARCHAR);
                }

                psOrder.setString(5, Status);
                psOrder.setString(6, currentDate);
                psOrder.setString(7, issueDate);

                System.out.println(psOrder);






                int rowsInsertedOrder = psOrder.executeUpdate();
                if (rowsInsertedOrder <= 0) {
                    throw new SQLException("Failed to save order.");
                }

                // Clear existing order items
                psDeleteOrderItems.setString(1, orderId);
                psDeleteOrderItems.executeUpdate();


                // Update item quantities and prepare order items insertion
                for (OrderItem orderItem : cartItems) {
                    psUpdateItem.setInt(1, orderItem.getQuantity());
                    psUpdateItem.setString(2, orderItem.getItem_code());
                    int rowsUpdatedItem = psUpdateItem.executeUpdate();
                    if (rowsUpdatedItem <= 0) {
                        throw new SQLException("Failed to update item quantity.");
                    }
                }

                // Save order items
                saveOrderItems(conn, orderId);

                // Commit transaction
                conn.commit();
                System.out.println("Order saved successfully.");
                showAlert(Alert.AlertType.INFORMATION, "Success", "Order updated successfully." + orderId);

                // Generate Jasper Report
                generateJasperReport(orderId);
                clearForm();

            } catch (SQLException e) {
                conn.rollback();
                System.err.println("Transaction rolled back: " + e.getMessage());
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to save order: " + e.getMessage());
            } finally {
                conn.setAutoCommit(true);
            }

        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Error saving order: " + e.getMessage());
            showAlert(Alert.AlertType.ERROR, "Error", "Error saving order: " + e.getMessage());
        }
    }

    //get Current date and time
    private String getCurrentDateTime() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDateTime = currentDateTime.format(formatter);
        return formattedDateTime;

    }

    private void saveOrderItems(Connection conn, String orderId) throws SQLException {
        String insertOrderItemQuery = "INSERT INTO 'OrderItem' (OrderID, Item_code, Item_name, Quantity, price, status,Total) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(insertOrderItemQuery)) {
            for (OrderItem orderItem : cartItems) {
                ps.setString(1, orderId);
                ps.setString(2, orderItem.getItem_code());
                ps.setString(3, orderItem.getItem_name());
                ps.setInt(4, orderItem.getQuantity());
                ps.setDouble(5, orderItem.getPrice());
                ps.setString(6,orderItem.getStatus());
                ps.setDouble(7, orderItem.getTotal());
                ps.addBatch();
            }

            int[] rowsInserted = ps.executeBatch();
            if (rowsInserted.length != cartItems.size()) {
                throw new SQLException("Failed to insert all order items.");
            }

            System.out.println("Inserted " + rowsInserted.length + " order items.");
        }
    }

    private void clearForm() {
        // Clear UI fields as needed
        txtTotal.clear();
        cartItems.clear();
        tblCart.refresh();
        // Clear other form fields as needed
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void generateJasperReport(String orderId) {
        try {
            // Verify the path
            String resourcePath = "/com/oragan/posSystem/view/reports/OraganJasper.jrxml";
            InputStream reportStream = getClass().getResourceAsStream(resourcePath);

            if (reportStream == null) {
                System.err.println("Report file not found at: " + resourcePath);
                throw new RuntimeException("Report file not found");
            } else {
                System.out.println("Report file found at: " + resourcePath);
            }

            JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);

            // Set parameters
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("orderId", orderId);

            // Fill the report with data
            Connection conn = DBConnection.getInstance().getConnection();
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);

            // View the report
            JasperViewer.viewReport(jasperPrint, false);
        } catch (JRException | SQLException | ClassNotFoundException e) {
            System.err.println("Error generating Jasper report: " + e.getMessage());
            showAlert(Alert.AlertType.ERROR, "Error", "Error generating report: " + e.getMessage());
        }
    }


    public void btnHoldOrderOnAction(ActionEvent actionEvent) {
        // Toggle status of each OrderItem in the cart between "Hold" and "Confirm"
        for (OrderItem orderItem : cartItems) {
            String currentStatus = orderItem.getStatus();
            if (currentStatus.equals("Hold")) {
                orderItem.setStatus("Confirm");
            } else if (currentStatus.equals("Confirm")) {
                orderItem.setStatus("Hold");
            }
        }

        // Refresh the table to show the updated status
        tblCart.refresh();

        // Update the button text based on the new status of the first item (assuming consistent status for all items in the cart)
        if (!cartItems.isEmpty()) {
            String newStatus = cartItems.get(0).getStatus().equals("Hold") ? "Confirm" : "Hold";
            btnOrderHold.setText(newStatus);
        }
    }
    //Option Coloumn set Table

    private void initializeTableColumns () {
        colItemCode.setCellValueFactory(new PropertyValueFactory<>("item_code"));
        colItemName.setCellValueFactory(new PropertyValueFactory<>("item_name"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));


        colOptions.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        colOptions.setCellFactory(param -> new TableCell<OrderItem, OrderItem>() {
            private final Button updateButton = new Button();
            private final Button deleteButton = new Button();

            @Override
            protected void updateItem(OrderItem orderItem, boolean empty) {
                super.updateItem(orderItem, empty);
                if (orderItem == null) {
                    setGraphic(null);
                    return;
                }

                // Load the update icon image
                Image updateImage = new Image(getClass().getResourceAsStream("/com/oragan/posSystem/assets/icons8-update-64.png"));
                if (updateImage != null) {
                    ImageView updateImageView = new ImageView(updateImage);
                    updateImageView.setFitWidth(20); // Adjust the size as needed
                    updateImageView.setFitHeight(20); // Adjust the size as needed
                    updateButton.setGraphic(updateImageView);
                }

                // Load the delete icon image
                Image deleteImage = new Image(getClass().getResourceAsStream("/com/oragan/posSystem/assets/icons8-delete-120.png"));
                if (deleteImage != null) {
                    ImageView deleteImageView = new ImageView(deleteImage);
                    deleteImageView.setFitWidth(20); // Adjust the size as needed
                    deleteImageView.setFitHeight(20); // Adjust the size as needed
                    deleteButton.setGraphic(deleteImageView);
                }

                HBox hBox = new HBox(updateButton, deleteButton);
                hBox.setAlignment(Pos.CENTER);
                hBox.setSpacing(10);
                setGraphic(hBox);

                updateButton.setOnAction(event -> handleUpdateOrder(orderItem));
                deleteButton.setOnAction(event -> handleDeleteOrderItem(orderItem));
            }
        });

        tblCart.setItems(cartItems);
    }

    private void handleUpdateOrder(OrderItem orderItem) {
        // Get the current quantity on hand for the selected item
        int currentQtyOnHand = Integer.parseInt(txtQtyOnHand.getText());

        // Calculate the difference in quantity
        int oldQuantity = orderItem.getQuantity();
        int newQuantity = Integer.parseInt(txtQuantity.getText());
        int quantityDifference = newQuantity - oldQuantity;

        // Validate if the new quantity is available
        if (quantityDifference > currentQtyOnHand) {
            showAlert(Alert.AlertType.ERROR, "Error", "Insufficient quantity available.");
            return;
        }

        // Update the quantity on hand preview in the text field
        txtQtyOnHand.setText(String.valueOf(currentQtyOnHand - quantityDifference));

        // Update the order item with new values
        String newItemCode = cmbItemCode.getValue();
        String newItemName = txtItemName.getText();
        double newPrice = Double.parseDouble(txtPrice.getText());

        orderItem.setItem_code(newItemCode);
        orderItem.setItem_name(newItemName);
        orderItem.setQuantity(newQuantity);
        orderItem.setPrice(newPrice);
        orderItem.setTotal(newQuantity * newPrice); // Assuming total is calculated as quantity * price

        // Refresh the table to show updated values
        tblCart.refresh();
        updateTotal();
    }

    private void handleDeleteOrderItem(OrderItem orderItem) {
        // Remove the selected order item from the cart
        cartItems.remove(orderItem);
        tblCart.refresh();
        updateTotal();
    }


    public void handleRowClick(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 1) { // Check if single click
            OrderItem selectedOrderItem = (OrderItem) tblCart.getSelectionModel().getSelectedItem();
            if (selectedOrderItem != null) {
                // Populate data fields with selected order item details
                cmbItemCode.setValue(selectedOrderItem.getItem_code());
                txtItemName.setText(selectedOrderItem.getItem_name());
                txtQuantity.setText(String.valueOf(selectedOrderItem.getQuantity()));
                txtPrice.setText(String.valueOf(selectedOrderItem.getPrice()));
            }
        }
    }

    public void SearchTxtNameFormOnAction(MouseEvent mouseEvent) throws IOException {
        URL resource = this.getClass().getResource("/com/oragan/posSystem/view/SearchTextFormCustomer.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(resource);
        Parent load = fxmlLoader.load();
        SearchTextFormCustomerController searchTextFormCustomerController = fxmlLoader.getController();

        // Set customer list in the search form controller
        ObservableList<Customer> customerList = FXCollections.observableArrayList(getAllCustomers());
        searchTextFormCustomerController.setCustomerNamesList(customerList);

        // Set the customer selection listener
        searchTextFormCustomerController.setCustomerSelectionListener(this::handleRowClick);
        Stage stage = new Stage();
        stage.setScene(new Scene(load));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("SearchTxt Name Form");
        stage.centerOnScreen();
        stage.show();

    }

    private void handleRowClick(Customer customer) {
        if (customer != null) {
            cmbCustomerID.setValue(customer.getCustomer_Id());
            txtCustomerName.setText(customer.getCustomer_name());
            txtCustomerAddress.setText(customer.getAddress());
            txtContactNumber.setText(customer.getContact_number());
        }
    }


}
