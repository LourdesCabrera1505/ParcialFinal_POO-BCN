package org.example.bcn.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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

    DBConnection connectionClients; //00149823 Conexion de la base de datos

    @FXML
    private ComboBox<String> IDFacilitador; //00149823  un combo box que tiene el facilitador de los tipos de tarjetas
    @FXML
    private ComboBox<String> IDCliente; //00149823 un combo box que tenga a los clientes
    @FXML
    private TextField IDNumber; //00149823 TextField donde se ingresaran los numeros de las tarjetas
    @FXML
    private DatePicker IDDate; //00149823 un DatePicker para poder seleccionar la fecha de expiracion
    @FXML
    private TextField IDTipo; //00149823 otro TextField para poder ingresar si es de Debito o Credito la tarjeta
    @FXML
    private TextField TextCliente; //00149823 TextField no visible para poder
    @FXML
    private TextField TextFacilitador; //00149823 TextField no visible
    @FXML
    private Button IDSendInfo; //00149823 Boton para poder enviar la informacion y que se actualice la base de datos
    @FXML
    private Button minimizeButton; //00149823 Boton para poder minimizar la pantalla
    @FXML
    private TextArea cardDisplayArea; //00149823 Area para poder leer los clientes con sus tarjetas
    @FXML
    private TextField CardID; //00149823 Id de la tarjeta
    @FXML
    private Button Read; //00149823 Boton para poder leer las tarjetas
    @FXML
    private Button Update; //00149823 Boton para poder actualizar la informacion de las tarjetas
    @FXML
    private Button Delete; //00149823 Boton para poder eliminar la informacion de los registros

    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) { //00149823 funcion para inicializar el boton que actualiza la base de datos
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
    protected void createCard(ActionEvent event) { //00149823 Funcion para poder crear una tarjeta
        try {
            Connection connection = DBConnection.getInstance().getConnection(); //00149823 Conexion a la base de datos desde la unica instancia
            PreparedStatement insertSQL = connection.prepareStatement("INSERT INTO Cards (CardNumber, CardType, DateExpired, ClienteID, FacilitatorID) VALUES (?,?,?,?,?)"); //00149823 Consulta para alterar los campos de la base de datos
            String Tarjeta = IDNumber.getText(); //00149823 Ingreso del numero de tarjeta
            String Tipo = IDTipo.getText(); //00149823 Ingreso del tipo de tarjeta
            String Cliente = IDCliente.getValue(); //00149823 Ingreso del cliente
            String Facilitador = IDFacilitador.getValue();
            LocalDate Fecha = IDDate.getValue(); //00149823 Ingreso de la fecha de expiracion de tarjeta
            int ClienteID = ObtenerCliente(connection, Cliente);
            int FacilitadorID = ObtenerFacilitador(connection, Facilitador);
            String FechaFormato = Fecha.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            insertSQL.setString(1, Tarjeta);
            insertSQL.setString(2, Tipo);
            insertSQL.setString(3, FechaFormato);
            insertSQL.setInt(4, ClienteID);
            insertSQL.setInt(5, FacilitadorID);
            insertSQL.executeUpdate();
            mostrarExito("Registro Exitoso"); //00149823 Mostrar mensaje de la insercion exitosa
            ResetForm();

        } catch (SQLException e) {
            e.printStackTrace();
            mostrarError("Error en el registro: " + e.getMessage());
        }
    }

    @FXML
    private void readCards(ActionEvent event) { //00149823 Funcion para poder leer las tarjetas
        try {
            Connection connection = DBConnection.getInstance().getConnection();//00149823 Conexion a la base de datos con instancia unica
            int clienteId = ObtenerCliente(connection, TextCliente.getText()); //00149823 Se obtiene el id del cliente
            String query = "SELECT CardNumber, CardType FROM Cards WHERE ClienteID = ?"; //00149823 Consulta para obtener las tarjetas de credito y debito
            PreparedStatement ps = connection.prepareStatement(query); //00149823 See prepara un statemente con la conexion
            ps.setInt(1, clienteId);
            ResultSet rs = ps.executeQuery();
            StringBuilder creditCards = new StringBuilder("Tarjetas de Crédito:\n");
            StringBuilder debitCards = new StringBuilder("Tarjetas de Débito:\n");
            boolean hasCreditCards = false;
            boolean hasDebitCards = false;
            while (rs.next()) {
                String cardNumber = rs.getString("CardNumber");
                String cardType = rs.getString("CardType");

                // Enmascarar el número de la tarjeta
                String maskedCardNumber = "XXXX XXXX XXXX " + cardNumber.substring(cardNumber.length() - 4);

                if ("Crédito".equalsIgnoreCase(cardType)) {
                    creditCards.append(maskedCardNumber).append("\n");
                    hasCreditCards = true;
                } else if ("Débito".equalsIgnoreCase(cardType)) {
                    debitCards.append(maskedCardNumber).append("\n");
                    hasDebitCards = true;
                }
            }
            if (!hasCreditCards) {
                creditCards.append("N/A\n");
            }
            if (!hasDebitCards) {
                debitCards.append("N/A\n");
            }
            cardDisplayArea.setText(creditCards.toString() + "\n" + debitCards.toString());
        } catch (SQLException e) {
            e.printStackTrace();
            mostrarError("Error al leer las tarjetas: " + e.getMessage());
        }
    }

    @FXML
    private void updateCard(ActionEvent event) {
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement updateSQL = connection.prepareStatement("UPDATE Cards SET CardNumber = ?, CardType = ?, DateExpired = ? WHERE CardID = ? AND ClienteID = ?"); //00149823 Consulta para poder actualizar los datos de las tarjetas
            String tarjeta = IDNumber.getText();
            String tipo = IDTipo.getText();
            String cliente = TextCliente.getText();
            LocalDate fecha = IDDate.getValue();
            String fechaFormato = fecha.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            int cardID = Integer.parseInt(CardID.getText());
            int clienteID = ObtenerCliente(connection, cliente);
            updateSQL.setString(1, tarjeta);
            updateSQL.setString(2, tipo);
            updateSQL.setString(3, fechaFormato);
            updateSQL.setInt(4, cardID);
            updateSQL.setInt(5, clienteID);
            updateSQL.executeUpdate();
            mostrarExito("Actualización Exitosa");
            ResetForm();
        } catch (SQLException e) {
            e.printStackTrace();
            mostrarError("Error al actualizar la tarjeta: " + e.getMessage());
        }
    }

    @FXML
    private void deleteCard(ActionEvent event) {
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement deleteSQL = connection.prepareStatement("DELETE FROM Cards WHERE CardID = ? AND ClienteID = ?"); //00149823 Consulta para poder eliminar datos de la tabla
            int cardID = Integer.parseInt(CardID.getText());
            String cliente = TextCliente.getText();
            int clienteID = ObtenerCliente(connection, cliente);

            deleteSQL.setInt(1, cardID);
            deleteSQL.setInt(2, clienteID);

            deleteSQL.executeUpdate();
            mostrarExito("Eliminación Exitosa");
            ResetForm();
        } catch (SQLException e) {
            e.printStackTrace();
            mostrarError("Error al eliminar la tarjeta: " + e.getMessage());
        }
    }


    private int ObtenerFacilitador(Connection connection, String facilitador) throws SQLException { //00149823 En esta funcion se obtienen los nombres de lls facilitadores de los tipos de tarjetas que hay
        String query = "SELECT FacilitatorID FROM Facilitator WHERE FacilitatorName = ?"; //00149823 Consulta para obtener los nobres de los facilitadores desde la Tabla FACILITADOR
        try (PreparedStatement ps = connection.prepareStatement(query)) { //00149823 Conexion con el requerimiento
            ps.setString(1, facilitador);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("FacilitatorID");
            } else {
                throw new SQLException("No se encontró el facilitador: " + facilitador);
            }
        }
    }

    private int ObtenerCliente(Connection connection, String cliente) throws SQLException { //00149823 En esta funcion se obtiene el cliente desde la base de datos
        String query = "SELECT ClienteID FROM Cliente WHERE NameCliente = ?"; //00149823 Consulta para poder obtener los nombres de los clientes desde la tabla CLIENTE
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

    // funcion que permite limpiar los campos del formulario despues de enviar un registro
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

    // funcion que permite mostrar un dialogo personalizado para el exito de un registro
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
    private  void HandleMinimizeButton (ActionEvent event) {
        Stage stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }


}
