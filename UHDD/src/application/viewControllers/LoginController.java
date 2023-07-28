package application.viewControllers;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

import application.CredentialManager;
import application.DBConnector;
import application.EmailManager;
import application.EmailManager.LoginResult;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class LoginController {

    private Stage stage;
    private Scene scene;
    private DBConnector dbConnector;
    private CredentialManager credentialManager;
    private static String currentUser;

    @FXML
    private TextField userGrabber;
    @FXML
    private TextField passGrabber;
    @FXML
    private Text actionGrabber;
    @FXML
    private Button btnLogin;
    @FXML
    private Button btnPwdReset;

    public enum LoginResult {
		SUCCESSFUL,
	    WRONG_CODE,
	    CANCELLED
	}

    public void setDbConnector(DBConnector dbConnector) {
        this.dbConnector = dbConnector;
    }

    public LoginResult verifyMFA(String username) throws NoSuchAlgorithmException{
            credentialManager = new CredentialManager();    
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

	        Optional<Integer> result = dialog.showAndWait();
	        if (result.isPresent()) {
	            inputCode = result.get();
	        } else {
	        	return LoginResult.CANCELLED;
	        }

            if (credentialManager.verifyOTP(username, inputCode)) {
	            System.out.println("Verification successful!");
	            return LoginResult.SUCCESSFUL;
	        } else if (!credentialManager.verifyOTP(username, inputCode)) {
	        	dialog.close();	  
	        	return LoginResult.WRONG_CODE;
	        }
            return LoginResult.CANCELLED;
    }

    public boolean loginSuccessful() throws ClassNotFoundException, SQLException, IOException, InvalidKeyException, NoSuchAlgorithmException {
        credentialManager = new CredentialManager();
        String userLog = userGrabber.getText();
        String passLog = passGrabber.getText();
        String emailTo = credentialManager.checkCredentialsInFile(userLog, passLog);
        if(!credentialManager.checkPasswordLastSetDate(userLog)){
            if (emailTo == null) {
                actionGrabber.setText("No user match found");
                actionGrabber.setFill(Color.RED);
                return false;
            }

            //2FA verification
            


            //EmailManager emailManager = new EmailManager();
            //LoginResult result = emailManager.verifyLogin(emailTo);
            LoginResult result = verifyMFA(userLog);

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
        } else {
            actionGrabber.setText("Password has expired. Please reset your password");
            //Open popup window
            Stage popupStage = new Stage();
            Parent popupRoot = FXMLLoader.load(getClass().getResource("/application/fxmlScenes/PopUpPwdExpired.fxml"));
            Scene popupScene = new Scene(popupRoot);
            popupStage.setScene(popupScene);
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.showAndWait();
            return false;
        }
    }

    @FXML
    protected void handlePwdResetAction(ActionEvent event) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("/application/fxmlScenes/PasswordReset.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    protected void handleSignInAction(ActionEvent event) throws IOException, ClassNotFoundException, SQLException, InvalidKeyException, NoSuchAlgorithmException {
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/mm/yyyy hh:mm:ss a");
        String formattedDateTime = currentDateTime.format(formatter);
        DBConnector dbConnector = new DBConnector();
        dbConnector.initialiseDB();

        String username = userGrabber.getText();
        UserSession.initInstance(username);
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
