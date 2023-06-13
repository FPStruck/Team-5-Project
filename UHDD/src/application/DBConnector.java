package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.scene.paint.Color;

public class DBConnector {
	// these objects will be used in querying the database and processing the results
		Connection connection; // the controllers needs to use this connection 
		private Statement statement;
		private ResultSet resultSet;
		
		
		public void initialiseDB() throws ClassNotFoundException, SQLException {
		
			// loads and checks the driver
			Class.forName("com.mysql.cj.jdbc.Driver");
			System.out.println("Driver loaded:");
		
			// connection for database...make sure the URL is correct JDBC:MYSQL
			// connect to Jonathan's cloud
			String url="jdbc:mysql://itc303-db01.mysql.database.azure.com:3306/testdb?useSSL=true";
			String username = "itc303admin";
			String password = "L3cr3X2bNCtcvf";
			
			// connect to local database
//			String url = "jdbc:mysql://127.0.0.1:3306/testdb";
//			String username = "root";
//			String password = "mysql";
		
			// connect to the database
			try {
				connection = DriverManager.getConnection(url, username, password);
				System.out.println("Database connected:");
				
				//labelStatus.setText("Database connected");
				//labelStatus.setTextFill(Color.GREEN);
				statement = connection.createStatement();
			} catch (SQLException ex) {
				System.out.println(ex.getMessage());
				//labelStatus.setText("Connection failed");
				//labelStatus.setTextFill(Color.RED);
			} 
		}
		
		public void closeConnection() throws SQLException {
			try {
	            connection.close();
			} catch (SQLException ex) {
				
			}
		}
		
		public ResultSet executeQueryReturnResults(String sql) throws SQLException {
	        Statement statement = connection.createStatement();
	        return statement.executeQuery(sql);
	    }

		public void executeUpdate(String sql) throws SQLException {
			Statement statement = connection.createStatement();
			statement.executeUpdate(sql);
	    }
		
		public void executeQuery(String sql) throws SQLException {
			Statement statement = connection.createStatement();
			statement.executeQuery(sql);
	    }
		
		public void createUserExecuteQuery(String username, String passwordHash, String params, String email) throws SQLException {
			String sql = "INSERT INTO user_details (username, password_hash, password_params, email) VALUES (?, ?, ?, ?)";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, username);
			statement.setString(2, passwordHash);
			statement.setString(3, params);
			statement.setString(4, email);
			statement.executeUpdate();
		}
		
		public ResultSet QueryReturnResultsFromUser(String username) throws SQLException {
			String sql = "SELECT * FROM testdb.user_details WHERE username = ?";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, username);
	        return statement.executeQuery();
	    }
		
		public ResultSet QueryReturnResultsFromPatientId(String patientId) throws SQLException {
			String sql = "SELECT * FROM testdb.test3 WHERE ID = ?";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, patientId);
			System.out.println(sql);
	        return statement.executeQuery();
	    }
		
}
