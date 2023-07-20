package application.viewControllers;

import java.io.IOException;
import java.sql.SQLException;

import application.CredentialManager;
import application.DBConnector;
import application.EmailManager;
import application.EmailManager.LoginResult;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
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
    private DBConnector dbConnector;
    private static String currentUser;

    @FXML
    private TextField userGrabber;

    @FXML
    private TextField passGrabber;

    @FXML
    private Text actionGrabber;

    @FXML
    private Button btnLogin;

    public void setDbConnector(DBConnector dbConnector) {
        this.dbConnector = dbConnector;
    }

    public boolean loginSuccessful() throws ClassNotFoundException, SQLException {
        CredentialManager credentialManager = new CredentialManager();
        String userLog = userGrabber.getText();
        String passLog = passGrabber.getText();
        String to = credentialManager.checkCredentialsInFile(userLog, passLog);

        if (to == null) {
            actionGrabber.setText("No user match found");
            actionGrabber.setFill(Color.RED);
            return false;
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

    @FXML
    protected void handleSignInAction(ActionEvent event) throws IOException, ClassNotFoundException, SQLException {
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/mm/yyyy hh:mm:ss a");
        String formattedDateTime = currentDateTime.format(formatter);
        DBConnector dbConnector = new DBConnector();
        dbConnector.initialiseDB();

        String username = userGrabber.getText();

        int loggedInStatus = dbConnector.getLoggedInStatus(username);

        if (userGrabber.getText().isEmpty() && passGrabber.getText().isEmpty()) {
            actionGrabber.setText("Username and Password cannot be empty");
            actionGrabber.setFill(Color.RED);
        } else if (userGrabber.getText().isEmpty()) {
            actionGrabber.setText("Username cannot be empty");
            actionGrabber.setFill(Color.RED);
        } else if (passGrabber.getText().isEmpty()) {
            actionGrabber.setText("Password cannot be empty");
            actionGrabber.setFill(Color.RED);
        } else if (loggedInStatus == 1) {
            actionGrabber.setText("Another user is already logged in with this username");
            actionGrabber.setFill(Color.RED);
            System.out.println("A user: " + username + " has attempted access from another device (all users logged out): " + formattedDateTime);
            dbConnector.setLoggedInStatus(username, 0);
        } else { 
        	
        	if (loginSuccessful()) {
                System.out.println("A user: " + username + " has successfully logged in at: " + formattedDateTime);
                dbConnector.setLoggedInStatus(username, 1);

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/fxmlScenes/Dashboard.fxml"));
                Parent root = loader.load();
                DashboardController dashboardController = loader.getController();
                dashboardController.setUserText(username);

                stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } else {
                System.out.println("A failed login attempt has been established with user: " + username + " At: " + formattedDateTime);

                // Redirect the current user to the login.fxml scene
                if (currentUser != null && currentUser.equals(username)) {
                    Parent root = FXMLLoader.load(getClass().getResource("/application/fxmlScenes/Login.fxml"));
                    stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                }
            }
        }
    }

    @FXML
    public void switchToCreateUser(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/application/fxmlScenes/UserCreation.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void bypassUserLogin(MouseEvent mouseEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/application/fxmlScenes/Dashboard.fxml"));
        stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}
