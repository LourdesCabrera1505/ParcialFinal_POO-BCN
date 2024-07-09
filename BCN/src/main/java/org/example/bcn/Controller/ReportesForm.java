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

    @FXML
    private  void HandleMinimizeButton (ActionEvent event) { // 00081523 Este método se llama cuando se hace clic en el botón de minimizar. Minimiza la ventana actual.
        Stage stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }



}
