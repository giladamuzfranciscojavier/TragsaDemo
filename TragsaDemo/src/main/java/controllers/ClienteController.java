package controllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import models.Producto;

public class ClienteController {
	
	public static List<Producto> listProductosComprados(String dni) {
		Connection conn = DBController.checkConnection();
		if(conn==null) {
			return null;
		}
		List<Producto> list = new ArrayList<>();
		try {
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM producto WHERE (cliente_dni=?)");
			ps.setString(1, dni);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				list.add(new Producto(rs.getInt(1), rs.getString(2), rs.getDouble(3), rs.getString(4),rs.getString(5)));
			}		
			return list;
			
		} catch (SQLException e) {			
			e.printStackTrace();
		}	
		return null;
	}
	
	
	public static boolean comprarProducto(String dni, int producto_id) {
		Connection conn = DBController.checkConnection();
		if(conn==null) {
			return false;
		}
		try {
			PreparedStatement ps = conn.prepareStatement("UPDATE producto SET cliente_dni=? WHERE (producto_id=?)");
			ps.setString(1, dni);
			ps.setInt(2, producto_id);
			ps.executeUpdate();			
			
			return true;
		} catch (SQLException e) {			
			e.printStackTrace();
		}	
		return false;
	}
	
}
