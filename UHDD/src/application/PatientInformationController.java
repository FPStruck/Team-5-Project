package application;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
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
	@FXML private TextArea textEmail = new TextArea();
	@FXML private ImageView QRCode = new ImageView();
	@FXML private ComboBox<String> dd = new ComboBox();
	
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
		Parent root = FXMLLoader.load(getClass().getResource("DashBoard.fxml"));
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
			labelStatus.setText("Record not found");
			labelStatus.setTextFill(Color.RED);
		}
	}
	
	@FXML 
	private void insert(ActionEvent event) throws SQLException, ClassNotFoundException {
		dbConnection.initialiseDB();
		String insertQuery = "INSERT INTO `testdb`.`test3` "
				+ "(ID, FirstName, MiddleName, LastName, Address, City, State, Telephone, Email) "
				+ "VALUES ('" + textId.getText().trim() + "', '" + textFirstName.getText().trim() + 
				"', '" + textMiddleName.getText().trim()+ "', '" + textLastName.getText().trim() + 
				"', '" + textAddress.getText().trim() + "', '" + textCity.getText().trim() +
				"', '" + textState.getText().trim() + "', '" + textTelephone.getText().trim() + 
				"', '" + textEmail.getText().trim() + "');";
		
		//System.out.println(insertQuery);
		
		try {
			// execute statement
			dbConnection.executeUpdate(insertQuery);
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
		textId.setText(null);
		textLastName.setText(null);
		textFirstName.setText(null);
		textMiddleName.setText(null);
		textAddress.setText(null);
		textCity.setText(null);
		textState.setText(null);
		textTelephone.setText(null);
		textEmail.setText(null); 
	}

	@FXML 
	private void update(ActionEvent event) throws ClassNotFoundException, SQLException {
		dbConnection.initialiseDB();
		// TODO Auto-generated method stub
		String updateQuery = "UPDATE `testdb`.`test3` SET "
				+ "FirstName = '" + textFirstName.getText().trim() + 
				"', MiddleName = '" + textMiddleName.getText().trim()+ 
				"', LastName = '" + textLastName.getText().trim() + 
				"', Address = '" + textAddress.getText().trim() + 
				"', City = '" + textCity.getText().trim() +
				"', State = '" + textState.getText().trim() + 
				"', Telephone = '" + textTelephone.getText().trim() + 
				"', Email = '" + textEmail.getText().trim() + 
				"' WHERE ID = '" + textId.getText().trim() + "';";
		
		System.out.println(updateQuery);
		
		try {
			// execute statement
			dbConnection.executeUpdate(updateQuery);
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
