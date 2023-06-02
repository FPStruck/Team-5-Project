package application;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class EncryptionController {
	private static final String SALT_PEPPER = "182253648768";
	
	public String hashData(String data) {
	    try {
	        // Hash the data using SHA-256
	        MessageDigest md = MessageDigest.getInstance("SHA-256");
	        byte[] hash = md.digest(data.getBytes());
	        
	        // Encode the hash as a Base64 string and return it
	        return Base64.getEncoder().encodeToString(hash);
	    } catch (NoSuchAlgorithmException e) {
	        e.printStackTrace();
	        return null;
	    }
	}

}