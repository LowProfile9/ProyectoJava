package com.rapidexpress.model.entity;

public class Conductor {

    private int id;
    private String numeroIdentificacion;
    private String nombreCompleto;
    private String tipoLicencia;
    private String telefonoContacto;
    private String estado;

    public Conductor(String numeroIdentificacion, String nombreCompleto, String tipoLicencia, String telefonoContacto, String estado) {
        this.numeroIdentificacion = numeroIdentificacion;
        this.nombreCompleto = nombreCompleto;
        this.tipoLicencia = tipoLicencia;
        this.telefonoContacto = telefonoContacto;
        this.estado = estado;
    }

    public Conductor(int id, String numeroIdentificacion, String nombreCompleto, String tipoLicencia, String telefonoContacto, String estado) {
        this(numeroIdentificacion, nombreCompleto, tipoLicencia, telefonoContacto, estado);
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getNumeroIdentificacion() {
        return numeroIdentificacion;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public String getTipoLicencia() {
        return tipoLicencia;
    }

    public String getTelefonoContacto() {
        return telefonoContacto;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Conductor{" +
                "id=" + id +
                ", numeroIdentificacion='" + numeroIdentificacion + '\'' +
                ", nombreCompleto='" + nombreCompleto + '\'' +
                ", tipoLicencia='" + tipoLicencia + '\'' +
                ", telefonoContacto='" + telefonoContacto + '\'' +
                ", estado='" + estado + '\'' +
                '}';
    }
}
