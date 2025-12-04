package com.rapidexpress.model.entity;

public class Vehiculo {

    private int id;
    private String placa;
    private String marca;
    private String modelo;
    private int anioFabricacion;
    private double capacidadMaxKg;
    private String estado;
    private Integer conductorId;

    private String conductorNombre;

    public Vehiculo(String placa, String marca, String modelo, int anioFabricacion,
                    double capacidadMaxKg, String estado) {
        this.placa = placa;
        this.marca = marca;
        this.modelo = modelo;
        this.anioFabricacion = anioFabricacion;
        this.capacidadMaxKg = capacidadMaxKg;
        this.estado = estado;
    }

    public Vehiculo(int id, String placa, String marca, String modelo, int anioFabricacion,
                    double capacidadMaxKg, String estado, Integer conductorId) {
        this(placa, marca, modelo, anioFabricacion, capacidadMaxKg, estado);
        this.id = id;
        this.conductorId = conductorId;
    }

    public int getId() {
        return id;
    }

    public String getPlaca() {
        return placa;
    }

    public String getMarca() {
        return marca;
    }

    public String getModelo() {
        return modelo;
    }

    public int getAnioFabricacion() {
        return anioFabricacion;
    }

    public double getCapacidadMaxKg() {
        return capacidadMaxKg;
    }

    public String getEstado() {
        return estado;
    }

    public Integer getConductorId() {
        return conductorId;
    }

    public void setConductorNombre(String conductorNombre) {
        this.conductorNombre = conductorNombre;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        String conductor = (conductorNombre != null) ? conductorNombre :
                          (conductorId != null) ? "Conductor#" + conductorId : "Sin conductor";

        return "Vehiculo{" +
                "id=" + id +
                ", placa='" + placa + '\'' +
                ", marca='" + marca + '\'' +
                ", modelo='" + modelo + '\'' +
                ", ano=" + anioFabricacion +
                ", capacidad=" + capacidadMaxKg + " kg" +
                ", estado='" + estado + '\'' +
                ", conductor=" + conductor +
                '}';
    }
}
