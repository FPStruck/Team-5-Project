package application;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Patient {
    private final SimpleIntegerProperty id;
    private final SimpleStringProperty familyName;
    private final SimpleStringProperty givenName;

    public Patient(int id, String familyName, String givenName) {
        this.id = new SimpleIntegerProperty(id);
        this.familyName = new SimpleStringProperty(familyName);
        this.givenName = new SimpleStringProperty(givenName);
    }

    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getFamilyName() {
        return familyName.get();
    }

    public void setFamilyName(String familyName) {
        this.familyName.set(familyName);
    }

    public String getGivenName() {
        return givenName.get();
    }

    public void setGivenName(String givenName) {
        this.givenName.set(givenName);
    }
}
