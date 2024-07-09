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
    private TableView<ReporteC> tableView; //00149823 Se agrega un TableView para poder ver la consulta
    @FXML
    private TableColumn<ReporteC, Integer> colIdCliente; //00149823 Se agrega una columna para el id del cliente
    @FXML
    private TableColumn<ReporteC, String> colNombre; //00149823 Nombre del cliente juento al id
    @FXML
    private TableColumn<ReporteC, String> colApellido; //00149824 Columna para el apellido del cliente
    @FXML
    private TableColumn<ReporteC, Integer> colIdCard; //00149823 Se agrega el id de la tarjeta
    @FXML
    private TableColumn<ReporteC, String> colNumTarjeta; //00149823 El numero de tarjeta del cliente


    @FXML
    private  void HandleMinimizeButton (ActionEvent event) {
        Stage stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    public void initialize() { //00149823 Esta función sirve para inicializar cada campo de la consulta
        colIdCliente.setCellValueFactory(new PropertyValueFactory<>("id_Cliente")); //00149823 Aqui es la columna y se pasa el "id_Cliente"
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colApellido.setCellValueFactory(new PropertyValueFactory<>("apellido"));
        colIdCard.setCellValueFactory(new PropertyValueFactory<>("id_Card"));
        colNumTarjeta.setCellValueFactory(new PropertyValueFactory<>("numeroCuentaCensurado"));

        tableView.setItems(getTarjetasFromDatabase()); //00149823 Se cargan los datos a la TableView
    }

    private ObservableList<ReporteC> getTarjetasFromDatabase () { //00149823 Esta funcion obtiene los observables de cada columna
        ObservableList<ReporteC> ReporteC = FXCollections.observableArrayList();

        String query = "SELECT ce.ClienteID, ce.NameCliente AS Nombre, ce.ApellidoCliente AS Apellido, c.CardNumber, c.CardType " +
                "FROM Cards AS c " +
                "INNER JOIN Cliente AS ce ON ce.ClienteID = c.ClienteID"; //00149823 Consulta para poder recuprar las tarjetas y los id de los clientes

        try (Connection conn = DBConnection.getInstance().getConnection();//00149823 Conexion con la base de datos mediante la instancia ya creada para que no haya repeticion
             Statement stmt = conn.createStatement(); //000149823 Se crea un statement para la conexion
             ResultSet rs = stmt.executeQuery(query)) { //00149823 El resultado del statement con la consulta

            while (rs.next()) {
                int idCliente = rs.getInt("ClienteID"); //00149823 Se agrega el id del cliente al resultado de la statement
                String nombre = rs.getString("Nombre"); //00149823 Se agrega el nombre del cliente al resultado de la statement
                String apellido = rs.getString("Apellido"); //00149823 Se agrega el apellido del cliente al resultado de la statement
                int idCard = rs.getInt("CardID");
                String numeroTarjeta = rs.getString("CardNumber");

                String numeroTarjetaCensurado = "XXXX XXXX XXXX " + numeroTarjeta.substring(numeroTarjeta.length() - 4); //00149823 se censura los primeros numeros de la tarjeta

                ReporteC.add(new ReporteC(idCliente, nombre, apellido, idCard, numeroTarjetaCensurado));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return ReporteC;
    }
}


