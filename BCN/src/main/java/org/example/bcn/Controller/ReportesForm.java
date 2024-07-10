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
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.example.bcn.Connection.DBConnection;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.URL;
import java.sql.*;

import org.example.bcn.Modelos.ReporteB;
import org.example.bcn.Modelos.ReporteD;
import org.example.bcn.Modelos.Transacciones;
import org.example.bcn.Modelos.ReporteC;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;


public class ReportesForm implements Initializable {


    // se inicializan todos los componentes que forman parte de formulario historial de transacciones
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


    // se inicializan las listas para cada historial
    private ObservableList<Transacciones> transaccionesList = FXCollections.observableArrayList();
    private ObservableList<ReporteC> reporteList = FXCollections.observableArrayList();
    private ObservableList<ReporteB> reporteBS = FXCollections.observableArrayList();
    private ObservableList<ReporteD> reporteDS = FXCollections.observableArrayList();
    // se declara la instancia de la clase transacciones para traer la informacion
    private Transacciones compraDAO;
    // Fin Tabla Reporte A

    // se inicializan los componentes que forman parte del formulario reporte de tarjetas
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

    // se inicializan los componentes que forman parte del formulario reporte de compras por cliente
    @FXML
    private DatePicker IDSetFechaInit;
    @FXML
    private DatePicker IDSetFechaEnd;
    @FXML
    private Button filtrar;

    // inicializan los componentes que forman parte del formulario facilitadores-clientes

    @FXML
    private TableView<ReporteD> TablaFacilitadores;
    @FXML
    private TableColumn<ReporteD, String> Clients;
    @FXML
    private TableColumn<ReporteD, Integer> Cantidad;
    @FXML
    private TableColumn<ReporteD, Double> GastoT;
    @FXML
    private Button Historial;
    @FXML
    private ComboBox<String> FacilitadoresConsulta;

    @FXML
    private Button IDSave2;
    @FXML
    private Button IDSave;
    @FXML
    private Button IDSave1;
    @FXML
    private Button IDSave4;
    // Funcion para configurar la tabla y cargar datos
    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        transaccionesList = FXCollections.observableArrayList();
        reporteList = FXCollections.observableArrayList();
        reporteBS = FXCollections.observableArrayList();
        reporteDS = FXCollections.observableArrayList();
        // Configuración de las columnas del TableView
        IDTransaccion.setCellValueFactory(cellData -> cellData.getValue().idTransaccionProperty().asObject());
        IDCliente.setCellValueFactory(cellData -> cellData.getValue().nameClienteProperty());
        // hace una llamada de callback es decir de regreso a la columna Date y se le menciona que los datos insertados seran los datos asignado de una visualizacion de valores
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

        Cliente.setCellValueFactory(cellData -> cellData.getValue().nameClienteProperty());
        Fecha.setCellValueFactory(cellData -> cellData.getValue().dateProperty().asString());
        Gastos.setCellValueFactory(cellData -> cellData.getValue().payProperty().asObject().asString());

        Clients.setCellValueFactory(cellData -> cellData.getValue().nombre_clienteProperty());
        Cantidad.setCellValueFactory(cellData -> cellData.getValue().idTransaccionProperty().asObject());
        GastoT.setCellValueFactory(cellData -> cellData.getValue().pagoTotalProperty().asObject());


        // Listener para la selección de filas
        TableTransactions.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                System.out.println("Cliente seleccionado: " + newSelection.clienteidProperty());
            }
        });
        TablaB.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection!= null) {
                System.out.println("Cliente seleccionado: " + newSelection.getIdCliente());
            }
        });
        TableTransactions.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        TablaB.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        // se valida qu si la accion es distinta de null entonces el metodo se asigna al boton de filtrar de l primer reporte
        if (IDFiltrar != null) {
            IDFiltrar.setOnAction(this::buscarCompras);
        }
        // se le asigna al boton una accion y se asigna como evento a la funcion filtrar fechas
        filtrar.setOnAction(event -> filtrarPorFechas());
        IDSave2.setOnAction(event -> guardarReporteHistorial());
        IDSave.setOnAction(event -> guardarHistorialTransacciones());
        IDSave1.setOnAction(event -> guardarReportePagos());
        IDSave4.setOnAction(event -> generarReporteTarjetas());
        // funciones que cargan e inicializan los campos de las tablas
        cargarDatosTabla();
        inicializarTabla();
        cargarDatos();
        cargarPagos();
        CargarHistorial();
        Historial.setOnAction(event -> BuscarHistorial_FacilitadoresTarjertas());
        try {
            CargarFacilitador();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    private void HandleMinimizeButton(ActionEvent event) {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }




    @FXML
    protected void buscarCompras(ActionEvent event) {
        // se obteienen las fechas seleccionadas por el usuario
        LocalDate fechaInicio = IDFechaInit.getValue();
        LocalDate fechaFin = IDFechaEnd.getValue();
        // se valida si el rango de fechas seleccionados estan dentro de los parametros es decir que cumple con una linea de tiempo en la que el cliente realizo una compra
        // si la validacion falla se asigna un mensaje de error

        if (fechaInicio == null || fechaFin == null || fechaInicio.isAfter(fechaFin)) {
            System.out.println("Error: Seleccione un rango de fechas válido.");
            return;
        }

        // Obtener el cliente seleccionado de la tabla TableTransactions
        Transacciones clienteSeleccionado = TableTransactions.getSelectionModel().getSelectedItem();
        if (clienteSeleccionado == null) {
            System.out.println("Error: No se ha seleccionado ningún cliente.");
            return;
        }

        // se guarda el valor como int
        int idClienteSeleccionado = clienteSeleccionado.getClienteid();
        // se genera un try-catch para tratar de realizar la inseerción de esa manera poder manejar casos de excepcion en caso existiera un problema en el sistema
        try {
            Connection connection = DBConnection.getInstance().getConnection(); // al ser una instancia, se declara una connection que permitira el paso de la comunicacion de la coneccion al controller
            // se realiza la consulta con los parametros del cliente seleccionado y el rango de fechas seleccionado
            String query = "SELECT s.TransactionID, ce.ClienteID, ce.NameCliente, s.DateShopping, s.TotalAmount, s.DescriptionShopping " +
                    "FROM Smart_Shopping s " +
                    "INNER JOIN Cards c ON c.CardID = s.CardID " +
                    "INNER JOIN Cliente ce ON ce.ClienteID = c.ClienteID " +
                    "WHERE ce.ClienteID = ? AND s.DateShopping BETWEEN ? AND ?";
            // se genera la preparacion de la sentencia sql
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, idClienteSeleccionado);
            statement.setDate(2, Date.valueOf(fechaInicio));
            statement.setDate(3, Date.valueOf(fechaFin));
            // se ejecuta la consulta
            ResultSet rs = statement.executeQuery();
            transaccionesList.clear(); // Limpiar la lista antes de agregar nuevas transacciones

            // hacemos un while que permitira el muestreo de las consultas en caso de que no exista ningun problema al generar la consulta
            while (rs.next()) {
                int idTransaccion = rs.getInt("TransactionID");
                int clienteid = rs.getInt("ClienteID");
                String nameCliente = rs.getString("NameCliente");
                Date date = rs.getDate("DateShopping");
                double pay = rs.getDouble("TotalAmount");
                String descripcion = rs.getString("DescriptionShopping");
                // se carga la tabla de transacciones

                transaccionesList.add(new Transacciones(idTransaccion, clienteid, nameCliente, date, pay, descripcion));
            }

            // Asignar los datos filtrados a la TableView en el hilo de JavaFX
            Platform.runLater(() -> {
                TableTransactions.setItems(transaccionesList);
            });

            // se depura para visualizar la cantidad de trannsaccione que entran a la tabla
            System.out.println("Número de transacciones encontradas: " + transaccionesList.size());

            // en caso d excepciones se muestra el mensaje de error
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error al ejecutar la consulta SQL: " + e.getMessage());
        }
    }



    // Método para cargar todos los datos de transacciones en la TableView
    private void cargarDatosTabla() {
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            String query = "SELECT s.TransactionID, ce.ClienteID, ce.NameCliente, s.DateShopping, s.TotalAmount, s.DescriptionShopping " +
                    "FROM Smart_Shopping s " +
                    "INNER JOIN Cards c ON c.CardID = s.CardID " +
                    "INNER JOIN Cliente ce ON ce.ClienteID = c.ClienteID";
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);

            transaccionesList.clear(); // Limpiar la lista antes de agregar nuevas transacciones

            while (rs.next()) {
                int idTransaccion = rs.getInt("TransactionID");
                int clienteid = rs.getInt("ClienteID");
                String nameCliente = rs.getString("NameCliente");
                Date date = rs.getDate("DateShopping");
                double pay = rs.getDouble("TotalAmount");
                String descripcion = rs.getString("DescriptionShopping");

                transaccionesList.add(new Transacciones(idTransaccion, clienteid, nameCliente, date, pay, descripcion));
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
                // verifica si la tarjeta pertennece al cliente de ser asi asigna las tarjetas correspondientes
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
            String query = "SELECT s.TransactionID, ce.ClienteID, ce.NameCliente, s.DateShopping, s.TotalAmount " +
                    "FROM Smart_Shopping s " +
                    "INNER JOIN Cards c ON c.CardID = s.CardID " +
                    "INNER JOIN Cliente ce ON ce.ClienteID = c.ClienteID";

            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);

            reporteBS.clear(); // Limpiar la lista antes de agregar nuevas transacciones

            while (rs.next()) {
                int idTransaccion = rs.getInt("TransactionID");
                int idCliente = rs.getInt("ClienteID");
                String nameCliente = rs.getString("NameCliente");
                String date = rs.getString("DateShopping");
                double pay = rs.getDouble("TotalAmount");

                reporteBS.add(new ReporteB(idTransaccion, idCliente, nameCliente, LocalDate.parse(date), pay));
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
    /**
     * Filtra y muestra los gastos totales de un cliente para un mes o año específico.
     * Se utiliza un DatePicker para seleccionar el año y mes para filtrar los datos.
     */
    protected void filtrarPorFechas() {
        // Obtener las fechas de inicio y fin seleccionadas por el usuario
        LocalDate fechaInicio, fechaFin;

        // Obtener el año seleccionado del DatePicker
        int year = IDSetFechaInit.getValue().getYear();

        // Obtener el mes seleccionado del DatePicker
        Month month = IDSetFechaEnd.getValue().getMonth();

        // Validar que se haya seleccionado un año válido
        if (year == 0) {
            showAlert("Error", "Seleccione un año válido.");
            return;
        }

        try {
            // Establecer conexión con la base de datos
            Connection connection = DBConnection.getInstance().getConnection();

            // Construir las fechas de inicio y fin según la selección del usuario
            if (month != null) {
                // Si se seleccionó un mes, filtrar por ese mes específico
                fechaInicio = LocalDate.of(year, month, 1);
                fechaFin = fechaInicio.plusMonths(1).minusDays(1);
            } else {
                // Si no se seleccionó un mes, filtrar por todo el año seleccionado
                fechaInicio = LocalDate.of(year, Month.JANUARY, 1);
                fechaFin = LocalDate.of(year, Month.DECEMBER, 31);
            }

            // Obtener el cliente seleccionado de la tabla ReporteTarjetas
            ReporteB clienteSeleccionado = TablaB.getSelectionModel().getSelectedItem();
            if (clienteSeleccionado == null) {
                System.out.println("Error: No se ha seleccionado ningún cliente.");
                return;
            }

            // Obtener el ID del cliente seleccionado
            int idClienteSeleccionado = clienteSeleccionado.getIdCliente();

            // Consulta SQL para obtener los datos necesarios
            String query = "SELECT ce.NameCliente, FORMAT(s.DateShopping, 'MMMM yyyy', 'es-ES') AS Fecha, " +
                    "       SUM(s.TotalAmount) AS TotalGastado " +
                    "FROM Smart_Shopping s " +
                    "INNER JOIN Cards c ON c.CardID = s.CardID " +
                    "INNER JOIN Cliente ce ON ce.ClienteID = c.ClienteID " +
                    "WHERE ce.ClienteID = ? AND s.DateShopping BETWEEN ? AND ? " +
                    "GROUP BY ce.NameCliente, FORMAT(s.DateShopping, 'MMMM yyyy', 'es-ES')";

            // Preparar la consulta SQL con parámetros
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, idClienteSeleccionado);
            statement.setDate(2, java.sql.Date.valueOf(fechaInicio));
            statement.setDate(3, java.sql.Date.valueOf(fechaFin));

            // Ejecutar la consulta y obtener los resultados
            ResultSet rs = statement.executeQuery();
            reporteBS.clear(); // Limpiar la lista antes de agregar nuevos datos


            // Recorrer los resultados obtenidos
            while (rs.next()) {
                // Obtener los datos del resultado
                String nameCliente = rs.getString("NameCliente");
                String fecha = rs.getString("Fecha");
                double totalGasto = rs.getDouble("TotalGastado");

                // Agregar un nuevo objeto ReporteB a la lista reporteBS
                reporteBS.add(new ReporteB(0, idClienteSeleccionado, nameCliente, convertirAFecha(fecha), totalGasto));

            }

            // Actualizar la tabla con los datos y mostrar el total gastado
            TablaB.setItems(reporteBS);

        } catch (SQLException e) {
            // Mostrar un mensaje de error si hay problemas con la consulta SQL
            showAlert("Error", "Error al ejecutar la consulta SQL: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Convierte una cadena de texto en formato "MMMM yyyy" a un objeto LocalDate.
     *
     * @param fechaString La cadena de texto con la fecha en formato "MMMM yyyy".
     * @return Objeto LocalDate correspondiente a la fecha.
     */
    private LocalDate convertirAFecha(String fechaString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy", Locale.forLanguageTag("es-ES"));
        return YearMonth.parse(fechaString, formatter).atDay(1);
    }

    // funcion que cargar los datos a la tabla facilitadores
    private void CargarHistorial() {
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            // Consulta SQL para obtener los datos necesarios
            String UploadSQL = "SELECT ce.NameCliente, COUNT(s.TransactionID) AS CantidadCompras, " +
                    "       SUM(s.TotalAmount) AS TotalGastado " +
                    "FROM Smart_Shopping s " +
                    "INNER JOIN Cards c ON c.CardID = s.CardID " +
                    "INNER JOIN Cliente ce ON ce.ClienteID = c.ClienteID " +
                    "INNER JOIN Facilitator f ON f.FacilitatorID = c.FacilitatorID " +
                    "GROUP BY ce.NameCliente";

            // Preparar la consulta SQL
            Statement st = connection.createStatement();
            // Ejecutar la consulta y obtener los resultados
            ResultSet rsa = st.executeQuery(UploadSQL);
            // Limpiar la lista antes de agregar nuevos datos
            reporteDS.clear();
            // Recorrer los resultados obtenidos
            while(rsa.next()) {
                // Obtener los datos del resultado
                String nombreCliente = rsa.getString("NameCliente");
                int cantidadCompras = rsa.getInt("CantidadCompras");
                double totalGastado = rsa.getDouble("TotalGastado");
                // Agregar un nuevo objeto ReporteD a la lista reporteDS
                reporteDS.add(new ReporteD(0, nombreCliente, cantidadCompras, totalGastado));
            }
            // Actualizar la tabla con los datos y mostrar el total gastado
            TablaFacilitadores.setItems(reporteDS);
        }catch (SQLException e) {
            // Mostrar un mensaje de error si hay problemas con la consulta SQL
            showAlert("Error", "Error al ejecutar la consulta SQL: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // funcion que filtrara los faciltadores y generara los resultados deseados
    @FXML
    protected  void  BuscarHistorial_FacilitadoresTarjertas () {
        String opcionFacilitador = FacilitadoresConsulta.getValue();

        if (opcionFacilitador == null) {
            showAlert("Error", "Seleccione un facilitador.");
            return;
        }

        try {
            Connection connection = DBConnection.getInstance().getConnection();
            // Consulta SQL para obtener los datos necesarios
            String query = "SELECT ce.ClienteID,  ce.NameCliente, COUNT(s.TransactionID) AS CantidadCompras, " +
                            "SUM(s.TotalAmount) AS TotalGastado " +
                            "FROM Smart_Shopping s " +
                            "INNER JOIN Cards c ON c.CardID = s.CardID " +
                            "INNER JOIN Cliente ce ON ce.ClienteID = c.ClienteID " +
                            "INNER JOIN Facilitator f ON f.FacilitatorID = c.FacilitatorID " +
                            "WHERE f.FacilitatorName = ? " +
                            "GROUP BY ce.ClienteID, ce.NameCliente";
            // Preparar la consulta SQL con el nombre del facilitador
            PreparedStatement statement =  connection.prepareStatement(query);
            statement.setString(1, opcionFacilitador);
            // Ejecutar la consulta y obtener los resultados
            ResultSet rs = statement.executeQuery();
            // Limpiar la lista antes de agregar nuevos datos
            reporteDS.clear();
            // Recorrer los resultados obtenidos
            while(rs.next()) {

                int CantidadCompras = rs.getInt("CantidadCompras");
                String NameCliente = rs.getString("NameCliente");
                double GastoTotal = rs.getDouble("TotalGastado");

                reporteDS.add(new ReporteD(0, NameCliente, CantidadCompras, GastoTotal));
            }
            TablaFacilitadores.setItems(reporteDS);

        } catch (SQLException e) {
            showAlert("Error", "Error al ejecutar la consulta  SQL :" + e.getMessage());
            e.printStackTrace();
        }
    }


    // funcion quee carga los facilitadores al combo box
    private void CargarFacilitador() throws SQLException {
        // Obtener la conexión con la base de datos
        Connection connection = DBConnection.getInstance().getConnection();
        // Consulta SQL para obtener los nombres de los facilitadores
        try (Statement st = connection.createStatement(); ResultSet rs = st.executeQuery("SELECT FacilitatorName FROM Facilitator")) {
            // Crear una lista observable para almacenar los nombres de los facilitadores
            ObservableList<String> opciones = FXCollections.observableArrayList();
            // Recorrer los resultados obtenidos
            while (rs.next()) {
                opciones.add(rs.getString("FacilitatorName"));
            }
            FacilitadoresConsulta.setItems(opciones);
        }
    }

    // Funcion para obtener el id de los facilitadores
    private int ObtenerFacilitador(Connection connection, String facilitador) throws SQLException {
        // Consulta SQL para obtener el ID del facilitador
        String query = "SELECT FacilitatorID FROM Facilitator WHERE FacilitatorName = ?";
        // Preparar la consulta SQL con el nombre del facilitador
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            // Establecer el nombre del facilitador al parámetro de la consulta
            ps.setString(1, facilitador);
            // Ejecutar la consulta y obtener el resultado
            ResultSet rs = ps.executeQuery();
            // Validar que el facilitador sea encontrado en la base de datos
            if (rs.next()) {
                // se devuelve el ID del facilitador
                return rs.getInt("FacilitatorID");
            } else {
                // se lanza  una excepción si el facilitador no se encuentra en la base de datos
                throw new SQLException("No se encontró el facilitador: " + facilitador);
            }
        }
    }
    // Funcion para mostrar un mensaje de alerta
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private void showAlert2(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    // funciones para generar los archivos de reportes
    protected void guardarReporteHistorial() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm-ss");
        String fechaHora = now.format(formatter);

        String rutaInicial = "src/main/java/org/example/bcn/Reportes";
        String nombreArchivo = "Reporte D - " + fechaHora + ".txt";

        // Crear el directorio inicial como un File
        File directorioInicial = new File(rutaInicial);

        // Crear el archivo seleccionado como un File en el directorio inicial
        File selectedFile = new File(directorioInicial, nombreArchivo);

        // Configurar el FileChooser para seleccionar la ubicación de guardado
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(directorioInicial);
        fileChooser.setInitialFileName(nombreArchivo);
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos de texto (*.txt)", "*.txt"));

        // Mostrar el diálogo para seleccionar la ubicación de guardado
        File archivoGuardado = fileChooser.showSaveDialog(new Stage());

        if (archivoGuardado != null) {
            try (FileWriter writer = new FileWriter(archivoGuardado)) {
                // Construir el contenido completo con todos los registros
                StringBuilder contenido = new StringBuilder();
                for (int i = 0; i < reporteDS.size(); i++) {
                    String nameCliente = reporteDS.get(i).getNombre_cliente();
                    int cantidadCompras = reporteDS.get(i).getIdTransaccion();
                    double totalGastado = reporteDS.get(i).getPagoTotal();

                    contenido.append("Cliente: ").append(nameCliente).append("\n")
                            .append("Cantidad de compras: ").append(cantidadCompras).append("\n")
                            .append("Total gastado: $").append(totalGastado).append("\n\n");
                }

                // Escribir todo el contenido en el archivo
                writer.write(contenido.toString());

                showAlert2("Éxito", "Reporte guardado como: " + archivoGuardado.getName());
            } catch (IOException e) {
                showAlert2("Error", "Error al guardar el reporte: " + e.getMessage());
            }
        }
    }

    @FXML
    protected void guardarHistorialTransacciones() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm-ss");
        String fechaHora = now.format(formatter);

        String rutaInicial = "src/main/java/org/example/bcn/Reportes";
        String nombreArchivo = "Reporte A - " + fechaHora + ".txt";

        // Crear el directorio inicial como un File
        File directorioInicial = new File(rutaInicial);

        // Crear el archivo seleccionado como un File en el directorio inicial
        File selectedFile = new File(directorioInicial, nombreArchivo);

        // Configurar el FileChooser para seleccionar la ubicación de guardado
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(directorioInicial);
        fileChooser.setInitialFileName(nombreArchivo);
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos de texto (*.txt)", "*.txt"));

        // apertura de el diálogo para seleccionar la ubicación de guardado
        File archivoGuardado = fileChooser.showSaveDialog(new Stage());

        if (archivoGuardado != null) {
            try (FileWriter writer = new FileWriter(archivoGuardado)) {
                StringBuilder contenidoTotal = new StringBuilder();

                for (Transacciones transaccion : transaccionesList) {
                    // se llaman a la clase transacciones que el modelo que guarda todas las variables
                    int idTransaccion = transaccion.getIdTransaccion();
                    String nameCliente = transaccion.getNameCliente();
                    java.sql.Date date = (java.sql.Date) transaccion.getDate();
                    double pagoTotal = transaccion.getPay();
                    String descripcion = transaccion.getDescripcion();

                    String contenido = "ID Transaccion: " + idTransaccion + "\n" +
                            "Cliente: " + nameCliente + "\n" +
                            "Fecha: " + date + "\n" +
                            "Total gastado: $" + pagoTotal + "\n" +
                            "Descripción: " + descripcion + "\n\n";

                    contenidoTotal.append(contenido);
                }

                // Escribir el contenido total en el archivo seleccionado
                writer.write(contenidoTotal.toString());

                showAlert2("Éxito", "Reporte guardado como: " + archivoGuardado.getName());
            } catch (IOException e) {
                showAlert2("Error", "Error al guardar el reporte: " + e.getMessage());
            }
        }
    }


    @FXML
    protected void guardarReportePagos() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm-ss");
        String fechaHora = now.format(formatter);

        String rutaInicial = "src/main/java/org/example/bcn/Reportes";
        String nombreArchivo = "Reporte B - " + fechaHora + ".txt";

        // Crear el directorio inicial como un File
        File directorioInicial = new File(rutaInicial);

        // Configurar el FileChooser para seleccionar la ubicación de guardado
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(directorioInicial);
        fileChooser.setInitialFileName(nombreArchivo);
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos de texto (*.txt)", "*.txt"));

        // Mostrar el diálogo para seleccionar la ubicación de guardado
        File archivoGuardado = fileChooser.showSaveDialog(new Stage());

        if (archivoGuardado != null) {
            try (FileWriter writer = new FileWriter(archivoGuardado)) {
                StringBuilder contenidoTotal = new StringBuilder();

                for (ReporteB reporte : reporteBS) {
                    int idTransaccion = reporte.getIdTransaccion();
                    int idCliente = reporte.getIdCliente();
                    String nameCliente = reporte.getNameCliente();
                    LocalDate fecha = reporte.getDate();
                    double totalGastado = reporte.getPay();

                    String contenido = "ID Transacción: " + idTransaccion + "\n" +
                            "ID Cliente: " + idCliente + "\n" +
                            "Cliente: " + nameCliente + "\n" +
                            "Fecha de compra: " + fecha + "\n" +
                            "Total gastado: $" + totalGastado + "\n\n";

                    contenidoTotal.append(contenido);
                }

                // Escribir el contenido total en el archivo seleccionado
                writer.write(contenidoTotal.toString());

                showAlert("Éxito", "Reporte guardado como: " + archivoGuardado.getName());
            } catch (IOException e) {
                showAlert("Error", "Error al guardar el reporte: " + e.getMessage());
            }
        }
    }

    @FXML
    protected void generarReporteTarjetas() {
        // se obtiene  la lista de ReporteC desde la base de datos
        ObservableList<ReporteC> reporteCList = getTarjetasFromDatabase();

        // se verifica si se encontraron datos en la base de datos
        if (reporteCList.isEmpty()) {
            showAlert("Información", "No se encontraron datos para generar el reporte.");
            return;
        }

        // se obtiene la fecha y hora actual para el nombre del archivo
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm-ss");
        String fechaHora = now.format(formatter);

        // Nombre del archivo de reporte
        String nombreArchivo = "Reporte C - " + fechaHora + ".txt";

        // Configurar el FileChooser para seleccionar la ubicación de guardado
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("src/main/java/org/example/bcn/Reportes"));
        fileChooser.setInitialFileName(nombreArchivo);
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos de texto (*.txt)", "*.txt"));

        // se muestra  el diálogo para seleccionar la ubicación de guardado
        File archivoGuardado = fileChooser.showSaveDialog(new Stage());

        // se guarda el contenido del reporte en el archivo seleccionado
        if (archivoGuardado != null) {
            // se realiza un try catch para intentar generar un nuevo archivo para guardar
            try (FileWriter writer = new FileWriter(archivoGuardado)) {
                // Se crea un StringBuilder para almacenar el contenido total del reporte
                StringBuilder contenidoTotal = new StringBuilder();

                for (ReporteC reporteC : reporteCList) {
                    contenidoTotal.append("Cliente: ").append(reporteC.getNombre()).append(" ").append(reporteC.getApellido()).append("\n");

                    // se obtienen y guardan las tarjetas de crédito del cliente
                    List<String> tarjetasCredito = reporteC.getTarjetasCredito();
                    if (!tarjetasCredito.isEmpty()) {
                        contenidoTotal.append(" - Tarjetas de crédito:\n");
                        for (String tarjeta : tarjetasCredito) {
                            contenidoTotal.append("   * ").append(tarjeta).append("\n");
                        }
                    }

                    // se obtiene y guardanr las tarjetas de débito del cliente
                    List<String> tarjetasDebito = reporteC.getTarjetasDebito();
                    if (!tarjetasDebito.isEmpty()) {
                        contenidoTotal.append(" - Tarjetas de débito:\n");
                        for (String tarjeta : tarjetasDebito) {
                            contenidoTotal.append("   * ").append(tarjeta).append("\n");
                        }
                    }

                    contenidoTotal.append("\n");
                }

                // se escribe el contenido total en el archivo seleccionado
                writer.write(contenidoTotal.toString());

                showAlert("Éxito", "Reporte de tarjetas guardado como: " + archivoGuardado.getName());
            } catch (IOException e) {
                showAlert("Error", "Error al guardar el reporte: " + e.getMessage());
            }
        }
    }



}










