package application.viewControllers;

public class UserSession {

    private static UserSession instance;

    private String userName;

    private UserSession(String userName) {
        this.userName = userName;
    }

    public static void initInstance(String userName) {
        if(instance == null) {
            instance = new UserSession(userName);
        }
    }

    public static UserSession getInstance() {
        return instance;
    }

    public String getUserName() {
        return userName;
    }

    public void cleanUserSession() {
        userName = "";// or null
        instance = null;
    }

}


