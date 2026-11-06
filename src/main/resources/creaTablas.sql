/*
  Script de creación de la base de datos de MetroPet Clinic
*/
drop database if exists metropet;
drop user if exists usuario_metropet;

-- Creación del esquema
create database metropet
  default character set utf8mb4
  default collate utf8mb4_unicode_ci;

-- Creación del usuario de la aplicación y sus permisos
create user 'usuario_metropet'@'%' identified by 'Metropet_Clave.';
grant select, insert, update, delete on metropet.* to 'usuario_metropet'@'%';
flush privileges;

use metropet;

-- --- Sección de Creación de Tablas ---
 
--- TABLA DE ROL
create table rol (
  id_rol INT NOT NULL AUTO_INCREMENT,
  nombre_rol VARCHAR(30) NOT NULL,
  descripcion VARCHAR(100),
  PRIMARY KEY (id_rol),
  UNIQUE (nombre_rol)
) ENGINE = InnoDB;
 
--- TABLA DE USUARIO
create table usuario (
  id_usuario INT NOT NULL AUTO_INCREMENT,
  correo VARCHAR(100) NOT NULL,
  contrasena VARCHAR(512) NOT NULL,
  estado BOOLEAN NOT NULL DEFAULT TRUE,
  id_rol INT NOT NULL,
  PRIMARY KEY (id_usuario),
  UNIQUE (correo),
  FOREIGN KEY fk_usuario_rol (id_rol) REFERENCES rol(id_rol)
) ENGINE = InnoDB;
 
--- TABLA CLIENTE
create table cliente (
  id_cliente INT NOT NULL AUTO_INCREMENT,
  identificacion VARCHAR(20) NOT NULL,
  nombre VARCHAR(100) NOT NULL,
  telefono VARCHAR(20),
  correo VARCHAR(100),
  direccion VARCHAR(255),
  id_usuario INT NULL,
  PRIMARY KEY (id_cliente),
  UNIQUE (identificacion),
  FOREIGN KEY fk_cliente_usuario (id_usuario) REFERENCES usuario(id_usuario)
) ENGINE = InnoDB;
 
--- TABLA MASCOTA
create table mascota (
  id_mascota INT NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(50) NOT NULL,
  especie VARCHAR(50) NOT NULL,
  raza VARCHAR(50),
  fecha_nacimiento DATE,
  id_cliente INT NOT NULL,
  PRIMARY KEY (id_mascota),
  FOREIGN KEY fk_mascota_cliente (id_cliente) REFERENCES cliente(id_cliente),
  INDEX ndx_mascota_cliente (id_cliente)
) ENGINE = InnoDB;
 
--- TABLA SERVICIO
create table servicio (
  id_servicio INT NOT NULL AUTO_INCREMENT,
  nombre_servicio VARCHAR(100) NOT NULL,
  descripcion VARCHAR(255),
  precio DECIMAL(12,2) NOT NULL,
  estado BOOLEAN NOT NULL DEFAULT TRUE,
  PRIMARY KEY (id_servicio),
  UNIQUE (nombre_servicio)
) ENGINE = InnoDB;
 
--- TABLA VETERINARIO
create table veterinario (
  id_veterinario INT NOT NULL AUTO_INCREMENT,
  identificacion VARCHAR(20) NOT NULL,
  nombre VARCHAR(100) NOT NULL,
  especialidad VARCHAR(100),
  id_usuario INT NULL,
  PRIMARY KEY (id_veterinario),
  UNIQUE (identificacion),
  FOREIGN KEY fk_veterinario_usuario (id_usuario) REFERENCES usuario(id_usuario)
) ENGINE = InnoDB;
 
--- TABLA CITA
create table cita (
  id_cita INT NOT NULL AUTO_INCREMENT,
  fecha DATE NOT NULL,
  hora TIME NOT NULL,
  motivo TEXT,
  estado_cita VARCHAR(20) NOT NULL DEFAULT 'PROGRAMADA', -- PROGRAMADA, CANCELADA o COMPLETADA
  id_mascota INT NOT NULL,
  id_veterinario INT NOT NULL,
  id_servicio INT NOT NULL,
  PRIMARY KEY (id_cita),
  FOREIGN KEY fk_cita_mascota (id_mascota) REFERENCES mascota(id_mascota),
  FOREIGN KEY fk_cita_veterinario (id_veterinario) REFERENCES veterinario(id_veterinario),
  FOREIGN KEY fk_cita_servicio (id_servicio) REFERENCES servicio(id_servicio),
  INDEX ndx_cita_agenda (id_veterinario, fecha, hora)
) ENGINE = InnoDB;
 
--- TABLA DE EXPEDIENTE MEDICO
create table expediente_medico (
  id_expediente INT NOT NULL AUTO_INCREMENT,
  fecha_registro DATE NOT NULL,
  observaciones TEXT,
  sintomas TEXT,
  diagnostico TEXT,
  tratamiento TEXT,
  id_mascota INT NOT NULL,
  id_veterinario INT NOT NULL,
  PRIMARY KEY (id_expediente),
  FOREIGN KEY fk_expediente_mascota (id_mascota) REFERENCES mascota(id_mascota),
  FOREIGN KEY fk_expediente_veterinario (id_veterinario) REFERENCES veterinario(id_veterinario),
  INDEX ndx_expediente_mascota (id_mascota)
) ENGINE = InnoDB;
 
--- TABLA DE VACUNA
create table vacuna (
  id_vacuna INT NOT NULL AUTO_INCREMENT,
  nombre_vacuna VARCHAR(100) NOT NULL,
  fecha_aplicacion DATE NOT NULL,
  proxima_dosis DATE,
  id_expediente INT NOT NULL,
  PRIMARY KEY (id_vacuna),
  FOREIGN KEY fk_vacuna_expediente (id_expediente) REFERENCES expediente_medico(id_expediente)
) ENGINE = InnoDB;
 
--- TABLA DE FACTURA
create table factura (
  id_factura INT NOT NULL AUTO_INCREMENT,
  numero_factura VARCHAR(20) NOT NULL,
  fecha_emision DATE NOT NULL,
  monto_total DECIMAL(12,2) NOT NULL,
  metodo_pago VARCHAR(30) NOT NULL,
  id_cliente INT NOT NULL,
  id_cita INT NOT NULL,
  PRIMARY KEY (id_factura),
  UNIQUE (numero_factura),
  FOREIGN KEY fk_factura_cliente (id_cliente) REFERENCES cliente(id_cliente),
  FOREIGN KEY fk_factura_cita (id_cita) REFERENCES cita(id_cita)
) ENGINE = InnoDB;
 
-- --- Sección de Inserción de Datos ---

--- INSERCION DE ROLES
insert into rol (nombre_rol, descripcion) values
  ('ROLE_ADMIN', 'Administrador'),
  ('ROLE_RECEPCIONISTA', 'Recepcionista'),
  ('ROLE_VETERINARIO', 'Veterinario'),
  ('ROLE_CLIENTE', 'Cliente');
 
--- INSERCION DE USUARIOS
insert into usuario (correo, contrasena, estado, id_rol) values
  ('admin@metropet.com',      '$2a$10$nCYAwz53.NLZjazam7IEs.RqytLvfXO4HzUOMDD829kSQvzwJWRAS', true, 1),
  ('recepcion@metropet.com',  '$2a$10$nCYAwz53.NLZjazam7IEs.RqytLvfXO4HzUOMDD829kSQvzwJWRAS', true, 2),
  ('laura.mora@metropet.com', '$2a$10$nCYAwz53.NLZjazam7IEs.RqytLvfXO4HzUOMDD829kSQvzwJWRAS', true, 3),
  ('carlos.jimenez@metropet.com', '$2a$10$nCYAwz53.NLZjazam7IEs.RqytLvfXO4HzUOMDD829kSQvzwJWRAS', true, 3),
  ('juan.chacon@correo.com',  '$2a$10$nCYAwz53.NLZjazam7IEs.RqytLvfXO4HzUOMDD829kSQvzwJWRAS', true, 4),
  ('maria.solis@correo.com',  '$2a$10$nCYAwz53.NLZjazam7IEs.RqytLvfXO4HzUOMDD829kSQvzwJWRAS', true, 4);
 
--- INSERCION DE CLIENTES
insert into cliente (identificacion, nombre, telefono, correo, direccion, id_usuario) values
  ('1-1111-1111', 'Juan Chacón Mora',   '8888-1111', 'juan.chacon@correo.com', 'San Pedro, San José', 5),
  ('2-2222-2222', 'María Solís Vargas', '8888-2222', 'maria.solis@correo.com', 'Curridabat, San José', 6),
  ('3-3333-3333', 'Pedro Alvarado Rojas', '8888-3333', 'pedro.alvarado@correo.com', 'Tres Ríos, Cartago', null);
 
--- INSERCION DE MASCOTAS
insert into mascota (nombre, especie, raza, fecha_nacimiento, id_cliente) values
  ('Max',   'Perro',  'Labrador',      '2020-03-15', 1),
  ('Luna',  'Gato',   'Siamés',        '2021-07-02', 1),
  ('Rocky', 'Perro',  'Pastor Alemán', '2019-11-20', 2),
  ('Mía',   'Gato',   'Criollo',       '2022-01-10', 2),
  ('Coco',  'Conejo', 'Enano',         '2023-05-05', 3);
 
--- INSERCION DE SERVICIOS
insert into servicio (nombre_servicio, descripcion, precio, estado) values
  ('Consulta general',   'Revisión médica general de la mascota', 10000, true),
  ('Vacunación',         'Aplicación de vacunas según el esquema', 12000, true),
  ('Baño y corte',       'Servicio de estética y cuidado del pelaje', 15000, true),
  ('Desparasitación',    'Tratamiento contra parásitos internos y externos', 8000, true),
  ('Limpieza dental',    'Profilaxis dental bajo supervisión veterinaria', 25000, true),
  ('Cirugía menor',      'Procedimientos quirúrgicos ambulatorios', 60000, true);
 
--- INSERCION DE VETERINARIOS
insert into veterinario (identificacion, nombre, especialidad, id_usuario) values
  ('4-4444-4444', 'Dra. Laura Mora',     'Medicina general', 3),
  ('5-5555-5555', 'Dr. Carlos Jiménez',  'Cirugía',          4);
 
--- INSERCION DE CITAS
insert into cita (fecha, hora, motivo, estado_cita, id_mascota, id_veterinario, id_servicio) values
  (curdate(), '08:00', 'Control anual',        'PROGRAMADA', 1, 1, 1),
  (curdate(), '09:30', 'Vacuna anual',         'PROGRAMADA', 2, 1, 2),
  (curdate(), '10:00', 'Cojea de una pata',    'CANCELADA',  3, 2, 1),
  (curdate(), '14:00', 'Desparasitación',      'PROGRAMADA', 4, 2, 4),
  (date_add(curdate(), interval 1 day), '08:30', 'Revisión de rutina', 'PROGRAMADA', 5, 1, 1),
  (date_sub(curdate(), interval 7 day), '11:00', 'Chequeo general',    'COMPLETADA', 1, 1, 1);
 
--- INSERCION DE EXPEDIENTES MEDICOS
insert into expediente_medico (fecha_registro, observaciones, sintomas, diagnostico, tratamiento, id_mascota, id_veterinario) values
  (date_sub(curdate(), interval 7 day), 'Paciente alerta y activo', 'Ninguno', 'Sano', 'Mantener dieta y ejercicio', 1, 1),
  (date_sub(curdate(), interval 30 day), 'Pérdida de apetito', 'Vómito y decaimiento', 'Gastritis leve', 'Dieta blanda y protector gástrico', 2, 1);
 
--- INSERCION DE VACUNAS
insert into vacuna (nombre_vacuna, fecha_aplicacion, proxima_dosis, id_expediente) values
  ('Séxtuple',     date_sub(curdate(), interval 7 day),  date_add(curdate(), interval 358 day), 1),
  ('Antirrábica',  date_sub(curdate(), interval 30 day), date_add(curdate(), interval 335 day), 2);
 
--- INSERCION DE FACTURAS
insert into factura (numero_factura, fecha_emision, monto_total, metodo_pago, id_cliente, id_cita) values
  ('FAC-0001', date_sub(curdate(), interval 7 day), 10000, 'Efectivo', 1, 6);
 