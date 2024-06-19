
package com.oragan.posSystem.controller;

import com.oragan.posSystem.db.DBConnection;
import com.oragan.posSystem.entity.Customer;
import javafx.event.ActionEvent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PurchaseOrderFormController {
    public ComboBox <String> cmbCustomerID;
    public TextField txtCustomerName;
    public TextField txtContactNumber;
    public ComboBox cmbItemCode;
    public TextField txtItemName;
    public TextField txtQtyOnHand;
    public TextField txtQty;
    public TextField txtCustomerAddress;
    private List<Customer> customers;



    public void initialize(){
        // Fetch customer data
        customers = getAllCustomers();

        // Populate combo box with customer IDs
        for (Customer customer : customers) {
            cmbCustomerID.getItems().add(customer.getCustomer_Id());
        }

        // Set default customer details if available
        if (!customers.isEmpty()) {
            cmbCustomerID.getSelectionModel().selectFirst();
            setCustomerDetails(customers.get(0).getCustomer_Id());
        }
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

        try (Connection conn = DBConnection.getInstance().getConnection();
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
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return customerList;
    }

    public void cmbCustomerIDOnAction(ActionEvent actionEvent) {
        String selectedCustomerId =  cmbCustomerID.getSelectionModel().getSelectedItem();
        setCustomerDetails(selectedCustomerId);
    }

    public void cmbItemCodeOnAction(ActionEvent actionEvent) {
    }
}
