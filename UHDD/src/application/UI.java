package application;
	
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

import javafx.application.Application;
import javafx.beans.property.ReadOnlyStringWrapper;
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
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
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
	@FXML private TableView maintable;
	@FXML private AnchorPane anchor;
	@FXML private TextField searchRef1;
	@FXML private TextField searchRef2;
	@FXML private Text actionGrabberCreator;
	@FXML private TextField userGrabberCreator;
	@FXML private TextField passGrabberCreator;
	@FXML private Button addColumn;
	@FXML private Button addRow;
	
	//Buttons for overview UI
	@FXML private Button fetchDiagnoses;
	@FXML private Button fetchHistory;
	@FXML private Button fetchPerscription;
	@FXML private Button fetchReport;

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
	
	public boolean loginSuccessful() {
		String userLog = userGrabber.getText();
		String passLog = passGrabber.getText();
		boolean credentialsMatch = checkCredentialsInFile(userLog, passLog);
		if(credentialsMatch) {
			return true;
		} else {
			return false;
		}
	}
	
	@FXML protected void handleSignInAction(ActionEvent event) throws IOException {
		
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
	
	//Overview button actions
	
    @FXML
    private void fetchDiagnoses() {
        System.out.println("Hopefully gets 'diagnoses' info");
    }
    
    // Other controller methods and variables
    
	
	
	@FXML
	private void fetchHistory() {
	    System.out.println("Get patient history");
	}
	
	// Other controller methods and variables
	
	
	
	@FXML
	private void fetchPerscription() {
	    System.out.println("Get Patient perscription");
	}
	
	// Other controller methods and variables
	
	
	
	@FXML
	private void fetchReport() {
	    System.out.println("Gets report");
	}
	
	// Other controller methods and variables
	
	}

