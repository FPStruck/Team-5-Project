package application;

import org.bouncycastle.crypto.params.Argon2Parameters;
import java.util.Base64;

public class PasswordHash {
    private byte[] hash;
    private Argon2Parameters params;

    public PasswordHash(byte[] hash, Argon2Parameters params) {
        this.hash = hash;
        this.params = params;
    }

    public byte[] getHash() {
        return this.hash;
    }

    public Argon2Parameters getParams() {
        return this.params;
    }

    public String getHashAsString() {
        return Base64.getEncoder().encodeToString(this.hash);
    }

    public String getParamsAsString() {
        return this.params.getSalt() + ":" + this.params.getMemory() + ":" + this.params.getIterations();
    }

    public static PasswordHash fromString(String hash, String params) {
        byte[] decodedHash = Base64.getDecoder().decode(hash);

        String[] splitParams = params.split(":");
        byte[] salt = splitParams[0].getBytes();
        int memory = Integer.parseInt(splitParams[1]);
        int iterations = Integer.parseInt(splitParams[2]);

        Argon2Parameters.Builder builder = new Argon2Parameters.Builder(Argon2Parameters.ARGON2_id)
                .withSalt(salt)
                .withMemoryAsKB(memory)
                .withIterations(iterations);

        Argon2Parameters paramsObj = builder.build();

        return new PasswordHash(decodedHash, paramsObj);
    }
}
