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
        if (c == null) {
            System.out.println("Error: No se puede agregar un cliente nulo");
            return;
        }

        String sql = "INSERT INTO cliente (num_identificacion, nombre_completo, telefono, email, direccion) "
                   + "VALUES (?, ?, ?, ?, ?)";

        Connection con = ConexionBD.conectar();
        if (con == null) {
            System.out.println("Error: No se pudo conectar a la base de datos");
            return;
        }

        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, c.getNumIdentificacion());
            ps.setString(2, c.getNombreCompleto());
            ps.setString(3, c.getTelefono());
            ps.setString(4, c.getEmail());
            ps.setString(5, c.getDireccion());

            int filas = ps.executeUpdate();

            if (filas > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        int idGenerado = rs.getInt(1);
                        System.out.println("Cliente agregado exitosamente con ID: " + idGenerado);
                    }
                }
            } else {
                System.out.println("Error: No se pudo agregar el cliente");
            }

        } catch (SQLException ex) {
            if (ex.getMessage().contains("Duplicate entry")) {
                System.out.println("Error: Ya existe un cliente con ese numero de identificacion");
            } else {
                System.out.println("Error al agregar cliente: " + ex.getMessage());
            }
        } finally {
            try {
                if (con != null) con.close();
            } catch (SQLException e) {
                System.out.println("Error al cerrar conexion");
            }
        }
    }

    public List<Cliente> listar() {
        List<Cliente> lista = new ArrayList<>();

        String sql = "SELECT id, num_identificacion, nombre_completo, telefono, email, direccion FROM cliente";

        Connection con = ConexionBD.conectar();
        if (con == null) {
            System.out.println("Error: No se pudo conectar a la base de datos");
            return lista;
        }

        try (Statement st = con.createStatement();
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
            System.out.println("Error al listar clientes: " + ex.getMessage());
        } finally {
            try {
                if (con != null) con.close();
            } catch (SQLException e) {
                System.out.println("Error al cerrar conexion");
            }
        }

        return lista;
    }

    public void eliminar(int id) {
        if (id <= 0) {
            System.out.println("Error: ID de cliente invalido");
            return;
        }

        String sql = "DELETE FROM cliente WHERE id = ?";

        Connection con = ConexionBD.conectar();
        if (con == null) {
            System.out.println("Error: No se pudo conectar a la base de datos");
            return;
        }

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            int filas = ps.executeUpdate();
            
            if (filas > 0) {
                System.out.println("Cliente eliminado exitosamente");
            } else {
                System.out.println("Error: No se encontro un cliente con ese ID");
            }

        } catch (SQLException ex) {
            if (ex.getMessage().contains("foreign key constraint")) {
                System.out.println("Error: No se puede eliminar el cliente porque tiene paquetes asociados");
            } else {
                System.out.println("Error al eliminar cliente: " + ex.getMessage());
            }
        } finally {
            try {
                if (con != null) con.close();
            } catch (SQLException e) {
                System.out.println("Error al cerrar conexion");
            }
        }
    }

    public Cliente buscarPorId(int id) {
        if (id <= 0) {
            return null;
        }

        String sql = "SELECT id, num_identificacion, nombre_completo, telefono, email, direccion FROM cliente WHERE id = ?";

        Connection con = ConexionBD.conectar();
        if (con == null) {
            System.out.println("Error: No se pudo conectar a la base de datos");
            return null;
        }

        try (PreparedStatement ps = con.prepareStatement(sql)) {
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
            System.out.println("Error al buscar cliente: " + ex.getMessage());
        } finally {
            try {
                if (con != null) con.close();
            } catch (SQLException e) {
                System.out.println("Error al cerrar conexion");
            }
        }

        return null;
    }

    public void actualizar(Cliente c) {
        if (c == null) {
            System.out.println("Error: No se puede actualizar un cliente nulo");
            return;
        }

        if (c.getId() <= 0) {
            System.out.println("Error: ID de cliente invalido");
            return;
        }

        String sql = "UPDATE cliente SET num_identificacion = ?, nombre_completo = ?, telefono = ?, email = ?, direccion = ? WHERE id = ?";

        Connection con = ConexionBD.conectar();
        if (con == null) {
            System.out.println("Error: No se pudo conectar a la base de datos");
            return;
        }

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, c.getNumIdentificacion());
            ps.setString(2, c.getNombreCompleto());
            ps.setString(3, c.getTelefono());
            ps.setString(4, c.getEmail());
            ps.setString(5, c.getDireccion());
            ps.setInt(6, c.getId());

            int filas = ps.executeUpdate();
            
            if (filas > 0) {
                System.out.println("Cliente actualizado exitosamente");
            } else {
                System.out.println("Error: No se encontro un cliente con ese ID");
            }

        } catch (SQLException ex) {
            if (ex.getMessage().contains("Duplicate entry")) {
                System.out.println("Error: Ya existe un cliente con ese numero de identificacion");
            } else {
                System.out.println("Error al actualizar cliente: " + ex.getMessage());
            }
        } finally {
            try {
                if (con != null) con.close();
            } catch (SQLException e) {
                System.out.println("Error al cerrar conexion");
            }
        }
    }
}

