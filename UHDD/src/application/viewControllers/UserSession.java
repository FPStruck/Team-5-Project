package application.viewControllers;

public class UserSession {

    private static UserSession instance;

    private String userName;
    private String role; // Added role member variable
    private String id;      // Added id member variable

    private UserSession(String userName, String role, String id) { // Modified constructor
        this.userName = userName;
        this.role = role;
        this.id = id;
    }

    public static void initInstance(String userName, String role, String id) { // Modified initInstance method
        if(instance == null) {
            instance = new UserSession(userName, role, id);
        }
    }

    public static UserSession getInstance() {
        if (instance == null) {
            throw new IllegalStateException("UserSession has not been initialized.");
        }
        return instance;
    }

    public String getUserName() {
        return userName;
    }

    public String getRole() { // Getter for role
        return role;
    }

    public String getId() { // Getter for id
        return id;
    }

    public void cleanUserSession() {
        userName = ""; // or null
        role = "";     // or null
        id = "";        // or any default value
        instance = null;
    }
}
