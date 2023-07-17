package application.viewControllers;

import java.io.IOException;
import java.sql.SQLException;

import application.CredentialManager;
import application.DBConnector;
import application.EmailManager;
import application.EmailManager.LoginResult;
import application.MultiSessionLogoutSystem;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LoginController {
	private Stage stage;
	private Scene scene;
	
	@FXML private TextField userGrabber;
	@FXML private TextField passGrabber;
	@FXML private Text actionGrabber;
	@FXML private Text actionGrabberCreator;
	@FXML private TextField userGrabberCreator;
	@FXML private TextField passGrabberCreator;
	@FXML private TextField emailGrabberCreator;
	@FXML private Button btnLogin;
	
	public boolean loginSuccessful() throws ClassNotFoundException, SQLException {
		CredentialManager credentialManager = new CredentialManager();
		String userLog = userGrabber.getText();
	    String passLog = passGrabber.getText();
	    String to = credentialManager.checkCredentialsInFile(userLog, passLog);
	    if(to == null) {
	    	actionGrabber.setText("No user match found");
			actionGrabber.setFill(Color.RED);
	    }
	    EmailManager emailManager = new EmailManager();
	    LoginResult result = emailManager.verifyLogin(userLog, passLog, to);
	    if (result == LoginResult.SUCCESSFUL) {
	        // Handle successful login
	    	return true;
	    } else if (result == LoginResult.WRONG_CODE) {
	        // Handle wrong code scenario
	    	actionGrabber.setText("Wrong code input. Email verification cancelled");
			actionGrabber.setFill(Color.RED);
			return false;
	    } else if (result == LoginResult.CANCELLED) {
	        // Handle login cancelled scenario
	    	actionGrabber.setText("Email verification cancelled");
			actionGrabber.setFill(Color.RED);
			return false;
	    } else {
	        // Handle any other result if necessary
	    	return false;
	    }

	}
	
	@FXML protected void handleSignInAction(ActionEvent event) throws IOException, ClassNotFoundException, SQLException {
		LocalDateTime currentDateTime = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/mm/yyyy hh:mm:ss a");
		String formattedDateTime = currentDateTime.format(formatter);
		
		if(userGrabber.getText().equals("") & passGrabber.getText().equals("")) {
			actionGrabber.setText("Username and Password cannot be empty");
			actionGrabber.setFill(Color.RED);
		} else if(userGrabber.getText().equals("")) {
			actionGrabber.setText("Username cannot be empty");
			actionGrabber.setFill(Color.RED);
		} else if(passGrabber.getText().equals("")) {
			actionGrabber.setText("Password cannot be empty");
			actionGrabber.setFill(Color.RED);
		} 
		else {
			
			MultiSessionLogoutSystem logoutSystem = new MultiSessionLogoutSystem(new DBConnector());
			
			if (loginSuccessful() == false) {
				System.out.println("A failed login attempt has been established with user: " + userGrabber.getText() + " At: " + formattedDateTime);
				logoutSystem.logoutAllSessions(userGrabber.getText());
				Parent root = FXMLLoader.load(getClass().getResource("/application/fxmlScenes/Login.fxml"));
	            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
	            scene = new Scene(root);
	            stage.setScene(scene);
	            stage.show();
			}
			else if(loginSuccessful() == true) {
				System.out.println("A user: " + userGrabber.getText() + " has successfully logged in at: " + formattedDateTime);
				Parent root = FXMLLoader.load(getClass().getResource("/application/fxmlScenes/Dashboard.fxml")); // change to dashboard
				stage = (Stage)((Node)event.getSource()).getScene().getWindow();
				scene = new Scene(root);
				stage.setScene(scene);
				stage.show();
			}
			
		}
	}

@FXML public void switchToCreateUser(ActionEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("/application/fxmlScenes/UserCreation.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

	@FXML public void bypassUserLogin(MouseEvent mouseEvent) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("/application/fxmlScenes/Dashboard.fxml")); // change to dashboard
		stage = (Stage)((Node)mouseEvent.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
}