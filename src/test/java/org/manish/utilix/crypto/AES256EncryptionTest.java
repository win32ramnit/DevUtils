package org.manish.utilix.crypto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AES256EncryptionTest {

    private static final String MODE = "AES/CBC/PKCS5Padding";
    private static final String SECRET_KEY = "ThisIsASecretKeyForTestsOnly123!";

    @Test
    void testEncryptionDecryption() {
        String original = "SensitiveData123";
        String encrypted = AES256Encryption.encrypt(original, SECRET_KEY, MODE);
        String decrypted = AES256Encryption.decrypt(encrypted, SECRET_KEY, MODE);

        assertNotNull(encrypted, "Encrypted result should not be null");
        assertNotEquals(original, encrypted, "Encrypted data should differ from plaintext");
        assertEquals(original, decrypted, "Decrypted result should match original plaintext");
    }

    @Test
    void testEncryptNullInput() {
        Exception exception = assertThrows(NullPointerException.class, () -> {
            AES256Encryption.encrypt(null);
        });
        assertTrue(exception.getMessage().contains("Plaintext"));
    }

    @Test
    void testDecryptNullInput() {
        Exception exception = assertThrows(NullPointerException.class, () -> {
            AES256Encryption.decrypt(null);
        });
        assertTrue(exception.getMessage().contains("Encrypted"));
    }

    @Test
    void testInvalidEncryptedData() {
        String invalidData = "not_base64_encoded";

        Exception exception = assertThrows(IllegalStateException.class, () -> {
            AES256Encryption.decrypt(invalidData, SECRET_KEY, MODE);
        });
        assertTrue(exception.getMessage().contains("Decryption failed"));
    }

    @Test
    void testPropertyFallback() {
        System.setProperty("ENCRYPTION_215_MODE", MODE);
        System.setProperty("AES_SECRETE_KEY", SECRET_KEY);
        try {
            String original = "FallbackData123";
            String encrypted = AES256Encryption.encrypt(original);
            String decrypted = AES256Encryption.decrypt(encrypted);

            assertNotNull(encrypted);
            assertEquals(original, decrypted);
        } finally {
            System.clearProperty("ENCRYPTION_215_MODE");
            System.clearProperty("AES_SECRETE_KEY");
        }
    }
}
