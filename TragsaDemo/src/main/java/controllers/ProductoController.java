package controllers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import models.Producto;

public class ProductoController {
	

	public static boolean createUpdateProducto(Producto producto) {
		if(DBController.checkConnection()==null) {
			return false;
		}
		
		try {
			PreparedStatement ps;
			
			
			if(producto.getProducto_ID()>0 && ProductoController.readProducto(producto.getProducto_ID()).equals(producto)) {
				ps = DBController.conn.prepareStatement("UPDATE producto SET nombre=?, precio=?,cliente_dni=? WHERE producto_id=?");
				ps.setString(1, producto.getNombre());
				ps.setDouble(2, producto.getPrecio());
				ps.setString(3, producto.getCliente_dni()!=null?producto.getCliente_dni():null);
				ps.setInt(4, producto.getProducto_ID());
				ps.executeUpdate();
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
}
