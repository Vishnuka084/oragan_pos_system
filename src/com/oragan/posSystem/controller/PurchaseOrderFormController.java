package com.oragan.posSystem.controller;

import com.oragan.posSystem.db.DBConnection;
import com.oragan.posSystem.entity.Customer;
import com.oragan.posSystem.entity.Item;
import javafx.event.ActionEvent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
    private List<Customer> customers;
    private List<Item> items;

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

    public void PlaceOderBillOnEvent(MouseEvent mouseEvent) {
        System.out.println("HEllo Jasperf 01 ");
        try {

            System.out.println("print coming  ");
            JasperDesign load = JRXmlLoader.load(this.getClass().getResourceAsStream("/view/reports/OraganJasper.jrxml"));
            System.out.println("Report view ");
            JasperReport compileReport = JasperCompileManager.compileReport(load);

            JasperPrint jasperPrint = JasperFillManager.fillReport(compileReport, null, new JREmptyDataSource(1));

            JasperViewer.viewReport(jasperPrint,false);


        } catch (JRException e) {
            e.printStackTrace();
        }
    }


}
