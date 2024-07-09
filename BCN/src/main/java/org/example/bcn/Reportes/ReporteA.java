package org.example.bcn.Reportes;

import org.example.bcn.Connection.DBConnection;
import org.example.bcn.ModeloTransacciones.Transacciones;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.Date;
import java.util.List;

public class ReporteA {

    public List<Transacciones> listarTransacciones_Cliente(int ClienteID, Date Fecha_Inicio, Date Fecha_Fin) throws SQLException {
        List<Transacciones> transacciones = new ArrayList<>();

        String query = "SELECT TransactionID, DateShopping, TotalAmount, DescriptionShopping" +
                        "FROM Smart_Shopping" +
                        "WHERE ClienteID = ? AND DateShopping BETWEEN ? AND ?";

        try(Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, ClienteID);
            statement.setDate(2, Fecha_Inicio);
            statement.setDate(3, Fecha_Fin);

            try(ResultSet resultSet = statement.executeQuery()) {
                while(resultSet.next()) {
                    int transactionID = resultSet.getInt("TransactionID");
                    Date dateShopping = resultSet.getDate("DateShopping");
                    double totalAmount = resultSet.getDouble("TotalAmount");
                    String descriptionShopping = resultSet.getString("DescriptionShopping");

                    Transacciones transact = new Transacciones();
                    transact.setTransactionID(transactionID);
                    transact.setDateShopping(dateShopping);
                    transact.setTotalAmount(totalAmount);
                    transact.setDescriptionShopping(descriptionShopping);
                    transact.setClienteID(ClienteID);

                    transacciones.add(transact);
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Error al listar las compras del cliente en el periodo  especificado ", e);
        }
        return transacciones;
    }
}
