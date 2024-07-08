package com.oragan.posSystem.controller;

import com.oragan.posSystem.db.DBConnection;
import com.oragan.posSystem.entity.Order;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

/**
 * Created by Vishnuka Yahan
 *
 * @author : Vishnuka Yahan
 * @data : 7/6/2024
 * @project : oragan_pos_system
 */
public class OderDetailsUpdateFormController {
    public AnchorPane updateOrderFromContext;
    public TextField txtOrderId;
    public TextField txtCustomerId;
    public TextField txtCustomerName;
    public TextField txtCurrentDate;
    public TextField txtTotal;
    public RadioButton radBtnHold;
    public RadioButton radBtnConfirm;
    public DatePicker issueDateId;

    private Order currentOrder;

    public void btnClearTxtFldOnAction(ActionEvent actionEvent) {
        exiteForm();
    }

    public void setOrderDetails(Order order) {
        this.currentOrder = order;
        txtOrderId.setText(order.getOrderID());
        txtCustomerId.setText(order.getCustomer_Id());
        txtCustomerName.setText(order.getCustomer_name());
        txtCurrentDate.setText(order.getCurrent_Date());
        txtTotal.setText(String.valueOf(order.getTotal()));

        // Set radio buttons based on order status
        if (order.getStatus().equals("Hold")) {
            radBtnHold.setSelected(true);
        } else if (order.getStatus().equals("Confirmed")) {
            radBtnConfirm.setSelected(true);
        }

        // Set issue date
        LocalDate issueDate = LocalDate.parse(order.getIssue_Date());
        issueDateId.setValue(issueDate);
    }

    //------------- OderDetails Save ------------------
    public void btnOderDetailsSaveAddOnAction(ActionEvent actionEvent) {
        if (currentOrder != null) {
            try {
                updateOrderDetails();
                showAlert(Alert.AlertType.INFORMATION, "Success", "Order details updated successfully.");
            } catch (SQLException | ClassNotFoundException e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to update order details: " + e.getMessage());
            }
        }
    }

    private void exiteForm() {
        // Retrieve the current stage using any node from the scene
        Stage stage = (Stage) updateOrderFromContext.getScene().getWindow();
        stage.close();

    }

    //------------- Update OderDetails ------------------
    private void updateOrderDetails() throws SQLException, ClassNotFoundException {
        String orderId = txtOrderId.getText();
        String customerId = txtCustomerId.getText();
        String customerName = txtCustomerName.getText();
        String orderDate = txtCurrentDate.getText();
        double total = Double.parseDouble(txtTotal.getText());
        String status = radBtnHold.isSelected() ? "Hold" : radBtnConfirm.isSelected() ? "Confirmed" : "";
        String issueDate = issueDateId.getValue().toString();

        String updateQuery = "UPDATE `Order` SET Customer_Id = ?, Customer_name = ?, Current_Date = ?, Total = ?, Status = ?, Issue_Date = ? WHERE OrderID = ?";

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(updateQuery)) {
            ps.setString(1, customerId);
            ps.setString(2, customerName);
            ps.setString(3, orderDate);
            ps.setDouble(4, total);
            ps.setString(5, status);
            ps.setString(6, issueDate);
            ps.setString(7, orderId);

            ps.executeUpdate();
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
