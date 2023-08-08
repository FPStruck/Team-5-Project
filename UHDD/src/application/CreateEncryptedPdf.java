package application;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Header;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CreateEncryptedPdf {

    

    
    public static void createPatientDetailsPdf(String patientId,String finalPath, String ownerPassword, String userPassword) throws ClassNotFoundException, SQLException {
        DBConnector dbconnector = new DBConnector();
        dbconnector.initialiseDB();
        Document document = new Document();
        try {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(finalPath));
            
            // Encrypt the PDF as it's being written
            writer.setEncryption(userPassword.getBytes(), ownerPassword.getBytes(), PdfWriter.ALLOW_PRINTING, PdfWriter.ENCRYPTION_AES_128);
            
            document.open();
            
            // Fetch patient data
            ResultSet resultSet = dbconnector.QueryReturnResultsFromPatientDataId(patientId);
            if (resultSet.next()) {
                document.add(new Paragraph("Patient Id: " + resultSet.getInt("patientId")));
                document.add(new Paragraph("Gender: " + resultSet.getString("gender")));
                document.add(new Paragraph("First Name: " + resultSet.getString("firstName")));
                document.add(new Paragraph("Middle Name: " + resultSet.getString("middleName")));
                document.add(new Paragraph("Last Name: " + resultSet.getString("lastName")));
                document.add(new Paragraph("Date of Birth: " + resultSet.getString("dateOfBirth")));
                document.add(new Paragraph("Address: " + resultSet.getString("address")));
                document.add(new Paragraph("City: " + resultSet.getString("city")));
                document.add(new Paragraph("State: " + resultSet.getString("state")));
                document.add(new Paragraph("Tele: " + resultSet.getString("telephone")));
                document.add(new Paragraph("Email: " + resultSet.getString("email")));
                
            } else {
                document.add(new Paragraph("No data found for patient ID: " + patientId));
            }

            document.close();
            writer.close();
            dbconnector.closeConnection();
            
        } catch (DocumentException | SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    public static void createMedicationPdf(String patientId, String finalPath, String ownerPassword, String userPassword) throws ClassNotFoundException, SQLException{
        DBConnector dbconnector = new DBConnector();
        dbconnector.initialiseDB();
        Document document = new Document();
        try {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(finalPath));
            
            // Encrypt the PDF as it's being written
            writer.setEncryption(userPassword.getBytes(), ownerPassword.getBytes(), PdfWriter.ALLOW_PRINTING, PdfWriter.ENCRYPTION_AES_128);
            
            document.open();

            // Fetch patient data
            ResultSet resultSet = dbconnector.QueryReturnResultsMedicationFromPatientId(patientId);
            while (resultSet.next()) {
                document.add(new Paragraph("Patient Id: " + resultSet.getInt("patientId")));
                document.add(new Paragraph("Medication Name: " + resultSet.getString("medication_name")));
                document.add(new Paragraph("Script Id: " + resultSet.getString("scriptId")));
                document.add(new Paragraph("Patient Id: " + resultSet.getString("patientId")));
                document.add(new Paragraph("Note Id: " + resultSet.getString("noteId")));
                document.add(new Paragraph("Doctor Id: " + resultSet.getString("prescribedBy")));
                document.add(new Paragraph("Date Prescribed: " + resultSet.getString("prescribed_date")));
                document.add(new Paragraph("Expiry Date: " + resultSet.getString("expired_date")));
                
            } 
            

            document.close();
            writer.close();
            dbconnector.closeConnection();
            
        } catch (DocumentException | SQLException | IOException e) {
            e.printStackTrace();
        }
    }
}
