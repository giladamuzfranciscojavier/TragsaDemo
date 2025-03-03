package controllers;

import java.sql.Statement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBController {
	
	private static final String DRIVER = "jdbc:mysql://";
	public static final String URL = "localhost";
	public static final String USER = "root";
	public static final String PSWD = "system..";	
	static Connection conn = null;
	
	
	//Conecta con la base de datos en la url con las credenciales indicadas
	public static boolean connect(String url, String user, String pswd, boolean test) {
				
		try {			
			conn = DriverManager.getConnection(DRIVER+url, user, pswd);			
			System.out.println("Exito al conectar con la base de datos");
			generateDB(test);
			
		}
		catch(Exception ex) {
			ex.printStackTrace();
			conn=null;
			System.out.println("Fallo al conectar con la base de datos");
			return false;
		}
		
		return true;
	}
	
	//Comprueba si existe una conexión válida
	public static Connection checkConnection() {
		try {
			if(conn!=null && conn.isValid(0)) {
				return conn;
			}
		} catch (SQLException e) {
			System.out.println("Error de conexión");
			e.printStackTrace();			
		}
		return null;
	}
	
	
	//Genera la base de datos y las tablas necesarias
	public static boolean generateDB(boolean test) {
		if(checkConnection()==null) {
			return false;
		}
		try {
			Statement st = conn.createStatement();
			String schemaName = test?"tragsademo_test":"tragsademo";
			st.executeUpdate("CREATE SCHEMA IF NOT EXISTS "+schemaName);
			st.executeUpdate("USE "+schemaName);
			st.executeUpdate("CREATE TABLE IF NOT EXISTS cliente(cliente_dni varchar(9) NOT NULL,"
					+ " nombre varchar(50) NOT NULL,"
					+ " PRIMARY KEY(cliente_dni))");
			
			System.out.println("Creada tabla cliente");
			
			st.executeUpdate("CREATE TABLE IF NOT EXISTS proveedor("
					+ "proveedor_dni varchar(9) NOT NULL, "
					+ "nombre varchar(50) NOT NULL, "
					+ "PRIMARY KEY(proveedor_dni))");
			
			System.out.println("Creada tabla proveedor");
			
			st.executeUpdate("CREATE TABLE IF NOT EXISTS producto ("
					+ "producto_ID int NOT NULL AUTO_INCREMENT,"
					+ " nombre varchar(50) NOT NULL,"
					+ " precio double NOT NULL,"
					+ " proveedor_dni varchar(9) NOT NULL,"
					+ " cliente_dni varchar(9),"
					+ " PRIMARY KEY (producto_id),"
					+ " CONSTRAINT FK_Cliente FOREIGN KEY (cliente_dni) REFERENCES cliente(cliente_dni) ON DELETE SET NULL,"
					+ " CONSTRAINT FK_Proveedor FOREIGN KEY (proveedor_dni) REFERENCES proveedor(proveedor_dni) ON DELETE CASCADE);");
			System.out.println("Creadas todas las tablas");
		} catch (SQLException e) {			
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public static boolean dropTestDB() {
	
		if(checkConnection()==null) {
			return false;
		}
		try {
			conn.createStatement().executeUpdate("DROP SCHEMA tragsademo_test");
		} catch (SQLException e) {
			e.printStackTrace();
			return false;			
		}
		return true;
	}
	
}
