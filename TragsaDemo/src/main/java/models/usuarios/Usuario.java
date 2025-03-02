package models.usuarios;

import java.util.Objects;

//Clase que proporciona la estructura de todos los tipos de usuario
//Si bien en este caso podría funcionar sin necesidad de ser heredada seguir este 
//enfoque proporcionaría una mayor flexibilidad a la hora de implementar nuevas funcionalidades
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

	//La igualdad de usuarios se determina por su dni
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
