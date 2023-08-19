//UHDB@lh3b.onmicrosoft.com
//Nap00764
package application;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.imageio.ImageIO;
import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.util.ByteArrayDataSource;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Properties;

public class EmailManager {

    private  String smtpHost = "smtp.office365.com";
    private  String smtpPort = "587";
    private  String smtpUser = "UHDB@lh3b.onmicrosoft.com";
    private  String smtpPassword = "Nap00764";

    public void sendEmailWithImage(String to, String subject, String body, BufferedImage image) throws MessagingException, IOException {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", smtpHost);
        properties.put("mail.smtp.port", smtpPort);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(smtpUser, smtpPassword);
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(smtpUser));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        message.setSubject(subject);

        MimeMultipart multipart = new MimeMultipart();

        // Add text part
        MimeBodyPart textPart = new MimeBodyPart();
        textPart.setText(body);
        multipart.addBodyPart(textPart);

        // Add image part
        MimeBodyPart imagePart = new MimeBodyPart();
        DataSource dataSource = new ByteArrayDataSource(convertBufferedImageToByteArray(image, "png"), "image/png");
        imagePart.setDataHandler(new DataHandler(dataSource));
        imagePart.setFileName("qrcode.png");
        imagePart.setHeader("Content-ID", "<qrcode>");
        multipart.addBodyPart(imagePart);

        message.setContent(multipart);

        Transport.send(message);
    }

    private byte[] convertBufferedImageToByteArray(BufferedImage image, String format) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, format, baos);
        return baos.toByteArray();
    }
}
