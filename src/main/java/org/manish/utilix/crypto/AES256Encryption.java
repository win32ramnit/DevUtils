package org.manish.utilix.crypto;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;
import java.util.Locale;
import java.util.Objects;

/**
 * Encrypts and decrypts UTF-8 strings using AES with a SHA-256 derived key.
 * <p>
 * The secret is resolved from {@code AES_SECRETE_KEY} or {@code AES_SECRET_KEY}, and the cipher
 * mode is resolved from {@code ENCRYPTION_215_MODE}. System properties are checked after
 * environment variables to make testing easier.
 * </p>
 * <p>
 * When the configured mode requires an IV, the generated IV is prefixed to the encrypted payload
 * before Base64 URL-safe encoding.
 * </p>
 */
public final class AES256Encryption {

    private AES256Encryption() {
    }

    private static final String ENCRYPTION_MODE_KEY = "ENCRYPTION_215_MODE";
    private static final String SECRET_KEY_ENV = "AES_SECRETE_KEY";
    private static final String SECRET_KEY_ENV_ALT = "AES_SECRET_KEY";
    private static final int GCM_IV_LENGTH = 12;
    private static final int GCM_TAG_LENGTH_BITS = 128;

    /**
     * Encrypts plaintext using the configured system properties or environment variables.
     *
     * @param plaintext the plaintext to encrypt
     * @return the encrypted payload encoded with Base64 URL-safe encoding without padding
     * @throws NullPointerException if {@code plaintext} is {@code null}
     * @throws IllegalArgumentException if the configured secret key or encryption mode is missing
     * @throws IllegalStateException if encryption fails
     */
    public static String encrypt(String plaintext) {
        Objects.requireNonNull(plaintext, "Plaintext input cannot be null");
        String secretKey = resolveSecretKey();
        if (isBlank(secretKey)) {
            throw new IllegalArgumentException(
                "Secret key environment variable is not set or empty");
        }
        String encryptionMode = resolveEncryptionMode();
        if (isBlank(encryptionMode)) {
            throw new IllegalArgumentException(
                "Encryption mode environment variable is not set or empty");
        }
        return encrypt(plaintext, secretKey, encryptionMode);
    }

    /**
     * Encrypts plaintext using the supplied secret key and cipher mode.
     *
     * @param plaintext the plaintext to encrypt
     * @param secretKey the raw secret text used to derive the AES key
     * @param encryptionMode the cipher transformation, such as {@code AES/CBC/PKCS5Padding}
     * @return the encrypted payload encoded with Base64 URL-safe encoding without padding
     * @throws NullPointerException if {@code plaintext} is {@code null}
     * @throws IllegalArgumentException if {@code secretKey} or {@code encryptionMode} is blank
     * @throws IllegalStateException if encryption fails
     */
    public static String encrypt(String plaintext, String secretKey, String encryptionMode) {
        Objects.requireNonNull(plaintext, "Plaintext input cannot be null");
        requireNotBlank(secretKey, "Secret key must not be blank");
        requireNotBlank(encryptionMode, "Encryption mode must not be blank");

        char[] secretKeyChars = secretKey.toCharArray();
        try {
            String mode = encryptionMode.trim();
            byte[] keyBytes = new String(secretKeyChars).getBytes(StandardCharsets.UTF_8);
            byte[] hashedKey = MessageDigest.getInstance("SHA-256").digest(keyBytes);
            SecretKeySpec secretKeySpec = new SecretKeySpec(hashedKey, "AES");

            Cipher cipher = Cipher.getInstance(mode);
            if (isGcm(mode)) {
                byte[] iv = new byte[GCM_IV_LENGTH];
                new SecureRandom().nextBytes(iv);
                GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH_BITS, iv);
                cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, spec);
            } else {
                cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            }
            byte[] iv = cipher.getIV();
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
            Arrays.fill(secretKeyChars, '\0');
        }
    }

    /**
     * Decrypts ciphertext using the configured system properties or environment variables.
     *
     * @param encryptedText the Base64 URL-safe encoded encrypted payload
     * @return the decrypted plaintext
     * @throws NullPointerException if {@code encryptedText} is {@code null}
     * @throws IllegalArgumentException if the configured secret key or encryption mode is missing
     * @throws IllegalStateException if decryption fails
     */
    public static String decrypt(String encryptedText) {
        Objects.requireNonNull(encryptedText, "Encrypted text cannot be null");
        String secretKey = resolveSecretKey();
        if (isBlank(secretKey)) {
            throw new IllegalArgumentException(
                "Secret key environment variable is not set or empty");
        }
        String encryptionMode = resolveEncryptionMode();
        if (isBlank(encryptionMode)) {
            throw new IllegalArgumentException(
                "Encryption mode environment variable is not set or empty");
        }
        return decrypt(encryptedText, secretKey, encryptionMode);
    }

    /**
     * Decrypts ciphertext using the supplied secret key and cipher mode.
     *
     * @param encryptedText the Base64 URL-safe encoded encrypted payload
     * @param secretKey the raw secret text used to derive the AES key
     * @param encryptionMode the cipher transformation used during encryption
     * @return the decrypted plaintext
     * @throws NullPointerException if {@code encryptedText} is {@code null}
     * @throws IllegalArgumentException if {@code secretKey} or {@code encryptionMode} is blank
     * @throws IllegalStateException if decryption fails
     */
    public static String decrypt(String encryptedText, String secretKey, String encryptionMode) {
        Objects.requireNonNull(encryptedText, "Encrypted text cannot be null");
        requireNotBlank(secretKey, "Secret key must not be blank");
        requireNotBlank(encryptionMode, "Encryption mode must not be blank");

        char[] secretKeyChars = secretKey.toCharArray();
        try {
            String mode = encryptionMode.trim();
            byte[] keyBytes = new String(secretKeyChars).getBytes(StandardCharsets.UTF_8);
            byte[] hashedKey = MessageDigest.getInstance("SHA-256").digest(keyBytes);
            SecretKeySpec secretKeySpec = new SecretKeySpec(hashedKey, "AES");

            byte[] inputBytes = Base64.getUrlDecoder().decode(encryptedText);

            Cipher cipher = Cipher.getInstance(mode);
            int blockSize = cipher.getBlockSize();
            int ivLength = ivLengthForMode(mode, blockSize);
            byte[] encryptedBytes;

            if (requiresIv(mode)) {
                if (inputBytes.length <= ivLength) {
                    throw new IllegalArgumentException("Encrypted data is too short");
                }
                byte[] iv = Arrays.copyOfRange(inputBytes, 0, ivLength);
                encryptedBytes = Arrays.copyOfRange(inputBytes, ivLength, inputBytes.length);
                if (isGcm(mode)) {
                    GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH_BITS, iv);
                    cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, spec);
                } else {
                    IvParameterSpec ivSpec = new IvParameterSpec(iv);
                    cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivSpec);
                }
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

    private static String resolveSecretKey() {
        String secretKey = getEnvOrProperty(SECRET_KEY_ENV);
        if (isBlank(secretKey)) {
            secretKey = getEnvOrProperty(SECRET_KEY_ENV_ALT);
        }
        return secretKey;
    }

    private static String resolveEncryptionMode() {
        return getEnvOrProperty(ENCRYPTION_MODE_KEY);
    }

    private static String getEnvOrProperty(String key) {
        String env = System.getenv(key);
        if (!isBlank(env)) {
            return env;
        }
        return System.getProperty(key);
    }

    private static boolean requiresIv(String mode) {
        String upper = mode.toUpperCase(Locale.ROOT);
        return upper.contains("/CBC")
            || upper.contains("/CFB")
            || upper.contains("/OFB")
            || upper.contains("/CTR")
            || upper.contains("/GCM");
    }

    private static boolean isGcm(String mode) {
        return mode.toUpperCase(Locale.ROOT).contains("/GCM");
    }

    private static int ivLengthForMode(String mode, int blockSize) {
        if (isGcm(mode)) {
            return GCM_IV_LENGTH;
        }
        return blockSize;
    }

    private static void requireNotBlank(String value, String message) {
        if (isBlank(value)) {
            throw new IllegalArgumentException(message);
        }
    }

    private static boolean isBlank(String value) {
        if (value == null) {
            return true;
        }
        for (int i = 0; i < value.length(); i++) {
            if (!Character.isWhitespace(value.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}
