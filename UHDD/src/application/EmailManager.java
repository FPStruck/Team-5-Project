package application;

import java.util.Optional;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import javafx.application.Platform;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.VBox;


public class EmailManager {
	/* 
	public enum LoginResult {
		SUCCESSFUL,
	    WRONG_CODE,
	    CANCELLED
	}
	
	public LoginResult verifyLogin(String toAddress) {
		
	    String from = "Verifier";
	    String host = "smtp.gmail.com";
	    int port = 587;
//	    String username = "jonoleo@gmail.com";
//	    //Will require a password here
//	    String password = "xx";
	    
	    // use my email
	    String username = "mina.gemian79@gmail.com";
	    String password = "yhgmqnodfoolhnmz";
	      
	    int expectedCode = (int) (Math.random() * 1000000);
	    
	    System.out.println(expectedCode);
	    
	    int inputCode = 0;
	    
	    //email properties
	    Properties properties = System.getProperties();  
//	    properties.setProperty("mail.smtp.host", host);
//	    properties.setProperty("mail.smtp.port", String.valueOf(port));
//	    properties.setProperty("mail.smtp.starttls.enable", "true");
//	    properties.setProperty("mail.smtp.auth", "true");
	    
	    // use my properties 
	    properties.put("mail.smtp.host", "smtp.gmail.com");
	    properties.put("mail.smtp.socketFactory.port", "587");
	    properties.put("mail.smtp.socketFactory.class", "javax.net.SocketFactory");
	    properties.put("mail.smtp.auth", "true");
	    properties.put("mail.smtp.port", "587");
	    properties.put("mail.smtp.ssl.enable", "false");
	    properties.put("mail.smtp.starttls.enable", "true");
	    properties.put("mail.smtp.ssl.trust", "smtp.gmail.com");

	    // Get the email session object  
	    Session session = Session.getDefaultInstance(properties,  
	        new javax.mail.Authenticator() {  
	            protected PasswordAuthentication getPasswordAuthentication() {  
	                return new PasswordAuthentication(username, password);  
	            }  
	        });  
	    
	    //message  
	    try {  
	    	MimeMessage message = new MimeMessage(session);
	        message.setFrom(new InternetAddress(from)); 	        
	        message.addRecipient(Message.RecipientType.TO, new InternetAddress(toAddress)); 
	        message.setSubject("Verification Code");  
	        message.setText("Your verification code is: " + expectedCode);  

	        //Send message to email
	        Transport transport = session.getTransport("smtp");
	        transport.connect(host, username, password);
	        transport.sendMessage(message, message.getAllRecipients());
	        transport.close();
	        
	        System.out.println("Verification code sent to " + toAddress);  

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

	        Optional<Integer> result = dialog.showAndWait();
	        if (result.isPresent()) {
	            inputCode = result.get();
	        } else {
	        	return LoginResult.CANCELLED;
	        }

	        // Check if the input code matches the expected value
	        if (inputCode == expectedCode) {
	            System.out.println("Verification successful!");
	            return LoginResult.SUCCESSFUL;
	        } else if (inputCode != expectedCode) {
	        	dialog.close();	  
	        	return LoginResult.WRONG_CODE;
	        }
	    } catch (MessagingException mex) {
	        mex.printStackTrace();
	    }
	    return LoginResult.CANCELLED;
	}
	*/
}
