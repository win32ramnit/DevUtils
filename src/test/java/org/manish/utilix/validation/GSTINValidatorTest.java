package org.manish.utilix.validation;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GSTINValidatorTest {
    private static final String CHARSET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final int MODULUS = CHARSET.length();

    @Test
    void testValidGSTIN() {
        String valid = generateValidGSTIN("27ABCDE1234F1Z");
        assertTrue(GSTINValidator.isValidGSTIN(valid));
        assertTrue(GSTINValidator.isValidGSTIN(valid.toLowerCase()));
    }

    @Test
    void testInvalidGSTIN() {
        String valid = generateValidGSTIN("27ABCDE1234F1Z");
        char invalidChecksum = valid.charAt(14) == '0' ? '1' : '0';
        assertFalse(GSTINValidator.isValidGSTIN(valid.substring(0, 14) + invalidChecksum));
        assertFalse(GSTINValidator.isValidGSTIN("27ABCDE1234F1"));
        assertFalse(GSTINValidator.isValidGSTIN("27ABC1234F1ZA"));
        assertFalse(GSTINValidator.isValidGSTIN(" 27ABCDE1234F1Z5"));
    }

    @Test
    void testNullGSTIN() {
        assertThrows(IllegalArgumentException.class, () -> GSTINValidator.isValidGSTIN(null));
    }

    private static String generateValidGSTIN(String base14) {
        int factor = 2;
        int sum = 0;
        for (int i = base14.length() - 1; i >= 0; i--) {
            int codePoint = CHARSET.indexOf(base14.charAt(i));
            int addend = factor * codePoint;
            factor = factor == 2 ? 1 : 2;
            addend = (addend / MODULUS) + (addend % MODULUS);
            sum += addend;
        }
        int remainder = sum % MODULUS;
        int checkCodePoint = (MODULUS - remainder) % MODULUS;
        return base14 + CHARSET.charAt(checkCodePoint);
    }
}

