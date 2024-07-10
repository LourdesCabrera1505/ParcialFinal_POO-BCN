package org.example.bcn.Reportes;

import javafx.beans.property.*;

import java.util.Date;

public class Transacciones {
    private final StringProperty idCliente;
    private final IntegerProperty idTransaccion;
    private ObjectProperty<Date> date = new SimpleObjectProperty<>();
    private final DoubleProperty pay;
    private final StringProperty descripcion;

    public Transacciones(int idTransaccion, String idCliente, Date date, double pay, String descripcion) {
        this.idTransaccion = new SimpleIntegerProperty(idTransaccion);
        this.idCliente = new SimpleStringProperty(idCliente);
        this.date = new SimpleObjectProperty<>(date);
        this.pay = new SimpleDoubleProperty(pay);
        this.descripcion = new SimpleStringProperty(descripcion);
    }

    public StringProperty idClienteProperty() {
        return idCliente;
    }

    public String getIdCliente() {
        return idCliente.get();
    }

    public void setIdCliente(String idCliente) {
        this.idCliente.set(idCliente);
    }

    public IntegerProperty idTransaccionProperty() {
        return idTransaccion;
    }

    public int getIdTransaccion() {
        return idTransaccion.get();
    }

    public void setIdTransaccion(int idTransaccion) {
        this.idTransaccion.set(idTransaccion);
    }

    public ObjectProperty<Date> dateProperty() {
        return date;
    }

    public Date getDate() {
        return date.get();
    }

    public void setDate(Date date) {
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

    public StringProperty descripcionProperty() {
        return descripcion;
    }

    public String getDescripcion() {
        return descripcion.get();
    }

    public void setDescripcion(String descripcion) {
        this.descripcion.set(descripcion);
    }
}
