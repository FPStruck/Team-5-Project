package application.viewControllers;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import com.google.protobuf.StringValue;

import application.CreateEncryptedPdf;
import application.DBConnector;
import application.Patient;
import application.PatientService;
import javafx.animation.PauseTransition;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import scala.Byte;

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
	@FXML private Text fullnameTXT;
	@FXML private Text genderTXT;
	@FXML private Text dobTXT;
	@FXML private Text locationTXT;
	@FXML private Text patientIdTXT;
	@FXML private Text phoneNoTXT;
	@FXML private Text emailTXT;
	@FXML private Text addressTXT;
	@FXML private Text insuranceNumberTXT;
	@FXML private Text detailsTXT;
	@FXML private Text cityTXT;
	@FXML private Text emergencyNoTXT;
	@FXML private Button nextPatientBTN;
	@FXML private Button btnPDFCreate;
	@FXML private Text viewPatientInfoBtn;
	@FXML private Text viewPatientInfoBtn2;
	@FXML private Text viewPatientInfoBtn3;
	@FXML private Text viewPatientInfoBtn4;
	@FXML private Text familyMedicalHistoryTXT;
	@FXML private Text progressNotesTXT;

	//Prescribed Medication Pane 
	@FXML private Text medName1;
	@FXML private Text medName2;
	@FXML private Text medName3;
	@FXML private Text medExpiry1;
	@FXML private Text medExpiry2;
	@FXML private Text medExpiry3;
	

	//Diagnosis Pane
	@FXML private Text diagnosisTxt1;
	@FXML private Text diagnosisTxt2;
	@FXML private Text diagnosisTxt3;
	@FXML private Text diagnosisDateTxt1;
	@FXML private Text diagnosisDateTxt2;
	@FXML private Text diagnosisDateTxt3;

	//Patient Notes
	@FXML private Text txtNoteDate1;
	@FXML private Text txtNote1;
	@FXML private Text txtNoteDate2;
	@FXML private Text txtNote2;
	@FXML private Text txtNoteDate3;
	@FXML private Text txtNote3;
	@FXML private Text txtScriptInc1;
	@FXML private Text txtScriptInc2;
	@FXML private Text txtScriptInc3;

	//Patient Notes PopUp
	@FXML private Text txtEnterNoteAsId;
	@FXML private Text txtNotePatientName;
	@FXML private TextArea txtAreaNote;
	@FXML private RadioButton radBtnYes;
	@FXML private RadioButton radBtnNo;
	@FXML private TextField inMedicationName;
	@FXML private TextField inScriptValidDays;
	@FXML private Button btnNoteSaveNClose;
	@FXML private Text txtCharCountWarn;
	private static final int MAX_LENGTH = 500;
	@FXML private Text txtNonIntWarn;


	String currentFXML;
	static Integer patient;
	
	DBConnector dbConnector = new DBConnector();
	
	public void setTextFieldsToPatientId(String Id) throws ClassNotFoundException, SQLException {
		dbConnector.initialiseDB();
		ResultSet patientDetails = dbConnector.QueryReturnResultsFromPatientId(Id);
		ResultSetMetaData meta = patientDetails.getMetaData(); // not need at the moment, this will get the mata data such as column size
		
		if(patientDetails.next()) {
			fullnameTXT.setText(patientDetails.getString("FirstName") + " " + patientDetails.getString("MiddleName") + " " + patientDetails.getString("LastName"));		
			dobTXT.setText(patientDetails.getString("DateOfBirth"));
			locationTXT.setText(patientDetails.getString("City"));
			patientIdTXT.setText(patientDetails.getString("ID"));
			insuranceNumberTXT.setText(patientDetails.getString("HealthInsuranceNumber"));
			detailsTXT.setText(patientDetails.getString("Details"));	
			familyMedicalHistoryTXT.setText(patientDetails.getString("FamilyMedicalHistory"));
			progressNotesTXT.setText(patientDetails.getString("ProgressNotes"));
	
			patient++;
		} else patient = 1;
		
		System.out.println(patient);
		
		dbConnector.closeConnection();
	}

	public void setPatientOverviewTxtFields (Patient patient){
		fullnameTXT.setText(patient.getGivenName() + " " + patient.getMiddleName() + " " + patient.getFamilyName());
		genderTXT.setText(patient.getGender());
		dobTXT.setText(patient.getDateOfBirth());
		locationTXT.setText(patient.getCity());
		patientIdTXT.setText(String.valueOf(patient.getId()));
		insuranceNumberTXT.setText(patient.getHealthInsuranceNumber());
		//currentFXML = CurrentFXMLInstance.getInstance().getCurrentFXML();
		System.out.println(currentFXML + " on set patient overview");
		if(currentFXML.equals("../fxmlScenes/PatientInfoViewOverview.fxml")){
			phoneNoTXT.setText(patient.getTelephone());
			emailTXT.setText(patient.getEmail());
			addressTXT.setText(patient.getAddress());
			cityTXT.setText(patient.getCity());
			emergencyNoTXT.setText(patient.getEmergencyContactNumber());
		}
	}

	public void setDiagnosisOverviewTxtFields (Patient patient) throws SQLException, ClassNotFoundException {
		dbConnector.initialiseDB();
		ResultSet diagnosisResultSet = dbConnector.QueryReturnResultsDiagnosisFromPatientId(String.valueOf(patient.getId()));
		int count = 0;
		while(diagnosisResultSet.next() && count < 3){
			String diagnosisName = diagnosisResultSet.getString("diagnosisName");
			LocalDate diagnosisDate = null;
			if (diagnosisResultSet.getDate("diagnosedDate") != null) {
				diagnosisDate = diagnosisResultSet.getDate("diagnosedDate").toLocalDate();
				switch(count) {
				case 0:
					diagnosisTxt1.setText(diagnosisName);
					diagnosisDateTxt1.setText(diagnosisDate.toString());
					break;
				case 1:
					diagnosisTxt2.setText(diagnosisName);
					diagnosisDateTxt2.setText(diagnosisDate.toString());
					break;
				case 2:
					diagnosisTxt3.setText(diagnosisName);
					diagnosisDateTxt3.setText(diagnosisDate.toString());
					break;
			}
			count++;
			} 
					
		}
		switch(count){
			case 0:
				diagnosisTxt1.setText("This patient currently has no diagnoses.");
				diagnosisTxt1.setFont(Font.font("System", FontWeight.BOLD, 12));
				diagnosisTxt1.setFill(Color.web("#9a9797"));
				diagnosisDateTxt1.setText("");
				diagnosisTxt2.setText("");
				diagnosisDateTxt2.setText("");
				diagnosisTxt3.setText("");
				diagnosisDateTxt3.setText("");
				break;
			case 1:
				diagnosisTxt2.setText("");
				diagnosisDateTxt2.setText("");
				diagnosisTxt3.setText("");
				diagnosisDateTxt3.setText("");
				break;
			case 2:
				diagnosisTxt3.setText("");
				diagnosisDateTxt3.setText("");
				break;
		}
	}

	public void setMedicationOverviewTxtFields (Patient patient) throws SQLException, ClassNotFoundException{
		dbConnector.initialiseDB();
		ResultSet medicationResultSet = dbConnector.QueryReturnResultsMedicationFromPatientId(String.valueOf(patient.getId()));
		int count = 0;
		while(medicationResultSet.next() && count < 3){
			String medicationName = medicationResultSet.getString("medication_name");
			LocalDate expiredDate = null;
			if (medicationResultSet.getDate("expired_date") != null) {
				expiredDate = medicationResultSet.getDate("expired_date").toLocalDate();
				switch(count) {
				case 0:
					medName1.setText(medicationName);
					medExpiry1.setText(expiredDate.toString());
					break;
				case 1:
					medName2.setText(medicationName);
					medExpiry2.setText(expiredDate.toString());
					break;
				case 2:
					medName3.setText(medicationName);
					medExpiry3.setText(expiredDate.toString());
					break;
			}
			count++;
			}
					
		}
		switch(count) {
				case 0:
					medName1.setText("No medication currently prescribed for this patient");
					medName1.setFont(Font.font("System", FontWeight.BOLD, 12));
					medName1.setFill(Color.web("#9a9797"));
					medExpiry1.setText("");
					medName2.setText("");
					medExpiry2.setText("");
					medName3.setText("");
					medExpiry3.setText("");
					break;
				case 1:
					medName2.setText("");
					medExpiry2.setText("");
					medName3.setText("");
					medExpiry3.setText("");
					break;
				case 2:
					medName3.setText("");
					medExpiry3.setText("");
					break;
		}
		dbConnector.closeConnection();
	}

	public void setNoteSummaryDetails(Patient patient) throws SQLException, ClassNotFoundException{
		dbConnector.initialiseDB();
		ResultSet noteResultSet = dbConnector.QueryReturnResultsNotesFromPatientId(String.valueOf(patient.getId()));
		
		int count = 0;
		while(noteResultSet.next() && count < 3){
			String noteSummary = noteResultSet.getString("noteText");
			LocalDate noteDate = null;
			if (noteResultSet.getDate("noteEnteredDate") != null) {
				noteDate = noteResultSet.getDate("noteEnteredDate").toLocalDate();
				int scriptInc = noteResultSet.getInt("scriptIncluded");
				if(noteSummary.length() >= 200){
				String trimmedNoteSummary = noteSummary.substring(0, 200) + "...";
				noteSummary = trimmedNoteSummary;
				}
				String scriptTxString = "No";
				if(scriptInc == 1){
					scriptTxString = "Yes";
				}
				switch(count) {
					case 0:
						txtNote1.setText(noteSummary);
						txtNoteDate1.setText(noteDate.toString());
						txtScriptInc1.setText(scriptTxString);
						break;
					case 1:
						txtNote2.setText(noteSummary);
						txtNoteDate2.setText(noteDate.toString());
						txtScriptInc2.setText(scriptTxString);
						break;	
					case 2:
						txtNote3.setText(noteSummary);
						txtNoteDate3.setText(noteDate.toString());
						txtScriptInc3.setText(scriptTxString);
						break;
					
				}
				count++;
			}
					
		}
		switch(count) {
				case 0:
					txtNote1.setText("No notes have been added to this patient's record.");
					txtNote1.setFont(Font.font("System", FontWeight.BOLD, 12));
					txtNote1.setFill(Color.web("#9a9797"));
					txtNoteDate1.setText("");
					txtScriptInc1.setText("");
					txtNoteDate2.setText("");
					txtScriptInc2.setText("");
					txtNote2.setText("");
					txtNoteDate3.setText("");
					txtScriptInc3.setText("");
					txtNote3.setText("");
					break;
				case 1:
					txtNoteDate2.setText("");
					txtScriptInc2.setText("");
					txtNote2.setText("");
					txtNoteDate3.setText("");
					txtScriptInc3.setText("");
					txtNote3.setText("");
					break;
				case 2:
					txtNoteDate3.setText("");
					txtScriptInc3.setText("");
					txtNote3.setText("");
					break;
		}
		dbConnector.closeConnection();
	}

	@FXML
	public void saveAndAddNote() throws ClassNotFoundException, SQLException{
		dbConnector.initialiseDB();
		Patient patient = PatientService.getInstance().getCurrentPatient();
		String noteText = txtAreaNote.getText();
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		String formatDateTime = now.format(formatter);
		int scriptIncluded = 0;
		if(radBtnYes.isSelected()){
			String MedicationName = inMedicationName.getText();
			String input = inScriptValidDays.getText();
			try {
				int value = Integer.parseInt(input);
				if (value < 1 || value > 730) {
					// Value is not between 1 and 730, handle this case
					txtNonIntWarn.setText(input + " is not between 1 and 730");
					txtNonIntWarn.setVisible(true);
				} else {
					// Value is valid
					LocalDateTime futureDate = now.plusDays(value);
					String formatFutureDate = futureDate.format(formatter);
					dbConnector.createNewMedicationExecuteQuery(String.valueOf(patient.getId()), MedicationName, formatDateTime, formatFutureDate);
					scriptIncluded = 1;
				}
			} catch (NumberFormatException e) {
				// Input is not an integer, handle this case
				txtNonIntWarn.setText(input + " is not an integer");
				txtNonIntWarn.setVisible(true);
			}
		}
		dbConnector.createNewNoteExecuteQuery(String.valueOf(patient.getId()),"105", noteText, formatDateTime, String.valueOf(scriptIncluded));
		dbConnector.closeConnection();
		PauseTransition delay = new PauseTransition(Duration.seconds(2)); // Creates a 2 seconds pause
			delay.setOnFinished( event -> {
				// Closes the window after the pause
				Stage stage = (Stage) txtAreaNote.getScene().getWindow(); 
				stage.close();
			});
		delay.play();
	}
	
	@FXML
	public void initialize() throws ClassNotFoundException, SQLException {
		
		if (patient == null) {
			patient = 1;
		} else patient--;
		//setTextFieldsToPatientId(patient.toString());
		Patient patientNew = PatientService.getInstance().getCurrentPatient();
		currentFXML = CurrentFXMLInstance.getInstance().getCurrentFXML();
		if(currentFXML.equals("../fxmlScenes/PopUpAddPatientNote.fxml")){
			txtNotePatientName.setText("Patient Name: " + patientNew.getGivenName() + " " + patientNew.getFamilyName());
			txtAreaNote.textProperty().addListener((observable, oldValue, newValue) -> {
				if (newValue != null && newValue.length() > MAX_LENGTH) {
					txtAreaNote.setText(oldValue);
					txtCharCountWarn.setVisible(true);
				} else {
					txtCharCountWarn.setVisible(false);
				}
			});
		} else {setPatientOverviewTxtFields(patientNew);}
		
		if(currentFXML.equals("../fxmlScenes/PatientInfoViewOverview.fxml")){
			setMedicationOverviewTxtFields(patientNew);
			setDiagnosisOverviewTxtFields(patientNew);
		} else if (currentFXML.equals("../fxmlScenes/PatientInfoViewPatientNotes.fxml")){
			setNoteSummaryDetails(patientNew);
		}
		
	}
	@FXML
	public void addPatientNote(MouseEvent mouseEvent) throws IOException{
			currentFXML = "../fxmlScenes/PopUpAddPatientNote.fxml";
			CurrentFXMLInstance.getInstance().setCurrentFXML(currentFXML);
			Stage popupStage = new Stage();
            Parent popupRoot = FXMLLoader.load(getClass().getResource(currentFXML));
            Scene popupScene = new Scene(popupRoot);
            popupStage.setScene(popupScene);
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.showAndWait();
	}


	@FXML
	public void nextPatient() throws ClassNotFoundException, SQLException {
		setTextFieldsToPatientId(patient.toString());
	}
	
	@FXML 	
	public void switchToPatientInformation(MouseEvent mouseEvent) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("../fxmlScenes/PatientInformation.fxml"));		
		stage = (Stage)((Node)mouseEvent.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	@FXML 	
	public void switchToDasboard(MouseEvent mouseEvent) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("../fxmlScenes/Dashboard.fxml"));		
		stage = (Stage)((Node)mouseEvent.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	@FXML 
	public void switchToPatientDirectory(MouseEvent mouseEvent) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("../fxmlScenes/PatientDirectory.fxml"));
		stage = (Stage)((Node)mouseEvent.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	@FXML	
	public void switchToPatientInfoView(MouseEvent mouseEvent) throws IOException {
	    currentFXML = "../fxmlScenes/PatientInfoViewOverview.fxml";
		CurrentFXMLInstance.getInstance().setCurrentFXML(currentFXML);
	    FXMLLoader loader = new FXMLLoader(getClass().getResource(currentFXML));
	    Parent root = loader.load();
	    Map<String, Object> namespace = loader.getNamespace();

	    stage = (Stage)((Node)mouseEvent.getSource()).getScene().getWindow();
	    scene = new Scene(root);
	    stage.setScene(scene);
	    stage.show();
	    System.out.println(currentFXML);
	}
	
	@FXML	
	public void switchToPatientInfoViewProgressPlan(MouseEvent mouseEvent) throws IOException {
		currentFXML = "../fxmlScenes/PatientInfoViewProgressPlan.fxml";
		CurrentFXMLInstance.getInstance().setCurrentFXML(currentFXML); 
		FXMLLoader loader = new FXMLLoader(getClass().getResource(currentFXML));
		Parent root = loader.load();
		Map<String, Object> namespace = loader.getNamespace();
	
		stage = (Stage)((Node)mouseEvent.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
		System.out.println(currentFXML);
	}
	
	@FXML	
	public void switchToPatientInfoViewPatientNotes(MouseEvent mouseEvent) throws IOException {
	    currentFXML = "../fxmlScenes/PatientInfoViewPatientNotes.fxml";
		CurrentFXMLInstance.getInstance().setCurrentFXML(currentFXML);
	    FXMLLoader loader = new FXMLLoader(getClass().getResource(currentFXML));
	    Parent root = loader.load();
	    Map<String, Object> namespace = loader.getNamespace();

	    stage = (Stage)((Node)mouseEvent.getSource()).getScene().getWindow();
	    scene = new Scene(root);
	    stage.setScene(scene);
	    stage.show();
	    System.out.println(currentFXML);
	}
	
	@FXML	
	public void switchToPatientInfoViewDocuments(MouseEvent mouseEvent) throws IOException {
	    currentFXML = "../fxmlScenes/PatientInfoViewDocuments.fxml";
		CurrentFXMLInstance.getInstance().setCurrentFXML(currentFXML);
	    FXMLLoader loader = new FXMLLoader(getClass().getResource(currentFXML));
	    Parent root = loader.load();
	    Map<String, Object> namespace = loader.getNamespace();

	    stage = (Stage)((Node)mouseEvent.getSource()).getScene().getWindow();
	    scene = new Scene(root);
	    stage.setScene(scene);
	    stage.show();
	    System.out.println(currentFXML);
	}
	
	@FXML 
	public void createPDF(MouseEvent mouseEvent) throws Exception {
		String userProfile = System.getenv("USERPROFILE");
		String filePath = userProfile + "\\Documents\\encryptedPdf.pdf";

		String ownerPassword = "owner";
		String userPassword = "user";
		CreateEncryptedPdf.create(filePath, ownerPassword, userPassword);
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
	public void setTxtFieldsNoEdit (MouseEvent mouseEvent) throws IOException {

		if(radBtnNo.isSelected()){
			inMedicationName.setEditable(false);
			inScriptValidDays.setEditable(false);
		}
	}

	@FXML
	public void setTxtFieldsEdit (MouseEvent mouseEvent) throws IOException {

		if(radBtnYes.isSelected()){
			inMedicationName.setEditable(true);
			inScriptValidDays.setEditable(true);
		}
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
	public void highlightIVDashboardPaneOnEnter(MouseEvent mouseEvent) throws IOException {
		dashboardIVPane.setStyle("-fx-background-color: #02181f");
	}
	
	@FXML	
	public void highlightIVDashboardPaneOnExit(MouseEvent mouseEvent) throws IOException {
		dashboardIVPane.setStyle("-fx-background-color:  #063847");
	}
}
