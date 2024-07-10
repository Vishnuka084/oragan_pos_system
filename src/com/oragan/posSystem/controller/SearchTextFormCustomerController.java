package com.oragan.posSystem.controller;

import com.oragan.posSystem.entity.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Vishnuka Yahan
 *
 * @date : 7/10/2024
 * @project : oragan_pos_system
 */
public class SearchTextFormCustomerController implements Initializable {
    @FXML
    private TextField txtCustomerName;

    @FXML
    private TableView<Customer> CustomerNametbl;

    @FXML
    private TableColumn<Customer, String> colId;

    @FXML
    private TableColumn<Customer, String> colName;

    private ObservableList<Customer> customerNamesList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colId.setCellValueFactory(new PropertyValueFactory<>("customer_id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("customer_name"));
        CustomerNametbl.setItems(customerNamesList);
    }


    @FXML
    public void searchCustomerByName() {
        String searchText = txtCustomerName.getText().trim();
        if (searchText.isEmpty()) {
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
}
