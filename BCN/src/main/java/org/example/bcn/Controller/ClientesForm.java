package org.example.bcn.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.example.bcn.Connection.DBConnection;

import java.net.URL;
import java.util.ResourceBundle;

public class ClientesForm implements Initializable {

    DBConnection connectionClients;

    @FXML
    private Button minimizeButton;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    private  void HandleMinimizeButton (ActionEvent event) {
        Stage stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }
}
