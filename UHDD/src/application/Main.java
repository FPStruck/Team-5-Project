package application;

import javafx.stage.Stage;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class Main extends Application {
	
	@Override
	public void start(Stage primaryStage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/application/fxmlScenes/Login.fxml"));
			Scene scene = new Scene(root,915,500);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	 public void stop() throws Exception{
		UserSession us = new UserSession();
		DBConnector db = new DBConnector();
		db.initialiseDB();
		String name = us.getUserName();
		System.out.println("Stage is closing: " + name);
		db.setLoggedInStatus(name, 0);
		db.closeConnection();
	 } 
	
}
