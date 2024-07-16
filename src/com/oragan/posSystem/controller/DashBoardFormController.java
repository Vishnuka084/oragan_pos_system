package com.oragan.posSystem.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class DashBoardFormController {

    public AnchorPane context;
    public Label timeLabel;
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("    yyyy-MM-dd | HH:mm:ss");

    public void initialize() {
        try {
            // Display DashBoardFunctionForm by default when DashBoardForm is loaded
            DashBoardOnAction(null);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Create a timeline to update the time label every second
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), event -> updateTime())
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void updateTime() {
        LocalDateTime now = LocalDateTime.now();
        timeLabel.setText(now.format(timeFormatter));
    }

    private void setUi2(String ui2) throws IOException {
        context.getChildren().clear();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/" + ui2 + ".fxml"));
        context.getChildren().add(loader.load());
    }


    public void CustomerOnAction(ActionEvent actionEvent) throws IOException {
        setUi2("CustomerForm");
    }

    public void ItemOnAction(ActionEvent actionEvent) throws IOException {

        setUi2("ItemForm");
    }

    public void PurchaseOrderOnAction(ActionEvent actionEvent) throws IOException {
        setUi2("PurchaseOrderForm");
    }

    public void OderDetailsOnAction(ActionEvent actionEvent) throws IOException {
        System.out.println("Hello Order details form");
        setUi2("OrderDetailsForm");
    }

    public void DashBoardOnAction(ActionEvent actionEvent) throws IOException {
        setUi2("DashBoardFunctionForm");

    }
}
