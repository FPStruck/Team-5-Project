package application;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Patient {
    private final SimpleIntegerProperty id;
    private final SimpleStringProperty familyName;
    private final SimpleStringProperty givenName;
    private final SimpleStringProperty middleName;
    private final SimpleStringProperty gender;
    private final SimpleStringProperty address;
    private final SimpleStringProperty city;
    private final SimpleStringProperty state;
    private final SimpleStringProperty telephone;
    private final SimpleStringProperty email;
    private final SimpleStringProperty dateOfBirth;
    private final SimpleStringProperty healthInsuranceNumber;
    private final SimpleStringProperty emergencyContactNumber;

    public Patient(int id, String familyName, String givenName) {
        this.id = new SimpleIntegerProperty(id);
        this.familyName = new SimpleStringProperty(familyName);
        this.givenName = new SimpleStringProperty(givenName);
        this.middleName = new SimpleStringProperty("");
        this.gender = new SimpleStringProperty("");
        this.address = new SimpleStringProperty("");
        this.city = new SimpleStringProperty("");
        this.state = new SimpleStringProperty("");
        this.telephone = new SimpleStringProperty("");
        this.email = new SimpleStringProperty("");
        this.dateOfBirth = new SimpleStringProperty("");
        this.healthInsuranceNumber = new SimpleStringProperty("");
        this.emergencyContactNumber = new SimpleStringProperty("");
    }

    public Patient(int id, String familyName, String givenName, String middleName, String gender, String address, String city, String state, String telephone, String email, String dateOfBirth, String healthInsuranceNumber, String emergencyContactNumber) {
        this.id = new SimpleIntegerProperty(id);
        this.familyName = new SimpleStringProperty(familyName);
        this.givenName = new SimpleStringProperty(givenName);
        this.middleName = new SimpleStringProperty(middleName);
        this.gender = new SimpleStringProperty(gender);
        this.address = new SimpleStringProperty(address);
        this.city = new SimpleStringProperty(city);
        this.state = new SimpleStringProperty(state);
        this.telephone = new SimpleStringProperty(telephone);
        this.email = new SimpleStringProperty(email);
        this.dateOfBirth = new SimpleStringProperty(dateOfBirth);
        this.healthInsuranceNumber = new SimpleStringProperty(healthInsuranceNumber);
        this.emergencyContactNumber = new SimpleStringProperty(emergencyContactNumber);
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

    public String getMiddleName() {
        return middleName.get();
    }

    public void setMiddleName(String middleName) {
        this.middleName.set(middleName);
    }

    public String getGender() {
        return gender.get();
    }

    public void setGender(String gender) {
        this.gender.set(gender);
    }

    public String getAddress() {
        return address.get();
    }

    public void setAddress(String address) {
        this.address.set(address);
    }

    public String getCity() {
        return city.get();
    }

    public void setCity(String city) {
        this.city.set(city);
    }

    public String getState() {
        return state.get();
    }

    public void setState(String state) {
        this.state.set(state);
    }

    public String getTelephone() {
        return telephone.get();
    }

    public void setTelephone(String telephone) {
        this.telephone.set(telephone);
    }

    public String getEmail() {
        return email.get();
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public String getDateOfBirth() {
        return dateOfBirth.get();
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth.set(dateOfBirth);
    }

    public String getHealthInsuranceNumber() {
        return healthInsuranceNumber.get();
    }

    public void setHealthInsuranceNumber(String healthInsuranceNumber) {
        this.healthInsuranceNumber.set(healthInsuranceNumber);
    }

    public String getEmergencyContactNumber() {
        return emergencyContactNumber.get();
    }

    public void setEmergencyContactNumber(String emergencyContactNumber) {
        this.emergencyContactNumber.set(emergencyContactNumber);
    }
}

