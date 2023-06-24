package application;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.calendarfx.model.Entry;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class DashboardController {
	private Stage stage;
	private static Stage calendarStage;
	private Scene scene;
	private Parent root;
	private static Parent calendarRoot;
	private static CalendarApp myCalendar;
	
	@FXML
	private Pane patientDirectoryDBPane;
	@FXML
	private Pane appointmentsDBPane;
	@FXML
	private Pane patientNotesDBPane;
	@FXML
	private Button viewPatientInfoBtn;
	@FXML
	private Button patientInformationBTN;
	@FXML
	private Text nextAppointment;
	@FXML
	private Text fullName;
	@FXML
	private Text phoneNumber;
	@FXML
	private Text pastMedicalConditions;
	@FXML
	private Text progressNotes;
	
	DBConnector dbConnector = new DBConnector();
	String nextA;
	
	
	@FXML
	public void initialize() throws ClassNotFoundException, SQLException{
		// trying to get the next appointment 
		
		// this breaks if you try to open the calendar again 
		System.out.println("Find entries" + CalendarApp.getDoctors().findEntries(LocalDate.now(), LocalDate.MAX, ZoneId.systemDefault()));
		Map<LocalDate, List<Entry<?>>> entry = CalendarApp.getDoctors().findEntries(LocalDate.now(), LocalDate.MAX, ZoneId.systemDefault());
		System.out.println("This is the entry: " + entry);	
			for (java.util.Map.Entry<LocalDate, List<Entry<?>>> l : entry.entrySet()) {
				System.out.println("This is the list: " + l);
				List<Entry<?>> e =  l.getValue();
				System.out.println("This is entry: " + e);
				for (Entry<?> ee: e) {
					nextA = ee.getTitle();
					System.out.println("This is the titile: " + nextA);
					// this will set the next appointment text 
					if (nextAppointment != null) { // this fixed the bug 
						nextAppointment.setText(nextA);
					}		// this is where it breaks because nestAppointment was null
				}
			}
		System.out.println("After loop: " + nextA);	
		System.out.println("After loop next apppointment: " + nextAppointment);	
		
		// this will display the next patient appointment details
		if (nextA != null) {
			dbConnector.initialiseDB();
			ResultSet patientDetails = dbConnector.QueryReturnResultsFromPatientName(nextA);
			System.out.println("This is the patient details: " + patientDetails);
			if(patientDetails.next()) {
				String name =  patientDetails.getString("FirstName") + " " 
						+ patientDetails.getString("MiddleName") + " " 
						+ patientDetails.getString("LastName");
				fullName.setText(name);
				phoneNumber.setText(patientDetails.getString("Telephone"));
				pastMedicalConditions.setText(patientDetails.getString("PastMedicalConditions"));
				progressNotes.setText(patientDetails.getString("ProgressNotes"));
			}
		}
		
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
	public void switchToPatientDirectory(MouseEvent mouseEvent) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("PatientDirectory.fxml"));
		stage = (Stage)((Node)mouseEvent.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	@FXML	
	public void switchToTableCreator(MouseEvent mouseEvent) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("TableCreator.fxml"));
		stage = (Stage)((Node)mouseEvent.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	@FXML	
	public void switchToPatientInfoView(MouseEvent mouseEvent) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("PatientInfoView.fxml"));
		stage = (Stage)((Node)mouseEvent.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	public void switchToCalendar(MouseEvent mouseEvent) throws Exception {
		if (myCalendar == null) {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Calendar.fxml"));
	        calendarRoot = (Parent) fxmlLoader.load();
			calendarStage = new Stage();
			calendarStage.setScene(new Scene(calendarRoot));
			calendarStage.show();
		
			myCalendar = new CalendarApp();
			myCalendar.start(calendarStage);
			} else calendarStage.show();
	}
	
	@FXML	
	public void highlightPatientDirectoryPane(MouseEvent mouseEvent) throws IOException {
		patientDirectoryDBPane.setStyle("-fx-background-color: #02181f");
	}
	
	@FXML	
	public void highlightPatientDirectoryPaneOnExit(MouseEvent mouseEvent) throws IOException {
		patientDirectoryDBPane.setStyle("-fx-background-color:  #063847");
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
}
