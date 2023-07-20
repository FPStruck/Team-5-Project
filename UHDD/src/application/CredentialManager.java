package application;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


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
	
	
	public String checkCredentialsInFile(String username, String password) throws ClassNotFoundException, SQLException {
		dbConnector.initialiseDB();
	    
		try (ResultSet userDetails = dbConnector.QueryReturnResultsFromUser(username)) {
			if (userDetails.next()) {  				
				PasswordHash passwordHash = PasswordHash.fromString(userDetails.getString("password_hash"), userDetails.getString("password_params"));				
				if(passwordHasher.verifyPassword(password, passwordHash)) {
					System.out.println("35");
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
