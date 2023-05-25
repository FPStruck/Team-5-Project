package application;

import java.io.IOException;
import java.util.Scanner;

import com.google.zxing.WriterException;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import de.taimos.totp.TOTP;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import org.apache.commons.codec.binary.Base32;
import org.apache.commons.codec.binary.Hex;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.SecureRandom;

public class GoogleAppAuthenticator {
	
	@FXML private static TextField TFACode;
	
	public static void main(String[] args) throws IOException, WriterException {
		String secretKey = "QDWSM3OYBPGTEVSPB5FKVDM3CSNCWHVI";
		String email = "Team5@gmail.com";
		String companyName = "CSU ITC 303";
		String barCodeUrl = Utils.getGoogleAuthenticatorBarCode(secretKey, email, companyName);
		Utils.createQRCode(barCodeUrl, "QRCode.png", 400, 400);
				
		System.out.print("Please enter 2fA code here -> ");
//		Scanner scanner = new Scanner(System.in);
//		String code = scanner.nextLine();
		String code = TFACode.getText();
		if (code.equals(Utils.getTOTPCode(secretKey))) {
			System.out.println("Logged in successfully");
		} else {
			System.out.println("Invalid 2FA Code");
		}

	}
	
	
	public class Utils {

		public static String generateSecretKey() {
			SecureRandom random = new SecureRandom();
			byte[] bytes = new byte[20];
			random.nextBytes(bytes);
			Base32 base32 = new Base32();
			return base32.encodeToString(bytes);
		}

		public static String getTOTPCode(String secretKey) {
			Base32 base32 = new Base32();
			byte[] bytes = base32.decode(secretKey);
			String hexKey = Hex.encodeHexString(bytes);
			return TOTP.getOTP(hexKey);
		}

		public static String getGoogleAuthenticatorBarCode(String secretKey, String account, String issuer) {
			try {
				return "otpauth://totp/"
						+ URLEncoder.encode(issuer + ":" + account, "UTF-8").replace("+", "%20")
						+ "?secret=" + URLEncoder.encode(secretKey, "UTF-8").replace("+", "%20")
						+ "&issuer=" + URLEncoder.encode(issuer, "UTF-8").replace("+", "%20");
			} catch (UnsupportedEncodingException e) {
				throw new IllegalStateException(e);
			}
		}

		public static void createQRCode(String barCodeData, String filePath, int height, int width)
				throws WriterException, IOException {
			BitMatrix matrix = new MultiFormatWriter().encode(barCodeData, BarcodeFormat.QR_CODE, width, height);
			try (FileOutputStream out = new FileOutputStream(filePath)) {
				MatrixToImageWriter.writeToStream(matrix, "png", out);
			}
		}

	}

}

