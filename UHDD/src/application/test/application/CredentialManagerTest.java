
package application.test.application;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import application.CredentialManager;

class CredentialManagerTest {

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
	}
    
	@Test
	void test() throws Exception {
		CredentialManager cm = new CredentialManager();
		cm.addNewUserToDB("toby", "password", "ts103@hotmail.com", "Nurse", "1");
		cm.verifyPassword("toby", "password");
		cm.checkPasswordLastSetDate("toby");
		cm.changePasswordInDB("toby", "newPassword");
		cm.verifyPassword("toby", "newPassword");
		System.out.println(cm.checkPasswordLastSetDate("toby"));
	}

}
