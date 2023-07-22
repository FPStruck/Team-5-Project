package application.viewControllers;

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

    public void resetPwd() {
        String oldPwd = txtOldPwd.getText();
        String newPwd = txtNewPwd.getText();
        String verifyPwd = txtVerifyPwd.getText();
        String username = UserSession.getInstance().getUserName();
        if (oldPwd.equals("") || newPwd.equals("") || verifyPwd.equals("")) {
            txtStatus.setText("Please fill in all fields");
        } else if (!newPwd.equals(verifyPwd)) {
            txtStatus.setText("New passwords do not match");
        } else {
            
        }
    }

}
