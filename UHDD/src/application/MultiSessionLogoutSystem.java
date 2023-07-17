package application;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class MultiSessionLogoutSystem {
    private Map<String, Boolean> activeSessions;
    private DBConnector dbConnector;

    public MultiSessionLogoutSystem(DBConnector dbConnector) {
        activeSessions = new HashMap<>();
        this.dbConnector = dbConnector;
    }

    public boolean login(String username) throws SQLException {
        // Check if the user already has an active session
        if (activeSessions.containsKey(username)) {
            // Logout all active sessions for the user
            logoutAllSessions(username);
            return false;
        }

        // Create a new session and add it to the active sessions
        activeSessions.put(username, true);
        return true;
    }

    public void logout(String username) throws SQLException {
        // Check if the user has an active session
        if (activeSessions.containsKey(username)) {
            // Remove the session from the active sessions
            activeSessions.remove(username);
            // Perform logout operation in the database
            dbConnector.logout(username);
        }
    }

    public void logoutAllSessions(String username) throws SQLException {
        // Check if the user has any active sessions
        if (activeSessions.containsKey(username)) {
            // Remove all active sessions for the user
            activeSessions.remove(username);
            // Perform logout operation in the database
            dbConnector.logoutAllSessions(username);
        }
    }
}
