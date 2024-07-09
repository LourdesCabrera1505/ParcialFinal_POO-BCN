package org.example.bcn.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.bcn.Connection.DBConnection;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.example.bcn.Connection.DBConnection;
import org.example.bcn.Reportes.ReporteC;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ReportesForm {

    public Button IDSave;
    public TextField tfIDA;
    public TextField tfFechaInicioA;
    public TextField tfFechaFinalA;
    public Button btnBuscarA;
    public TableColumn<String, String> Columna2A;
    public TableView<String> tablaA;
    public TextField tfIDB;
    public TextField tfFechaInicioB;
    public TextField tfFechaFinalB;
    public Button btnBuscarB;
    public TableColumn<String, String> Columna2B;
    public TableView<String> tablaB;
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


