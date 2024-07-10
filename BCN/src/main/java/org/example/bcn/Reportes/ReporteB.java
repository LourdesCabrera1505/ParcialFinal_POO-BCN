package org.example.bcn.Reportes;

import java.util.Date;

public class ReporteB {
    private int idTransaccion;
    private String idCliente;
    private Date date;
    private double pay;

    public ReporteB(int idTransaccion, String idCliente, Date date, double pay) {
        this.idTransaccion = idTransaccion;
        this.idCliente = idCliente;
        this.date = date;
        this.pay = pay;
    }

    public int getIdTransaccion() {
        return idTransaccion;
    }

    public void setIdTransaccion(int idTransaccion) {
        this.idTransaccion = idTransaccion;
    }

    public String getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getPay() {
        return pay;
    }

    public void setPay(double pay) {
        this.pay = pay;
    }
}
