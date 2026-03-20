package org.manish.utilix.validation;

import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Validates GSTIN values using both structural rules and checksum verification.
 */
public final class GSTINValidator {
    private static final String CHARSET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final int MODULUS = CHARSET.length();
    private static final Pattern GSTIN_PATTERN = Pattern.compile(
        "^[0-9]{2}[A-Z]{5}[0-9]{4}[A-Z][1-9A-Z]Z[0-9A-Z]$"
    );

    private GSTINValidator() {
    }

    /**
     * Determines whether the supplied value is a valid GSTIN.
     *
     * @param gstin the GSTIN value to validate
     * @return {@code true} when the value matches the GSTIN format and checksum
     * @throws IllegalArgumentException if {@code gstin} is {@code null}
     */
    public static boolean isValidGSTIN(String gstin) {
        if (gstin == null) {
            throw new IllegalArgumentException("gstin must not be null");
        }
        if (!gstin.equals(gstin.trim())) {
            return false;
        }

        String normalized = gstin.toUpperCase(Locale.ROOT);
        if (!GSTIN_PATTERN.matcher(normalized).matches()) {
            return false;
        }

        char expectedChecksum = generateChecksum(normalized.substring(0, 14));
        return normalized.charAt(14) == expectedChecksum;
    }

    private static char generateChecksum(String input) {
        int factor = 2;
        int sum = 0;

        for (int i = input.length() - 1; i >= 0; i--) {
            int codePoint = CHARSET.indexOf(input.charAt(i));
            int addend = factor * codePoint;
            factor = factor == 2 ? 1 : 2;
            addend = (addend / MODULUS) + (addend % MODULUS);
            sum += addend;
        }

        int remainder = sum % MODULUS;
        int checkCodePoint = (MODULUS - remainder) % MODULUS;
        return CHARSET.charAt(checkCodePoint);
    }
}
