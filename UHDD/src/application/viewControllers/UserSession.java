package application.viewControllers;

public class UserSession {

    private static UserSession instance;

    private String userName;
    private String email; // added email field

    private UserSession(String userName, String email) { // added email parameter
        this.userName = userName;
        this.email = email;
    }

    public static void initInstance(String userName, String email) { // added email parameter
        if(instance == null) {
            instance = new UserSession(userName, email);
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

    public String getEmail() { // added getEmail method
        return email;
    }

    public void cleanUserSession() {
        userName = ""; // or null
        email = ""; // or null
        instance = null;
    }

}
