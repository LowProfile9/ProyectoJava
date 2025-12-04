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
                System.out.println("Error: No se pudo establecer conexion con la base de datos");
                return null;
            }
            return con;
        } catch (SQLException e) {
            String mensaje = "Error de conexion a la base de datos";
            if (e.getMessage().contains("Access denied")) {
                mensaje = "Error: Credenciales de base de datos incorrectas";
            } else if (e.getMessage().contains("Unknown database")) {
                mensaje = "Error: La base de datos no existe. Ejecute los scripts SQL primero";
            } else if (e.getMessage().contains("Communications link failure")) {
                mensaje = "Error: No se puede conectar al servidor MySQL. Verifique que este en ejecucion";
            }
            System.out.println(mensaje);
            return null;
        }
    }
}
