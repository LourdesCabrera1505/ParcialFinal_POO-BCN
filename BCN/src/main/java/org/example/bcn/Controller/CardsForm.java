package org.example.bcn.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.example.bcn.Connection.DBConnection;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class CardsForm implements Initializable {

    @FXML
    private ComboBox<String> IDFacilitador;
    @FXML
    private ComboBox<String> IDCliente;
    @FXML
    private TextField IDNumber;
    @FXML
    private DatePicker IDDate;
    @FXML
    private TextField IDTipo;
    @FXML
    private TextField TextCliente;
    @FXML
    private TextField TextFacilitador;
    @FXML
    private Button IDSendInfo;
    @FXML
    private Button minimizeButton;

    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (IDSendInfo != null) {
            IDSendInfo.setOnAction(this::createCard);
            try {
                CargarCliente();
                CargarFacilitador();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @FXML
    protected void createCard(ActionEvent event) {
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement insertSQL = connection.prepareStatement("INSERT INTO Cards (CardNumber, CardType, DateExpired, ClienteID, FacilitatorID) VALUES (?,?,?,?,?)");
            String Tarjeta = IDNumber.getText();
            String Tipo = IDTipo.getText();
            String Cliente = IDCliente.getValue();
            String Facilitador = IDFacilitador.getValue();
            LocalDate Fecha = IDDate.getValue();
            int ClienteID = ObtenerCliente(connection, Cliente);
            int FacilitadorID = ObtenerFacilitador(connection, Facilitador);
            String FechaFormato = Fecha.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            insertSQL.setString(1, Tarjeta);
            insertSQL.setString(2, Tipo);
            insertSQL.setString(3, FechaFormato);
            insertSQL.setInt(4, ClienteID);
            insertSQL.setInt(5, FacilitadorID);
            insertSQL.executeUpdate();
            mostrarExito("Registro Exitoso");
            ResetForm();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private int ObtenerFacilitador(Connection connection, String facilitador) throws SQLException {
        String query = "SELECT FacilitatorID FROM Facilitator WHERE FacilitatorName = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, facilitador);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("FacilitatorID");
            } else {
                throw new SQLException("No se encontró el facilitador: " + facilitador);
            }
        }
    }

    private int ObtenerCliente(Connection connection, String cliente) throws SQLException {
        String query = "SELECT ClienteID FROM Cliente WHERE NameCliente = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, cliente);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("ClienteID");
            } else {
                throw new SQLException("No se encontró el cliente: " + cliente);
            }
        }
    }

    private void CargarFacilitador() throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        try (Statement st = connection.createStatement(); ResultSet rs = st.executeQuery("SELECT FacilitatorName FROM Facilitator")) {
            ObservableList<String> opciones = FXCollections.observableArrayList();
            while (rs.next()) {
                opciones.add(rs.getString("FacilitatorName"));
            }
            IDFacilitador.setItems(opciones);
        }
    }

    private void CargarCliente() throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        try (Statement st = connection.createStatement(); ResultSet rs = st.executeQuery("SELECT NameCliente FROM Cliente")) {
            ObservableList<String> opciones = FXCollections.observableArrayList();
            while (rs.next()) {
                opciones.add(rs.getString("NameCliente"));
            }
            IDCliente.setItems(opciones);
        }
    }

    private void ResetForm() {
        IDNumber.clear();
        IDTipo.clear();
        IDFacilitador.setValue(null);
        IDDate.setValue(null);  // Corrección aquí
        IDCliente.setValue(null);
        TextCliente.clear();
        TextFacilitador.clear();
    }


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

    @FXML
    private void HandleMinimizeButton(ActionEvent event) {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }
}
