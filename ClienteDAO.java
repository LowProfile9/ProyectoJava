package com.rapidexpress.dao;

import com.rapidexpress.model.entity.Cliente;
import com.rapidexpress.utils.ConexionBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {

    public void agregar(Cliente c) {
        String sql = "INSERT INTO cliente (num_identificacion, nombre_completo, telefono, email, direccion) "
                   + "VALUES (?, ?, ?, ?, ?)";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, c.getNumIdentificacion());
            ps.setString(2, c.getNombreCompleto());
            ps.setString(3, c.getTelefono());
            ps.setString(4, c.getEmail());
            ps.setString(5, c.getDireccion());

            int filas = ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int idGenerado = rs.getInt(1);
                    System.out.println("Cliente insertado con id = " + idGenerado);
                }
            }

            System.out.println("Cliente agregado. Filas afectadas: " + filas);

        } catch (SQLException ex) {
            System.out.println("ClienteDAO.agregar - Error SQL: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public List<Cliente> listar() {
        List<Cliente> lista = new ArrayList<>();

        String sql = "SELECT id, num_identificacion, nombre_completo, telefono, email, direccion FROM cliente";

        try (Connection con = ConexionBD.conectar();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String numId = rs.getString("num_identificacion");
                String nombre = rs.getString("nombre_completo");
                String telefono = rs.getString("telefono");
                String email = rs.getString("email");
                String direccion = rs.getString("direccion");

                Cliente c = new Cliente(id, numId, nombre, telefono, email, direccion);
                lista.add(c);
            }

        } catch (SQLException ex) {
            System.out.println("ClienteDAO.listar - Error SQL: " + ex.getMessage());
            ex.printStackTrace();
        }

        return lista;
    }

    public void eliminar(int id) {
        String sql = "DELETE FROM cliente WHERE id = ?";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            int filas = ps.executeUpdate();
            System.out.println("Cliente eliminado. Filas afectadas: " + filas);

        } catch (SQLException ex) {
            System.out.println("ClienteDAO.eliminar - Error SQL: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public Cliente buscarPorId(int id) {
        String sql = "SELECT id, num_identificacion, nombre_completo, telefono, email, direccion FROM cliente WHERE id = ?";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String numId = rs.getString("num_identificacion");
                    String nombre = rs.getString("nombre_completo");
                    String telefono = rs.getString("telefono");
                    String email = rs.getString("email");
                    String direccion = rs.getString("direccion");

                    return new Cliente(id, numId, nombre, telefono, email, direccion);
                }
            }

        } catch (SQLException ex) {
            System.out.println("ClienteDAO.buscarPorId - Error SQL: " + ex.getMessage());
            ex.printStackTrace();
        }

        return null;
    }

    public void actualizar(Cliente c) {
        String sql = "UPDATE cliente SET num_identificacion = ?, nombre_completo = ?, telefono = ?, email = ?, direccion = ? WHERE id = ?";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, c.getNumIdentificacion());
            ps.setString(2, c.getNombreCompleto());
            ps.setString(3, c.getTelefono());
            ps.setString(4, c.getEmail());
            ps.setString(5, c.getDireccion());
            ps.setInt(6, c.getId());

            int filas = ps.executeUpdate();
            System.out.println("Cliente actualizado. Filas afectadas: " + filas);

        } catch (SQLException ex) {
            System.out.println("ClienteDAO.actualizar - Error SQL: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}

