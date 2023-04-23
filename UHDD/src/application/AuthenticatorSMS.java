package application;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import java.util.Random;
import java.util.Scanner;

public class AuthenticatorSMS {
    public static void main(String[] args) {
        // Collect the phone number from the user
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your phone number: ");
        String phoneNumber = scanner.nextLine();

        // Generate a random 6-digit code
        int code = generateCode();

        // Send the code to the phone number
        sendCodeToPhoneNumber(phoneNumber, code);

        // Ask the user to enter the code
        System.out.print("Enter the verification code: ");
        int enteredCode = scanner.nextInt();

        // Verify the code
        if (enteredCode == code) {
            System.out.println("You are verified!");
        } else {
            System.out.println("Verification failed. Please try again.");
        }
    }

    private static int generateCode() {
        Random random = new Random();
        return random.nextInt(900000) + 100000;
    }

    private static void sendCodeToPhoneNumber(String phoneNumber, int code) {
        // Set up your Twilio account
        String ACCOUNT_SID = "AC77c5dc873265a2c5fc42a0baebf461e6";
        String AUTH_TOKEN = "d4af4f572b27a1f5773eba3f605e1593";
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

        // Send the message
        Message message = Message.creator(
            new PhoneNumber(phoneNumber),
            new PhoneNumber("+15077095074"),
            "Your verification code is: " + code)
            .create();
        System.out.println("Sent verification code " + code + " to " + phoneNumber);
    }
}
