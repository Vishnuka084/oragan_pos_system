package com.oragan.posSystem.controller;


import com.oragan.posSystem.entity.Item;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Vishnuka Yahan
 *
 * @author : Vishnuka Yahan
 * @data : 7/11/2024
 * @project : oragan_pos_system
 */
public class SearchTextFromItemController implements Initializable {
    @FXML
    private TextField txtItemName;

    @FXML
    private TableView<Item> ItemtableName;

    @FXML
    private TableColumn<Item, String> colItemCode;

    @FXML
    private TableColumn<Item, String> colItemName;


    public interface ItemSelectionListener {
        void onItemSelected(Item item);
    }

    private ItemSelectionListener itemSelectionListener;

    private ObservableList<Item> itemNamesList = FXCollections.observableArrayList();

    public void setItemSelectionListener(ItemSelectionListener listener) {
        this.itemSelectionListener = listener;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colItemCode.setCellValueFactory(new PropertyValueFactory<>("Item_code"));
        colItemName.setCellValueFactory(new PropertyValueFactory<>("Item_name"));
        ItemtableName.setItems(itemNamesList);

        txtItemName.textProperty().addListener((observable, oldValue, newValue) -> filterTable(newValue));

        ItemtableName.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) { // Double click
                Item selectedItem = ItemtableName.getSelectionModel().getSelectedItem();
                if (selectedItem != null && itemSelectionListener != null) {
                    itemSelectionListener.onItemSelected(selectedItem);
                }
            }
        });
    }

    public void searchItemByName() {
        String searchText = txtItemName.getText().trim();
        if (searchText.isEmpty()) {
            ItemtableName.setItems(itemNamesList);
            return;
        }

        filterTable(searchText);
    }

    private void filterTable(String searchText) {
        if (searchText == null || searchText.isEmpty()) {
            ItemtableName.setItems(itemNamesList);
            return;
        }

        ObservableList<Item> filteredList = FXCollections.observableArrayList();
        for (Item item : itemNamesList) {
            if (item.getItem_name().toLowerCase().contains(searchText.toLowerCase())) {
                filteredList.add(item);
            }
        }

        ItemtableName.setItems(filteredList);
    }

    public void setItemNamesList(ObservableList<Item> itemNamesList) {
        this.itemNamesList = itemNamesList;
        ItemtableName.setItems(itemNamesList);
    }
}