package com.rapidexpress.dao;

import com.rapidexpress.model.entity.Conductor;
import com.rapidexpress.utils.ConexionBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ConductorDAO {

    public void agregar(Conductor c) {
        String sql = "INSERT INTO conductor (numero_identificacion, nombre_completo, tipo_licencia, telefono_contacto, estado_id) "
                   + "VALUES (?, ?, ?, ?, (SELECT id FROM estado_conductor WHERE nombre = ?))";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, c.getNumeroIdentificacion());
            ps.setString(2, c.getNombreCompleto());
            ps.setString(3, c.getTipoLicencia());
            ps.setString(4, c.getTelefonoContacto());
            ps.setString(5, c.getEstado());

            int filas = ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int idGenerado = rs.getInt(1);
                    System.out.println("Conductor insertado con id = " + idGenerado);
                }
            }

            System.out.println("Conductor agregado. Filas afectadas: " + filas);

        } catch (SQLException ex) {
            System.out.println("ConductorDAO.agregar - Error SQL: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public List<Conductor> listar() {
        List<Conductor> lista = new ArrayList<>();

        String sql = "SELECT c.id, c.numero_identificacion, c.nombre_completo, c.tipo_licencia, c.telefono_contacto, "
                   + "ec.nombre as estado "
                   + "FROM conductor c "
                   + "LEFT JOIN estado_conductor ec ON c.estado_id = ec.id";

        try (Connection con = ConexionBD.conectar();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String numId = rs.getString("numero_identificacion");
                String nombre = rs.getString("nombre_completo");
                String tipoLicencia = rs.getString("tipo_licencia");
                String telefono = rs.getString("telefono_contacto");
                String estado = rs.getString("estado");

                Conductor c = new Conductor(id, numId, nombre, tipoLicencia, telefono, estado);
                lista.add(c);
            }

        } catch (SQLException ex) {
            System.out.println("ConductorDAO.listar - Error SQL: " + ex.getMessage());
            ex.printStackTrace();
        }

        return lista;
    }

    public void eliminar(int id) {
        String sql = "DELETE FROM conductor WHERE id = ?";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            int filas = ps.executeUpdate();
            System.out.println("Conductor eliminado. Filas afectadas: " + filas);

        } catch (SQLException ex) {
            System.out.println("ConductorDAO.eliminar - Error SQL: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public Conductor buscarPorId(int id) {
        String sql = "SELECT c.id, c.numero_identificacion, c.nombre_completo, c.tipo_licencia, c.telefono_contacto, "
                   + "ec.nombre as estado "
                   + "FROM conductor c "
                   + "LEFT JOIN estado_conductor ec ON c.estado_id = ec.id "
                   + "WHERE c.id = ?";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String numId = rs.getString("numero_identificacion");
                    String nombre = rs.getString("nombre_completo");
                    String tipoLicencia = rs.getString("tipo_licencia");
                    String telefono = rs.getString("telefono_contacto");
                    String estado = rs.getString("estado");

                    return new Conductor(id, numId, nombre, tipoLicencia, telefono, estado);
                }
            }

        } catch (SQLException ex) {
            System.out.println("ConductorDAO.buscarPorId - Error SQL: " + ex.getMessage());
            ex.printStackTrace();
        }

        return null;
    }

    public void actualizar(Conductor c) {
        String sql = "UPDATE conductor SET numero_identificacion = ?, nombre_completo = ?, "
                   + "tipo_licencia = ?, telefono_contacto = ?, estado_id = (SELECT id FROM estado_conductor WHERE nombre = ?) "
                   + "WHERE id = ?";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, c.getNumeroIdentificacion());
            ps.setString(2, c.getNombreCompleto());
            ps.setString(3, c.getTipoLicencia());
            ps.setString(4, c.getTelefonoContacto());
            ps.setString(5, c.getEstado());
            ps.setInt(6, c.getId());

            int filas = ps.executeUpdate();
            System.out.println("Conductor actualizado. Filas afectadas: " + filas);

        } catch (SQLException ex) {
            System.out.println("ConductorDAO.actualizar - Error SQL: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public List<Conductor> listarPorEstado(String estado) {
        List<Conductor> lista = new ArrayList<>();

        String sql = "SELECT c.id, c.numero_identificacion, c.nombre_completo, c.tipo_licencia, c.telefono_contacto, "
                   + "ec.nombre as estado "
                   + "FROM conductor c "
                   + "LEFT JOIN estado_conductor ec ON c.estado_id = ec.id "
                   + "WHERE ec.nombre = ?";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, estado);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String numId = rs.getString("numero_identificacion");
                    String nombre = rs.getString("nombre_completo");
                    String tipoLicencia = rs.getString("tipo_licencia");
                    String telefono = rs.getString("telefono_contacto");
                    String estadoResult = rs.getString("estado");

                    Conductor c = new Conductor(id, numId, nombre, tipoLicencia, telefono, estadoResult);
                    lista.add(c);
                }
            }

        } catch (SQLException ex) {
            System.out.println("ConductorDAO.listarPorEstado - Error SQL: " + ex.getMessage());
            ex.printStackTrace();
        }

        return lista;
    }
}

