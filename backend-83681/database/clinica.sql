-- =====================================================================
-- Base de datos de la clinica veterinaria.
-- Generada a partir de las entidades JPA del backend (paquete entity).
-- Compatible con MySQL 8 y MariaDB 10.4+.
--
-- Las restricciones UNIQUE evitan registros duplicados y las llaves
-- foraneas impiden eliminar registros que estan siendo usados.
-- =====================================================================

CREATE DATABASE IF NOT EXISTS clinica
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_general_ci;

USE clinica;

-- ---------------------------------------------------------------------
-- Usuarios del sistema
-- ---------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS usuario (
    id             BIGINT       NOT NULL AUTO_INCREMENT,
    username       VARCHAR(50)  NOT NULL,
    password_hash  TEXT,
    rol            VARCHAR(30)  NOT NULL,
    fecha_creacion DATETIME,
    activo         TINYINT(1)   DEFAULT 1,
    email          VARCHAR(255) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uk_usuario_username UNIQUE (username),
    CONSTRAINT uk_usuario_email UNIQUE (email)
);

-- ---------------------------------------------------------------------
-- Clientes (dueños de las mascotas)
-- ---------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS cliente (
    cliente_id       BIGINT       NOT NULL AUTO_INCREMENT,
    tipo_documento   VARCHAR(10)  NOT NULL,
    numero_documento VARCHAR(20)  NOT NULL,
    nombres          VARCHAR(100) NOT NULL,
    apellidos        VARCHAR(100) NOT NULL,
    fecha_nacimiento DATE,
    genero           VARCHAR(20),
    telefono         VARCHAR(20),
    direccion        VARCHAR(150),
    activo           TINYINT(1)   DEFAULT 1,
    PRIMARY KEY (cliente_id),
    CONSTRAINT uk_cliente_documento UNIQUE (tipo_documento, numero_documento)
);

-- ---------------------------------------------------------------------
-- Razas
-- ---------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS raza (
    raza_id            INT         NOT NULL AUTO_INCREMENT,
    nombre             VARCHAR(80) NOT NULL,
    especie            VARCHAR(50) NOT NULL,
    fecha_creacion     DATETIME,
    fecha_modificacion DATETIME,
    PRIMARY KEY (raza_id),
    CONSTRAINT uk_raza_nombre_especie UNIQUE (nombre, especie)
);

-- ---------------------------------------------------------------------
-- Mascotas
-- ---------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS mascota (
    mascota_id         BIGINT      NOT NULL AUTO_INCREMENT,
    nombre_mascota     VARCHAR(80) NOT NULL,
    edad               INT,
    fecha_registro     DATETIME,
    fecha_modificacion DATETIME,
    raza_id            INT         NOT NULL,
    cliente_id         BIGINT      NOT NULL,
    PRIMARY KEY (mascota_id),
    -- Un cliente no puede tener dos mascotas con el mismo nombre.
    CONSTRAINT uk_mascota_nombre_cliente UNIQUE (nombre_mascota, cliente_id),
    CONSTRAINT fk_mascota_raza FOREIGN KEY (raza_id) REFERENCES raza (raza_id),
    CONSTRAINT fk_mascota_cliente FOREIGN KEY (cliente_id) REFERENCES cliente (cliente_id)
);

-- ---------------------------------------------------------------------
-- Especializaciones de los medicos
-- ---------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS especializacion (
    id                     BIGINT       NOT NULL AUTO_INCREMENT,
    nombre                 VARCHAR(100) NOT NULL,
    descripcion            TEXT,
    codigo_especializacion VARCHAR(10)  NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uk_especializacion_nombre UNIQUE (nombre),
    CONSTRAINT uk_especializacion_codigo UNIQUE (codigo_especializacion)
);

-- ---------------------------------------------------------------------
-- Medicos veterinarios
-- ---------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS medico (
    id                   BIGINT       NOT NULL AUTO_INCREMENT,
    tipo_documento       VARCHAR(10)  NOT NULL,
    numero_documento     VARCHAR(20)  NOT NULL,
    nombres              VARCHAR(100) NOT NULL,
    apellidos            VARCHAR(100) NOT NULL,
    telefono             VARCHAR(20),
    registro_profesional VARCHAR(50)  NOT NULL,
    especializacion_id   BIGINT       NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uk_medico_documento UNIQUE (tipo_documento, numero_documento),
    CONSTRAINT uk_medico_registro UNIQUE (registro_profesional),
    CONSTRAINT fk_medico_especializacion FOREIGN KEY (especializacion_id) REFERENCES especializacion (id)
);

-- ---------------------------------------------------------------------
-- Citas medicas
-- ---------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS cita (
    id         BIGINT      NOT NULL AUTO_INCREMENT,
    cliente_id BIGINT      NOT NULL,
    mascota_id BIGINT      NOT NULL,
    medico_id  BIGINT      NOT NULL,
    fecha_hora DATETIME    NOT NULL,
    estado     VARCHAR(20) NOT NULL,
    motivo     TEXT,
    PRIMARY KEY (id),
    CONSTRAINT fk_cita_cliente FOREIGN KEY (cliente_id) REFERENCES cliente (cliente_id),
    CONSTRAINT fk_cita_mascota FOREIGN KEY (mascota_id) REFERENCES mascota (mascota_id),
    CONSTRAINT fk_cita_medico FOREIGN KEY (medico_id) REFERENCES medico (id)
);

-- ---------------------------------------------------------------------
-- Formulas medicas
-- ---------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS formula_medica (
    id                           BIGINT   NOT NULL AUTO_INCREMENT,
    cita_id                      BIGINT   NOT NULL,
    medicamento_id               INT      NOT NULL,
    dosis                        TEXT     NOT NULL,
    indicaciones                 TEXT,
    fecha_creacion_registro      DATETIME NOT NULL,
    fecha_actualizacion_registro DATETIME,
    PRIMARY KEY (id),
    CONSTRAINT fk_formula_cita FOREIGN KEY (cita_id) REFERENCES cita (id)
);

-- ---------------------------------------------------------------------
-- Historias medicas y sus anotaciones
-- ---------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS historia_medica (
    id             BIGINT NOT NULL AUTO_INCREMENT,
    paciente_id    BIGINT NOT NULL,
    fecha_creacion DATETIME,
    PRIMARY KEY (id),
    CONSTRAINT fk_historia_mascota FOREIGN KEY (paciente_id) REFERENCES mascota (mascota_id)
);

CREATE TABLE IF NOT EXISTS anotacion_historia (
    id          BIGINT   NOT NULL AUTO_INCREMENT,
    historia_id BIGINT   NOT NULL,
    medico_id   BIGINT   NOT NULL,
    fecha       DATETIME,
    descripcion TEXT     NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_anotacion_historia FOREIGN KEY (historia_id) REFERENCES historia_medica (id),
    CONSTRAINT fk_anotacion_medico FOREIGN KEY (medico_id) REFERENCES medico (id)
);

-- ---------------------------------------------------------------------
-- Datos iniciales (catalogos)
-- ---------------------------------------------------------------------
INSERT IGNORE INTO especializacion (nombre, descripcion, codigo_especializacion) VALUES
    ('Medicina General', 'Consulta general y medicina preventiva', 'MG'),
    ('Cirugía', 'Procedimientos quirúrgicos', 'CIR'),
    ('Dermatología', 'Enfermedades de la piel', 'DER');

INSERT IGNORE INTO raza (nombre, especie, fecha_creacion) VALUES
    ('Criollo', 'Perro', NOW()),
    ('Labrador', 'Perro', NOW()),
    ('Criollo', 'Gato', NOW()),
    ('Persa', 'Gato', NOW());
