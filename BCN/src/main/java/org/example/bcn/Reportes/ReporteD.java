package org.example.bcn.Reportes;

public class ReporteD {
    private int clienteId;
    private String nombre;
    private String apellido;
    private int cantidadCompras;
    private double totalGastado;

    public ReporteD(int clienteId, String nombre, String apellido, int cantidadCompras, double totalGastado) {
        this.clienteId = clienteId;
        this.nombre = nombre;
        this.apellido = apellido;
        this.cantidadCompras = cantidadCompras;
        this.totalGastado = totalGastado;
    }

    public int getClienteId() {
        return clienteId;
    }

    public void setClienteId(int clienteId) {
        this.clienteId = clienteId;
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

    public int getCantidadCompras() {
        return cantidadCompras;
    }

    public void setCantidadCompras(int cantidadCompras) {
        this.cantidadCompras = cantidadCompras;
    }

    public double getTotalGastado() {
        return totalGastado;
    }

    public void setTotalGastado(double totalGastado) {
        this.totalGastado = totalGastado;
    }
}
