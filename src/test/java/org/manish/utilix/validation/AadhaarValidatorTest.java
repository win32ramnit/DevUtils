package org.manish.utilix.validation;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AadhaarValidatorTest {
    private static final int[][] D_TABLE = {
        {0, 1, 2, 3, 4, 5, 6, 7, 8, 9},
        {1, 2, 3, 4, 0, 6, 7, 8, 9, 5},
        {2, 3, 4, 0, 1, 7, 8, 9, 5, 6},
        {3, 4, 0, 1, 2, 8, 9, 5, 6, 7},
        {4, 0, 1, 2, 3, 9, 5, 6, 7, 8},
        {5, 9, 8, 7, 6, 0, 4, 3, 2, 1},
        {6, 5, 9, 8, 7, 1, 0, 4, 3, 2},
        {7, 6, 5, 9, 8, 2, 1, 0, 4, 3},
        {8, 7, 6, 5, 9, 3, 2, 1, 0, 4},
        {9, 8, 7, 6, 5, 4, 3, 2, 1, 0}
    };
    private static final int[][] P_TABLE = {
        {0, 1, 2, 3, 4, 5, 6, 7, 8, 9},
        {1, 5, 7, 6, 2, 8, 3, 0, 9, 4},
        {5, 8, 0, 3, 7, 9, 6, 1, 4, 2},
        {8, 9, 1, 6, 0, 4, 3, 5, 2, 7},
        {9, 4, 5, 3, 1, 2, 6, 8, 7, 0},
        {4, 2, 8, 6, 5, 7, 3, 9, 0, 1},
        {2, 7, 9, 3, 8, 0, 6, 4, 1, 5},
        {7, 0, 4, 6, 9, 1, 3, 2, 5, 8}
    };
    private static final int[] INV_TABLE = {0, 4, 3, 2, 1, 5, 6, 7, 8, 9};

    @Test
    void testValidAadhaar() {
        String valid = generateValidAadhaar("23412341234");
        assertTrue(AadhaarValidator.isValidAadhaar(valid));
        assertTrue(AadhaarValidator.isValidAadhaar(valid.substring(0, 4) + " " + valid.substring(4, 8) + " " + valid.substring(8)));
    }

    @Test
    void testInvalidAadhaar() {
        String valid = generateValidAadhaar("23412341234");
        assertFalse(AadhaarValidator.isValidAadhaar(valid.substring(0, 11) + "0"));
        assertFalse(AadhaarValidator.isValidAadhaar("123456789012"));
        assertFalse(AadhaarValidator.isValidAadhaar("2341234123"));
        assertFalse(AadhaarValidator.isValidAadhaar("23412341234A"));
    }

    @Test
    void testNullAadhaar() {
        assertThrows(IllegalArgumentException.class, () -> AadhaarValidator.isValidAadhaar(null));
    }

    private static String generateValidAadhaar(String base11Digits) {
        int checksum = 0;
        for (int i = 0; i < base11Digits.length(); i++) {
            int digit = base11Digits.charAt(base11Digits.length() - 1 - i) - '0';
            checksum = D_TABLE[checksum][P_TABLE[(i + 1) % 8][digit]];
        }
        return base11Digits + INV_TABLE[checksum];
    }
}
