package com.rapidexpress.dao;

import com.rapidexpress.model.entity.RutaPaquete;
import com.rapidexpress.utils.ConexionBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class RutaPaqueteDAO {

    public void agregar(RutaPaquete rp) {
        String sql = "INSERT INTO ruta_paquete (hoja_ruta_id, paquete_id, orden_entrega) "
                   + "VALUES (?, ?, ?)";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, rp.getHojaRutaId());
            ps.setInt(2, rp.getPaqueteId());
            if (rp.getOrdenEntrega() != null) {
                ps.setInt(3, rp.getOrdenEntrega());
            } else {
                ps.setNull(3, java.sql.Types.INTEGER);
            }

            int filas = ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int idGenerado = rs.getInt(1);
                    System.out.println("RutaPaquete insertado con id = " + idGenerado);
                }
            }

            System.out.println("RutaPaquete agregado. Filas afectadas: " + filas);

        } catch (SQLException ex) {
            System.out.println("RutaPaqueteDAO.agregar - Error SQL: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public List<RutaPaquete> listar() {
        List<RutaPaquete> lista = new ArrayList<>();

        String sql = "SELECT id, hoja_ruta_id, paquete_id, orden_entrega FROM ruta_paquete";

        try (Connection con = ConexionBD.conectar();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                int hojaRutaId = rs.getInt("hoja_ruta_id");
                int paqueteId = rs.getInt("paquete_id");
                Integer ordenEntrega = rs.getObject("orden_entrega", Integer.class);

                RutaPaquete rp = new RutaPaquete(id, hojaRutaId, paqueteId, ordenEntrega);
                lista.add(rp);
            }

        } catch (SQLException ex) {
            System.out.println("RutaPaqueteDAO.listar - Error SQL: " + ex.getMessage());
            ex.printStackTrace();
        }

        return lista;
    }

    public void eliminar(int id) {
        String sql = "DELETE FROM ruta_paquete WHERE id = ?";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            int filas = ps.executeUpdate();
            System.out.println("RutaPaquete eliminado. Filas afectadas: " + filas);

        } catch (SQLException ex) {
            System.out.println("RutaPaqueteDAO.eliminar - Error SQL: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public RutaPaquete buscarPorId(int id) {
        String sql = "SELECT id, hoja_ruta_id, paquete_id, orden_entrega FROM ruta_paquete WHERE id = ?";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int hojaRutaId = rs.getInt("hoja_ruta_id");
                    int paqueteId = rs.getInt("paquete_id");
                    Integer ordenEntrega = rs.getObject("orden_entrega", Integer.class);

                    return new RutaPaquete(id, hojaRutaId, paqueteId, ordenEntrega);
                }
            }

        } catch (SQLException ex) {
            System.out.println("RutaPaqueteDAO.buscarPorId - Error SQL: " + ex.getMessage());
            ex.printStackTrace();
        }

        return null;
    }
}

