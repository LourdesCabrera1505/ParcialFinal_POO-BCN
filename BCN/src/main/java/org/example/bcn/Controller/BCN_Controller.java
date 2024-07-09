package org.example.bcn.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class BCN_Controller implements Initializable {

    @FXML
    private ProgressBar ID_Progress; // Declaración privada de un progress bar denominado ID_Progress. Se utiliza para la identificación del componente a utilizar

    @FXML
    private Label progressLabel;

    @FXML
    private Button minimizeButton;

    @FXML
    private Button CloseButton;

    public ProgressBar getID_Progress() {
        return ID_Progress;
    }

    public Label getProgressLabel() {
        return progressLabel;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    private void HandleMinimizeButton (ActionEvent event) {
        Stage stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    private void HandleCloseButton (ActionEvent event) {
        Stage stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        stage.close();
    }
}