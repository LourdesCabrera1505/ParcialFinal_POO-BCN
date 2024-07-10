package org.example.bcn.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.example.bcn.Connection.DBConnection;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ClientesForm implements Initializable {

    DBConnection connectionClients;

    @FXML
    private Button minimizeButton;
    @FXML
    private TextField NombreCliente;
    @FXML
    private TextField ApellidoCliente;
    @FXML
    private TextField Contacto;
    @FXML
    private TextField DireccionCliente;
    @FXML
    private Button IDSendInfo;


    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        if (IDSendInfo != null) {
            IDSendInfo.setOnAction(this::SetInformation);
        }
    }


    @FXML
    private  void HandleMinimizeButton (ActionEvent event) {
        Stage stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    protected  void SetInformation (ActionEvent event) {
        try {
            connectionClients = DBConnection.getInstance();

            connectionClients.getConnection();

            PreparedStatement pstmt = connectionClients.getConnection().prepareStatement("INSERT INTO Cliente (NameCliente, LastNameCliente, Phone, Direction) VALUES (?,?,?,?)");

            String Cliente = NombreCliente.getText();
            String Apellido = ApellidoCliente.getText();
            String Phone = Contacto.getText();
            String Direction = DireccionCliente.getText();

            pstmt.setString(1, Cliente);
            pstmt.setString(2, Apellido);
            pstmt.setString(3, Phone);
            pstmt.setString(4, Direction);
            pstmt.executeUpdate();
            mostrarExito("Exito : Cliente Registrado Exitosamente");
            ResetForm();


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // funcion que permite limpiar los campos del formulario despues de enviar un registro
    private void ResetForm () {
        NombreCliente.clear();
        ApellidoCliente.clear();
        Contacto.clear();
        DireccionCliente.clear();
    }

    // funcion que permite mostrar un dialogo personalizado para el exito de un registro
    private void mostrarExito(String Message) {
        Dialog<Void> dialog = new Dialog<Void>(); // se crea un dialogo personalizado
        dialog.getDialogPane().setPrefHeight(95);  dialog.getDialogPane().setPrefWidth(400);
        dialog.setTitle("Exito");
        dialog.setHeaderText(null);
        //creando al aviso botones personalizados
        ButtonType ok = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(ok);
        // creando contenido al aviso personalizado
        Label label = new Label(Message);
        label.setPrefWidth(150);  // Ancho deseado
        label.setWrapText(true);  // Permitir envolver el texto si es necesario
        label.setStyle("-fx-font-size: 14px; -fx-padding: 20px; -fx-text-fill: green; -fx-text-align: center; -fx-font-weight: bold 2px; -fx-font-family: 'Baskerville Old Face', 'Britannic Bold'");
        dialog.getDialogPane().setContent(label);

        dialog.initStyle(StageStyle.DECORATED);

        Node okNode = dialog.getDialogPane().lookupButton(ok); // se personaliza el boton , Node es una importación que sirve como base para los elementos visuales creados desde programacion
        if (okNode instanceof  Button) {
            okNode.setStyle("-fx-pref-width: 217px; -fx-pref-height: 32px");
            okNode.setStyle("-fx-background-color:  #5F72D9; -fx-text-fill: white; -fx-border-color: transparent; -fx-background-insets: 0"); // se personaliza el color y el texto del boton ok
            okNode.setOnMouseEntered(e -> {
                okNode.setStyle("-fx-background-color: #A9D4D9; -fx-text-fill: white");
            });
            okNode.setOnMouseExited(e -> {
                okNode.setStyle("-fx-background-color: #5F72D9; -fx-text-fill: black");
            });
        }


        // Maneja el tipo de accioo que realizara el boton
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ok) {
                return null;
            }
            return null;
        });
        dialog.showAndWait();
    }
}
