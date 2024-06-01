
package com.oragan.posSystem.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class ItemFormController {
    public void btnAddItemFormOnAction(ActionEvent actionEvent) throws IOException {
        URL resource = this.getClass().getResource("/com/oragan/posSystem/view/AddItemForm.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(resource);
        Parent load = fxmlLoader.load();
        // AddCustomerFormController AddCustomerFormController = fxmlLoader.getController();
        // AddCustomerFormController.init(tblStudent,this);
        Stage stage = new Stage();
        stage.setScene(new Scene(load));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Add Customer Form");
        stage.centerOnScreen();
        stage.show();
    }
}
