package com.rapidexpress.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {
    private static final String URL = "jdbc:mysql://localhost:3306/proyecto_java?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String USER = "root";
    private static final String PASS = "99110202608.";

    public static Connection conectar() {
        try {
            Connection con = DriverManager.getConnection(URL, USER, PASS);
            if (con == null) {
                System.out.println("ConexionBD - Error: La conexion retorno null");
            }
            return con;
        } catch (SQLException e) {
            System.out.println("ConexionBD - Error al conectar a la BD: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
