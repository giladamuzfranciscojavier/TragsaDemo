package controllers;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import controllers.DBController;
import models.usuarios.*;
import models.*;

class DBTest {

	static Connection conn;
	
	
	//Además de preparar la base de datos de pruebas, comprueba que los métodos de conexión y generado de la base de datos funcionan correctamente
	@BeforeAll
	static void setupDB() {	
		assertDoesNotThrow(() -> {			
			return DBController.connect("127.0.0.1:3306", DBController.USER, DBController.PSWD);
		}, "Error de Conexión");
		
		assertTrue(DBController.generateDB(true));
		
	}
	
	@Test
	void clienteCRUDTest() {
		Cliente c = new Cliente("12345678A","Fulano");
		assertAll(
			"Operaciones CRUD Cliente",
			()-> assertTrue(UsuarioController.createUpdateUsuario(true, false, c)),
			()-> assertTrue(UsuarioController.readUsuario(true, false, c.getDNI()).equals(c)),
			()-> assertTrue(UsuarioController.createUpdateUsuario(true, false, new Cliente(c.getDNI(), "Mengano"))),
			()-> assertTrue(UsuarioController.readUsuario(true, false, c.getDNI()).getNombre().equals("Mengano")),
			()-> assertTrue(UsuarioController.deleteUsuario(true, false, c.getDNI())),
			()-> assertTrue(UsuarioController.readUsuario(true, false, c.getDNI())==null)		
		);		
	}


	@Test
	void ProductoCRUDTest() {
		Proveedor p = new Proveedor("87654321Z", "Mengano");
		assertTrue(UsuarioController.createUpdateUsuario(false, true, p));
		Producto pr = new Producto("Cosa", 99.99, p.getDNI());
		
		/*Por problemas de diseño derivados de la urgencia de desarrollo es tremendamente complicado obtener 
		el id de producto de forma automática habiendo más de 1 test, por lo que se truncará dicha tabla antes 
		de cada test para que estén en una posición previsible. Si bien no es lo ideal es una solución rápida
		*/
		
		try {
			DBController.checkConnection().createStatement().executeUpdate("TRUNCATE producto");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		assertAll(
				"Operaciones CRUD Producto",
				()-> assertTrue(ProductoController.createUpdateProducto(pr)),
				()-> assertTrue(ProductoController.readProducto(1).getNombre().equals(pr.getNombre())),
				()-> assertTrue(ProductoController.createUpdateProducto(new Producto(1, "Otra Cosa", 9.99, pr.getProveedor_dni()))),
				()-> assertTrue(ProductoController.readProducto(1).getNombre().equals("Otra Cosa")),
				()-> assertTrue(ProductoController.deleteProducto(1)),
				()-> assertTrue(ProductoController.readProducto(1)==null)
		);
	}
	
	
	@Test
	void ClienteProveedorCRUDTest() {
		ClienteProveedor cp = new ClienteProveedor("13579246A","Cetano");
		assertAll(
				"Operaciones CRUD Cliente-Proveedor",
				() -> assertTrue(UsuarioController.createUpdateUsuario(true, true, cp)),
				()-> assertTrue(UsuarioController.readUsuario(true, true, cp.getDNI()).equals(cp)),
				()-> assertTrue(UsuarioController.createUpdateUsuario(true, true, new ClienteProveedor(cp.getDNI(), "Mengano"))),
				()-> assertTrue(UsuarioController.readUsuario(true, true, cp.getDNI()).getNombre().equals("Mengano")),
				()-> assertTrue(UsuarioController.deleteUsuario(true, true, cp.getDNI())),
				()-> assertTrue(UsuarioController.readUsuario(true, true, cp.getDNI())==null)
			);
	}
	
	
	@Test
	void CompraTest() {
		Cliente cl = new Cliente("ClTest", null);		
		Proveedor pr = new Proveedor("PrTest", null);		
		Producto pr1 = new Producto("Cosa Comprada", 12.34, "PrTest");
		Producto pr2 = new Producto("Cosa NO Comprada", 56.78, "PrTest");
		
		try {
			DBController.checkConnection().createStatement().executeUpdate("TRUNCATE producto");
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		
		assertTrue(UsuarioController.createUpdateUsuario(true, false, cl));
		assertTrue(UsuarioController.createUpdateUsuario(false, true, pr));
		assertTrue(ProductoController.createUpdateProducto(pr1));
		assertTrue(ProductoController.createUpdateProducto(pr2));
		
		assertTrue(ClienteController.comprarProducto(cl.getDNI(), pr1.getProducto_ID()));
		
		
		assertAll(
				"Operación de Compra",
				() -> assertTrue(ClienteController.comprarProducto(cl.getDNI(), 1)),
				() -> assertTrue(ProductoController.readAllProductos().size()==2),
				() -> assertTrue(ClienteController.listProductosComprados(cl.getDNI()).size()==1),
				() -> assertTrue(ClienteController.listProductosComprados(cl.getDNI()).get(0).getNombre().equals("Cosa Comprada"))
			);
		
		
	}
	
	
	//Limpieza para futuras pruebas en local
	@AfterAll
	static void cleanDB() {
		DBController.dropTestDB();
	}
	
}
