package com.oragan.posSystem.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;


public class DashBoardFormController {

    public AnchorPane context;


    private void setUi2(String ui2) throws IOException {
        context.getChildren().clear();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/" + ui2 + ".fxml"));
        Parent parent = loader.load();
        context.getChildren().add(parent);
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
        setUi2("OrderDetailsForm");

    }

    public void DashBoardOnAction(ActionEvent actionEvent) throws IOException {
        setUi2("DashBoardFunctionForm");

    }
}
