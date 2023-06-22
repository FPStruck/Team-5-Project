package application;

import java.io.IOException;
import java.sql.SQLException;

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
	
	@FXML protected void handleCreateNewUsernAction(ActionEvent event) throws ClassNotFoundException, SQLException {
		System.out.println(cbRole.getValue());
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
				String roleCreate = cbRole.getValue();
				CredentialManager.addNewUserToDB(userCreate, passCreate, emailCreate, roleCreate);
			}
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
