package application;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
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
	
	DBConnector dbConnector = new DBConnector();
	
	public void setTextFieldsToPatientId(String Id) throws ClassNotFoundException, SQLException {
		dbConnector.initialiseDB();
		ResultSet patientDetails = dbConnector.QueryReturnResultsFromPatientId(Id);
			
		if(patientDetails.next()) {
			fullnameTXT.setText(patientDetails.getString("firstName") + " " + patientDetails.getString("middleName") + " " + patientDetails.getString("lastName"));
			emailTXT.setText(patientDetails.getString("email"));
			dobTXT.setText(patientDetails.getString("dateOfBirth"));
			locationTXT.setText(patientDetails.getString("city"));
			patientIdTXT.setText(patientDetails.getString("patientId"));
			phoneNoTXT.setText(patientDetails.getString("telephone"));
			addressTXT.setText(patientDetails.getString("address"));
			if(patientDetails.getString("gender").equals("M")){
				genderTXT.setText("Male");
			} else if(patientDetails.getString("gender").equals("F")) {
				genderTXT.setText("Female");
			} else {
				genderTXT.setText("Other");
			}
			
		}
		
		
		dbConnector.closeConnection();
	}
	
	@FXML
	public void initialize() throws ClassNotFoundException, SQLException {
		setTextFieldsToPatientId("2");
	}
	
	@FXML 	
	public void switchToPatientInformation(MouseEvent mouseEvent) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("PatientInformation.fxml"));		
		stage = (Stage)((Node)mouseEvent.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	@FXML 	
	public void switchToDasboard(MouseEvent mouseEvent) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("Dashboard.fxml"));		
		stage = (Stage)((Node)mouseEvent.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
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
