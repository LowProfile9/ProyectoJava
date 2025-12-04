-- Base de datos MySQL 8
-- Sistema de Logística y Entrega

CREATE DATABASE IF NOT EXISTS proyecto_java;

USE proyecto_java;

-- Tabla de estados de ruta
CREATE TABLE estado_ruta (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(50) NOT NULL
);

-- Tabla de estados de paquete
CREATE TABLE estado_paquete (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(50) NOT NULL
);

-- Tabla de estados de vehiculo
CREATE TABLE estado_vehiculo (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(50) NOT NULL
);

-- Tabla de estados de conductor
CREATE TABLE estado_conductor (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(50) NOT NULL
);

-- Tabla de tipos de mantenimiento
CREATE TABLE tipo_mantenimiento (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(50) NOT NULL
);

-- Tabla de roles de usuario
CREATE TABLE rol_usuario (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(50) NOT NULL UNIQUE,
    descripcion VARCHAR(255)
);

-- Tabla de usuarios del sistema
CREATE TABLE usuario (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    nombre_completo VARCHAR(200) NOT NULL,
    rol_id INT NOT NULL,
    conductor_id INT NULL,
    activo BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (rol_id) REFERENCES rol_usuario(id),
    FOREIGN KEY (conductor_id) REFERENCES conductor(id)
);

-- Tabla de clientes
CREATE TABLE cliente (
    id INT PRIMARY KEY AUTO_INCREMENT,
    num_identificacion VARCHAR(50) NOT NULL UNIQUE,
    nombre_completo VARCHAR(200) NOT NULL,
    telefono VARCHAR(20),
    email VARCHAR(100),
    direccion VARCHAR(255)
);

-- Tabla de conductores
CREATE TABLE conductor (
    id INT PRIMARY KEY AUTO_INCREMENT,
    numero_identificacion VARCHAR(50) NOT NULL UNIQUE,
    nombre_completo VARCHAR(200) NOT NULL,
    tipo_licencia VARCHAR(20),
    telefono_contacto VARCHAR(20),
    estado_id INT,
    FOREIGN KEY (estado_id) REFERENCES estado_conductor(id)
);

-- Tabla de vehiculos
CREATE TABLE vehiculo (
    id INT PRIMARY KEY AUTO_INCREMENT,
    placa VARCHAR(20) NOT NULL UNIQUE,
    marca VARCHAR(50),
    modelo VARCHAR(50),
    anio_fabricacion INT,
    capacidad_max_kg DECIMAL(10,2),
    estado_id INT,
    conductor_id INT UNIQUE,
    FOREIGN KEY (estado_id) REFERENCES estado_vehiculo(id),
    FOREIGN KEY (conductor_id) REFERENCES conductor(id)
);

-- Tabla de paquetes
CREATE TABLE paquete (
    id INT PRIMARY KEY AUTO_INCREMENT,
    codigo_seguimiento VARCHAR(50) NOT NULL UNIQUE,
    descripcion TEXT,
    peso_kg DECIMAL(10,2),
    largo_cm DECIMAL(10,2),
    ancho_cm DECIMAL(10,2),
    alto_cm DECIMAL(10,2),
    direccion_origen VARCHAR(255),
    direccion_destino VARCHAR(255),
    remitente_id INT,
    destinatario_id INT,
    estado_id INT,
    FOREIGN KEY (remitente_id) REFERENCES cliente(id),
    FOREIGN KEY (destinatario_id) REFERENCES cliente(id),
    FOREIGN KEY (estado_id) REFERENCES estado_paquete(id)
);

-- Tabla de hojas de ruta
CREATE TABLE hoja_ruta (
    id INT PRIMARY KEY AUTO_INCREMENT,
    fecha DATE NOT NULL,
    hora_inicio TIMESTAMP NULL,
    hora_fin TIMESTAMP NULL,
    peso_total_kg DECIMAL(10,2),
    vehiculo_id INT,
    conductor_id INT,
    estado_id INT,
    FOREIGN KEY (vehiculo_id) REFERENCES vehiculo(id),
    FOREIGN KEY (conductor_id) REFERENCES conductor(id),
    FOREIGN KEY (estado_id) REFERENCES estado_ruta(id)
);

-- Tabla de relación ruta-paquete
CREATE TABLE ruta_paquete (
    id INT PRIMARY KEY AUTO_INCREMENT,
    hoja_ruta_id INT NOT NULL,
    paquete_id INT NOT NULL UNIQUE,
    orden_entrega INT,
    FOREIGN KEY (hoja_ruta_id) REFERENCES hoja_ruta(id),
    FOREIGN KEY (paquete_id) REFERENCES paquete(id)
);

-- Tabla de mantenimientos
CREATE TABLE mantenimiento (
    id INT PRIMARY KEY AUTO_INCREMENT,
    fecha_programa DATE,
    fecha_real DATE,
    descripcion TEXT,
    costo DECIMAL(10,2),
    kilometraje INT,
    vehiculo_id INT,
    tipo_mantenimiento_id INT,
    FOREIGN KEY (vehiculo_id) REFERENCES vehiculo(id),
    FOREIGN KEY (tipo_mantenimiento_id) REFERENCES tipo_mantenimiento(id)
);

-- Datos iniciales: Estados de ruta
INSERT INTO estado_ruta (nombre) VALUES
('PLANIFICADA'),
('EN_CURSO'),
('FINALIZADO'),
('CANCELADO');

-- Datos iniciales: Estados de paquete
INSERT INTO estado_paquete (nombre) VALUES
('EN_BODEGA'),
('ASIGNADO_A_RUTA'),
('EN_TRANSITO'),
('ENTREGADO'),
('DEVUELTO');

-- Datos iniciales: Estados de vehiculo
INSERT INTO estado_vehiculo (nombre) VALUES
('DISPONIBLE'),
('EN_RUTA'),
('EN_MANTENIMIENTO');

-- Datos iniciales: Estados de conductor
INSERT INTO estado_conductor (nombre) VALUES
('ACTIVO'),
('DE_VACACIONES'),
('INACTIVO'),
('EN_RUTA');

-- Datos iniciales: Tipos de mantenimiento
INSERT INTO tipo_mantenimiento (nombre) VALUES
('PREVENTIVO'),
('CORRECTIVO');

-- Datos iniciales: Roles de usuario
INSERT INTO rol_usuario (nombre, descripcion) VALUES
('ADMINISTRADOR', 'Acceso completo al sistema'),
('OPERADOR_LOGISTICA', 'Crear rutas y gestionar paquetes'),
('CONDUCTOR', 'Ver sus rutas y actualizar estados de paquetes'),
('AUDITOR', 'Solo ver reportes y auditoría');

-- ============================================
-- TABLA DE AUDITORÍA
-- ============================================

CREATE TABLE auditoria (
    id INT PRIMARY KEY AUTO_INCREMENT,
    tabla_afectada VARCHAR(50) NOT NULL,
    operacion VARCHAR(20) NOT NULL,
    id_registro INT,
    descripcion TEXT,
    usuario VARCHAR(100),
    fecha_hora TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================================
-- TRIGGERS DE AUDITORÍA 
-- ============================================

CREATE TRIGGER trg_vehiculo_insert AFTER INSERT ON vehiculo
FOR EACH ROW
INSERT INTO auditoria (tabla_afectada, operacion, id_registro, descripcion, usuario)
VALUES ('vehiculo', 'INSERT', NEW.id, CONCAT('Vehiculo registrado: ', NEW.placa), USER());

CREATE TRIGGER trg_vehiculo_update AFTER UPDATE ON vehiculo
FOR EACH ROW
INSERT INTO auditoria (tabla_afectada, operacion, id_registro, descripcion, usuario)
VALUES ('vehiculo', 'UPDATE', NEW.id, CONCAT('Vehiculo actualizado: ', NEW.placa), USER());

CREATE TRIGGER trg_paquete_insert AFTER INSERT ON paquete
FOR EACH ROW
INSERT INTO auditoria (tabla_afectada, operacion, id_registro, descripcion, usuario)
VALUES ('paquete', 'INSERT', NEW.id, CONCAT('Paquete registrado: ', NEW.codigo_seguimiento), USER());

CREATE TRIGGER trg_paquete_update AFTER UPDATE ON paquete
FOR EACH ROW
INSERT INTO auditoria (tabla_afectada, operacion, id_registro, descripcion, usuario)
VALUES ('paquete', 'UPDATE', NEW.id, CONCAT('Paquete actualizado: ', NEW.codigo_seguimiento), USER());

CREATE TRIGGER trg_hoja_ruta_insert AFTER INSERT ON hoja_ruta
FOR EACH ROW
INSERT INTO auditoria (tabla_afectada, operacion, id_registro, descripcion, usuario)
VALUES ('hoja_ruta', 'INSERT', NEW.id, CONCAT('Hoja de ruta creada: ID ', NEW.id), USER());

CREATE TRIGGER trg_hoja_ruta_update AFTER UPDATE ON hoja_ruta
FOR EACH ROW
INSERT INTO auditoria (tabla_afectada, operacion, id_registro, descripcion, usuario)
VALUES ('hoja_ruta', 'UPDATE', NEW.id, CONCAT('Hoja de ruta actualizada: ID ', NEW.id), USER());
