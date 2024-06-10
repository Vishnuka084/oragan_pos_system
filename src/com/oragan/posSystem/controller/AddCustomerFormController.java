/**
 * authour By Pamindu Nawodya
 * Date:6/1/2024
 * Time:10:14 PM
 * Project Name:oragan_pos_system
 */
package com.oragan.posSystem.controller;

import javafx.event.ActionEvent;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class AddCustomerFormController {


    public AnchorPane addCustomerFormContext;
    public TextField txtCustomerId;
    public TextField txtCustomerName;
    public TextField txtCustomerAddress;
    public TextField txtContactNumber;

    public void btnClearTxtFldOnAction(ActionEvent actionEvent) {
        txtCustomerId.clear();
        txtCustomerName.clear();
        txtCustomerAddress.clear();
        txtContactNumber.clear();
    }

    public void btnCustomerAddOnAction(ActionEvent actionEvent) {
        String customerID=txtCustomerId.getText();


    }
}
