package com.rapidexpress.model.entity;

public class Paquete {

    private int id;
    private String codigoSeguimiento;
    private String descripcion;
    private double pesoKg;
    private double largoCm;
    private double anchoCm;
    private double altoCm;
    private String direccionOrigen;
    private String direccionDestino;
    private Integer remitenteId;
    private Integer destinatarioId;
    private String estado;

    private String remitenteNombre;
    private String destinatarioNombre;

    public Paquete(String codigoSeguimiento, String descripcion, double pesoKg, double largoCm,
                   double anchoCm, double altoCm, String direccionOrigen, String direccionDestino,
                   Integer remitenteId, Integer destinatarioId, String estado) {
        this.codigoSeguimiento = codigoSeguimiento;
        this.descripcion = descripcion;
        this.pesoKg = pesoKg;
        this.largoCm = largoCm;
        this.anchoCm = anchoCm;
        this.altoCm = altoCm;
        this.direccionOrigen = direccionOrigen;
        this.direccionDestino = direccionDestino;
        this.remitenteId = remitenteId;
        this.destinatarioId = destinatarioId;
        this.estado = estado;
    }

    public Paquete(int id, String codigoSeguimiento, String descripcion, double pesoKg, double largoCm,
                   double anchoCm, double altoCm, String direccionOrigen, String direccionDestino,
                   Integer remitenteId, Integer destinatarioId, String estado) {
        this(codigoSeguimiento, descripcion, pesoKg, largoCm, anchoCm, altoCm, direccionOrigen,
             direccionDestino, remitenteId, destinatarioId, estado);
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getCodigoSeguimiento() {
        return codigoSeguimiento;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public double getPesoKg() {
        return pesoKg;
    }

    public double getLargoCm() {
        return largoCm;
    }

    public double getAnchoCm() {
        return anchoCm;
    }

    public double getAltoCm() {
        return altoCm;
    }

    public String getDireccionOrigen() {
        return direccionOrigen;
    }

    public String getDireccionDestino() {
        return direccionDestino;
    }

    public Integer getRemitenteId() {
        return remitenteId;
    }

    public Integer getDestinatarioId() {
        return destinatarioId;
    }

    public String getEstado() {
        return estado;
    }

    public void setRemitenteNombre(String remitenteNombre) {
        this.remitenteNombre = remitenteNombre;
    }

    public void setDestinatarioNombre(String destinatarioNombre) {
        this.destinatarioNombre = destinatarioNombre;
    }

    @Override
    public String toString() {
        String remitente = (remitenteNombre != null) ? remitenteNombre :
                          (remitenteId != null) ? "Cliente#" + remitenteId : "Sin remitente";
        String destinatario = (destinatarioNombre != null) ? destinatarioNombre :
                             (destinatarioId != null) ? "Cliente#" + destinatarioId : "Sin destinatario";

        return "Paquete{" +
                "id=" + id +
                ", codigoSeguimiento='" + codigoSeguimiento + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", pesoKg=" + pesoKg +
                ", dimensiones=" + largoCm + "x" + anchoCm + "x" + altoCm + " cm" +
                ", estado='" + estado + '\'' +
                ", remitente=" + remitente +
                ", destinatario=" + destinatario +
                '}';
    }
}
