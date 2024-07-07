package com.oragan.posSystem.controller;

import com.oragan.posSystem.db.DBConnection;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

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
    public LineChart<Number, Number> lineChartID;

    public void initialize() {
        loadDashboardData();
        loadLineChart();
    }

    /*private void loadLineChart() {
        // Define the axes
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Date");
        yAxis.setLabel("Total Income");

        // Create a LineChart
        lineChartID = new LineChart<>(xAxis, yAxis);
        lineChartID.setTitle("Total Income Over Time");

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
            lineChartID.getData().add(series);
        } catch (ClassNotFoundException | SQLException e) {
            showError("Error loading line chart data", e);
        }
    }*/
    private void loadLineChart() {
        lineChartID.setTitle("Total Income Over Time");

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
            lineChartID.getData().add(series);
        } catch (ClassNotFoundException | SQLException e) {
            showError("Error loading line chart data", e);
        }
    }

    private void loadDashboardData() {
        loadCustomerCount();
        loadItemCount();
        loadOrderCount();
        loadTotalIncome();

    }

    private void loadTotalIncome() {
        String sql = "SELECT SUM(Total) AS totalIncome FROM \"Order\"";
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
        String sql = "SELECT COUNT(*) AS count FROM \"Order\"";
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
}
