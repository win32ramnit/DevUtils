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

/**
 * Legacy AES helper retained for backward compatibility with older code.
 * <p>
 * Despite the historical name, this class still derives a 256-bit AES key by hashing the supplied
 * secret with SHA-256. New code should prefer {@link AES256Encryption}.
 * </p>
 *
 * @deprecated Use {@link AES256Encryption} for new development.
 */
@Deprecated
public final class AES512Encryption {

    private AES512Encryption() {
    }

    private static final String ENCRYPTION_MODE_KEY = "ENCRYPTION_215_MODE";
    private static final String SECRET_KEY_ENV = "AES_SECRETE_KEY";
    private static final String SECRET_KEY_ENV_ALT = "AES_SECRET_KEY";
    private static final int GCM_IV_LENGTH = 12;
    private static final int GCM_TAG_LENGTH_BITS = 128;

    /**
     * Encrypts plaintext using configuration resolved from system properties or environment
     * variables.
     *
     * @param data the plaintext to encrypt
     * @return the encrypted payload encoded as Base64
     * @throws Exception if encryption fails or the cipher cannot be initialized
     */
    public static String encrypt(String data) throws Exception {
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
        return encrypt(data, secretKey, encryptionMode);
    }

    /**
     * Encrypts plaintext using the supplied secret key and cipher mode.
     *
     * @param data the plaintext to encrypt
     * @param secretKey the raw secret text used to derive the AES key
     * @param encryptionMode the cipher transformation, such as {@code AES/CBC/PKCS5Padding}
     * @return the encrypted payload encoded as Base64
     * @throws Exception if encryption fails or the cipher cannot be initialized
     */
    public static String encrypt(String data, String secretKey, String encryptionMode)
        throws Exception {
        if (data == null) {
            throw new IllegalArgumentException("data must not be null");
        }
        requireNotBlank(secretKey, "Secret key must not be blank");
        requireNotBlank(encryptionMode, "Encryption mode must not be blank");

        char[] secretKeyChars = secretKey.toCharArray();
        try {
            String mode = encryptionMode.trim();
            byte[] keyBytes = new String(secretKeyChars).getBytes(StandardCharsets.UTF_8);

            MessageDigest sha = MessageDigest.getInstance("SHA-256");
            byte[] hashKey = sha.digest(keyBytes);

            SecretKeySpec secretKeySpec = new SecretKeySpec(hashKey, "AES");
            Cipher cipher = Cipher.getInstance(mode);
            if (isGcm(mode)) {
                byte[] iv = new byte[GCM_IV_LENGTH];
                new SecureRandom().nextBytes(iv);
                GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH_BITS, iv);
                cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, spec);
            } else {
                cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            }

            byte[] encrypted = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
            byte[] iv = cipher.getIV();

            byte[] output;
            if (iv != null && iv.length > 0) {
                output = new byte[iv.length + encrypted.length];
                System.arraycopy(iv, 0, output, 0, iv.length);
                System.arraycopy(encrypted, 0, output, iv.length, encrypted.length);
            } else {
                output = encrypted;
            }

            return Base64.getEncoder().encodeToString(output).replaceAll("\\s", "");
        } finally {
            Arrays.fill(secretKeyChars, '\0');
        }
    }

    /**
     * Decrypts ciphertext using configuration resolved from system properties or environment
     * variables.
     *
     * @param encryptedData the Base64 encoded encrypted payload
     * @return the decrypted plaintext
     * @throws Exception if decryption fails or the cipher cannot be initialized
     */
    public static String decrypt(String encryptedData) throws Exception {
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
        return decrypt(encryptedData, secretKey, encryptionMode);
    }

    /**
     * Decrypts ciphertext using the supplied secret key and cipher mode.
     *
     * @param encryptedData the Base64 encoded encrypted payload
     * @param secretKey the raw secret text used to derive the AES key
     * @param encryptionMode the cipher transformation used during encryption
     * @return the decrypted plaintext
     * @throws Exception if decryption fails or the cipher cannot be initialized
     */
    public static String decrypt(String encryptedData, String secretKey, String encryptionMode)
        throws Exception {
        if (encryptedData == null) {
            throw new IllegalArgumentException("encryptedData must not be null");
        }
        requireNotBlank(secretKey, "Secret key must not be blank");
        requireNotBlank(encryptionMode, "Encryption mode must not be blank");

        char[] secretKeyChars = secretKey.toCharArray();
        try {
            String mode = encryptionMode.trim();
            byte[] keyBytes = new String(secretKeyChars).getBytes(StandardCharsets.UTF_8);

            MessageDigest sha = MessageDigest.getInstance("SHA-256");
            byte[] hashKey = sha.digest(keyBytes);

            SecretKeySpec secretKeySpec = new SecretKeySpec(hashKey, "AES");
            Cipher cipher = Cipher.getInstance(mode);

            byte[] decoded = Base64.getDecoder().decode(encryptedData.replaceAll("\"", ""));
            byte[] encryptedBytes;

            if (requiresIv(mode)) {
                int ivLength = ivLengthForMode(mode, cipher.getBlockSize());
                if (decoded.length <= ivLength) {
                    throw new IllegalArgumentException("Encrypted data is too short");
                }
                byte[] iv = Arrays.copyOfRange(decoded, 0, ivLength);
                encryptedBytes = Arrays.copyOfRange(decoded, ivLength, decoded.length);
                if (isGcm(mode)) {
                    GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH_BITS, iv);
                    cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, spec);
                } else {
                    IvParameterSpec ivSpec = new IvParameterSpec(iv);
                    cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivSpec);
                }
            } else {
                encryptedBytes = decoded;
                cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            }

            byte[] decrypted = cipher.doFinal(encryptedBytes);

            return new String(decrypted, StandardCharsets.UTF_8);
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
