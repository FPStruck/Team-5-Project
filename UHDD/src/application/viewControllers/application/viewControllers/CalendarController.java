package application.viewControllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.ZonedDateTime;
import java.util.*;

import application.CalendarActivity;

public class CalendarController implements Initializable {

    ZonedDateTime dateFocus;
    ZonedDateTime today;
    static Map<Integer, List<CalendarActivity>> calendarActivityMap = new HashMap<>(); // create a hash map
    static List<CalendarActivity> newList = new ArrayList<>();

    @FXML
    private Text year;

    @FXML
    private Text month;

    @FXML
    private FlowPane calendar;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dateFocus = ZonedDateTime.now(); // get the local zone date
        today = ZonedDateTime.now(); // get the local zone time
        drawCalendar(); // the FXML is blank apart from the edit text boxes
        System.out.println("Drawing");
    }

    @FXML
    // button function to draw a new calendar form the previous month
    void backOneMonth(ActionEvent event) {
        dateFocus = dateFocus.minusMonths(1);
        calendar.getChildren().clear();
        drawCalendar();
    }

    @FXML
    // button function to draw a new calendar for the next month
    void forwardOneMonth(ActionEvent event) {
        dateFocus = dateFocus.plusMonths(1);
        calendar.getChildren().clear();
        drawCalendar();
    }

    private void drawCalendar(){
        year.setText(String.valueOf(dateFocus.getYear())); // set the year text box in the FXML
        month.setText(String.valueOf(dateFocus.getMonth())); // set the month text box in the FXML

        double calendarWidth = calendar.getPrefWidth(); // get the width from the pane
        double calendarHeight = calendar.getPrefHeight(); // get the height from the pane
        double strokeWidth = 1;
        double spacingH = calendar.getHgap(); // get the horizontal gap from the next component 
        double spacingV = calendar.getVgap(); // get the vertical gap from the next component 

        //List of activities for a given month
        Map<Integer, List<CalendarActivity>> calendarActivityMap = getCalendarActivitiesMonth(dateFocus); // this will store the activities 

        int monthMaxDate = dateFocus.getMonth().maxLength(); // this months maximum date 
        
        //Check for leap year
        if(dateFocus.getYear() % 4 != 0 && monthMaxDate == 29){
            monthMaxDate = 28; // change the month maximum date for leap years
        }
        // get the off set date for this month
        int dateOffset = ZonedDateTime.of(dateFocus.getYear(), dateFocus.getMonthValue(), 1,0,0,0,0,dateFocus.getZone()).getDayOfWeek().getValue();
//        System.out.println(dateOffset);
        
        for (int i = 0; i < 6; i++) { // six boxes down
            for (int j = 0; j < 7; j++) { // seven boxed across
                StackPane stackPane = new StackPane(); // new pane for each rectangle
                
                // start drawing the rectangles 
                Rectangle rectangle = new Rectangle();
                rectangle.setFill(Color.TRANSPARENT);
                rectangle.setStroke(Color.BLACK);
                rectangle.setStrokeWidth(strokeWidth);
                double rectangleWidth =(calendarWidth/7) - strokeWidth - spacingH; // calculate the rectangle width by dividing the pane width by 7 minus the line and gap
                rectangle.setWidth(rectangleWidth);
                double rectangleHeight = (calendarHeight/6) - strokeWidth - spacingV; // same calculation as above however from the top to bottom
                rectangle.setHeight(rectangleHeight);
                stackPane.getChildren().add(rectangle);

                int calculatedDate = (j+1)+(7*i); // looping through each rectangle created 
                if(calculatedDate > dateOffset){ // make sure the first box used is after the off-set
                    int currentDate = calculatedDate - dateOffset; // the first current date will be 1 since you minus the offset from the first box
                    if(currentDate <= monthMaxDate){
                        Text date = new Text(String.valueOf(currentDate)); // write the date number i.e. 1 to 31
                        double textTranslationY = - (rectangleHeight / 2) * 0.75; // make sure the text fits the rectangle 
                        date.setTranslateY(textTranslationY);
                        stackPane.getChildren().add(date); // insert the date into the rectangle

                        List<CalendarActivity> calendarActivities = calendarActivityMap.get(currentDate); // make a list from the activity map created earlier 
                        if(calendarActivities != null){ // if the list is not empty than we will create an activity 
                            createCalendarActivity(calendarActivities, rectangleHeight, rectangleWidth, stackPane);
                        }
                    }
                    if(today.getYear() == dateFocus.getYear() && today.getMonth() == dateFocus.getMonth() && today.getDayOfMonth() == currentDate){
                        rectangle.setStroke(Color.BLUE);  // highlight todays date
                    }
                }
                calendar.getChildren().add(stackPane); // add the rectangle into the main pane
            }
        }
    }

    private void createCalendarActivity(List<CalendarActivity> calendarActivities, double rectangleHeight, double rectangleWidth, StackPane stackPane) {
        VBox calendarActivityBox = new VBox(); // new vbox for the activities inside the date rectangle 
        for (int k = 0; k < calendarActivities.size(); k++) { // loop through the activity list 
            if(k >= 2) {
                Text moreActivities = new Text("..."); // since we can only fit two activities in each box the rest will not be added
                calendarActivityBox.getChildren().add(moreActivities);
                moreActivities.setOnMouseClicked(mouseEvent -> {
                    //On ... click print all activities for given date
                	try {
						switchToActivityCreation(mouseEvent);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                    System.out.println(calendarActivities);
                });
                break;
            }
            // if the activities are the first tow than we will create insert the text into the vbox
            Text text = new Text(calendarActivities.get(k).getClientName() + ", " + calendarActivities.get(k).getDate().toLocalTime());
            calendarActivityBox.getChildren().add(text);
            text.setOnMouseClicked(mouseEvent -> {
                //On Text clicked
            	try {
					switchToActivityCreation(mouseEvent);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                System.out.println(text.getText());
            });
        }
        
        // make sure the vbox fits inside the rectangle date box
        calendarActivityBox.setTranslateY((rectangleHeight / 2) * 0.20);
        calendarActivityBox.setMaxWidth(rectangleWidth * 0.8);
        calendarActivityBox.setMaxHeight(rectangleHeight * 0.65);
        calendarActivityBox.setStyle("-fx-background-color:GRAY");
        stackPane.getChildren().add(calendarActivityBox); // insert the activity 
    }

    private Map<Integer, List<CalendarActivity>> getCalendarActivitiesMonth(ZonedDateTime dateFocus) {
    	List<CalendarActivity> calendarActivities = new ArrayList<>(); // create a list to return
        int year = dateFocus.getYear();
        int month = dateFocus.getMonth().getValue();
        
        // create a random activity 
//        Random random = new Random();
//        for (int i = 0; i < dateFocus.getMonth().maxLength(); i += 2) {
//            ZonedDateTime time = ZonedDateTime.of(year, month, i+1, random.nextInt(23)+1,0,0,0,dateFocus.getZone()); // make sure the activity is between 1 and 28
////            System.out.println("Time: " + time);
//            calendarActivities.add(new CalendarActivity(time, "Toby", random.nextInt()));
//        }

        return createCalendarMap(calendarActivities); // return a map which is created by the function below
    }
    
    private Map<Integer, List<CalendarActivity>> createCalendarMap(List<CalendarActivity> calendarActivities) {
        
        ActivityCreationController ca = new ActivityCreationController();
        for (CalendarActivity activity: calendarActivities) { // loop through the list
            int activityDate = activity.getDate().getDayOfMonth(); // get the date from each element in the list
            if(!calendarActivityMap.containsKey(activityDate)){
                calendarActivityMap.put(activityDate, calendarActivities); // created a key from the date
            } else {
                // make a new list from the old list
            	List<CalendarActivity> OldListByDate = calendarActivityMap.get(activityDate);

                newList = new ArrayList<>(OldListByDate);
                newList.add(activity);
                calendarActivityMap.put(activityDate, newList); // insert the list into the map with a key
            }
        }
        if (!(ca.getCa().getDate() == null)) {
	        int caActivityDate = ca.getCa().getDate().getDayOfMonth(); // get the date from each element in the list
//	        List<CalendarActivity> caList = new ArrayList<>();
	        newList.add(ca.getCa());
	        calendarActivityMap.put(caActivityDate, newList); // insert the list into the map with a key
        }
        return  calendarActivityMap; // return the map
    }
    
    @FXML	
	public void switchToActivityCreation(MouseEvent mouseEvent) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("/application/fxmlscenes/ActivityCreation.fxml"));
		Stage stage = (Stage)((Node)mouseEvent.getSource()).getScene().getWindow();
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();		
	}
}
