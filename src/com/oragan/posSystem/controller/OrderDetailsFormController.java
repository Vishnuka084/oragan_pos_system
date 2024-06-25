
package com.oragan.posSystem.controller;

import javafx.event.ActionEvent;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class OrderDetailsFormController {
    public TextField txtSearchField;
    public TableView orderdetailstbl;
    public MenuButton cmbCustomerId;
    public TableColumn colOderId;
    public TableColumn colCustId;
    public TableColumn colCusName;
    public TableColumn colDate;
    public TableColumn colTotal;
    public TableColumn colDiscount;
    public TableColumn colAmount;
    public TableColumn colStatus;
    public TableColumn colOption;

    public void txtSearchFieldOnAction(ActionEvent actionEvent) {
    }

    public void cmbSearchByOrderOnAction(ActionEvent actionEvent) {
    }
}
