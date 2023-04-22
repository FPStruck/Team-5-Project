package application;

import java.security.*;
import java.security.spec.*;

public class Ecc {

 public static void main(String[] args) throws Exception {

     // Create a public and private key for secp384r1.
	 
	 /* All the increase complexity of curves
	  * secp192r1 (NIST P-192)
		secp224r1 (NIST P-224)
		secp256r1 (NIST P-256)
		secp384r1 (NIST P-384)
		secp521r1 (NIST P-521)
	  */
	 
     KeyPairGenerator g = KeyPairGenerator.getInstance("EC","SunEC");
     ECGenParameterSpec ecsp = new ECGenParameterSpec("secp160k1");
     g.initialize(ecsp);

     KeyPair kp = g.genKeyPair();
     PrivateKey privKey = kp.getPrivate();
     PublicKey pubKey = kp.getPublic();

     // Select the signature algorithm.
     Signature s = Signature.getInstance("SHA256withECDSA","SunEC");
     s.initSign(privKey);

     // Compute the signature.
     byte[] msg = "Hello, World!".getBytes("UTF-8");
     byte[] sig;
     s.update(msg);
     sig = s.sign();

     // Verify the signature.
     Signature sg = Signature.getInstance("SHA256withECDSA", "SunEC");
     sg.initVerify(pubKey);
     sg.update(msg);
     boolean validSignature = sg.verify(sig);

     // Shows the public key, what will be given out freely
     System.out.println(pubKey);
          
     // Shows the signature for learning purposes 
     System.out.println(sig);
     
     //Is the Signature valid?
     System.out.println(validSignature);
 }
}