package org.manish.utilix.crypto;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;
import java.util.Objects;

public class AES256Encryption {

    private static final String ENCRYPTION_MODE = System.getenv("ENCRYPTION_215_MODE");
        // e.g., "AES/CBC/PKCS5Padding"
    private static final String SECRET_KEY_ENV = "AES_SECRETE_KEY";

    public static String encrypt(String plaintext) {
        Objects.requireNonNull(plaintext, "Plaintext input cannot be null");

        String secretKey = System.getenv(SECRET_KEY_ENV);
        if (secretKey == null || secretKey.isBlank()) {
            throw new IllegalArgumentException(
                "Secret key environment variable is not set or empty");
        }

        if (ENCRYPTION_MODE == null || ENCRYPTION_MODE.isBlank()) {
            throw new IllegalArgumentException(
                "Encryption mode environment variable is not set or empty");
        }

        char[] secretKeyChars = secretKey.toCharArray();
        try {
            byte[] keyBytes = new String(secretKeyChars).getBytes(StandardCharsets.UTF_8);
            byte[] hashedKey = MessageDigest.getInstance("SHA-256").digest(keyBytes);
            SecretKeySpec secretKeySpec = new SecretKeySpec(hashedKey, "AES");

            Cipher cipher = Cipher.getInstance(ENCRYPTION_MODE);
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            byte[] iv = cipher.getIV(); // may be null for ECB
            byte[] encryptedBytes = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));

            byte[] finalOutput;
            if (iv != null && iv.length > 0) {
                finalOutput = new byte[iv.length + encryptedBytes.length];
                System.arraycopy(iv, 0, finalOutput, 0, iv.length);
                System.arraycopy(encryptedBytes, 0, finalOutput, iv.length, encryptedBytes.length);
            } else {
                finalOutput = encryptedBytes;
            }

            return Base64.getUrlEncoder().withoutPadding().encodeToString(finalOutput);
        } catch (Exception e) {
            throw new IllegalStateException("Encryption failed: " + e.getMessage(), e);
        } finally {
            Arrays.fill(secretKeyChars, '\0'); // wipe sensitive data
        }
    }

    public static String decrypt(String encryptedText) {
        Objects.requireNonNull(encryptedText, "Encrypted text cannot be null");

        String secretKey = System.getenv(SECRET_KEY_ENV);
        if (secretKey == null || secretKey.isBlank()) {
            throw new IllegalArgumentException(
                "Secret key environment variable is not set or empty");
        }

        if (ENCRYPTION_MODE == null || ENCRYPTION_MODE.isBlank()) {
            throw new IllegalArgumentException(
                "Encryption mode environment variable is not set or empty");
        }

        char[] secretKeyChars = secretKey.toCharArray();
        try {
            byte[] keyBytes = new String(secretKeyChars).getBytes(StandardCharsets.UTF_8);
            byte[] hashedKey = MessageDigest.getInstance("SHA-256").digest(keyBytes);
            SecretKeySpec secretKeySpec = new SecretKeySpec(hashedKey, "AES");

            byte[] inputBytes = Base64.getUrlDecoder().decode(encryptedText);

            Cipher cipher = Cipher.getInstance(ENCRYPTION_MODE);
            int blockSize = cipher.getBlockSize();

            IvParameterSpec ivSpec = null;
            byte[] encryptedBytes;

            if (ENCRYPTION_MODE.contains("/CBC") || ENCRYPTION_MODE.contains(
                "/CFB") || ENCRYPTION_MODE.contains("/OFB")) {
                byte[] iv = Arrays.copyOfRange(inputBytes, 0, blockSize);
                encryptedBytes = Arrays.copyOfRange(inputBytes, blockSize, inputBytes.length);
                ivSpec = new IvParameterSpec(iv);
                cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivSpec);
            } else {
                encryptedBytes = inputBytes;
                cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            }

            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
            return new String(decryptedBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new IllegalStateException("Decryption failed: " + e.getMessage(), e);
        } finally {
            Arrays.fill(secretKeyChars, '\0');
        }
    }
}
