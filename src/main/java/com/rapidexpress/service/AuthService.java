package com.rapidexpress.service;

import com.rapidexpress.dao.UsuarioDAO;
import com.rapidexpress.model.entity.Usuario;

public class AuthService {
    
    private UsuarioDAO usuarioDAO;
    private Usuario usuarioActual;
    
    public AuthService() {
        this.usuarioDAO = new UsuarioDAO();
        this.usuarioActual = null;
    }
    
    public boolean login(String username, String password) {
        Usuario usuario = usuarioDAO.buscarPorUsername(username);
        
        if (usuario == null) {
            return false;
        }
        
        String passwordHash = UsuarioDAO.hashPassword(password);
        
        if (usuario.getPassword().equals(passwordHash)) {
            this.usuarioActual = usuario;
            return true;
        }
        
        return false;
    }
    
    public void logout() {
        this.usuarioActual = null;
    }
    
    public Usuario getUsuarioActual() {
        return usuarioActual;
    }
    
    public boolean estaAutenticado() {
        return usuarioActual != null;
    }
    
    public boolean tienePermiso(String permiso) {
        if (!estaAutenticado()) {
            return false;
        }
        
        switch (permiso) {
            case "GESTION_VEHICULOS":
            case "GESTION_CONDUCTORES":
            case "GESTION_CLIENTES":
            case "GESTION_MANTENIMIENTOS":
                return usuarioActual.esAdministrador();
                
            case "GESTION_PAQUETES":
            case "CREAR_RUTAS":
            case "GESTION_RUTAS":
                return usuarioActual.esAdministrador() || usuarioActual.esOperadorLogistica();
                
            case "VER_MIS_RUTAS":
            case "ACTUALIZAR_ESTADO_PAQUETES":
                return usuarioActual.esAdministrador() || usuarioActual.esConductor();
                
            case "VER_REPORTES":
            case "VER_AUDITORIA":
                return usuarioActual.esAdministrador() || usuarioActual.esAuditor();
                
            default:
                return false;
        }
    }
    
    public boolean puedeAccederAModulo(String modulo) {
        if (!estaAutenticado()) {
            return false;
        }
        
        switch (modulo) {
            case "VEHICULOS":
                return tienePermiso("GESTION_VEHICULOS");
            case "CONDUCTORES":
                return tienePermiso("GESTION_CONDUCTORES");
            case "PAQUETES":
                return tienePermiso("GESTION_PAQUETES");
            case "CLIENTES":
                return tienePermiso("GESTION_CLIENTES");
            case "RUTAS":
                return tienePermiso("GESTION_RUTAS") || tienePermiso("VER_MIS_RUTAS");
            case "MANTENIMIENTOS":
                return tienePermiso("GESTION_MANTENIMIENTOS");
            case "REPORTES":
                return tienePermiso("VER_REPORTES");
            case "AUDITORIA":
                return tienePermiso("VER_AUDITORIA");
            default:
                return false;
        }
    }
}

