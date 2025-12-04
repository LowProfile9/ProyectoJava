package com.rapidexpress.model.entity;

public class Cliente {

    private int id;
    private String numIdentificacion;
    private String nombreCompleto;
    private String telefono;
    private String email;
    private String direccion;

    public Cliente(String numIdentificacion, String nombreCompleto, String telefono, String email, String direccion) {
        this.numIdentificacion = numIdentificacion;
        this.nombreCompleto = nombreCompleto;
        this.telefono = telefono;
        this.email = email;
        this.direccion = direccion;
    }

    public Cliente(int id, String numIdentificacion, String nombreCompleto, String telefono, String email, String direccion) {
        this(numIdentificacion, nombreCompleto, telefono, email, direccion);
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getNumIdentificacion() {
        return numIdentificacion;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getEmail() {
        return email;
    }

    public String getDireccion() {
        return direccion;
    }

    @Override
    public String toString() {
        return "Cliente{" +
                "id=" + id +
                ", numIdentificacion='" + numIdentificacion + '\'' +
                ", nombreCompleto='" + nombreCompleto + '\'' +
                ", telefono='" + telefono + '\'' +
                ", email='" + email + '\'' +
                ", direccion='" + direccion + '\'' +
                '}';
    }
}
