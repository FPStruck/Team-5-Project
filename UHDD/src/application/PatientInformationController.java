package application;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


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
	
	
	@FXML public void view(ActionEvent event) throws ClassNotFoundException, SQLException, FileNotFoundException {
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
		Parent root = FXMLLoader.load(getClass().getResource("\\fxmlScenes\\DashBoard.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	public void switchToPatientDirectory(ActionEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("\\fxmlScenes\\PatientDirectory.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	private void loadFields(ResultSet results) throws SQLException {
		if (results.next()) {
			textLastName.setText(results.getString(2));
			textFirstName.setText(results.getString(3));
			textMiddleName.setText(results.getString(4));
			textAddress.setText(results.getString(5));
			textCity.setText(results.getString(6));
			textState.setText(results.getString(7));
			textTelephone.setText(results.getString(8));
			textEmail.setText(results.getString(9));
			textHealthInsuranceNumber.setText(results.getString(10));
			textEmergencyContactNumber.setText(results.getString(11));
			textAllergies.setText(results.getString(12));
			textPastMedicalConditions.setText(results.getString(13));
			textSurgeriesOrProcedures.setText(results.getString(14));
			textMedications.setText(results.getString(15));
			textImmunisations.setText(results.getString(16));
			textDetails.setText(results.getString(17));
			textFamilyMedicalHistory.setText(results.getString(18));
			textProgressNotes.setText(results.getString(19));
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
	private void insert(ActionEvent event) throws SQLException, ClassNotFoundException {
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
		
		// new query easier to read
		String insertQuery1 = "INSERT INTO `testdb`.`test3` (ID, FirstName, MiddleName, LastName, Address, City, "
				+ "State, Telephone, Email, HealthInsuranceNumber, EmergencyContactNumber, Allergies, "
				+ "PastMedicalConditions, SurgeriesOrProcedures, Medications, Immunisations, Details, FamilyMedicalHistory, ProgressNotes, DateOfBirth) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		PreparedStatement statement = dbConnection.connection.prepareStatement(insertQuery1);
		statement.setString(1, textId.getText().trim());
		statement.setString(2, textFirstName.getText().trim());
		statement.setString(3, textMiddleName.getText().trim());
		statement.setString(4, textLastName.getText().trim());
		statement.setString(5, textAddress.getText().trim());
		statement.setString(6, textCity.getText().trim());
		statement.setString(7, textState.getText().trim());
		statement.setString(8, textTelephone.getText().trim());
		statement.setString(9, textEmail.getText().trim());
		statement.setString(10, textHealthInsuranceNumber.getText().trim());
		statement.setString(11, textEmergencyContactNumber.getText().trim());
		statement.setString(12, textAllergies.getText().trim());
		statement.setString(13, textPastMedicalConditions.getText().trim());
		statement.setString(14, textSurgeriesOrProcedures.getText().trim());
		statement.setString(15, textMedications.getText().trim());
		statement.setString(16, textImmunisations.getText().trim());
		statement.setString(17, textDetails.getText().trim());
		statement.setString(18, textFamilyMedicalHistory.getText().trim());
		statement.setString(19, textProgressNotes.getText().trim());
		statement.setString(20, datePicker.getValue().toString());
		 
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
	private void update(ActionEvent event) throws ClassNotFoundException, SQLException {
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
		
		// new query easier to read
		String updateQuery1 = "UPDATE `testdb`.`test3` SET FirstName = ?, MiddleName = ?, LastName = ?, Address = ?, City = ?, State = ?, Telephone = ?, Email = ?, "
				+ "HealthInsuranceNumber = ?, EmergencyContactNumber = ?, Allergies = ?, PastMedicalConditions = ?, SurgeriesOrProcedures = ?, Medications = ?, Immunisations = ?,"
				+ "Details =?, FamilyMedicalHistory = ?, ProgressNotes = ?, DateOfBirth = ? WHERE ID = ?";
		PreparedStatement statement = dbConnection.connection.prepareStatement(updateQuery1);
		statement.setString(1, textFirstName.getText().trim());
		statement.setString(2, textMiddleName.getText().trim());
		statement.setString(3, textLastName.getText().trim());
		statement.setString(4, textAddress.getText().trim());
		statement.setString(5, textCity.getText().trim());
		statement.setString(6, textState.getText().trim());
		statement.setString(7, textTelephone.getText().trim());
		statement.setString(8, textEmail.getText().trim());
		statement.setString(9, textHealthInsuranceNumber.getText().trim());
		statement.setString(10, textEmergencyContactNumber.getText().trim());
		statement.setString(11, textAllergies.getText().trim());
		statement.setString(12, textPastMedicalConditions.getText().trim());
		statement.setString(13, textSurgeriesOrProcedures.getText().trim());
		statement.setString(14, textMedications.getText().trim());
		statement.setString(15, textImmunisations.getText().trim());
		statement.setString(16, textDetails.getText().trim());
		statement.setString(17, textFamilyMedicalHistory.getText().trim());
		statement.setString(18, textProgressNotes.getText().trim());
		statement.setString(19, datePicker.getValue().toString());
		statement.setString(20, textId.getText().trim());
		
		 
		System.out.println(updateQuery1); // this line is to ensure the query is formatted correctly 
		
		try {
			// execute statement
//			dbConnection.executeUpdate(updateQuery); // old query
			statement.executeUpdate();
			labelStatus.setText("Update completed or does not exist please view the ID");
			labelStatus.setTextFill(Color.GREEN);
			System.out.println("Update suceeded");
			
		} catch (SQLException Ex){
			labelStatus.setText("Update failed");
			labelStatus.setTextFill(Color.RED);
			System.out.println(Ex.getMessage());
		}
		dbConnection.closeConnection();
	}
}
