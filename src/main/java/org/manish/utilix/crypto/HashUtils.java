package org.manish.utilix.crypto;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Provides hashing helpers for common production use cases.
 */
public final class HashUtils {
    private HashUtils() {
    }

    /**
     * Computes a SHA-256 hash in lowercase hexadecimal form.
     *
     * @param value the input text to hash
     * @return the SHA-256 digest as a lowercase hexadecimal string
     * @throws IllegalArgumentException if {@code value} is {@code null}
     */
    public static String sha256Hex(String value) {
        return digestHex("SHA-256", value);
    }

    /**
     * Computes a SHA-512 hash in lowercase hexadecimal form.
     *
     * @param value the input text to hash
     * @return the SHA-512 digest as a lowercase hexadecimal string
     * @throws IllegalArgumentException if {@code value} is {@code null}
     */
    public static String sha512Hex(String value) {
        return digestHex("SHA-512", value);
    }

    /**
     * Computes an HMAC-SHA256 signature in lowercase hexadecimal form.
     *
     * @param key the HMAC secret key
     * @param message the message to sign
     * @return the HMAC-SHA256 signature as a lowercase hexadecimal string
     * @throws IllegalArgumentException if {@code key} or {@code message} is {@code null}
     */
    public static String hmacSha256Hex(String key, String message) {
        if (key == null) {
            throw new IllegalArgumentException("key must not be null");
        }
        if (message == null) {
            throw new IllegalArgumentException("message must not be null");
        }

        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            mac.init(keySpec);
            return toHex(mac.doFinal(message.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception ex) {
            throw new IllegalStateException("Failed to compute HMAC-SHA256", ex);
        }
    }

    private static String digestHex(String algorithm, String value) {
        if (value == null) {
            throw new IllegalArgumentException("value must not be null");
        }

        try {
            MessageDigest digest = MessageDigest.getInstance(algorithm);
            return toHex(digest.digest(value.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception ex) {
            throw new IllegalStateException("Failed to compute digest for " + algorithm, ex);
        }
    }

    private static String toHex(byte[] bytes) {
        StringBuilder out = new StringBuilder(bytes.length * 2);
        for (byte value : bytes) {
            out.append(String.format("%02x", value));
        }
        return out.toString();
    }
}
