package application.viewControllers;
import java.io.IOException;
import java.time.ZonedDateTime;

import application.CalendarActivity;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class ActivityCreationController {
	 public static CalendarActivity ca = new CalendarActivity();
	
	 @FXML
	 private TextField clientGrabberCreator;

	 @FXML
	 private TextField doctorGrabberCreator;

	 @FXML
	 private TextField patientIdGrabberCreator ;
	 
	 public CalendarActivity getCa() {
		return ca;
	}

	@FXML
	 private TextField timeGrabberCreator;
	 
	 @FXML	
	 public void handleCreateNewActivityAction(MouseEvent mouseEvent) throws IOException{
		  ca.setClientName(clientGrabberCreator.getText());
		  ca.setDoctorName(doctorGrabberCreator.getText());
		  ca.setPatientNo(Integer.parseInt(patientIdGrabberCreator.getText()));	
		  
		  // zone date example 2023-08-21T23:00+10:00[Australia/Sydney]
		  
		  // year the year to represent, from MIN_YEAR to MAX_YEAR
		  // month the month-of-year to represent, from 1 (January) to 12 (December)
		  // dayOfMonth the day-of-month to represent, from 1 to 31
		  // hour the hour-of-day to represent, from 0 to 23
		  // minute the minute-of-hour to represent, from 0 to 59
		  // second the second-of-minute to represent, from 0 to 59
		  // nanoOfSecond the nano-of-second to represent, from 0 to 999,999,999
		  // zone the time-zone, not null
		  ZonedDateTime dateFocus = ZonedDateTime.now(); ;
		  ZonedDateTime time = ZonedDateTime.of(2023, 8, 14, 6, 30, 0, 0, dateFocus.getZone());
		  ca.setDate(time);
		  System.out.println(time);
		  switchToHomepage(mouseEvent);
	 }
	 
	
	 @FXML	
	 public void switchToHomepage(MouseEvent mouseEvent) throws IOException{
		 Parent root = FXMLLoader.load(getClass().getResource("/application/fxmlscenes/calendar_new.fxml"));
		 Stage stage = (Stage)((Node)mouseEvent.getSource()).getScene().getWindow();
		 Scene scene = new Scene(root);
		 stage.setScene(scene);
		 stage.show();
	 }

}
