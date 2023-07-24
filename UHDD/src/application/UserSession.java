package application;

public class UserSession {
    private String userId;

    public UserSession(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }
}
