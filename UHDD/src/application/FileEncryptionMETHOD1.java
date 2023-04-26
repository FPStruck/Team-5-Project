package application;
import java.io.*;
import java.util.Scanner;
import javax.crypto.*;
import javax.crypto.spec.*;

public class FileEncryptionMETHOD1 {

    public static void main(String[] args) {
        
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the text you want to encrypt: ");
        String plaintext = scanner.nextLine();
        
        try {
            // Create a file to store the encrypted data
            File file = new File("encrypted.txt");
            file.createNewFile();
            
            // Generate a random encryption key
            KeyGenerator keyGen = KeyGenerator.getInstance("AES");
            keyGen.init(128);
            SecretKey secretKey = keyGen.generateKey();
            
            // Initialize the encryption cipher
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            
            // Encrypt the plaintext and write it to the file
            byte[] iv = cipher.getIV();
            FileOutputStream fileOut = new FileOutputStream(file);
            fileOut.write(iv);
            CipherOutputStream cipherOut = new CipherOutputStream(fileOut, cipher);
            cipherOut.write(plaintext.getBytes());
            cipherOut.flush();
            cipherOut.close();
            fileOut.close();
            
            // Read the encrypted data from the file
            FileInputStream fileIn = new FileInputStream(file);
            byte[] fileData = new byte[(int) file.length()];
            fileIn.read(fileData);
            fileIn.close();
            
            // Decrypt the data using the same key and IV used for encryption
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(fileData, 0, 16));
            String decryptedText = new String(cipher.doFinal(fileData, 16, fileData.length-16));
            
            System.out.println("Encrypted file contents: " + fileData.toString());
            System.out.println("Decrypted file contents: " + decryptedText);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        scanner.close();
    }

}