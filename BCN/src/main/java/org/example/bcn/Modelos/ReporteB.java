package org.example.bcn.Modelos;

import javafx.beans.property.*;

import java.time.LocalDate;

public class ReporteB {
    private final IntegerProperty idTransaccion;
    private final IntegerProperty idCliente;
    private final StringProperty nameCliente;
    private final ObjectProperty<LocalDate> date;
    private final DoubleProperty pay;

    /**
     * Constructor para la clase ReporteB.
     *
     * @param idTransaccion El ID de la transacción.
     * @param idCliente     El ID del cliente.
     * @param nameCliente   El nombre del cliente.
     * @param date          La fecha de la transacción.
     * @param pay           El monto pagado en la transacción.
     */
    public ReporteB(int idTransaccion, int idCliente, String nameCliente, LocalDate date, double pay) {
        this.idTransaccion = new SimpleIntegerProperty(idTransaccion);
        this.idCliente = new SimpleIntegerProperty(idCliente);
        this.nameCliente = new SimpleStringProperty(nameCliente);
        this.date = new SimpleObjectProperty<>(date);
        this.pay = new SimpleDoubleProperty(pay);
    }

    // Getters y Setters para propiedades observables

    public IntegerProperty idTransaccionProperty() {
        return idTransaccion;
    }

    public int getIdTransaccion() {
        return idTransaccion.get();
    }

    public void setIdTransaccion(int idTransaccion) {
        this.idTransaccion.set(idTransaccion);
    }

    public IntegerProperty idClienteProperty() {
        return idCliente;
    }

    public int getIdCliente() {
        return idCliente.get();
    }

    public void setIdCliente(int idCliente) {
        this.idCliente.set(idCliente);
    }

    public StringProperty nameClienteProperty() {
        return nameCliente;
    }

    public String getNameCliente() {
        return nameCliente.get();
    }

    public void setNameCliente(String nameCliente) {
        this.nameCliente.set(nameCliente);
    }

    public ObjectProperty<LocalDate> dateProperty() {
        return date;
    }

    public LocalDate getDate() {
        return date.get();
    }

    public void setDate(LocalDate date) {
        this.date.set(date);
    }

    public DoubleProperty payProperty() {
        return pay;
    }

    public double getPay() {
        return pay.get();
    }

    public void setPay(double pay) {
        this.pay.set(pay);
    }
}
