package models;

import java.util.Objects;


//Clase que representa a los productos
public class Producto {
	private int producto_ID;
	
	private String nombre;
	private double precio;
	private String proveedor_dni;
	private String cliente_dni;
	
	
	/* Constructores para los siguientes casos:
	 * 
	 * 1. No se tiene id ni cliente 
	 * 2. No se tiene id, pero sí cliente
	 * 3. Se tiene id, pero no cliente
	 * 4. Se tienen todos los parámetros
	 * 
	 * */
	
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

	
	//La igualdad entre productos viene determinada por su id.
	//Esto claramente dificulta las pruebas, ya que la id la determina 
	//la base de datos, pero no se dispone de mayor información que los diferencie
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
