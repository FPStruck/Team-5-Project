package application;
import java.time.LocalDate;
import java.time.LocalTime;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.Calendar.Style;
import com.calendarfx.model.CalendarSource;
import com.calendarfx.view.CalendarView;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class CalendarApp extends Application {

    private static Stage primaryStage;
    private static CalendarView calendarView;

    static Calendar<Object> doctors = new Calendar<>("Doctor's");
    static Calendar<Object> nurses = new Calendar<>("Nurse's");
    static CalendarSource myCalendarSource = new CalendarSource("My Calendars");

    @Override
    public void start(Stage primaryStage) throws Exception {
        CalendarApp.primaryStage = primaryStage;

        doctors.setStyle(Style.STYLE2);
        nurses.setStyle(Style.STYLE3);

        if (myCalendarSource.getCalendars().isEmpty()) {
            myCalendarSource.getCalendars().addAll(doctors, nurses);
        }

        calendarView = new CalendarView();
        calendarView.getCalendarSources().addAll(myCalendarSource);
        calendarView.setRequestedTime(LocalTime.now());

        // Remove the default calendar
        calendarView.getCalendarSources().forEach(calendarSource -> {
            calendarSource.getCalendars().removeIf(calendar -> calendar.getName().equals("Default"));
        });

        Thread updateTimeThread = new Thread(() -> {
            while (true) {
                Platform.runLater(() -> {
                    calendarView.setToday(LocalDate.now());
                    calendarView.setTime(LocalTime.now());
                });

                try {
                    Thread.sleep(10000); // update every 10 seconds
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        updateTimeThread.setPriority(Thread.MIN_PRIORITY);
        updateTimeThread.setDaemon(true);
        updateTimeThread.start();

        primaryStage.setOnCloseRequest(event -> {
            event.consume(); // Consume the event to prevent the stage from closing
            primaryStage.hide(); // Hide the stage instead of closing it
        });

        Button addButton = new Button("Add Calendar");
        addButton.setOnAction(event -> {
            Calendar<Object> doctor2 = new Calendar<>("Doctor 2");
            myCalendarSource.getCalendars().add(doctor2);
        });

        AnchorPane buttonContainer = new AnchorPane(addButton);
        buttonContainer.setStyle("-fx-background-color: transparent;");

        AnchorPane root = new AnchorPane(calendarView, buttonContainer);
        AnchorPane.setTopAnchor(calendarView, 0.0);
        AnchorPane.setBottomAnchor(calendarView, 0.0);
        AnchorPane.setLeftAnchor(calendarView, 0.0);
        AnchorPane.setRightAnchor(calendarView, 0.0);
        AnchorPane.setTopAnchor(buttonContainer, 10.0);
        AnchorPane.setRightAnchor(buttonContainer, 10.0);

        Scene scene = new Scene(root, 1000, 800);
        primaryStage.setTitle("Calendar");
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

    public static void showCalendar() {
        Platform.runLater(() -> {
            if (primaryStage.isShowing()) {
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("Calendar");
                alert.setHeaderText("Calendar is already open");
                alert.setContentText("The calendar is already being displayed.");
                alert.showAndWait();
            } else {
                primaryStage.show();
            }
        });
    }

    public static void hideCalendar() {
        Platform.runLater(() -> primaryStage.hide());
    }

    public static Calendar<Object> getDoctors() {
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
