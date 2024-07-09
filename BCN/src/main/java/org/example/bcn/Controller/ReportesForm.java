package org.example.bcn.Controller;

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
    public void initialize(){

    }

    @FXML
    private  void HandleMinimizeButton (ActionEvent event) {
        Stage stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }

    public void btnBuscarRegistroAction(ActionEvent event) {
        String clienteID = tfIDA.getText();
        String fechaInicioStr = tfFechaInicioA.getText();
        String fechaFinalStr = tfFechaFinalA.getText();

        // Convertir las fechas a objetos java.sql.Date
        java.sql.Date fechaInicio = java.sql.Date.valueOf(fechaInicioStr);
        java.sql.Date fechaFinal = java.sql.Date.valueOf(fechaFinalStr);

        //Consulta SQL
        String sql = "SELECT S.DateShopping " +
                "FROM Cliente C " +
                "INNER JOIN Cards T ON C.ClienteID = T.ClienteID " +
                "INNER JOIN Smart_Shopping S ON T.CardID = S.CardID " +
                "WHERE C.ClienteID = ? " +
                "AND S.DateShopping BETWEEN ? AND ?";

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, clienteID);
            stmt.setDate(2, fechaInicio);
            stmt.setDate(3, fechaFinal);

            ResultSet rs = stmt.executeQuery();

            // Limpiar la tabla antes de agregar nuevos datos
            tablaA.getItems().clear();

            // Agregar las fechas de compras a la columna2A de la tabla
            while (rs.next()) {
                String fechaCompra = rs.getString("DateShopping");
                tablaA.getItems().add(fechaCompra);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void IDSaveRepoAAction(ActionEvent event) {
        String clienteID = tfIDA.getText();
        String fechaInicio = tfFechaInicioA.getText();
        String fechaFinal = tfFechaFinalA.getText();

        // Consulta SQL para obtener las fechas de compras
        String sql = "SELECT S.DateShopping " +
                "FROM Cliente C " +
                "INNER JOIN Cards T ON C.ClienteID = T.ClienteID " +
                "INNER JOIN Smart_Shopping S ON T.CardID = S.CardID " +
                "WHERE C.ClienteID = ? " +
                "AND S.DateShopping BETWEEN ? AND ?";

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, clienteID);
            stmt.setString(2, fechaInicio);
            stmt.setString(3, fechaFinal);

            ResultSet rs = stmt.executeQuery();

            // Limpiar la tabla antes de agregar nuevos datos
            tablaA.getItems().clear();

            // Escribir en el archivo file.txt
            String fileName = "file.txt";
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
                // Escribir el ID del cliente
                writer.write("ID del cliente: " + clienteID);
                writer.newLine();

                // Escribir las fechas de compras en la tabla A y en el archivo
                while (rs.next()) {
                    String fechaCompra = rs.getString("DateShopping");
                    tablaA.getItems().add(fechaCompra); // Agregar a la tabla A

                    // Escribir en el archivo
                    writer.write(fechaCompra);
                    writer.newLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void btnBuscarRegistroBAction(ActionEvent event) {
        String clienteID = tfIDB.getText();
        String fechaInicio = tfFechaInicioB.getText();
        String fechaFinal = tfFechaFinalB.getText();

        // Convertir las fechas a objetos java.sql.Date
        java.sql.Date fechaInicioSQL = java.sql.Date.valueOf(fechaInicio);
        java.sql.Date fechaFinalSQL = java.sql.Date.valueOf(fechaFinal);

        // Consulta SQL para obtener el total gastado por el cliente en el mes específico
        String sql = "SELECT SUM(S.MontoCompra) AS TotalGastado " +
                "FROM Cliente C " +
                "INNER JOIN Cards T ON C.ClienteID = T.ClienteID " +
                "INNER JOIN Smart_Shopping S ON T.CardID = S.CardID " +
                "WHERE C.ClienteID = ? " +
                "AND MONTH(S.DateShopping) = MONTH(?) " +
                "AND YEAR(S.DateShopping) = YEAR(?)";

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, clienteID);
            stmt.setDate(2, fechaInicioSQL);
            stmt.setDate(3, fechaFinalSQL);

            ResultSet rs = stmt.executeQuery();

            // Obtener el resultado del total gastado
            if (rs.next()) {
                double totalGastado = rs.getDouble("TotalGastado");

                // Aquí puedes mostrar el total gastado en la interfaz de usuario
                // Por ejemplo, podrías imprimirlo en la consola
                System.out.println("Total gastado en el mes: " + totalGastado);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void IDSaveReporteBAction(ActionEvent event) {
    }
}
