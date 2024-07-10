package org.example.bcn.Modelos;

import javafx.beans.property.*;

public class ReporteD {

    private final IntegerProperty idFacilitador;
    private final StringProperty nombre_cliente;
    private final IntegerProperty idTransaccion;
    private final DoubleProperty pagoTotal;

    public ReporteD(int idFacilitador, String nombre_cliente, int idTransaccion, double pagoTotal) {
        this.idFacilitador = new SimpleIntegerProperty(idFacilitador);
        this.nombre_cliente = new SimpleStringProperty(nombre_cliente);
        this.idTransaccion = new SimpleIntegerProperty(idTransaccion);
        this.pagoTotal = new SimpleDoubleProperty(pagoTotal);
    }



    public int getIdFacilitador() {
        return idFacilitador.get();
    }

    public IntegerProperty idFacilitadorProperty() {
        return idFacilitador;
    }

    public void setIdFacilitador(int idFacilitador) {
        this.idFacilitador.set(idFacilitador);
    }

    public String getNombre_cliente() {
        return nombre_cliente.get();
    }

    public StringProperty nombre_clienteProperty() {
        return nombre_cliente;
    }

    public void setNombre_cliente(String nombre_cliente) {
        this.nombre_cliente.set(nombre_cliente);
    }

    public int getIdTransaccion() {
        return idTransaccion.get();
    }

    public IntegerProperty idTransaccionProperty() {
        return idTransaccion;
    }

    public void setIdTransaccion(int idTransaccion) {
        this.idTransaccion.set(idTransaccion);
    }

    public double getPagoTotal() {
        return pagoTotal.get();
    }

    public DoubleProperty pagoTotalProperty() {
        return pagoTotal;
    }

    public void setPagoTotal(double pagoTotal) {
        this.pagoTotal.set(pagoTotal);
    }
}
