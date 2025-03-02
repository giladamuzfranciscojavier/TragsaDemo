package controllers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import models.usuarios.Cliente;
import models.usuarios.ClienteProveedor;
import models.usuarios.Proveedor;
import models.usuarios.Usuario;

public class UsuarioController {

	public static boolean createUpdateUsuario(boolean cliente, boolean proveedor, Usuario usuario) {
		//Se omite la operación si no hay una conexión válida. También se omite si ninguno de los booleanos es verdadero.
		if(DBController.checkConnection()==null || (!cliente && !proveedor)) {
			return false;
		}
		
		
		try {
			PreparedStatement[] ps = new PreparedStatement[2];
			
			//Comprueba si ya existe el usuario (cliente y/o proveedor) y en dicho caso realiza una actualización de los datos
			Usuario u = UsuarioController.readUsuario(cliente,proveedor,usuario.getDNI());
			if(u!=null && u.equals(usuario)) {
				
				//No es necesario realizar comprobaciones adicionales, ya que si no hay filas que cumplan la condición simplemente no hace nada
				ps[0] = DBController.conn.prepareStatement("UPDATE cliente SET nombre=? Where(cliente_dni=?)");
				ps[1] = DBController.conn.prepareStatement("UPDATE proveedor SET nombre=? Where(proveedor_dni=?)");
				for(PreparedStatement p:ps) {
					p.setString(1, usuario.getNombre());
					p.setString(2, usuario.getDNI());
					p.executeUpdate();
				}
			}
			
			else {
				//Dependiendo de los parámetros indicados inserta un nuevo cliente y/o un nuevo proveedor
				if(cliente && readUsuario(true,false,usuario.getDNI())==null) {ps[0] = DBController.conn.prepareStatement("INSERT INTO cliente VALUES (?,?)");}
				if(proveedor && readUsuario(false,true,usuario.getDNI())==null) {ps[1] = DBController.conn.prepareStatement("INSERT INTO proveedor VALUES(?,?)");}			
				
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
	
	public static Usuario readUsuario(boolean cliente, boolean proveedor, String dni) {
		if(DBController.checkConnection()==null || (!cliente && !proveedor)) {
			return null;
		}
		
		try {		
			PreparedStatement ps;
			if(cliente) {
				if(proveedor) {
					//Si se marcan ambos booleanos solo muestran aquellos usuarios que sean clientes Y proveedores
					ps = DBController.conn.prepareStatement("SELECT * FROM cliente JOIN proveedor WHERE (cliente_dni=?)");
					ps.setString(1, dni);
					ResultSet rs = ps.executeQuery();
					if(rs.next()) {
						return new ClienteProveedor(rs.getString(1), rs.getString(2));
					}
				}
				else {
					ps = DBController.conn.prepareStatement("SELECT * FROM cliente WHERE (cliente_dni=?)");
					ps.setString(1, dni);
					ResultSet rs = ps.executeQuery();
					if(rs.next()) {
						return new Cliente(rs.getString(1), rs.getString(2));
					}
				}				
			}
			
			else if(proveedor) {
				ps = DBController.conn.prepareStatement("SELECT * FROM proveedor WHERE (proveedor_dni=?)");
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
	
	
	public static List<Usuario> readAllUsuarios(boolean cliente, boolean proveedor){
		if(DBController.checkConnection()==null || (!cliente && !proveedor)) {
			return null;
		}
		
		try {		
			Statement s;
			List<Usuario> list = new ArrayList<>(); 
			if(cliente) {
					if(proveedor) {
						//Si se marcan ambos booleanos solo muestran aquellos usuarios que sean clientes Y proveedores
						s = DBController.conn.createStatement();
						ResultSet rs = s.executeQuery("SELECT * FROM cliente JOIN proveedor");						
						while(rs.next()) {
							list.add(new ClienteProveedor(rs.getString(1), rs.getString(2)));
						}
					}
					else {
						s = DBController.conn.createStatement();
						ResultSet rs = s.executeQuery("SELECT * FROM cliente");
						while(rs.next()) {
							list.add(new Cliente(rs.getString(1), rs.getString(2)));
						}						
					}				
				}
				
				else if(proveedor) {
					s = DBController.conn.createStatement();
					ResultSet rs = s.executeQuery("SELECT * FROM proveedor");
					while(rs.next()) {
						list.add(new Proveedor(rs.getString(1), rs.getString(2)));						
					}
					
				}
			return list;
		}
		catch(SQLException e) {
			e.printStackTrace();			
		}
		return null;
	}

	//Solo borra a tipos de usuario marcados como parámetro (se puede borrar a un cliente-proveedor como cliente, proveedor o como ambos)
	public static boolean deleteUsuario(boolean cliente, boolean proveedor, String dni) {
		if(DBController.checkConnection()==null || (!cliente && !proveedor)) {
			return false;
		}
		
		try {
			PreparedStatement[] ps = new PreparedStatement[2]; 
			if(cliente) {ps[0] = DBController.conn.prepareStatement("DELETE FROM cliente WHERE (cliente_dni=?)");}
			if(proveedor) {ps[1] = DBController.conn.prepareStatement("DELETE FROM proveedor Where(proveedor_dni=?)");}	
			
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

	

}
