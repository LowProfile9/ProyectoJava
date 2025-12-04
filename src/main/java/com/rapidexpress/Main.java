/**
 * Main application class for RapidExpress Logistics System
 * Provides console-based user interface for managing logistics operations
 */
package com.rapidexpress;

import com.rapidexpress.dao.*;
import com.rapidexpress.model.entity.*;
import com.rapidexpress.service.AuditoriaService;
import com.rapidexpress.service.RutaService;
import com.rapidexpress.service.ReporteService;
import com.rapidexpress.service.AuthService;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static Scanner scanner = new Scanner(System.in);
    private static ClienteDAO clienteDAO = new ClienteDAO();
    private static ConductorDAO conductorDAO = new ConductorDAO();
    private static VehiculoDAO vehiculoDAO = new VehiculoDAO();
    private static PaqueteDAO paqueteDAO = new PaqueteDAO();
    private static HojaRutaDAO hojaRutaDAO = new HojaRutaDAO();
    private static RutaPaqueteDAO rutaPaqueteDAO = new RutaPaqueteDAO();
    private static MantenimientoDAO mantenimientoDAO = new MantenimientoDAO();
    private static RutaService rutaService = new RutaService();
    private static ReporteService reporteService = new ReporteService();
    private static AuditoriaService auditoriaService = new AuditoriaService();
    private static AuthService authService = new AuthService();

    /**
     * Main entry point of the application
     * Initializes directories and starts login process
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        crearDirectorios();
        if (login()) {
            Usuario usuario = authService.getUsuarioActual();
            auditoriaService.setUsuarioActual(usuario.getUsername());
            mostrarMenuPrincipal();
        } else {
            System.out.println("Acceso denegado. Saliendo del sistema...");
        }
    }
    
    /**
     * Handles user login with maximum 3 attempts
     * @return true if login successful, false otherwise
     */
    private static boolean login() {
        System.out.println("\n========================================");
        System.out.println("   SISTEMA DE LOGISTICA Y ENTREGAS");
        System.out.println("   RAPIDEXPRESS - INICIO DE SESION");
        System.out.println("========================================");
        
        int intentos = 0;
        int maxIntentos = 3;
        
        while (intentos < maxIntentos) {
            System.out.print("Usuario: ");
            String username = scanner.nextLine();
            System.out.print("Contrasena: ");
            String password = scanner.nextLine();
            
            if (authService.login(username, password)) {
                Usuario usuario = authService.getUsuarioActual();
                System.out.println("\nBienvenido, " + usuario.getNombreCompleto() + "!");
                System.out.println("Rol: " + usuario.getRolNombre());
                return true;
            } else {
                intentos++;
                System.out.println("Usuario o contrasena incorrectos. Intentos restantes: " + (maxIntentos - intentos));
            }
        }
        
        return false;
    }

    private static void crearDirectorios() {
        java.io.File dirReportes = new java.io.File("reportes");
        java.io.File dirLogs = new java.io.File("logs");
        if (!dirReportes.exists()) dirReportes.mkdirs();
        if (!dirLogs.exists()) dirLogs.mkdirs();
    }

    private static void mostrarMenuPrincipal() {
        int opcion;
        do {
            Usuario usuario = authService.getUsuarioActual();
            System.out.println("\n========================================");
            System.out.println("   SISTEMA DE LOGISTICA Y ENTREGAS");
            System.out.println("   Usuario: " + usuario.getNombreCompleto() + " (" + usuario.getRolNombre() + ")");
            System.out.println("========================================");
            System.out.println("1. Gestion de Vehiculos" + (authService.puedeAccederAModulo("VEHICULOS") ? "" : " [Sin acceso]"));
            System.out.println("2. Gestion de Conductores" + (authService.puedeAccederAModulo("CONDUCTORES") ? "" : " [Sin acceso]"));
            System.out.println("3. Gestion de Paquetes" + (authService.puedeAccederAModulo("PAQUETES") ? "" : " [Sin acceso]"));
            System.out.println("4. Gestion de Clientes" + (authService.puedeAccederAModulo("CLIENTES") ? "" : " [Sin acceso]"));
            System.out.println("5. Planificacion y Seguimiento de Rutas" + (authService.puedeAccederAModulo("RUTAS") ? "" : " [Sin acceso]"));
            System.out.println("6. Mantenimientos" + (authService.puedeAccederAModulo("MANTENIMIENTOS") ? "" : " [Sin acceso]"));
            System.out.println("7. Reportes" + (authService.puedeAccederAModulo("REPORTES") ? "" : " [Sin acceso]"));
            System.out.println("8. Auditoria" + (authService.puedeAccederAModulo("AUDITORIA") ? "" : " [Sin acceso]"));
            System.out.println("0. Salir");
            System.out.println("========================================");
            System.out.print("Seleccione una opcion: ");

            opcion = leerEntero();

            switch (opcion) {
                case 1 -> {
                    if (authService.puedeAccederAModulo("VEHICULOS")) {
                        menuVehiculos();
                    } else {
                        System.out.println("No tiene permisos para acceder a este modulo");
                    }
                }
                case 2 -> {
                    if (authService.puedeAccederAModulo("CONDUCTORES")) {
                        menuConductores();
                    } else {
                        System.out.println("No tiene permisos para acceder a este modulo");
                    }
                }
                case 3 -> {
                    if (authService.puedeAccederAModulo("PAQUETES")) {
                        menuPaquetes();
                    } else {
                        System.out.println("No tiene permisos para acceder a este modulo");
                    }
                }
                case 4 -> {
                    if (authService.puedeAccederAModulo("CLIENTES")) {
                        menuClientes();
                    } else {
                        System.out.println("No tiene permisos para acceder a este modulo");
                    }
                }
                case 5 -> {
                    if (authService.puedeAccederAModulo("RUTAS")) {
                        menuRutas();
                    } else {
                        System.out.println("No tiene permisos para acceder a este modulo");
                    }
                }
                case 6 -> {
                    if (authService.puedeAccederAModulo("MANTENIMIENTOS")) {
                        menuMantenimientos();
                    } else {
                        System.out.println("No tiene permisos para acceder a este modulo");
                    }
                }
                case 7 -> {
                    if (authService.puedeAccederAModulo("REPORTES")) {
                        menuReportes();
                    } else {
                        System.out.println("No tiene permisos para acceder a este modulo");
                    }
                }
                case 8 -> {
                    if (authService.puedeAccederAModulo("AUDITORIA")) {
                        menuAuditoria();
                    } else {
                        System.out.println("No tiene permisos para acceder a este modulo");
                    }
                }
                case 0 -> {
                    System.out.println("Cerrando sesion...");
                    authService.logout();
                }
                default -> System.out.println("Opcion no valida");
            }
        } while (opcion != 0);
    }

    /**
     * Displays and handles vehicle management menu options
     */
    private static void menuVehiculos() {
        int opcion;
        do {
            System.out.println("\n--- GESTION DE VEHICULOS ---");
            if (authService.tienePermiso("GESTION_VEHICULOS")) {
                System.out.println("1. Registrar vehiculo");
                System.out.println("4. Actualizar vehiculo");
                System.out.println("5. Eliminar vehiculo");
            }
            System.out.println("2. Listar vehiculos");
            System.out.println("3. Buscar vehiculo por ID");
            System.out.println("6. Ver historial de mantenimientos");
            System.out.println("0. Volver al menu principal");
            System.out.print("Seleccione una opcion: ");

            opcion = leerEntero();

            switch (opcion) {
                case 1 -> {
                    if (authService.tienePermiso("GESTION_VEHICULOS")) {
                        registrarVehiculo();
                    } else {
                        System.out.println("No tiene permisos para esta operacion");
                    }
                }
                case 2 -> listarVehiculos();
                case 3 -> buscarVehiculoPorId();
                case 4 -> {
                    if (authService.tienePermiso("GESTION_VEHICULOS")) {
                        actualizarVehiculo();
                    } else {
                        System.out.println("No tiene permisos para esta operacion");
                    }
                }
                case 5 -> {
                    if (authService.tienePermiso("GESTION_VEHICULOS")) {
                        eliminarVehiculo();
                    } else {
                        System.out.println("No tiene permisos para esta operacion");
                    }
                }
                case 6 -> verMantenimientosVehiculo();
            }
        } while (opcion != 0);
    }

    private static void registrarVehiculo() {
        System.out.println("\n--- REGISTRAR VEHICULO ---");
        System.out.print("Placa: ");
        String placa = scanner.nextLine();
        System.out.print("Marca: ");
        String marca = scanner.nextLine();
        System.out.print("Modelo: ");
        String modelo = scanner.nextLine();
        System.out.print("Ano de fabricacion: ");
        int anio = leerEntero();
        System.out.print("Capacidad maxima (kg): ");
        double capacidad = leerDouble();
        System.out.print("Estado (DISPONIBLE/EN_RUTA/EN_MANTENIMIENTO): ");
        String estado = scanner.nextLine().toUpperCase();

        Vehiculo v = new Vehiculo(placa, marca, modelo, anio, capacidad, estado);
        vehiculoDAO.agregar(v);
        auditoriaService.registrarOperacion("vehiculo", "INSERT", 0, "Vehiculo registrado: " + placa);
    }

    private static void listarVehiculos() {
        System.out.println("\n--- LISTA DE VEHICULOS ---");
        List<Vehiculo> vehiculos = vehiculoDAO.listar();
        if (vehiculos.isEmpty()) {
            System.out.println("No hay vehiculos registrados");
        } else {
            for (Vehiculo v : vehiculos) {
                System.out.println(v);
            }
        }
    }

    private static void buscarVehiculoPorId() {
        System.out.print("\nIngrese ID del vehiculo: ");
        int id = leerEntero();
        Vehiculo v = vehiculoDAO.buscarPorId(id);
        if (v != null) {
            System.out.println(v);
        } else {
            System.out.println("Vehiculo no encontrado");
        }
    }

    private static void actualizarVehiculo() {
        System.out.print("\nIngrese ID del vehiculo a actualizar: ");
        int id = leerEntero();
        Vehiculo v = vehiculoDAO.buscarPorId(id);
        if (v == null) {
            System.out.println("Vehiculo no encontrado");
            return;
        }

        System.out.println("Vehiculo actual: " + v);
        System.out.print("Nueva placa (Enter para mantener): ");
        String placa = scanner.nextLine();
        if (!placa.isEmpty()) {
            v = new Vehiculo(v.getId(), placa, v.getMarca(), v.getModelo(), 
                           v.getAnioFabricacion(), v.getCapacidadMaxKg(), v.getEstado(), v.getConductorId());
        }
        System.out.print("Nueva marca (Enter para mantener): ");
        String marca = scanner.nextLine();
        if (!marca.isEmpty()) {
            v = new Vehiculo(v.getId(), v.getPlaca(), marca, v.getModelo(), 
                           v.getAnioFabricacion(), v.getCapacidadMaxKg(), v.getEstado(), v.getConductorId());
        }
        System.out.print("Nuevo estado (Enter para mantener): ");
        String estado = scanner.nextLine();
        if (!estado.isEmpty()) {
            v = new Vehiculo(v.getId(), v.getPlaca(), v.getMarca(), v.getModelo(), 
                           v.getAnioFabricacion(), v.getCapacidadMaxKg(), estado.toUpperCase(), v.getConductorId());
        }

        vehiculoDAO.actualizar(v);
        auditoriaService.registrarOperacion("vehiculo", "UPDATE", id, "Vehiculo actualizado: " + v.getPlaca());
    }

    private static void eliminarVehiculo() {
        System.out.print("\nIngrese ID del vehiculo a eliminar: ");
        int id = leerEntero();
        Vehiculo v = vehiculoDAO.buscarPorId(id);
        if (v != null) {
            vehiculoDAO.eliminar(id);
            auditoriaService.registrarOperacion("vehiculo", "DELETE", id, "Vehiculo eliminado: " + v.getPlaca());
        } else {
            System.out.println("Vehiculo no encontrado");
        }
    }

    private static void verMantenimientosVehiculo() {
        System.out.print("\nIngrese ID del vehiculo: ");
        int id = leerEntero();
        List<Mantenimiento> mantenimientos = mantenimientoDAO.listarPorVehiculo(id);
        if (mantenimientos.isEmpty()) {
            System.out.println("No hay mantenimientos registrados para este vehiculo");
        } else {
            System.out.println("\n--- HISTORIAL DE MANTENIMIENTOS ---");
            for (Mantenimiento m : mantenimientos) {
                System.out.println(m);
            }
        }
    }

    /**
     * Displays and handles driver management menu
     */
    private static void menuConductores() {
        int opcion;
        do {
            System.out.println("\n--- GESTION DE CONDUCTORES ---");
            if (authService.tienePermiso("GESTION_CONDUCTORES")) {
                System.out.println("1. Registrar conductor");
                System.out.println("4. Actualizar conductor");
                System.out.println("5. Eliminar conductor");
            }
            System.out.println("2. Listar conductores");
            System.out.println("3. Buscar conductor por ID");
            System.out.println("6. Listar por estado");
            System.out.println("0. Volver al menu principal");
            System.out.print("Seleccione una opcion: ");

            opcion = leerEntero();

            switch (opcion) {
                case 1 -> {
                    if (authService.tienePermiso("GESTION_CONDUCTORES")) {
                        registrarConductor();
                    } else {
                        System.out.println("No tiene permisos para esta operacion");
                    }
                }
                case 2 -> listarConductores();
                case 3 -> buscarConductorPorId();
                case 4 -> {
                    if (authService.tienePermiso("GESTION_CONDUCTORES")) {
                        actualizarConductor();
                    } else {
                        System.out.println("No tiene permisos para esta operacion");
                    }
                }
                case 5 -> {
                    if (authService.tienePermiso("GESTION_CONDUCTORES")) {
                        eliminarConductor();
                    } else {
                        System.out.println("No tiene permisos para esta operacion");
                    }
                }
                case 6 -> listarConductoresPorEstado();
            }
        } while (opcion != 0);
    }

    private static void registrarConductor() {
        System.out.println("\n--- REGISTRAR CONDUCTOR ---");
        System.out.print("Numero de identificacion: ");
        String numId = scanner.nextLine();
        System.out.print("Nombre completo: ");
        String nombre = scanner.nextLine();
        System.out.print("Tipo de licencia (A1/A2/A3/B1/B2/C1/C2/D1/D2): ");
        String tipoLicencia = scanner.nextLine().toUpperCase();
        System.out.print("Telefono de contacto: ");
        String telefono = scanner.nextLine();
        System.out.print("Estado (ACTIVO/DE_VACACIONES/INACTIVO/EN_RUTA): ");
        String estado = scanner.nextLine().toUpperCase();

        Conductor c = new Conductor(numId, nombre, tipoLicencia, telefono, estado);
        conductorDAO.agregar(c);
        auditoriaService.registrarOperacion("conductor", "INSERT", 0, "Conductor registrado: " + nombre);
    }

    private static void listarConductores() {
        System.out.println("\n--- LISTA DE CONDUCTORES ---");
        List<Conductor> conductores = conductorDAO.listar();
        if (conductores.isEmpty()) {
            System.out.println("No hay conductores registrados");
        } else {
            for (Conductor c : conductores) {
                System.out.println(c);
            }
        }
    }

    private static void buscarConductorPorId() {
        System.out.print("\nIngrese ID del conductor: ");
        int id = leerEntero();
        Conductor c = conductorDAO.buscarPorId(id);
        if (c != null) {
            System.out.println(c);
        } else {
            System.out.println("Conductor no encontrado");
        }
    }

    private static void actualizarConductor() {
        System.out.print("\nIngrese ID del conductor a actualizar: ");
        int id = leerEntero();
        Conductor c = conductorDAO.buscarPorId(id);
        if (c == null) {
            System.out.println("Conductor no encontrado");
            return;
        }

        System.out.println("Conductor actual: " + c);
        System.out.print("Nuevo nombre (Enter para mantener): ");
        String nombre = scanner.nextLine();
        if (!nombre.isEmpty()) {
            c = new Conductor(id, c.getNumeroIdentificacion(), nombre, c.getTipoLicencia(), c.getTelefonoContacto(), c.getEstado());
        }
        System.out.print("Nuevo tipo de licencia (Enter para mantener): ");
        String tipoLicencia = scanner.nextLine();
        if (!tipoLicencia.isEmpty()) {
            c = new Conductor(id, c.getNumeroIdentificacion(), c.getNombreCompleto(), tipoLicencia.toUpperCase(), c.getTelefonoContacto(), c.getEstado());
        }
        System.out.print("Nuevo telefono (Enter para mantener): ");
        String telefono = scanner.nextLine();
        if (!telefono.isEmpty()) {
            c = new Conductor(id, c.getNumeroIdentificacion(), c.getNombreCompleto(), c.getTipoLicencia(), telefono, c.getEstado());
        }
        System.out.print("Nuevo estado (Enter para mantener): ");
        String estado = scanner.nextLine();
        if (!estado.isEmpty()) {
            c = new Conductor(id, c.getNumeroIdentificacion(), c.getNombreCompleto(), c.getTipoLicencia(), c.getTelefonoContacto(), estado.toUpperCase());
        }

        conductorDAO.actualizar(c);
        auditoriaService.registrarOperacion("conductor", "UPDATE", id, "Conductor actualizado: " + c.getNombreCompleto());
    }

    private static void eliminarConductor() {
        System.out.print("\nIngrese ID del conductor a eliminar: ");
        int id = leerEntero();
        Conductor c = conductorDAO.buscarPorId(id);
        if (c != null) {
            conductorDAO.eliminar(id);
            auditoriaService.registrarOperacion("conductor", "DELETE", id, "Conductor eliminado: " + c.getNombreCompleto());
        } else {
            System.out.println("Conductor no encontrado");
        }
    }

    private static void listarConductoresPorEstado() {
        System.out.print("\nIngrese estado (ACTIVO/DE_VACACIONES/INACTIVO/EN_RUTA): ");
        String estado = scanner.nextLine().toUpperCase();
        List<Conductor> conductores = conductorDAO.listarPorEstado(estado);
        if (conductores.isEmpty()) {
            System.out.println("No hay conductores con ese estado");
        } else {
            for (Conductor c : conductores) {
                System.out.println(c);
            }
        }
    }

    /**
     * Displays and handles package management menu
     */
    private static void menuPaquetes() {
        int opcion;
        do {
            System.out.println("\n--- GESTION DE PAQUETES ---");
            if (authService.tienePermiso("GESTION_PAQUETES")) {
                System.out.println("1. Registrar paquete");
                System.out.println("5. Actualizar paquete");
            }
            System.out.println("2. Listar paquetes");
            System.out.println("3. Buscar paquete por ID");
            System.out.println("4. Buscar por codigo de seguimiento");
            if (authService.tienePermiso("ACTUALIZAR_ESTADO_PAQUETES")) {
                System.out.println("6. Cambiar estado de paquete");
            }
            System.out.println("7. Listar por estado");
            System.out.println("8. Buscar por remitente");
            System.out.println("9. Buscar por destinatario");
            System.out.println("0. Volver al menu principal");
            System.out.print("Seleccione una opcion: ");

            opcion = leerEntero();

            switch (opcion) {
                case 1 -> {
                    if (authService.tienePermiso("GESTION_PAQUETES")) {
                        registrarPaquete();
                    } else {
                        System.out.println("No tiene permisos para esta operacion");
                    }
                }
                case 2 -> listarPaquetes();
                case 3 -> buscarPaquetePorId();
                case 4 -> buscarPaquetePorCodigo();
                case 5 -> {
                    if (authService.tienePermiso("GESTION_PAQUETES")) {
                        actualizarPaquete();
                    } else {
                        System.out.println("No tiene permisos para esta operacion");
                    }
                }
                case 6 -> {
                    if (authService.tienePermiso("ACTUALIZAR_ESTADO_PAQUETES")) {
                        cambiarEstadoPaquete();
                    } else {
                        System.out.println("No tiene permisos para esta operacion");
                    }
                }
                case 7 -> listarPaquetesPorEstado();
                case 8 -> buscarPaquetesPorRemitente();
                case 9 -> buscarPaquetesPorDestinatario();
            }
        } while (opcion != 0);
    }

    private static void registrarPaquete() {
        System.out.println("\n--- REGISTRAR PAQUETE ---");
        String codigo = paqueteDAO.generarCodigoSeguimiento();
        System.out.println("Codigo de seguimiento generado: " + codigo);
        System.out.print("Descripcion: ");
        String descripcion = scanner.nextLine();
        System.out.print("Peso (kg): ");
        double peso = leerDouble();
        System.out.print("Largo (cm): ");
        double largo = leerDouble();
        System.out.print("Ancho (cm): ");
        double ancho = leerDouble();
        System.out.print("Alto (cm): ");
        double alto = leerDouble();
        System.out.print("Direccion origen: ");
        String dirOrigen = scanner.nextLine();
        System.out.print("Direccion destino: ");
        String dirDestino = scanner.nextLine();
        System.out.print("ID Remitente (Enter si no tiene): ");
        String remitenteStr = scanner.nextLine();
        Integer remitenteId = remitenteStr.isEmpty() ? null : Integer.parseInt(remitenteStr);
        System.out.print("ID Destinatario (Enter si no tiene): ");
        String destinatarioStr = scanner.nextLine();
        Integer destinatarioId = destinatarioStr.isEmpty() ? null : Integer.parseInt(destinatarioStr);
        System.out.print("Estado (EN_BODEGA/ASIGNADO_A_RUTA/EN_TRANSITO/ENTREGADO/DEVUELTO): ");
        String estado = scanner.nextLine().toUpperCase();

        Paquete p = new Paquete(codigo, descripcion, peso, largo, ancho, alto, dirOrigen, dirDestino, remitenteId, destinatarioId, estado);
        paqueteDAO.agregar(p);
        auditoriaService.registrarOperacion("paquete", "INSERT", 0, "Paquete registrado: " + codigo);
    }

    private static void listarPaquetes() {
        System.out.println("\n--- LISTA DE PAQUETES ---");
        List<Paquete> paquetes = paqueteDAO.listar();
        if (paquetes.isEmpty()) {
            System.out.println("No hay paquetes registrados");
        } else {
            for (Paquete p : paquetes) {
                System.out.println(p);
            }
        }
    }

    private static void buscarPaquetePorId() {
        System.out.print("\nIngrese ID del paquete: ");
        int id = leerEntero();
        Paquete p = paqueteDAO.buscarPorId(id);
        if (p != null) {
            System.out.println(p);
        } else {
            System.out.println("Paquete no encontrado");
        }
    }

    private static void buscarPaquetePorCodigo() {
        System.out.print("\nIngrese codigo de seguimiento: ");
        String codigo = scanner.nextLine();
        Paquete p = paqueteDAO.buscarPorCodigoSeguimiento(codigo);
        if (p != null) {
            System.out.println(p);
        } else {
            System.out.println("Paquete no encontrado");
        }
    }

    private static void actualizarPaquete() {
        System.out.print("\nIngrese ID del paquete a actualizar: ");
        int id = leerEntero();
        Paquete p = paqueteDAO.buscarPorId(id);
        if (p == null) {
            System.out.println("Paquete no encontrado");
            return;
        }

        System.out.println("Paquete actual: " + p);
        System.out.print("Nueva descripcion (Enter para mantener): ");
        String descripcion = scanner.nextLine();
        if (!descripcion.isEmpty()) {
            p = new Paquete(p.getId(), p.getCodigoSeguimiento(), descripcion, p.getPesoKg(), 
                          p.getLargoCm(), p.getAnchoCm(), p.getAltoCm(), p.getDireccionOrigen(), 
                          p.getDireccionDestino(), p.getRemitenteId(), p.getDestinatarioId(), p.getEstado());
        }

        paqueteDAO.actualizar(p);
        auditoriaService.registrarOperacion("paquete", "UPDATE", id, "Paquete actualizado: " + p.getCodigoSeguimiento());
    }

    private static void cambiarEstadoPaquete() {
        System.out.print("\nIngrese ID del paquete: ");
        int id = leerEntero();
        System.out.print("Nuevo estado (EN_BODEGA/ASIGNADO_A_RUTA/EN_TRANSITO/ENTREGADO/DEVUELTO): ");
        String estado = scanner.nextLine().toUpperCase();
        paqueteDAO.cambiarEstado(id, estado);
        auditoriaService.registrarOperacion("paquete", "UPDATE", id, "Estado cambiado a: " + estado);
    }

    private static void listarPaquetesPorEstado() {
        System.out.print("\nIngrese estado (EN_BODEGA/ASIGNADO_A_RUTA/EN_TRANSITO/ENTREGADO/DEVUELTO): ");
        String estado = scanner.nextLine().toUpperCase();
        List<Paquete> paquetes = paqueteDAO.listarPorEstado(estado);
        if (paquetes.isEmpty()) {
            System.out.println("No hay paquetes con ese estado");
        } else {
            for (Paquete p : paquetes) {
                System.out.println(p);
            }
        }
    }

    private static void buscarPaquetesPorRemitente() {
        System.out.print("\nIngrese ID del remitente: ");
        int id = leerEntero();
        List<Paquete> paquetes = paqueteDAO.buscarPorRemitente(id);
        if (paquetes.isEmpty()) {
            System.out.println("No hay paquetes enviados por ese cliente");
        } else {
            for (Paquete p : paquetes) {
                System.out.println(p);
            }
        }
    }

    private static void buscarPaquetesPorDestinatario() {
        System.out.print("\nIngrese ID del destinatario: ");
        int id = leerEntero();
        List<Paquete> paquetes = paqueteDAO.buscarPorDestinatario(id);
        if (paquetes.isEmpty()) {
            System.out.println("No hay paquetes recibidos por ese cliente");
        } else {
            for (Paquete p : paquetes) {
                System.out.println(p);
            }
        }
    }

    /**
     * Displays client management menu
     */
    private static void menuClientes() {
        int opcion;
        do {
            System.out.println("\n--- GESTION DE CLIENTES ---");
            if (authService.tienePermiso("GESTION_CLIENTES")) {
                System.out.println("1. Registrar cliente");
                System.out.println("4. Actualizar cliente");
                System.out.println("5. Eliminar cliente");
            }
            System.out.println("2. Listar clientes");
            System.out.println("3. Buscar cliente por ID");
            System.out.println("0. Volver al menu principal");
            System.out.print("Seleccione una opcion: ");

            opcion = leerEntero();

            switch (opcion) {
                case 1 -> {
                    if (authService.tienePermiso("GESTION_CLIENTES")) {
                        registrarCliente();
                    } else {
                        System.out.println("No tiene permisos para esta operacion");
                    }
                }
                case 2 -> listarClientes();
                case 3 -> buscarClientePorId();
                case 4 -> {
                    if (authService.tienePermiso("GESTION_CLIENTES")) {
                        actualizarCliente();
                    } else {
                        System.out.println("No tiene permisos para esta operacion");
                    }
                }
                case 5 -> {
                    if (authService.tienePermiso("GESTION_CLIENTES")) {
                        eliminarCliente();
                    } else {
                        System.out.println("No tiene permisos para esta operacion");
                    }
                }
            }
        } while (opcion != 0);
    }

    private static void registrarCliente() {
        System.out.println("\n--- REGISTRAR CLIENTE ---");
        System.out.print("Numero de identificacion: ");
        String numId = scanner.nextLine();
        System.out.print("Nombre completo: ");
        String nombre = scanner.nextLine();
        System.out.print("Telefono: ");
        String telefono = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Direccion: ");
        String direccion = scanner.nextLine();

        Cliente c = new Cliente(numId, nombre, telefono, email, direccion);
        clienteDAO.agregar(c);
        auditoriaService.registrarOperacion("cliente", "INSERT", 0, "Cliente registrado: " + nombre);
    }

    private static void listarClientes() {
        System.out.println("\n--- LISTA DE CLIENTES ---");
        List<Cliente> clientes = clienteDAO.listar();
        if (clientes.isEmpty()) {
            System.out.println("No hay clientes registrados");
        } else {
            for (Cliente c : clientes) {
                System.out.println(c);
            }
        }
    }

    private static void buscarClientePorId() {
        System.out.print("\nIngrese ID del cliente: ");
        int id = leerEntero();
        Cliente c = clienteDAO.buscarPorId(id);
        if (c != null) {
            System.out.println(c);
        } else {
            System.out.println("Cliente no encontrado");
        }
    }

    private static void actualizarCliente() {
        System.out.print("\nIngrese ID del cliente a actualizar: ");
        int id = leerEntero();
        Cliente c = clienteDAO.buscarPorId(id);
        if (c == null) {
            System.out.println("Cliente no encontrado");
            return;
        }

        System.out.println("Cliente actual: " + c);
        System.out.print("Nuevo nombre (Enter para mantener): ");
        String nombre = scanner.nextLine();
        if (!nombre.isEmpty()) {
            c = new Cliente(id, c.getNumIdentificacion(), nombre, c.getTelefono(), c.getEmail(), c.getDireccion());
        }
        System.out.print("Nuevo telefono (Enter para mantener): ");
        String telefono = scanner.nextLine();
        if (!telefono.isEmpty()) {
            c = new Cliente(id, c.getNumIdentificacion(), c.getNombreCompleto(), telefono, c.getEmail(), c.getDireccion());
        }
        System.out.print("Nuevo email (Enter para mantener): ");
        String email = scanner.nextLine();
        if (!email.isEmpty()) {
            c = new Cliente(id, c.getNumIdentificacion(), c.getNombreCompleto(), c.getTelefono(), email, c.getDireccion());
        }
        System.out.print("Nueva direccion (Enter para mantener): ");
        String direccion = scanner.nextLine();
        if (!direccion.isEmpty()) {
            c = new Cliente(id, c.getNumIdentificacion(), c.getNombreCompleto(), c.getTelefono(), c.getEmail(), direccion);
        }

        clienteDAO.actualizar(c);
        auditoriaService.registrarOperacion("cliente", "UPDATE", id, "Cliente actualizado: " + c.getNombreCompleto());
    }

    private static void eliminarCliente() {
        System.out.print("\nIngrese ID del cliente a eliminar: ");
        int id = leerEntero();
        Cliente c = clienteDAO.buscarPorId(id);
        if (c != null) {
            clienteDAO.eliminar(id);
            auditoriaService.registrarOperacion("cliente", "DELETE", id, "Cliente eliminado: " + c.getNombreCompleto());
        } else {
            System.out.println("Cliente no encontrado");
        }
    }

    /**
     * Displays route planning and tracking menu with role-based options
     */
    private static void menuRutas() {
        int opcion;
        Usuario usuario = authService.getUsuarioActual();
        boolean esConductor = usuario.esConductor();
        
        do {
            System.out.println("\n--- PLANIFICACION Y SEGUIMIENTO DE RUTAS ---");
            if (authService.tienePermiso("CREAR_RUTAS")) {
                System.out.println("1. Crear hoja de ruta");
            }
            if (esConductor) {
                System.out.println("2. Ver mis rutas");
            } else {
                System.out.println("2. Listar hojas de ruta");
            }
            System.out.println("3. Buscar hoja de ruta por ID");
            if (authService.tienePermiso("CREAR_RUTAS")) {
                System.out.println("4. Agregar paquete a ruta");
                System.out.println("5. Iniciar ruta");
                System.out.println("6. Finalizar ruta");
            }
            System.out.println("7. Ver paquetes de una ruta");
            if (esConductor) {
                System.out.println("8. Ver mis rutas activas");
            } else {
                System.out.println("8. Listar rutas activas");
            }
            if (authService.tienePermiso("ACTUALIZAR_ESTADO_PAQUETES")) {
                System.out.println("9. Actualizar estado de paquete en ruta");
            }
            System.out.println("0. Volver al menu principal");
            System.out.print("Seleccione una opcion: ");

            opcion = leerEntero();

            switch (opcion) {
                case 1 -> {
                    if (authService.tienePermiso("CREAR_RUTAS")) {
                        crearHojaRuta();
                    } else {
                        System.out.println("No tiene permisos para crear rutas");
                    }
                }
                case 2 -> {
                    if (esConductor) {
                        verMisRutas();
                    } else {
                        listarHojasRuta();
                    }
                }
                case 3 -> buscarHojaRutaPorId();
                case 4 -> {
                    if (authService.tienePermiso("CREAR_RUTAS")) {
                        agregarPaqueteARuta();
                    } else {
                        System.out.println("No tiene permisos para esta operacion");
                    }
                }
                case 5 -> {
                    if (authService.tienePermiso("CREAR_RUTAS")) {
                        iniciarRuta();
                    } else {
                        System.out.println("No tiene permisos para esta operacion");
                    }
                }
                case 6 -> {
                    if (authService.tienePermiso("CREAR_RUTAS")) {
                        finalizarRuta();
                    } else {
                        System.out.println("No tiene permisos para esta operacion");
                    }
                }
                case 7 -> verPaquetesDeRuta();
                case 8 -> {
                    if (esConductor) {
                        verMisRutasActivas();
                    } else {
                        listarRutasActivas();
                    }
                }
                case 9 -> {
                    if (authService.tienePermiso("ACTUALIZAR_ESTADO_PAQUETES")) {
                        actualizarEstadoPaqueteEnRuta();
                    } else {
                        System.out.println("No tiene permisos para esta operacion");
                    }
                }
            }
        } while (opcion != 0);
    }
    
    private static void verMisRutas() {
        Usuario usuario = authService.getUsuarioActual();
        if (usuario.getConductorId() == null) {
            System.out.println("Este usuario no esta asociado a un conductor");
            return;
        }
        
        System.out.println("\n--- MIS RUTAS ---");
        List<HojaRuta> rutas = hojaRutaDAO.listarPorConductor(usuario.getConductorId());
        if (rutas.isEmpty()) {
            System.out.println("No tiene rutas asignadas");
        } else {
            for (HojaRuta hr : rutas) {
                System.out.println(hr);
            }
        }
    }
    
    private static void verMisRutasActivas() {
        Usuario usuario = authService.getUsuarioActual();
        if (usuario.getConductorId() == null) {
            System.out.println("Este usuario no esta asociado a un conductor");
            return;
        }
        
        System.out.println("\n--- MIS RUTAS ACTIVAS ---");
        List<HojaRuta> rutas = hojaRutaDAO.listarActivasPorConductor(usuario.getConductorId());
        if (rutas.isEmpty()) {
            System.out.println("No tiene rutas activas");
        } else {
            for (HojaRuta hr : rutas) {
                System.out.println(hr);
            }
        }
    }

    private static void crearHojaRuta() {
        System.out.println("\n--- CREAR HOJA DE RUTA ---");
        System.out.print("Fecha (YYYY-MM-DD): ");
        String fechaStr = scanner.nextLine();
        LocalDate fecha;
        try {
            fecha = LocalDate.parse(fechaStr);
        } catch (DateTimeParseException e) {
            System.out.println("Fecha invalida. Usando fecha actual");
            fecha = LocalDate.now();
        }
        System.out.print("ID Vehiculo: ");
        int vehiculoId = leerEntero();
        System.out.print("ID Conductor: ");
        int conductorId = leerEntero();

        HojaRuta hr = new HojaRuta(fecha, vehiculoId, conductorId, "PLANIFICADA");
        hojaRutaDAO.agregar(hr);
        auditoriaService.registrarOperacion("hoja_ruta", "INSERT", 0, "Hoja de ruta creada para fecha: " + fecha);
    }

    private static void listarHojasRuta() {
        System.out.println("\n--- LISTA DE HOJAS DE RUTA ---");
        List<HojaRuta> rutas = hojaRutaDAO.listar();
        if (rutas.isEmpty()) {
            System.out.println("No hay hojas de ruta registradas");
        } else {
            for (HojaRuta hr : rutas) {
                System.out.println(hr);
            }
        }
    }

    private static void buscarHojaRutaPorId() {
        System.out.print("\nIngrese ID de la hoja de ruta: ");
        int id = leerEntero();
        HojaRuta hr = hojaRutaDAO.buscarPorId(id);
        if (hr != null) {
            System.out.println(hr);
        } else {
            System.out.println("Hoja de ruta no encontrada");
        }
    }

    private static void agregarPaqueteARuta() {
        System.out.print("\nIngrese ID de la hoja de ruta: ");
        int hojaRutaId = leerEntero();
        System.out.print("ID del paquete: ");
        int paqueteId = leerEntero();
        System.out.print("Orden de entrega (Enter si no aplica): ");
        String ordenStr = scanner.nextLine();
        Integer orden = ordenStr.isEmpty() ? null : Integer.parseInt(ordenStr);

        rutaService.agregarPaqueteARuta(hojaRutaId, paqueteId, orden);
        auditoriaService.registrarOperacion("ruta_paquete", "INSERT", 0, "Paquete " + paqueteId + " agregado a ruta " + hojaRutaId);
    }

    private static void iniciarRuta() {
        System.out.print("\nIngrese ID de la hoja de ruta: ");
        int id = leerEntero();
        rutaService.iniciarRuta(id);
        auditoriaService.registrarOperacion("hoja_ruta", "UPDATE", id, "Ruta iniciada");
    }

    private static void finalizarRuta() {
        System.out.print("\nIngrese ID de la hoja de ruta: ");
        int id = leerEntero();
        rutaService.finalizarRuta(id);
        auditoriaService.registrarOperacion("hoja_ruta", "UPDATE", id, "Ruta finalizada");
    }

    private static void verPaquetesDeRuta() {
        System.out.print("\nIngrese ID de la hoja de ruta: ");
        int id = leerEntero();
        List<RutaPaquete> paquetes = rutaService.obtenerPaquetesDeRuta(id);
        if (paquetes.isEmpty()) {
            System.out.println("No hay paquetes asignados a esta ruta");
        } else {
            System.out.println("\n--- PAQUETES DE LA RUTA ---");
            for (RutaPaquete rp : paquetes) {
                Paquete p = paqueteDAO.buscarPorId(rp.getPaqueteId());
                System.out.println("Orden: " + rp.getOrdenEntrega() + " - " + p);
            }
        }
    }

    private static void listarRutasActivas() {
        System.out.println("\n--- RUTAS ACTIVAS ---");
        List<HojaRuta> rutas = hojaRutaDAO.listarPorEstado("EN_CURSO");
        if (rutas.isEmpty()) {
            System.out.println("No hay rutas activas");
        } else {
            for (HojaRuta hr : rutas) {
                System.out.println(hr);
            }
        }
    }

    private static void actualizarEstadoPaqueteEnRuta() {
        System.out.print("\nIngrese ID del paquete: ");
        int id = leerEntero();
        System.out.print("Nuevo estado (EN_TRANSITO/ENTREGADO/DEVUELTO): ");
        String estado = scanner.nextLine().toUpperCase();
        paqueteDAO.cambiarEstado(id, estado);
        auditoriaService.registrarOperacion("paquete", "UPDATE", id, "Estado cambiado a: " + estado + " durante ruta");
    }

    /**
     * Displays maintenance management menu
     */
    private static void menuMantenimientos() {
        int opcion;
        do {
            System.out.println("\n--- GESTION DE MANTENIMIENTOS ---");
            if (authService.tienePermiso("GESTION_MANTENIMIENTOS")) {
                System.out.println("1. Registrar mantenimiento");
            }
            System.out.println("2. Listar mantenimientos");
            System.out.println("3. Buscar mantenimiento por ID");
            System.out.println("4. Ver mantenimientos de un vehiculo");
            System.out.println("0. Volver al menu principal");
            System.out.print("Seleccione una opcion: ");

            opcion = leerEntero();

            switch (opcion) {
                case 1 -> {
                    if (authService.tienePermiso("GESTION_MANTENIMIENTOS")) {
                        registrarMantenimiento();
                    } else {
                        System.out.println("No tiene permisos para esta operacion");
                    }
                }
                case 2 -> listarMantenimientos();
                case 3 -> buscarMantenimientoPorId();
                case 4 -> verMantenimientosVehiculo();
            }
        } while (opcion != 0);
    }

    private static void registrarMantenimiento() {
        System.out.println("\n--- REGISTRAR MANTENIMIENTO ---");
        System.out.print("Fecha programada (YYYY-MM-DD o Enter para hoy): ");
        String fechaProgStr = scanner.nextLine();
        LocalDate fechaProg;
        try {
            fechaProg = fechaProgStr.isEmpty() ? LocalDate.now() : LocalDate.parse(fechaProgStr);
        } catch (DateTimeParseException e) {
            System.out.println("Fecha programada invalida. Usando fecha de hoy.");
            fechaProg = LocalDate.now();
        }
        
        System.out.print("Fecha real (YYYY-MM-DD o Enter si no aplica): ");
        String fechaRealStr = scanner.nextLine();
        LocalDate fechaReal = null;
        if (!fechaRealStr.isEmpty()) {
            try {
                fechaReal = LocalDate.parse(fechaRealStr);
            } catch (DateTimeParseException e) {
                System.out.println("Fecha real invalida. Se omitira.");
            }
        }
        System.out.print("Descripcion: ");
        String descripcion = scanner.nextLine();
        System.out.print("Costo (Enter si no aplica): ");
        String costoStr = scanner.nextLine();
        Double costo = null;
        if (!costoStr.isEmpty()) {
            try {
                costo = Double.parseDouble(costoStr);
            } catch (NumberFormatException e) {
                System.out.println("Costo invalido. Se omitira.");
            }
        }
        
        System.out.print("Kilometraje (Enter si no aplica): ");
        String kmStr = scanner.nextLine();
        Integer kilometraje = null;
        if (!kmStr.isEmpty()) {
            try {
                kilometraje = Integer.parseInt(kmStr);
            } catch (NumberFormatException e) {
                System.out.println("Kilometraje invalido. Se omitira.");
            }
        }
        System.out.print("ID Vehiculo: ");
        int vehiculoId = leerEntero();
        System.out.print("Tipo (PREVENTIVO/CORRECTIVO): ");
        String tipo = scanner.nextLine().toUpperCase();

        Mantenimiento m = new Mantenimiento(fechaProg, fechaReal, descripcion, costo, kilometraje, vehiculoId, tipo);
        mantenimientoDAO.agregar(m);
    }

    private static void listarMantenimientos() {
        System.out.println("\n--- LISTA DE MANTENIMIENTOS ---");
        List<Mantenimiento> mantenimientos = mantenimientoDAO.listar();
        if (mantenimientos.isEmpty()) {
            System.out.println("No hay mantenimientos registrados");
        } else {
            for (Mantenimiento m : mantenimientos) {
                System.out.println(m);
            }
        }
    }

    private static void buscarMantenimientoPorId() {
        System.out.print("\nIngrese ID del mantenimiento: ");
        int id = leerEntero();
        Mantenimiento m = mantenimientoDAO.buscarPorId(id);
        if (m != null) {
            System.out.println(m);
        } else {
            System.out.println("Mantenimiento no encontrado");
        }
    }

    /**
     * Displays reports menu with delivery and vehicle history options
     */
    private static void menuReportes() {
        int opcion;
        do {
            System.out.println("\n--- REPORTES ---");
            System.out.println("1. Reporte de entregas por conductor");
            System.out.println("2. Historial de rutas de un vehiculo");
            System.out.println("0. Volver al menu principal");
            System.out.print("Seleccione una opcion: ");

            opcion = leerEntero();

            switch (opcion) {
                case 1 -> generarReporteEntregas();
                case 2 -> generarHistorialVehiculo();
            }
        } while (opcion != 0);
    }

    private static void generarReporteEntregas() {
        System.out.print("\nIngrese ID del conductor: ");
        int conductorId = leerEntero();
        if (conductorId < 0) {
            System.out.println("ID de conductor invalido");
            return;
        }
        
        System.out.print("Fecha inicio (YYYY-MM-DD): ");
        String fechaInicioStr = scanner.nextLine();
        LocalDate fechaInicio;
        try {
            fechaInicio = LocalDate.parse(fechaInicioStr);
        } catch (DateTimeParseException e) {
            System.out.println("Fecha de inicio invalida. Formato requerido: YYYY-MM-DD");
            return;
        }
        
        System.out.print("Fecha fin (YYYY-MM-DD): ");
        String fechaFinStr = scanner.nextLine();
        LocalDate fechaFin;
        try {
            fechaFin = LocalDate.parse(fechaFinStr);
        } catch (DateTimeParseException e) {
            System.out.println("Fecha de fin invalida. Formato requerido: YYYY-MM-DD");
            return;
        }
        
        reporteService.generarReporteEntregasConductor(conductorId, fechaInicio, fechaFin);
    }

    private static void generarHistorialVehiculo() {
        System.out.print("\nIngrese ID del vehiculo: ");
        int vehiculoId = leerEntero();
        reporteService.generarHistorialRutasVehiculo(vehiculoId);
    }

    /**
     * Displays audit log viewing interface
     */
    private static void menuAuditoria() {
        System.out.println("\n--- AUDITORIA ---");
        System.out.print("Ingrese cantidad de registros a mostrar (default 50): ");
        String limiteStr = scanner.nextLine();
        int limite = limiteStr.isEmpty() ? 50 : Integer.parseInt(limiteStr);
        auditoriaService.listarAuditoria(limite);
    }

    private static int leerEntero() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static double leerDouble() {
        try {
            return Double.parseDouble(scanner.nextLine());
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
}










