package application;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.Calendar.Style;
import com.calendarfx.model.CalendarSource;
import com.calendarfx.model.Entry;
import com.calendarfx.view.CalendarView;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.scene.layout.BackgroundImage;

public class CalendarApp extends Application {
	 	// add calendars, this will not save the default calendar 
		static Calendar<Object> doctors = new Calendar<Object>("Doctor's"); 
		static Calendar<Object> nurses = new Calendar<Object>("Nurse's");
		static CalendarSource myCalendarSource = new CalendarSource("My Calendars");
		static CalendarView calendarView = new CalendarView();

        @Override
        public void start(Stage primaryStage) throws Exception {

            CalendarView calendarView = new CalendarView(); // create a new calendar view
            
//          BackgroundImage bgTmage = new BackgroundImage(new Image("https://www.google.com/images/srpr/logo3w.png"), null, null, null, null); // not working 
            
            // colors for each calendar to distinguish from each other 
            doctors.setStyle(Style.STYLE2);
            nurses.setStyle(Style.STYLE3);
            
            // create a calendar source 
            if (myCalendarSource.getCalendars().isEmpty()) {
            	myCalendarSource.getCalendars().addAll(doctors, nurses);
            }
            
            // add all the calendars to the view 
            calendarView.getCalendarSources().addAll(myCalendarSource);
            
            // set the time 
            calendarView.setRequestedTime(LocalTime.now());
            
            // create a thread to sleep for 10 seconds than update the day and time
            Thread updateTimeThread = new Thread("Calendar: Update Time Thread") {
            	@Override
                public void run() {
                    while (true) {
                       Platform.runLater(() -> {
                            calendarView.setToday(LocalDate.now());
                            calendarView.setTime(LocalTime.now());
                        });

                        try {
                            // update every 10 seconds
                            sleep(10000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
            	}
            };
            
            // set the thread
            updateTimeThread.setPriority(Thread.MIN_PRIORITY); // run last
            updateTimeThread.setDaemon(true); // cause the thread to stop when there is only deamon threads running
            updateTimeThread.start();

            Scene scene = new Scene(calendarView);
            primaryStage.setTitle("Calendar");
            primaryStage.setScene(scene);
            primaryStage.setWidth(1000);
            primaryStage.setHeight(800);
            primaryStage.centerOnScreen();
            primaryStage.show();
        }

        public static CalendarView getCalendarView() {
			return calendarView;
		}

		public static Calendar<Object> getDoctors() {
//			Map<LocalDate, List<Entry<?>>> entry = doctors.findEntries(LocalDate.now(), LocalDate.MAX, ZoneId.systemDefault());
//			System.out.println(entry);
			return doctors;
		}

		public static Calendar<Object> getNurses() {
			return nurses;
		}

		public static CalendarSource getMyCalendarSource() {
			return myCalendarSource;
		}

		public static void main(String[] args) {
                launch(args);
        }
}
