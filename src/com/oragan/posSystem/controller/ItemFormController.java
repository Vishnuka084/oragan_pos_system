package com.oragan.posSystem.controller;

import com.oragan.posSystem.db.DBConnection;
import com.oragan.posSystem.entity.Item;
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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;

public class ItemFormController {

    public AnchorPane context;
    public TableView<Item> tblItem;
    public TableColumn<Item, String> colItemCode;
    public TableColumn<Item, String> colItemName;
    public TableColumn<Item, Double> colPrice;
    public TableColumn<Item, Integer> colQtyOnHand;
    public TableColumn<Item, Item> colOptions;
    public TextField txtSearchField;
    public ComboBox<String> cmbItemCode;

    private ObservableList<String> itemCodes = FXCollections.observableArrayList();
    private ObservableList<Item> itemList = FXCollections.observableArrayList();

    public void initialize() {
        loadItemData();
        initializeTableColumns();
        loadItemCodes();
        txtSearchField.textProperty().addListener((observable, oldValue, newValue) -> filterItemList(newValue));
    }

    public void btnAddItemFormOnAction(ActionEvent actionEvent) throws IOException {
        URL resource = this.getClass().getResource("/com/oragan/posSystem/view/AddItemForm.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(resource);
        Parent load = fxmlLoader.load();
        AddItemFormController addItemFormController = fxmlLoader.getController();
        addItemFormController.init(tblItem, this);
        Stage stage = new Stage();
        stage.setScene(new Scene(load));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Add Item Form");
        stage.centerOnScreen();
        stage.show();
    }

    private void initializeTableColumns() {
        colItemCode.setCellValueFactory(new PropertyValueFactory<>("Item_code"));
        colItemName.setCellValueFactory(new PropertyValueFactory<>("Item_name"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colQtyOnHand.setCellValueFactory(new PropertyValueFactory<>("qty"));

        colOptions.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        colOptions.setCellFactory(param -> new TableCell<Item, Item>() {
            private final Button updateButton = new Button();
            private final Button deleteButton = new Button();

            @Override
            protected void updateItem(Item item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null) {
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

                updateButton.setOnAction(event -> handleUpdateItem(item));
                deleteButton.setOnAction(event -> handleDeleteItem(item));
            }
        });

        tblItem.setItems(itemList);
    }

    private void handleUpdateItem(Item item) {
        try {
            URL resource = this.getClass().getResource("/com/oragan/posSystem/view/AddItemForm.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader(resource);
            Parent load = fxmlLoader.load();
            AddItemFormController addItemFormController = fxmlLoader.getController();
            addItemFormController.initForUpdate(tblItem, this, item);
            Stage stage = new Stage();
            stage.setScene(new Scene(load));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Update Item Form");
            stage.centerOnScreen();
            stage.show();
        } catch (IOException e) {
            System.out.println("Error loading update item form: " + e.getMessage());
        }
    }

    private void loadItemData() {
        itemList.clear();
        itemCodes.clear(); // Clear the itemCodes list as well
        String sql = "SELECT * FROM items";
        try {
            Connection conn = DBConnection.getInstance().getConnection();
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    String itemCode = rs.getString("Item_code");
                    String itemName = rs.getString("Item_name");
                    double price = rs.getDouble("price");
                    int qty = rs.getInt("qty");
                    Item item = new Item(itemCode, itemName, price, qty);
                    itemList.add(item);
                    itemCodes.add(itemCode); // Add itemCode to the itemCodes list
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Error loading item data from the database: " + e.getMessage());
        }
    }

    private void handleDeleteItem(Item item) {
        String sql = "DELETE FROM items WHERE Item_code = ?";
        try {
            Connection conn = DBConnection.getInstance().getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, item.getItem_code());
                pstmt.executeUpdate();
                System.out.println("Item deleted successfully");

                // Remove the item from the table
                tblItem.getItems().remove(item);
                itemCodes.remove(item.getItem_code()); // Remove the itemCode from the itemCodes list
            }
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Error deleting item: " + e.getMessage());
        }
    }

    public void refreshItemData() {
        loadItemData();
        loadItemCodes();
    }

    // Search item

    public void txtSearchFieldOnAction(ActionEvent actionEvent) {
        String searchQuery = txtSearchField.getText().toLowerCase();
        if (searchQuery.isEmpty()) {
            tblItem.setItems(itemList);  // Display all items if search query is empty
        } else {
            ObservableList<Item> filteredList = FXCollections.observableArrayList();
            for (Item item : itemList) {
                if (item.getItem_name().toLowerCase().contains(searchQuery)) {
                    filteredList.add(item);
                }
            }
            tblItem.setItems(filteredList);
        }
    }

    private void filterItemList(String searchQuery) {
        if (searchQuery == null || searchQuery.isEmpty()) {
            tblItem.setItems(itemList);  // Display all items if search query is empty
        } else {
            ObservableList<Item> filteredList = FXCollections.observableArrayList();
            for (Item item : itemList) {
                if (item.getItem_name().toLowerCase().contains(searchQuery.toLowerCase())) {
                    filteredList.add(item);
                }
            }
            tblItem.setItems(filteredList);
        }
    }

    // Search item codes combobox

    private void loadItemCodes() {
        cmbItemCode.setItems(itemCodes); // Bind the ComboBox with itemCodes list
    }

    public void cmbItemCodeOnAction(ActionEvent actionEvent) {
        String selectedItemCode = cmbItemCode.getValue();
        if (selectedItemCode != null) {
            ObservableList<Item> selectedItemList = FXCollections.observableArrayList();
            for (Item item : itemList) {
                if (item.getItem_code().equals(selectedItemCode)) {
                    selectedItemList.add(item);
                    break;
                }
            }
            tblItem.setItems(selectedItemList);
        }
    }
}
