package database;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbOperations {
	
	private static final String DRIVER = "jdbc:mysql://";
	public static final String URL = "localhost";
	public static final String USER = "root";
	public static final String PSWD = "system..";	
	private static Connection conn = null;
	
	
	//Conecta con la base de datos en la url con las credenciales indicadas
	public static boolean connect(String url, String user, String pswd) {
				
		try {			
			conn = DriverManager.getConnection(DRIVER+url, user, pswd);
			
			System.out.println("Exito al conectar con la base de datos");
			
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
	public static boolean checkConnection() {
		try {
			if(conn!=null && conn.isValid(0)) {
				return true;
			}
		} catch (SQLException e) {
			System.out.println("Error de conexión");
			e.printStackTrace();			
		}
		return false;
	}
	
	
	//Genera la base de datos y las tablas necesarias
	public static boolean generateDB(boolean test) {
		//Se omite si no hay una conexión válida
		if(!checkConnection()) {
			return false;
		}
		try {
			Statement st = conn.createStatement();
			String schemaName = test?"tragsademo_test":"tragsademo";
			st.executeUpdate("CREATE SCHEMA IF NOT EXISTS "+schemaName);
			st.executeUpdate("USE "+schemaName);
			st.executeUpdate("CREATE TABLE IF NOT EXISTS cliente(cliente_dni int NOT NULL AUTO_INCREMENT,"
					+ " nombre varchar(50),"
					+ " PRIMARY KEY(cliente_dni))");
			
			st.executeUpdate("CREATE TABLE IF NOT EXISTS proveedor("
					+ "proveedor_dni int NOT NULL AUTO_INCREMENT, "
					+ "nombre varchar(50), "
					+ "PRIMARY KEY(proveedor_dni))");
			
			st.executeUpdate("CREATE TABLE IF NOT EXISTS producto ("
					+ "producto_ID int NOT NULL AUTO_INCREMENT,"
					+ " nombre varchar(50) NOT NULL,"
					+ " precio double NOT NULL,"
					+ " proveedor_dni int NOT NULL,"
					+ " cliente_dni int,"
					+ " PRIMARY KEY (producto_id),"
					+ " CONSTRAINT FK_Cliente FOREIGN KEY (cliente_dni) REFERENCES Cliente(cliente_dni),"
					+ " CONSTRAINT FK_Proveedor FOREIGN KEY (proveedor_dni) REFERENCES Proveedor(proveedor_dni));");
		} catch (SQLException e) {			
			e.printStackTrace();
			return false;
		}		
		return true;
	}
}
