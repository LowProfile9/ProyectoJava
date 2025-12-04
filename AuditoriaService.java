package com.rapidexpress.service;

import com.rapidexpress.utils.ConexionBD;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class AuditoriaService {
    
    private String usuarioActual = null;

    public void setUsuarioActual(String usuario) {
        this.usuarioActual = usuario;
    }

    public void registrarOperacion(String tabla, String operacion, int idRegistro, String descripcion) {
        String usuario = usuarioActual != null ? usuarioActual : "SISTEMA";
        
        try (Connection con = ConexionBD.conectar()) {
            if (con == null) {
                System.out.println("AuditoriaService.registrarOperacion - Error: No se pudo conectar a la BD");
                escribirLogArchivo(tabla, operacion, idRegistro, descripcion, usuario);
                return;
            }
            
            try (PreparedStatement ps = con.prepareStatement("INSERT INTO auditoria (tabla_afectada, operacion, id_registro, descripcion, usuario) VALUES (?, ?, ?, ?, ?)")) {
                ps.setString(1, tabla);
                ps.setString(2, operacion);
                ps.setInt(3, idRegistro);
                ps.setString(4, descripcion);
                ps.setString(5, usuario);
                ps.executeUpdate();
            }

            escribirLogArchivo(tabla, operacion, idRegistro, descripcion, usuario);

        } catch (SQLException ex) {
            System.out.println("AuditoriaService.registrarOperacion - Error: " + ex.getMessage());
            ex.printStackTrace();
            escribirLogArchivo(tabla, operacion, idRegistro, descripcion, usuario);
        }
    }
    
    private void escribirLogArchivo(String tabla, String operacion, int idRegistro, String descripcion, String usuario) {
        try (PrintWriter pw = new PrintWriter(new FileWriter("logs/auditoria.txt", true))) {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            pw.println("[" + timestamp + "] Usuario: " + usuario + " - " + operacion + " en " + tabla + " (ID: " + idRegistro + ") - " + descripcion);
        } catch (IOException ex) {
            System.out.println("Error al escribir en archivo de auditoria: " + ex.getMessage());
        }
    }

    public void listarAuditoria(int limite) {
        try (Connection con = ConexionBD.conectar()) {
            if (con == null) {
                System.out.println("AuditoriaService.listarAuditoria - Error: No se pudo conectar a la BD");
                return;
            }
            
            try (Statement st = con.createStatement();
                 ResultSet rs = st.executeQuery("SELECT * FROM auditoria ORDER BY fecha_hora DESC LIMIT " + limite)) {

                System.out.println("\n========================================");
                System.out.println("REGISTRO DE AUDITORIA");
                System.out.println("========================================");

                while (rs.next()) {
                    System.out.println("ID: " + rs.getInt("id"));
                    System.out.println("Tabla: " + rs.getString("tabla_afectada"));
                    System.out.println("Operacion: " + rs.getString("operacion"));
                    System.out.println("ID Registro: " + rs.getInt("id_registro"));
                    System.out.println("Descripcion: " + rs.getString("descripcion"));
                    System.out.println("Usuario: " + rs.getString("usuario"));
                    System.out.println("Fecha/Hora: " + rs.getTimestamp("fecha_hora"));
                    System.out.println("----------------------------------------");
                }
            }

        } catch (SQLException ex) {
            System.out.println("AuditoriaService.listarAuditoria - Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

}


