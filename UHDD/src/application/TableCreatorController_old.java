package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.util.Callback;

public class TableCreatorController_old {
	private Stage stage;
	private Scene scene;
	@FXML private TableView maintable;
	@FXML private TextField searchRef1;
	@FXML private TextField searchRef2;
	@FXML private TableView<ObservableList> tableView = new TableView();
	static ObservableList<ObservableList> data;
	static TableColumn col;
	static ObservableList<String> ddList;
	@FXML private ComboBox<String> dd = new ComboBox();
	
	DBConnector dbConnection = new DBConnector();
	
	@FXML
    public void initialize() throws ClassNotFoundException, SQLException { // this will load all the variables in the fields referring to components  
		ddList = FXCollections.observableArrayList("one", "two", "three");
		dd.getItems().addAll(ddList);
		System.out.println(dd.getItems());		
    }
	
	public void switchToMySQL(ActionEvent event) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("../fxmlScenes/Test.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
		MySQL_test mySQL_test = new MySQL_test();
		mySQL_test.start(stage);
	}
	
	@FXML 
	public void switchToPatientInformation(ActionEvent event) throws Exception {
		
		Parent root = FXMLLoader.load(getClass().getResource("../fxmlScenes/PatientInformation.fxml"));		
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	@FXML 
	public void switchToPatientDirectory(ActionEvent event) throws Exception {
		
		Parent root = FXMLLoader.load(getClass().getResource("../fxmlScenes/PatientDirectory.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
		
	}
	
	@FXML 
	protected void handleAddColumn(ActionEvent event) {
		
	
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
	
	@FXML 
	protected void handleAddRow(ActionEvent event) {
		
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
	
	@FXML 
	protected void handleRemoveButton(ActionEvent event) {
		maintable.getItems().removeAll(maintable.getSelectionModel().getSelectedItem());
	}
	
	@FXML 
	protected void handleColumnRemoval(ActionEvent event) {
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
	
	@FXML public void viewTable(ActionEvent event) throws ClassNotFoundException, SQLException, FileNotFoundException {
		System.out.println("view table");
		// TODO Auto-generated method stub
		dbConnection.initialiseDB();
		
		data = FXCollections.observableArrayList();
        try {
            //MySql query table
            String SQL = "SELECT * from `testdb`.`test3`";
            //ResultSet
            ResultSet rs = dbConnection.executeQueryReturnResults(SQL);

            // add table column dynamically
            for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                //We are using non property style for making dynamic table
                final int j = i;
                col = new TableColumn(rs.getMetaData().getColumnName(i + 1)); // loops through the results and grabs the column name
                col.setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() { // makes the columns ready for population
                    public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
                        return new SimpleStringProperty(param.getValue().get(j).toString());
                    }
                });

                tableView.getColumns().addAll(col);
                System.out.println("Column [" + i + "] ");
            }

            // add to observation list
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

            //add to table view
            tableView.setItems(data);
            System.out.println("Data: " + data);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error on Building Data");
        }
        dbConnection.closeConnection();	
        }
}
