package com.rapidexpress.service;

import com.rapidexpress.dao.*;
import com.rapidexpress.model.entity.*;
import com.rapidexpress.utils.ConexionBD;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReporteService {

    private HojaRutaDAO hojaRutaDAO;
    private PaqueteDAO paqueteDAO;
    private ConductorDAO conductorDAO;
    private VehiculoDAO vehiculoDAO;

    public ReporteService() {
        this.hojaRutaDAO = new HojaRutaDAO();
        this.paqueteDAO = new PaqueteDAO();
        this.conductorDAO = new ConductorDAO();
        this.vehiculoDAO = new VehiculoDAO();
    }

    public void generarReporteEntregasConductor(int conductorId, LocalDate fechaInicio, LocalDate fechaFin) {
        try (Connection con = ConexionBD.conectar()) {
            String sql = "SELECT hr.id, hr.fecha, COUNT(DISTINCT rp.paquete_id) as total_paquetes, " +
                        "COUNT(CASE WHEN ep.nombre = 'ENTREGADO' THEN 1 END) as paquetes_entregados, " +
                        "hr.peso_total_kg " +
                        "FROM hoja_ruta hr " +
                        "INNER JOIN ruta_paquete rp ON hr.id = rp.hoja_ruta_id " +
                        "INNER JOIN paquete p ON rp.paquete_id = p.id " +
                        "INNER JOIN estado_paquete ep ON p.estado_id = ep.id " +
                        "WHERE hr.conductor_id = ? AND hr.fecha BETWEEN ? AND ? " +
                        "GROUP BY hr.id, hr.fecha, hr.peso_total_kg " +
                        "ORDER BY hr.fecha DESC";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, conductorId);
            ps.setDate(2, java.sql.Date.valueOf(fechaInicio));
            ps.setDate(3, java.sql.Date.valueOf(fechaFin));

            ResultSet rs = ps.executeQuery();
            List<String> lineas = new ArrayList<>();
            Conductor conductor = conductorDAO.buscarPorId(conductorId);
            
            lineas.add("========================================");
            lineas.add("REPORTE DE ENTREGAS POR CONDUCTOR");
            lineas.add("========================================");
            lineas.add("Conductor: " + (conductor != null ? conductor.getNombreCompleto() : "ID " + conductorId));
            lineas.add("Periodo: " + fechaInicio + " a " + fechaFin);
            lineas.add("----------------------------------------");

            int totalRutas = 0;
            int totalPaquetes = 0;
            int totalEntregados = 0;

            while (rs.next()) {
                totalRutas++;
                int hojaRutaId = rs.getInt("id");
                LocalDate fecha = rs.getDate("fecha").toLocalDate();
                int totalPaq = rs.getInt("total_paquetes");
                int entregados = rs.getInt("paquetes_entregados");
                Double peso = rs.getObject("peso_total_kg", Double.class);

                totalPaquetes += totalPaq;
                totalEntregados += entregados;

                lineas.add("Ruta ID: " + hojaRutaId);
                lineas.add("  Fecha: " + fecha);
                lineas.add("  Total Paquetes: " + totalPaq);
                lineas.add("  Entregados: " + entregados);
                lineas.add("  Peso Total: " + (peso != null ? peso + " kg" : "N/A"));
                lineas.add("----------------------------------------");
            }

            lineas.add("RESUMEN:");
            lineas.add("  Total Rutas: " + totalRutas);
            lineas.add("  Total Paquetes: " + totalPaquetes);
            lineas.add("  Total Entregados: " + totalEntregados);
            lineas.add("========================================");

            escribirArchivo("reporte_entregas_conductor_" + conductorId + ".txt", lineas);
            imprimirLineas(lineas);

        } catch (SQLException ex) {
            System.out.println("ReporteService.generarReporteEntregasConductor - Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public void generarHistorialRutasVehiculo(int vehiculoId) {
        try (Connection con = ConexionBD.conectar()) {
            String sql = "SELECT hr.id, hr.fecha, hr.hora_inicio, hr.hora_fin, " +
                        "er.nombre as estado, hr.peso_total_kg, " +
                        "c.nombre_completo as conductor, " +
                        "COUNT(rp.paquete_id) as total_paquetes " +
                        "FROM hoja_ruta hr " +
                        "LEFT JOIN estado_ruta er ON hr.estado_id = er.id " +
                        "LEFT JOIN conductor c ON hr.conductor_id = c.id " +
                        "LEFT JOIN ruta_paquete rp ON hr.id = rp.hoja_ruta_id " +
                        "WHERE hr.vehiculo_id = ? " +
                        "GROUP BY hr.id, hr.fecha, hr.hora_inicio, hr.hora_fin, er.nombre, hr.peso_total_kg, c.nombre_completo " +
                        "ORDER BY hr.fecha DESC, hr.hora_inicio DESC";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, vehiculoId);

            ResultSet rs = ps.executeQuery();
            List<String> lineas = new ArrayList<>();
            Vehiculo vehiculo = vehiculoDAO.buscarPorId(vehiculoId);

            lineas.add("========================================");
            lineas.add("HISTORIAL DE RUTAS DE VEHICULO");
            lineas.add("========================================");
            lineas.add("Vehiculo: " + (vehiculo != null ? vehiculo.getPlaca() + " - " + vehiculo.getMarca() + " " + vehiculo.getModelo() : "ID " + vehiculoId));
            lineas.add("----------------------------------------");

            int totalRutas = 0;
            double pesoTotalAcumulado = 0;

            while (rs.next()) {
                totalRutas++;
                int hojaRutaId = rs.getInt("id");
                LocalDate fecha = rs.getDate("fecha").toLocalDate();
                java.sql.Timestamp horaInicio = rs.getTimestamp("hora_inicio");
                java.sql.Timestamp horaFin = rs.getTimestamp("hora_fin");
                String estado = rs.getString("estado");
                Double peso = rs.getObject("peso_total_kg", Double.class);
                String conductor = rs.getString("conductor");
                int totalPaquetes = rs.getInt("total_paquetes");

                if (peso != null) {
                    pesoTotalAcumulado += peso;
                }

                lineas.add("Ruta ID: " + hojaRutaId);
                lineas.add("  Fecha: " + fecha);
                lineas.add("  Hora Inicio: " + (horaInicio != null ? horaInicio.toLocalDateTime() : "N/A"));
                lineas.add("  Hora Fin: " + (horaFin != null ? horaFin.toLocalDateTime() : "N/A"));
                lineas.add("  Estado: " + estado);
                lineas.add("  Conductor: " + (conductor != null ? conductor : "N/A"));
                lineas.add("  Paquetes: " + totalPaquetes);
                lineas.add("  Peso Total: " + (peso != null ? peso + " kg" : "N/A"));
                lineas.add("----------------------------------------");
            }

            lineas.add("RESUMEN:");
            lineas.add("  Total Rutas: " + totalRutas);
            lineas.add("  Peso Total Acumulado: " + pesoTotalAcumulado + " kg");
            lineas.add("========================================");

            escribirArchivo("historial_rutas_vehiculo_" + vehiculoId + ".txt", lineas);
            imprimirLineas(lineas);

        } catch (SQLException ex) {
            System.out.println("ReporteService.generarHistorialRutasVehiculo - Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void escribirArchivo(String nombreArchivo, List<String> lineas) {
        try (PrintWriter pw = new PrintWriter(new FileWriter("reportes/" + nombreArchivo))) {
            for (String linea : lineas) {
                pw.println(linea);
            }
            System.out.println("Reporte guardado en: reportes/" + nombreArchivo);
        } catch (IOException ex) {
            System.out.println("Error al escribir archivo: " + ex.getMessage());
        }
    }

    private void imprimirLineas(List<String> lineas) {
        for (String linea : lineas) {
            System.out.println(linea);
        }
    }
}


