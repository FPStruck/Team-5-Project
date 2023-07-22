package application.viewControllers;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.cj.protocol.Resultset;

import application.CredentialManager;
import application.DBConnector;
import application.PasswordHash;
import application.PasswordHasher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

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

    DBConnector dbConnector = new DBConnector();
	PasswordHasher passwordHasher = new PasswordHasher();

    
    public void resetPwd() throws SQLException, ClassNotFoundException {
        String oldPwd = txtOldPwd.getText();
        String newPwd = txtNewPwd.getText();
        String verifyPwd = txtVerifyPwd.getText();
        String username = UserSession.getInstance().getUserName();
        System.out.println(username + " is the username");
        dbConnector.initialiseDB();
        System.out.println("DB initialised");
        ResultSet userDetails = dbConnector.QueryReturnResultsFromUser(username);
        userDetails.next();
        PasswordHash passwordHash = PasswordHash.fromString(userDetails.getString("password_hash"), userDetails.getString("password_params"));
        CredentialManager credentialManager = new CredentialManager();
    

        if (oldPwd.equals("") || newPwd.equals("") || verifyPwd.equals("")) {
            txtStatus.setText("Please fill in all fields");
        } else if (!newPwd.equals(verifyPwd)) {
            txtStatus.setText("New passwords do not match");
        } else if (newPwd.equals(oldPwd)) {
            txtStatus.setText("New password cannot be the same as old password");
        } else if (newPwd.length() < 8 || newPwd.length() > 64) {
            txtStatus.setText("New password must be between 8 and 64 characters");
        } else if (passwordHasher.verifyPassword(oldPwd, passwordHash)){
            credentialManager.changePasswordInDB(username, newPwd);
            txtStatus.setText("Password Updated");
        }
        dbConnector.closeConnection();
    }

    @FXML
    protected void handlePwdResetAction(ActionEvent event) throws IOException, ClassNotFoundException, SQLException{
        resetPwd();
    }
}
