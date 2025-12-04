package com.rapidexpress.dao;

import com.rapidexpress.model.entity.HojaRuta;
import com.rapidexpress.utils.ConexionBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class HojaRutaDAO {

    public void agregar(HojaRuta hr) {
        String sql = "INSERT INTO hoja_ruta (fecha, hora_inicio, hora_fin, peso_total_kg, vehiculo_id, conductor_id, estado_id) "
                   + "VALUES (?, ?, ?, ?, ?, ?, (SELECT id FROM estado_ruta WHERE nombre = ?))";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setDate(1, java.sql.Date.valueOf(hr.getFecha()));
            if (hr.getHoraInicio() != null) {
                ps.setTimestamp(2, Timestamp.valueOf(hr.getHoraInicio()));
            } else {
                ps.setNull(2, java.sql.Types.TIMESTAMP);
            }
            if (hr.getHoraFin() != null) {
                ps.setTimestamp(3, Timestamp.valueOf(hr.getHoraFin()));
            } else {
                ps.setNull(3, java.sql.Types.TIMESTAMP);
            }
            if (hr.getPesoTotalKg() != null) {
                ps.setDouble(4, hr.getPesoTotalKg());
            } else {
                ps.setNull(4, java.sql.Types.DOUBLE);
            }
            if (hr.getVehiculoId() != null) {
                ps.setInt(5, hr.getVehiculoId());
            } else {
                ps.setNull(5, java.sql.Types.INTEGER);
            }
            if (hr.getConductorId() != null) {
                ps.setInt(6, hr.getConductorId());
            } else {
                ps.setNull(6, java.sql.Types.INTEGER);
            }
            ps.setString(7, hr.getEstado());

            int filas = ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int idGenerado = rs.getInt(1);
                    System.out.println("HojaRuta insertada con id = " + idGenerado);
                }
            }

            System.out.println("HojaRuta agregada. Filas afectadas: " + filas);

        } catch (SQLException ex) {
            System.out.println("HojaRutaDAO.agregar - Error SQL: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public List<HojaRuta> listar() {
        List<HojaRuta> lista = new ArrayList<>();

        String sql = "SELECT hr.id, hr.fecha, hr.hora_inicio, hr.hora_fin, hr.peso_total_kg, "
                   + "hr.vehiculo_id, hr.conductor_id, er.nombre as estado, "
                   + "v.placa as vehiculo_placa, c.nombre_completo as conductor_nombre "
                   + "FROM hoja_ruta hr "
                   + "LEFT JOIN estado_ruta er ON hr.estado_id = er.id "
                   + "LEFT JOIN vehiculo v ON hr.vehiculo_id = v.id "
                   + "LEFT JOIN conductor c ON hr.conductor_id = c.id";

        try (Connection con = ConexionBD.conectar();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                LocalDate fecha = rs.getDate("fecha").toLocalDate();
                Timestamp horaInicio = rs.getTimestamp("hora_inicio");
                Timestamp horaFin = rs.getTimestamp("hora_fin");
                Double pesoTotal = rs.getObject("peso_total_kg", Double.class);
                Integer vehiculoId = rs.getObject("vehiculo_id", Integer.class);
                Integer conductorId = rs.getObject("conductor_id", Integer.class);
                String estado = rs.getString("estado");

                LocalDateTime horaInicioLD = horaInicio != null ? horaInicio.toLocalDateTime() : null;
                LocalDateTime horaFinLD = horaFin != null ? horaFin.toLocalDateTime() : null;

                HojaRuta hr = new HojaRuta(id, fecha, horaInicioLD, horaFinLD, pesoTotal, vehiculoId, conductorId, estado);
                String vehiculoPlaca = rs.getString("vehiculo_placa");
                String conductorNombre = rs.getString("conductor_nombre");
                if (vehiculoPlaca != null) {
                    hr.setVehiculoPlaca(vehiculoPlaca);
                }
                if (conductorNombre != null) {
                    hr.setConductorNombre(conductorNombre);
                }
                lista.add(hr);
            }

        } catch (SQLException ex) {
            System.out.println("HojaRutaDAO.listar - Error SQL: " + ex.getMessage());
            ex.printStackTrace();
        }

        return lista;
    }

    public void eliminar(int id) {
        String sql = "DELETE FROM hoja_ruta WHERE id = ?";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            int filas = ps.executeUpdate();
            System.out.println("HojaRuta eliminada. Filas afectadas: " + filas);

        } catch (SQLException ex) {
            System.out.println("HojaRutaDAO.eliminar - Error SQL: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public HojaRuta buscarPorId(int id) {
        String sql = "SELECT hr.id, hr.fecha, hr.hora_inicio, hr.hora_fin, hr.peso_total_kg, "
                   + "hr.vehiculo_id, hr.conductor_id, er.nombre as estado, "
                   + "v.placa as vehiculo_placa, c.nombre_completo as conductor_nombre "
                   + "FROM hoja_ruta hr "
                   + "LEFT JOIN estado_ruta er ON hr.estado_id = er.id "
                   + "LEFT JOIN vehiculo v ON hr.vehiculo_id = v.id "
                   + "LEFT JOIN conductor c ON hr.conductor_id = c.id "
                   + "WHERE hr.id = ?";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    LocalDate fecha = rs.getDate("fecha").toLocalDate();
                    Timestamp horaInicio = rs.getTimestamp("hora_inicio");
                    Timestamp horaFin = rs.getTimestamp("hora_fin");
                    Double pesoTotal = rs.getObject("peso_total_kg", Double.class);
                    Integer vehiculoId = rs.getObject("vehiculo_id", Integer.class);
                    Integer conductorId = rs.getObject("conductor_id", Integer.class);
                    String estado = rs.getString("estado");

                    LocalDateTime horaInicioLD = horaInicio != null ? horaInicio.toLocalDateTime() : null;
                    LocalDateTime horaFinLD = horaFin != null ? horaFin.toLocalDateTime() : null;

                    HojaRuta hr = new HojaRuta(id, fecha, horaInicioLD, horaFinLD, pesoTotal, vehiculoId, conductorId, estado);
                    String vehiculoPlaca = rs.getString("vehiculo_placa");
                    String conductorNombre = rs.getString("conductor_nombre");
                    if (vehiculoPlaca != null) {
                        hr.setVehiculoPlaca(vehiculoPlaca);
                    }
                    if (conductorNombre != null) {
                        hr.setConductorNombre(conductorNombre);
                    }
                    return hr;
                }
            }

        } catch (SQLException ex) {
            System.out.println("HojaRutaDAO.buscarPorId - Error SQL: " + ex.getMessage());
            ex.printStackTrace();
        }

        return null;
    }

    public void actualizar(HojaRuta hr) {
        String sql = "UPDATE hoja_ruta SET fecha = ?, hora_inicio = ?, hora_fin = ?, peso_total_kg = ?, "
                   + "vehiculo_id = ?, conductor_id = ?, estado_id = (SELECT id FROM estado_ruta WHERE nombre = ?) "
                   + "WHERE id = ?";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDate(1, java.sql.Date.valueOf(hr.getFecha()));
            if (hr.getHoraInicio() != null) {
                ps.setTimestamp(2, Timestamp.valueOf(hr.getHoraInicio()));
            } else {
                ps.setNull(2, java.sql.Types.TIMESTAMP);
            }
            if (hr.getHoraFin() != null) {
                ps.setTimestamp(3, Timestamp.valueOf(hr.getHoraFin()));
            } else {
                ps.setNull(3, java.sql.Types.TIMESTAMP);
            }
            if (hr.getPesoTotalKg() != null) {
                ps.setDouble(4, hr.getPesoTotalKg());
            } else {
                ps.setNull(4, java.sql.Types.DOUBLE);
            }
            if (hr.getVehiculoId() != null) {
                ps.setInt(5, hr.getVehiculoId());
            } else {
                ps.setNull(5, java.sql.Types.INTEGER);
            }
            if (hr.getConductorId() != null) {
                ps.setInt(6, hr.getConductorId());
            } else {
                ps.setNull(6, java.sql.Types.INTEGER);
            }
            ps.setString(7, hr.getEstado());
            ps.setInt(8, hr.getId());

            int filas = ps.executeUpdate();
            System.out.println("HojaRuta actualizada. Filas afectadas: " + filas);

        } catch (SQLException ex) {
            System.out.println("HojaRutaDAO.actualizar - Error SQL: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public List<HojaRuta> listarPorEstado(String estado) {
        List<HojaRuta> lista = new ArrayList<>();

        String sql = "SELECT hr.id, hr.fecha, hr.hora_inicio, hr.hora_fin, hr.peso_total_kg, "
                   + "hr.vehiculo_id, hr.conductor_id, er.nombre as estado, "
                   + "v.placa as vehiculo_placa, c.nombre_completo as conductor_nombre "
                   + "FROM hoja_ruta hr "
                   + "LEFT JOIN estado_ruta er ON hr.estado_id = er.id "
                   + "LEFT JOIN vehiculo v ON hr.vehiculo_id = v.id "
                   + "LEFT JOIN conductor c ON hr.conductor_id = c.id "
                   + "WHERE er.nombre = ?";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, estado);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    LocalDate fecha = rs.getDate("fecha").toLocalDate();
                    Timestamp horaInicio = rs.getTimestamp("hora_inicio");
                    Timestamp horaFin = rs.getTimestamp("hora_fin");
                    Double pesoTotal = rs.getObject("peso_total_kg", Double.class);
                    Integer vehiculoId = rs.getObject("vehiculo_id", Integer.class);
                    Integer conductorId = rs.getObject("conductor_id", Integer.class);
                    String estadoResult = rs.getString("estado");

                    LocalDateTime horaInicioLD = horaInicio != null ? horaInicio.toLocalDateTime() : null;
                    LocalDateTime horaFinLD = horaFin != null ? horaFin.toLocalDateTime() : null;

                    HojaRuta hr = new HojaRuta(id, fecha, horaInicioLD, horaFinLD, pesoTotal, vehiculoId, conductorId, estadoResult);
                    String vehiculoPlaca = rs.getString("vehiculo_placa");
                    String conductorNombre = rs.getString("conductor_nombre");
                    if (vehiculoPlaca != null) {
                        hr.setVehiculoPlaca(vehiculoPlaca);
                    }
                    if (conductorNombre != null) {
                        hr.setConductorNombre(conductorNombre);
                    }
                    lista.add(hr);
                }
            }

        } catch (SQLException ex) {
            System.out.println("HojaRutaDAO.listarPorEstado - Error SQL: " + ex.getMessage());
            ex.printStackTrace();
        }

        return lista;
    }
    
    public List<HojaRuta> listarPorConductor(int conductorId) {
        List<HojaRuta> lista = new ArrayList<>();

        String sql = "SELECT hr.id, hr.fecha, hr.hora_inicio, hr.hora_fin, hr.peso_total_kg, "
                   + "hr.vehiculo_id, hr.conductor_id, er.nombre as estado, "
                   + "v.placa as vehiculo_placa, c.nombre_completo as conductor_nombre "
                   + "FROM hoja_ruta hr "
                   + "LEFT JOIN estado_ruta er ON hr.estado_id = er.id "
                   + "LEFT JOIN vehiculo v ON hr.vehiculo_id = v.id "
                   + "LEFT JOIN conductor c ON hr.conductor_id = c.id "
                   + "WHERE hr.conductor_id = ? "
                   + "ORDER BY hr.fecha DESC, hr.hora_inicio DESC";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, conductorId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    LocalDate fecha = rs.getDate("fecha").toLocalDate();
                    Timestamp horaInicio = rs.getTimestamp("hora_inicio");
                    Timestamp horaFin = rs.getTimestamp("hora_fin");
                    Double pesoTotal = rs.getObject("peso_total_kg", Double.class);
                    Integer vehiculoId = rs.getObject("vehiculo_id", Integer.class);
                    Integer condId = rs.getObject("conductor_id", Integer.class);
                    String estado = rs.getString("estado");

                    LocalDateTime horaInicioLD = horaInicio != null ? horaInicio.toLocalDateTime() : null;
                    LocalDateTime horaFinLD = horaFin != null ? horaFin.toLocalDateTime() : null;

                    HojaRuta hr = new HojaRuta(id, fecha, horaInicioLD, horaFinLD, pesoTotal, vehiculoId, condId, estado);
                    String vehiculoPlaca = rs.getString("vehiculo_placa");
                    String conductorNombre = rs.getString("conductor_nombre");
                    if (vehiculoPlaca != null) {
                        hr.setVehiculoPlaca(vehiculoPlaca);
                    }
                    if (conductorNombre != null) {
                        hr.setConductorNombre(conductorNombre);
                    }
                    lista.add(hr);
                }
            }

        } catch (SQLException ex) {
            System.out.println("HojaRutaDAO.listarPorConductor - Error SQL: " + ex.getMessage());
            ex.printStackTrace();
        }

        return lista;
    }
    
    public List<HojaRuta> listarActivasPorConductor(int conductorId) {
        List<HojaRuta> lista = new ArrayList<>();

        String sql = "SELECT hr.id, hr.fecha, hr.hora_inicio, hr.hora_fin, hr.peso_total_kg, "
                   + "hr.vehiculo_id, hr.conductor_id, er.nombre as estado, "
                   + "v.placa as vehiculo_placa, c.nombre_completo as conductor_nombre "
                   + "FROM hoja_ruta hr "
                   + "LEFT JOIN estado_ruta er ON hr.estado_id = er.id "
                   + "LEFT JOIN vehiculo v ON hr.vehiculo_id = v.id "
                   + "LEFT JOIN conductor c ON hr.conductor_id = c.id "
                   + "WHERE hr.conductor_id = ? AND er.nombre = 'EN_CURSO' "
                   + "ORDER BY hr.fecha DESC, hr.hora_inicio DESC";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, conductorId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    LocalDate fecha = rs.getDate("fecha").toLocalDate();
                    Timestamp horaInicio = rs.getTimestamp("hora_inicio");
                    Timestamp horaFin = rs.getTimestamp("hora_fin");
                    Double pesoTotal = rs.getObject("peso_total_kg", Double.class);
                    Integer vehiculoId = rs.getObject("vehiculo_id", Integer.class);
                    Integer condId = rs.getObject("conductor_id", Integer.class);
                    String estado = rs.getString("estado");

                    LocalDateTime horaInicioLD = horaInicio != null ? horaInicio.toLocalDateTime() : null;
                    LocalDateTime horaFinLD = horaFin != null ? horaFin.toLocalDateTime() : null;

                    HojaRuta hr = new HojaRuta(id, fecha, horaInicioLD, horaFinLD, pesoTotal, vehiculoId, condId, estado);
                    String vehiculoPlaca = rs.getString("vehiculo_placa");
                    String conductorNombre = rs.getString("conductor_nombre");
                    if (vehiculoPlaca != null) {
                        hr.setVehiculoPlaca(vehiculoPlaca);
                    }
                    if (conductorNombre != null) {
                        hr.setConductorNombre(conductorNombre);
                    }
                    lista.add(hr);
                }
            }

        } catch (SQLException ex) {
            System.out.println("HojaRutaDAO.listarActivasPorConductor - Error SQL: " + ex.getMessage());
            ex.printStackTrace();
        }

        return lista;
    }
}

