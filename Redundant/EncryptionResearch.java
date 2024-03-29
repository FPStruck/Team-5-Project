package application;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.util.Scanner;

/*
 * AES technique 
 */

public class EncryptionResearch {
    
    public static void main(String[] args) throws Exception {
        // Get user input
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter text to encrypt: ");
        String plainText = scanner.nextLine();

        // Generate a random 256-bit key for AES encryption
        KeyGenerator keyGeneratorAES = KeyGenerator.getInstance("AES");
        keyGeneratorAES.init(256);
        SecretKey secretKeyAES = keyGeneratorAES.generateKey();

        // Encrypt the user input with AES
        Cipher cipherAES = Cipher.getInstance("AES");
        cipherAES.init(Cipher.ENCRYPT_MODE, secretKeyAES); // initialize with the operation mode/code and key
        byte[] encryptedTextAES = cipherAES.doFinal(plainText.getBytes()); // sends the message in bytes to be encrypted 

        // Decrypt the AES encrypted text
        cipherAES.init(Cipher.DECRYPT_MODE, secretKeyAES); // initialize with the operation mode/code and key
        byte[] decryptedTextAES = cipherAES.doFinal(encryptedTextAES); // decrypts the encrypted text which was encrypted with AES

        // Print the decrypted AES text
        System.out.println("Decrypted AES text: " + new String(decryptedTextAES));

        // Same technique as above however with RSA
        // Generate an RSA key pair
        KeyPairGenerator keyPairGeneratorRSA = KeyPairGenerator.getInstance("RSA");
        keyPairGeneratorRSA.initialize(2048);
        KeyPair keyPairRSA = keyPairGeneratorRSA.generateKeyPair();
        PublicKey publicKeyRSA = keyPairRSA.getPublic();

        // Encrypt the AES key with RSA
        Cipher cipherRSA = Cipher.getInstance("RSA");
        cipherRSA.init(Cipher.ENCRYPT_MODE, publicKeyRSA);
        byte[] encryptedKeyRSA = cipherRSA.doFinal(secretKeyAES.getEncoded()); // we use encoded format for the secret key
        
        // Decrypt the AES key with RSA
        PrivateKey privateKeyRSA = keyPairRSA.getPrivate();
        cipherRSA.init(Cipher.DECRYPT_MODE, privateKeyRSA);
        byte[] decryptedKeyRSA = cipherRSA.doFinal(encryptedKeyRSA); // decrypts the encrypted secret key
        SecretKey originalKeyAES = new SecretKeySpec(decryptedKeyRSA, 0, decryptedKeyRSA.length, "AES"); // creates a new key with using AES
        

        // Encrypt the user input with RSA-encrypted AES key
        Cipher cipherRSA_AES = Cipher.getInstance("AES");
        cipherRSA_AES.init(Cipher.ENCRYPT_MODE, originalKeyAES); // encrypt with he new key created by the secret key
        byte[] encryptedTextRSA = cipherRSA_AES.doFinal(plainText.getBytes()); // encrypt the plain text

        // Decrypt the RSA-encrypted AES encrypted text
        cipherRSA_AES.init(Cipher.DECRYPT_MODE, originalKeyAES);
        byte[] decryptedTextRSA = cipherRSA_AES.doFinal(encryptedTextRSA); // decrypt the plain text using the key created by the secrete key into an array

        // Print the decrypted RSA-encrypted AES text
        System.out.println("Decrypted RSA-encrypted AES text: " + new String(decryptedTextRSA));
        
        // same technique as above using blow fish
        // Generate a random 256-bit key for Blowfish encryption
        KeyGenerator keyGeneratorBlowfish = KeyGenerator.getInstance("Blowfish");
        keyGeneratorBlowfish.init(256);
        SecretKey secretKeyBlowfish = keyGeneratorBlowfish.generateKey();

        // Encrypt the user input with Blowfish
        Cipher cipherBlowfish = Cipher.getInstance("Blowfish");
        cipherBlowfish.init(Cipher.ENCRYPT_MODE, secretKeyBlowfish);
        byte[] encryptedTextBlowfish = cipherBlowfish.doFinal(plainText.getBytes());

        // Decrypt the Blowfish encrypted text
        cipherBlowfish.init(Cipher.DECRYPT_MODE, secretKeyBlowfish);
        byte[] decryptedTextBlowfish = cipherBlowfish.doFinal(encryptedTextBlowfish);
        
        // Print the decrypted Blowfish text
        System.out.println("Decrypted Blowfish text: " + new String(decryptedTextBlowfish));
    
    }
}