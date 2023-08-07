package application;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CreateEncryptedPdf {

    

    
    public static void create(String patientId,String finalPath, String ownerPassword, String userPassword) throws ClassNotFoundException, SQLException {
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
                document.add(new Paragraph("First Name: " + resultSet.getString("firstName")));
                
                document.add(new Paragraph("Middle Name: " + resultSet.getString("middleName")));
                document.add(new Paragraph("Last Name: " + resultSet.getString("lastName")));
                
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
}
