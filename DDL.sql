DROP DATABASE IF EXISTS java;

CREATE DATABASE java;

USE java;



CREATE TABLE `Oficio` (

  `idOficio` int(11) NOT NULL,

  `descripcion` varchar(45) DEFAULT NULL,

  PRIMARY KEY (`idOficio`)

);



CREATE TABLE `Usuario` (

  `idUsuario` int(11) NOT NULL AUTO_INCREMENT,

  `nombre` varchar(45) DEFAULT NULL,

  `apellidos` varchar(45) DEFAULT NULL,

  `Oficio_idOficio` int(11) NOT NULL,

  PRIMARY KEY (`idUsuario`),

  KEY `fk_Usuario_Oficio_idx` (`Oficio_idOficio`),

  CONSTRAINT `fk_Usuario_Oficio` FOREIGN KEY (`Oficio_idOficio`) REFERENCES `Oficio` (`idOficio`) ON DELETE NO ACTION ON UPDATE NO ACTION);

