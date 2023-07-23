package application;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import java.sql.Date;


public class CredentialManager {
	DBConnector dbConnector = new DBConnector();
	PasswordHasher passwordHasher = new PasswordHasher();
	
	public void addNewUserToDB(String username, String password, String email, String role) throws ClassNotFoundException, SQLException {
		dbConnector.initialiseDB();
		//Get the current date and time
		LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formatDateTime = now.format(formatter);
		//Hash the password
		PasswordHash passwordHash = passwordHasher.hashPassword(password);
		String passwordHashAsString = passwordHash.getHashAsString();		
		String paramsAsString = passwordHash.getParamsAsString();	
		dbConnector.createUserExecuteQuery(username, passwordHashAsString, paramsAsString, email, role, formatDateTime);
		dbConnector.closeConnection();
		B2CUserService newB2C = new B2CUserService();
		newB2C.createUser(username, password, email);
	}

	public boolean verifyPassword(String username, String password) throws ClassNotFoundException, SQLException {
		dbConnector.initialiseDB();
		try (ResultSet userDetails = dbConnector.QueryReturnResultsFromUser(username)) {
			if (userDetails.next()) {
				// Retrieve the "password_hash" and "password_params" fields from the database
				PasswordHash passwordHash = PasswordHash.fromString(userDetails.getString("password_hash"), userDetails.getString("password_params"));
	
				// Verify the password
				if (passwordHasher.verifyPassword(password, passwordHash)) {
					// The password is correct
					System.out.println("Password is correct");
					return true;
				} else {
					// The password is incorrect
					System.out.println("Password is incorrect");
					return false;
				}
			} else {
				// The user was not found in the database
				System.out.println("User not found");
				return false;
			}
		} catch (SQLException e) {
			// Handle the exception that occurred while accessing the database
		}
	
		// Close the database connection
		dbConnector.closeConnection();
		return false;
	}
	
	public void changePasswordInDB(String username, String password) throws ClassNotFoundException, SQLException {
		dbConnector.initialiseDB();
		//Get the current date and time
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		String formatDateTime = now.format(formatter);
		//Hash the password
		PasswordHash passwordHash = passwordHasher.hashPassword(password);
		String passwordHashAsString = passwordHash.getHashAsString();		
		String paramsAsString = passwordHash.getParamsAsString();	
		dbConnector.changePasswordExecuteQuery(username, passwordHashAsString, paramsAsString, formatDateTime);
		dbConnector.closeConnection();
	}

	//Check if password last set date was more than 30 days ago
	public boolean checkPasswordLastSetDate(String username) throws ClassNotFoundException, SQLException {
		dbConnector.initialiseDB();
		try (ResultSet userDetails = dbConnector.QueryReturnResultsFromUser(username)) {
			if (userDetails.next()) {
				// Retrieve the "password_last_modified" field from the database
				Date passwordLastModified = userDetails.getDate("password_last_modified");
	
				// Get the current date
				LocalDate currentDate = LocalDate.now();
	
				// Convert the "passwordLastModified" Date to LocalDate
				LocalDate lastModifiedDate = passwordLastModified.toLocalDate();
	
				// Calculate the difference between the current date and the password last modified date
				long daysDifference = java.time.temporal.ChronoUnit.DAYS.between(lastModifiedDate, currentDate);
	
				// Check if the password is older than 30 days
				System.out.println("password expiry " + daysDifference);
				dbConnector.closeConnection();
				return daysDifference > 30;
				
			} else {
				// The user was not found in the database
				dbConnector.closeConnection();
				return false;
			}
		} catch (SQLException e) {
			// Handle the exception that occurred while accessing the database
			dbConnector.closeConnection();
		}
	
		// Close the database connection and return false as the default result if any exception occurred
		dbConnector.closeConnection();
		return false;
	}
	
	public String checkCredentialsInFile(String username, String password) throws ClassNotFoundException, SQLException {
		checkPasswordLastSetDate(username);
		dbConnector.initialiseDB();	    
		try (ResultSet userDetails = dbConnector.QueryReturnResultsFromUser(username)) {
			if (userDetails.next()) {  				
				PasswordHash passwordHash = PasswordHash.fromString(userDetails.getString("password_hash"), userDetails.getString("password_params"));				
				if(passwordHasher.verifyPassword(password, passwordHash)) {
		            String email = userDetails.getString("email");
		            dbConnector.closeConnection();
		            return email;
		            
		        } else {
		        	System.out.println("false");
		        	dbConnector.closeConnection();
		            return null;
		        }
		    } else {
		    	dbConnector.closeConnection();
		        return null;   
		    }  		
		} catch (SQLException e) {			
		    // handle exception
			dbConnector.closeConnection();
		}
		dbConnector.closeConnection();
		return null;
	    
	}
	
}
