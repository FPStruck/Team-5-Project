package application;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Scanner;

public class PassHashAndSaltMETHOD2 {

    public static void main(String[] args) throws NoSuchAlgorithmException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter a message to hash and salt: ");
        String message = scanner.nextLine();
        
        // Hashing the message
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        byte[] hash = md.digest(message.getBytes());
        System.out.println("Hashed message: (512) " + bytesToHex(hash));
        
        MessageDigest md2 = MessageDigest.getInstance("SHA-256");
        byte[] hash2 = md2.digest(message.getBytes());
        System.out.println("Hashed message: (256) " + bytesToHex(hash2));
        
        // Generating a salt
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        
        // Combining the message and salt and hashing again
        md2.reset();
        md2.update(salt);
        byte[] saltedHash2 = md.digest(message.getBytes());
        System.out.println("Salted and hashed message: (256) " + bytesToHex(saltedHash2));
        
        md.reset();
        md.update(salt);
        byte[] saltedHash = md.digest(message.getBytes());
        System.out.println("Salted and hashed message: (512) " + bytesToHex(saltedHash));
        
        // Prompting the user to enter a message to check against the hashed message
        System.out.print("Enter a message to check against the hashed message: ");
        String checkMessage = scanner.nextLine();
        byte[] checkHash = md.digest(checkMessage.getBytes());
        
        // Checking if the hashed check message matches the original hashed message
        if (MessageDigest.isEqual(hash, checkHash)) {
            System.out.println("The checked message matches the original hashed message.");
        } else {
            System.out.println("The checked message does not match the original hashed message.");
        }
       
    }
    
    public static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

}