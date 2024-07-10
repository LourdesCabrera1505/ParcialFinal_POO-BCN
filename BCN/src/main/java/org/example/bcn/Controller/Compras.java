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
import javafx.util.StringConverter;
import org.example.bcn.Connection.DBConnection;
import org.example.bcn.Conversion.DoubleStringConverter;

import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class Compras implements Initializable {

    @FXML
    private DatePicker IDFechaVenta;
    @FXML
    private Spinner<Double> IDPagoTotal;
    @FXML
    private ComboBox<String> ID_InformacionCuenta;
    @FXML
    private TextArea IDCompraDescripcion;
    @FXML
    private Button IDSendInfo;

    @FXML
    private  void HandleMinimizeButton (ActionEvent event) {
        Stage stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }
    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (IDSendInfo != null) {
            IDSendInfo.setOnAction(this::SendInformation);
            SpinnerValueFactory<Double> valueFactory= new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 1000.0, 0.00, 1.0);
            StringConverter<Double> converter = new DoubleStringConverter();
            valueFactory.setConverter(converter);
            IDPagoTotal.setValueFactory(valueFactory);
            try {
                CargarInformationCuenta();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @FXML
    protected  void SendInformation (ActionEvent event) {
        // Send Information to the server
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            // Send data to the server
            PreparedStatement statement = connection.prepareStatement("INSERT INTO Smart_Shopping (DateShopping, TotalAmount, DescriptionShopping, CardID) VALUES (?,?,?,?)");

            LocalDate FechaVenta = IDFechaVenta.getValue();
            double Pago = IDPagoTotal.getValue();
            String Descripcion = IDCompraDescripcion.getText();
            String InformacionCuenta = ID_InformacionCuenta.getValue();

            int CardID = ObtenerInformacionCuenta(connection, InformacionCuenta);

            statement.setDate(1, Date.valueOf(FechaVenta));
            statement.setDouble(2, Pago);
            statement.setString(3, Descripcion);
            statement.setInt(4, CardID);

            statement.executeUpdate();

            mostrarExito("Compra realizada exitosamente !");

            Reset();

        }catch (SQLException e){

        }
    }

    private void CargarInformationCuenta () throws SQLException{
        Connection connection = DBConnection.getInstance().getConnection();
        try (Statement st = connection.createStatement(); ResultSet rs = st.executeQuery("SELECT c.CardNumber , ce.NameCliente FROM Cards as c INNER JOIN  Cliente as ce ON ce.ClienteID = c.ClienteID ")){
            ObservableList<String> options = FXCollections.observableArrayList();
            while (rs.next()) {
                String cardNumber = censurarNumeroTarjeta(rs.getString("CardNumber"));
                String clienteNombre = rs.getString("NameCliente");
                options.add(clienteNombre + " - " + cardNumber);
            }
            ID_InformacionCuenta.setItems(options);
        }
    }

    private int ObtenerInformacionCuenta(Connection connection, String  NumeroTarjeta) throws SQLException {
        // Obtener el número de tarjeta de la cadena seleccionada en el ComboBox

        String query = "SELECT CardID FROM Card WHERE NumberCard = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, NumeroTarjeta);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("CardID");
            } else {
                throw new SQLException("No se encontró ninguna tarjeta con el número especificado.");
            }
        }
    }
    private String censurarNumeroTarjeta(String numeroTarjeta) {
        // Ejemplo de censurado: mostrar solo los últimos cuatro dígitos
        if (numeroTarjeta.length() >= 4) {
            String Digito4 = numeroTarjeta.substring(numeroTarjeta.length() - 4);
            return "XXXX XXXX XXXX " + Digito4; // Oculta los primeros dígitos
        } else {
            return "XXXX XXXX XXXX XXXX"; // Manejo de longitud insuficiente, por ejemplo
        }
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

    public  void Reset() {
        IDFechaVenta.setValue(null);
        IDPagoTotal.getValueFactory().setValue(0.0);
        IDCompraDescripcion.clear();
        ID_InformacionCuenta.setValue(null);
    }

}
