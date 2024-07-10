package org.example.bcn.Controller;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.example.bcn.Connection.DBConnection;
import java.net.URL;
import java.sql.*;
import org.example.bcn.Reportes.Transacciones;
import org.example.bcn.Reportes.ReporteC;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ResourceBundle;


public class ReportesForm implements Initializable {


    @FXML
    private TableView<Transacciones> TableTransactions;
    @FXML
    private TableColumn<Transacciones, Integer> IDTransaccion;
    @FXML
    private TableColumn<Transacciones, String> IDCliente;
    @FXML
    private TableColumn<Transacciones, Date> IDDate;
    @FXML
    private TableColumn<Transacciones, Double> IDPay;
    @FXML
    private TableColumn<Transacciones, String> IDDescripcion;
    @FXML
    private DatePicker IDFechaInit;
    @FXML
    private DatePicker IDFechaEnd;
    @FXML
    private Button IDFiltrar;
    @FXML
    private ObservableList<Transacciones> transaccionesList = FXCollections.observableArrayList();
    private Transacciones compraDAO;
    // Fin Tabla Reporte A
    @FXML
    private Button minimizeButton;
    @FXML
    private TableView<ReporteC> ReporteTarjetas;
    @FXML
    private TableColumn<ReporteC, Integer> CClienteID;
    @FXML
    private TableColumn<ReporteC, String> colNombre; //00149823 Nombre del cliente juento al id
    @FXML
    private TableColumn<ReporteC, String> colApellido; //00149824 Columna para el apellido del cliente
    @FXML
    private TableColumn<ReporteC, String> colNumTarjeta;
    @FXML
    private TableColumn<ReporteC, String> colTipoTarjeta;

    // Funcion para configurar la tabla y cargar datos
    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        transaccionesList = FXCollections.observableArrayList();
        // Configuración de las columnas del TableView
        IDTransaccion.setCellValueFactory(cellData -> cellData.getValue().idTransaccionProperty().asObject());
        IDCliente.setCellValueFactory(cellData -> cellData.getValue().idClienteProperty());
        IDDate.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Transacciones, Date>, ObservableValue<Date>>() {
            @Override
            public ObservableValue<Date> call(TableColumn.CellDataFeatures<Transacciones, Date> cellData) {
                ObjectProperty<java.util.Date> dateProperty = cellData.getValue().dateProperty();
                return new SimpleObjectProperty<>(new Date(dateProperty.get().getTime()));
            }
        });
        IDPay.setCellValueFactory(cellData -> cellData.getValue().payProperty().asObject());
        IDDescripcion.setCellValueFactory(cellData -> cellData.getValue().descripcionProperty());
        //Fin configuracion de las columnas del TableView
      if (IDFiltrar != null) {
            IDFiltrar.setOnAction(this::buscarCompras);
     }
        cargarDatosTabla();

    }

    @FXML
    private void HandleMinimizeButton(ActionEvent event) {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }



    @FXML
    protected void buscarCompras(ActionEvent event) {
        LocalDate fechaInicio = IDFechaInit.getValue();
        LocalDate fechaFin = IDFechaEnd.getValue();

        if (fechaInicio == null || fechaFin == null || fechaInicio.isAfter(fechaFin)) {
            System.out.println("Error: Seleccione un rango de fechas válido.");
            return;
        }

        // Obtener el cliente seleccionado de la tabla ReporteTarjetas
        ReporteC clienteSeleccionado = ReporteTarjetas.getSelectionModel().getSelectedItem();
        if (clienteSeleccionado == null) {
            System.out.println("Error: No se ha seleccionado ningún cliente.");
            return;
        }

        int idClienteSeleccionado = clienteSeleccionado.getId_Cliente();

        try {
            Connection connection = DBConnection.getInstance().getConnection();
            String query = "SELECT s.TransactionID, ce.NameCliente, s.DateShopping, s.TotalAmount, s.DescriptionShopping " +
                    "FROM Smart_Shopping s " +
                    "INNER JOIN Cards c ON c.CardID = s.CardID " +
                    "INNER JOIN Cliente ce ON ce.ClienteID = c.ClienteID " +
                    "WHERE ce.ClienteID = ? AND s.DateShopping BETWEEN ? AND ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, idClienteSeleccionado); // Usamos el ID del cliente seleccionado
            statement.setDate(2, Date.valueOf(fechaInicio));
            statement.setDate(3, Date.valueOf(fechaFin));

            ResultSet rs = statement.executeQuery();
            transaccionesList.clear(); // Limpiar la lista antes de agregar nuevas transacciones

            while (rs.next()) {
                int idTransaccion = rs.getInt("TransactionID");
                String idCliente = rs.getString("NameCliente");
                Date date = rs.getDate("DateShopping");
                double pay = rs.getDouble("TotalAmount");
                String descripcion = rs.getString("DescriptionShopping");

                transaccionesList.add(new Transacciones(idTransaccion, idCliente, date, pay, descripcion));
            }

            // Asignar los datos filtrados a la TableView en el hilo de JavaFX
            Platform.runLater(() -> {
                TableTransactions.setItems(transaccionesList);
            });

            System.out.println("Número de transacciones encontradas: " + transaccionesList.size());

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error al ejecutar la consulta SQL: " + e.getMessage());
        }
    }

    // Método para cargar todos los datos de transacciones en la TableView
    private void cargarDatosTabla() {
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            String query = "SELECT s.TransactionID, ce.NameCliente, s.DateShopping, s.TotalAmount, s.DescriptionShopping " +
                    "FROM Smart_Shopping s " +
                    "INNER JOIN Cards c ON c.CardID = s.CardID " +
                    "INNER JOIN Cliente ce ON ce.ClienteID = c.ClienteID";
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);

            transaccionesList.clear(); // Limpiar la lista antes de agregar nuevas transacciones

            while (rs.next()) {
                int idTransaccion = rs.getInt("TransactionID");
                String idCliente = rs.getString("NameCliente");
                Date date = rs.getDate("DateShopping");
                double pay = rs.getDouble("TotalAmount");
                String descripcion = rs.getString("DescriptionShopping");

                transaccionesList.add(new Transacciones(idTransaccion, idCliente, date, pay, descripcion));
            }

            // Asignar los datos a la TableView en el hilo de JavaFX
            Platform.runLater(() -> {
                TableTransactions.setItems(transaccionesList);
            });

            System.out.println("Número de transacciones encontradas: " + transaccionesList.size());

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error al ejecutar la consulta SQL: " + e.getMessage());
        }
    }



//    private ObservableList<ReporteC> getTarjetasFromDatabase () { //00149823 Esta funcion obtiene los observables de cada columna
//        ObservableList<ReporteC> ReporteC = FXCollections.observableArrayList();
//
//
//        String query = "SELECT ce.ClienteID, ce.NameCliente AS Nombre, ce.LastNameCliente AS Apellido, c.CardNumber " +
//                "FROM Cards AS c " + "INNER JOIN Cliente AS ce ON ce.ClienteID = c.ClienteID"; //00149823 Consulta para poder recuprar las tarjetas y los id de los clientes
//
//        try (Connection conn = DBConnection.getInstance().getConnection(); //00149823 Conexion con la base de datos mediante la instancia ya creada para que no haya repeticion
//             Statement stmt = conn.createStatement(); //000149823 Se crea un statement para la conexion
//             ResultSet rs = stmt.executeQuery(query)) { //00149823 El resultado del statement con la consulta
//
//            while (rs.next()) {
//                int id_Cliente = rs.getInt("ClienteID"); //00149823 Se agrega el id del cliente al resultado de la statement
//                String nombre = rs.getString("NameCliente"); //00149823 Se agrega el nombre del cliente al resultado de la statement
//                String apellido = rs.getString("LastNameCliente"); //00149823 Se agrega el apellido del cliente al resultado de la statement
//                String numeroCuenta = rs.getString("CardNumber");
//                String tipo_tarjeta = rs.getString("CardType");
//
//                ReporteC reporte = new ReporteC(id_Cliente,nombre, apellido);
//
//                if(tipo_tarjeta.equalsIgnoreCase("Tarjeta Credito")) {
//                    reporte.getTarjetasCredito().add(numeroCuenta);
//                } else if(tipo_tarjeta.equalsIgnoreCase("Tarjeta Debito")) {
//                    reporte.getTarjetasDebito().add(numeroCuenta);
//                }
//
//                ReporteC.add(reporte);
//
//            }
//
//            for (ReporteC reporte : ReporteC){
//                reporte.setTipo_tarjeta(reporte.getTarjetas());
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//
//        return ReporteC;
//
//    }
}


