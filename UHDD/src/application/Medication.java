package application;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import java.time.LocalDate;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class Medication {
    private final SimpleIntegerProperty scriptId;
    private final SimpleIntegerProperty patientId;
    private final SimpleStringProperty medicationName;
    private final ObjectProperty<LocalDate> prescribedDate;
    private final ObjectProperty<LocalDate> expiredDate;

    public Medication(int scriptId, int patientId, String medicationName, LocalDate prescribedDate, LocalDate expiredDate) {
        this.scriptId = new SimpleIntegerProperty(scriptId);
        this.patientId = new SimpleIntegerProperty(patientId);
        this.medicationName = new SimpleStringProperty(medicationName);
        this.prescribedDate = new SimpleObjectProperty<>(prescribedDate);
        this.expiredDate = new SimpleObjectProperty<>(expiredDate);
    }

    public int getScriptId() {
        return scriptId.get();
    }

    public void setScriptId(int scriptId) {
        this.scriptId.set(scriptId);
    }

    public int getPatientId() {
        return patientId.get();
    }

    public void setPatientId(int patientId) {
        this.patientId.set(patientId);
    }

    public String getMedicationName() {
        return medicationName.get();
    }

    public void setMedicationName(String medicationName) {
        this.medicationName.set(medicationName);
    }

    public LocalDate getPrescribedDate() {
        return prescribedDate.get();
    }

    public void setPrescribedDate(LocalDate prescribedDate) {
        this.prescribedDate.set(prescribedDate);
    }

    public LocalDate getExpiredDate() {
        return expiredDate.get();
    }

    public void setExpiredDate(LocalDate expiredDate) {
        this.expiredDate.set(expiredDate);
    }
}
