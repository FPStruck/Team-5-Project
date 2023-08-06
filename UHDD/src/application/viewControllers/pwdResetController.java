package application.viewControllers;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.cj.protocol.Resultset;

import application.CredentialManager;
import application.DBConnector;
import application.EmailManager;
import application.EmailManager.LoginResult;
import application.PasswordHash;
import application.PasswordHasher;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class pwdResetController {
    @FXML
    private Button btnResetPwd;
    @FXML
    private Button btnCancel;
    @FXML
    private TextField txtOldPwd;
    @FXML
    private TextField txtNewPwd;
    @FXML
    private TextField txtVerifyPwd;
    @FXML
    private Text txtStatus;

  

    
    public void resetPwd() throws SQLException, ClassNotFoundException {
        String oldPwd = txtOldPwd.getText();
        String newPwd = txtNewPwd.getText();
        String verifyPwd = txtVerifyPwd.getText();
        String username = UserSession.getInstance().getUserName();
        String email = UserSession.getInstance().getEmail();
        System.out.println(username + " is the username");
        CredentialManager credentialManager = new CredentialManager();
    

        if (oldPwd.equals("") || newPwd.equals("") || verifyPwd.equals("")) {
            txtStatus.setText("Please fill in all fields");
        } else if (!newPwd.equals(verifyPwd)) {
            txtStatus.setText("New passwords do not match");
        } else if (newPwd.equals(oldPwd)) {
            txtStatus.setText("New password cannot be the same as old password");
        } else if (newPwd.length() < 8 || newPwd.length() > 64) {
            txtStatus.setText("New password must be between 8 and 64 characters");
        } else if (credentialManager.verifyPassword(username, oldPwd)){
            
            EmailManager emailManager = new EmailManager();
            LoginResult result = emailManager.verifyLogin(email);

            if (result == LoginResult.SUCCESSFUL) {
                // Handle successful login
                credentialManager.changePasswordInDB(username, newPwd);
                txtStatus.setText("Password Updated");
                txtStatus.setStroke(javafx.scene.paint.Color.GREEN);

                PauseTransition delay = new PauseTransition(Duration.seconds(2)); // Creates a 2 seconds pause
                delay.setOnFinished( event -> {
                    // Closes the window after the pause
                    Stage stage = (Stage) txtStatus.getScene().getWindow(); 
                    stage.close();
                });
                delay.play();
            } else if (result == LoginResult.WRONG_CODE) {
                // Handle wrong code scenario
                txtStatus.setText("Wrong code input. Email verification cancelled");
                txtStatus.setFill(Color.RED);
                
            } else if (result == LoginResult.CANCELLED) {
                // Handle login cancelled scenario
                txtStatus.setText("Email verification cancelled");
                txtStatus.setFill(Color.RED);
                
            } else {
                // Handle any other result if necessary
                
            }

            
        }
        else {
            txtStatus.setText("Old password is incorrect");
        }
    }

    @FXML
    protected void handlePwdResetAction(ActionEvent event) throws IOException, ClassNotFoundException, SQLException{
        resetPwd();
    }
}
