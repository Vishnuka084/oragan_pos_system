package com.oragan.posSystem.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

/**
 * Created by Vishnuka Yahan
 *
 * @author : Vishnuka Yahan
 * @data : 5/31/2024
 * @project : oragan_posSystem
 */
public class DashBoardFormController {

    public AnchorPane context;

    private void setUi2(String ui2) throws IOException {
        context.getChildren().clear();
        Parent parent = FXMLLoader.load(getClass().getResource("../view/"+ui2+".fxml"));
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
