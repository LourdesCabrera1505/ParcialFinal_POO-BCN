package org.example.bcn.Reportes;

import java.util.Date;
import javafx.beans.property.*;

public class ReporteB {

    private final IntegerProperty idTransaccion;
    private final StringProperty idCliente;
    private final ObjectProperty<Date> date;
    private final DoubleProperty pay;


    public ReporteB(int idTransaccion, String idCliente, Date date, double pay) {
        this.idTransaccion = new SimpleIntegerProperty(idTransaccion);
        this.idCliente = new SimpleStringProperty(idCliente);
        this.date = new SimpleObjectProperty<>(date);
        this.pay = new SimpleDoubleProperty(pay);

    }

    // Getters
    public int getIdTransaccion() {
        return idTransaccion.get();
    }

    public String getIdCliente() {
        return idCliente.get();
    }

    public Date getDate() {
        return date.get();
    }

    public double getPay() {
        return pay.get();
    }



    // Property Getters
    public IntegerProperty idTransaccionProperty() {
        return idTransaccion;
    }

    public StringProperty idClienteProperty() {
        return idCliente;
    }

    public ObjectProperty<Date> dateProperty() {
        return date;
    }

    public DoubleProperty payProperty() {
        return pay;
    }



    // Setters (if needed)
    public void setIdTransaccion(int idTransaccion) {
        this.idTransaccion.set(idTransaccion);
    }

    public void setIdCliente(String idCliente) {
        this.idCliente.set(idCliente);
    }

    public void setDate(Date date) {
        this.date.set(date);
    }

    public void setPay(double pay) {
        this.pay.set(pay);
    }


}
