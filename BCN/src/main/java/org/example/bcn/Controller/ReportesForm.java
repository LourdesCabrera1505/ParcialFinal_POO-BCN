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

    public void initialize() { // 00081523 Este método se llama cuando se inicializa el controlador en la interfaz. Establece la conexión a la base de datos utilizando la clase DBConnection
        try { // 00081523 Comienza un bloque try para manejar excepciones.
            connect = DBConnection.getInstance().getConnection(); // 00081523 Intenta obtener una conexión a la base de datos utilizando el método getConnection() de la clase DBConnection.
        } catch (SQLException e) { // 00081523 Captura cualquier excepción de tipo SQLException que pueda ocurrir durante la conexión a la base de datos y muestra la traza de la pila.
            e.printStackTrace(); // 00081523 Imprime el mensaje de la excepción
        }
    }

    @FXML
    public void generarReporteD(){ // 00081523 Este método se llama cuando se hace clic en el botón para generar el informe.
        String facilitador = facilitadorField.getText(); // 00081523 Obtiene el nombre del facilitador desde un campo de texto.
        if (!facilitador.isEmpty()){ // 00081523 Verifica si el campo del facilitador no está vacío.
            try{ // 00081523 Comienza un bloque try para manejar las excepciones
                // 00081523 Crea una consulta SQL para obtener información sobre los clientes que han realizado compras con el facilitador especificado.
            String sql = "SELECT C.ClienteID, C.NameCliente, C.LastNameCliente, COUNT(S.TransactionID) AS cantidad_compras, SUM(S.TotalAmount) AS total_gastado " +
                    "FROM Cliente C " +
                    "JOIN Cards Ca ON C.ClienteID = Ca.ClienteID " +
                    "JOIN Smart_Shopping S ON Ca.CardID = S.CardID " +
                    "JOIN Facilitator F ON Ca.FacilitatorID = F.FacilitatorID " +
                    "WHERE F.FacilitatorName = '" + facilitador + "' " +
                    "GROUP BY C.ClienteID, C.NameCliente, C.LastNameCliente";
            try(PreparedStatement statement = connect.prepareStatement(sql)){ // 00081523 Prepara una sentencia SQL utilizando la conexión establecida previamente.
            statement.setString(1, facilitador); // 00081523 Establece el valor del parámetro en la consulta SQL con el nombre del facilitador.
                try (ResultSet resultSet = statement.executeQuery()) { // 00081523 Ejecuta la consulta y obtiene un conjunto de resultados.
                    StringBuilder reporte = new StringBuilder();
                    while (resultSet.next()) { // 00081523 Itera a través de los resultados obtenidos.
                        int clienteId = resultSet.getInt("ClienteID"); // 00081523 Obtiene el ID del cliente del resultado.
                        String nombre = resultSet.getString("NameCliente"); // 00081523 Obtiene el nombre del cliente.
                        String apellido = resultSet.getString("LastNameCliente"); // 00081523 Obtiene el apellido del cliente.
                        int cantidadCompras = resultSet.getInt("cantidad_compras"); // 00081523 Obtiene la cantidad de compras realizadas por el cliente.
                        double totalGastado = resultSet.getDouble("total_gastado"); // 00081523 Obtiene el total gastado por el cliente

                        reporte.append("ID Cliente: ").append(clienteId).append("\n") // 00081523 Construye una cadena de texto con la información del cliente.
                                .append("Nombre Completo: ").append(nombre).append(" ").append(apellido).append("\n")
                                .append("Cantidad de Compras: ").append(cantidadCompras).append("\n")
                                .append("Total Gastado: ").append(totalGastado).append("\n")
                                .append("------------------------------------------\n");
                    }
                    reporteArea.setText(reporte.toString()); // 00081523 Establece el texto del área de informe con la cadena construida.
                    guardarReporteEnArchivo(reporte.toString(), facilitador); // 00081523 Guarda el informe en un archivo de texto.
                }
            }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Por favor, ingrese un facilitador de tarjeta."); // 00081523 Si el campo del facilitador está vacío, muestra un mensaje en la consola.
        }
            }

    private void guardarReporteEnArchivo(String reporte, String facilitador) { // 0081523 Este método guarda el informe en un archivo de texto.
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String fechaHora = sdf.format(new Date());
        String nombreArchivo = "Reportes/ReporteD_" + facilitador + ".txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(nombreArchivo, true))) {
            writer.write("Reporte D - Clientes que han realizado compras con el facilitador: " + facilitador);
            writer.newLine();
            writer.write("Fecha y Hora: " + fechaHora);
            writer.newLine();
            writer.write("******************************************");
            writer.newLine();
            writer.write(reporte);
            writer.write("*******************************************");
            writer.newLine();
            System.out.println("Reporte generado exitosamente: " + nombreArchivo);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
        main
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

        // Obtener el cliente seleccionado de la tabla ReporteTarjetas
        ReporteB clienteSeleccionado = TablaB.getSelectionModel().getSelectedItem();
        if (clienteSeleccionado == null) {
            System.out.println("Error: No se ha seleccionado ningún cliente.");
            return;
        }
        int idClienteSeleccionado = Integer.parseInt(clienteSeleccionado.getIdCliente());

        try {
            Connection connection = DBConnection.getInstance().getConnection();
            String query = "SELECT TransactionID, NameCliente, DateShopping, TotalAmount " +
                    "FROM Smart_Shopping s " +
                    "INNER JOIN Cards c ON c.CardID = s.CardID " +
                    "INNER JOIN Cliente ce ON ce.ClienteID = c.ClienteID " +
                    "WHERE ce.ClienteID = ? AND s.DateShopping BETWEEN ? AND ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, idClienteSeleccionado); // Reemplazar con el ID del cliente (ejemplo: 1)
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






