package org.example.bcn.Modelos;

import javafx.beans.property.*;

import java.util.Date;

public class Transacciones {
    private final StringProperty nameCliente;//String Property : utilizada para encapsular el nombre del cliente limitantdo el acceso a estee como privado
    private final IntegerProperty clienteid; //Integer property -> alamcena el id del cliente para  la utilización de este en la clase buscar compras para filtrar las compras por cliente
    private final IntegerProperty idTransaccion;// se alamcena el id del transaccion y see declara como un parametro privadao
    private final ObjectProperty<Date> date; //Objecto Property ->  se almacena la fecha de cuando se realizo la compra y se guarda para la futura manipulacion de esta
    private final DoubleProperty pay;  // Double Property -> se alamcena  el pago de la compra como un valor punto flotante de doble precision, se ha utilizado para almacenar y manipular el monto pagado
    private final StringProperty descripcion; //String Property -> se alamcena la descripcion de la compra

    // Constructor instanciado con los distintos parametro que se utilizaran posteriormete para las futuras consultas
    public Transacciones(int idTransaccion, int clienteid, String nameCliente, Date date, double pay, String descripcion) {
        this.idTransaccion = new SimpleIntegerProperty(idTransaccion); // se declaran los parametro como nuevas instancias simples de almacenamiento
        this.clienteid = new SimpleIntegerProperty(clienteid);
        this.nameCliente = new SimpleStringProperty(nameCliente);
        this.date = new SimpleObjectProperty<>(date);
        this.pay = new SimpleDoubleProperty(pay);
        this.descripcion = new SimpleStringProperty(descripcion);
    }

    // se instancias los setter y los getter que permiten insertar o traer informacuónn almacenada de la base de datos
    public IntegerProperty clienteidProperty() {
        return clienteid;
    }

    public int getClienteid() {
        return clienteid.get();
    }

    public void setClienteid(int clienteid) {
        this.clienteid.set(clienteid);
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
