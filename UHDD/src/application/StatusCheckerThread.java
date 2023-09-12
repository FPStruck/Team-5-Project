package application;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

public class StatusCheckerThread extends Thread {
    private String username;
    private volatile boolean running = true; // Flag to control the thread execution
    private DBConnector dbConnector;
    private int alertCounter = 0;

    public StatusCheckerThread(String username, DBConnector dbConnector) {
        this.username = username;
        this.dbConnector = dbConnector;
    }

    public void stopThread() {
        running = false;
    }

    @Override
    public void run() {
        while (running) {
            try {
                int loggedInStatus = dbConnector.getLoggedInStatus(username);

                if (loggedInStatus == 0) {
                    stopThread(); // Stop the thread before showing the alert

                    Platform.runLater(() -> {
                        handleSecondLogin();
                    });

                    // Wait until the user makes a choice in the alert
                    while (!running) {
                        Thread.sleep(1000); // Sleep for a short duration
                        alertCounter++;

                        if (alertCounter >= 15) {
                            Platform.runLater(() -> {
                                try {
									handleCloseApplication();
								} catch (SQLException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
                            });
                        }
                    }
                }

                Timestamp lastLoggedInDate = dbConnector.getLastLoggedInDate(username);

                if (lastLoggedInDate != null) {
                    LocalDateTime currentDateTime = LocalDateTime.now();
                    Timestamp currentTimeStamp = Timestamp.valueOf(currentDateTime);

                    Duration timeDifference = Duration.between(lastLoggedInDate.toLocalDateTime(), currentDateTime);
                    long hoursDifference = timeDifference.toHours();

                    if (hoursDifference > 5) {
                        stopThread();

                        // User has been inactive for 5 hours or more, show the inactivity alert
                        Platform.runLater(() -> {
                            handleInactivity();
                        });

                        while (!running) {
                            Thread.sleep(1000); // Sleep for a short duration
                            alertCounter++;

                            if (alertCounter >= 15) {
                                Platform.runLater(() -> {
                                    try {
										handleCloseApplication();
									} catch (SQLException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
                                });
                            }
                        }
                    }
                }

                // Sleep for a period (e.g., every 5 seconds)
                Thread.sleep(5000);
            } catch (InterruptedException | SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleSecondLogin() {
        Platform.runLater(() -> {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Login Attempt Detected");
            alert.setHeaderText("Another User Has Attempted to Access This Account:");
            alert.setContentText("Do you want to continue the session? (You will be automatically logged out within 15 seconds if an option is not chosen)");

            ButtonType continueButton = new ButtonType("Continue Session");
            ButtonType quitButton = new ButtonType("Quit Session");

            alert.getButtonTypes().setAll(continueButton, quitButton);

            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == continueButton) {
                try {
                    dbConnector.setLoggedInStatus(UsernameStorage.getUsername(), 1);
                    startThread(); // Start the thread again
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else if (result.isPresent() && result.get() == quitButton) {
                try {
                    dbConnector.setLoggedInStatus(username, 0);
                    dbConnector.closeConnection();
                    Platform.exit();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void handleInactivity() {
        Platform.runLater(() -> {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Inactivity Detected");
            alert.setHeaderText("You Have Been Logged In For An Extended Period:");
            alert.setContentText("Do you want to continue the session? (You will be automatically logged out within 15 seconds if an option is not chosen)");

            ButtonType continueButton = new ButtonType("Continue Session");
            ButtonType quitButton = new ButtonType("Quit Session");

            alert.getButtonTypes().setAll(continueButton, quitButton);

            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == continueButton) {
                try {
                    dbConnector.setLoggedInStatus(UsernameStorage.getUsername(), 1);
                    LocalDateTime currentDateTime = LocalDateTime.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    String formattedDateTime = currentDateTime.format(formatter);
                    Timestamp currentTimestamp = Timestamp.valueOf(formattedDateTime);

                    dbConnector.setLastLoggedInTime(UsernameStorage.getUsername(), currentTimestamp);
                    startThread(); // Start the thread again
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else if (result.isPresent() && result.get() == quitButton) {
                try {
                    dbConnector.setLoggedInStatus(username, 0);
                    dbConnector.closeConnection();
                    Platform.exit();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void handleCloseApplication() throws SQLException {
    	dbConnector.setLoggedInStatus(username, 0);
    	dbConnector.closeConnection();
    	Platform.exit();
        System.exit(0);
    }

    private void startThread() {
        running = true;
    }
}
