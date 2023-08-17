package application.viewControllers;

import java.io.FileNotFoundException;
import java.security.Key;
import java.security.SecureRandom;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import application.DBConnector;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

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
	
	
	@FXML public void view(ActionEvent event) throws Exception {
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
			textLastName.setText(decryptAndDecode(results.getString(2), encryptionKey));
			textFirstName.setText(decryptAndDecode(results.getString(3), encryptionKey));
			textMiddleName.setText(decryptAndDecode(results.getString(4), encryptionKey));
			textAddress.setText(decryptAndDecode(results.getString(5), encryptionKey));
			textCity.setText(decryptAndDecode(results.getString(6), encryptionKey));
			textState.setText(decryptAndDecode(results.getString(7), encryptionKey));
			textTelephone.setText(decryptAndDecode(results.getString(8), encryptionKey));
			textEmail.setText(decryptAndDecode(results.getString(9), encryptionKey));
			textHealthInsuranceNumber.setText(decryptAndDecode(results.getString(10), encryptionKey));
			textEmergencyContactNumber.setText(decryptAndDecode(results.getString(11), encryptionKey));
			textAllergies.setText(decryptAndDecode(results.getString(12), encryptionKey));
			textPastMedicalConditions.setText(decryptAndDecode(results.getString(13), encryptionKey));
			textSurgeriesOrProcedures.setText(decryptAndDecode(results.getString(14), encryptionKey));
			textMedications.setText(decryptAndDecode(results.getString(15), encryptionKey));
			textImmunisations.setText(decryptAndDecode(results.getString(16), encryptionKey));
			textDetails.setText(decryptAndDecode(results.getString(17), encryptionKey));
			textFamilyMedicalHistory.setText(decryptAndDecode(results.getString(18), encryptionKey));
			textProgressNotes.setText(decryptAndDecode(results.getString(19), encryptionKey));
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
		
		// old query to long 
//		String insertQuery = "INSERT INTO `testdb`.`test3` "
//				+ "(ID, FirstName, MiddleName, LastName, Address, City, State, Telephone, Email, Details) "
//				+ "VALUES ('" + textId.getText().trim() + "', '" + textFirstName.getText().trim() + 
//				"', '" + textMiddleName.getText().trim() + "', '" + textLastName.getText().trim() + 
//				"', '" + textAddress.getText().trim() + "', '" + textCity.getText().trim() +
//				"', '" + textState.getText().trim() + "', '" + textTelephone.getText().trim() + 
//				"', '" + textEmail.getText().trim() + "', '" + textDetails.getText().trim() + "');";
//		
//		System.out.println(insertQuery); // this line is to ensure the query is formatted correctly 
		
		String encryptionKey = generateAESKey();
		    
		// new query easier to read
		String insertQuery1 = "INSERT INTO `testdb`.`test3` (ID, FirstName, MiddleName, LastName, Address, City, "
				+ "State, Telephone, Email, HealthInsuranceNumber, EmergencyContactNumber, Allergies, "
				+ "PastMedicalConditions, SurgeriesOrProcedures, Medications, Immunisations, Details, FamilyMedicalHistory, ProgressNotes, DateOfBirth, EncryptionKey) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		PreparedStatement statement = dbConnection.connection.prepareStatement(insertQuery1);
		statement.setString(1, textId.getText().trim());
		
		statement.setString(2, Base64.getEncoder().encodeToString(encrypt(textFirstName.getText().trim(), encryptionKey)));
		statement.setString(3, Base64.getEncoder().encodeToString(encrypt(textMiddleName.getText().trim(), encryptionKey)));
		statement.setString(4, Base64.getEncoder().encodeToString(encrypt(textLastName.getText().trim(), encryptionKey)));
		statement.setString(5, Base64.getEncoder().encodeToString(encrypt(textAddress.getText().trim(), encryptionKey)));
		statement.setString(6, Base64.getEncoder().encodeToString(encrypt(textCity.getText().trim(), encryptionKey)));
		statement.setString(7, Base64.getEncoder().encodeToString(encrypt(textState.getText().trim(), encryptionKey)));
		statement.setString(8, Base64.getEncoder().encodeToString(encrypt(textTelephone.getText().trim(), encryptionKey)));
		statement.setString(9, Base64.getEncoder().encodeToString(encrypt(textEmail.getText().trim(), encryptionKey)));
		statement.setString(10, Base64.getEncoder().encodeToString(encrypt(textHealthInsuranceNumber.getText().trim(), encryptionKey)));
		statement.setString(11, Base64.getEncoder().encodeToString(encrypt(textEmergencyContactNumber.getText().trim(), encryptionKey)));
		statement.setString(12, Base64.getEncoder().encodeToString(encrypt(textAllergies.getText().trim(), encryptionKey)));
		statement.setString(13, Base64.getEncoder().encodeToString(encrypt(textPastMedicalConditions.getText().trim(), encryptionKey)));
		statement.setString(14, Base64.getEncoder().encodeToString(encrypt(textSurgeriesOrProcedures.getText().trim(), encryptionKey)));
		statement.setString(15, Base64.getEncoder().encodeToString(encrypt(textMedications.getText().trim(), encryptionKey)));
		statement.setString(16, Base64.getEncoder().encodeToString(encrypt(textImmunisations.getText().trim(), encryptionKey)));
		statement.setString(17, Base64.getEncoder().encodeToString(encrypt(textDetails.getText().trim(), encryptionKey)));
		statement.setString(18, Base64.getEncoder().encodeToString(encrypt(textFamilyMedicalHistory.getText().trim(), encryptionKey)));
		statement.setString(19, Base64.getEncoder().encodeToString(encrypt(textProgressNotes.getText().trim(), encryptionKey)));
		statement.setString(20, datePicker.getValue().toString());		
		statement.setString(21, encryptionKey);

		System.out.println(insertQuery1); // this line is to ensure the query is formatted correctly 
		
		try {
			// execute statement
//			dbConnection.executeUpdate(insertQuery); // used with old query
			statement.executeUpdate();
			labelStatus.setText("Insert completed");
			labelStatus.setTextFill(Color.GREEN);
			System.out.println("Insert suceeded");
			
		} catch (SQLException Ex){
			labelStatus.setText("Insert failed");
			labelStatus.setTextFill(Color.RED);
			System.out.println(Ex.getMessage());
		} 
		dbConnection.closeConnection();
	}
	
	@FXML 
	private void clear(ActionEvent event) {
		// clear the input text
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
		// old query to long and hard to maintain 
//		String updateQuery = "UPDATE `testdb`.`test3` SET "
//				+ "FirstName = '" + textFirstName.getText().trim() + 
//				"', MiddleName = '" + textMiddleName.getText().trim()+ 
//				"', LastName = '" + textLastName.getText().trim() + 
//				"', Address = '" + textAddress.getText().trim() + 
//				"', City = '" + textCity.getText().trim() +
//				"', State = '" + textState.getText().trim() + 
//				"', Telephone = '" + textTelephone.getText().trim() + 
//				"', Email = '" + textEmail.getText().trim() + 
//				"' WHERE ID = '" + textId.getText().trim() + "';";
//		
//		System.out.println(updateQuery);
		
		
		String newEncryptionKey = generateAESKey();
		
		// new query easier to read
		String updateQuery = "UPDATE `testdb`.`test3` SET FirstName = ?, MiddleName = ?, LastName = ?, Address = ?, City = ?, State = ?, Telephone = ?, Email = ?, "
	            + "HealthInsuranceNumber = ?, EmergencyContactNumber = ?, Allergies = ?, PastMedicalConditions = ?, SurgeriesOrProcedures = ?, Medications = ?, Immunisations = ?,"
	            + "Details =?, FamilyMedicalHistory = ?, ProgressNotes = ?, DateOfBirth = ?, EncryptionKey = ? WHERE ID = ?";
	    PreparedStatement statement = dbConnection.connection.prepareStatement(updateQuery);
	    statement.setString(1, Base64.getEncoder().encodeToString(encrypt(textFirstName.getText().trim(), newEncryptionKey)));
	    statement.setString(2, Base64.getEncoder().encodeToString(encrypt(textMiddleName.getText().trim(), newEncryptionKey)));
	    statement.setString(3, Base64.getEncoder().encodeToString(encrypt(textLastName.getText().trim(), newEncryptionKey)));
	    statement.setString(4, Base64.getEncoder().encodeToString(encrypt(textAddress.getText().trim(), newEncryptionKey)));
	    statement.setString(5, Base64.getEncoder().encodeToString(encrypt(textCity.getText().trim(), newEncryptionKey)));
	    statement.setString(6, Base64.getEncoder().encodeToString(encrypt(textState.getText().trim(), newEncryptionKey)));
	    statement.setString(7, Base64.getEncoder().encodeToString(encrypt(textTelephone.getText().trim(), newEncryptionKey)));
	    statement.setString(8, Base64.getEncoder().encodeToString(encrypt(textEmail.getText().trim(), newEncryptionKey)));
	    statement.setString(9, Base64.getEncoder().encodeToString(encrypt(textHealthInsuranceNumber.getText().trim(), newEncryptionKey)));
	    statement.setString(10, Base64.getEncoder().encodeToString(encrypt(textEmergencyContactNumber.getText().trim(), newEncryptionKey)));
	    statement.setString(11, Base64.getEncoder().encodeToString(encrypt(textAllergies.getText().trim(), newEncryptionKey)));
	    statement.setString(12, Base64.getEncoder().encodeToString(encrypt(textPastMedicalConditions.getText().trim(), newEncryptionKey)));
	    statement.setString(13, Base64.getEncoder().encodeToString(encrypt(textSurgeriesOrProcedures.getText().trim(), newEncryptionKey)));
	    statement.setString(14, Base64.getEncoder().encodeToString(encrypt(textMedications.getText().trim(), newEncryptionKey)));
	    statement.setString(15, Base64.getEncoder().encodeToString(encrypt(textImmunisations.getText().trim(), newEncryptionKey)));
	    statement.setString(16, Base64.getEncoder().encodeToString(encrypt(textDetails.getText().trim(), newEncryptionKey)));
	    statement.setString(17, Base64.getEncoder().encodeToString(encrypt(textFamilyMedicalHistory.getText().trim(), newEncryptionKey)));
	    statement.setString(18, Base64.getEncoder().encodeToString(encrypt(textProgressNotes.getText().trim(), newEncryptionKey)));
	    statement.setString(19, datePicker.getValue().toString());
	    statement.setString(20, newEncryptionKey);
	    statement.setString(21, textId.getText().trim());

		 
		System.out.println(updateQuery); // this line is to ensure the query is formatted correctly 
		
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
												//ENCRYPTING METHODS
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private static byte[] encrypt(String data, String encryptionKey) throws Exception {
	    SecretKeySpec keySpec = new SecretKeySpec(encryptionKey.getBytes("UTF-8"), "AES");
	    Cipher cipher = Cipher.getInstance("AES");
	    cipher.init(Cipher.ENCRYPT_MODE, keySpec);
	    return cipher.doFinal(data.getBytes("UTF-8"));
	}
	
	private String decryptAndDecode(String encryptedValue, String encryptionKey) throws Exception {
	    byte[] decodedEncryptedValue = Base64.getDecoder().decode(encryptedValue);
	    byte[] decryptedBytes = decrypt(decodedEncryptedValue, encryptionKey);
	    return new String(decryptedBytes, "UTF-8");
	}

	private byte[] decrypt(byte[] encryptedData, String encryptionKey) throws Exception {
	    SecretKeySpec keySpec = new SecretKeySpec(encryptionKey.getBytes("UTF-8"), "AES");
	    Cipher cipher = Cipher.getInstance("AES");
	    cipher.init(Cipher.DECRYPT_MODE, keySpec);
	    return cipher.doFinal(encryptedData);
	}
	
	private String generateAESKey() throws Exception {
	    KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
	    keyGenerator.init(128); // 128-bit key size
	    SecretKey secretKey = keyGenerator.generateKey();
	    byte[] encodedKey = secretKey.getEncoded();
	    return Base64.getEncoder().encodeToString(encodedKey);
	}
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

}
