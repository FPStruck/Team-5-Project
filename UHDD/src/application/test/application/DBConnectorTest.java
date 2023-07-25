package application.test.application;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import application.DBConnector;

class DBConnectorTest {
	static DBConnector db = new DBConnector();
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		db.initialiseDB();
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
	}

	@Test
	void dbConnectorTest() throws ClassNotFoundException, SQLException {
//		fail("Not yet implemented");
		System.out.println("Logged in status for user: " + db.getLoggedInStatus("toby"));
		System.out.println("Is user logged in? " + db.isAnyUserLoggedIn("toby")); 
		db.setLoggedInStatus("toby", 1);
		System.out.println("Logged in status for user: " + db.getLoggedInStatus("toby"));
		System.out.println("Is user logged in? " + db.isAnyUserLoggedIn("toby"));
		
		System.out.println("Logged in status for user: " + db.getLoggedInStatus("toby"));
		System.out.println("Is user logged in? " + db.isAnyUserLoggedIn("toby"));
		db.setLoggedInStatus("toby", 0);
		System.out.println("Logged in status for user: " + db.getLoggedInStatus("toby"));
		System.out.println("Is user logged in? " + db.isAnyUserLoggedIn("toby"));
	}
	
	@Test
	void dbCreateUserTest() throws ClassNotFoundException, SQLException {
//		fail("Not yet implemented");
	}

}
