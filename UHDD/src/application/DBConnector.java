package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.calendarfx.model.Entry;

public class DBConnector {
	// these objects will be used in querying the database and processing the results
		public Connection connection; // the controllers needs to use this connection 
		String nextA;
		String nextTitle;
		String nextId;
		Boolean nextFullDay;
		LocalDate nextStartDate;
		LocalDate nextEndDate;
		LocalTime nextStartTime;
		LocalTime nextEndTime;
		ZoneId nextZoneId;
		Boolean nextRecurring;
		String nextRRule;
		Boolean nextRecurrence;		
		
		public void initialiseDB() throws Exception {
		
			// loads and checks the driver
			Class.forName("com.mysql.cj.jdbc.Driver");
			System.out.println("Driver loaded:");
		
			// connection for database...make sure the URL is correct JDBC:MYSQL
			// connect to Jonathan's cloud
			String url="jdbc:mysql://itc303-db01.mysql.database.azure.com:3306/testdb?useSSL=true";
			String username = "itc303admin";
			String password = Decryptor.decrypt("HTTjDc5keIHeQ48mt3A+Vw=="); // Hash this password using a function and unhash it when it's called for this line so its not stored in plain text (makes it harder for people to access DB)
			
			// connect to local database
//			String url = "jdbc:mysql://127.0.0.1:3306/testdb";
//			String username = "root";
//			String password = "mysql";
		
			// connect to the database
			try {
				connection = DriverManager.getConnection(url, username, password);
				System.out.println("Database connected:");
				
				connection.createStatement();
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
		
	    public void setLoggedInStatus(String username, int loggedIn) throws SQLException {
	        String sql = "UPDATE user_details SET logged_in = ? WHERE username = ?";
	        PreparedStatement statement = connection.prepareStatement(sql);
	        statement.setInt(1, loggedIn);
	        statement.setString(2, username);
	        statement.executeUpdate();
	    }
	    
	    public void setLastLoggedInTime(String username, Timestamp timestamp) throws SQLException {
	        String sql = "UPDATE user_details SET last_logged_in_date = ? WHERE username = ?";
	        PreparedStatement statement = connection.prepareStatement(sql);
	        statement.setTimestamp(1, timestamp);
	        statement.setString(2, username);
	        statement.executeUpdate();
	    }
	    
	    public int getLoggedInStatus(String username) throws SQLException {
	        int loggedInStatus = -1;
	        String sql = "SELECT logged_in FROM user_details WHERE username = ?";

	        try (PreparedStatement statement = connection.prepareStatement(sql)) {
	            statement.setString(1, username);
	            try (ResultSet resultSet = statement.executeQuery()) {
	                if (resultSet.next()) {
	                    loggedInStatus = resultSet.getInt("logged_in");
	                }
	            }
	        }
	        return loggedInStatus;
	    }

	    public Timestamp getLastLoggedInDate(String username) throws SQLException {
	        Timestamp lastLoggedInDate = null;
	        String sql = "SELECT last_logged_in_date FROM user_details WHERE username = ?";

	        try (PreparedStatement statement = connection.prepareStatement(sql)) {
	            statement.setString(1, username);
	            try (ResultSet resultSet = statement.executeQuery()) {
	                if (resultSet.next()) {
	                    lastLoggedInDate = resultSet.getTimestamp("last_logged_in_date");
	                }
	            }
	        }
	        return lastLoggedInDate;
	    }

	    
	    public void checkAndSetLoggedOutStatus() throws SQLException {
	        String sql = "SELECT username, logged_in, last_logged_in_date FROM user_details WHERE logged_in = 1";
	        PreparedStatement statement = connection.prepareStatement(sql);
	        ResultSet resultSet = statement.executeQuery();
	        List<String> usersToUpdateStatus = new ArrayList<>();

	        while (resultSet.next()) {
	            String username = resultSet.getString("username");
	            int loggedInStatus = resultSet.getInt("logged_in");
	            Timestamp lastLoggedInDate = resultSet.getTimestamp("last_logged_in_date");

	            LocalDateTime currentDateTime = LocalDateTime.now();
	            Timestamp loginTimestamp = Timestamp.valueOf(currentDateTime);

	            if (loggedInStatus == 1) {
	                if (lastLoggedInDate == null) {
	                    // Update last logged-in date to current date and time
	                    updateLastLoggedInDateAndStatus(username, loginTimestamp);
	                } else {
	                    // Check if the user has exceeded 5 minutes of inactivity
	                    Duration timeDifference = Duration.between(lastLoggedInDate.toLocalDateTime(), currentDateTime);
	                    long hoursDifference = timeDifference.toHours();

	                    if (hoursDifference > 5) {
	                        usersToUpdateStatus.add(username);
	                    }
	                }
	            } else {
	                // User has a logged_in status of 0, so we can skip them for inactivity check
	            }
	        }

	        // Update the status for users who have exceeded 5 minutes of inactivity
	        if (!usersToUpdateStatus.isEmpty()) {
	            String updateSql = "UPDATE user_details SET logged_in = 0 WHERE username IN (";
	            for (int i = 0; i < usersToUpdateStatus.size(); i++) {
	                updateSql += "?";
	                if (i < usersToUpdateStatus.size() - 1) {
	                    updateSql += ",";
	                }
	            }
	            updateSql += ")";

	            PreparedStatement updateStatement = connection.prepareStatement(updateSql);
	            for (int i = 0; i < usersToUpdateStatus.size(); i++) {
	                updateStatement.setString(i + 1, usersToUpdateStatus.get(i));
	            }
	            updateStatement.executeUpdate();
	        }
	    }

	    public void updateLastLoggedInDateAndStatus(String username, Timestamp loginTimestamp) throws SQLException {
	        String sql = "UPDATE user_details SET last_logged_in_date = ?, logged_in = ? WHERE username = ?";
	        PreparedStatement statement = connection.prepareStatement(sql);
	        statement.setTimestamp(1, loginTimestamp);
	        statement.setInt(2, 1); // Set logged_in status to 1 (logged in)
	        statement.setString(3, username);
	        statement.executeUpdate();
	    }
	    
	    public void updateLastLoggedInDate(String username) throws SQLException {
	        LocalDateTime currentDateTime = LocalDateTime.now();
	        Timestamp loginTimestamp = Timestamp.valueOf(currentDateTime);

	        String sql = "UPDATE user_details SET last_logged_in_date = ? WHERE username = ?";
	        PreparedStatement statement = connection.prepareStatement(sql);
	        statement.setTimestamp(1, loginTimestamp);
	        statement.setString(2, username);
	        statement.executeUpdate();
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
		
		public void createUserExecuteQuery(String username, String passwordHash, String params, String email, String role, String dateString)
				  throws SQLException {
					String sql = "INSERT INTO user_details (username, password_hash, password_params, email, role, user_creation_date, password_last_modified) VALUES (?, ?, ?, ?, ?, ?, ?)";
					PreparedStatement statement = connection.prepareStatement(sql);
					statement.setString(1, username);
					statement.setString(2, passwordHash);
					statement.setString(3, params);
					statement.setString(4, email);
					statement.setString(5, role);
					statement.setString(6, dateString);
					statement.setString(7, dateString);
					statement.executeUpdate();
					
				}
		
		public void changePasswordExecuteQuery(String username, String passwordHash, String params, String dateString)
				  throws SQLException {
					String sql = "UPDATE user_details SET password_hash = ?, password_params = ?, password_last_modified = ? WHERE username = ?";
					PreparedStatement statement = connection.prepareStatement(sql);
					statement.setString(1, passwordHash);
					statement.setString(2, params);
					statement.setString(3, dateString);
					statement.setString(4, username);
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
		
		public ResultSet QueryReturnResultsFromPatientName(String patientName) throws SQLException {
		    String sql = "SELECT * FROM testdb.test3 WHERE firstName LIKE ? AND lastName LIKE ?";
		    PreparedStatement statement = connection.prepareStatement(sql);
		    String[] name = patientName.split(" ");

		    String fName = "";
		    String lName = "";

		    if (name.length >= 2) {
		        fName = name[0];
		        lName = name[1];
		    } else if (name.length == 1) {
		        fName = name[0];
		        lName = ""; // Set last name as empty if not provided
		    } else {
		        throw new IllegalArgumentException("Invalid patient name format");
		    }

		    statement.setString(1, "%" + fName + "%");
		    statement.setString(2, "%" + lName + "%");
		    System.out.println(statement);

		    return statement.executeQuery();
		}

		
		public void addCalendarEvent(String nextTitle, String nextId, Boolean nextFullDay, LocalDate nextStartDate, 
				LocalDate nextEndDate, LocalTime nextStartTime,	LocalTime nextEndTime, ZoneId nextZoneId,
				Boolean nextRecurring, String nextRRule, Boolean nextRecurrence) throws SQLException {
			String sql = "INSERT INTO doctor_calendar (Title, Id, FullDay, StartDate, EndDate, StartTime, EndTime, ZoneId, Recurring, "
					+ "RRule, Recurrence) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, nextTitle);
			statement.setString(2, nextId);
			statement.setString(3, nextFullDay.toString());
			statement.setString(4, nextStartDate.toString());
			statement.setString(5, nextEndDate.toString());
			statement.setString(6, nextStartTime.toString());
			statement.setString(7, nextEndTime.toString());
			statement.setString(8, nextZoneId.toString());
			statement.setString(9, nextRecurring.toString());
			statement.setString(10, nextRRule);
			statement.setString(11, nextRecurrence.toString());
			statement.executeUpdate();
		}
		
		public void getCalendarEvents() throws SQLException {
			// grabs all the rows from the doctor_calendar DB
			String query = "SELECT * FROM testdb.doctor_calendar";
			ResultSet rs = this.executeQueryReturnResults(query);
			System.out.println(rs.getRow());
			while (rs.next()) {
				nextTitle = rs.getString("Title");
				nextId = rs.getString("Id");
				nextFullDay = Boolean.valueOf(rs.getString("FullDay"));
				nextStartDate = LocalDate.parse(rs.getString("StartDate"));
				nextEndDate = LocalDate.parse(rs.getString("EndDate"));
				nextStartTime = LocalTime.parse(rs.getString("StartTime"));
				nextEndTime = LocalTime.parse(rs.getString("StartTime"));
				nextZoneId = ZoneId.of(rs.getString("ZoneId"));
				nextRecurring = Boolean.valueOf(rs.getString("Recurring"));
				nextRRule = rs.getString("RRule");
				nextRecurrence = Boolean.valueOf(rs.getString("Recurrence"));
				
				System.out.println("Entry from loop: " + nextTitle + ", " + nextId + ", " 
				+ nextFullDay + ", " + nextStartDate + ", " + nextEndDate + ", "
				+ nextStartTime + ", " + nextEndTime + "' " + nextZoneId + ", "
				+ nextRecurring + ", " + nextRRule + ", " + nextRecurrence);	
				
				// need to add amap list
				Map<LocalDate, List<Entry<?>>> newMapEntry = null;
				Entry newEntry = new Entry<Object>();
				newEntry.setTitle(nextTitle);
				newEntry.setId(nextId);
				newEntry.setFullDay(nextFullDay);
				newEntry.changeStartDate(nextStartDate);
				newEntry.changeEndDate(nextEndDate);
				newEntry.changeStartTime(nextStartTime);
				newEntry.changeEndTime(nextEndTime);
				newEntry.changeZoneId(nextZoneId);
				newEntry.setRecurrenceRule(nextRRule);
				
				System.out.println("New Entry: " + newEntry);
				 // add the new entry to the doctor calendar
				CalendarApp.getDoctors().addEntry(newEntry);
			} 
			
		}
		
}
