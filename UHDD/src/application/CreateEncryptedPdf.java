package application;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.ResultSet;

public class CreateEncryptedPdf {
    public static void create(String filePath, String userPassword, String ownerPassword) {
    try {
        // Fetch data from database
        DBConnector dbConnector = new DBConnector();
        dbConnector.initialiseDB();
        ResultSet rs = dbConnector.QueryReturnResultsFromPatients();

        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filePath));
        document.open();

        // Define table column headers
        String[] headers = new String[]{"Patient ID", "First Name", "Middle Name", "Last Name",
                "Gender", "Address", "City", "State", "Telephone", "Email",
                "Date of Birth", "Health Insurance Number", "Emergency Contact Number"};

        PdfPTable table = new PdfPTable(headers.length);

        // Add column headers to table
        for (String header : headers) {
            PdfPCell cell = new PdfPCell();
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            cell.setBorderWidth(2);
            cell.setPhrase(new Phrase(header));
            table.addCell(cell);
        }

        // Fill the table with data from the database
        while (rs.next()) {
            table.addCell(rs.getString("patientId"));
            table.addCell(rs.getString("firstName"));
            table.addCell(rs.getString("middleName"));
            table.addCell(rs.getString("lastName"));
            table.addCell(rs.getString("gender"));
            table.addCell(rs.getString("address"));
            table.addCell(rs.getString("city"));
            table.addCell(rs.getString("state"));
            table.addCell(rs.getString("telephone"));
            table.addCell(rs.getString("email"));
            table.addCell(rs.getString("dateOfBirth"));
            table.addCell(rs.getString("healthInsuranceNumber"));
            table.addCell(rs.getString("emergencyContactNumber"));
        }

        document.add(table);
        document.close();
        dbConnector.closeConnection();
        // Encrypt the PDF
        PdfReader reader = new PdfReader(filePath);
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(filePath));
        stamper.setEncryption(userPassword.getBytes(), ownerPassword.getBytes(),
                PdfWriter.ALLOW_PRINTING, PdfWriter.ENCRYPTION_AES_128 | PdfWriter.DO_NOT_ENCRYPT_METADATA);
        stamper.close();
        reader.close();

    } catch (Exception e) {
        e.printStackTrace();
    }
}
}

