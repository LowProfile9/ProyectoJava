package com.rapidexpress.model.entity;

public class RutaPaquete {

    private int id;
    private int hojaRutaId;
    private int paqueteId;
    private Integer ordenEntrega;

    public RutaPaquete(int hojaRutaId, int paqueteId, Integer ordenEntrega) {
        this.hojaRutaId = hojaRutaId;
        this.paqueteId = paqueteId;
        this.ordenEntrega = ordenEntrega;
    }

    public RutaPaquete(int id, int hojaRutaId, int paqueteId, Integer ordenEntrega) {
        this(hojaRutaId, paqueteId, ordenEntrega);
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public int getHojaRutaId() {
        return hojaRutaId;
    }

    public int getPaqueteId() {
        return paqueteId;
    }

    public Integer getOrdenEntrega() {
        return ordenEntrega;
    }

    @Override
    public String toString() {
        return "RutaPaquete{" +
                "id=" + id +
                ", hojaRutaId=" + hojaRutaId +
                ", paqueteId=" + paqueteId +
                ", ordenEntrega=" + ordenEntrega +
                '}';
    }
}
