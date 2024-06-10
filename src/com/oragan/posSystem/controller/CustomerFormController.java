
package com.oragan.posSystem.controller;

import com.oragan.posSystem.db.DBConnection;
import com.oragan.posSystem.entity.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CustomerFormController {


    public AnchorPane context;
    public AnchorPane addCustomerFormContext;
    public TableView tblCustomer;
    public TableColumn tblCustomerIdField;
    public TableColumn tblCustomerNameFIeld;
    public TableColumn tblCustomerAddressField;
    public TableColumn tblCustomerContactNoField;
    private ObservableList<Customer> customerList = FXCollections.observableArrayList();

    public void initialize() {
        loadCustomerData();
        initializeTableColumns();

    }


    public void btnAddCustomerFormOnAction(ActionEvent actionEvent) throws IOException {
        URL resource = this.getClass().getResource("/com/oragan/posSystem/view/AddCustomerForm.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(resource);
        Parent load = fxmlLoader.load();
         AddCustomerFormController AddCustomerFormController = fxmlLoader.getController();
         AddCustomerFormController.init(tblCustomer,this);
        Stage stage = new Stage();
        stage.setScene(new Scene(load));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Add Customer Form");
        stage.centerOnScreen();
        stage.show();
    }



    private void initializeTableColumns() {
        tblCustomerIdField.setCellValueFactory(new PropertyValueFactory<>("customer_Id"));
        tblCustomerNameFIeld.setCellValueFactory(new PropertyValueFactory<>("Customer_name"));
        tblCustomerAddressField.setCellValueFactory(new PropertyValueFactory<>("Address"));
        tblCustomerContactNoField.setCellValueFactory(new PropertyValueFactory<>("contact_number"));
        tblCustomer.setItems(customerList);
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
        } catch (ClassNotFoundException  | SQLException e) {
            System.out.println("Error loading customer data from the database: " + e.getMessage());
        }
    }

}
