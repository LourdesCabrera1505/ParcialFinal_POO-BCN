package org.example.bcn.ModeloTransacciones;

import java.sql.Date;

public class Transacciones {
    private int TransactionID;
    private Date DateShopping;
    private double TotalAmount;
    private String DescriptionShopping;
    private int ClienteID;
    private int Seleccionado;

    public Transacciones() {
    }

    public int getTransactionID() {
        return TransactionID;
    }

    public void setTransactionID(int transactionID) {
        TransactionID = transactionID;
    }

    public Date getDateShopping() {
        return DateShopping;
    }

    public void setDateShopping(Date dateShopping) {
        DateShopping = dateShopping;
    }

    public double getTotalAmount() {
        return TotalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        TotalAmount = totalAmount;
    }

    public String getDescriptionShopping() {
        return DescriptionShopping;
    }

    public void setDescriptionShopping(String descriptionShopping) {
        DescriptionShopping = descriptionShopping;
    }

    public int getClienteID() {
        return ClienteID;
    }

    public void setClienteID(int clienteID) {
        ClienteID = clienteID;
    }

    public int getSeleccionado() {
        return Seleccionado;
    }

    public void setSeleccionado(int seleccionado) {
        Seleccionado = seleccionado;
    }
}
