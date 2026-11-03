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



-- --- Sección de Inserción de Datos ---
