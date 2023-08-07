

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import application.DBConnector;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class PatientInfoViewController {
	private Stage stage;
	private Scene scene;
	@FXML
	private Pane patientDirectoryIVPane;
	@FXML
	private Pane appointmentsDBPane;
	@FXML
	private Pane patientNotesDBPane;
	@FXML
	private Pane dashboardIVPane;
	
	//labels that update
	@FXML
	private Text fullnameTXT;
	@FXML
	private Text genderTXT;
	@FXML
	private Text dobTXT;
	@FXML
	private Text locationTXT;
	@FXML
	private Text patientIdTXT;
	@FXML
	private Text phoneNoTXT;
	@FXML 
	private Text emailTXT;
	@FXML
	private Text addressTXT;
	@FXML
	private Text insuranceNumberTXT;
	@FXML
	private Text detailsTXT;
	@FXML
	private Text cityTXT;
	@FXML
	private Text emergencyNoTXT;
	@FXML
	private Button nextPatientBTN;
	@FXML
	private Text viewPatientInfoBtn;
	@FXML
	private Text viewPatientInfoBtn2;
	@FXML
	private Text viewPatientInfoBtn3;
	@FXML
	private Text viewPatientInfoBtn4;
	@FXML
	private Text familyMedicalHistoryTXT;
	@FXML
	private Text progressNotesTXT;
	
	static Integer patient;
	
	DBConnector dbConnector = new DBConnector();
	
	public void setTextFieldsToPatientId(String Id) throws ClassNotFoundException, SQLException {
		dbConnector.initialiseDB();
		ResultSet patientDetails = dbConnector.QueryReturnResultsFromPatientId(Id);
		ResultSetMetaData meta = patientDetails.getMetaData(); // not need at the moment, this will get the mata data such as column size
		
		if(patientDetails.next()) {
			fullnameTXT.setText(patientDetails.getString("FirstName") + " " + patientDetails.getString("MiddleName") + " " + patientDetails.getString("LastName"));
			emailTXT.setText(patientDetails.getString("Email"));
			dobTXT.setText(patientDetails.getString("DateOfBirth"));
			locationTXT.setText(patientDetails.getString("City"));
			patientIdTXT.setText(patientDetails.getString("ID"));
			phoneNoTXT.setText(patientDetails.getString("Telephone"));
			addressTXT.setText(patientDetails.getString("Address"));
			insuranceNumberTXT.setText(patientDetails.getString("HealthInsuranceNumber"));
			detailsTXT.setText(patientDetails.getString("Details"));
			cityTXT.setText(patientDetails.getString("City"));
			emergencyNoTXT.setText(patientDetails.getString("EmergencyContactNumber"));
			familyMedicalHistoryTXT.setText(patientDetails.getString("FamilyMedicalHistory"));
			progressNotesTXT.setText(patientDetails.getString("ProgressNotes"));
			
//			if(patientDetails.getString("gender").equals("M")){
//				genderTXT.setText("Male");
//			} else if(patientDetails.getString("gender").equals("F")) {
//				genderTXT.setText("Female");
//			} else {
//				genderTXT.setText("Other");
//			}
	
			patient++;
		} else patient = 1;
		
		System.out.println(patient);
		
		dbConnector.closeConnection();
	}
	
	@FXML
	public void initialize() throws ClassNotFoundException, SQLException {
		if (patient == null) {
			patient = 1;
		} else patient--;
		setTextFieldsToPatientId(patient.toString());
	}
	
	@FXML
	public void nextPatient() throws ClassNotFoundException, SQLException {
		setTextFieldsToPatientId(patient.toString());
	}
	
	@FXML 	
	public void switchToPatientInformation(MouseEvent mouseEvent) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("/fxmlScenes/PatientInformation.fxml"));		
		stage = (Stage)((Node)mouseEvent.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	@FXML 	
	public void switchToDasboard(MouseEvent mouseEvent) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("/fxmlScenes/Dashboard.fxml"));		
		stage = (Stage)((Node)mouseEvent.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	@FXML 
	public void switchToPatientDirectory(MouseEvent mouseEvent) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("/fxmlScenes/PatientDirectory.fxml"));
		stage = (Stage)((Node)mouseEvent.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	@FXML	
	public void switchToPatientInfoView(MouseEvent mouseEvent) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("/fxmlScenes/PatientInfoView.fxml"));
		stage = (Stage)((Node)mouseEvent.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	@FXML	
	public void switchToPatientInfoView2(MouseEvent mouseEvent) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("/fxmlScenes/PatientInfoView2.fxml"));
		stage = (Stage)((Node)mouseEvent.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	@FXML	
	public void switchToPatientInfoView3(MouseEvent mouseEvent) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("/fxmlScenes/PatientInfoView3.fxml"));
		stage = (Stage)((Node)mouseEvent.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	@FXML	
	public void switchToPatientInfoView4(MouseEvent mouseEvent) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("/fxmlScenes/PatientInfoView4.fxml"));
		stage = (Stage)((Node)mouseEvent.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	@FXML
	public void switchToOverviewTab(MouseEvent mouseEvent) throws IOException {
		// this will change the subheader text to bold and make the text visible 
		viewPatientInfoBtn.setFont(Font.font("System", FontWeight.BOLD, 14));
		viewPatientInfoBtn2.setFont(Font.font("System",  14));
		viewPatientInfoBtn3.setFont(Font.font("System", 14));
		viewPatientInfoBtn4.setFont(Font.font("System", 14));
		familyMedicalHistoryTXT.setVisible(false);
		progressNotesTXT.setVisible(false);
		// overview tab has no text to view as of code written 
	}
	
	@FXML
	public void switchToPatientProgressAndPlanTab(MouseEvent mouseEvent) throws IOException {
		// this will change the subheader text to bold and make the text visible 
		viewPatientInfoBtn.setFont(Font.font("System", 14));
		viewPatientInfoBtn2.setFont(Font.font("System", FontWeight.BOLD, 14));
		viewPatientInfoBtn3.setFont(Font.font("System", 14));
		viewPatientInfoBtn4.setFont(Font.font("System", 14));
		familyMedicalHistoryTXT.setVisible(true);
		progressNotesTXT.setVisible(false);;
	}
	
	@FXML
	public void switchToPatientNotesTab(MouseEvent mouseEvent) throws IOException {
		// this will change the subheader text to bold and make the text visible
		viewPatientInfoBtn.setFont(Font.font("System", 14));
		viewPatientInfoBtn2.setFont(Font.font("System", 14));
		viewPatientInfoBtn3.setFont(Font.font("System",FontWeight.BOLD , 14));
		viewPatientInfoBtn4.setFont(Font.font("System", 14));
		familyMedicalHistoryTXT.setVisible(false);
		progressNotesTXT.setVisible(true);;
	}
	
	@FXML
	public void switchToPatientDocumentsTab(MouseEvent mouseEvent) throws IOException {
		// this will change the subheader text to bold and make the text visible 
		viewPatientInfoBtn.setFont(Font.font("System", 14));
		viewPatientInfoBtn2.setFont(Font.font("System", 14));
		viewPatientInfoBtn3.setFont(Font.font("System", 14));
		viewPatientInfoBtn4.setFont(Font.font("System",FontWeight.BOLD , 14));
		familyMedicalHistoryTXT.setVisible(false);
		progressNotesTXT.setVisible(false);;
		// document tab has no text to view as of code written 
	}
	
	@FXML	
	public void highlightPatientDirectoryPane(MouseEvent mouseEvent) throws IOException {
		patientDirectoryIVPane.setStyle("-fx-background-color: #02181f");
	}
	
	@FXML	
	public void highlightPatientDirectoryPaneOnExit(MouseEvent mouseEvent) throws IOException {
		patientDirectoryIVPane.setStyle("-fx-background-color:  #063847");
	}
	
	@FXML	
	public void highlightAppointmentsPaneOnEnter(MouseEvent mouseEvent) throws IOException {
		appointmentsDBPane.setStyle("-fx-background-color: #02181f");
	}
	
	@FXML	
	public void highlightAppointmentsPaneOnExit(MouseEvent mouseEvent) throws IOException {
		appointmentsDBPane.setStyle("-fx-background-color:  #063847");
	}
	
	@FXML	
	public void highlightPatientNotesPaneOnEnter(MouseEvent mouseEvent) throws IOException {
		patientNotesDBPane.setStyle("-fx-background-color: #02181f");
	}
	
	@FXML	
	public void highlightPatientNotesPaneOnExit(MouseEvent mouseEvent) throws IOException {
		patientNotesDBPane.setStyle("-fx-background-color:  #063847");
	}
	
	@FXML	
	public void highlightIVDashboardPaneOnEnter(MouseEvent mouseEvent) throws IOException {
		dashboardIVPane.setStyle("-fx-background-color: #02181f");
	}
	
	@FXML	
	public void highlightIVDashboardPaneOnExit(MouseEvent mouseEvent) throws IOException {
		dashboardIVPane.setStyle("-fx-background-color:  #063847");
	}
}
