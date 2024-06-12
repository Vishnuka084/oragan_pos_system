package com.oragan.posSystem.controller;

import com.oragan.posSystem.db.DBConnection;
import com.oragan.posSystem.entity.Item;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.sql.*;

public class AddItemFormController {

    public AnchorPane addItemFormContext;
    public TextField txtItemCode;
    public TextField txtItemName;
    public TextField txtItemPrice;
    public TextField txtItemQty;
    private TableView<Item> tblItem;
    private ItemFormController itemFormController;
    private Item itemToUpdate;  // Track the item being updated

    public void initialize() {
        // If itemToUpdate is not set, it's an add operation
        if (itemToUpdate == null) {
            setNewItemCode();
        }
    }

    public void initForUpdate(TableView<Item> tblItem, ItemFormController itemFormController, Item item) {
        this.tblItem = tblItem;
        this.itemFormController = itemFormController;
        this.itemToUpdate = item;

        // Pre-fill the form fields with item data
        txtItemCode.setText(item.getItem_code());
        txtItemName.setText(item.getItem_name());
        txtItemPrice.setText(String.valueOf(item.getPrice()));
        txtItemQty.setText(String.valueOf(item.getQty()));
        txtItemCode.setEditable(false);  // Disable editing of code during update
    }

    public void init(TableView<Item> tblItem, ItemFormController itemFormController) {
        this.tblItem = tblItem;
        this.itemFormController = itemFormController;
    }

    private void setNewItemCode() {
        String newItemCode = generateNewItemCode();
        if (newItemCode != null) {
            txtItemCode.setText(newItemCode);
        } else {
            System.out.println("Error generating new item code.");
        }
    }

    public void clearBtnOnAction(ActionEvent actionEvent) {
        txtItemName.clear();
        txtItemPrice.clear();
        txtItemQty.clear();
        resetFieldStyles();

        if (itemToUpdate == null) {
            setNewItemCode();
        } else {
            txtItemCode.setText(itemToUpdate.getItem_code());
        }
    }

    public void saveBtnOnAction(ActionEvent actionEvent) {
        if (!validateInput()) {
            return;
        }

        if (itemToUpdate != null) {
            updateItem();
        } else {
            addNewItem();
        }
    }

    private void addNewItem() {
        String newItemCode = txtItemCode.getText();
        String sql = "INSERT INTO items(Item_code, Item_name, price, qty) VALUES(?,?,?,?)";
        try {
            Connection conn = DBConnection.getInstance().getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, newItemCode);
                pstmt.setString(2, txtItemName.getText());
                pstmt.setDouble(3, Double.parseDouble(txtItemPrice.getText()));
                pstmt.setInt(4, Integer.parseInt(txtItemQty.getText()));
                pstmt.executeUpdate();
                showAlert(Alert.AlertType.INFORMATION, "Success", "Item added successfully.");
                itemFormController.refreshItemData();
                clearBtnOnAction(null);
                setNewItemCode();
            }
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Error adding item to the database: " + e.getMessage());
        }
    }

    private void updateItem() {
        String sql = "UPDATE items SET Item_name=?, price=?, qty=? WHERE Item_code=?";
        try {
            Connection conn = DBConnection.getInstance().getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, txtItemName.getText());
                pstmt.setDouble(2, Double.parseDouble(txtItemPrice.getText()));
                pstmt.setInt(3, Integer.parseInt(txtItemQty.getText()));
                pstmt.setString(4, txtItemCode.getText());
                pstmt.executeUpdate();
                showAlert(Alert.AlertType.INFORMATION, "Success", "Item updated successfully.");
                itemFormController.refreshItemData();
                ((Stage) addItemFormContext.getScene().getWindow()).close();
            }
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Error updating item in the database: " + e.getMessage());
        }
    }

    private String generateNewItemCode() {
        String sql = "SELECT Item_code FROM items ORDER BY Item_code DESC LIMIT 1";
        try {
            Connection conn = DBConnection.getInstance().getConnection();
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                if (rs.next()) {
                    String lastCode = rs.getString("Item_code");
                    if (lastCode != null && lastCode.length() > 1 && lastCode.startsWith("I")) {
                        String numericPartStr = lastCode.substring(1);
                        try {
                            int numericPart = Integer.parseInt(numericPartStr) + 1;
                            return String.format("I%03d", numericPart);
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid numeric part in the last item code: " + numericPartStr);
                            return null;
                        }
                    } else {
                        System.out.println("Invalid format of the last item code: " + lastCode);
                        return null;
                    }
                } else {
                    return "I001";
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Error fetching last item code: " + e.getMessage());
            return null;
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean validateInput() {
        boolean valid = true;
        resetFieldStyles();

        if (!txtItemName.getText().matches("^[A-Z][a-zA-Z0-9\\s]*$")) {
            txtItemName.setStyle("-fx-border-color: red;");
            Tooltip tooltip = new Tooltip("Name should start with a capital letter and contain only letters, numbers, and spaces.");
            Tooltip.install(txtItemName, tooltip);
            valid = false;
        } else {
            txtItemName.setStyle("-fx-border-color: #2aff2a;");
        }

        if (!txtItemPrice.getText().matches("^\\d+(\\.\\d{1,2})?$")) {
            txtItemPrice.setStyle("-fx-border-color: red;");
            Tooltip tooltip = new Tooltip("Price should be a valid number with up to 2 decimal places.");
            Tooltip.install(txtItemPrice, tooltip);
            valid = false;
        } else {
            txtItemPrice.setStyle("-fx-border-color: #2aff2a;");
        }

        if (!txtItemQty.getText().matches("^\\d+$")) {
            txtItemQty.setStyle("-fx-border-color: red;");
            Tooltip tooltip = new Tooltip("Quantity should be a valid whole number.");
            Tooltip.install(txtItemQty, tooltip);
            valid = false;
        } else {
            txtItemQty.setStyle("-fx-border-color: #2aff2a;");
        }

        if (!valid) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Please correct the input fields.");
        }

        return valid;
    }

    private void resetFieldStyles() {
        txtItemName.setStyle(null);
        txtItemPrice.setStyle(null);
        txtItemQty.setStyle(null);
    }

    public void clearbtnOnAction(ActionEvent actionEvent) {
    }
}
