package org.example.bcn.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Spinner;
import javafx.stage.Stage;
import
import java.net.URL;
import java.util.ResourceBundle;

public class Compras implements Initializable {

    @FXML
    private DatePicker IDFechaVenta;
    @FXML
    private Spinner<Double> IDPagoTotal;
    @FXML
    private ComboBox<String> ID_InformacionCuenta;
    @FXML
    private ComboBox<String> IDCompraDescripcion;
    @FXML
    private Button IDSendInfo;

    @FXML
    private  void HandleMinimizeButton (ActionEvent event) {
        Stage stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }
    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }


}
