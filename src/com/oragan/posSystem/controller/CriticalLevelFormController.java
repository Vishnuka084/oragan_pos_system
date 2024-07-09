/**
 * authour By Pamindu Nawodya
 * Date:7/9/2024
 * Time:12:14 AM
 * Project Name:oragan_pos_system
 */
package com.oragan.posSystem.controller;
import com.oragan.posSystem.entity.Item;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;


public class CriticalLevelFormController {

    public AnchorPane contextItemPopUp;
    public TableColumn<Item, String> colItemCode;
    public TableColumn<Item, String> colItemName;
    public TableColumn<Item, Integer> colQtyOnHand;
    public TableView<Item> tblCriticalItems;

    private ObservableList<String> itemCodes = FXCollections.observableArrayList();
    private ObservableList<Item> itemList = FXCollections.observableArrayList();

    public void initialize() {
        colItemCode.setCellValueFactory(new PropertyValueFactory<>("Item_code"));
        colItemName.setCellValueFactory(new PropertyValueFactory<>("item_name"));
        colQtyOnHand.setCellValueFactory(new PropertyValueFactory<>("qty"));
    }

    public void setItemsData(ObservableList<Item> items) {
        this.itemList = items;
        loadCriticalItemsData();
    }

    private void loadCriticalItemsData() {
        ObservableList<Item> criticalItems = FXCollections.observableArrayList();
        for (Item item : itemList) {
            if (item.getQty() <= item.getCritical_level()) {
                criticalItems.add(item);
            }
        }
        tblCriticalItems.setItems(criticalItems);
    }
}
