package models;

import java.util.Objects;

public class Producto {
	private int producto_ID;
	
	private String nombre;
	private double precio;
	private String proveedor_dni;
	private String cliente_dni;
	
	public Producto(String nombre, double precio, String proveedor_dni) {
		this.nombre=nombre;
		this.precio=precio;
		this.proveedor_dni=proveedor_dni;
	}	
	
	public Producto(String nombre, double precio, String proveedor_dni, String cliente_dni) {
		this.nombre=nombre;
		this.precio=precio;
		this.proveedor_dni=proveedor_dni;
		this.cliente_dni = cliente_dni;		
	}	
	
	
	public Producto(int producto_ID,String nombre, double precio, String proveedor_dni) {
		this.producto_ID=producto_ID;
		this.nombre=nombre;
		this.precio=precio;
		this.proveedor_dni=proveedor_dni;	
	}
	
	public Producto(int producto_ID, String nombre, double precio, String proveedor_dni, String cliente_dni) {
		this.producto_ID=producto_ID;
		this.nombre=nombre;
		this.precio=precio;
		this.proveedor_dni=proveedor_dni;
		this.cliente_dni = cliente_dni;		
	}

	public int getProducto_ID() {
		return producto_ID;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public double getPrecio() {
		return precio;
	}

	public void setPrecio(double precio) {
		this.precio = precio;
	}

	public String getCliente_dni() {
		return cliente_dni;
	}

	public void setCliente_dni(String cliente_dni) {
		this.cliente_dni = cliente_dni;
	}

	public String getProveedor_dni() {
		return proveedor_dni;
	}

	@Override
	public int hashCode() {
		return Objects.hash(producto_ID);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Producto other = (Producto) obj;
		return producto_ID == other.producto_ID;
	}
	
	

	
}
