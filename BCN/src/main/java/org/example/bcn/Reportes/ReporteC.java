package org.example.bcn.Reportes;

import javafx.beans.binding.BooleanExpression;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ReporteC {
    private SimpleIntegerProperty clienteID;
    private StringProperty nombre;
    private StringProperty apellido;
    private StringProperty tarjetas;

    public ReporteC(int clienteID, String nombre, String apellido) {
        this.clienteID = new SimpleIntegerProperty(clienteID);
        this.nombre = new SimpleStringProperty(nombre);
        this.apellido = new SimpleStringProperty(apellido);
        this.tarjetas = new SimpleStringProperty("N/A");
    }

    public int getClienteID() {
        return clienteID.get();
    }

    public void setClienteID(int clienteID) {
        this.clienteID.set(clienteID);
    }

    public String getNombre() {
        return nombre.get();
    }

    public StringProperty nombreProperty() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre.set(nombre);
    }

    public String getApellido() {
        return apellido.get();
    }

    public StringProperty apellidoProperty() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido.set(apellido);
    }

    public String getTarjetas() {
        return tarjetas.get();
    }

    public StringProperty tarjetasProperty() {
        return tarjetas;
    }

    public void setTarjetas(String tarjetas) {
        this.tarjetas.set(tarjetas);
    }

    public void agregarTarjeta(String numeroTarjeta, String tipoTarjeta) {
        if (tipoTarjeta.equalsIgnoreCase("Tarjeta Crédito")) {
            this.tarjetas.set(this.tarjetas.get() + "\nXXXX XXXX XXXX " + numeroTarjeta.substring(numeroTarjeta.length() - 4));
        } else if (tipoTarjeta.equalsIgnoreCase("Tarjeta Débito")) {
            this.tarjetas.set(this.tarjetas.get() + "\nXXXX XXXX XXXX " + numeroTarjeta.substring(numeroTarjeta.length() - 4));
        }
    }

    public SimpleIntegerProperty clienteIDProperty() {
        return clienteID;
    }

}
