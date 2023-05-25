package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


public class MySQL_test extends Application{

	private Label labelStatus = new Label();
	
	private Label labelId = new Label("ID");
	private TextField textId = new TextField();
	private Label labelLastName = new Label("Last Name");
	private TextField textLastName = new TextField();
	private Label labelFirstName = new Label("First Name");
	private TextField textFirstName = new TextField();
	private Label labelMiddleName = new Label("Middle Name");
	private TextField textMiddleName = new TextField();
	private Label labelAddress = new Label("Address");
	private TextField textAddress = new TextField();
	private Label labelCity = new Label("City");
	private TextField textCity = new TextField();
	private Label labelState = new Label("State");
	private TextField textState = new TextField();
	private Label labelTelephone = new Label("Telephone");
	private TextField textTelephone = new TextField();
	private Label labelEmail = new Label("Email");
	private TextField textEmail = new TextField();
	
	private Button buttonView = new Button("View");
	private Button buttonInsert = new Button("Insert");
	private Button buttonUpdate = new Button("Update");
	private Button buttonClear = new Button("Clear");
	
	// these objects will be used in querying the database and processing the results
	private Connection connection;
	private Statement statement;
	private ResultSet results;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		
		HBox hbox1 = new HBox(5);
		hbox1.getChildren().addAll(labelId, textId);
		
		HBox hbox2 = new HBox(5);
		hbox2.getChildren().addAll(labelLastName, textLastName, labelFirstName, textFirstName, 
				labelMiddleName, textMiddleName);
		
		HBox hbox3 = new HBox(5);
		hbox3.getChildren().addAll(labelAddress, textAddress);
		
		HBox hbox4 = new HBox(5);
		hbox4.getChildren().addAll(labelCity, textCity, labelState, textState);
		
		HBox hbox5 = new HBox(5);
		hbox5.getChildren().addAll(labelTelephone, textTelephone, labelEmail, textEmail);
		
		VBox vbox = new VBox(5);
		vbox.getChildren().addAll(hbox1, hbox2, hbox3, hbox4, hbox5);
		
		HBox bottomBox = new HBox(5);
		bottomBox.getChildren().addAll(buttonView, buttonInsert, buttonUpdate, buttonClear);
		bottomBox.setAlignment(Pos.CENTER);
		
		labelStatus.setTextFill(Color.RED);
		
		BorderPane borderpane = new BorderPane();
		borderpane.setTop(labelStatus);
		borderpane.setCenter(vbox);
		borderpane.setBottom(bottomBox);
		
		Scene scene = new Scene(borderpane);
		primaryStage.setTitle("My SQL");
		primaryStage.setScene(scene);
		primaryStage.show();
		
		initialDB();
		
		buttonView.setOnAction(e -> view());
		buttonInsert.setOnAction(e -> insert());
		buttonClear.setOnAction(e -> clear());
		buttonUpdate.setOnAction(e -> update());
				
	}
	
	private void update() {
		// TODO Auto-generated method stub
		String updateQuery = "UPDATE `javabook`.`test` SET "
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
			statement.executeUpdate(updateQuery);
			labelStatus.setText("Update completed:");
			System.out.println("Update suceeded");
			
		} catch (SQLException Ex){
			labelStatus.setText("Update failed:");
			System.out.println(Ex.getMessage());
		}
	}
	
	private void clear() {
		// TODO Auto-generated method stub
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
	
	private void insert() {
		// TODO Auto-generated method stub
		String insertQuery = "INSERT INTO `javabook`.`test` "
				+ "(ID, FirstName, MiddleName, LastName, Address, City, State, Telephone, Email) "
				+ "VALUES ('" + textId.getText().trim() + "', '" + textFirstName.getText().trim() + 
				"', '" + textMiddleName.getText().trim()+ "', '" + textLastName.getText().trim() + 
				"', '" + textAddress.getText().trim() + "', '" + textCity.getText().trim() +
				"', '" + textState.getText().trim() + "', '" + textTelephone.getText().trim() + 
				"', '" + textEmail.getText().trim() + "');";
		
		//System.out.println(insertQuery);
		
		try {
			// execute statement
			statement.executeUpdate(insertQuery);
			labelStatus.setText("Insert completed:");
			System.out.println("Insert suceeded");
			
		} catch (SQLException Ex){
			labelStatus.setText("Insert failed:");
			System.out.println(Ex.getMessage());
		}
		
	}
	
	private void view() {
		// TODO Auto-generated method stub
		String query = "SELECT * FROM `javabook`.`test` WHERE ID = '" + textId.getText().trim() + "'";
		
		try {
			// execute statement
			results = statement.executeQuery(query);
			loadFields(results);
			
		} catch (SQLException Ex){
			labelStatus.setText("Record failed:");
			System.out.println(Ex.getMessage());
		}
	}
	
	private void loadFields(ResultSet results) throws SQLException {
		// TODO Auto-generated method stub
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
		}
	}

	private void initialDB() throws ClassNotFoundException, SQLException {
		// TODO Auto-generated method stub
		// loads and checks the driver
		Class.forName("com.mysql.cj.jdbc.Driver");
		System.out.println("Driver loaded:");
		
		// connection for database...make sure the URL is correct JDBC:MYSQL
		String url = "jdbc:mysql://127.0.0.1:3306/javabook";
		String username = "root";
		String password = "mysql";
		
		// connect to the database
		try {
			connection = DriverManager.getConnection(url, username, password);
			System.out.println("Database connected:");
			labelStatus.setText("Database connected");
			statement = connection.createStatement();
		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
			labelStatus.setText("Connection failed");
		} 
	}

	public static void main(String[] args) {
		Application.launch();
	}
		
}

