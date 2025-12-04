package com.rapidexpress.dao;

import com.rapidexpress.model.entity.Paquete;
import com.rapidexpress.utils.ConexionBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class PaqueteDAO {

    public String generarCodigoSeguimiento() {
        String fecha = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String prefijo = "RE-" + fecha + "-";
        
        try (Connection con = ConexionBD.conectar()) {
            if (con == null) {
                System.out.println("PaqueteDAO.generarCodigoSeguimiento - Error: No se pudo conectar a la BD");
                return prefijo + String.format("%04d", 1);
            }
            
            String sql = "SELECT codigo_seguimiento FROM paquete " +
                        "WHERE codigo_seguimiento LIKE ? " +
                        "ORDER BY codigo_seguimiento DESC LIMIT 1";
            
            try (PreparedStatement ps = con.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {
                
                ps.setString(1, prefijo + "%");
                int siguienteNumero = 1;
                
                if (rs.next()) {
                    String ultimoCodigo = rs.getString("codigo_seguimiento");
                    String numeroStr = ultimoCodigo.substring(prefijo.length());
                    try {
                        siguienteNumero = Integer.parseInt(numeroStr) + 1;
                    } catch (NumberFormatException e) {
                        siguienteNumero = 1;
                    }
                }
                
                return prefijo + String.format("%04d", siguienteNumero);
            }
            
        } catch (SQLException ex) {
            System.out.println("PaqueteDAO.generarCodigoSeguimiento - Error: " + ex.getMessage());
            ex.printStackTrace();
            return prefijo + String.format("%04d", 1);
        }
    }

    public void agregar(Paquete p) {
        String sql = "INSERT INTO paquete (codigo_seguimiento, descripcion, peso_kg, largo_cm, ancho_cm, alto_cm, "
                   + "direccion_origen, direccion_destino, remitente_id, destinatario_id, estado_id) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, (SELECT id FROM estado_paquete WHERE nombre = ?))";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, p.getCodigoSeguimiento());
            ps.setString(2, p.getDescripcion());
            ps.setDouble(3, p.getPesoKg());
            ps.setDouble(4, p.getLargoCm());
            ps.setDouble(5, p.getAnchoCm());
            ps.setDouble(6, p.getAltoCm());
            ps.setString(7, p.getDireccionOrigen());
            ps.setString(8, p.getDireccionDestino());
            if (p.getRemitenteId() != null) {
                ps.setInt(9, p.getRemitenteId());
            } else {
                ps.setNull(9, java.sql.Types.INTEGER);
            }
            if (p.getDestinatarioId() != null) {
                ps.setInt(10, p.getDestinatarioId());
            } else {
                ps.setNull(10, java.sql.Types.INTEGER);
            }
            ps.setString(11, p.getEstado());

            int filas = ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int idGenerado = rs.getInt(1);
                    System.out.println("Paquete insertado con id = " + idGenerado);
                }
            }

            System.out.println("Paquete agregado. Filas afectadas: " + filas);

        } catch (SQLException ex) {
            System.out.println("PaqueteDAO.agregar - Error SQL: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public List<Paquete> listar() {
        List<Paquete> lista = new ArrayList<>();

        String sql = "SELECT p.id, p.codigo_seguimiento, p.descripcion, p.peso_kg, p.largo_cm, p.ancho_cm, p.alto_cm, "
                   + "p.direccion_origen, p.direccion_destino, p.remitente_id, p.destinatario_id, "
                   + "ep.nombre as estado, "
                   + "cr.nombre_completo as remitente_nombre, "
                   + "cd.nombre_completo as destinatario_nombre "
                   + "FROM paquete p "
                   + "LEFT JOIN estado_paquete ep ON p.estado_id = ep.id "
                   + "LEFT JOIN cliente cr ON p.remitente_id = cr.id "
                   + "LEFT JOIN cliente cd ON p.destinatario_id = cd.id";

        try (Connection con = ConexionBD.conectar();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String codigo = rs.getString("codigo_seguimiento");
                String descripcion = rs.getString("descripcion");
                double peso = rs.getDouble("peso_kg");
                double largo = rs.getDouble("largo_cm");
                double ancho = rs.getDouble("ancho_cm");
                double alto = rs.getDouble("alto_cm");
                String dirOrigen = rs.getString("direccion_origen");
                String dirDestino = rs.getString("direccion_destino");
                Integer remitenteId = rs.getObject("remitente_id", Integer.class);
                Integer destinatarioId = rs.getObject("destinatario_id", Integer.class);
                String estado = rs.getString("estado");

                Paquete p = new Paquete(id, codigo, descripcion, peso, largo, ancho, alto, dirOrigen, dirDestino,
                                       remitenteId, destinatarioId, estado);
                String remitenteNombre = rs.getString("remitente_nombre");
                String destinatarioNombre = rs.getString("destinatario_nombre");
                if (remitenteNombre != null) {
                    p.setRemitenteNombre(remitenteNombre);
                }
                if (destinatarioNombre != null) {
                    p.setDestinatarioNombre(destinatarioNombre);
                }
                lista.add(p);
            }

        } catch (SQLException ex) {
            System.out.println("PaqueteDAO.listar - Error SQL: " + ex.getMessage());
            ex.printStackTrace();
        }

        return lista;
    }

    public void eliminar(int id) {
        String sql = "DELETE FROM paquete WHERE id = ?";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            int filas = ps.executeUpdate();
            System.out.println("Paquete eliminado. Filas afectadas: " + filas);

        } catch (SQLException ex) {
            System.out.println("PaqueteDAO.eliminar - Error SQL: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public Paquete buscarPorId(int id) {
        String sql = "SELECT p.id, p.codigo_seguimiento, p.descripcion, p.peso_kg, p.largo_cm, p.ancho_cm, p.alto_cm, "
                   + "p.direccion_origen, p.direccion_destino, p.remitente_id, p.destinatario_id, "
                   + "ep.nombre as estado, "
                   + "cr.nombre_completo as remitente_nombre, "
                   + "cd.nombre_completo as destinatario_nombre "
                   + "FROM paquete p "
                   + "LEFT JOIN estado_paquete ep ON p.estado_id = ep.id "
                   + "LEFT JOIN cliente cr ON p.remitente_id = cr.id "
                   + "LEFT JOIN cliente cd ON p.destinatario_id = cd.id "
                   + "WHERE p.id = ?";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String codigo = rs.getString("codigo_seguimiento");
                    String descripcion = rs.getString("descripcion");
                    double peso = rs.getDouble("peso_kg");
                    double largo = rs.getDouble("largo_cm");
                    double ancho = rs.getDouble("ancho_cm");
                    double alto = rs.getDouble("alto_cm");
                    String dirOrigen = rs.getString("direccion_origen");
                    String dirDestino = rs.getString("direccion_destino");
                    Integer remitenteId = rs.getObject("remitente_id", Integer.class);
                    Integer destinatarioId = rs.getObject("destinatario_id", Integer.class);
                    String estado = rs.getString("estado");

                    Paquete p = new Paquete(id, codigo, descripcion, peso, largo, ancho, alto, dirOrigen, dirDestino,
                                           remitenteId, destinatarioId, estado);
                    String remitenteNombre = rs.getString("remitente_nombre");
                    String destinatarioNombre = rs.getString("destinatario_nombre");
                    if (remitenteNombre != null) {
                        p.setRemitenteNombre(remitenteNombre);
                    }
                    if (destinatarioNombre != null) {
                        p.setDestinatarioNombre(destinatarioNombre);
                    }
                    return p;
                }
            }

        } catch (SQLException ex) {
            System.out.println("PaqueteDAO.buscarPorId - Error SQL: " + ex.getMessage());
            ex.printStackTrace();
        }

        return null;
    }

    public void actualizar(Paquete p) {
        String sql = "UPDATE paquete SET codigo_seguimiento = ?, descripcion = ?, peso_kg = ?, "
                   + "largo_cm = ?, ancho_cm = ?, alto_cm = ?, direccion_origen = ?, direccion_destino = ?, "
                   + "remitente_id = ?, destinatario_id = ?, estado_id = (SELECT id FROM estado_paquete WHERE nombre = ?) "
                   + "WHERE id = ?";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, p.getCodigoSeguimiento());
            ps.setString(2, p.getDescripcion());
            ps.setDouble(3, p.getPesoKg());
            ps.setDouble(4, p.getLargoCm());
            ps.setDouble(5, p.getAnchoCm());
            ps.setDouble(6, p.getAltoCm());
            ps.setString(7, p.getDireccionOrigen());
            ps.setString(8, p.getDireccionDestino());
            if (p.getRemitenteId() != null) {
                ps.setInt(9, p.getRemitenteId());
            } else {
                ps.setNull(9, java.sql.Types.INTEGER);
            }
            if (p.getDestinatarioId() != null) {
                ps.setInt(10, p.getDestinatarioId());
            } else {
                ps.setNull(10, java.sql.Types.INTEGER);
            }
            ps.setString(11, p.getEstado());
            ps.setInt(12, p.getId());

            int filas = ps.executeUpdate();
            System.out.println("Paquete actualizado. Filas afectadas: " + filas);

        } catch (SQLException ex) {
            System.out.println("PaqueteDAO.actualizar - Error SQL: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public void cambiarEstado(int id, String nuevoEstado) {
        String sql = "UPDATE paquete SET estado_id = (SELECT id FROM estado_paquete WHERE nombre = ?) WHERE id = ?";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nuevoEstado);
            ps.setInt(2, id);

            int filas = ps.executeUpdate();
            System.out.println("Estado de paquete actualizado. Filas afectadas: " + filas);

        } catch (SQLException ex) {
            System.out.println("PaqueteDAO.cambiarEstado - Error SQL: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public Paquete buscarPorCodigoSeguimiento(String codigoSeguimiento) {
        String sql = "SELECT p.id, p.codigo_seguimiento, p.descripcion, p.peso_kg, p.largo_cm, p.ancho_cm, p.alto_cm, "
                   + "p.direccion_origen, p.direccion_destino, p.remitente_id, p.destinatario_id, "
                   + "ep.nombre as estado, "
                   + "cr.nombre_completo as remitente_nombre, "
                   + "cd.nombre_completo as destinatario_nombre "
                   + "FROM paquete p "
                   + "LEFT JOIN estado_paquete ep ON p.estado_id = ep.id "
                   + "LEFT JOIN cliente cr ON p.remitente_id = cr.id "
                   + "LEFT JOIN cliente cd ON p.destinatario_id = cd.id "
                   + "WHERE p.codigo_seguimiento = ?";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, codigoSeguimiento);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id");
                    String codigo = rs.getString("codigo_seguimiento");
                    String descripcion = rs.getString("descripcion");
                    double peso = rs.getDouble("peso_kg");
                    double largo = rs.getDouble("largo_cm");
                    double ancho = rs.getDouble("ancho_cm");
                    double alto = rs.getDouble("alto_cm");
                    String dirOrigen = rs.getString("direccion_origen");
                    String dirDestino = rs.getString("direccion_destino");
                    Integer remitenteId = rs.getObject("remitente_id", Integer.class);
                    Integer destinatarioId = rs.getObject("destinatario_id", Integer.class);
                    String estado = rs.getString("estado");

                    Paquete p = new Paquete(id, codigo, descripcion, peso, largo, ancho, alto, dirOrigen, dirDestino,
                                           remitenteId, destinatarioId, estado);
                    String remitenteNombre = rs.getString("remitente_nombre");
                    String destinatarioNombre = rs.getString("destinatario_nombre");
                    if (remitenteNombre != null) {
                        p.setRemitenteNombre(remitenteNombre);
                    }
                    if (destinatarioNombre != null) {
                        p.setDestinatarioNombre(destinatarioNombre);
                    }
                    return p;
                }
            }

        } catch (SQLException ex) {
            System.out.println("PaqueteDAO.buscarPorCodigoSeguimiento - Error SQL: " + ex.getMessage());
            ex.printStackTrace();
        }

        return null;
    }

    public List<Paquete> listarPorEstado(String estado) {
        List<Paquete> lista = new ArrayList<>();

        String sql = "SELECT p.id, p.codigo_seguimiento, p.descripcion, p.peso_kg, p.largo_cm, p.ancho_cm, p.alto_cm, "
                   + "p.direccion_origen, p.direccion_destino, p.remitente_id, p.destinatario_id, "
                   + "ep.nombre as estado, "
                   + "cr.nombre_completo as remitente_nombre, "
                   + "cd.nombre_completo as destinatario_nombre "
                   + "FROM paquete p "
                   + "LEFT JOIN estado_paquete ep ON p.estado_id = ep.id "
                   + "LEFT JOIN cliente cr ON p.remitente_id = cr.id "
                   + "LEFT JOIN cliente cd ON p.destinatario_id = cd.id "
                   + "WHERE ep.nombre = ?";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, estado);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String codigo = rs.getString("codigo_seguimiento");
                    String descripcion = rs.getString("descripcion");
                    double peso = rs.getDouble("peso_kg");
                    double largo = rs.getDouble("largo_cm");
                    double ancho = rs.getDouble("ancho_cm");
                    double alto = rs.getDouble("alto_cm");
                    String dirOrigen = rs.getString("direccion_origen");
                    String dirDestino = rs.getString("direccion_destino");
                    Integer remitenteId = rs.getObject("remitente_id", Integer.class);
                    Integer destinatarioId = rs.getObject("destinatario_id", Integer.class);
                    String estadoResult = rs.getString("estado");

                    Paquete p = new Paquete(id, codigo, descripcion, peso, largo, ancho, alto, dirOrigen, dirDestino,
                                           remitenteId, destinatarioId, estadoResult);
                    String remitenteNombre = rs.getString("remitente_nombre");
                    String destinatarioNombre = rs.getString("destinatario_nombre");
                    if (remitenteNombre != null) {
                        p.setRemitenteNombre(remitenteNombre);
                    }
                    if (destinatarioNombre != null) {
                        p.setDestinatarioNombre(destinatarioNombre);
                    }
                    lista.add(p);
                }
            }

        } catch (SQLException ex) {
            System.out.println("PaqueteDAO.listarPorEstado - Error SQL: " + ex.getMessage());
            ex.printStackTrace();
        }

        return lista;
    }

    public List<Paquete> buscarPorRemitente(int remitenteId) {
        List<Paquete> lista = new ArrayList<>();

        String sql = "SELECT p.id, p.codigo_seguimiento, p.descripcion, p.peso_kg, p.largo_cm, p.ancho_cm, p.alto_cm, "
                   + "p.direccion_origen, p.direccion_destino, p.remitente_id, p.destinatario_id, "
                   + "ep.nombre as estado, "
                   + "cr.nombre_completo as remitente_nombre, "
                   + "cd.nombre_completo as destinatario_nombre "
                   + "FROM paquete p "
                   + "LEFT JOIN estado_paquete ep ON p.estado_id = ep.id "
                   + "LEFT JOIN cliente cr ON p.remitente_id = cr.id "
                   + "LEFT JOIN cliente cd ON p.destinatario_id = cd.id "
                   + "WHERE p.remitente_id = ?";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, remitenteId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String codigo = rs.getString("codigo_seguimiento");
                    String descripcion = rs.getString("descripcion");
                    double peso = rs.getDouble("peso_kg");
                    double largo = rs.getDouble("largo_cm");
                    double ancho = rs.getDouble("ancho_cm");
                    double alto = rs.getDouble("alto_cm");
                    String dirOrigen = rs.getString("direccion_origen");
                    String dirDestino = rs.getString("direccion_destino");
                    Integer remitenteIdResult = rs.getObject("remitente_id", Integer.class);
                    Integer destinatarioId = rs.getObject("destinatario_id", Integer.class);
                    String estado = rs.getString("estado");

                    Paquete p = new Paquete(id, codigo, descripcion, peso, largo, ancho, alto, dirOrigen, dirDestino,
                                           remitenteIdResult, destinatarioId, estado);
                    String remitenteNombre = rs.getString("remitente_nombre");
                    String destinatarioNombre = rs.getString("destinatario_nombre");
                    if (remitenteNombre != null) {
                        p.setRemitenteNombre(remitenteNombre);
                    }
                    if (destinatarioNombre != null) {
                        p.setDestinatarioNombre(destinatarioNombre);
                    }
                    lista.add(p);
                }
            }

        } catch (SQLException ex) {
            System.out.println("PaqueteDAO.buscarPorRemitente - Error SQL: " + ex.getMessage());
            ex.printStackTrace();
        }

        return lista;
    }

    public List<Paquete> buscarPorDestinatario(int destinatarioId) {
        List<Paquete> lista = new ArrayList<>();

        String sql = "SELECT p.id, p.codigo_seguimiento, p.descripcion, p.peso_kg, p.largo_cm, p.ancho_cm, p.alto_cm, "
                   + "p.direccion_origen, p.direccion_destino, p.remitente_id, p.destinatario_id, "
                   + "ep.nombre as estado, "
                   + "cr.nombre_completo as remitente_nombre, "
                   + "cd.nombre_completo as destinatario_nombre "
                   + "FROM paquete p "
                   + "LEFT JOIN estado_paquete ep ON p.estado_id = ep.id "
                   + "LEFT JOIN cliente cr ON p.remitente_id = cr.id "
                   + "LEFT JOIN cliente cd ON p.destinatario_id = cd.id "
                   + "WHERE p.destinatario_id = ?";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, destinatarioId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String codigo = rs.getString("codigo_seguimiento");
                    String descripcion = rs.getString("descripcion");
                    double peso = rs.getDouble("peso_kg");
                    double largo = rs.getDouble("largo_cm");
                    double ancho = rs.getDouble("ancho_cm");
                    double alto = rs.getDouble("alto_cm");
                    String dirOrigen = rs.getString("direccion_origen");
                    String dirDestino = rs.getString("direccion_destino");
                    Integer remitenteId = rs.getObject("remitente_id", Integer.class);
                    Integer destinatarioIdResult = rs.getObject("destinatario_id", Integer.class);
                    String estado = rs.getString("estado");

                    Paquete p = new Paquete(id, codigo, descripcion, peso, largo, ancho, alto, dirOrigen, dirDestino,
                                           remitenteId, destinatarioIdResult, estado);
                    String remitenteNombre = rs.getString("remitente_nombre");
                    String destinatarioNombre = rs.getString("destinatario_nombre");
                    if (remitenteNombre != null) {
                        p.setRemitenteNombre(remitenteNombre);
                    }
                    if (destinatarioNombre != null) {
                        p.setDestinatarioNombre(destinatarioNombre);
                    }
                    lista.add(p);
                }
            }

        } catch (SQLException ex) {
            System.out.println("PaqueteDAO.buscarPorDestinatario - Error SQL: " + ex.getMessage());
            ex.printStackTrace();
        }

        return lista;
    }
}

