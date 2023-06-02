package application;

import java.security.*;
import java.security.spec.*;

public class Ecc {

 public static void main(String[] args) throws Exception {
<<<<<<< HEAD
   
=======

>>>>>>> main
     // Create a public and private key for secp384r1.
	 
	 /* All the increase complexity of curves
	  * secp192r1 (NIST P-192)
		secp224r1 (NIST P-224)
		secp256r1 (NIST P-256)
		secp384r1 (NIST P-384)
		secp521r1 (NIST P-521)
	  */
	 
<<<<<<< HEAD

=======
>>>>>>> main
     KeyPairGenerator g = KeyPairGenerator.getInstance("EC","SunEC"); // create a key pair object with two which represent the algorithm and the provider
     ECGenParameterSpec ecsp = new ECGenParameterSpec("secp256r1"); // create a elliptic-curve with a name string 
     g.initialize(ecsp);

     KeyPair kp = g.genKeyPair(); // created the key pair 
     PrivateKey privKey = kp.getPrivate();
     PublicKey pubKey = kp.getPublic();

     // Select the signature algorithm.
     
     Signature s = Signature.getInstance("SHA256withECDSA","SunEC"); // creates a signature with the algorithm and provider
     s.initSign(privKey);

     // Compute the signature.
     byte[] msg = "Hello, World!".getBytes("UTF-8"); // converts the message into bits 
     byte[] sig;
     s.update(msg); // updates the message with the signature 
     sig = s.sign(); // returns the sign signature in bytes

     // Verify the signature.
     Signature sg = Signature.getInstance("SHA256withECDSA", "SunEC"); // creates another signature object
     sg.initVerify(pubKey); // this time with a public key
     sg.update(msg); // update the public key
     boolean validSignature = sg.verify(sig); // verifies the signature created with the public key with the signed signature 

     // Shows the private key, what will be given out freely
     System.out.println("This is the private key: " + privKey);
     
     // Shows the public key, what will be given out freely
     System.out.println("This is the public key: " + pubKey);
          
     // Shows the signature for learning purposes 
     System.out.println("This is the signed signature with the private key: " + sig);
     
     //Is the Signature valid?
     System.out.println("This is the results after comparing the signature created by the private key with the public key: " + validSignature);
<<<<<<< HEAD
   
        // Shows the public key, what will be given out freely
     System.out.println(pubKey);
     System.out.println();

     // Shows the signature for learning purposes 
     System.out.println(privKey);
=======
>>>>>>> main
 }
}