package application.viewControllers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import application.DBConnector;
import application.Patient;
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
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;

public class PatientDirectoryController {
	private Stage stage;
	private Scene scene;
	@FXML private Button back;
	@FXML private Button viewTable;
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
	DBConnector dbConnector = new DBConnector();
	
	public void switchToDashBoard(MouseEvent mouseEvent) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("../fxmlScenes/DashBoard.fxml"));
		stage = (Stage)((Node)mouseEvent.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	@FXML 
	public void initialize() throws ClassNotFoundException, SQLException{
		System.out.println("Initialise PD");
		ObservableList<Patient> patientOL = FXCollections.observableArrayList();

        pdTableViewId.setCellValueFactory(new PropertyValueFactory<>("id"));
        pdTableViewFamilyName.setCellValueFactory(new PropertyValueFactory<>("familyName"));
        pdTableViewGivenName.setCellValueFactory(new PropertyValueFactory<>("givenName"));
		pdTableViewGender.setCellValueFactory(new PropertyValueFactory<>("gender"));

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
                if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY) {
                    Patient clickedPatient = row.getItem();
                    System.out.println("Clicked on: " + clickedPatient.getFamilyName() + " " + clickedPatient.getGivenName());
                    // Handle the click event here, e.g., open a new window or display patient details
                }
            });
            return row;
        });

        dbConnector.closeConnection();
	}
	
	@FXML public void viewTable(MouseEvent mouseEvent) throws ClassNotFoundException, SQLException, FileNotFoundException {
		System.out.println("view table");
		// TODO Auto-generated method stub
		dbConnector.initialiseDB();
		data = FXCollections.observableArrayList();
        try {
            //MySql query table
            String SQL = "SELECT * from `testdb`.`test3`";
            //ResultSet
            ResultSet rs = dbConnector.executeQueryReturnResults(SQL);
            System.out.println("After result set obtained");
            // add table column dynamically
            for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                //We are using non property style for making dynamic table
                final int j = i;
                col = new TableColumn(rs.getMetaData().getColumnName(i + 1)); // loops through the results and grabs the column name
                System.out.println("j: " + j);
                col.setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() { // makes the columns ready for population
                    public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
                        return new SimpleStringProperty(param.getValue().get(j).toString());
                        
                    }
                });
                System.out.println("After column is prepared");
                patientDirectorytableView.getColumns().addAll(col);
                System.out.println("Column [" + i + "] ");
            }

            // add to observation list
            System.out.println("Starting observation list...");
            while (rs.next()) {
                //Iterate Row
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    //Iterate Column
                    row.add(rs.getString(i));
                    System.out.println("Counting column...");
                }
                System.out.println("Row [1] added " + row);
                data.add((Patient) row);

            }

            //add to table view
            patientDirectorytableView.setItems(data);
            System.out.println("Data: " + data);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error on Building Data");
        }
        dbConnector.closeConnection();
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
