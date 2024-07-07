package org.example.bcn.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class BCN_Controller {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}