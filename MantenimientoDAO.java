package com.rapidexpress.dao;

import com.rapidexpress.model.entity.Mantenimiento;
import com.rapidexpress.utils.ConexionBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MantenimientoDAO {

    public void agregar(Mantenimiento m) {
        String sql = "INSERT INTO mantenimiento (fecha_programa, fecha_real, descripcion, costo, kilometraje, "
                   + "vehiculo_id, tipo_mantenimiento_id) "
                   + "VALUES (?, ?, ?, ?, ?, ?, (SELECT id FROM tipo_mantenimiento WHERE nombre = ?))";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            if (m.getFechaPrograma() != null) {
                ps.setDate(1, java.sql.Date.valueOf(m.getFechaPrograma()));
            } else {
                ps.setNull(1, java.sql.Types.DATE);
            }
            if (m.getFechaReal() != null) {
                ps.setDate(2, java.sql.Date.valueOf(m.getFechaReal()));
            } else {
                ps.setNull(2, java.sql.Types.DATE);
            }
            ps.setString(3, m.getDescripcion());
            if (m.getCosto() != null) {
                ps.setDouble(4, m.getCosto());
            } else {
                ps.setNull(4, java.sql.Types.DOUBLE);
            }
            if (m.getKilometraje() != null) {
                ps.setInt(5, m.getKilometraje());
            } else {
                ps.setNull(5, java.sql.Types.INTEGER);
            }
            if (m.getVehiculoId() != null) {
                ps.setInt(6, m.getVehiculoId());
            } else {
                ps.setNull(6, java.sql.Types.INTEGER);
            }
            ps.setString(7, m.getTipoMantenimiento());

            int filas = ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int idGenerado = rs.getInt(1);
                    System.out.println("Mantenimiento insertado con id = " + idGenerado);
                }
            }

            System.out.println("Mantenimiento agregado. Filas afectadas: " + filas);

        } catch (SQLException ex) {
            System.out.println("MantenimientoDAO.agregar - Error SQL: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public List<Mantenimiento> listar() {
        List<Mantenimiento> lista = new ArrayList<>();

        String sql = "SELECT m.id, m.fecha_programa, m.fecha_real, m.descripcion, m.costo, m.kilometraje, "
                   + "m.vehiculo_id, tm.nombre as tipo_mantenimiento, v.placa as vehiculo_placa "
                   + "FROM mantenimiento m "
                   + "LEFT JOIN tipo_mantenimiento tm ON m.tipo_mantenimiento_id = tm.id "
                   + "LEFT JOIN vehiculo v ON m.vehiculo_id = v.id";

        try (Connection con = ConexionBD.conectar();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                java.sql.Date fechaPrograma = rs.getDate("fecha_programa");
                java.sql.Date fechaReal = rs.getDate("fecha_real");
                String descripcion = rs.getString("descripcion");
                Double costo = rs.getObject("costo", Double.class);
                Integer kilometraje = rs.getObject("kilometraje", Integer.class);
                Integer vehiculoId = rs.getObject("vehiculo_id", Integer.class);
                String tipoMantenimiento = rs.getString("tipo_mantenimiento");

                LocalDate fechaProgramaLD = fechaPrograma != null ? fechaPrograma.toLocalDate() : null;
                LocalDate fechaRealLD = fechaReal != null ? fechaReal.toLocalDate() : null;

                Mantenimiento m = new Mantenimiento(id, fechaProgramaLD, fechaRealLD, descripcion, costo,
                                                   kilometraje, vehiculoId, tipoMantenimiento);
                String vehiculoPlaca = rs.getString("vehiculo_placa");
                if (vehiculoPlaca != null) {
                    m.setVehiculoPlaca(vehiculoPlaca);
                }
                lista.add(m);
            }

        } catch (SQLException ex) {
            System.out.println("MantenimientoDAO.listar - Error SQL: " + ex.getMessage());
            ex.printStackTrace();
        }

        return lista;
    }

    public void eliminar(int id) {
        String sql = "DELETE FROM mantenimiento WHERE id = ?";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            int filas = ps.executeUpdate();
            System.out.println("Mantenimiento eliminado. Filas afectadas: " + filas);

        } catch (SQLException ex) {
            System.out.println("MantenimientoDAO.eliminar - Error SQL: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public Mantenimiento buscarPorId(int id) {
        String sql = "SELECT m.id, m.fecha_programa, m.fecha_real, m.descripcion, m.costo, m.kilometraje, "
                   + "m.vehiculo_id, tm.nombre as tipo_mantenimiento, v.placa as vehiculo_placa "
                   + "FROM mantenimiento m "
                   + "LEFT JOIN tipo_mantenimiento tm ON m.tipo_mantenimiento_id = tm.id "
                   + "LEFT JOIN vehiculo v ON m.vehiculo_id = v.id "
                   + "WHERE m.id = ?";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    java.sql.Date fechaPrograma = rs.getDate("fecha_programa");
                    java.sql.Date fechaReal = rs.getDate("fecha_real");
                    String descripcion = rs.getString("descripcion");
                    Double costo = rs.getObject("costo", Double.class);
                    Integer kilometraje = rs.getObject("kilometraje", Integer.class);
                    Integer vehiculoId = rs.getObject("vehiculo_id", Integer.class);
                    String tipoMantenimiento = rs.getString("tipo_mantenimiento");

                    LocalDate fechaProgramaLD = fechaPrograma != null ? fechaPrograma.toLocalDate() : null;
                    LocalDate fechaRealLD = fechaReal != null ? fechaReal.toLocalDate() : null;

                    Mantenimiento m = new Mantenimiento(id, fechaProgramaLD, fechaRealLD, descripcion, costo,
                                                       kilometraje, vehiculoId, tipoMantenimiento);
                    String vehiculoPlaca = rs.getString("vehiculo_placa");
                    if (vehiculoPlaca != null) {
                        m.setVehiculoPlaca(vehiculoPlaca);
                    }
                    return m;
                }
            }

        } catch (SQLException ex) {
            System.out.println("MantenimientoDAO.buscarPorId - Error SQL: " + ex.getMessage());
            ex.printStackTrace();
        }

        return null;
    }

    public List<Mantenimiento> listarPorVehiculo(int vehiculoId) {
        List<Mantenimiento> lista = new ArrayList<>();

        String sql = "SELECT m.id, m.fecha_programa, m.fecha_real, m.descripcion, m.costo, m.kilometraje, "
                   + "m.vehiculo_id, tm.nombre as tipo_mantenimiento, v.placa as vehiculo_placa "
                   + "FROM mantenimiento m "
                   + "LEFT JOIN tipo_mantenimiento tm ON m.tipo_mantenimiento_id = tm.id "
                   + "LEFT JOIN vehiculo v ON m.vehiculo_id = v.id "
                   + "WHERE m.vehiculo_id = ? "
                   + "ORDER BY m.fecha_programa DESC, m.fecha_real DESC";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, vehiculoId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    java.sql.Date fechaPrograma = rs.getDate("fecha_programa");
                    java.sql.Date fechaReal = rs.getDate("fecha_real");
                    String descripcion = rs.getString("descripcion");
                    Double costo = rs.getObject("costo", Double.class);
                    Integer kilometraje = rs.getObject("kilometraje", Integer.class);
                    Integer vehiculoIdResult = rs.getObject("vehiculo_id", Integer.class);
                    String tipoMantenimiento = rs.getString("tipo_mantenimiento");

                    LocalDate fechaProgramaLD = fechaPrograma != null ? fechaPrograma.toLocalDate() : null;
                    LocalDate fechaRealLD = fechaReal != null ? fechaReal.toLocalDate() : null;

                    Mantenimiento m = new Mantenimiento(id, fechaProgramaLD, fechaRealLD, descripcion, costo,
                                                       kilometraje, vehiculoIdResult, tipoMantenimiento);
                    String vehiculoPlaca = rs.getString("vehiculo_placa");
                    if (vehiculoPlaca != null) {
                        m.setVehiculoPlaca(vehiculoPlaca);
                    }
                    lista.add(m);
                }
            }

        } catch (SQLException ex) {
            System.out.println("MantenimientoDAO.listarPorVehiculo - Error SQL: " + ex.getMessage());
            ex.printStackTrace();
        }

        return lista;
    }
}

