package org.example.bcn.Modelos;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ReporteC {
    private final SimpleIntegerProperty clienteID;
    private final SimpleStringProperty nombre;
    private final SimpleStringProperty apellido;
    private final ObservableList<String> tarjetasCredito;
    private final ObservableList<String> tarjetasDebito;

    public ReporteC(int clienteID, String nombre, String apellido) {
        this.clienteID = new SimpleIntegerProperty(clienteID);
        this.nombre = new SimpleStringProperty(nombre);
        this.apellido = new SimpleStringProperty(apellido);
        this.tarjetasCredito = FXCollections.observableArrayList();
        this.tarjetasDebito = FXCollections.observableArrayList();
    }

    public int getClienteID() {
        return clienteID.get();
    }

    public SimpleIntegerProperty clienteIDProperty() {
        return clienteID;
    }

    public String getNombre() {
        return nombre.get();
    }

    public SimpleStringProperty nombreProperty() {
        return nombre;
    }

    public String getApellido() {
        return apellido.get();
    }

    public SimpleStringProperty apellidoProperty() {
        return apellido;
    }

    public ObservableList<String> getTarjetasCredito() {
        return tarjetasCredito;
    }

    public ObservableList<String> getTarjetasDebito() {
        return tarjetasDebito;
    }

    public void agregarTarjeta(String numeroTarjeta, String tipoTarjeta) {
        if ("Tarjeta Credito".equalsIgnoreCase(tipoTarjeta)) {
            tarjetasCredito.add(numeroTarjeta);
        } else if ("Tarjeta Debito".equalsIgnoreCase(tipoTarjeta)) {
            tarjetasDebito.add(numeroTarjeta);
        }
    }

    public String tarjetasProperty() {
        StringBuilder sb = new StringBuilder();
        sb.append("Tarjetas de crédito:\n");
        if (!tarjetasCredito.isEmpty()) {
            for (String tarjeta : tarjetasCredito) {
                sb.append("XXXX XXXX XXXX ").append(tarjeta.substring(tarjeta.length() - 4)).append("\n");
            }
        } else {
            sb.append("N/A \n");
        }

        sb.append("Tarjetas de Débito:\n");
        if (!tarjetasDebito.isEmpty()) {
            for (String tarjeta : tarjetasDebito) {
                sb.append("XXXX XXXX XXXX ").append(tarjeta.substring(tarjeta.length() - 4)).append("\n");
            }
        } else {
            sb.append("N/A \n");
        }

        return sb.toString();
    }



}
