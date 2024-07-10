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
    private void HandleMinimizeButton(ActionEvent event) {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Inicialización del controlador al cargar la vista FXML
        if (IDSendInfo != null && IDPagoTotal != null) {
            IDSendInfo.setOnAction(this::SendInformation);

            // Configurar el Spinner de Pago Total
            SpinnerValueFactory<Double> valueFactory = new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 1000.0, 0.00, 1.0);
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
    protected void SendInformation(ActionEvent event) {
        // Método para enviar la información a la base de datos
        try {
            Connection connection = DBConnection.getInstance().getConnection();

            // Preparar la sentencia SQL para insertar la compra
            String insertQuery = "INSERT INTO Smart_Shopping (DateShopping, TotalAmount, DescriptionShopping, CardID) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(insertQuery);

            // Obtener los datos desde los controles de la interfaz
            LocalDate fechaVenta = IDFechaVenta.getValue();
            Double pagoTotal = IDPagoTotal.getValue(); // Verifica que el tipo de retorno sea Double
            String descripcionCompra = IDCompraDescripcion.getText();
            String informacionCuenta = ID_InformacionCuenta.getValue();

            // Validar los datos obtenidos
            if (fechaVenta == null || pagoTotal == null || descripcionCompra == null || descripcionCompra.trim().isEmpty() || informacionCuenta == null) {
                System.out.println("Error: Todos los campos deben estar llenos.");
                return;
            }

            // Obtener el CardID desde la base de datos
            int cardID = ObtenerCardID(connection, informacionCuenta);
            if (cardID == -1) {
                // Si no se encontró ninguna tarjeta con el número especificado, manejar el error
                System.out.println("Error: No se encontró ninguna tarjeta con el número especificado.");
                return; // Salir del método si hay un error
            }

            // Establecer los parámetros de la sentencia preparada
            statement.setDate(1, Date.valueOf(fechaVenta));
            statement.setDouble(2, pagoTotal);
            statement.setString(3, descripcionCompra);
            statement.setInt(4, cardID);

            // Ejecutar la inserción
            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                // Mostrar éxito al usuario
                mostrarExito("Compra realizada exitosamente");
                // Limpiar los campos después de la inserción
                Reset();
            } else {
                System.out.println("Error: No se pudo insertar el registro.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void CargarInformationCuenta() throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        String query = "SELECT c.CardNumber, ce.NameCliente FROM Cards AS c INNER JOIN Cliente AS ce ON ce.ClienteID = c.ClienteID";
        try (Statement st = connection.createStatement(); ResultSet rs = st.executeQuery(query)) {
            ObservableList<String> options = FXCollections.observableArrayList();
            while (rs.next()) {
                String cardNumber = censurarNumeroTarjeta(rs.getString("CardNumber"));
                String clienteNombre = rs.getString("NameCliente");
                options.add(clienteNombre + " - " + cardNumber);
            }
            ID_InformacionCuenta.setItems(options);
        }
    }

    private int ObtenerCardID(Connection connection, String informacionCuenta) throws SQLException {
        // Verificar si informacionCuenta es nulo o vacío
        if (informacionCuenta == null || informacionCuenta.isEmpty()) {
            System.out.println("Error: La información de cuenta está vacía.");
            return -1; // Manejar el error si el ComboBox no tiene valor
        }

        // Dividir la cadena para obtener el número de tarjeta
        String[] parts = informacionCuenta.split(" - ");
        if (parts.length != 2) {
            System.out.println("Error: Formato de información de cuenta incorrecto.");
            return -1; // Manejar el error si el formato del ComboBox no es correcto
        }

        // Obtener el número de tarjeta limpio
        String cardInfo = parts[1].trim();
        String cardNumber = cardInfo.substring(cardInfo.length() - 4); // Obtener los últimos 4 dígitos
        if (cardNumber.isEmpty()) {
            System.out.println("Error: Número de tarjeta vacío.");
            return -1; // Manejar el caso de número de tarjeta vacío
        }

        System.out.println("Debug: Número de tarjeta limpio: " + cardNumber); // Mensaje de depuración

        // Consulta para obtener el CardID desde el número de tarjeta
        String query = "SELECT CardID FROM Cards WHERE RIGHT(CardNumber, 4) = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, cardNumber);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int cardID = rs.getInt("CardID");
                System.out.println("Debug: CardID encontrado: " + cardID); // Mensaje de depuración
                return cardID;
            } else {
                System.out.println("Error: No se encontró ninguna tarjeta con el número especificado.");
                return -1; // Manejar el caso en que no se encontró ninguna tarjeta
            }
        }
    }

    private String censurarNumeroTarjeta(String numeroTarjeta) {
        // Método para censurar el número de tarjeta (solo mostrar los últimos cuatro dígitos)
        if (numeroTarjeta.length() >= 4) {
            String Digito4 = numeroTarjeta.substring(numeroTarjeta.length() - 4);
            return "XXXX XXXX XXXX " + Digito4; // Oculta los primeros dígitos
        } else {
            return "XXXX XXXX XXXX XXXX"; // Manejo de longitud insuficiente, por ejemplo
        }
    }

    public void Reset() {
        // Método para limpiar los campos después de una operación
        IDFechaVenta.setValue(null);
        IDPagoTotal.getValueFactory().setValue(0.0);
        IDCompraDescripcion.clear();
        ID_InformacionCuenta.setValue(null);
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
}
