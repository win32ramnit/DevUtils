package org.manish.utilix.crypto;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;

public class AES512Encryption {

    public static final String ENCRYPTION_215_MODE = System.getenv("ENCRYPTION_215_MODE");
    public static final String AES_SECRETE_KEY = "AES_SECRETE_KEY";


    public static String encrypt(String data) throws Exception {
        char[] secretKeyChars = System.getenv(AES_SECRETE_KEY).toCharArray();
        try {
            byte[] keyBytes = new String(secretKeyChars).getBytes(StandardCharsets.UTF_8);

            MessageDigest sha = MessageDigest.getInstance("SHA-256");
            byte[] hashKey = sha.digest(keyBytes);

            SecretKeySpec secretKeySpec = new SecretKeySpec(hashKey, "AES");
            Cipher cipher = Cipher.getInstance(ENCRYPTION_215_MODE);
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);

            byte[] encrypted = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));

            return Base64.getEncoder().encodeToString(encrypted).replaceAll("\\s", "");
        } finally {
            Arrays.fill(secretKeyChars, '\0');
        }
    }

    public static String decrypt(String encryptedData) throws Exception {
        char[] secretKeyChars = System.getenv(AES_SECRETE_KEY).toCharArray();
        try {
            byte[] keyBytes = new String(secretKeyChars).getBytes(StandardCharsets.UTF_8);

            MessageDigest sha = MessageDigest.getInstance("SHA-256");
            byte[] hashKey = sha.digest(keyBytes);

            SecretKeySpec secretKeySpec = new SecretKeySpec(hashKey, "AES");
            Cipher cipher = Cipher.getInstance(ENCRYPTION_215_MODE);
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);

            byte[] decrypted =
                cipher.doFinal(Base64.getDecoder().decode(encryptedData.replaceAll("\"", "")));

            return new String(decrypted);
        } finally {
            Arrays.fill(secretKeyChars, '\0');
        }
    }

}
