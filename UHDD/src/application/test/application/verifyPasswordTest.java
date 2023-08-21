/*
package application.test.application;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

import java.sql.ResultSet;

import application.CredentialManager;
import application.PasswordHasher;
import application.DBConnector;
import application.PasswordHash;

class verifyPasswordTest {

    private CredentialManager credentialManager;
    private DBConnector dbConnector;
    private PasswordHasher passwordHasher;
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
    public void setUp() {
        dbConnector = mock(DBConnector.class);
        passwordHasher = mock(PasswordHasher.class);
        credentialManager = new CredentialManager();
    }


	@AfterEach
	void tearDown() throws Exception {
	}

    @Test
    public void testVerifyCorrectPassword() throws Exception {
        // Set up
        String username = "testUser";
        String password = "correctPassword123";
      //  PasswordHash passwordHash = PasswordHash.fromString("hashValue", "paramsValue");

        when(dbConnector.QueryReturnResultsFromUser(username)).thenReturn(mock(ResultSet.class));
        when(passwordHasher.verifyPassword(eq(password), any(PasswordHash.class))).thenReturn(true);

        // Test
        boolean result = credentialManager.verifyPassword(username, password);

        // Verify
        assertTrue(result, "Correct password should be verified");
    }

    @Test
    public void testVerifyIncorrectPassword() throws Exception {
        // Set up
        String username = "testUser";
        String password = "incorrectPassword";
        
        when(dbConnector.QueryReturnResultsFromUser(username)).thenReturn(mock(ResultSet.class));
        when(passwordHasher.verifyPassword(eq(password), any(PasswordHash.class))).thenReturn(false);

        // Test
        boolean result = credentialManager.verifyPassword(username, password);

        // Verify
        assertFalse(result, "Incorrect password should not be verified");
    }
    
}
*/