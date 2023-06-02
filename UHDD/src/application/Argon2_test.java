package application;

/*
 * This class requires bcprov-debug-jdk15to18-173.jar
 * The jar can be downloaded from https://www.bouncycastle.org/latest_releases.html
 */

import java.util.Scanner;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;
import org.bouncycastle.crypto.generators.Argon2BytesGenerator;
import org.bouncycastle.crypto.params.Argon2Parameters;

public class Argon2_test {
	public static boolean verifyPassword(String input, byte[] originalHash, Argon2Parameters params) {
        Argon2BytesGenerator gen = new Argon2BytesGenerator();
        gen.init(params);
        byte[] inputHash = new byte[32]; // 256-bit hash
        gen.generateBytes(input.toCharArray(), inputHash);

        return Arrays.equals(originalHash, inputHash);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Please enter a password: ");
        String userInput = scanner.nextLine();

        // Generate a secure random salt
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);

        // Configure the Argon2 parameters
        Argon2Parameters.Builder builder = new Argon2Parameters.Builder(Argon2Parameters.ARGON2_id)
                .withSalt(salt)
                .withParallelism(4) // use 4 threads
                .withMemoryAsKB(65536) // use 64 MB
                .withIterations(3);

        Argon2Parameters params = builder.build();

        // Hash the password
        Argon2BytesGenerator gen = new Argon2BytesGenerator();
        gen.init(params);
        byte[] hash = new byte[32]; // 256-bit hash
        gen.generateBytes(userInput.toCharArray(), hash);

        // Output the hashed password
        System.out.println("Hashed password: " + Base64.getEncoder().encodeToString(hash));

        // Verify the password
        System.out.print("Please enter the password again to verify: ");
        String inputToVerify = scanner.nextLine();

        if (verifyPassword(inputToVerify, hash, params)) {
            System.out.println("The entered password is correct!");
        } else {
            System.out.println("The entered password is incorrect!");
        }
    }

}