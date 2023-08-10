package application.viewControllers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import application.DBConnector;
import application.Patient;
import application.PatientService;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;

public class PatientDirectoryController {
	private Stage stage;
	private Scene scene;
	@FXML private Button back;
	@FXML private Button searchTable;
	@FXML private TableView<Patient> patientDirectorytableView = new TableView();
	static ObservableList<Patient> data;
	static TableColumn col;
	@FXML private Pane patientDirectoryDBPane;
	@FXML private Pane appointmentsDBPane;
	@FXML private Pane patientNotesDBPane;
	@FXML private Button viewPatientInfoBtn;
	@FXML private Pane dashboardDBPane;
	@FXML private TableColumn<Patient, String> pdTableViewId;
	@FXML private TableColumn<Patient, String> pdTableViewFamilyName;
	@FXML private TableColumn<Patient, String> pdTableViewGivenName;
	@FXML private TableColumn<Patient, String> pdTableViewGender;
	@FXML private TableColumn<Patient, String> pdTableViewDOB;
	@FXML private TableColumn<Patient, String> pdTableViewAddress;
	@FXML private TableColumn<Patient, String> pdTableViewCity;
	@FXML private TableColumn<Patient, String> pdTableViewEmail;
	@FXML private TextField searchTxtId;
	@FXML private Text txtPatientIdStatus;
	
	//Add patient 
	@FXML private TextField InputFirstName;
	@FXML private TextField InputLastName;
	@FXML private TextField InputMiddleName;
	@FXML private RadioButton radBtnFemale;
	@FXML private RadioButton radBtnMale;
	@FXML private TextField InputTelephone;
	@FXML private TextField InputEmail;
	@FXML private DatePicker datePickDateOfBirth;
	@FXML private TextField InputAddress;
	@FXML private TextField InputCity;
	@FXML private ChoiceBox<String> choiceBoxState;
	@FXML private TextField InputHealthInsuranceNumber;
	@FXML private TextField InputEmergencyNumber;
	@FXML private Button btnAddPatient;
	@FXML private Text txtFirstNameError;
	@FXML private Text txtMiddleNameError;
	@FXML private Text txtLastNameError;
	@FXML private Text txtGenderError;
	@FXML private Text txtTelephoneError;
	@FXML private Text txtEmailError;
	@FXML private Text txtDateOfBirthError;
	@FXML private Text txtAddressError;
	@FXML private Text txtCityError;
	@FXML private Text txtStateError;
	@FXML private Text txtHINError;
	@FXML private Text txtEmergencyNumberError;


	
	String currentFXML;
	DBConnector dbConnector = new DBConnector();
	
	public void switchToDashBoard(MouseEvent mouseEvent) throws IOException {
		currentFXML = "../fxmlScenes/Dashboard.fxml";
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
	public void switchToPatientDirectory(MouseEvent mouseEvent) throws IOException {
		currentFXML = "../fxmlScenes/PatientDirectory.fxml";
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
	}

	@FXML	
	public void switchToNewPatient(MouseEvent mouseEvent) throws IOException {
		currentFXML = "../fxmlScenes/CreateNewPatient.fxml";
		CurrentFXMLInstance.getInstance().setCurrentFXML(currentFXML);
	    FXMLLoader loader = new FXMLLoader(getClass().getResource(currentFXML));
	    Parent root = loader.load();
	    Map<String, Object> namespace = loader.getNamespace();
	    stage = (Stage)((Node)mouseEvent.getSource()).getScene().getWindow();
	    scene = new Scene(root);
	    stage.setScene(scene);
	    stage.show();
	}

	public void NonMouseEventSwitchToPatientInfoView() throws IOException {
		currentFXML = "../fxmlScenes/PatientInfoViewOverview.fxml";
		CurrentFXMLInstance.getInstance().setCurrentFXML(currentFXML);
	    FXMLLoader loader = new FXMLLoader(getClass().getResource(currentFXML));
	    Parent root = loader.load();
	    Map<String, Object> namespace = loader.getNamespace();
	    stage = new Stage();
	    scene = new Scene(root);
	    stage.setScene(scene);
	    stage.show();
	}
	
	@FXML 
	public void initialize() throws ClassNotFoundException, SQLException{
		currentFXML = CurrentFXMLInstance.getInstance().getCurrentFXML();
		System.out.println("Current FXML: " + currentFXML);
		if(currentFXML.equals("../fxmlScenes/PatientDirectory.fxml")){
			System.out.println("Initialise PD");
			ObservableList<Patient> patientOL = FXCollections.observableArrayList();

			pdTableViewId.setCellValueFactory(new PropertyValueFactory<>("id"));
			pdTableViewFamilyName.setCellValueFactory(new PropertyValueFactory<>("familyName"));
			pdTableViewGivenName.setCellValueFactory(new PropertyValueFactory<>("givenName"));
			pdTableViewGender.setCellValueFactory(new PropertyValueFactory<>("gender"));
			pdTableViewDOB.setCellValueFactory(new PropertyValueFactory<>("dateOfBirth"));
			pdTableViewAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
			pdTableViewCity.setCellValueFactory(new PropertyValueFactory<>("city"));
			pdTableViewEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

			dbConnector.initialiseDB();
			ResultSet rs = dbConnector.QueryReturnResultsFromPatients();
			while (rs.next()) {
				int id = rs.getInt("patientId");
				String familyName = rs.getString("lastName");
				String givenName = rs.getString("firstName");
				String middleName = rs.getString("middleName");
				String gender = rs.getString("gender");
				String address = rs.getString("address");
				String city = rs.getString("city");
				String state = rs.getString("state");
				String telephone = rs.getString("telephone");
				String email = rs.getString("email");
				String dob = rs.getString("dateOfBirth");
				String HIN = rs.getString("healthInsuranceNumber");
				String emergencyContactNumber = rs.getString("emergencyContactNumber");
				Patient patient = new Patient(id, familyName, givenName, middleName, gender, address, city, state, telephone, email, dob, HIN, emergencyContactNumber);
				patientOL.add(patient);
				System.out.println(patient.getId() + " " + patient.getFamilyName() + " " + patient.getGivenName());
			}
			patientDirectorytableView.setItems(patientOL);

			// Set row factory for clickable rows
			patientDirectorytableView.setRowFactory(tv -> {
				TableRow<Patient> row = new TableRow<>();
				row.setOnMouseClicked(event -> {
					if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
						Patient clickedPatient = row.getItem();
						PatientService.getInstance().setCurrentPatient(clickedPatient);
						System.out.println("Clicked on: " + clickedPatient.getFamilyName() + " " + clickedPatient.getGivenName());
						try {
							NonMouseEventSwitchToPatientInfoView();
						} catch (IOException e) {
							e.printStackTrace();
						}
					
					}
				});
				return row;
			});

			dbConnector.closeConnection();
		}
	}
	
	@FXML
	public void searchTable(MouseEvent mouseEvent) throws IOException, SQLException, ClassNotFoundException {
		Boolean patientIdValid = false;
		dbConnector.initialiseDB();
		String patientId = searchTxtId.getText();
		try {
			int parsedDiagnosisId = Integer.parseInt(patientId);
			if(dbConnector.verifyPatientIdExists(String.valueOf(parsedDiagnosisId))){
				patientIdValid = true;
			} else {
				txtPatientIdStatus.setText(patientId + " Does not exist");
			}
		} catch (NumberFormatException e) {
			txtPatientIdStatus.setText(patientId + " is not a number");
		}
        
		if(patientIdValid){
			ObservableList<Patient> patientOL = FXCollections.observableArrayList();
			ResultSet rs = dbConnector.QueryReturnResultsFromPatientDataId(patientId);
			while (rs.next()) {
				int id = rs.getInt("patientId");
				String familyName = rs.getString("lastName");
				String givenName = rs.getString("firstName");
				String middleName = rs.getString("middleName");
				String gender = rs.getString("gender");
				String address = rs.getString("address");
				String city = rs.getString("city");
				String state = rs.getString("state");
				String telephone = rs.getString("telephone");
				String email = rs.getString("email");
				String dob = rs.getString("dateOfBirth");
				String HIN = rs.getString("healthInsuranceNumber");
				String emergencyContactNumber = rs.getString("emergencyContactNumber");
				Patient patient = new Patient(id, familyName, givenName, middleName, gender, address, city, state, telephone, email, dob, HIN, emergencyContactNumber);
				patientOL.add(patient);
				System.out.println(patient.getId() + " " + patient.getFamilyName() + " " + patient.getGivenName());
			}
        patientDirectorytableView.setItems(patientOL);
		}
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
	public void highlightDashboardPaneOnEnter(MouseEvent mouseEvent) throws IOException {
		dashboardDBPane.setStyle("-fx-background-color: #02181f");
	}
	
	@FXML	
	public void highlightDashboardPaneOnExit(MouseEvent mouseEvent) throws IOException {
		dashboardDBPane.setStyle("-fx-background-color:  #063847");
	}
}
