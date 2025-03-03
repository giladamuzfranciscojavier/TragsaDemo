CREATE SCHEMA IF NOT EXISTS tragsademo;
USE tragsademo;

CREATE TABLE IF NOT EXISTS cliente(cliente_dni varchar(9) NOT NULL, nombre varchar(50) NOT NULL, PRIMARY KEY(cliente_dni));
CREATE TABLE IF NOT EXISTS proveedor(proveedor_dni varchar(9) NOT NULL, nombre varchar(50) NOT NULL, PRIMARY KEY(proveedor_dni));

CREATE TABLE IF NOT EXISTS producto (
	producto_ID int NOT NULL AUTO_INCREMENT, 
	nombre varchar(50) NOT NULL,
	precio DOUBLE NOT NULL, 
	proveedor_dni varchar(9) NOT NULL,
	cliente_dni varchar(9),
	PRIMARY KEY (producto_id), 
	CONSTRAINT FK_Cliente FOREIGN KEY (cliente_dni) REFERENCES Cliente(cliente_dni) ON DELETE SET NULL,
	CONSTRAINT FK_Proveedor FOREIGN KEY (proveedor_dni) REFERENCES Proveedor(proveedor_dni) ON DELETE CASCADE
);
