package database;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.junit.*;
import org.junit.jupiter.api.Test;

class DbOperationsTest {

	static Connection conn;
	
	
	//Además de preparar la base de datos de pruebas, comprueba que los métodos de conexión y generado de la base de datos funcionan correctamente
	@Test
	void setupDB() {	
		assertDoesNotThrow(() -> {			
			return DbOperations.connect("127.0.0.1:3306", DbOperations.USER, DbOperations.PSWD);
		}, "Error de Conexión");
		
		assertTrue(DbOperations.generateDB(true));
		
	}
	
}
