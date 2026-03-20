package org.manish.utilix.validation;

/**
 * Validates Aadhaar numbers using length, leading-digit, and Verhoeff checksum rules.
 * <p>
 * Spaces are ignored so values such as {@code 1234 5678 9012} can be validated directly.
 * This utility validates only the structural format and checksum; it does not verify issuance.
 * </p>
 */
public final class AadhaarValidator {
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

    private AadhaarValidator() {
    }

    /**
     * Determines whether the supplied value is a structurally valid Aadhaar number.
     *
     * @param aadhaarNumber the Aadhaar number to validate
     * @return {@code true} when the value contains 12 digits, starts with 2-9, and passes the
     *     Verhoeff checksum
     * @throws IllegalArgumentException if {@code aadhaarNumber} is {@code null}
     */
    public static boolean isValidAadhaar(String aadhaarNumber) {
        if (aadhaarNumber == null) {
            throw new IllegalArgumentException("aadhaarNumber must not be null");
        }

        String normalized = aadhaarNumber.replace(" ", "");
        if (!normalized.matches("^[2-9][0-9]{11}$")) {
            return false;
        }

        int checksum = 0;
        for (int i = 0; i < normalized.length(); i++) {
            int digit = normalized.charAt(normalized.length() - 1 - i) - '0';
            checksum = D_TABLE[checksum][P_TABLE[i % 8][digit]];
        }
        return checksum == 0;
    }
}
