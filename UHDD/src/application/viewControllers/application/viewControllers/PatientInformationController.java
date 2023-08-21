
package application.viewControllers;

import application.DBConnector;
import application.DataEncryptorDecryptor;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;

public class PatientInformationController {
    private Stage stage;
    private Scene scene;
    @FXML private Label labelStatus = new Label();

    @FXML private Label labelId = new Label("ID");
    @FXML private TextField textId = new TextField();
    @FXML private Label labelLastName = new Label("Last Name");
    @FXML private TextField textLastName = new TextField();
    @FXML private Label labelFirstName = new Label("First Name");
    @FXML private TextField textFirstName = new TextField();
    @FXML private Label labelMiddleName = new Label("Middle Name");
    @FXML private TextField textMiddleName = new TextField();
    @FXML private Label labelAddress = new Label("Address");
    @FXML private TextField textAddress = new TextField();
    @FXML private Label labelCity = new Label("City");
    @FXML private TextField textCity = new TextField();
    @FXML private Label labelState = new Label("State");
    @FXML private TextField textState = new TextField();
    @FXML private Label labelTelephone = new Label("Telephone");
    @FXML private TextField textTelephone = new TextField();
    @FXML private Label labelEmail = new Label("Email");
    @FXML private TextField textEmail = new TextField();
    @FXML private TextField textHealthInsuranceNumber = new TextField();
    @FXML private TextField textEmergencyContactNumber = new TextField();
    @FXML private TextField textAllergies = new TextField();
    @FXML private TextField textPastMedicalConditions = new TextField();
    @FXML private TextField textSurgeriesOrProcedures = new TextField();
    @FXML private TextField textMedications = new TextField();
    @FXML private TextField textImmunisations = new TextField();
    @FXML private TextArea textDetails = new TextArea();
    @FXML private TextArea textFamilyMedicalHistory = new TextArea();
    @FXML private TextArea textProgressNotes = new TextArea();
    @FXML private ImageView QRCode = new ImageView();
    @FXML private ComboBox<String> dd = new ComboBox();
    @FXML private ComboBox<String> mm = new ComboBox();
    @FXML private ComboBox<String> yy = new ComboBox();
    @FXML private DatePicker datePicker = new DatePicker();

    DBConnector dbConnection = new DBConnector();

    @FXML
    public void view(ActionEvent event) throws Exception {
        System.out.println("viewed");
        String query = "SELECT * FROM `testdb`.`test3` WHERE ID = '" + textId.getText().trim() + "'";
        dbConnection.initialiseDB();
        try {
            ResultSet rsView = dbConnection.executeQueryReturnResults(query);
            loadFields(rsView);

        } catch (SQLException Ex){
            labelStatus.setText("Record failed");
            System.out.println(Ex.getMessage());
        }
        dbConnection.closeConnection();
    }

    public void switchToDashBoard(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../fxmlScenes/DashBoard.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToPatientDirectory(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../fxmlScenes/PatientDirectory.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    private void loadFields(ResultSet results) throws Exception {
        if (results.next()) {
            String encryptionKey = results.getString(21);
            textLastName.setText(DataEncryptorDecryptor.decrypt(results.getString(2), encryptionKey));
            textFirstName.setText(DataEncryptorDecryptor.decrypt(results.getString(3), encryptionKey));
            textMiddleName.setText(DataEncryptorDecryptor.decrypt(results.getString(4), encryptionKey));
            textAddress.setText(DataEncryptorDecryptor.decrypt(results.getString(5), encryptionKey));
            textCity.setText(DataEncryptorDecryptor.decrypt(results.getString(6), encryptionKey));
            textState.setText(DataEncryptorDecryptor.decrypt(results.getString(7), encryptionKey));
            textTelephone.setText(DataEncryptorDecryptor.decrypt(results.getString(8), encryptionKey));
            textEmail.setText(DataEncryptorDecryptor.decrypt(results.getString(9), encryptionKey));
            textHealthInsuranceNumber.setText(DataEncryptorDecryptor.decrypt(results.getString(10), encryptionKey));
            textEmergencyContactNumber.setText(DataEncryptorDecryptor.decrypt(results.getString(11), encryptionKey));
            textAllergies.setText(DataEncryptorDecryptor.decrypt(results.getString(12), encryptionKey));
            textPastMedicalConditions.setText(DataEncryptorDecryptor.decrypt(results.getString(13), encryptionKey));
            textSurgeriesOrProcedures.setText(DataEncryptorDecryptor.decrypt(results.getString(14), encryptionKey));
            textMedications.setText(DataEncryptorDecryptor.decrypt(results.getString(15), encryptionKey));
            textImmunisations.setText(DataEncryptorDecryptor.decrypt(results.getString(16), encryptionKey));
            textDetails.setText(DataEncryptorDecryptor.decrypt(results.getString(17), encryptionKey));
            textFamilyMedicalHistory.setText(DataEncryptorDecryptor.decrypt(results.getString(18), encryptionKey));
            textProgressNotes.setText(DataEncryptorDecryptor.decrypt(results.getString(19), encryptionKey));
            datePicker.setPromptText(results.getString(20));
            labelStatus.setText("Record found");

            labelStatus.setTextFill(Color.GREEN);
        } else {
            textLastName.setText("");
            textFirstName.setText("");
            textMiddleName.setText("");
            textAddress.setText("");
            textCity.setText("");
            textState.setText("");
            textTelephone.setText("");
            textEmail.setText("");
            datePicker.setPromptText("This cannot be empty when adding!!!");;
            textHealthInsuranceNumber.setText("");
            textEmergencyContactNumber.setText("");
            textAllergies.setText("");
            textPastMedicalConditions.setText("");
            textSurgeriesOrProcedures.setText("");
            textMedications.setText("");
            textImmunisations.setText("");
            textDetails.setText("");
            textFamilyMedicalHistory.setText("");
            textProgressNotes.setText("");
            labelStatus.setText("Record not found");
            labelStatus.setTextFill(Color.RED);
        }
    }

    @FXML
    private void insert(ActionEvent event) throws Exception {
        dbConnection.initialiseDB();

        String encryptionKey = DataEncryptorDecryptor.generateAESKey();

        String insertQuery1 = "INSERT INTO `testdb`.`test3` (ID, FirstName, MiddleName, LastName, Address, City, "
                + "State, Telephone, Email, HealthInsuranceNumber, EmergencyContactNumber, Allergies, "
                + "PastMedicalConditions, SurgeriesOrProcedures, Medications, Immunisations, Details, FamilyMedicalHistory, ProgressNotes, DateOfBirth, EncryptionKey) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement statement = dbConnection.connection.prepareStatement(insertQuery1);
        statement.setString(1, textId.getText().trim());

        statement.setString(2, DataEncryptorDecryptor.encrypt(textFirstName.getText().trim(), encryptionKey));
        statement.setString(3, DataEncryptorDecryptor.encrypt(textMiddleName.getText().trim(), encryptionKey));
        statement.setString(4, DataEncryptorDecryptor.encrypt(textLastName.getText().trim(), encryptionKey));
        statement.setString(5, DataEncryptorDecryptor.encrypt(textAddress.getText().trim(), encryptionKey));
        statement.setString(6, DataEncryptorDecryptor.encrypt(textCity.getText().trim(), encryptionKey));
        statement.setString(7, DataEncryptorDecryptor.encrypt(textState.getText().trim(), encryptionKey));
        statement.setString(8, DataEncryptorDecryptor.encrypt(textTelephone.getText().trim(), encryptionKey));
        statement.setString(9, DataEncryptorDecryptor.encrypt(textEmail.getText().trim(), encryptionKey));
        statement.setString(10, DataEncryptorDecryptor.encrypt(textHealthInsuranceNumber.getText().trim(), encryptionKey));
        statement.setString(11, DataEncryptorDecryptor.encrypt(textEmergencyContactNumber.getText().trim(), encryptionKey));
        statement.setString(12, DataEncryptorDecryptor.encrypt(textAllergies.getText().trim(), encryptionKey));
        statement.setString(13, DataEncryptorDecryptor.encrypt(textPastMedicalConditions.getText().trim(), encryptionKey));
        statement.setString(14, DataEncryptorDecryptor.encrypt(textSurgeriesOrProcedures.getText().trim(), encryptionKey));
        statement.setString(15, DataEncryptorDecryptor.encrypt(textMedications.getText().trim(), encryptionKey));
        statement.setString(16, DataEncryptorDecryptor.encrypt(textImmunisations.getText().trim(), encryptionKey));
        statement.setString(17, DataEncryptorDecryptor.encrypt(textDetails.getText().trim(), encryptionKey));
        statement.setString(18, DataEncryptorDecryptor.encrypt(textFamilyMedicalHistory.getText().trim(), encryptionKey));
        statement.setString(19, DataEncryptorDecryptor.encrypt(textProgressNotes.getText().trim(), encryptionKey));
        statement.setString(20, datePicker.getValue().toString());
        statement.setString(21, encryptionKey);

        try {
            statement.executeUpdate();
            labelStatus.setText("Insert completed");
            labelStatus.setTextFill(Color.GREEN);
            System.out.println("Insert succeeded");

        } catch (SQLException Ex){
            labelStatus.setText("Insert failed");
            labelStatus.setTextFill(Color.RED);
            System.out.println(Ex.getMessage());
        }
        dbConnection.closeConnection();
    }

    @FXML
    private void clear(ActionEvent event) {
        textId.setText("");
        textLastName.setText("");
        textFirstName.setText("");
        textMiddleName.setText("");
        textAddress.setText("");
        textCity.setText("");
        textState.setText("");
        textTelephone.setText("");
        textEmail.setText("");
        datePicker.setPromptText("This cannot be empty when adding!!!");
        textHealthInsuranceNumber.setText("");
        textEmergencyContactNumber.setText("");
        textAllergies.setText("");
        textPastMedicalConditions.setText("");
        textSurgeriesOrProcedures.setText("");
        textMedications.setText("");
        textImmunisations.setText("");
        textDetails.setText("");
        textFamilyMedicalHistory.setText("");
        textProgressNotes.setText("");
    }

    @FXML
    private void update(ActionEvent event) throws Exception {
        dbConnection.initialiseDB();
        String newEncryptionKey = DataEncryptorDecryptor.generateAESKey();

        String updateQuery = "UPDATE `testdb`.`test3` SET FirstName = ?, MiddleName = ?, LastName = ?, Address = ?, City = ?, State = ?, Telephone = ?, Email = ?, "
                + "HealthInsuranceNumber = ?, EmergencyContactNumber = ?, Allergies = ?, PastMedicalConditions = ?, SurgeriesOrProcedures = ?, Medications = ?, Immunisations = ?,"
                + "Details =?, FamilyMedicalHistory = ?, ProgressNotes = ?, DateOfBirth = ?, EncryptionKey = ? WHERE ID = ?";
        PreparedStatement statement = dbConnection.connection.prepareStatement(updateQuery);
        statement.setString(1, DataEncryptorDecryptor.encrypt(textFirstName.getText().trim(), newEncryptionKey));
        statement.setString(2, DataEncryptorDecryptor.encrypt(textMiddleName.getText().trim(), newEncryptionKey));
        statement.setString(3, DataEncryptorDecryptor.encrypt(textLastName.getText().trim(), newEncryptionKey));
        statement.setString(4, DataEncryptorDecryptor.encrypt(textAddress.getText().trim(), newEncryptionKey));
        statement.setString(5, DataEncryptorDecryptor.encrypt(textCity.getText().trim(), newEncryptionKey));
        statement.setString(6, DataEncryptorDecryptor.encrypt(textState.getText().trim(), newEncryptionKey));
        statement.setString(7, DataEncryptorDecryptor.encrypt(textTelephone.getText().trim(), newEncryptionKey));
        statement.setString(8, DataEncryptorDecryptor.encrypt(textEmail.getText().trim(), newEncryptionKey));
        statement.setString(9, DataEncryptorDecryptor.encrypt(textHealthInsuranceNumber.getText().trim(), newEncryptionKey));
        statement.setString(10, DataEncryptorDecryptor.encrypt(textEmergencyContactNumber.getText().trim(), newEncryptionKey));
        statement.setString(11, DataEncryptorDecryptor.encrypt(textAllergies.getText().trim(), newEncryptionKey));
        statement.setString(12, DataEncryptorDecryptor.encrypt(textPastMedicalConditions.getText().trim(), newEncryptionKey));
        statement.setString(13, DataEncryptorDecryptor.encrypt(textSurgeriesOrProcedures.getText().trim(), newEncryptionKey));
        statement.setString(14, DataEncryptorDecryptor.encrypt(textMedications.getText().trim(), newEncryptionKey));
        statement.setString(15, DataEncryptorDecryptor.encrypt(textImmunisations.getText().trim(), newEncryptionKey));
        statement.setString(16, DataEncryptorDecryptor.encrypt(textDetails.getText().trim(), newEncryptionKey));
        statement.setString(17, DataEncryptorDecryptor.encrypt(textFamilyMedicalHistory.getText().trim(), newEncryptionKey));
        statement.setString(18, DataEncryptorDecryptor.encrypt(textProgressNotes.getText().trim(), newEncryptionKey));
        statement.setString(19, datePicker.getValue().toString());
        statement.setString(20, newEncryptionKey);
        statement.setString(21, textId.getText().trim());

        try {
            statement.executeUpdate();
            labelStatus.setText("Update completed");
            labelStatus.setTextFill(Color.GREEN);
            System.out.println("Update succeeded");
        } catch (SQLException Ex){
            labelStatus.setText("Update failed");
            labelStatus.setTextFill(Color.RED);
            System.out.println(Ex.getMessage());
        }
        dbConnection.closeConnection();
    }
	
}
