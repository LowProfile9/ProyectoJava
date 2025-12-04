package com.rapidexpress.model.entity;

import java.time.LocalDate;

public class Mantenimiento {

    private int id;
    private LocalDate fechaPrograma;
    private LocalDate fechaReal;
    private String descripcion;
    private Double costo;
    private Integer kilometraje;
    private Integer vehiculoId;
    private String tipoMantenimiento;

    private String vehiculoPlaca;

    public Mantenimiento(LocalDate fechaPrograma, LocalDate fechaReal, String descripcion,
                         Double costo, Integer kilometraje, Integer vehiculoId, String tipoMantenimiento) {
        this.fechaPrograma = fechaPrograma;
        this.fechaReal = fechaReal;
        this.descripcion = descripcion;
        this.costo = costo;
        this.kilometraje = kilometraje;
        this.vehiculoId = vehiculoId;
        this.tipoMantenimiento = tipoMantenimiento;
    }

    public Mantenimiento(int id, LocalDate fechaPrograma, LocalDate fechaReal, String descripcion,
                         Double costo, Integer kilometraje, Integer vehiculoId, String tipoMantenimiento) {
        this(fechaPrograma, fechaReal, descripcion, costo, kilometraje, vehiculoId, tipoMantenimiento);
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public LocalDate getFechaPrograma() {
        return fechaPrograma;
    }

    public LocalDate getFechaReal() {
        return fechaReal;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public Double getCosto() {
        return costo;
    }

    public Integer getKilometraje() {
        return kilometraje;
    }

    public Integer getVehiculoId() {
        return vehiculoId;
    }

    public String getTipoMantenimiento() {
        return tipoMantenimiento;
    }

    public void setVehiculoPlaca(String vehiculoPlaca) {
        this.vehiculoPlaca = vehiculoPlaca;
    }

    @Override
    public String toString() {
        String vehiculo = (vehiculoPlaca != null) ? vehiculoPlaca :
                         (vehiculoId != null) ? "Vehiculo#" + vehiculoId : "Sin vehiculo";

        return "Mantenimiento{" +
                "id=" + id +
                ", fechaPrograma=" + fechaPrograma +
                ", fechaReal=" + fechaReal +
                ", descripcion='" + descripcion + '\'' +
                ", costo=" + costo +
                ", kilometraje=" + kilometraje +
                ", tipoMantenimiento='" + tipoMantenimiento + '\'' +
                ", vehiculo=" + vehiculo +
                '}';
    }
}
