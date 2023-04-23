package application;

import java.io.*;
import java.security.*;
import javax.crypto.*;
import java.security.spec.*;
import javax.crypto.spec.*;

public class FileEncryptionMETHOD1 {
    private static final String ALGORITHM = "RSA";
    private static final String TRANSFORMATION = "RSA/ECB/PKCS1Padding";
    private static final String PRIVATE_KEY_FILE = "private.key";
    private static final String PUBLIC_KEY_FILE = "public.key";
    private static final int KEY_SIZE = 2048;
    
    private KeyPair generateKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance(ALGORITHM);
        keyGen.initialize(KEY_SIZE);
        return keyGen.generateKeyPair();
    }
    
    private void writeToFile(String fileName, byte[] data) throws IOException {
        FileOutputStream fos = new FileOutputStream(fileName);
        fos.write(data);
        fos.flush();
        fos.close();
    }
    
    private byte[] readFromFile(String fileName) throws IOException {
        FileInputStream fis = new FileInputStream(fileName);
        byte[] data = new byte[fis.available()];
        fis.read(data);
        fis.close();
        return data;
    }
    
    private byte[] encrypt(byte[] data, PublicKey key) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(data);
    }
    
    private byte[] decrypt(byte[] data, PrivateKey key) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.DECRYPT_MODE, key);
        return cipher.doFinal(data);
    }
    
    public void encryptFile(String fileName) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, IOException, InvalidKeySpecException {
        KeyPair keyPair = generateKeyPair();
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();
        File dir = new File(System.getProperty("user.home") + "/Documents/encrypted_files"); //////FILE IS STORED HERE///////////////
        if (!dir.exists()) {
            dir.mkdir();
        }
        
        writeToFile(PUBLIC_KEY_FILE, publicKey.getEncoded());
        writeToFile(PRIVATE_KEY_FILE, privateKey.getEncoded());
        
        byte[] fileData = readFromFile(fileName);
        byte[] encryptedData = encrypt(fileData, publicKey);
        writeToFile(fileName + ".enc", encryptedData);
    }
    
    public void decryptFile(String fileName) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, IOException, InvalidKeySpecException {
        byte[] privateKeyData = readFromFile(PRIVATE_KEY_FILE);
        byte[] encryptedData = readFromFile(fileName);
        PrivateKey privateKey = KeyFactory.getInstance(ALGORITHM).generatePrivate(new PKCS8EncodedKeySpec(privateKeyData));
        byte[] decryptedData = decrypt(encryptedData, privateKey);
        System.out.println(new String(decryptedData));
    }
    
    public static void main(String[] args) {
        try {
        	FileEncryptionMETHOD1 fileEncryption = new FileEncryptionMETHOD1();
            String fileName = "data.txt";
            
            // Write data to file
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Enter data to write to file: ");
            String data = reader.readLine();
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
            writer.write(data);
            writer.close();
            
            // Encrypt file
            fileEncryption.encryptFile(fileName);
            
            // Decrypt file and print data
            fileEncryption.decryptFile(fileName + ".enc");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}