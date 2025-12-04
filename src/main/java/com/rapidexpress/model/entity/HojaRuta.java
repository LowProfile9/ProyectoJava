package com.rapidexpress.model.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class HojaRuta {

    private int id;
    private LocalDate fecha;
    private LocalDateTime horaInicio;
    private LocalDateTime horaFin;
    private Double pesoTotalKg;
    private Integer vehiculoId;
    private Integer conductorId;
    private String estado;

    private String vehiculoPlaca;
    private String conductorNombre;

    public HojaRuta(LocalDate fecha, Integer vehiculoId, Integer conductorId, String estado) {
        this.fecha = fecha;
        this.vehiculoId = vehiculoId;
        this.conductorId = conductorId;
        this.estado = estado;
    }

    public HojaRuta(int id, LocalDate fecha, LocalDateTime horaInicio, LocalDateTime horaFin,
                    Double pesoTotalKg, Integer vehiculoId, Integer conductorId, String estado) {
        this(fecha, vehiculoId, conductorId, estado);
        this.id = id;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.pesoTotalKg = pesoTotalKg;
    }

    public int getId() {
        return id;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public LocalDateTime getHoraInicio() {
        return horaInicio;
    }

    public LocalDateTime getHoraFin() {
        return horaFin;
    }

    public Double getPesoTotalKg() {
        return pesoTotalKg;
    }

    public Integer getVehiculoId() {
        return vehiculoId;
    }

    public Integer getConductorId() {
        return conductorId;
    }

    public String getEstado() {
        return estado;
    }

    public void setVehiculoPlaca(String vehiculoPlaca) {
        this.vehiculoPlaca = vehiculoPlaca;
    }

    public void setConductorNombre(String conductorNombre) {
        this.conductorNombre = conductorNombre;
    }

    public void setHoraInicio(LocalDateTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public void setHoraFin(LocalDateTime horaFin) {
        this.horaFin = horaFin;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        String vehiculo = (vehiculoPlaca != null) ? vehiculoPlaca :
                         (vehiculoId != null) ? "Vehiculo#" + vehiculoId : "Sin vehiculo";
        String conductor = (conductorNombre != null) ? conductorNombre :
                         (conductorId != null) ? "Conductor#" + conductorId : "Sin conductor";

        return "HojaRuta{" +
                "id=" + id +
                ", fecha=" + fecha +
                ", horaInicio=" + horaInicio +
                ", horaFin=" + horaFin +
                ", pesoTotalKg=" + pesoTotalKg +
                ", estado='" + estado + '\'' +
                ", vehiculo=" + vehiculo +
                ", conductor=" + conductor +
                '}';
    }
}
