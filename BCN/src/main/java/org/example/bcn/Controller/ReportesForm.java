package org.example.bcn.Controller;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.example.bcn.Connection.DBConnection;
import java.net.URL;
import java.sql.*;

import org.example.bcn.Reportes.ReporteB;
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
    private ObservableList<ReporteC> reporteList = FXCollections.observableArrayList();

    private ObservableList<ReporteB> reporteBS = FXCollections.observableArrayList();
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
    private TableColumn<ReporteB, String> Transaccion; //00149825 Columna para el número de tarjeta del cliente
    @FXML
    private TableView<ReporteB> TablaB;
    @FXML
    private TableColumn<ReporteB, String> Cliente; //00149826 Columna para el nombre del cliente
    @FXML
    private TableColumn<ReporteB, String> Fecha;
    @FXML
    private TableColumn<ReporteB, String> Gastos;

    @FXML
    private DatePicker IDSetFechaInit;
    @FXML
    private DatePicker IDSetFechaEnd;
    @FXML
    private Label lblTotalGastado;
    @FXML
    private TableColumn<ReporteC, String> colTipoTarjeta;
    @FXML
    private Button filtrar;

    // Funcion para configurar la tabla y cargar datos
    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        transaccionesList = FXCollections.observableArrayList();
        reporteList = FXCollections.observableArrayList();
        reporteBS = FXCollections.observableArrayList();
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
        Transaccion.setCellValueFactory(new PropertyValueFactory<>("idTransaccion"));
        Cliente.setCellValueFactory(new PropertyValueFactory<>("idCliente"));
        Fecha.setCellValueFactory(new PropertyValueFactory<>("date"));
        Gastos.setCellValueFactory(new PropertyValueFactory<>("pay"));


        if (IDFiltrar != null) {
            IDFiltrar.setOnAction(this::buscarCompras);
        }
        filtrar.setOnAction(event -> filtrarPorFechas());
        cargarDatosTabla();
        inicializarTabla();
        cargarDatos();
        cargarPagos();

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
        Transacciones clienteSeleccionado = TableTransactions.getSelectionModel().getSelectedItem();
        if (clienteSeleccionado == null) {
            System.out.println("Error: No se ha seleccionado ningún cliente.");
            return;
        }

        int idClienteSeleccionado = Integer.parseInt(clienteSeleccionado.getIdCliente());

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


    private void inicializarTabla() {
        CClienteID.setCellValueFactory(cellData -> cellData.getValue().clienteIDProperty().asObject());
        colNombre.setCellValueFactory(cellData -> cellData.getValue().nombreProperty());
        colApellido.setCellValueFactory(cellData -> cellData.getValue().apellidoProperty());
        colNumTarjeta.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().tarjetasProperty()));

        ReporteTarjetas.setItems(reporteList);
    }

    private void cargarDatos() {
        reporteList.addAll(getTarjetasFromDatabase());

        for (ReporteC reporte : reporteList) {
            reporte.tarjetasProperty();
        }
    }

    private ObservableList<ReporteC> getTarjetasFromDatabase() {
        ObservableList<ReporteC> reporteCList = FXCollections.observableArrayList();
        String query = "SELECT ce.ClienteID, ce.NameCliente AS Nombre, ce.LastNameCliente AS Apellido, c.CardNumber, c.CardType " +
                "FROM Cards AS c " +
                "INNER JOIN Cliente AS ce ON ce.ClienteID = c.ClienteID";

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int clienteID = rs.getInt("ClienteID");
                String nombre = rs.getString("Nombre");
                String apellido = rs.getString("Apellido");
                String numeroTarjeta = rs.getString("CardNumber");
                String tipoTarjeta = rs.getString("CardType");

                boolean encontrado = false;
                for (ReporteC reporteC : reporteCList) {
                    if (reporteC.getClienteID() == clienteID) {
                        reporteC.agregarTarjeta(numeroTarjeta, tipoTarjeta);
                        encontrado = true;
                        break;
                    }
                }

                if (!encontrado) {
                    ReporteC reporteC = new ReporteC(clienteID, nombre, apellido);
                    reporteC.agregarTarjeta(numeroTarjeta, tipoTarjeta);
                    reporteCList.add(reporteC);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reporteCList;
    }

    private void cargarPagos() {
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            String query = "SELECT s.TransactionID, ce.NameCliente, s.DateShopping, s.TotalAmount " +
                    "FROM Smart_Shopping s " +
                    "INNER JOIN Cards c ON c.CardID = s.CardID " +
                    "INNER JOIN Cliente ce ON ce.ClienteID = c.ClienteID";

            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);

            reporteBS.clear(); // Limpiar la lista antes de agregar nuevas transacciones

            while (rs.next()) {
                int idTransaccion = rs.getInt("TransactionID");
                String idCliente = rs.getString("NameCliente");
                java.sql.Date date = rs.getDate("DateShopping");
                double pay = rs.getDouble("TotalAmount");

                reporteBS.add(new ReporteB(idTransaccion, idCliente, date, pay));
            }

            // Asignar los datos a la TableView en el hilo de JavaFX
            Platform.runLater(() -> {
                TablaB.setItems(reporteBS);
            });

            System.out.println("Número de transacciones encontradas: " + reporteBS.size());

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error al ejecutar la consulta SQL: " + e.getMessage());
        }
    }


    @FXML

    protected void filtrarPorFechas() {
        LocalDate fechaInicio = IDFechaInit.getValue();
        LocalDate fechaFin = IDFechaEnd.getValue();

        if (fechaInicio == null || fechaFin == null || fechaInicio.isAfter(fechaFin)) {
            showAlert("Error", "Seleccione un rango de fechas válido.");
            return;
        }

        try {
            Connection connection = DBConnection.getInstance().getConnection();
            String query = "SELECT TransactionID, NameCliente, DateShopping, TotalAmount " +
                    "FROM Smart_Shopping s " +
                    "INNER JOIN Cards c ON c.CardID = s.CardID " +
                    "INNER JOIN Cliente ce ON ce.ClienteID = c.ClienteID " +
                    "WHERE ce.ClienteID = ? AND s.DateShopping BETWEEN ? AND ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, 1); // Reemplazar con el ID del cliente (ejemplo: 1)
            statement.setDate(2, Date.valueOf(fechaInicio));
            statement.setDate(3, Date.valueOf(fechaFin));

            ResultSet rs = statement.executeQuery();
            transaccionesList.clear();

            double totalGastado = 0;
            while (rs.next()) {
                int idTransaccion = rs.getInt("TransactionID");
                String idCliente = rs.getString("NameCliente");
                Date date = rs.getDate("DateShopping");
                double pay = rs.getDouble("TotalAmount");

                reporteBS.add(new ReporteB(idTransaccion, idCliente, date, pay));
                totalGastado += pay;
            }

            TablaB.setItems(reporteBS);
            lblTotalGastado.setText("Total gastado: $" + totalGastado);

        } catch (SQLException e) {
            showAlert("Error", "Error al ejecutar la consulta SQL: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}






