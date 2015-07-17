/*
    Script para MySQL.
    Script de creación de la BD.
 */

CREATE DATABASE IF NOT EXISTS ZombieLand;

USE ZombieLand;

DROP TABLE IF EXISTS Jugador;
    
CREATE TABLE Jugador (
    usr VARCHAR(25) NOT NULL,
    pass VARCHAR(20) NOT NULL,
    pregunta_secreta VARCHAR(40) NOT NULL,
    respuesta_secreta VARCHAR(30) NOT NULL,
    puntaje INT NULL,
    partidas_jugadas INT NULL,
    partidas_ganadas INT NULL
);
ALTER TABLE Jugador ADD CONSTRAINT pk_usr PRIMARY KEY (usr);