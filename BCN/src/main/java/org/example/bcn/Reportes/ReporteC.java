package org.example.bcn.Reportes;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ReporteC {
    private int id_Cliente;
    private String nombre;
    private String apellido;
    private ObservableList<String> TarjetasCredito;
    private ObservableList<String> TarjetasDebito;

    private String tipo_tarjeta;

    public ReporteC(int id_Cliente, String nombre, String apellido) {
        this.id_Cliente = id_Cliente;
        this.nombre = nombre;
        this.apellido = apellido;
        this.TarjetasCredito = FXCollections.observableArrayList();
        this.TarjetasDebito = FXCollections.observableArrayList();

    }



    public int getId_Cliente() {
        return id_Cliente;
    }

    public void setId_Cliente(int id_Cliente) {
        this.id_Cliente = id_Cliente;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }


    public ObservableList<String> getTarjetasCredito() {
        return TarjetasCredito;
    }

    public void setTarjetasCredito(ObservableList<String> tarjetasCredito) {
        this.TarjetasCredito = tarjetasCredito;
    }

    public ObservableList<String> getTarjetasDebito() {
        return TarjetasDebito;
    }


    public String getTipo_tarjeta() {
        return tipo_tarjeta;
    }

    public void setTipo_tarjeta(String tipo_tarjeta) {
        this.tipo_tarjeta = tipo_tarjeta;
    }

    public void setTarjetasDebito(ObservableList<String> tarjetasDebito) {
        this.TarjetasDebito = tarjetasDebito;
    }

    public String getTarjetas() {
        if (!TarjetasDebito.isEmpty() || !TarjetasCredito.isEmpty()) {
            return  "N/A";
        }

        StringBuilder censura = new StringBuilder();
        if(!TarjetasCredito.isEmpty()) {
            censura.append("Tarjetas de Credito:\n");
            for (String tarjeta : TarjetasCredito) {
                censura.append("XXXX XXXX XXXX").append(tarjeta.substring(tarjeta.length()-4)).append("\n");
            }
        }
        if(!TarjetasDebito.isEmpty()) {
            censura.append("Tarjetas de Debito:\n");
            for (String tarjeta : TarjetasDebito) {
                censura.append("XXXX XXXX XXXX").append(tarjeta.substring(tarjeta.length()-4)).append("\n");
            }
        }
        return censura.toString().trim();
    }


}
