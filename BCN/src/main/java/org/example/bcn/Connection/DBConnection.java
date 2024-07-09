package org.example.bcn.Connection;
import java.sql.*;

public class DBConnection {

    private static DBConnection instance;

    private Connection connection; // Creando instancia para la conección a la base de datos

    // Detalles de las conección
    private final String url = "jdbc:sqlserver://localhost:1433;databaseName=BCN_ParcialFinal;encrypt=true;trustServerCertificate=true";; //Asignandp la url de la base de datos
    private final String user = "sa"; // definiendo el usuario con el que se accedera a la base de datos
    private final String password = "<Delilah.1234>"; // contraseña que da el paso al usuario a la base de datos


    // Constructor privado para evitar la creacion de instancias directas
    private DBConnection()throws  SQLException {
        try {

            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");// Se realiza la carga del driver de sql server
            this.connection = DriverManager.getConnection(url, user, password); // se establece la conección con la base de datos
        }catch (ClassNotFoundException e) {
            throw  new SQLException("El driver de sql server no ha sido encontrado", e);
        }
    }

    // Método estático para obtener la instancia de la conección
    public static  DBConnection getInstance() throws SQLException {
        if (instance == null) {
            synchronized (DBConnection.class) {
                if (instance == null) {
                    instance = new DBConnection();
                }
            }
        } else if (instance.getConnection().isClosed()) {
            instance = new DBConnection(); // Si la conexión está cerrada, se crea una nueva
        }
        return instance;
    }

    // Método para obtener la conexión actual
    public Connection getConnection() {
        return connection;
    }
}
