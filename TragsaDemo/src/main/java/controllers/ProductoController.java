package controllers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import models.Producto;

public class ProductoController {
	
	//Creación y actualización de producto
	public static boolean createUpdateProducto(Producto producto) {
		if(DBController.checkConnection()==null) {
			return false;
		}
		
		try {
			PreparedStatement ps;
			
			
			if(producto.getProducto_ID()>0 && ProductoController.readProducto(producto.getProducto_ID()).equals(producto)) {
				ps = DBController.conn.prepareStatement("UPDATE producto SET nombre=?, precio=?, proveedor_dni=?, cliente_dni=? WHERE producto_id=?");
				ps.setString(1, producto.getNombre());				
				ps.setDouble(2, producto.getPrecio());
				ps.setString(3, producto.getProveedor_dni());
				
				//Por alguna razón en lugar de ser un tipo null tiene el valor "null"
				if(producto.getCliente_dni()!=null && !producto.getCliente_dni().equals("null")) {
					ps.setString(4, producto.getCliente_dni());
				}
				else {
					ps.setNull(4, Types.VARCHAR);
				}
				ps.setInt(5, producto.getProducto_ID());
				ps.executeUpdate();
				
				
				if(producto.getCliente_dni()!=null) {
					ps.setString(1, producto.getCliente_dni());
				}
				
			}
			
			else {
				ps = DBController.conn.prepareStatement("INSERT INTO producto (nombre, precio, proveedor_dni, cliente_dni) VALUES (?,?,?,?)"); 
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

	//Lectura de un producto en base a la id proporcionada
	public static Producto readProducto(int id) {
		if(DBController.checkConnection()==null) {
			return null;
		}
		
		try {
			PreparedStatement ps = DBController.conn.prepareStatement("SELECT * FROM producto WHERE (producto_id=?)");
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
	
	//Lectura de todos los productos
	public static List<Producto> readAllProductos(){
		if(DBController.checkConnection()==null) {
			return null;
		}
		
		try {		
			Statement s;
			List<Producto> list = new ArrayList<>(); 
			s = DBController.conn.createStatement();
			ResultSet rs = s.executeQuery("SELECT * FROM producto");						
			while(rs.next()) {
				list.add(new Producto(rs.getInt(1), rs.getString(2), rs.getDouble(3), rs.getString(4),rs.getString(5)));
			}
			return list;
		}
		catch(SQLException e) {
			e.printStackTrace();			
		}
		return null;
	}
	
	//Devuelve todos los productos sin un cliente asignado
	public static List<Producto> readAllProductosSinComprar(){
		if(DBController.checkConnection()==null) {
			return null;
		}
		
		try {		
			Statement s;
			List<Producto> list = new ArrayList<>(); 
			s = DBController.conn.createStatement();
			ResultSet rs = s.executeQuery("SELECT * FROM producto Where(cliente_dni IS null)");						
			while(rs.next()) {
				list.add(new Producto(rs.getInt(1), rs.getString(2), rs.getDouble(3), rs.getString(4),rs.getString(5)));
			}
			return list;
		}
		catch(SQLException e) {
			e.printStackTrace();			
		}
		return null;
	}

	

	//Elimina el producto indicado de la base de datos
	public static boolean deleteProducto(int id) {
		if(DBController.checkConnection()==null) {
			return false;
		}
		
		try {			
			PreparedStatement ps = DBController.conn.prepareStatement("DELETE FROM producto WHERE (producto_id=?)");
			ps.setInt(1,id);
			ps.executeUpdate();
			
		} catch (SQLException e) {			
			e.printStackTrace();
			return false;
		}
		return true;
		
	}
	
	
	//Elimina el cliente asociado al producto indicado
	public static boolean restablecerCompra(int id) {
		if(DBController.checkConnection()==null) {
			return false;
		}
		
		try {			
			PreparedStatement ps = DBController.conn.prepareStatement("UPDATE producto SET cliente_dni=NULL WHERE (producto_id=?)");
			ps.setInt(1,id);
			ps.executeUpdate();
			
		} catch (SQLException e) {			
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	
}
