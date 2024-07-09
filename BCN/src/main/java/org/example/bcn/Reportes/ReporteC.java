package org.example.bcn.Reportes;

public class ReporteC {
    private int id_Cliente;
    private String nombre;
    private String apellido;
    private int id_Card;
    private String NumeroCuentaCensurado;

    public ReporteC(int id_Cliente, String nombre, String apellido, int id_Card, String numeroCuentaCensurado) {
        this.id_Cliente = id_Cliente;
        this.nombre = nombre;
        this.apellido = apellido;
        this.id_Card = id_Card;
        NumeroCuentaCensurado = numeroCuentaCensurado;
    }

    public int getId_Cliente() {
        return id_Cliente;
    }

    public void setId_Cliente(int id_Cliente) {
        this.id_Cliente = id_Cliente;
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

    public int getId_Card() {
        return id_Card;
    }

    public void setId_Card(int id_Card) {
        this.id_Card = id_Card;
    }

    public String getNumeroCuentaCensurado() {
        String numeroCuenta;
        numeroCuenta = NumeroCuentaCensurado;

        if (numeroCuenta.length() < 16) {
            return "XXXX XXXX XXXX XXXX";
        }
        return "XXXX XXXX XXXX " + numeroCuenta.substring(numeroCuenta.length() - 4);
    }

    public void setNumeroCuentaCensurado(String numeroCuentaCensurado) {
        NumeroCuentaCensurado = numeroCuentaCensurado;
    }
}
