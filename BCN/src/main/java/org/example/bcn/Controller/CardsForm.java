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

    DBConnection connectionClients; //00149823 Conexion de la base de datos

    @FXML
    private ComboBox IDFacilitador; //00149823  un combo box que tiene el facilitador de los tipos de tarjetas
    @FXML
    private ComboBox IDCliente; //00149823 un combo box que tenga a los clientes
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
    public void initialize(URL url, ResourceBundle resourceBundle) { //00149823 funcion para inicializar el boton que actualiza la base de datos
        if (IDSendInfo != null){
            IDSendInfo.setOnAction(this::createCard);
        }
    }

    protected void createCard(ActionEvent event) { //00149823 Funcion para poder crear una tarjeta
        try {
            connectionClients = DBConnection.getInstance(); //00149823 Conexion a la base de datos desde la unica instancia
            connectionClients.getConnection(); //00149823 Conexion para los clientes
            PreparedStatement insertSQL = connectionClients.getConnection().prepareStatement("INSERT INTO Cards (CardNumber, CardType, DateExpired, ClienteID, FacilitatorID) VALUES (?,?,?,?,?)"); //00149823 Consulta para alterar los campos de la base de datos
            String Tarjeta = IDNumber.getText(); //00149823 Ingreso del numero de tarjeta
            String Tipo = IDTipo.getText(); //00149823 Ingreso del tipo de tarjeta
            String Cliente = TextCliente.getText(); //00149823 Ingreso del cliente
            String Facilitador = TextFacilitador.getText();
            LocalDate Fecha = IDDate.getValue(); //00149823 Ingreso de la fecha de expiracion de tarjeta
            String FechaFormato = Fecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            int IDFacilitador = ObtenerFacilitador((Connection) connectionClients, Facilitador);
            int IDCliente = ObtenerCliente((Connection) connectionClients, Cliente);
            insertSQL.setString(1, Tarjeta);
            insertSQL.setString(2, Tipo);
            insertSQL.setString(3, FechaFormato);
            insertSQL.setInt(4, IDCliente);
            insertSQL.setInt(5, IDFacilitador);
            insertSQL.executeUpdate();
            mostrarExito("Registro Exitoso"); //00149823 Mostrar mensaje de la insercion exitosa
            ResetForm();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private int ObtenerFacilitador(Connection connection, String facilitador) throws SQLException { //00149823 En esta funcion se obtienen los nombres de lls facilitadores de los tipos de tarjetas que hay

        String query = "SELECT FacilitatorID FROM FACILITATOR WHERE FacilitatorName = ?"; //00149823 Consulta para obtener los nobres de los facilitadores desde la Tabla FACILITADOR
        try(PreparedStatement ps = connection.prepareStatement(query)) { //00149823 Conexion con el requerimiento
            ps.setString(1, facilitador);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                return rs.getInt("FacilitatorID");
            }else {
                throw new SQLException("No se encontro el facilitador" + facilitador); //00149823 se muestra un mensaje sino se ha encontrado el facilitador
            }
        }
    }

    private int ObtenerCliente(Connection connection, String cliente) throws SQLException { //00149823 En esta funcion se obtiene el cliente desde la base de datos
        String query = "SELECT ClienteID FROM CLIENTE WHERE NameCliente = ?"; //00149823 Consulta para poder obtener los nombres de los clientes desde la tabla CLIENTE
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
