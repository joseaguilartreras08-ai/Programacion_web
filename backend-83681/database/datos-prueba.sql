-- =====================================================================
-- Datos de prueba (inventados) para la clinica veterinaria.
-- Ejecutar despues de clinica.sql. Se puede correr varias veces:
-- INSERT IGNORE salta los registros que ya existen (por las llaves UNIQUE)
-- y las relaciones se buscan por documento / nombre, no por id.
--
-- Contraseña de todos los usuarios de prueba: Clinica123 (guardada en MD5,
-- igual que PasswordUtil del backend).
-- =====================================================================

USE clinica;

-- ---------------------------------------------------------------------
-- Usuarios
-- ---------------------------------------------------------------------
INSERT IGNORE INTO usuario (username, password_hash, rol, fecha_creacion, activo, email) VALUES
    ('admin',       MD5('Clinica123'), 'Administrador', NOW(), 1, 'admin@clinicaveterinaria.com'),
    ('mrodriguez',  MD5('Clinica123'), 'Medico',        NOW(), 1, 'maria.rodriguez@clinicaveterinaria.com'),
    ('jgarcia',     MD5('Clinica123'), 'Medico',        NOW(), 1, 'juan.garcia@clinicaveterinaria.com'),
    ('lmartinez',   MD5('Clinica123'), 'Recepcionista', NOW(), 1, 'laura.martinez@clinicaveterinaria.com'),
    ('cgomez',      MD5('Clinica123'), 'Auxiliar',      NOW(), 1, 'carlos.gomez@clinicaveterinaria.com'),
    ('aherrera',    MD5('Clinica123'), 'Recepcionista', NOW(), 0, 'andrea.herrera@clinicaveterinaria.com'),
    ('dlopez',      MD5('Clinica123'), 'Auxiliar',      NOW(), 1, 'diego.lopez@clinicaveterinaria.com');

-- ---------------------------------------------------------------------
-- Clientes
-- ---------------------------------------------------------------------
INSERT IGNORE INTO cliente (tipo_documento, numero_documento, nombres, apellidos, fecha_nacimiento, genero, telefono, direccion, activo) VALUES
    ('CC', '1020304050', 'Ana María',    'Pérez Castro',     '1990-03-15', 'F', '3001234567', 'Calle 45 # 12-30, Bogotá',        1),
    ('CC', '79856412',   'Luis Alberto', 'Gómez Ruiz',       '1978-07-22', 'M', '3109876543', 'Carrera 7 # 80-15, Bogotá',       1),
    ('CC', '1032456789', 'Valentina',    'Rojas Mejía',      '1998-11-02', 'F', '3204567890', 'Av. Boyacá # 68-40, Bogotá',      1),
    ('CE', 'E4567321',   'Carlos',       'Fernández López',  '1985-01-30', 'M', '3156789012', 'Calle 100 # 15-20, Bogotá',       1),
    ('CC', '52987654',   'Sandra Milena','Ortiz Vargas',     '1982-05-18', 'F', '3012345678', 'Carrera 50 # 26-11, Bogotá',      1),
    ('CC', '1015478963', 'Santiago',     'Ramírez Torres',   '2000-09-09', 'M', '3187654321', 'Calle 13 # 34-56, Soacha',        1),
    ('TI', '1098765432', 'Camila',       'Herrera Suárez',   '2008-02-14', 'F', '3145678901', 'Transversal 20 # 5-60, Chía',     1),
    ('CC', '80123456',   'Jorge Enrique','Moreno Díaz',      '1975-12-01', 'M', '3176543210', 'Calle 80 # 90-12, Bogotá',        1),
    ('CC', '1023987456', 'Daniela',      'Castillo Peña',    '1995-06-25', 'F', '3023456789', 'Carrera 68 # 24-30, Bogotá',      1),
    ('PA', 'AB1234567',  'Michael',      'Smith',            '1988-04-10', 'M', '3134567890', 'Calle 93 # 11-27, Bogotá',        1),
    ('CC', '39765432',   'Gloria Inés',  'Salazar Ríos',     '1968-08-19', 'F', '3198765432', 'Carrera 15 # 45-80, Zipaquirá',   0),
    ('CC', '1001234987', 'Andrés Felipe','Navarro Quintero', '2001-10-05', 'M', '3051234567', 'Calle 26 # 69-76, Bogotá',        1);

-- ---------------------------------------------------------------------
-- Razas
-- ---------------------------------------------------------------------
INSERT IGNORE INTO raza (nombre, especie, fecha_creacion) VALUES
    ('Criollo',         'Perro',  NOW()),
    ('Labrador',        'Perro',  NOW()),
    ('Golden Retriever','Perro',  NOW()),
    ('Bulldog Francés', 'Perro',  NOW()),
    ('Pastor Alemán',   'Perro',  NOW()),
    ('Poodle',          'Perro',  NOW()),
    ('Schnauzer',       'Perro',  NOW()),
    ('Criollo',         'Gato',   NOW()),
    ('Persa',           'Gato',   NOW()),
    ('Siamés',          'Gato',   NOW()),
    ('Angora',          'Gato',   NOW()),
    ('Holland Lop',     'Conejo', NOW()),
    ('Periquito',       'Ave',    NOW());

-- ---------------------------------------------------------------------
-- Mascotas (cliente por numero de documento, raza por nombre + especie)
-- ---------------------------------------------------------------------
INSERT IGNORE INTO mascota (nombre_mascota, edad, fecha_registro, raza_id, cliente_id)
SELECT m.nombre, m.edad, NOW(), r.raza_id, c.cliente_id
FROM (
    SELECT 'Max'      AS nombre, 4  AS edad, 'Labrador'         AS raza, 'Perro'  AS especie, '1020304050' AS doc UNION ALL
    SELECT 'Luna',            2,  'Siamés',           'Gato',   '1020304050' UNION ALL
    SELECT 'Rocky',           6,  'Pastor Alemán',    'Perro',  '79856412'   UNION ALL
    SELECT 'Michi',           3,  'Criollo',          'Gato',   '1032456789' UNION ALL
    SELECT 'Toby',            1,  'Bulldog Francés',  'Perro',  '1032456789' UNION ALL
    SELECT 'Bella',           5,  'Golden Retriever', 'Perro',  'E4567321'   UNION ALL
    SELECT 'Nala',            7,  'Persa',            'Gato',   '52987654'   UNION ALL
    SELECT 'Coco',            2,  'Poodle',           'Perro',  '1015478963' UNION ALL
    SELECT 'Tambor',          1,  'Holland Lop',      'Conejo', '1098765432' UNION ALL
    SELECT 'Firulais',        9,  'Criollo',          'Perro',  '80123456'   UNION ALL
    SELECT 'Kiwi',            1,  'Periquito',        'Ave',    '1023987456' UNION ALL
    SELECT 'Simba',           4,  'Angora',           'Gato',   '1023987456' UNION ALL
    SELECT 'Zeus',            3,  'Schnauzer',        'Perro',  'AB1234567'  UNION ALL
    SELECT 'Canela',          11, 'Criollo',          'Perro',  '39765432'   UNION ALL
    SELECT 'Oreo',            2,  'Criollo',          'Gato',   '1001234987'
) m
JOIN cliente c ON c.numero_documento = m.doc
JOIN raza r ON r.nombre = m.raza AND r.especie = m.especie;

-- ---------------------------------------------------------------------
-- Especializaciones
-- ---------------------------------------------------------------------
INSERT IGNORE INTO especializacion (nombre, descripcion, codigo_especializacion) VALUES
    ('Medicina General', 'Consulta general y medicina preventiva',             'MG'),
    ('Cirugía',          'Procedimientos quirúrgicos',                         'CIR'),
    ('Dermatología',     'Enfermedades de la piel',                            'DER'),
    ('Odontología',      'Salud oral y limpiezas dentales',                    'ODO'),
    ('Cardiología',      'Diagnóstico y tratamiento de enfermedades cardíacas', 'CAR');

-- ---------------------------------------------------------------------
-- Medicos (especializacion por codigo)
-- ---------------------------------------------------------------------
INSERT IGNORE INTO medico (tipo_documento, numero_documento, nombres, apellidos, telefono, registro_profesional, especializacion_id)
SELECT d.tipo, d.doc, d.nombres, d.apellidos, d.tel, d.registro, e.id
FROM (
    SELECT 'CC' AS tipo, '52345678' AS doc, 'María Fernanda' AS nombres, 'Rodríguez Peña' AS apellidos, '3112223344' AS tel, 'MV-10234' AS registro, 'MG' AS esp UNION ALL
    SELECT 'CC', '80456789', 'Juan Carlos', 'García Molina',  '3123334455', 'MV-10567', 'CIR' UNION ALL
    SELECT 'CC', '1019876543', 'Paola Andrea', 'Silva Cárdenas', '3134445566', 'MV-11023', 'DER' UNION ALL
    SELECT 'CC', '79345612', 'Ricardo', 'Mendoza Ávila',  '3145556677', 'MV-09876', 'ODO' UNION ALL
    SELECT 'CE', 'E9876543', 'Lucía', 'Benítez Ramos', '3156667788', 'MV-11345', 'CAR'
) d
JOIN especializacion e ON e.codigo_especializacion = d.esp;
