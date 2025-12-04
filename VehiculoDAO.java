package com.rapidexpress.dao;

import com.rapidexpress.model.entity.Vehiculo;
import com.rapidexpress.utils.ConexionBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class VehiculoDAO {

    public void agregar(Vehiculo v) {
        String sql = "INSERT INTO vehiculo (placa, marca, modelo, anio_fabricacion, capacidad_max_kg, estado_id, conductor_id) "
                   + "VALUES (?, ?, ?, ?, ?, (SELECT id FROM estado_vehiculo WHERE nombre = ?), ?)";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, v.getPlaca());
            ps.setString(2, v.getMarca());
            ps.setString(3, v.getModelo());
            ps.setInt(4, v.getAnioFabricacion());
            ps.setDouble(5, v.getCapacidadMaxKg());
            ps.setString(6, v.getEstado());
            if (v.getConductorId() != null) {
                ps.setInt(7, v.getConductorId());
            } else {
                ps.setNull(7, java.sql.Types.INTEGER);
            }

            int filas = ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int idGenerado = rs.getInt(1);
                    System.out.println("Vehiculo insertado con id = " + idGenerado);
                }
            }

            System.out.println("Vehiculo agregado. Filas afectadas: " + filas);

        } catch (SQLException ex) {
            System.out.println("VehiculoDAO.agregar - Error SQL: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public List<Vehiculo> listar() {
        List<Vehiculo> lista = new ArrayList<>();

        String sql = "SELECT v.id, v.placa, v.marca, v.modelo, v.anio_fabricacion, v.capacidad_max_kg, "
                   + "ev.nombre as estado, v.conductor_id, c.nombre_completo as conductor_nombre "
                   + "FROM vehiculo v "
                   + "LEFT JOIN estado_vehiculo ev ON v.estado_id = ev.id "
                   + "LEFT JOIN conductor c ON v.conductor_id = c.id";

        try (Connection con = ConexionBD.conectar();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String placa = rs.getString("placa");
                String marca = rs.getString("marca");
                String modelo = rs.getString("modelo");
                int anio = rs.getInt("anio_fabricacion");
                double capacidad = rs.getDouble("capacidad_max_kg");
                String estado = rs.getString("estado");
                Integer conductorId = rs.getObject("conductor_id", Integer.class);

                Vehiculo v = new Vehiculo(id, placa, marca, modelo, anio, capacidad, estado, conductorId);
                String conductorNombre = rs.getString("conductor_nombre");
                if (conductorNombre != null) {
                    v.setConductorNombre(conductorNombre);
                }
                lista.add(v);
            }

        } catch (SQLException ex) {
            System.out.println("VehiculoDAO.listar - Error SQL: " + ex.getMessage());
            ex.printStackTrace();
        }

        return lista;
    }

    public void eliminar(int id) {
        String sql = "DELETE FROM vehiculo WHERE id = ?";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            int filas = ps.executeUpdate();
            System.out.println("Vehiculo eliminado. Filas afectadas: " + filas);

        } catch (SQLException ex) {
            System.out.println("VehiculoDAO.eliminar - Error SQL: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public Vehiculo buscarPorId(int id) {
        String sql = "SELECT v.id, v.placa, v.marca, v.modelo, v.anio_fabricacion, v.capacidad_max_kg, "
                   + "ev.nombre as estado, v.conductor_id, c.nombre_completo as conductor_nombre "
                   + "FROM vehiculo v "
                   + "LEFT JOIN estado_vehiculo ev ON v.estado_id = ev.id "
                   + "LEFT JOIN conductor c ON v.conductor_id = c.id "
                   + "WHERE v.id = ?";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String placa = rs.getString("placa");
                    String marca = rs.getString("marca");
                    String modelo = rs.getString("modelo");
                    int anio = rs.getInt("anio_fabricacion");
                    double capacidad = rs.getDouble("capacidad_max_kg");
                    String estado = rs.getString("estado");
                    Integer conductorId = rs.getObject("conductor_id", Integer.class);

                    Vehiculo v = new Vehiculo(id, placa, marca, modelo, anio, capacidad, estado, conductorId);
                    String conductorNombre = rs.getString("conductor_nombre");
                    if (conductorNombre != null) {
                        v.setConductorNombre(conductorNombre);
                    }
                    return v;
                }
            }

        } catch (SQLException ex) {
            System.out.println("VehiculoDAO.buscarPorId - Error SQL: " + ex.getMessage());
            ex.printStackTrace();
        }

        return null;
    }

    public void actualizar(Vehiculo v) {
        String sql = "UPDATE vehiculo SET placa = ?, marca = ?, modelo = ?, anio_fabricacion = ?, "
                   + "capacidad_max_kg = ?, estado_id = (SELECT id FROM estado_vehiculo WHERE nombre = ?), "
                   + "conductor_id = ? WHERE id = ?";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, v.getPlaca());
            ps.setString(2, v.getMarca());
            ps.setString(3, v.getModelo());
            ps.setInt(4, v.getAnioFabricacion());
            ps.setDouble(5, v.getCapacidadMaxKg());
            ps.setString(6, v.getEstado());
            if (v.getConductorId() != null) {
                ps.setInt(7, v.getConductorId());
            } else {
                ps.setNull(7, java.sql.Types.INTEGER);
            }
            ps.setInt(8, v.getId());

            int filas = ps.executeUpdate();
            System.out.println("Vehiculo actualizado. Filas afectadas: " + filas);

        } catch (SQLException ex) {
            System.out.println("VehiculoDAO.actualizar - Error SQL: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}

