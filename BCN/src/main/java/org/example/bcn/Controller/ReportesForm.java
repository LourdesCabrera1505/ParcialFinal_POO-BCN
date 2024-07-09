package org.example.bcn.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.example.bcn.Connection.DBConnection;
import org.example.bcn.Reportes.ReporteC;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ReportesForm {

    @FXML
    private Button minimizeButton;
    @FXML
    private TableView<ReporteC> ReporteTarjetas;
    @FXML
    private TableColumn<ReporteC, Integer> CClienteID;
    @FXML
    private TableColumn<ReporteC, String> colNombre;
    @FXML
    private TableColumn<ReporteC, String> colApellido;
    @FXML
    private TableColumn<ReporteC, String> colNumTarjeta;
    @FXML
    private TableColumn<ReporteC, String> colTipoTarjeta;


    @FXML
    private  void HandleMinimizeButton (ActionEvent event) {
        Stage stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    public void initialize(){
        CClienteID.setCellValueFactory(new PropertyValueFactory<>("ID"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("Nombre"));
        colApellido.setCellValueFactory(new PropertyValueFactory<>("Apellido"));
        colNumTarjeta.setCellValueFactory(new PropertyValueFactory<>("N° Cuenta"));
    }

    private ObservableList<ReporteC> getTarjetasFromDatabase () {
        ObservableList<ReporteC> ReporteC = FXCollections.observableArrayList();

        String query = "SELECT ce.ClienteID, ce.NameCliente AS Nombre, ce.LastNameCliente AS Apellido, c.CardNumber " +
                "FROM Cards AS c " + "INNER JOIN Cliente AS ce ON ce.ClienteID = c.ClienteID";

        try (Connection conn = DBConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int id_Cliente = rs.getInt("ClienteID");
                String nombre = rs.getString("NameCliente");
                String apellido = rs.getString("LastNameCliente");
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


