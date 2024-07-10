package com.oragan.posSystem.controller;

import com.oragan.posSystem.entity.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;


import java.net.URL;
import java.util.ResourceBundle;

public class SearchTextFormCustomerController implements Initializable {
    @FXML
    private TextField txtCustomerName;

    @FXML
    private TableView<Customer> CustomerNametbl;

    @FXML
    private TableColumn<Customer, String> colId;

    @FXML
    private TableColumn<Customer, String> colName;


    public interface CustomerSelectionListener {
        void onCustomerSelected(Customer customer);
    }

    private CustomerSelectionListener customerSelectionListener;

    private ObservableList<Customer> customerNamesList = FXCollections.observableArrayList();

    public void setCustomerSelectionListener(CustomerSelectionListener listener) {
        this.customerSelectionListener = listener;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colId.setCellValueFactory(new PropertyValueFactory<>("customer_Id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("customer_name"));
        CustomerNametbl.setItems(customerNamesList);

        txtCustomerName.textProperty().addListener((observable, oldValue, newValue) -> filterTable(newValue));

        CustomerNametbl.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) { // Double click
                Customer selectedCustomer = CustomerNametbl.getSelectionModel().getSelectedItem();
                if (selectedCustomer != null && customerSelectionListener != null) {
                    customerSelectionListener.onCustomerSelected(selectedCustomer);
                }
            }
        });
    }

    public void searchCustomerByName() {
        String searchText = txtCustomerName.getText().trim();
        if (searchText.isEmpty()) {
            CustomerNametbl.setItems(customerNamesList);
            return;
        }

        filterTable(searchText);
    }

    private void filterTable(String searchText) {
        if (searchText == null || searchText.isEmpty()) {
            CustomerNametbl.setItems(customerNamesList);
            return;
        }

        ObservableList<Customer> filteredList = FXCollections.observableArrayList();
        for (Customer customer : customerNamesList) {
            if (customer.getCustomer_name().toLowerCase().contains(searchText.toLowerCase())) {
                filteredList.add(customer);
            }
        }

        CustomerNametbl.setItems(filteredList);
    }

    public void setCustomerNamesList(ObservableList<Customer> customerNamesList) {
        this.customerNamesList = customerNamesList;
        CustomerNametbl.setItems(customerNamesList);
    }
}
