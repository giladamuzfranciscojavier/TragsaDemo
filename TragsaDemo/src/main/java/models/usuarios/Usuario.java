package models.usuarios;

import java.util.Objects;

public abstract class Usuario {
	private String DNI;
	private String nombre;
	
	public Usuario(String DNI, String nombre) {
		this.DNI=DNI;
		this.nombre=nombre;
	}
	
	public String getDNI() {
		return DNI;
	}
	public String getNombre() {
		return nombre;
	}
	public void setName(String nombre) {
		this.nombre=nombre;
	}

	@Override
	public int hashCode() {
		return Objects.hash(DNI);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Usuario other = (Usuario) obj;
		return Objects.equals(DNI, other.DNI);
	}
	
	
}
