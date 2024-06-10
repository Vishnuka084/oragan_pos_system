package com.oragan.posSystem.controller;

import javafx.event.ActionEvent;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class AddItemFormController {
    
    public TextField txtItemName;
    public TextField txtItemCode;
    public TextField txtItemPrice;
    public TextField txtItemQty;
    public AnchorPane addItemFormContext;

    public void initialize() {

    }

    public void clearbtnOnAction(ActionEvent actionEvent) {
        txtItemCode.clear();
        txtItemName.clear();
        txtItemPrice.clear();
        txtItemQty.clear();
    }

    public void saveBtnOnAction(ActionEvent actionEvent) {

    }
}
