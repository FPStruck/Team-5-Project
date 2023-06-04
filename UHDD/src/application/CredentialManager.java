 

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
			dbConnector.closeConnection();
			if (userDetails.next()) {  				
				PasswordHash passwordHash = PasswordHash.fromString(userDetails.getString("password_hash"), userDetails.getString("password_params"));				
				if(passwordHasher.verifyPassword(password, passwordHash)) {
		            String email = userDetails.getString("email");
		            return email;
		        } else {
		        	System.out.println("false");
		            return null;
		        }
		    } else {
		        return null;   
		    }  		
		} catch (SQLException e) {
		    // handle exception
		}
		return null;
	    
	}
	
}
