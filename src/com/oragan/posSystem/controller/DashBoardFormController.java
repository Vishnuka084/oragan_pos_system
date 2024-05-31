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
        Parent parent = FXMLLoader.load(getClass().getResource("../view/"+ui2+".fxml"));
        context.getChildren().add(parent);
    }

    public void DashBoardOnAction(ActionEvent actionEvent) throws IOException {
        setUi2("CustomerForm");
    }

    public void CustomerOnAction(ActionEvent actionEvent) throws IOException {
        setUi2("ItemForm");
    }

    public void ItemOnAction(ActionEvent actionEvent) throws IOException {
        setUi2("PurchaseOrderForm");
    }

    public void PurchaseOrderOnAction(ActionEvent actionEvent) throws IOException {
        setUi2("OrderDetailsForm");
    }

    public void OderDetailsOnAction(ActionEvent actionEvent) throws IOException {
        setUi2("DashBoardFunctionForm");
    }
}
