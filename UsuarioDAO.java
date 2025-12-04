package com.rapidexpress.dao;

import com.rapidexpress.model.entity.Usuario;
import com.rapidexpress.utils.ConexionBD;

import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {
    
    public Usuario buscarPorUsername(String username) {
        String sql = "SELECT u.id, u.username, u.password, u.nombre_completo, u.rol_id, " +
                    "u.conductor_id, u.activo, r.nombre as rol_nombre " +
                    "FROM usuario u " +
                    "INNER JOIN rol_usuario r ON u.rol_id = r.id " +
                    "WHERE u.username = ? AND u.activo = TRUE";
        
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, username);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id");
                    String password = rs.getString("password");
                    String nombreCompleto = rs.getString("nombre_completo");
                    int rolId = rs.getInt("rol_id");
                    Integer conductorId = rs.getObject("conductor_id", Integer.class);
                    boolean activo = rs.getBoolean("activo");
                    String rolNombre = rs.getString("rol_nombre");
                    
                    Usuario usuario = new Usuario(id, username, password, nombreCompleto, rolId, conductorId, activo);
                    usuario.setRolNombre(rolNombre);
                    return usuario;
                }
            }
            
        } catch (SQLException ex) {
            System.out.println("UsuarioDAO.buscarPorUsername - Error SQL: " + ex.getMessage());
            ex.printStackTrace();
        }
        
        return null;
    }
    
    public Usuario buscarPorId(int id) {
        String sql = "SELECT u.id, u.username, u.password, u.nombre_completo, u.rol_id, " +
                    "u.conductor_id, u.activo, r.nombre as rol_nombre " +
                    "FROM usuario u " +
                    "INNER JOIN rol_usuario r ON u.rol_id = r.id " +
                    "WHERE u.id = ?";
        
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String username = rs.getString("username");
                    String password = rs.getString("password");
                    String nombreCompleto = rs.getString("nombre_completo");
                    int rolId = rs.getInt("rol_id");
                    Integer conductorId = rs.getObject("conductor_id", Integer.class);
                    boolean activo = rs.getBoolean("activo");
                    String rolNombre = rs.getString("rol_nombre");
                    
                    Usuario usuario = new Usuario(id, username, password, nombreCompleto, rolId, conductorId, activo);
                    usuario.setRolNombre(rolNombre);
                    return usuario;
                }
            }
            
        } catch (SQLException ex) {
            System.out.println("UsuarioDAO.buscarPorId - Error SQL: " + ex.getMessage());
            ex.printStackTrace();
        }
        
        return null;
    }
    
    public List<Usuario> listar() {
        List<Usuario> lista = new ArrayList<>();
        
        String sql = "SELECT u.id, u.username, u.password, u.nombre_completo, u.rol_id, " +
                    "u.conductor_id, u.activo, r.nombre as rol_nombre " +
                    "FROM usuario u " +
                    "INNER JOIN rol_usuario r ON u.rol_id = r.id " +
                    "ORDER BY u.username";
        
        try (Connection con = ConexionBD.conectar();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            
            while (rs.next()) {
                int id = rs.getInt("id");
                String username = rs.getString("username");
                String password = rs.getString("password");
                String nombreCompleto = rs.getString("nombre_completo");
                int rolId = rs.getInt("rol_id");
                Integer conductorId = rs.getObject("conductor_id", Integer.class);
                boolean activo = rs.getBoolean("activo");
                String rolNombre = rs.getString("rol_nombre");
                
                Usuario usuario = new Usuario(id, username, password, nombreCompleto, rolId, conductorId, activo);
                usuario.setRolNombre(rolNombre);
                lista.add(usuario);
            }
            
        } catch (SQLException ex) {
            System.out.println("UsuarioDAO.listar - Error SQL: " + ex.getMessage());
            ex.printStackTrace();
        }
        
        return lista;
    }
    
    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hashBytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception ex) {
            System.out.println("UsuarioDAO.hashPassword - Error: " + ex.getMessage());
            return password;
        }
    }
}

