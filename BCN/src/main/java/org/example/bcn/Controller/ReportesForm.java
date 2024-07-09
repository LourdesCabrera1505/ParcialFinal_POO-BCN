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
    private TableView<ReporteC> tableView;
    @FXML
    private TableColumn<ReporteC, Integer> colId;
    @FXML
    private TableColumn<ReporteC, String> colNombre;
    @FXML
    private TableColumn<ReporteC, String> colApellido;
    @FXML
    private TableColumn<ReporteC, String> colNumTarjeta;


    @FXML
    private  void HandleMinimizeButton (ActionEvent event) {
        Stage stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    public void initialize(){
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colApellido.setCellValueFactory(new PropertyValueFactory<>("apellido"));
        colId.setCellValueFactory(new PropertyValueFactory<>("id_Card"));
    }

    private ObservableList<ReporteC> getTarjetasFromDatabase () {
        ObservableList<ReporteC> ReporteC = FXCollections.observableArrayList();

        String query = "SELECT ce.ClienteID, ce.NameCliente AS Nombre, ce.ApellidoCliente AS Apellido, c.CardNumber, c.CardType " +
                "FROM Cards AS c " +
                "INNER JOIN Cliente AS ce ON ce.ClienteID = c.ClienteID";

        try (Connection conn = DBConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(selectSQL)) {

            while (rs.next()) {
                int id_Cliente = rs.getInt("ClienteID");
                String nombre = rs.getString("");
                String apellido = rs.getString("");
                String numeroCuenta = rs.getString("");
                String tipo_tarjeta = rs.getString("");

                ReporteC.add(new ReporteC(“ClienteID: ”+ id_Cliente + “, Nombre: “ + nombre + “Apellido” + apellido + “Numero Cuenta: “ + NumeroCuentaCensurado + “Tipo de tarjeta: “ + tipo_tarjeta));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return ReporteC;
    }
}


