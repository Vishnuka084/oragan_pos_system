package com.oragan.posSystem.controller;

import com.oragan.posSystem.db.DBConnection;
import com.oragan.posSystem.entity.Item;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class DashBoardFunctionFormController {
    public AnchorPane context;
    public Text totalItem;
    public Text total;
    public Text CustCount;
    public Text ItemCount;
    public Text OrderCount;
    public javafx.scene.chart.AreaChart<Number, Number> areaChartID;
    public ImageView imgNotificationIcon;
    public Label lblNotificationCount;
    public PieChart pieChart1;


    private ObservableList<Item> itemList = FXCollections.observableArrayList();

    public void initialize() {
        loadDashboardData();
        loadAreaChart();
        loadItemsData();
        loadPieChart(); // Initialize and update the PieChart
    }

    private void loadItemsData() {
        itemList.clear();
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
                    int criticalLevel = rs.getInt("critical_level");
                    Item item = new Item(itemCode, itemName, price, qty, criticalLevel);
                    itemList.add(item);
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            showError("Error loading item data from the database", e);
        }
        updateCriticalItemsCount();
    }

    private void updateCriticalItemsCount() {
        long count = itemList.stream().filter(item -> item.getQty() <= item.getCritical_level()).count();
        lblNotificationCount.setText(String.valueOf(count));
    }

    private void loadAreaChart() {
        areaChartID.setTitle("Total Income Over Time");

        // Fetch data from the database and populate the chart
        String sql = "SELECT Issue_Date, SUM(Total) AS dailyIncome FROM `Order` GROUP BY Issue_Date";
        try (Connection conn = DBConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // Create a series to add data
            XYChart.Series<Number, Number> series = new XYChart.Series<>();
            series.setName("Daily Income");

            // A map to store dates and corresponding income
            Map<LocalDate, Double> dataMap = new HashMap<>();

            while (rs.next()) {
                LocalDate date = LocalDate.parse(rs.getString("Issue_Date"));
                double dailyIncome = rs.getDouble("dailyIncome");
                dataMap.put(date, dailyIncome);
            }

            // Add data to the series
            for (Map.Entry<LocalDate, Double> entry : dataMap.entrySet()) {
                series.getData().add(new XYChart.Data<>(entry.getKey().toEpochDay(), entry.getValue()));
            }

            // Add series to the chart
            areaChartID.getData().add(series);
        } catch (ClassNotFoundException | SQLException e) {
            showError("Error loading area chart data", e);
        }
    }

    // Load Dashboard Data
    private void loadDashboardData() {
        loadCustomerCount();
        loadItemCount();
        loadOrderCount();
        loadTotalIncome();
    }

    private void loadTotalIncome() {
        String sql = "SELECT SUM(Total) AS totalIncome FROM `Order`";
        try (Connection conn = DBConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                total.setText(String.format("%.2f", rs.getDouble("totalIncome")));
            }
        } catch (ClassNotFoundException | SQLException e) {
            showError("Error loading total income", e);
        }
    }

    private void loadOrderCount() {
        String sql = "SELECT COUNT(*) AS count FROM `Order`";
        try (Connection conn = DBConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                OrderCount.setText(String.valueOf(rs.getInt("count")));
            }
        } catch (ClassNotFoundException | SQLException e) {
            showError("Error loading order count", e);
        }
    }

    private void loadCustomerCount() {
        String sql = "SELECT COUNT(*) AS count FROM customers";
        try (Connection conn = DBConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                CustCount.setText(String.valueOf(rs.getInt("count")));
            }
        } catch (ClassNotFoundException | SQLException e) {
            showError("Error loading customer count", e);
        }
    }

    private void loadItemCount() {
        String sql = "SELECT COUNT(*) AS count FROM items";
        try (Connection conn = DBConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                ItemCount.setText(String.valueOf(rs.getInt("count")));
            }
        } catch (ClassNotFoundException | SQLException e) {
            showError("Error loading item count", e);
        }
    }

    private void showError(String message, Exception e) {
        System.out.println(message + ": " + e.getMessage());
        // Optionally, you can show a dialog to the user
    }

    private void loadPieChart() {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        // Add data to the pie chart
        for (Item item : itemList) {
            pieChartData.add(new PieChart.Data(item.getItem_name(), item.getQty()));
        }

        // Set the data to the pie chart
        pieChart1.setData(pieChartData);
    }

    public void btnNotificationIconOnAction(MouseEvent mouseEvent) throws IOException {
        URL resource = this.getClass().getResource("/com/oragan/posSystem/view/CriticalLevelForm.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(resource);
        Parent load = fxmlLoader.load();
        CriticalLevelFormController criticalLevelFormController = fxmlLoader.getController();
        criticalLevelFormController.setItemsData(itemList); // Pass the item list to the controller
        Stage stage = new Stage();
        stage.setScene(new Scene(load));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Critical Level Items");
        stage.centerOnScreen();
        stage.show();
    }

    public void imgCreditCustomerClickEvent(MouseEvent mouseEvent) throws IOException {
        URL resource = this.getClass().getResource("/com/oragan/posSystem/view/CreditCustomerForm.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(resource);
        Parent load = fxmlLoader.load();

        CreditCustomerFormController creditCustomerFormController = fxmlLoader.getController();
        // Call the FetchCreditCustomers method on the controller
        creditCustomerFormController.FetchCreditCustomers();

        Stage stage = new Stage();
        stage.setScene(new Scene(load));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Critical Level Items");
        stage.centerOnScreen();
        stage.show();
    }
}
