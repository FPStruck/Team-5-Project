package application;

/*
 * The purpose of this code is to show that RSA encryption will work with a signature for Team 5 UDDD
 * The code will illustrate that by using techniques from Sanfoundry.com and geeksforgeeks.org
 * The team will be able to achieve a RSA class that will convert the string into a byte array 
 * Then encrypt the byte to a big integer by using the RSA technique of modular exponentiation
 * To decrypt the message we require a decryption key by using modular multiplicative inverse  
 * 
 * The signature will be created by using a signing algorithm which we will use a string
 * The RSA encryption will be used to create a Key pair
 * The Key pair generator is a built in function by java
 * The Signature and verify signature is also built in by java
 *  During Demo
 */
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.util.Scanner;

import javax.xml.bind.DatatypeConverter;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.Random;
 
public class RSA_DSA{
	// RSA declarations
    private BigInteger p; // a prime number to determine the modular number
    private BigInteger q; // another prime number to determine the modular number
    private BigInteger N; // the modular number (private and public key)
    private BigInteger phi; // phi is require to determine d (decryption key)
    private BigInteger e; // a prime number for the encryption key (part of the public key)
    private BigInteger d; // the decryption key(part of the private key)
    private int bitlength = 1024;
    private Random r; // a random number to determine the prime numbers
    static String teststring; // the user input text
    static byte[] encrypted; // after the text bytes get encrypted
    static byte[] decrypted ;
    static byte[] B2S ;
    static String RSAString; // for the signature however returning a null(maybe too long)
    
    // Signing declarations
 	private static final String	SIGNING_ALGORITHM = "SHA256withRSA";
 	private static final String RSA = "RSA"; 
 	static String input = "GEEKSFORGEEKS IS A"	+ " COMPUTER SCIENCE PORTAL"; // the message
 
    public RSA_DSA(){
        r = new Random();
        p = BigInteger.probablePrime(bitlength, r);
        q = BigInteger.probablePrime(bitlength, r);
        N = p.multiply(q);
        phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
        e = BigInteger.probablePrime(bitlength / 2, r); // e needs to be a prime that is half the bit length of p and q
        while (phi.gcd(e).compareTo(BigInteger.ONE) > 0 && e.compareTo(phi) < 0){ // gcd of e and phi needs to be more than 1; and e needs to be less than phi
            e.add(BigInteger.ONE); // increment e
        }
        d = e.modInverse(phi); // the formula to inverse e modular phi
        System.out.println("D is : " + d); // the decryption key
        
    }
 
    public RSA_DSA(BigInteger e, BigInteger d, BigInteger N){ // constructor
        this.e = e;
        this.d = d;
        this.N = N;
    }
 
    @SuppressWarnings("deprecation")
    public static void main(String[] args) throws Exception{
        RSA_DSA rsa = new RSA_DSA(); // create a new object
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
     
        System.out.println("Enter the plain text:");
        teststring = in.readLine();
        System.out.println("Encrypting String: " + teststring); // for testing
        System.out.println("String getBytes: " + teststring.getBytes()); // check the byte value is working
        System.out.println("String in Bytes: " + bytesToString(teststring.getBytes())); // check the byte value is working
        
        // encrypt
        encrypted = rsa.encrypt(teststring.getBytes());
        System.out.println("This is after the encryption(signature): " + bytesToString(encrypted)); // check the encryption as a string
        
        // create RSA string for the signature (needs more work)
        RSAString = bytesToString(encrypted);
        System.out.println(RSAString);
        
        KeyPair keyPair	= Generate_RSA_KeyPair(); // create a key pair
        
        // create a signature
        byte[] signature = Create_Digital_Signature(input.getBytes(), keyPair.getPrivate());
        
        //System.out.println("Signature Value:\n " + DatatypeConverter.printHexBinary(signature)); // not working in branch but works locally
	
		System.out.println("Verification: " + Verify_Digital_Signature(input.getBytes(),signature, keyPair.getPublic()));
		
		// decrypt
	    decrypted = rsa.decrypt(encrypted);
	    System.out.println("Decrypting Bytes: " + bytesToString(decrypted));
	    System.out.println("Decrypted String: " + new String(decrypted));
    }
    
    // Methods below
    // Bytes to string
    public static String bytesToString(byte[] encrypted){
//    	System.out.println("In bytesToString encrypted: " + encrypted);
        String test = "";
        for (byte b : encrypted){
            test += Byte.toString(b);
        }
//        B2S = test.getBytes();
//        System.out.println("In bytesToString test: " + B2S);
        return test;
    }
 
    // Encrypt message
    public byte[] encrypt(byte[] message){
    	System.out.println("tempByteArray ByteArray inside encrypt: " + message);
    	System.out.println("tempByteArray String inside encrypt: " + new String(message));
        return (new BigInteger(message)).modPow(e, N).toByteArray();
    }
 
    // Decrypt message
    public byte[] decrypt(byte[] message){
    	System.out.println("tempByteArray ByteArray inside decrypt: " + message);
    	System.out.println("tempByteArray String inside decrypt: " + new String(message));
        return (new BigInteger(message)).modPow(d, N).toByteArray();
    }

	// Create a signature
	public static byte[] Create_Digital_Signature(byte[] input,	PrivateKey Key)	throws Exception{
		Signature signature	= Signature.getInstance(SIGNING_ALGORITHM);	signature.initSign(Key); signature.update(input);
		return signature.sign();
	}

	// Generating a key pair
	public static KeyPair Generate_RSA_KeyPair() throws Exception{
		SecureRandom secureRandom = new SecureRandom();
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(RSA); keyPairGenerator.initialize(2048, secureRandom);
		return keyPairGenerator
			.generateKeyPair();
	}

	// Verify the new signature with the original
	public static boolean Verify_Digital_Signature(byte[] input, byte[] signatureToVerify, PublicKey key) throws Exception{
		Signature signature	= Signature.getInstance(SIGNING_ALGORITHM);	signature.initVerify(key); signature.update(input);
		return signature.verify(signatureToVerify);
	}

	public BigInteger getN() {
		return N;
	}

	public BigInteger getE() {
		return e;
	}

	public BigInteger getD() {
		return d;
	}

}
