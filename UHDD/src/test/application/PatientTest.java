package application.test.application;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Random;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.Random;

import application.Patient;
import javafx.beans.property.SimpleStringProperty;

class PatientTest {

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void test() {
		 Random random = new Random();
		 
	      for (int i = 0; i < 10; i++) {
	    	  int id = random.nextInt(1000);
	    	  System.out.println(id);
	    	  String familyName = "familyName" + i;
	    	  String givenName = "givenName" + i;
	    	  String middleName = "middleName" + i;
	    	  String gender = "gender" + i;
	    	  String address = "address" + i;
	    	  String city = "city" + i;
	    	  String state = "state" + i;
	    	  String telephone = "telephone" + i;
	    	  String email = "email" + i;
	    	  String dateOfBirth = "dateOfBirth" + i; //Does this need to be a regular date
	    	  String healthInsuranceNumber = "healthInsuranceNumber" + i;
	    	  String emergencyContactNumber = "emergencyContactNumber" + i;
	    	  
	    	  Patient patient = new Patient(id, familyName, givenName, middleName, gender, address, city,state,telephone, email,dateOfBirth, healthInsuranceNumber, emergencyContactNumber); 
	    	  patient.setId(id);
	    	  patient.setFamilyName(familyName);
	    	  patient.setGivenName(givenName);
	    	  patient.setMiddleName(middleName);
	    	  patient.setGender(gender);
	    	  patient.setAddress(address);
	    	  patient.setCity(city);
	    	  patient.setState(state);
	    	  patient.setTelephone(telephone);
	    	  patient.setEmail(email);
	    	  patient.setDateOfBirth(dateOfBirth);
	    	  patient.setHealthInsuranceNumber(healthInsuranceNumber);
	    	  patient.setEmergencyContactNumber(emergencyContactNumber);
	    	  
	    	  assertEquals(id, patient.getId(), "ID does not match");
	          assertEquals(familyName, patient.getFamilyName(), "Family name does not match");
	          assertEquals(givenName, patient.getGivenName(), "Given name does not match");
	          assertEquals(middleName, patient.getMiddleName(), "Middle name does not match");
	          assertEquals(gender, patient.getGender(), "Gender does not match");
	          assertEquals(address, patient.getAddress(), "Address does not match");
	          assertEquals(city, patient.getCity(), "City does not match");
	          assertEquals(state, patient.getState(), "State does not match");
	          assertEquals(telephone, patient.getTelephone(), "Telephone does not match");
	          assertEquals(email, patient.getEmail(), "Email does not match");
	          assertEquals(dateOfBirth, patient.getDateOfBirth(), "Date of birth does not match");
	          assertEquals(healthInsuranceNumber, patient.getHealthInsuranceNumber(), "Health insurance number does not match");
	          assertEquals(emergencyContactNumber, patient.getEmergencyContactNumber(), "Emergency contact number does not match");
	    	  
	      }
	}

}
