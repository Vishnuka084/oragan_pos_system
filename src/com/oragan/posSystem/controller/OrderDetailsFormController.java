
package com.oragan.posSystem.controller;

import com.oragan.posSystem.db.DBConnection;
import com.oragan.posSystem.entity.Order;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;


public class OrderDetailsFormController {
    public TableView<Order> tblOrder;
    public TableColumn<Order, String> colOrderID;
    public TableColumn<Order, String> colCustomerID;
    public TableColumn<Order, Date>  colCurrentDate;
    public TableColumn <Order, Date> colIssueDate;
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
        colCurrentDate.setCellValueFactory(new PropertyValueFactory<>("Current_Date"));
        colIssueDate.setCellValueFactory(new PropertyValueFactory<>("Issue_Date"));
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

        String query = " SELECT OrderID, Total, Customer_name, Customer_Id, Status, Current_Date, Issue_Date FROM 'Order';";

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String orderID = rs.getString("OrderID");
                double total = rs.getDouble("Total");
                String customerID = rs.getString("Customer_Id");
                String customerName = rs.getString("Customer_name");
                String status = rs.getString("Status");
                String currentDate = rs.getString("Current_Date");
                String issueDate=rs.getString("Issue_Date");



                System.out.println(issueDate);
                System.out.println(currentDate);
                System.out.println(orderID);


                Order order = new Order(orderID, total, customerID, customerName, status, currentDate, issueDate);
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
        colCurrentDate.setCellValueFactory(new PropertyValueFactory<>("Current_Date"));
        colIssueDate.setCellValueFactory(new PropertyValueFactory<>("Issue_Date"));

        colOptions.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        colOptions.setCellFactory(param -> new TableCell<Order, Order>() {

            private final Button updateButton = new Button();
            private final Button deleteButton = new Button();

            @Override
            protected void updateItem(Order order, boolean empty) {
                super.updateItem(order, empty);

                if (order == null) {
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

                HBox hBox = new HBox(deleteButton,updateButton);
                hBox.setAlignment(Pos.CENTER);
                hBox.setSpacing(10);
                setGraphic(hBox);

                deleteButton.setOnAction(event -> {
                    // Handle the delete action
                    handleDeleteOrder(order);
                });

                updateButton.setOnAction(event -> {
                    // Handle the update action
                    openUpdateForm(order);
                });
            }
        });

        tblOrder.setItems(orderList);
    }

    private void openUpdateForm(Order order) {
        System.out.println("order details update'");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/oragan/posSystem/view/OderDetailsUpdateForm.fxml"));
            AnchorPane root = loader.load();

            OderDetailsUpdateFormController controller = loader.getController();
            controller.setOrderDetails(order);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Update Order Details");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            // Refresh the order list after the update form is closed
            fetchOrdersFromDatabase();

        } catch (IOException e) {
            e.printStackTrace(); // Handle or log exception
        }
    }

    private void handleDeleteOrder(Order order) {
        // Remove the order from the orderList
        orderList.remove(order);

        // SQL queries to delete from OrderItem and Order tables
        String deleteOrderItemQuery = "DELETE FROM OrderItem WHERE OrderID = ?";
        String deleteOrderQuery = "DELETE FROM \"Order\" WHERE OrderID = ?";

        // Using try-with-resources to manage the database connection and statements
        try (Connection conn = DBConnection.getInstance().getConnection()) {
            // Start transaction
            conn.setAutoCommit(false);

            try (PreparedStatement psOrderItem = conn.prepareStatement(deleteOrderItemQuery);
                 PreparedStatement psOrder = conn.prepareStatement(deleteOrderQuery)) {

                // Set parameters for the OrderItem deletion query
                psOrderItem.setString(1, order.getOrderID());
                psOrderItem.executeUpdate();

                // Set parameters for the Order deletion query
                psOrder.setString(1, order.getOrderID());
                psOrder.executeUpdate();

                // Commit the transaction
                conn.commit();
            } catch (SQLException e) {
                // Rollback the transaction in case of error
                conn.rollback();
                e.printStackTrace(); // Handle or log exception
            } finally {
                // Restore auto-commit mode
                conn.setAutoCommit(true);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace(); // Handle or log exception
        }
    }


}


