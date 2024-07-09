package org.example.bcn.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.example.bcn.Connection.DBConnection;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
public class ReportesForm {
    @FXML
    private TextField facilitadorField;
    @FXML
    private TextArea reporteArea;

    private Connection connect;

    @FXML
    private Button minimizeButton;

    public void initialize() {
        try {
            connect = DBConnection.getInstance().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void generarReporteD(){
        String facilitador = facilitadorField.getText();
        if (!facilitador.isEmpty()){
            try{
            String sql = "SELECT C.ClienteID, C.NameCliente, C.LastNameCliente, COUNT(S.TransactionID) AS cantidad_compras, SUM(S.TotalAmount) AS total_gastado " +
                    "FROM Cliente C " +
                    "JOIN Cards Ca ON C.ClienteID = Ca.ClienteID " +
                    "JOIN Smart_Shopping S ON Ca.CardID = S.CardID " +
                    "JOIN Facilitator F ON Ca.FacilitatorID = F.FacilitatorID " +
                    "WHERE F.FacilitatorName = ? " +
                    "GROUP BY C.ClienteID, C.NameCliente, C.LastNameCliente";
            try(PreparedStatement statement = connect.prepareStatement(sql)){
            statement.setString(1, facilitador);
                try (ResultSet resultSet = statement.executeQuery()) {
                    StringBuilder reporte = new StringBuilder();
                    while (resultSet.next()) {
                        int clienteId = resultSet.getInt("ClienteID");
                        String nombre = resultSet.getString("NameCliente");
                        String apellido = resultSet.getString("LastNameCliente");
                        int cantidadCompras = resultSet.getInt("cantidad_compras");
                        double totalGastado = resultSet.getDouble("total_gastado");

                        reporte.append("ID Cliente: ").append(clienteId).append("\n")
                                .append("Nombre Completo: ").append(nombre).append(" ").append(apellido).append("\n")
                                .append("Cantidad de Compras: ").append(cantidadCompras).append("\n")
                                .append("Total Gastado: ").append(totalGastado).append("\n")
                                .append("------------------------------------------\n");
                    }
                    reporteArea.setText(reporte.toString());
                    guardarReporteEnArchivo(reporte.toString(), facilitador);
                }
            }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Por favor, ingrese un facilitador de tarjeta.");
        }
            }

    private void guardarReporteEnArchivo(String reporte, String facilitador) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String fechaHora = sdf.format(new Date());
        String nombreArchivo = "Reportes/ReporteD_" + facilitador + ".txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(nombreArchivo, true))) {
            writer.write("Reporte D - Clientes que han realizado compras con el facilitador: " + facilitador);
            writer.newLine();
            writer.write("Fecha y Hora: " + fechaHora);
            writer.newLine();
            writer.write("==========================================");
            writer.newLine();
            writer.write(reporte);
            writer.write("==========================================");
            writer.newLine();
            System.out.println("Reporte generado exitosamente: " + nombreArchivo);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private  void HandleMinimizeButton (ActionEvent event) {
        Stage stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }



}
