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

    DBConnection connectionClients;

    @FXML
    private ComboBox IDFacilitador;
    @FXML
    private ComboBox IDCliente;
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
        if (IDSendInfo != null){
            IDSendInfo.setOnAction(this::createCard);
        }
    }

    protected void createCard(ActionEvent event) {
        try {
            connectionClients = DBConnection.getInstance();
            connectionClients.getConnection();
            PreparedStatement insertSQL = connectionClients.getConnection().prepareStatement("INSERT INTO Cards (CardNumber, CardType, DateExpired, ClienteID, FacilitatorID) VALUES (?,?,?,?,?)");
            String Tarjeta = IDNumber.getText();
            String Tipo = IDTipo.getText();
            String Cliente = TextCliente.getText();
            String Facilitador = TextFacilitador.getText();
            LocalDate Fecha = IDDate.getValue();
            String FechaFormato = Fecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            int IDFacilitador = ObtenerFacilitador((Connection) connectionClients, Facilitador);
            int IDCliente = ObtenerCliente((Connection) connectionClients, Cliente);
            insertSQL.setString(1, Tarjeta);
            insertSQL.setString(2, Tipo);
            insertSQL.setString(3, FechaFormato);
            insertSQL.setInt(4, IDCliente);
            insertSQL.setInt(5, IDFacilitador);
            insertSQL.executeUpdate();
            mostrarExito("Registro Exitoso");
            ResetForm();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private int ObtenerFacilitador(Connection connection, String facilitador) throws SQLException {

        String query = "SELECT FacilitatorID FROM FACILITATOR WHERE FacilitatorName = ?";
        try(PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, facilitador);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                return rs.getInt("FacilitatorID");
            }else {
                throw new SQLException("No se encontro el facilitador" + facilitador);
            }
        }
    }

    private int ObtenerCliente(Connection connection, String cliente) throws SQLException {
        String query = "SELECT ClienteID FROM CLIENTE WHERE NameCliente = ?";
        try(PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, cliente);
            ResultSet rs = ps.executeQuery();

            if(rs.next()) {
                return rs.getInt("ClienteID");
            }else {
                throw new SQLException("No se encontro el cliente" + cliente);
            }
        }
    }

    private void CargarFacilitador() throws SQLException {
        connectionClients = DBConnection.getInstance();
        connectionClients.getConnection();

        Statement st = null;
        ResultSet rs = null;
        try {
            Connection facilitador = connectionClients.getConnection();
            st = facilitador.createStatement();
            rs = st.executeQuery("SELECT FacilitatorName FROM Facilitator");
            ObservableList<String> opciones = FXCollections.observableArrayList();
            while(rs.next()) {
                opciones.add(rs.getString("FacilitatorName"));
            }
            IDFacilitador.setItems(opciones);
        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(rs != null)
                    rs.close();
                if (st != null)
                    st.close();
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void CargarCliente() throws SQLException {
        connectionClients = DBConnection.getInstance();
        connectionClients.getConnection();

        Statement st = null;
        ResultSet rs = null;
        try {
            Connection cliente = connectionClients.getConnection();
            st = cliente.createStatement();
            rs = st.executeQuery("SELECT NameCliente FROM Cliente");
            ObservableList<String> opciones = FXCollections.observableArrayList();
            while(rs.next()) {
                opciones.add(rs.getString("NameCliente"));
            }
            IDCliente.setItems(opciones);
        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(rs != null)
                    rs.close();
                if (st != null)
                    st.close();
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // funcion que permite limpiar los campos del formulario despues de enviar un registro
    private void ResetForm () {
        IDNumber.clear();
        IDTipo.clear();
        IDFacilitador.setValue(null);
        IDDate.getEditor().clear();
        IDCliente.setValue(null);
        TextCliente.clear();
        TextFacilitador.clear();
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


    @FXML
    private  void HandleMinimizeButton (ActionEvent event) {
        Stage stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }


}
