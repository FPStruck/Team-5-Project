package application.test.application;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.Random;

import application.Medication;

class MedicationTest {
			
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
	        
	        for (int i = 0; i < 10; i++) { // Run the test with 10 random sets of inputs
	        	//Making random inputs for all conditions 
	            int scriptId = random.nextInt(1000);
	            System.out.println(scriptId);
	            int patientId = random.nextInt(1000);
	            String medicationName = "Medication" + i;
	            LocalDate prescribedDate = LocalDate.now().minusDays(random.nextInt(30));
	            LocalDate expiredDate = prescribedDate.plusDays(random.nextInt(60));
	            
	            //Using the set methods
	            Medication medication = new Medication(scriptId, patientId, medicationName, prescribedDate, expiredDate);
	            medication.setScriptId(scriptId);
	            medication.setPatientId(patientId);
	            medication.setMedicationName(medicationName);
	            medication.setPrescribedDate(prescribedDate);
	            medication.setExpiredDate(expiredDate);
	            
	            //Using the get methods and checking that they are correct
	            assertEquals(scriptId, medication.getScriptId(), "Script ID does not match");
	            assertEquals(patientId, medication.getPatientId(), "Patient ID does not match");
	            assertEquals(medicationName, medication.getMedicationName(), "Medication name does not match");
	            assertEquals(prescribedDate, medication.getPrescribedDate(), "Prescribed date does not match");
	            assertEquals(expiredDate, medication.getExpiredDate(), "Expired date does not match");
	        }
	}

}
