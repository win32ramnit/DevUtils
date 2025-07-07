package org.manish.utilix.crypto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AES256EncryptionTest {


    @BeforeEach
    void setup() {
        // Ensure required environment variables are set for the test
        System.setProperty("ENCRYPTION_215_MODE", "AES/CBC/PKCS5Padding");
        System.setProperty("AES_SECRETE_KEY", "ThisIsASecretKeyForTestsOnly123!");

//        System.out.println(System.getProperty("SECRET_KEY_ENV"));

        // Optional fallback for getEnv() usage in your implementation
        // You can use System.setProperty and read via System.getProperty() in code if neede
    }

    @Test
    void testEncryptionDecryption() {
        String original = "SensitiveData123";
        String encrypted = AES256Encryption.encrypt(original);
        String decrypted = AES256Encryption.decrypt(encrypted);

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
            AES256Encryption.decrypt(invalidData);
        });
        assertTrue(exception.getMessage().contains("Decryption failed"));
    }

}
