package application;

import java.io.IOException;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class UserCreationController {
	private Stage stage;
	private Scene scene;
	@FXML private Text actionGrabberCreator;
	@FXML private TextField userGrabberCreator;
	@FXML private TextField passGrabberCreator;
	@FXML private TextField emailGrabberCreator;
	@FXML private Rectangle ucRectanglePane;
	@FXML private ComboBox<String> cbRole;
	
	static ObservableList<String> roles;
	private static final String regex = "^(.+)@(.+)$"; // ensure that it is a valid email address *@*
	Pattern specialCharPattern = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE); // not a lower or upper case letter or number
    Pattern upperCasePattern = Pattern.compile("[A-Z ]"); // upper case regex
    Pattern lowerCasePattern = Pattern.compile("[a-z ]"); // lower case regex
    Pattern digitCasePattern = Pattern.compile("[0-9 ]"); // number regex
	
	@FXML protected void handleCreateNewUsernAction(ActionEvent event) throws ClassNotFoundException, SQLException {
		System.out.println(cbRole.getValue());
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher= pattern.matcher(emailGrabberCreator.getText());
		System.out.println("Matcher: " + matcher);
		CredentialManager CredentialManager = new CredentialManager();
			if(userGrabberCreator.getText().equals("") & passGrabberCreator.getText().equals("") & emailGrabberCreator.getText().equals("")) {
				actionGrabberCreator.setText("All fields cannot be empty");
				actionGrabberCreator.setFill(Color.RED);
			} else if(userGrabberCreator.getText().equals("")) {
				actionGrabberCreator.setText("Username cannot be empty");
				actionGrabberCreator.setFill(Color.RED);
			} else if(passGrabberCreator.getText().equals("") || checkPasswordRequirements(passGrabberCreator.getText()) < 3) {
				actionGrabberCreator.setText("Password cannot be empty or at least 8 to 64 characters. It requires 3 out of 4 of lowercase, uppercase, numbers, or symbols.");
				actionGrabberCreator.setFill(Color.RED);
			} else if(emailGrabberCreator.getText().equals("") || !(matcher.matches())) {
				actionGrabberCreator.setText("Email cannot be empty or not a valid email address");
				actionGrabberCreator.setFill(Color.RED);
			} 
			else {
				actionGrabberCreator.setText("User Creation Successful");
				actionGrabberCreator.setFill(Color.GREEN);
				String userCreate = userGrabberCreator.getText();
				String passCreate = passGrabberCreator.getText();
				String emailCreate = emailGrabberCreator.getText();
				String roleCreate = cbRole.getValue();
				CredentialManager.addNewUserToDB(userCreate, passCreate, emailCreate, roleCreate);
			}
		}
	
	public int checkPasswordRequirements(String password) {
		int passwordCount = 0;
		if (password.length() >= 8) {
			if (upperCasePattern.matcher(password).find()) {
				passwordCount++;
			}
			if (lowerCasePattern.matcher(password).find()) {
				passwordCount++;
			}
			if (specialCharPattern.matcher(password).find() ) {
				passwordCount++;
			}
			if (digitCasePattern.matcher(password).find()) {
				passwordCount++;
			}
		}
		
		System.out.println("Password count requirement: " + passwordCount);
		return passwordCount;
		
	}
	
	public void switchToHomepage(ActionEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	public void initialize() {
		roles = FXCollections.observableArrayList("Doctor", "Nurse");
		cbRole.getItems().addAll(roles);
		System.out.println(cbRole.getItems());
		
	}
}
