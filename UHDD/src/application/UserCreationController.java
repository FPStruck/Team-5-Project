package application;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
	
	@FXML protected void handleCreateNewUsernAction(ActionEvent event) {
		CredentialManager CredentialManager = new CredentialManager();
			if(userGrabberCreator.getText().equals("") & passGrabberCreator.getText().equals("") & emailGrabberCreator.getText().equals("")) {
				actionGrabberCreator.setText("All fields cannot be empty");
				actionGrabberCreator.setFill(Color.RED);
			} else if(userGrabberCreator.getText().equals("")) {
				actionGrabberCreator.setText("Username cannot be empty");
				actionGrabberCreator.setFill(Color.RED);
			} else if(passGrabberCreator.getText().equals("")) {
				actionGrabberCreator.setText("Password cannot be empty");
				actionGrabberCreator.setFill(Color.RED);
			} else if(emailGrabberCreator.getText().equals("")) {
				actionGrabberCreator.setText("Email cannot be empty");
				actionGrabberCreator.setFill(Color.RED);
			} 
			else {
				actionGrabberCreator.setText("User Creation Successful");
				actionGrabberCreator.setFill(Color.GREEN);
				String userCreate = userGrabberCreator.getText();
				String passCreate = passGrabberCreator.getText();
				String emailCreate = emailGrabberCreator.getText();
				CredentialManager.saveCredentialsToFile(userCreate, passCreate, emailCreate);
			}
		}
	
	public void switchToHomepage(ActionEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
}
