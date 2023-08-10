package application.viewControllers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

	public void NonMouseEventSwitchToPatientDirectory() throws IOException{
		currentFXML = "../fxmlScenes/PatientDirectory.fxml";
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
		} else if (currentFXML.equals("../fxmlScenes/CreateNewPatient.fxml")){
			choiceBoxState.getItems().addAll("NSW", "QLD", "VIC", "ACT", "SA", "WA", "NT", "TAS");
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
	public void AddPatient(MouseEvent mouseEvent) throws SQLException, ClassNotFoundException, IOException{
		boolean isValid = true;
		txtFirstNameError.setVisible(false);
		txtMiddleNameError.setVisible(false);
		txtLastNameError.setVisible(false);
		txtGenderError.setVisible(false);
		txtAddressError.setVisible(false);
		txtCityError.setVisible(false);
		txtStateError.setVisible(false);
		txtTelephoneError.setVisible(false);
		txtEmailError.setVisible(false);
		txtDateOfBirthError.setVisible(false);
		txtHINError.setVisible(false);
		txtEmergencyNumberError.setVisible(false);


		// Validate First Name
		String firstName = InputFirstName.getText();
		if (firstName == null || firstName.trim().isEmpty() || !firstName.matches("[a-zA-Z]{1,50}")) {
			txtFirstNameError.setText("Must be alphabetic and less than 50 characters.");
			txtFirstNameError.setVisible(true);
			isValid = false;
		} else {
			txtFirstNameError.setText("");
		}

		// Validate Middle Name
		String middleName = InputMiddleName.getText();
		if (middleName == null || middleName.trim().isEmpty() || !middleName.matches("[a-zA-Z]{1,50}")) {
			txtMiddleNameError.setText("Must be alphabetic and less than 50 characters.");
			txtMiddleNameError.setVisible(true);
			isValid = false;
		} else {
			txtMiddleNameError.setText("");
		}

		// Validate Last Name
		String lastName = InputLastName.getText();
		if (lastName == null || lastName.trim().isEmpty() || !lastName.matches("[a-zA-Z]{1,50}")) {
			txtLastNameError.setText("Must be alphabetic and less than 50 characters.");
			txtLastNameError.setVisible(true);
			isValid = false;
		} else {
			txtLastNameError.setText("");
		}

		// Validate Address
		String address = InputAddress.getText();
		if (address == null || address.trim().isEmpty() || address.length() > 100) {
			txtAddressError.setText("Address must be less than 100 characters.");
			txtAddressError.setVisible(true);
			isValid = false;
		} else {
			txtAddressError.setText("");
		}

		// Validate City
		String city = InputCity.getText();
		if (city == null || city.trim().isEmpty() || !city.matches("[a-zA-Z ]{1,50}")) {
			txtCityError.setText("City must be alphabetic (with spaces allowed) and less than 50 characters.");
			txtCityError.setVisible(true);
			isValid = false;
		} else {
			txtCityError.setText("");
		}


		// Validate State
		String state = choiceBoxState.getValue();
		if (state == null || state.trim().isEmpty() || !state.matches("[a-zA-Z]{1,50}")) {
			txtStateError.setText("State must be alphabetic and less than 50 characters.");
			txtStateError.setVisible(true);
			isValid = false;
		} else {
			txtStateError.setText("");
		}

		// Validate Telephone
		String telephone = InputTelephone.getText();
		if (telephone == null || telephone.trim().isEmpty() || !telephone.matches("\\d{1,20}")) {
			txtTelephoneError.setText("Telephone must be numerical and less than 20 characters.");
			txtTelephoneError.setVisible(true);
			isValid = false;
		} else {
			txtTelephoneError.setText("");
		}

		// Validate Emergency Number
		String emergencyNumber = InputEmergencyNumber.getText();
		if (emergencyNumber == null || emergencyNumber.trim().isEmpty() || !emergencyNumber.matches("\\d{1,20}")) {
			txtEmergencyNumberError.setText("Emergency number must be numeric and less than 20 characters.");
			txtEmergencyNumberError.setVisible(true);
			isValid = false;
		} else {
			txtEmergencyNumberError.setText("");
		}

		// Validate Date of Birth
		LocalDate dob = datePickDateOfBirth.getValue();
		LocalDate today = LocalDate.now();
		if (dob == null || dob.isAfter(today)) {
			txtDateOfBirthError.setText("Date of birth cannot be today, or in the future.");
			txtDateOfBirthError.setVisible(true);
			isValid = false;
		} else {
			txtDateOfBirthError.setText("");
		}

		// Validate Health Insurance Number
		String healthInsuranceNumber = InputHealthInsuranceNumber.getText();
		if (healthInsuranceNumber == null || healthInsuranceNumber.trim().isEmpty() || healthInsuranceNumber.length() > 50) {
			txtHINError.setText("Health insurance number must be less than 50 characters.");
			txtHINError.setVisible(true);
			isValid = false;
		} else {
			txtHINError.setText("");
		}

		// Validate Email
		String email = InputEmail.getText();
		if (email == null || email.trim().isEmpty() || !isValidEmail(email)) {
			txtEmailError.setText("Invalid or empty email format.");
			txtEmailError.setVisible(true);
			isValid = false;
		} else {
			txtEmailError.setVisible(false);
		}
	

		String gender = "?";
		if(radBtnFemale.isSelected()){
			gender = "F";
		} else if (radBtnMale.isSelected()){
			gender = "M";
		} else {
			txtGenderError.setText("Please select a gender.");
			isValid = false;
		}

		// If all fields are valid, proceed with further processing
		if (isValid) {
			dbConnector.initialiseDB();
			dbConnector.createNewPatientExecuteQuery(firstName, middleName, lastName, gender, address, city, state, telephone, email, dob.toString(), healthInsuranceNumber, emergencyNumber);
			dbConnector.closeConnection();
			
		}

	}

	private boolean isValidEmail(String email) {
		// Regular expression to validate email format
		String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();
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
