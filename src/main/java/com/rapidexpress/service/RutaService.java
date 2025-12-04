/**
 * Service class for managing routes and route-package relationships
 * Handles route creation, package assignment, and route lifecycle
 */
package com.rapidexpress.service;

import com.rapidexpress.dao.*;
import com.rapidexpress.model.entity.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import com.rapidexpress.utils.ConexionBD;

public class RutaService {

    private HojaRutaDAO hojaRutaDAO;
    private RutaPaqueteDAO rutaPaqueteDAO;
    private VehiculoDAO vehiculoDAO;
    private ConductorDAO conductorDAO;
    private PaqueteDAO paqueteDAO;

    /**
     * Initializes RutaService with all required DAO dependencies
     */
    public RutaService() {
        this.hojaRutaDAO = new HojaRutaDAO();
        this.rutaPaqueteDAO = new RutaPaqueteDAO();
        this.vehiculoDAO = new VehiculoDAO();
        this.conductorDAO = new ConductorDAO();
        this.paqueteDAO = new PaqueteDAO();
    }

    /**
     * Validates if a package can be added to a route based on vehicle capacity
     * @param hojaRutaId Route ID
     * @param paqueteId Package ID
     * @return true if package fits, false otherwise
     */
    public boolean validarCapacidad(int hojaRutaId, int paqueteId) {
        try (Connection con = ConexionBD.conectar()) {
            String sql = "SELECT v.capacidad_max_kg, " +
                        "COALESCE(SUM(p.peso_kg), 0) as peso_actual, " +
                        "(SELECT peso_kg FROM paquete WHERE id = ?) as peso_paquete " +
                        "FROM hoja_ruta hr " +
                        "INNER JOIN vehiculo v ON hr.vehiculo_id = v.id " +
                        "LEFT JOIN ruta_paquete rp ON hr.id = rp.hoja_ruta_id " +
                        "LEFT JOIN paquete p ON rp.paquete_id = p.id " +
                        "WHERE hr.id = ? " +
                        "GROUP BY v.capacidad_max_kg";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, paqueteId);
            ps.setInt(2, hojaRutaId);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                double capacidad = rs.getDouble("capacidad_max_kg");
                double pesoActual = rs.getDouble("peso_actual");
                double pesoPaquete = rs.getDouble("peso_paquete");

                return (pesoActual + pesoPaquete) <= capacidad;
            }
        } catch (SQLException ex) {
            System.out.println("RutaService.validarCapacidad - Error: " + ex.getMessage());
        }
        return false;
    }

    /**
     * Starts a route and updates vehicle, driver and package statuses
     */
    public void iniciarRuta(int hojaRutaId) {
        HojaRuta hr = hojaRutaDAO.buscarPorId(hojaRutaId);
        if (hr == null) {
            System.out.println("Error: Hoja de ruta no encontrada");
            return;
        }

        if (!"PLANIFICADA".equals(hr.getEstado())) {
            System.out.println("Error: Solo se pueden iniciar rutas planificadas");
            return;
        }

        hr.setHoraInicio(LocalDateTime.now());
        hr.setEstado("EN_CURSO");
        hojaRutaDAO.actualizar(hr);

        Vehiculo v = vehiculoDAO.buscarPorId(hr.getVehiculoId());
        if (v != null) {
            v.setEstado("EN_RUTA");
            vehiculoDAO.actualizar(v);
        }

        Conductor c = conductorDAO.buscarPorId(hr.getConductorId());
        if (c != null) {
            c.setEstado("EN_RUTA");
            conductorDAO.actualizar(c);
        }

        List<RutaPaquete> paquetes = obtenerPaquetesDeRuta(hojaRutaId);
        for (RutaPaquete rp : paquetes) {
            paqueteDAO.cambiarEstado(rp.getPaqueteId(), "EN_TRANSITO");
        }

        System.out.println("Ruta iniciada correctamente");
    }

    /**
     * Finalizes a route and resets vehicle and driver statuses
     */
    public void finalizarRuta(int hojaRutaId) {
        HojaRuta hr = hojaRutaDAO.buscarPorId(hojaRutaId);
        if (hr == null) {
            System.out.println("Error: Hoja de ruta no encontrada");
            return;
        }

        if (!"EN_CURSO".equals(hr.getEstado())) {
            System.out.println("Error: Solo se pueden finalizar rutas en curso");
            return;
        }

        hr.setHoraFin(LocalDateTime.now());
        hr.setEstado("FINALIZADO");
        hojaRutaDAO.actualizar(hr);

        Vehiculo v = vehiculoDAO.buscarPorId(hr.getVehiculoId());
        if (v != null) {
            v.setEstado("DISPONIBLE");
            vehiculoDAO.actualizar(v);
        }

        Conductor c = conductorDAO.buscarPorId(hr.getConductorId());
        if (c != null) {
            c.setEstado("ACTIVO");
            conductorDAO.actualizar(c);
        }

        System.out.println("Ruta finalizada correctamente");
    }

    public void agregarPaqueteARuta(int hojaRutaId, int paqueteId, Integer ordenEntrega) {
        if (!validarCapacidad(hojaRutaId, paqueteId)) {
            System.out.println("Error: El peso total excede la capacidad máxima del vehículo");
            return;
        }

        Paquete p = paqueteDAO.buscarPorId(paqueteId);
        if (p == null) {
            System.out.println("Error: Paquete no encontrado");
            return;
        }

        if (!"EN_BODEGA".equals(p.getEstado())) {
            System.out.println("Error: Solo se pueden agregar paquetes en bodega a una ruta");
            return;
        }

        RutaPaquete rp = new RutaPaquete(hojaRutaId, paqueteId, ordenEntrega);
        rutaPaqueteDAO.agregar(rp);

        paqueteDAO.cambiarEstado(paqueteId, "ASIGNADO_A_RUTA");

        calcularPesoTotalRuta(hojaRutaId);
        System.out.println("Paquete agregado a la ruta correctamente");
    }

    /**
     * Calculates and updates the total weight of all packages in a route
     * @param hojaRutaId Route ID
     */
    public void calcularPesoTotalRuta(int hojaRutaId) {
        try (Connection con = ConexionBD.conectar()) {
            String sql = "UPDATE hoja_ruta SET peso_total_kg = (" +
                        "SELECT COALESCE(SUM(p.peso_kg), 0) " +
                        "FROM ruta_paquete rp " +
                        "INNER JOIN paquete p ON rp.paquete_id = p.id " +
                        "WHERE rp.hoja_ruta_id = ?" +
                        ") WHERE id = ?";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, hojaRutaId);
            ps.setInt(2, hojaRutaId);
            ps.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("RutaService.calcularPesoTotalRuta - Error: " + ex.getMessage());
        }
    }

    public List<RutaPaquete> obtenerPaquetesDeRuta(int hojaRutaId) {
        List<RutaPaquete> todos = rutaPaqueteDAO.listar();
        List<RutaPaquete> filtrados = new ArrayList<>();
        for (RutaPaquete rp : todos) {
            if (rp.getHojaRutaId() == hojaRutaId) {
                filtrados.add(rp);
            }
        }
        return filtrados;
    }
}






