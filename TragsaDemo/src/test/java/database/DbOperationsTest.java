package database;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import models.usuarios.*;
import models.*;

class DbOperationsTest {

	static Connection conn;
	
	
	//Además de preparar la base de datos de pruebas, comprueba que los métodos de conexión y generado de la base de datos funcionan correctamente
	@BeforeAll
	static void setupDB() {	
		assertDoesNotThrow(() -> {			
			return DbOperations.connect("127.0.0.1:3306", DbOperations.USER, DbOperations.PSWD);
		}, "Error de Conexión");
		
		assertTrue(DbOperations.generateDB(true));
		
	}
	
	@Test
	void clienteCRUDTest() {
		Cliente c = new Cliente("12345678A","Fulano");
		assertAll(
			"Operaciones CRUD Cliente",
			()-> assertTrue(DbOperations.createUpdateUsuario(true, false, c)),
			()-> assertTrue(DbOperations.readUsuario(true, false, c.getDNI()).equals(c)),
			()-> assertTrue(DbOperations.createUpdateUsuario(true, false, new Cliente(c.getDNI(), "Mengano"))),
			()-> assertTrue(DbOperations.readUsuario(true, false, c.getDNI()).getNombre().equals("Mengano")),
			()-> assertTrue(DbOperations.deleteUsuario(true, false, c.getDNI())),
			()-> assertTrue(DbOperations.readUsuario(true, false, c.getDNI())==null)		
		);		
	}


	@Test
	void ProductoCRUDTest() {
		Proveedor p = new Proveedor("87654321Z", "Mengano");
		assertTrue(DbOperations.createUpdateUsuario(false, true, p));
		Producto pr = new Producto("Cosa", 99.99, p.getDNI());
		assertAll(
				"Operaciones CRUD Producto",
				()-> assertTrue(DbOperations.createUpdateProducto(pr)),
				()-> assertTrue(DbOperations.readProducto(1).getNombre().equals(pr.getNombre())),
				()-> assertTrue(DbOperations.createUpdateProducto(new Producto(1, "Otra Cosa", 9.99, pr.getProveedor_dni()))),
				()-> assertTrue(DbOperations.readProducto(1).getNombre().equals("Otra Cosa")),
				()-> assertTrue(DbOperations.deleteProducto(1)),
				()-> assertTrue(DbOperations.readProducto(1)==null)
		);
	}
	
	
	@Test
	void ClienteProveedorCRUDTest() {
		ClienteProveedor cp = new ClienteProveedor("13579246A","Cetano");
		assertAll(
				"Operaciones CRUD Cliente-Proveedor",
				() -> assertTrue(DbOperations.createUpdateUsuario(true, true, cp)),
				()-> assertTrue(DbOperations.readUsuario(true, true, cp.getDNI()).equals(cp)),
				()-> assertTrue(DbOperations.createUpdateUsuario(true, true, new ClienteProveedor(cp.getDNI(), "Mengano"))),
				()-> assertTrue(DbOperations.readUsuario(true, true, cp.getDNI()).getNombre().equals("Mengano")),
				()-> assertTrue(DbOperations.deleteUsuario(true, true, cp.getDNI())),
				()-> assertTrue(DbOperations.readUsuario(true, true, cp.getDNI())==null)
				);
	}
	
	
	//Limpieza para futuras pruebas en local
	@AfterAll
	static void cleanDB() {
		DbOperations.dropTestDB();
	}
	
}
