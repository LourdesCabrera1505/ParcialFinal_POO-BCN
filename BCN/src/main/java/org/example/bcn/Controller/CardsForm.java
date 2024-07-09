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
            mostrarError("Error en el registro: " + e.getMessage());
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

    private void mostrarExito(String mensaje) {
        mostrarDialogo("Éxito", mensaje, "-fx-text-fill: green;");
    }

    private void mostrarError(String mensaje) {
        mostrarDialogo("Error", mensaje, "-fx-text-fill: red;");
    }

    private void mostrarDialogo(String titulo, String mensaje, String estiloTexto) {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle(titulo);
        dialog.getDialogPane().setPrefSize(400, 95);
        ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(okButtonType);
        Label label = new Label(mensaje);
        label.setStyle(estiloTexto + " -fx-font-size: 14px; -fx-padding: 20px;");
        label.setWrapText(true);
        dialog.getDialogPane().setContent(label);
        dialog.initStyle(StageStyle.DECORATED);
        dialog.showAndWait();
    }

    @FXML
    private void HandleMinimizeButton(ActionEvent event) {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }
}
