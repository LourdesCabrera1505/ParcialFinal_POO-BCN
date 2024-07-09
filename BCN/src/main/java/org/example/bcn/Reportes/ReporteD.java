package org.example.bcn.Reportes;

public class ReporteD {
    private int clienteId; // 00081523 Variable que representa el id del cliente.
    private String nombre; // 00081523 Variable que representa el nombre del cliente.
    private String apellido; // 00081523 Variable que representa el apellido del cliente.
    private int cantidadCompras; // 00081523 Variable que guardará el número de compras realizas por el cliente.
    private double totalGastado; // 00081523 Variable que guardará la cantidad de dinero gastado por el cliente.

    // 00081523 Constructor de la clase
    public ReporteD(int clienteId, String nombre, String apellido, int cantidadCompras, double totalGastado) {
        this.clienteId = clienteId;
        this.nombre = nombre;
        this.apellido = apellido;
        this.cantidadCompras = cantidadCompras;
        this.totalGastado = totalGastado;
    }

    // 00081523 Obtener el valor de id del cliente.
    public int getClienteId() {
        return clienteId;
    }

    public void setClienteId(int clienteId) {
        this.clienteId = clienteId;
    }

    // 00081523 Obtener el valor del nombre del cliente.
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    // 00081523 Obtener el valor del apellido del cliente
    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }
    // 00081523 Obtener la cantidad de compras.
    public int getCantidadCompras() {
        return cantidadCompras;
    }

    public void setCantidadCompras(int cantidadCompras) {
        this.cantidadCompras = cantidadCompras;
    }

    // 00081523 Obtener el valor del total gastado.
    public double getTotalGastado() {
        return totalGastado;
    }

    public void setTotalGastado(double totalGastado) {
        this.totalGastado = totalGastado;
    }
}
