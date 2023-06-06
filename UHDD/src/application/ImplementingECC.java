package application;

import java.io.FileOutputStream;
import java.io.PrintWriter;

import java.nio.charset.StandardCharsets;
import java.security.PublicKey;
import java.security.PrivateKey;
import java.security.Security;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.KeyPair;
import java.security.SecureRandom;
import java.util.Base64;
import java.security.spec.ECGenParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.KeyAgreement;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.Arrays;

public class ImplementingECC {

	public static void main(String[] args) throws Exception {

	
	Security.addProvider(new BouncyCastleProvider());
	
	KeyPairGenerator gen = KeyPairGenerator.getInstance("ECDH", "BC");
	ECGenParameterSpec spec = new ECGenParameterSpec("secp256r1");
	gen.initialize(spec, new SecureRandom());
	
	KeyPair pair1 = gen.genKeyPair();
	KeyPair pair2 = gen.genKeyPair();
	
	PrivateKey partyAPrivKey = pair1.getPrivate();
	PublicKey partyAPubKey = pair1.getPublic();
	
	PrivateKey partyBPrivKey = pair2.getPrivate();
	PublicKey partyBPubKey = pair2.getPublic();
		
	

	
	// 1. Generate the pre-master shared secret
	KeyAgreement ka = KeyAgreement.getInstance("ECDH", "BC");
	ka.init(partyBPrivKey);
	ka.doPhase(partyBPubKey, true);
	byte[] sharedSecret = ka.generateSecret();

	// 2. (Optional) Hash the shared secret.
//	 		Alternatively, you don't need to hash it.
	MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
	messageDigest.update(sharedSecret);
	byte[] digest = messageDigest.digest();

	// 3. (Optional) Split up hashed shared secret into an initialization vector and a session key
//	 		Alternatively, you can just use the shared secret as the session key and not use an iv.
	int digestLength = digest.length;
	byte[] iv = Arrays.copyOfRange(digest, 0, (digestLength + 1)/2);
	byte[] sessionKey = Arrays.copyOfRange(digest, (digestLength + 1)/2, digestLength);

	// 4. Create a secret key from the session key and initialize a cipher with the secret key
	SecretKey secretKey = new SecretKeySpec(sessionKey, 0, sessionKey.length, "AES");
	Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding", "BC");
	IvParameterSpec ivSpec = new IvParameterSpec(iv);
	cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);

	// 5. Encrypt whatever message you want to send
	String encryptMe = "Hello world!";
	byte[] encryptMeBytes = encryptMe.getBytes(StandardCharsets.UTF_8);
	byte[] cipherText = cipher.doFinal(encryptMeBytes);
	String cipherString = Base64.getEncoder().encodeToString(cipherText);
	
	// Same stuff as before...
	cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);

	// 6. Decrypt the message that was sent
	String decryptMe = cipherString; // Message received from Party A
	byte[] decryptMeBytes = Base64.getDecoder().decode(decryptMe);
	byte[] textBytes = cipher.doFinal(decryptMeBytes);
	String originalText = new String(textBytes);
	
//	// 7. Saving the key for use in another file
//	try (PrintWriter out = new PrintWriter("key_Pub.txt")) {
//	    out.println(partyBPubKey);
////	}
//	try (PrintWriter out = new PrintWriter("key_Priv")) {
//	    out.println(partyAPrivKey);
//	}
//	// Saving the encrypted file
//	try (PrintWriter out = new PrintWriter("Secret.txt")) {
//	    out.println(decryptMe);
//	}

	System.out.println(secretKey);
	System.out.println(ivSpec);
	System.out.println(originalText);
	
	}	
}