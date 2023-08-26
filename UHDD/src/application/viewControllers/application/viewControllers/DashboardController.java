package application.viewControllers;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;

import com.calendarfx.model.Entry;

import application.CalendarApp;
import application.DBConnector;
import application.Main;
import application.Medication;
import application.Patient;
import application.PatientService;
import application.UserSession;
import application.UsernameStorage;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class DashboardController {
	private Stage stage;
	private static Stage calendarStage;
	private Scene scene;
	private static Parent calendarRoot;
	private static CalendarApp myCalendar;
	private String currentUser;
	private Timer timer;
	String currentFXML;
	
	@FXML private TableView<Patient> patientDirectoryDBTV;
	@FXML private TableColumn<Patient, Integer> patientDirectoryDBTVId;
	@FXML private TableColumn<Patient, String> patientDirectoryDBTVFamilyName;
	@FXML private TableColumn<Patient, String> patientDirectoryDBTVGivenName;

	@FXML private TableView<Medication> prescribedMedsDBTV;
	@FXML private TableColumn<Medication, Integer> prescribedMedsDBTVScriptId;
	@FXML private TableColumn<Medication, String> prescribedMedsDBTVMedicationName;
	@FXML private TableColumn<Medication, LocalDate> prescribedMedsDBTVPrescribedDate;
	@FXML private TableColumn<Medication, LocalDate> prescribedMedsDBTVExpiredDate;

	@FXML private Pane patientDirectoryDBPane;
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
	private Text userText;
	@FXML
	private Text phoneNumber;
	@FXML
	private Text pastMedicalConditions;
	@FXML
	private Text progressNotes;
	
	DBConnector dbConnector = new DBConnector();
	String nextA;
	String nextTitle;
	String nextId;
	Boolean nextFullDay;
	LocalDate nextStartDate;
	LocalDate nextEndDate;
	LocalTime nextStartTime;
	LocalTime nextEndTime;
	ZoneId nextZoneId;
	Boolean nextRecurring;
	String nextRRule;
	Boolean nextRecurrence;
	UserSession us = new UserSession();
	private Timer loggedInStatusTimer;
	private Timer inactivityStatusTimer;
	@FXML
	public void initialize() throws Exception{
		setUserText();
		currentUser = UsernameStorage.getUsername();
		Platform.runLater(() -> {
	        try {
	            updateNextAppointment();
	            updatePatientDirectoryDBTableView();
	            updatePrescribedMedsDBTableView();
	        } catch (ClassNotFoundException | SQLException | NullPointerException e) {
	            e.printStackTrace();
	        } catch (Exception e) {
				e.printStackTrace();
			}
	    });
	}

	public void updatePatientDirectoryDBTableView() throws Exception{
		ObservableList<Patient> patientOL = FXCollections.observableArrayList();
		
		patientDirectoryDBTVId.setCellValueFactory(new PropertyValueFactory<>("id"));
		patientDirectoryDBTVFamilyName.setCellValueFactory(new PropertyValueFactory<>("familyName"));
		patientDirectoryDBTVGivenName.setCellValueFactory(new PropertyValueFactory<>("givenName"));
			
		dbConnector.initialiseDB();
		ResultSet rs = dbConnector.QueryReturnResultsFromPatients();
		while (rs.next()) {
			int id = rs.getInt("patientId");
			String familyName = rs.getString("lastName");
			String givenName = rs.getString("firstName");
			Patient patient = new Patient(id, familyName, givenName);
			patientOL.add(patient);
			//Commenting out to reduce noise whilst bug fixing
			//System.out.println(patient.getId() + " " + patient.getFamilyName() + " " + patient.getGivenName());
		}
		patientDirectoryDBTV.setItems(patientOL);

	}
	
	public void updatePrescribedMedsDBTableView() throws Exception{
		ObservableList<Medication> medicationOL = FXCollections.observableArrayList();
		
		prescribedMedsDBTVScriptId.setCellValueFactory(new PropertyValueFactory<>("scriptId"));
		prescribedMedsDBTVMedicationName.setCellValueFactory(new PropertyValueFactory<>("medicationName"));
		prescribedMedsDBTVPrescribedDate.setCellValueFactory(new PropertyValueFactory<>("prescribedDate"));
		prescribedMedsDBTVExpiredDate.setCellValueFactory(new PropertyValueFactory<>("expiredDate"));		
		
		dbConnector.initialiseDB();
		ResultSet rs = dbConnector.QueryReturnResultsFromMedication();
		while (rs.next()) {
			int scriptId = rs.getInt("scriptId");
			int patientId = rs.getInt("patientId");
			String medicationName = rs.getString("medication_name");
			LocalDate prescribedDate = rs.getDate("prescribed_date").toLocalDate();
			LocalDate expiredDate = rs.getDate("expired_date").toLocalDate();
			Medication medication = new Medication(scriptId,patientId, medicationName, prescribedDate, expiredDate);
			medicationOL.add(medication);
		}
		prescribedMedsDBTV.setItems(medicationOL);
	}

	

	public void updateNextAppointment() throws Exception{
		LocalTime LastEndTime = LocalTime.MAX ; // need a time to compare that is the Max
		dbConnector.initialiseDB();
		
		// grabs the events from the database and inserts to the calendar
		dbConnector.getCalendarEvents(); // this will ensure that the next appointment is displayed 
		
		// get the next appointment
		//commenting out to reduce noise whilst bug fixing 
		//System.out.println("Find entries" + CalendarApp.getDoctors().findEntries(LocalDate.now(), LocalDate.MAX, ZoneId.systemDefault()));
		Map<LocalDate, List<Entry<?>>> entry = CalendarApp.getDoctors().findEntries(LocalDate.now(), LocalDate.MAX, ZoneId.systemDefault());
		//System.out.println("This is the entry: " + entry);	
		//System.out.println("This is the calendar: " + CalendarApp.getDoctors());	
			for (java.util.Map.Entry<LocalDate, List<Entry<?>>> l : entry.entrySet()) {
				//System.out.println("This is the list: " + l);
				List<Entry<?>> e =  l.getValue();
				//System.out.println("This is entry: " + e);
				nextA = e.get(0).getTitle();
				//System.out.println("First entry: " + nextA);
				// this will set the next appointment text 
				if (nextAppointment != null) { // this fixed the bug 
					nextAppointment.setText(nextA);
				}		// this is where it previously breaks because nestAppointment was null
				
				// loop through to add to database
				for (Entry<?> ee: e) {
					nextTitle = ee.getTitle();
					nextId = ee.getId();
					nextFullDay = ee.isFullDay();
					nextStartDate = ee.getStartDate();
					nextEndDate = ee.getEndDate();
					nextStartTime = ee.getStartTime();
					nextEndTime = ee.getEndTime();
					nextZoneId = ee.getZoneId();
					nextRecurring = ee.isRecurring();
					nextRRule = ee.recurrenceRuleProperty().getValue();
					nextRecurrence = ee.isRecurrence();
					/*
					 * Reduce noise whilst bug fixing
					
					System.out.println("Entry from loop: " + nextTitle + ", " + nextId + ", " 
					+ nextFullDay + ", " + nextStartDate + ", " + nextEndDate + ", "
					+ nextStartTime + ", " + nextEndTime + "' " + nextZoneId + ", "
					+ nextRecurring + ", " + nextRRule + ", " + nextRecurrence);	
					*/

					// adds the event to the database
					try {dbConnector.addCalendarEvent(nextTitle, nextId, nextFullDay, nextStartDate, 
							nextEndDate, nextStartTime,	nextEndTime, nextZoneId,
							nextRecurring, nextRRule, nextRecurrence); 
					} catch (SQLIntegrityConstraintViolationException error) {
						System.out.println(error);
						
					}
					
					int value = nextEndTime.compareTo(LocalTime.now()); // ensure that the time is more than the local time for the next appointment 
					int value2 = nextEndTime.compareTo(LastEndTime); // ensure that the current appointment is not later than the next appointment 
					
					if (value > 0 && value2 < 0) { // ensure that the event is more than the local time and less than the next appointment(remember the next appointment must be more than the local time
						if (nextAppointment != null) {
							nextAppointment.setText(nextTitle);
						}
						LastEndTime = nextEndTime;
						// this will display the next patient appointment details
						if (nextA != null && nextAppointment != null) { // there was a bug where it broke if the next appointment was null, fixed by adding nextAppointment
							ResultSet patientDetails = dbConnector.QueryReturnResultsFromPatientDataId(nextId);
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
					}		// this is where it previously breaks because nestAppointment was null
					
					
				}
			}
		//Reduce noise whilst bug fixing
		//System.out.println("After loop: " + nextA);	
		//System.out.println("After loop next apppointment: " + nextAppointment);			
		startInactivityStatusTimer();
		startLoggedInStatusTimer();  // (THIS LINE OF CODE MUST BE PRESENT WHEN THE PROGRAM IS BEING COMPLETED. WITHOUT THIS LINE, THE MULTI-LOGIN SYSTEM WILL NOT OPERATE)
	}
	
	private void setUserText() {
	    String username = UsernameStorage.getUsername();
	    userText.setText(username); // Set the userText to the username obtained from UsernameStorage
	}
	
	
	// Call this method to start the timer
		private void startInactivityStatusTimer() {
			inactivityStatusTimer = new Timer();
			inactivityStatusTimer.scheduleAtFixedRate(new TimerTask() {
	            @Override
	            public void run() {
	                checkInactivityStatus();
	            }
	        }, 0, 5000); // Run the task every 5 seconds
	    }
	
	// Call this method to start the timer
	private void startLoggedInStatusTimer() {
        loggedInStatusTimer = new Timer();
        loggedInStatusTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    checkLoggedInStatus();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, 0, 5000); // Run the task every 5 seconds
    }

    // Modify this method to check the loggedInStatus
    private void checkLoggedInStatus() throws IOException {
        Platform.runLater(() -> {
            try {
                dbConnector.initialiseDB();
                int loggedInStatus = dbConnector.getLoggedInStatus(currentUser);
                if (loggedInStatus == 2) {
                    loggedInStatusTimer.cancel(); // Stop the timer
                    displayLoginAlert();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
    
    private void checkInactivityStatus() {
        try {
            dbConnector.initialiseDB();
            Timestamp lastLoggedInDate = dbConnector.getLastLoggedInDate(currentUser);

            if (lastLoggedInDate != null) {
                LocalDateTime currentDateTime = LocalDateTime.now();
                Timestamp currentTimeStamp = Timestamp.valueOf(currentDateTime);

                Duration timeDifference = Duration.between(lastLoggedInDate.toLocalDateTime(), currentDateTime);
                long hoursDifference = timeDifference.toHours();

                if (hoursDifference > 5) {
                    // User has been inactive for 5 hours or more, show the inactivity alert
                    displayInactivityAlert();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean dialogDisplayed = false; // Add this field

	 // Modify the displayLoginAlert method
	 private void displayLoginAlert() {
	     if (dialogDisplayed) {
	         return; // Don't display the dialog if it's already shown
	     }
	     
	     dialogDisplayed = true; // Set the flag to true to indicate that the dialog is displayed
	
	     Alert alert = new Alert(AlertType.CONFIRMATION);
	     alert.setTitle("Login Attempt Detected");
	     alert.setHeaderText("Another User Has Attempted to Access This Account:");
	     alert.setContentText("Do you want to continue the session? (You will be automatically logged out within 15 seconds if an option is not chosen)");
	
	     ButtonType continueButton = new ButtonType("Continue");
	     ButtonType quitButton = new ButtonType("Quit");
	
	     alert.getButtonTypes().setAll(continueButton, quitButton);
	
	     Optional<ButtonType> result = alert.showAndWait();
	
	     if (result.isPresent() && result.get() == continueButton) {
	         // User chose to continue, set logged_in status to 1
	         try {
	             dbConnector.setLoggedInStatus(UsernameStorage.getUsername(), 1);
	             startInactivityStatusTimer(); // Restart the timer
	         } catch (SQLException e) {
	             e.printStackTrace();
	         }
	     } else if (result.isPresent() && result.get() == quitButton) {
	         // User chose to logout, set logged_in status to 0 and go back to the login screen
	         try {
	             dbConnector.setLoggedInStatus(currentUser, 0);
	             Platform.exit();
	             dbConnector.closeConnection();
	         } catch (SQLException e) {
	             e.printStackTrace();
	         }
	     }
	 }
	
	
	 private void displayInactivityAlert() {
		    if (dialogDisplayed) {
		        return; // Don't display the dialog if it's already shown
		    }

		    dialogDisplayed = true; // Set the flag to true to indicate that the dialog is displayed

		    Platform.runLater(() -> {
		        Alert alert = new Alert(AlertType.CONFIRMATION);
		        alert.setTitle("Inactivity Detected");
		        alert.setHeaderText("You Have Been Logged In For An Extended Period:");
		        alert.setContentText("Do you want to continue the session? (You will be automatically logged out within 15 seconds if an option is not chosen)");

		        ButtonType continueButton = new ButtonType("Continue");
		        ButtonType quitButton = new ButtonType("Quit");

		        alert.getButtonTypes().setAll(continueButton, quitButton);

		        Optional<ButtonType> result = alert.showAndWait();

		        if (result.isPresent() && result.get() == continueButton) {
		            // User chose to continue, set logged_in status to 1
		            try {
		                dbConnector.setLoggedInStatus(UsernameStorage.getUsername(), 1);

		                // Set the lastLoggedInTime to the current date and time
		                LocalDateTime currentDateTime = LocalDateTime.now();
		                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		                String formattedDateTime = currentDateTime.format(formatter);
		                Timestamp currentTimestamp = Timestamp.valueOf(formattedDateTime);

		                dbConnector.setLastLoggedInTime(UsernameStorage.getUsername(), currentTimestamp);

		                startLoggedInStatusTimer(); // Restart the timer
		            } catch (SQLException e) {
		                e.printStackTrace();
		            }
		        } else if (result.isPresent() && result.get() == quitButton) {
		            // User chose to logout, set logged_in status to 0 and go back to the login screen
		            try {
		                dbConnector.setLoggedInStatus(currentUser, 0);
		                Platform.exit();
		                dbConnector.closeConnection();
		            } catch (SQLException e) {
		                e.printStackTrace();
		            }
		        }
		    });
		}
	 
    @FXML
    private void switchToLoginScreen() throws IOException {
    	Stage currentStage = (Stage) userText.getScene().getWindow();
        currentStage.close();

        // Start a new instance of the Main class to run the program again from the beginning
        Main mainApp = new Main();
        mainApp.start(new Stage());
    }
	
	@FXML 	
	public void switchToPatientInformation(MouseEvent mouseEvent) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("/application/fxmlScenes/PatientInformation.fxml"));		
		stage = (Stage)((Node)mouseEvent.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	@FXML 
	public void switchToPatientDirectory(MouseEvent mouseEvent) throws Exception {
		currentFXML = "/application/fxmlScenes/PatientDirectory.fxml";
		CurrentFXMLInstance.getInstance().setCurrentFXML(currentFXML);
	    FXMLLoader loader = new FXMLLoader(getClass().getResource(currentFXML));
	    Parent root = loader.load();
	    Map<String, Object> namespace = loader.getNamespace();
		stage = (Stage)((Node)mouseEvent.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	@FXML	
	public void switchToTableCreator(MouseEvent mouseEvent) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("/application/fxmlScenes/TableCreator.fxml"));
		stage = (Stage)((Node)mouseEvent.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	@FXML	
	public void switchToPatientInfoView(MouseEvent mouseEvent) throws IOException {
		currentFXML = "/application/fxmlScenes/PatientInfoViewOverview.fxml";
		CurrentFXMLInstance.getInstance().setCurrentFXML(currentFXML);
	    FXMLLoader loader = new FXMLLoader(getClass().getResource(currentFXML));
	    Parent root = loader.load();
	    Map<String, Object> namespace = loader.getNamespace();
	    stage = (Stage)((Node)mouseEvent.getSource()).getScene().getWindow();
	    scene = new Scene(root);
	    stage.setScene(scene);
	    stage.show();
	}

	@FXML	
	public void switchToPatientInfoViewPatientNotes(MouseEvent mouseEvent) throws IOException {
		currentFXML = "/application/fxmlScenes/PatientInfoViewPatientNotes.fxml";
		CurrentFXMLInstance.getInstance().setCurrentFXML(currentFXML);
	    FXMLLoader loader = new FXMLLoader(getClass().getResource(currentFXML));
	    Parent root = loader.load();
	    Map<String, Object> namespace = loader.getNamespace();
	    stage = (Stage)((Node)mouseEvent.getSource()).getScene().getWindow();
	    scene = new Scene(root);
	    stage.setScene(scene);
	    stage.show();
	}

	@FXML
	public void addPatientNote(MouseEvent mouseEvent) throws IOException{
			currentFXML = "/application/fxmlScenes/PopUpAddPatientNote.fxml";
			CurrentFXMLInstance.getInstance().setCurrentFXML(currentFXML);
			Stage popupStage = new Stage();
            Parent popupRoot = FXMLLoader.load(getClass().getResource(currentFXML));
            Scene popupScene = new Scene(popupRoot);
            popupStage.setScene(popupScene);
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.showAndWait();
	}
	
	public void switchToCalendar(MouseEvent mouseEvent) throws Exception {
		if (myCalendar == null) {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/application/fxmlScenes/Calendar.fxml"));
	        calendarRoot = (Parent) fxmlLoader.load();
			calendarStage = new Stage();
			calendarStage.setScene(new Scene(calendarRoot));
			calendarStage.show();
		
			myCalendar = new CalendarApp();
			myCalendar.start(calendarStage);	
			
		} else calendarStage.show();
		
//		Parent root = FXMLLoader.load(getClass().getResource("/application/fxmlScenes/Calendar_new.fxml"));
//		stage = (Stage)((Node)mouseEvent.getSource()).getScene().getWindow();
//		scene = new Scene(root);
//		stage.setScene(scene);
//		stage.show();
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
