package application.test.application;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import application.UserSession;

class UserSessionTest {
	UserSession us = new UserSession("500", "1@2.com");
	
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
	void getUserNameTest() {
//		fail("Not yet implemented");
		System.out.println(us.getUserName());
	}
	
	@Test
	void getUserEmailTest() {
//		fail("Not yet implemented");
		System.out.println(us.getEmail());
	}

}
