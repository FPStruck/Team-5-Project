package application;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import javafx.application.Platform;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.Date;



public class CredentialManager {

	
	DBConnector dbConnector = new DBConnector();
	PasswordHasher passwordHasher = new PasswordHasher();
	
	public void addNewUserToDB(String username, String password, String email, String role, String OTPSecretKey) throws ClassNotFoundException, SQLException {
		dbConnector.initialiseDB();
		//Get the current date and time
		LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formatDateTime = now.format(formatter);
		//Hash the password
		PasswordHash passwordHash = passwordHasher.hashPassword(password);
		String passwordHashAsString = passwordHash.getHashAsString();		
		String paramsAsString = passwordHash.getParamsAsString();	
		dbConnector.createUserExecuteQuery(username, passwordHashAsString, paramsAsString, email, role, formatDateTime, OTPSecretKey);
		dbConnector.closeConnection();
	}

	public boolean verifyPassword(String username, String password) throws ClassNotFoundException, SQLException {
		dbConnector.initialiseDB();	
		try (ResultSet userDetailsFromDb = dbConnector.QueryReturnResultsFromUser(username)) {
			if (userDetailsFromDb.next()) {
				System.out.println("verify password thinks user is : " + username);
				// Retrieve the "password_hash" and "password_params" fields from the database
				PasswordHash passwordHash = PasswordHash.fromString(userDetailsFromDb.getString("password_hash"), userDetailsFromDb.getString("password_params"));
	
				// Verify the password
				if (passwordHasher.verifyPassword(password, passwordHash)) {
					// The password is correct
					System.out.println("Password is correct");
					dbConnector.closeConnection();
					return true;
				} else {
					// The password is incorrect
					System.out.println("Password is incorrect");
					dbConnector.closeConnection();
					return false;
				}
			} else {
				// The user was not found in the database
				System.out.println("User not found");
				dbConnector.closeConnection();
				return false;
			}
		} catch (SQLException e) {
			System.out.println("Error querying the database");
			e.printStackTrace();
			return false;
		}
	
	}

	public boolean verifyOTP(String username, int OTP) throws NoSuchAlgorithmException{
		OTPService otpService;
		try {
			otpService = new OTPService();
		} catch (NoSuchAlgorithmException e) {
			System.out.println("OTP Service not found");
			e.printStackTrace();
			return false;
		}
	
		String secretKey = getOTPSecretKeyFromDatabase(username);
		if(secretKey == null){
			System.out.println("No secret key found for user: " + username);
			return false;
		}
	
		try {
			if(otpService.validateOTP(secretKey, OTP)) {
				System.out.println("OTP is correct");
				return true;
			} else {
				System.out.println("OTP is incorrect");
				return false;
			}
		} catch (InvalidKeyException e) {
			System.out.println("Invalid Key");
			e.printStackTrace();
			return false;
		}
	}
	
	public String getOTPSecretKeyFromDatabase(String username){
		try {
			dbConnector.initialiseDB();
			ResultSet OTPSecretKey = dbConnector.QueryReturnOTPSecretKeyFromUser(username);
			if(OTPSecretKey.next()){
				return OTPSecretKey.getString("OTPSecretKey");
			}
		} catch (ClassNotFoundException | SQLException e) {
			System.out.println(e.getClass().getName());
			e.printStackTrace();
		}
		return null;
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

	public LoginResult verifyMFA(String username) throws NoSuchAlgorithmException{
            int inputCode = 0;
        
            // Display the dialog box for verification code
	        Dialog<Integer> dialog = new Dialog<>();
	        dialog.setTitle("Verification Code");
	        //dialog.setHeaderText("Enter the verification code:");

	        ButtonType submitButton = new ButtonType("Submit", ButtonData.OK_DONE);
	        dialog.getDialogPane().getButtonTypes().addAll(submitButton, ButtonType.CANCEL);

	        TextField verificationCodeField = new TextField();
	        Platform.runLater(() -> verificationCodeField.requestFocus());
	        dialog.getDialogPane().setContent(new VBox(8, new Label("Verification code:"), verificationCodeField));
	        dialog.setResultConverter(dialogButton -> {
	            if (dialogButton == submitButton) {
	                return Integer.parseInt(verificationCodeField.getText());
	            }
	            return null;
	        });

            //Get the result from the dialog box
	        Optional<Integer> result = dialog.showAndWait();
	        if (result.isPresent()) {
	            inputCode = result.get();
	        } else {
	        	return LoginResult.CANCELLED;
	        }

            //Verify the code
            if (verifyOTP(username, inputCode)) {
	            System.out.println("Verification successful!");
	            return LoginResult.SUCCESSFUL;
	        } else if (!verifyOTP(username, inputCode)) {
	        	dialog.close();	  
	        	return LoginResult.WRONG_CODE;
	        }
            return LoginResult.CANCELLED;
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
	
	public String verifyPasswordAndReturnEmail(String username, String password) throws ClassNotFoundException, SQLException {
		checkPasswordLastSetDate(username);
		dbConnector.initialiseDB();	    
		try (ResultSet userDetails = dbConnector.QueryReturnResultsFromUser(username)) {
			if (userDetails.next()) {  				
				PasswordHash passwordHash = PasswordHash.fromString(userDetails.getString("password_hash"), userDetails.getString("password_params"));				
				if(passwordHasher.verifyPassword(password, passwordHash)) {
		            String email = userDetails.getString("email");
		            dbConnector.closeConnection();
					System.out.println("password is valid here is email: " + email);
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
