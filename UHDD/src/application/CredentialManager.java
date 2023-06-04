 

package application;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import javafx.scene.paint.Color;

public class CredentialManager {
	DBConnector dbConnector = new DBConnector();
	PasswordHasher passwordHasher = new PasswordHasher();
	
	protected void addNewUserToDB(String username, String password, String email) throws ClassNotFoundException, SQLException {
		dbConnector.initialiseDB();		
		PasswordHash passwordHash = passwordHasher.hashPassword(password);
		String passwordHashAsString = passwordHash.getHashAsString();		
		String paramsAsString = passwordHash.getParamsAsString();	
		dbConnector.createUserExecuteQuery(username, passwordHashAsString, paramsAsString, email);
		dbConnector.closeConnection();
	}
	
	
	protected String checkCredentialsInFile(String username, String password) throws ClassNotFoundException, SQLException {
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
