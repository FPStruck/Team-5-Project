package application;
	
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.file.Path;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.codec.binary.Base32;
import org.apache.commons.codec.binary.Hex;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

import de.taimos.totp.TOTP;
import javafx.application.Application;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;


public class UI {
	private Stage stage;
	private Scene scene;
	private Parent root;
	@FXML private Text actionGrabber;
	@FXML private TextField userGrabber;
	@FXML private TextField passGrabber;
	@FXML private TextField tfacode  = new TextField();
	
	@FXML private TableView maintable;
	@FXML private AnchorPane anchor;
	@FXML private TextField searchRef1;
	@FXML private TextField searchRef2;
	@FXML private Text actionGrabberCreator;
	@FXML private TextField userGrabberCreator;
	@FXML private TextField passGrabberCreator;
	@FXML private Button addColumn;
	@FXML private Button addRow;
	@FXML private Button view;
	@FXML private Button update;
	@FXML private Button insert;
	@FXML private Button clear;
	@FXML private Button viewTable;
	@FXML private TableView<ObservableList> tableView = new TableView();
	
	//Toby's changes
	static byte[] encryptedPassword; // for the encryption
	static byte[] decryptedPassword; // for the decryption
	static RSA_DSA rsa;
	static byte[] tempByteArray;
	static ObservableList<ObservableList> data;
	static TableColumn col;
	
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
	@FXML private ComboBox<String> dd = new ComboBox<String>();
	
	
	private ObservableList<String> ddList = FXCollections.observableArrayList("one", "two", "three");
	
	// these objects will be used in querying the database and processing the results
	private Connection connection;
	private Statement statement;
	private ResultSet results;

	public void switchToCreateUser(ActionEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("UserCreation.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	public void switchToHomepage(ActionEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	public void switchToTableCreator(ActionEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("TableCreator.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	public void switchToMySQL(ActionEvent event) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("Test.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
		MySQL_test mySQL_test = new MySQL_test();
		mySQL_test.start(stage);
	}
	
	@FXML public void switchToMySQLTest(ActionEvent event) throws Exception {
		
//		dd.setItems(ddList);
		
		
		Parent root = FXMLLoader.load(getClass().getResource("MySQL.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
		initialDB();
		
		dd.getItems().addAll(ddList);
		System.out.println(dd.getItems());
		
//		MySQL_test2 mySQL_test2 = new MySQL_test2();
//		mySQL_test2.start(stage);
	}
	
@FXML public void switchToDoctorView(ActionEvent event) throws Exception {
		
		dd.setItems(FXCollections.observableArrayList("1", "2", "3"));
		System.out.println(dd.getItems());
		
		Parent root = FXMLLoader.load(getClass().getResource("PatientDirectory.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
		initialDB();
		
//		MySQL_test2 mySQL_test2 = new MySQL_test2();
//		mySQL_test2.start(stage);
	}
	
	public boolean loginSuccessful() throws IOException, WriterException {
		String userLog = userGrabber.getText();
		String passLog = passGrabber.getText();
		boolean credentialsMatch = checkCredentialsInFile(userLog, passLog);
		if(credentialsMatch) {
			boolean tfa = startGoogleAppAuthenticator();
			return tfa;
		} else {
			return false;
		}
	}
	
	@FXML protected void handleSignInAction(ActionEvent event) throws IOException, WriterException {
		
		if(userGrabber.getText().equals("") & passGrabber.getText().equals("")) {
			actionGrabber.setText("Username and Password cannot be empty");
			actionGrabber.setFill(Color.RED);
		} else if(userGrabber.getText().equals("")) {
			actionGrabber.setText("Username cannot be empty");
			actionGrabber.setFill(Color.RED);
		} else if(passGrabber.getText().equals("")) {
			actionGrabber.setText("Password cannot be empty");
			actionGrabber.setFill(Color.RED);
		} 
		else {
			if(loginSuccessful() == true) {
				Parent root = FXMLLoader.load(getClass().getResource("TableCreator.fxml"));
				stage = (Stage)((Node)event.getSource()).getScene().getWindow();
				scene = new Scene(root);
				stage.setScene(scene);
				stage.show();
			} else if (loginSuccessful() == false) {
				actionGrabber.setText("No Credentials Found");
			}
		}
	}
	
	
     @FXML protected void handleCreateNewUsernAction(ActionEvent event) {
		
		if(userGrabberCreator.getText().equals("") & passGrabberCreator.getText().equals("")) {
			actionGrabberCreator.setText("Username and Password cannot be empty");
			actionGrabberCreator.setFill(Color.RED);
		} else if(userGrabberCreator.getText().equals("")) {
			actionGrabberCreator.setText("Username cannot be empty");
			actionGrabberCreator.setFill(Color.RED);
		} else if(passGrabberCreator.getText().equals("")) {
			actionGrabberCreator.setText("Password cannot be empty");
			actionGrabberCreator.setFill(Color.RED);
		} 
		else {
			actionGrabberCreator.setText("User Creation Successful");
			actionGrabberCreator.setFill(Color.GREEN);
			String userCreate = userGrabberCreator.getText();
			String passCreate = passGrabberCreator.getText();
			
			// Toby's added
//			rsa = new RSA_DSA(); // create a new object
//			System.out.println("N:" + rsa.getN());
//			System.out.println("D:" + rsa.getD());
//			System.out.println("E:" + rsa.getE());
//			
//			encryptedPassword = rsa.encrypt(passCreate.getBytes());
//			System.out.println("Encrypting BytesToString: " + RSA_DSA.bytesToString(encryptedPassword));
//			System.out.println("PassCreate: " + passCreate); // check the byte value is working
//			System.out.println("String in Bytes: " + RSA_DSA.bytesToString(passCreate.getBytes())); // check the byte value is working
//			System.out.println("PassCreate getBytes: " + passCreate.getBytes()); // check the byte value is working
			
			
			saveCredentialsToFile(userCreate, passCreate);
		}
	}

	@FXML protected void handleAddColumn(ActionEvent event) {
		
		
		TextField htf = new TextField("");
		
		//Creates table column object with header
		TableColumn<List<String>, String> newColumn = new TableColumn<>();
		
		// Set value for table column
	    newColumn.setCellValueFactory(cellData -> {
	        List<String> row = cellData.getValue();
	        int index = maintable.getColumns().indexOf(cellData.getTableColumn());
	        if (row.size() > index) {
	            return new ReadOnlyStringWrapper(row.get(index));
	        } else {
	            return new ReadOnlyStringWrapper("");
	        }
	    });
		
	    //Without this there will be no edit text cells for a column
		newColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		
		//An on edit handler for table columns (gets row and column index of edited cells)
		newColumn.setOnEditCommit(action -> {
	        TablePosition<List<String>, String> position = action.getTablePosition();
	        int row = position.getRow();
	        int col = position.getColumn();
	        List<String> rowData = (List<String>) maintable.getItems().get(row);
	        rowData.set(col, action.getNewValue()); // Sets new value for edited cell data to row
	    });
		
		// For each row add a new value in table (Prevents out of bounds exception)
		ObservableList<List<String>> items = maintable.getItems();
		for (List<String> row : items) {
		    row.add("");
		}
		
		// These are for the edit text field labels at the top of each column
		newColumn.setEditable(true);
		newColumn.setResizable(true);
	    newColumn.setGraphic(htf);
	    newColumn.setMaxWidth(120);
	    newColumn.setMinWidth(80);
	    maintable.getColumns().add(newColumn);
		
	}
	
	@FXML protected void handleAddRow(ActionEvent event) {
		
		int index = maintable.getSelectionModel().getSelectedIndex();
		if(index == -1) {
			index = maintable.getItems().size();
		} else {
			index++;
		}
		
		ObservableList<String> row = FXCollections.observableArrayList();
		for(int i = 0; i < maintable.getColumns().size(); i++) {
			row.add("");
		}
		maintable.getItems().add(index, row);
		maintable.getSelectionModel().select(index);
	}
	
	@FXML protected void handleRemoveButton(ActionEvent event) {
		maintable.getItems().removeAll(maintable.getSelectionModel().getSelectedItem());
	}
	
	@FXML protected void handleColumnRemoval(ActionEvent event) {
		ObservableList<TableColumn> columns = maintable.getColumns();
		
		if(columns.isEmpty()) {
			System.out.println("No columns present. Try adding a column"); 
		}
		
		TableColumn mostRecentColumn = columns.get(columns.size() - 1);
		
		maintable.getColumns().remove(mostRecentColumn);
	}
	
	@FXML protected void handleSearchDeletion(ActionEvent event) {
		String fIn = searchRef1.getText();
		String sIn = searchRef2.getText();
		
		for(int i = 0 ; i < maintable.getItems().size(); i++) {
			List<String> row = (List<String>) maintable.getItems().get(i);
			if(row.contains(fIn) && row.contains(sIn)) {
				maintable.getItems().remove(i);
				break;
			}
		}
	}
	
	@FXML protected void handleSave(ActionEvent event) {
		// Get the documents path
	    String desktopPath = System.getProperty("user.home") + "/Documents/";
	    
	    // Create the file name
	    String fileName = "tableview.txt";
	    
	    // Create the file
	    File file = new File(desktopPath + fileName);
	    
	    // Open the file writer
	    try (PrintWriter writer = new PrintWriter(file)) {
	        // Write the headers
	    	ObservableList<TableColumn<List<String>, ?>> columns = maintable.getColumns();
            for (TableColumn<List<String>, ?> column : columns) {
                writer.write(column.getText() + "\t");
            }
            writer.println();
	        
	        // Write the data
	        ObservableList<List<String>> rows = maintable.getItems();
	        for (List<String> row : rows) {
	            for (String cell : row) {
	                writer.print(cell);
	                writer.print("\t");
	            }
	            writer.println();
	        }
	    } catch (FileNotFoundException e) {
	        e.printStackTrace();
	    }
		
	}
	
	private void saveCredentialsToFile(String username, String password) {
		try {
			String directory = System.getProperty("user.home");
			String filePath = directory + "/Documents/credentials.txt";
			FileWriter writer = new FileWriter(filePath, true);
			writer.write(username + ":" + password + "\n");
			writer.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	private boolean checkCredentialsInFile(String username, String password) {
		try {
			String directory = System.getProperty("user.home");
			String filePath = directory + "/Documents/credentials.txt";
			File file = new File(filePath);
			Scanner scan = new Scanner(file);
			while(scan.hasNextLine()) {
				String data = scan.nextLine();
				String[] part = data.split(":");
		
				// Toby's changes
//				System.out.println("****************************************************************************");
//				System.out.println("Password entered: " +  part[1]);
//				tempByteArray = part[1].getBytes();
//				System.out.println("tempByteArray String: " + new String(tempByteArray));
//				decryptedPassword = rsa.decrypt(encryptedPassword);
////				decryptedPassword = rsa.decrypt(part[1].getBytes());
////				System.out.println("Part1 plain: " + part[1]);
////				System.out.println("Part1 bytes: " + part[1].getBytes());
//				
//				//String passwordChecker = RSA_DSA.bytesToString(decryptedPassword);
//				System.out.println("Decrypting Bytes: " + RSA_DSA.bytesToString(decryptedPassword));
//				System.out.println("Decrypted String: " + new String(decryptedPassword));
//				
//				String result = RSA_DSA.bytesToString(decryptedPassword);
//			    System.out.println("Decrypted String result: " + result);
//			    System.out.println("Decrypted String result in bytes: " + result.getBytes());
//			    
//			    System.out.println("N:" + rsa.getN());
//			    System.out.println("D:" + rsa.getD());
//			    System.out.println("E:" + rsa.getE());
//				
//				System.out.println("Decrypting Bytes: " + RSA_DSA.bytesToString(decryptedPassword));
//			    System.out.println("Decrypted String: " + new String(decryptedPassword));
				
			    if(part.length == 2 && part[0].equals(username) && part[1].equals(password)) {
				scan.close();
				return true;
				} 
			}
			scan.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	@FXML public void viewTable(ActionEvent event) throws ClassNotFoundException, SQLException, FileNotFoundException {
		System.out.println("view table");
		// TODO Auto-generated method stub
		initialDB();
		data = FXCollections.observableArrayList();
        try {
            //SQL FOR SELECTING ALL OF CUSTOMER
            String SQL = "SELECT * from `javabook`.`test`";
            //ResultSet
            ResultSet rs = connection.createStatement().executeQuery(SQL);

            /**
             * ********************************
             * TABLE COLUMN ADDED DYNAMICALLY *
             *********************************
             */
            for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                //We are using non property style for making dynamic table
                final int j = i;
                col = new TableColumn(rs.getMetaData().getColumnName(i + 1));
                col.setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                    public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
                        return new SimpleStringProperty(param.getValue().get(j).toString());
                    }
                });

                tableView.getColumns().addAll(col);
                System.out.println("Column [" + i + "] ");
            }

            /**
             * ******************************
             * Data added to ObservableList *
             *******************************
             */
            while (rs.next()) {
                //Iterate Row
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    //Iterate Column
                    row.add(rs.getString(i));
                }
                System.out.println("Row [1] added " + row);
                data.add(row);

            }

            //FINALLY ADDED TO TableView
            tableView.setItems(data);
            System.out.println("Data: " + data);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error on Building Data");
        }
	}
	
	@FXML public void view(ActionEvent event) throws ClassNotFoundException, SQLException, FileNotFoundException {
		System.out.println("viewed");
		// TODO Auto-generated method stub
		initialDB();
		String query = "SELECT * FROM `javabook`.`test` WHERE ID = '" + textId.getText().trim() + "'";
		
		try {
			// execute statement
			results = statement.executeQuery(query);
			loadFields(results);
			
		} catch (SQLException Ex){
			labelStatus.setText("Record failed");
			System.out.println(Ex.getMessage());
		}
	}
	
	@FXML private void update(ActionEvent event) {
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
			labelStatus.setText("Update completed or does not exist please view the ID");
			labelStatus.setTextFill(Color.GREEN);
			System.out.println("Update suceeded");
			
		} catch (SQLException Ex){
			labelStatus.setText("Update failed");
			labelStatus.setTextFill(Color.RED);
			System.out.println(Ex.getMessage());
		}
	}
	
	@FXML private void clear(ActionEvent event) {
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
	
	@FXML private void insert(ActionEvent event) {
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
			labelStatus.setText("Insert completed");
			labelStatus.setTextFill(Color.GREEN);
			System.out.println("Insert suceeded");
			
		} catch (SQLException Ex){
			labelStatus.setText("Insert failed");
			labelStatus.setTextFill(Color.RED);
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
	
	public void initialDB() throws ClassNotFoundException, SQLException {
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
	
	public boolean startGoogleAppAuthenticator () throws WriterException, IOException{
		
			String secretKey = "QDWSM3OYBPGTEVSPB5FKVDM3CSNCWHVI";
			String email = "Team5@gmail.com";
			String companyName = "CSU ITC 303";
			String barCodeUrl = getGoogleAuthenticatorBarCode(secretKey, email, companyName);
			createQRCode(barCodeUrl, "QRCode.png", 400, 400);
					
			System.out.print("Please enter 2fA code here -> ");
//			Scanner scanner = new Scanner(System.in);
//			String code = scanner.nextLine();
			String code = tfacode.getText();
			if (code.equals(getTOTPCode(secretKey))) {
				System.out.println("Logged in successfully");
				return true;
			} else {
				System.out.println("Invalid 2FA Code");
				return false;
			}

		
	}
	
	

		public static String generateSecretKey() {
			SecureRandom random = new SecureRandom();
			byte[] bytes = new byte[20];
			random.nextBytes(bytes);
			Base32 base32 = new Base32();
			return base32.encodeToString(bytes);
		}

		public static String getTOTPCode(String secretKey) {
			Base32 base32 = new Base32();
			byte[] bytes = base32.decode(secretKey);
			String hexKey = Hex.encodeHexString(bytes);
			return TOTP.getOTP(hexKey);
		}

		public static String getGoogleAuthenticatorBarCode(String secretKey, String account, String issuer) {
			try {
				return "otpauth://totp/"
						+ URLEncoder.encode(issuer + ":" + account, "UTF-8").replace("+", "%20")
						+ "?secret=" + URLEncoder.encode(secretKey, "UTF-8").replace("+", "%20")
						+ "&issuer=" + URLEncoder.encode(issuer, "UTF-8").replace("+", "%20");
			} catch (UnsupportedEncodingException e) {
				throw new IllegalStateException(e);
			}
		}

		public static void createQRCode(String barCodeData, String filePath, int height, int width)
				throws WriterException, IOException {
			BitMatrix matrix = new MultiFormatWriter().encode(barCodeData, BarcodeFormat.QR_CODE, width, height);
			try (FileOutputStream out = new FileOutputStream(filePath)) {
				MatrixToImageWriter.writeToStream(matrix, "png", out);
			}
		}

	

}

