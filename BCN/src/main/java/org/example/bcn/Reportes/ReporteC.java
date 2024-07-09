package org.example.bcn.Reportes;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ReporteC {
    private int id_Cliente; //00149823 Aqui se declare una variable para poder tener el id_Cliente
    private String nombre; //00149823 Variable que contiene el nombre del cliente
    private String apellido; //00149823 Variable que tiene el apellido del cliente
    private ObservableList<String> TarjetasCredito; //00149823 Variable que tiene a las tarjetas de credito
    private ObservableList<String> TarjetasDebito; //00149823 Variable que tiene a las tarjetas de debito
    private String tipo_tarjeta; //00149823 Variable que se usa para tener los tipos de Tarjeta

    public ReporteC(int id_Cliente, String nombre, String apellido) { //Constructor publico para las variables
        this.id_Cliente = id_Cliente;
        this.nombre = nombre;
        this.apellido = apellido;
        this.TarjetasCredito = FXCollections.observableArrayList();
        this.TarjetasDebito = FXCollections.observableArrayList();

    }


    public int getId_Cliente() { //00149823 Obtener el valor del Id_Cliente
        return id_Cliente;
    }

    public void setId_Cliente(int id_Cliente) {
        this.id_Cliente = id_Cliente;
    }

    public String getNombre() { //00149823 Obtener el nombre del cliente
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() { //00149823 Obtener el apellido del cliente
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }


    public ObservableList<String> getTarjetasCredito() { // 00149823 Obtener las Tarjetas de Credito
        return TarjetasCredito;
    }

    public void setTarjetasCredito(ObservableList<String> tarjetasCredito) {
        this.TarjetasCredito = tarjetasCredito;
    }

    public ObservableList<String> getTarjetasDebito() { //00149823 Obtener las Tarjetas de Debito
        return TarjetasDebito;
    }


    public String getTipo_tarjeta() { //00149823 Obtener los tipos de Tarjeta
        return tipo_tarjeta;
    }

    public void setTipo_tarjeta(String tipo_tarjeta) {
        this.tipo_tarjeta = tipo_tarjeta;
    }

    public void setTarjetasDebito(ObservableList<String> tarjetasDebito) {
        this.TarjetasDebito = tarjetasDebito;
    }

    public String getTarjetas() { // 00149823 Obtener los tipos de tarjeta
        if (!TarjetasDebito.isEmpty() || !TarjetasCredito.isEmpty()) { //00149823 Se ve si la tarjeta de Credito o Debito estan vacias
            return  "N/A"; //00149823 Se retorna un mensaje "N/A" que significa que no tienen de ningun tipo
        }

        StringBuilder censura = new StringBuilder(); // 00149823 Se censurar el numero de tarjeta
        if(!TarjetasCredito.isEmpty()) { //00149823 Si la Tarjeta de credito esta vacia
            censura.append("Tarjetas de Credito:\n"); //00149823 Se manda un mensaje donde dice "Tarjeta de Credito:"
            for (String tarjeta : TarjetasCredito) {
                censura.append("XXXX XXXX XXXX").append(tarjeta.substring(tarjeta.length()-4)).append("\n"); //00149823 Se agregan las XXXX para poder censurar el numeroy solo mostrar los ultimos 4
            }
        }
        if(!TarjetasDebito.isEmpty()) { //00149823 Si la Tarjeta de Debito esta vacia
            censura.append("Tarjetas de Debito:\n"); //00149823 Se manda un mensaje donde dice "Tarjeta de Debito:"
            for (String tarjeta : TarjetasDebito) {
                censura.append("XXXX XXXX XXXX").append(tarjeta.substring(tarjeta.length()-4)).append("\n"); //00149823 Se agregan las XXXX para poder censurar el numeroy solo mostrar los ultimos 4
            }
        }
        return censura.toString().trim();
    }


}
