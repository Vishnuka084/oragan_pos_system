package com.oragan.posSystem.controller;

import com.oragan.posSystem.db.DBConnection;
import com.oragan.posSystem.entity.Customer;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CustomerFormController {
    public AnchorPane context;
    public AnchorPane addCustomerFormContext;
    public TableView<Customer> tblCustomer;
    public TableColumn<Customer, String> tblCustomerIdField;
    public TableColumn<Customer, String> tblCustomerNameFIeld;
    public TableColumn<Customer, String> tblCustomerAddressField;
    public TableColumn<Customer, String> tblCustomerContactNoField;
    public TableColumn<Customer, Customer> tblOptionsColumn;

    private ObservableList<Customer> customerList = FXCollections.observableArrayList();

    public void initialize() {
        loadCustomerData();
        initializeTableColumns();
    }

    public void btnAddCustomerFormOnAction(ActionEvent actionEvent) throws IOException {
        URL resource = this.getClass().getResource("/com/oragan/posSystem/view/AddCustomerForm.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(resource);
        Parent load = fxmlLoader.load();
        AddCustomerFormController addCustomerFormController = fxmlLoader.getController();
        addCustomerFormController.init(tblCustomer, this);
        Stage stage = new Stage();
        stage.setScene(new Scene(load));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Add Customer Form");
        stage.centerOnScreen();
        stage.show();
    }

    private void initializeTableColumns() {
        tblCustomerIdField.setCellValueFactory(new PropertyValueFactory<>("Customer_Id"));
        tblCustomerNameFIeld.setCellValueFactory(new PropertyValueFactory<>("Customer_name"));
        tblCustomerAddressField.setCellValueFactory(new PropertyValueFactory<>("address"));
        tblCustomerContactNoField.setCellValueFactory(new PropertyValueFactory<>("contact_number"));

        tblOptionsColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        tblOptionsColumn.setCellFactory(param -> new TableCell<Customer, Customer>() {
            private final Button updateButton = new Button();
            private final Button deleteButton = new Button();

            @Override
            protected void updateItem(Customer customer, boolean empty) {
                super.updateItem(customer, empty);
                if (customer == null) {
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

                updateButton.setOnAction(event -> handleUpdateCustomer(customer));
                deleteButton.setOnAction(event -> handleDeleteCustomer(customer));
            }
        });

        tblCustomer.setItems(customerList);
    }

    private void handleUpdateCustomer(Customer customer) {
        try {
            URL resource = this.getClass().getResource("/com/oragan/posSystem/view/AddCustomerForm.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader(resource);
            Parent load = fxmlLoader.load();
            AddCustomerFormController addCustomerFormController = fxmlLoader.getController();
            addCustomerFormController.initForUpdate(tblCustomer, this, customer);
            Stage stage = new Stage();
            stage.setScene(new Scene(load));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Update Customer Form");
            stage.centerOnScreen();
            stage.show();
        } catch (IOException e) {
            System.out.println("Error loading update customer form: " + e.getMessage());
        }
    }

    private void loadCustomerData() {
        customerList.clear();
        String sql = "SELECT * FROM customers";
        try {
            Connection conn = DBConnection.getInstance().getConnection();
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    String customerId = rs.getString("Customer_Id");
                    String customerName = rs.getString("customer_name");
                    String customerAddress = rs.getString("address");
                    String contactNumber = rs.getString("contact_number");
                    Customer customer = new Customer(customerId, customerName, customerAddress, contactNumber);
                    customerList.add(customer);
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Error loading customer data from the database: " + e.getMessage());
        }
    }

    private void handleDeleteCustomer(Customer customer) {
        String sql = "DELETE FROM customers WHERE Customer_Id = ?";
        try {
            Connection conn = DBConnection.getInstance().getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, customer.getCustomer_Id());
                pstmt.executeUpdate();
                System.out.println("Customer deleted successfully");

                // Remove the customer from the table
                tblCustomer.getItems().remove(customer);
            }
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Error deleting customer: " + e.getMessage());
        }
    }

    public void refreshCustomerData() {
        loadCustomerData();
    }
}


//----------------------------