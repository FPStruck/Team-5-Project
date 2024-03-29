package application;


import java.util.*;  
import javax.mail.*;  
import javax.mail.internet.*;  
import javax.activation.*;  
  
public class AuthenticatorEMAIL
{  
 public static void main(String [] args){  
	 System.out.print("Input email to send verification code: ");
     Scanner in  = new Scanner(System.in); 
     String to = in.next();
      String from = "Verifier";
      String host = "smtp.gmail.com";
      int port = 587;
      String username = "jonoleo@gmail.com";
      String password = "xxx";
      
      // Set email properties
      Properties properties = System.getProperties();  
//      properties.setProperty("mail.smtp.host", host);
//      properties.setProperty("mail.smtp.port", String.valueOf(port));
//      properties.setProperty("mail.smtp.starttls.enable", "true");
//      properties.setProperty("mail.smtp.auth", "true");
      
//      properties.put("mail.smtp.starttls.required", "true");
//      properties.put("mail.smtp.ssl.protocols", "TLSv1.2");
//      properties.setProperty("mail.smtp.ssl.protocols", "TLSv1.2"); 
//      properties.setProperty("mail.smtp.ssl.ciphersuites", "TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA256");
      
      properties.put("mail.smtp.host", "smtp.gmail.com");
      properties.put("mail.smtp.socketFactory.port", "587");
      properties.put("mail.smtp.socketFactory.class", "javax.net.SocketFactory");
      properties.put("mail.smtp.auth", "true");
      properties.put("mail.smtp.port", "587");
      properties.put("mail.smtp.ssl.enable", "false");
      properties.put("mail.smtp.starttls.enable", "true");
      properties.put("mail.smtp.ssl.trust", "smtp.gmail.com");

      // Get the email session object  
      Session session = Session.getDefaultInstance(properties,  
          new javax.mail.Authenticator() {  
              protected PasswordAuthentication getPasswordAuthentication() {  
                  return new PasswordAuthentication(username, password);  
              }  
          });  

      // Compose the message  
      try {  
          MimeMessage message = new MimeMessage(session);  
          message.setFrom(new InternetAddress(from));  
          message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));  
          message.setSubject("Verification Code");  
          int code = (int) (Math.random() * 1000000); // Generate a random 6-digit code
          int expectedCode = code; // Change this to the expected verification code
          message.setText("Your verification code is: " + code);  

          // Send the message  
          Transport transport = session.getTransport("smtp");
          transport.connect(host, username, password);
          transport.sendMessage(message, message.getAllRecipients());
          transport.close();
          System.out.println("Verification code sent to " + to);  
          
          // Prompt the user to enter the verification code
          Scanner scanner = new Scanner(System.in);
          System.out.print("Enter the verification code: ");
          int inputCode = scanner.nextInt();
          
          // Check if the input code matches the expected value
          if (inputCode == expectedCode) {
              System.out.println("Verification successful!");
          } else {
              System.out.println("Verification failed!");
          }
      } catch (MessagingException mex) {
          mex.printStackTrace();
      }
 }
} 