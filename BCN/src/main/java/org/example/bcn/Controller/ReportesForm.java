package org.example.bcn.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.stage.Stage;
import org.example.bcn.Connection.DBConnection;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.example.bcn.Connection.DBConnection;
import org.example.bcn.ModeloTransacciones.Transacciones;
import org.example.bcn.Reportes.ReporteA;
import org.example.bcn.Reportes.ReporteC;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;
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
    private ReporteA compraDAO;
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

    @FXML
    private  void HandleMinimizeButton (ActionEvent event) {
        Stage stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }


    // Funcion para configurar la tabla y cargar datos


    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        compraDAO = new ReporteA();
        IDTransaccion.setCellValueFactory(new PropertyValueFactory<>("TransactionID"));
        IDCliente.setCellValueFactory(new PropertyValueFactory<>("Nombre"));
        IDDate.setCellValueFactory(new PropertyValueFactory<>("DateShopping"));
        IDPay.setCellValueFactory(new PropertyValueFactory<>("TotalAmount"));
        IDDescripcion.setCellValueFactory(new PropertyValueFactory<>("DescriptionShopping"));


        TableTransactions.setItems(FXCollections.observableArrayList());
        // fin declaracioon para reporte A
    }

    @FXML
    protected void buscarCompras (ActionEvent event) {
        // Obtener los datos de la fecha de inicio y fin
        LocalDate fechaInicial = IDFechaInit.getValue();
        LocalDate fechaFinal = IDFechaEnd.getValue();

        // Validar que las fechas sean correctas
        if (fechaInicial == null || fechaFinal == null || fechaInicial.isAfter(fechaFinal)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error en la fecha");
            alert.setContentText("La fecha de inicio debe ser anterior a la fecha de fin.");
            alert.showAndWait();
            return;
        }

        Date fechaInicioSQL = Date.valueOf(fechaInicial);
        Date fechaFinalSQL = Date.valueOf(fechaFinal);

        int IDCliente = obtenerCliente();
        if (IDCliente == -1) {
            return;
        }


        try {
            List<Transacciones> transacciones = compraDAO.listarTransacciones_Cliente(IDCliente, fechaInicioSQL, fechaFinalSQL);
            ObservableList<Transacciones> lisTransacciones = FXCollections.observableArrayList(transacciones);

            TableTransactions.setItems(lisTransacciones);
        }catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error en la base de datos" + e.getMessage());
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }
    @FXML
    private int obtenerCliente() {
        Transacciones clienteSeleccionado = TableTransactions.getSelectionModel().getSelectedItem();
        if (clienteSeleccionado != null) {
            return clienteSeleccionado.getClienteID();
        } else {
            // Si no hay ningún cliente seleccionado, se muetsra  un mensaje de error  negando que no se selecciono el cliente
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Cliente no seleccionado");
            alert.setContentText("Por favor, selecciona un cliente.");
            alert.showAndWait();

            // Se devuelve  un valor que indica que no se ha seleccionado ningún cliente
            // En este caso, se devuelve -1
            return -1;
        }
    }


    private ObservableList<ReporteC> getTarjetasFromDatabase () { //00149823 Esta funcion obtiene los observables de cada columna
        ObservableList<ReporteC> ReporteC = FXCollections.observableArrayList();


        String query = "SELECT ce.ClienteID, ce.NameCliente AS Nombre, ce.LastNameCliente AS Apellido, c.CardNumber " +
                "FROM Cards AS c " + "INNER JOIN Cliente AS ce ON ce.ClienteID = c.ClienteID"; //00149823 Consulta para poder recuprar las tarjetas y los id de los clientes

        try (Connection conn = DBConnection.getInstance().getConnection(); //00149823 Conexion con la base de datos mediante la instancia ya creada para que no haya repeticion
             Statement stmt = conn.createStatement(); //000149823 Se crea un statement para la conexion
             ResultSet rs = stmt.executeQuery(query)) { //00149823 El resultado del statement con la consulta

            while (rs.next()) {
                int id_Cliente = rs.getInt("ClienteID"); //00149823 Se agrega el id del cliente al resultado de la statement
                String nombre = rs.getString("NameCliente"); //00149823 Se agrega el nombre del cliente al resultado de la statement
                String apellido = rs.getString("LastNameCliente"); //00149823 Se agrega el apellido del cliente al resultado de la statement
                String numeroCuenta = rs.getString("CardNumber");
                String tipo_tarjeta = rs.getString("CardType");

                ReporteC reporte = new ReporteC(id_Cliente,nombre, apellido);

                if(tipo_tarjeta.equalsIgnoreCase("Tarjeta Credito")) {
                    reporte.getTarjetasCredito().add(numeroCuenta);
                } else if(tipo_tarjeta.equalsIgnoreCase("Tarjeta Debito")) {
                    reporte.getTarjetasDebito().add(numeroCuenta);
                }

                ReporteC.add(reporte);

            }

            for (ReporteC reporte : ReporteC){
                reporte.setTipo_tarjeta(reporte.getTarjetas());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return ReporteC;

    }
}


