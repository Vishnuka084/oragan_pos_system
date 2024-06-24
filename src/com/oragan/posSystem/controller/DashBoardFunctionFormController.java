package com.oragan.posSystem.controller;

import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;



public class DashBoardFunctionFormController {
    public AnchorPane context;
    public Text totalItem;
    public Text total;
    public Text CustCount;
    public Text ItemCount;
    public Text OrderCount;

    public void initialize() {
        loadDashboardData();
    }

    private void loadDashboardData() {
        loadCustomerCount();
        loadItemCount();
    }

    private void loadCustomerCount() {
//        String sql = "SELECT COUNT(*) AS count FROM customers";
//        try (Connection conn = DBConnection.getInstance().getConnection();
//             Statement stmt = conn.createStatement();
//             ResultSet rs = stmt.executeQuery(sql)) {
//
//            if (rs.next()) {
//                CustCount.setText(String.valueOf(rs.getInt("count")));
//            }
//        } catch (ClassNotFoundException | SQLException e) {
//            showError("Error loading customer count", e);
//        }
    }

    private void loadItemCount() {
//        String sql = "SELECT COUNT(*) AS count FROM items";
//        try (Connection conn = DBConnection.getInstance().getConnection();
//             Statement stmt = conn.createStatement();
//             ResultSet rs = stmt.executeQuery(sql)) {
//
//            if (rs.next()) {
//                ItemCount.setText(String.valueOf(rs.getInt("count")));
//            }
//        } catch (ClassNotFoundException | SQLException e) {
//            showError("Error loading item count", e);
//        }
    }

    private void showError(String message, Exception e) {
        System.out.println(message + ": " + e.getMessage());
        // Optionally, you can show a dialog to the user
    }
}
