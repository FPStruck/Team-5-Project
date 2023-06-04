 

package application;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;

import javafx.scene.paint.Color;

public class CredentialManager {
	DBConnector dbConnector = new DBConnector();
	
	protected void saveCredentialsToFile(String username, String password, String email) throws ClassNotFoundException, SQLException {
		dbConnector.initialiseDB();
		PasswordHasher passwordHasher = new PasswordHasher();		
		PasswordHash passwordHash = passwordHasher.hashPassword(password);
		String passwordHashAsString = passwordHash.getHashAsString();
		String paramsAsString = passwordHash.getParamsAsString();
		dbConnector.createUserExecuteQuery(username, passwordHashAsString, paramsAsString, email);
		dbConnector.closeConnection();
		/*
		try {
			String directory = System.getProperty("user.home");
			String filePath = directory + "/Documents/credentials.txt";
			FileWriter writer = new FileWriter(filePath, true);
			EncryptionController enc = new EncryptionController();
			String hashedPassword = enc.hashData(password);
			writer.write(username + ":" + hashedPassword + ":" + email + "\n");
			System.out.println(hashedPassword);
			writer.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
		*/
	}
	
	
	protected String checkCredentialsInFile(String username, String password) {
	    //loops through the credentials.txt to store each line in an array which should have an email at index 2
		try {
	    	EncryptionController enc = new EncryptionController();
	    	String directory = System.getProperty("user.home");
	        String filePath = directory + "/Documents/credentials.txt";
	        File file = new File(filePath);
	        Scanner scan = new Scanner(file);
	        while (scan.hasNextLine()) {
	            String data = scan.nextLine();
	            String[] part = data.split(":");
	            if (part.length == 3 && part[0].equals(username)) {
	                String storedHashedPassword = part[1];
	                String inputHashedPassword = enc.hashData(password);
	                System.out.println(inputHashedPassword);
	                System.out.println(storedHashedPassword);
	                if (storedHashedPassword.equals(inputHashedPassword)) {
	                	System.out.println(part[2]);
	                    scan.close();
	                    return part[2]; // return email address
	                } 
	            }
	        }
	        scan.close();
	        /*
	        actionGrabber.setText("no match found");
			actionGrabber.setFill(Color.RED);
			*/
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    return null;
	}
	
}
