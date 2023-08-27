package application;

public class PatientService {
    private static PatientService instance = new PatientService();
    private Patient currentPatient;

    private PatientService() {}

    public static PatientService getInstance() {
        return instance;
    }

    public Patient getCurrentPatient() {
        return currentPatient;
    }

    public void setCurrentPatient(Patient currentPatient) {
        this.currentPatient = currentPatient;
    }
}
