package com.rapidexpress.model.entity;

public class Usuario {
    
    private int id;
    private String username;
    private String password;
    private String nombreCompleto;
    private int rolId;
    private String rolNombre;
    private Integer conductorId;
    private boolean activo;
    
    public Usuario(String username, String password, String nombreCompleto, int rolId, Integer conductorId, boolean activo) {
        this.username = username;
        this.password = password;
        this.nombreCompleto = nombreCompleto;
        this.rolId = rolId;
        this.conductorId = conductorId;
        this.activo = activo;
    }
    
    public Usuario(int id, String username, String password, String nombreCompleto, int rolId, Integer conductorId, boolean activo) {
        this(username, password, nombreCompleto, rolId, conductorId, activo);
        this.id = id;
    }
    
    public int getId() {
        return id;
    }
    
    public String getUsername() {
        return username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public String getNombreCompleto() {
        return nombreCompleto;
    }
    
    public int getRolId() {
        return rolId;
    }
    
    public String getRolNombre() {
        return rolNombre;
    }
    
    public void setRolNombre(String rolNombre) {
        this.rolNombre = rolNombre;
    }
    
    public Integer getConductorId() {
        return conductorId;
    }
    
    public boolean isActivo() {
        return activo;
    }
    
    public boolean esAdministrador() {
        return "ADMINISTRADOR".equals(rolNombre);
    }
    
    public boolean esOperadorLogistica() {
        return "OPERADOR_LOGISTICA".equals(rolNombre);
    }
    
    public boolean esConductor() {
        return "CONDUCTOR".equals(rolNombre);
    }
    
    public boolean esAuditor() {
        return "AUDITOR".equals(rolNombre);
    }
    
    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", nombreCompleto='" + nombreCompleto + '\'' +
                ", rolNombre='" + rolNombre + '\'' +
                ", activo=" + activo +
                '}';
    }
}

