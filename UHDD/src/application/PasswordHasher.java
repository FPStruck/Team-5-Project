package application;

import java.security.SecureRandom;
import java.util.Arrays;

import org.bouncycastle.crypto.generators.Argon2BytesGenerator;
import org.bouncycastle.crypto.params.Argon2Parameters;

public class PasswordHasher {
    private SecureRandom random;
    private Argon2BytesGenerator gen;

    public PasswordHasher() {
        this.random = new SecureRandom();
        this.gen = new Argon2BytesGenerator();
    }

    public PasswordHash hashPassword(String password) {
        // Generate a secure random salt
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
        gen.init(params);
        byte[] hash = new byte[32]; // 256-bit hash
        gen.generateBytes(password.toCharArray(), hash);

        return new PasswordHash(hash, params);
    }

    public boolean verifyPassword(String password, PasswordHash passwordHash) {
        gen.init(passwordHash.getParams());
        byte[] inputHash = new byte[32]; // 256-bit hash
        gen.generateBytes(password.toCharArray(), inputHash);

        return Arrays.equals(passwordHash.getHash(), inputHash);
    }
}
