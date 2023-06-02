package application;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import javafx.scene.paint.Color;

public class CredentialManager {
	protected void saveCredentialsToFile(String username, String password, String email) {
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
	}
	
	/*
	protected String checkCredentialsInFile(String username, String password) {
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
	        actionGrabber.setText("no match found");
			actionGrabber.setFill(Color.RED);
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    return null;
	}
	*/
}
