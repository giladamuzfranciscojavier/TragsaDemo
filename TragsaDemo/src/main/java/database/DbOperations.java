package database;

import java.sql.Statement;

import models.usuarios.*;
import models.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
	
	
	public static boolean createUpdateUsuario(boolean cliente, boolean proveedor, Usuario usuario) {
		//Se omite la operación si no hay una conexión válida. También se omite si ninguno de los booleanos es verdadero.
		if(!checkConnection() || (!cliente && !proveedor)) {
			return false;
		}
		
		
		try {
			PreparedStatement[] ps = new PreparedStatement[2];
			
			//Comprueba si ya existe el usuario (cliente y/o proveedor) y en dicho caso realiza una actualización de los datos
			Usuario u = readUsuario(cliente,proveedor,usuario.getDNI());
			if(u!=null && u.equals(usuario)) {
				
				//No es necesario realizar comprobaciones adicionales, ya que si no hay filas que cumplan la condición simplemente no hace nada
				ps[0] = conn.prepareStatement("UPDATE cliente SET nombre=? Where(cliente_dni=?)");
				ps[1] = conn.prepareStatement("UPDATE proveedor SET nombre=? Where(proveedor_dni=?)");
				for(PreparedStatement p:ps) {
					p.setString(1, usuario.getNombre());
					p.setString(2, usuario.getDNI());
					p.executeUpdate();
				}
			}
			
			else {
				//Dependiendo de los parámetros indicados inserta un nuevo cliente y/o un nuevo proveedor
				if(cliente) {ps[0] = conn.prepareStatement("INSERT INTO cliente VALUES (?,?)");}
				if(proveedor) {ps[1] = conn.prepareStatement("INSERT INTO proveedor VALUES(?,?)");}			
				
				for(PreparedStatement p : ps) {
					if(p==null) {
						continue;
					}
					p.setString(1, usuario.getDNI());
					p.setString(2, usuario.getNombre());
					p.executeUpdate();
				}
			}			
			
			
		} catch (SQLException e) {			
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
		
	public static boolean createUpdateProducto(Producto producto) {
		if(!checkConnection()) {
			return false;
		}
		
		try {
			PreparedStatement ps;
			
			
			if(producto.getProducto_ID()>0 && readProducto(producto.getProducto_ID()).equals(producto)) {
				ps = conn.prepareStatement("UPDATE producto SET nombre=?, precio=?,cliente_dni=? WHERE producto_id=?");
				ps.setString(1, producto.getNombre());
				ps.setDouble(2, producto.getPrecio());
				ps.setString(3, producto.getCliente_dni()!=null?producto.getCliente_dni():null);
				ps.setInt(4, producto.getProducto_ID());
				ps.executeUpdate();
			}
			
			else {
				ps = conn.prepareStatement("INSERT INTO producto (nombre, precio, proveedor_dni, cliente_dni) VALUES (?,?,?,?)"); 
				ps.setString(1, producto.getNombre());
				ps.setDouble(2, producto.getPrecio());
				ps.setString(3, producto.getProveedor_dni());
				ps.setString(4, producto.getCliente_dni()!=null?producto.getCliente_dni():null);
				ps.executeUpdate();
			}
			
			
		} catch (SQLException e) {			
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	
	public static Usuario readUsuario(boolean cliente, boolean proveedor, String dni) {
		if(!checkConnection() || (!cliente && !proveedor)) {
			return null;
		}
		
		try {		
			PreparedStatement ps;
			if(cliente) {
				if(proveedor) {
					//Si se marcan ambos booleanos solo muestran aquellos usuarios que sean clientes Y proveedores
					ps = conn.prepareStatement("SELECT * FROM cliente JOIN proveedor WHERE (cliente_dni=?)");
					ps.setString(1, dni);
					ResultSet rs = ps.executeQuery();
					if(rs.next()) {
						return new ClienteProveedor(rs.getString(1), rs.getString(2));
					}
				}
				else {
					ps = conn.prepareStatement("SELECT * FROM cliente WHERE (cliente_dni=?)");
					ps.setString(1, dni);
					ResultSet rs = ps.executeQuery();
					if(rs.next()) {
						return new Cliente(rs.getString(1), rs.getString(2));
					}
				}				
			}
			
			else if(proveedor) {
				ps = conn.prepareStatement("SELECT * FROM proveedor WHERE (proveedor_dni=?)");
				ps.setString(1, dni);
				ResultSet rs = ps.executeQuery();
				if(rs.next()) {
					return new Proveedor(rs.getString(1), rs.getString(2));
				}
			}
		}
		catch(SQLException e) {
			e.printStackTrace();			
		}
		return null;
	}
	
	public static Producto readProducto(int id) {
		if(!checkConnection()) {
			return null;
		}
		
		try {
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM producto WHERE (producto_id=?)");
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				return new Producto(id, rs.getString(2), rs.getDouble(3), rs.getString(4), rs.getString(5));				
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//Solo borra a tipos de usuario marcados como parámetro (se puede borrar a un cliente-proveedor como cliente, proveedor o como ambos)
	public static boolean deleteUsuario(boolean cliente, boolean proveedor, String dni) {
		if(!checkConnection() || (!cliente && !proveedor)) {
			return false;
		}
		
		try {
			PreparedStatement[] ps = new PreparedStatement[2]; 
			if(cliente) {ps[0] = conn.prepareStatement("DELETE FROM cliente WHERE (cliente_dni=?)");}
			if(proveedor) {ps[1] = conn.prepareStatement("DELETE FROM proveedor Where(proveedor_dni=?)");}	
			
			for(PreparedStatement p : ps) {
				if(p==null) {
					continue;
				}
				p.setString(1, dni);
				p.executeUpdate();
			}
			
			
		} catch (SQLException e) {			
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	
	public static boolean deleteProducto(int id) {
		if(!checkConnection()) {
			return false;
		}
		
		try {			
			PreparedStatement ps = conn.prepareStatement("DELETE FROM producto WHERE (producto_id=?)");
			ps.setInt(1,id);
			ps.executeUpdate();
			
		} catch (SQLException e) {			
			e.printStackTrace();
			return false;
		}
		return true;
		
	}
	
	
	
	//Genera la base de datos y las tablas necesarias
	public static boolean generateDB(boolean test) {
		if(!checkConnection()) {
			return false;
		}
		try {
			Statement st = conn.createStatement();
			String schemaName = test?"tragsademo_test":"tragsademo";
			st.executeUpdate("CREATE SCHEMA IF NOT EXISTS "+schemaName);
			st.executeUpdate("USE "+schemaName);
			st.executeUpdate("CREATE TABLE IF NOT EXISTS cliente(cliente_dni varchar(9) NOT NULL,"
					+ " nombre varchar(50),"
					+ " PRIMARY KEY(cliente_dni))");
			
			System.out.println("Creada tabla cliente");
			
			st.executeUpdate("CREATE TABLE IF NOT EXISTS proveedor("
					+ "proveedor_dni varchar(9) NOT NULL, "
					+ "nombre varchar(50), "
					+ "PRIMARY KEY(proveedor_dni))");
			
			System.out.println("Creada tabla proveedor");
			
			st.executeUpdate("CREATE TABLE IF NOT EXISTS producto ("
					+ "producto_ID int NOT NULL AUTO_INCREMENT,"
					+ " nombre varchar(50) NOT NULL,"
					+ " precio double NOT NULL,"
					+ " proveedor_dni varchar(9) NOT NULL,"
					+ " cliente_dni varchar(9),"
					+ " PRIMARY KEY (producto_id),"
					+ " CONSTRAINT FK_Cliente FOREIGN KEY (cliente_dni) REFERENCES cliente(cliente_dni),"
					+ " CONSTRAINT FK_Proveedor FOREIGN KEY (proveedor_dni) REFERENCES proveedor(proveedor_dni) ON DELETE CASCADE);");
			System.out.println("Creadas todas las tablas");
		} catch (SQLException e) {			
			e.printStackTrace();
			return false;
		}		
		return true;
	}
	
	public static boolean dropTestDB() {
	
		if(!checkConnection()) {
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
