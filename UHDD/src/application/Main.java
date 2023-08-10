package application;
	
import javafx.stage.Stage;

import java.sql.SQLException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;


public class Main extends Application {
	@Override
	
	public void start(Stage primaryStage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/resource/Login.fxml"));
			Scene scene = new Scene(root,915,500);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	 public void stop() throws SQLException, ClassNotFoundException{
		UserSession us = new UserSession();
		DBConnector db = new DBConnector();
		db.initialiseDB();
		String name = us.getUserName();
		System.out.println("Stage is closing: " + name);
		db.setLoggedInStatus(name, 0);
	 } 
	
	public static void main(String[] args) {
		Main.launch(args);
	}
}
