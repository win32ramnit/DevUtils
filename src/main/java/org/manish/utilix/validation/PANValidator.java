package org.manish.utilix.validation;

import java.util.regex.Pattern;

/**
 * Validates the basic format of Indian Permanent Account Numbers (PANs).
 * <p>
 * A valid PAN contains 10 characters in the pattern {@code AAAAA9999A}. This utility validates
 * only the structural format and does not attempt to verify that the PAN is officially issued.
 * </p>
 */
public final class PANValidator {
    private static final Pattern PAN_PATTERN = Pattern.compile("^[A-Z]{5}[0-9]{4}[A-Z]{1}$");

    private PANValidator() {
    }

    /**
     * Determines whether the supplied PAN matches the expected format.
     *
     * @param pan the PAN value to validate
     * @return {@code true} when the value matches the standard PAN pattern
     * @throws IllegalArgumentException if {@code pan} is {@code null}
     */
    public static boolean isValidPAN(String pan) {
        if (pan == null) {
            throw new IllegalArgumentException("PAN cannot be null");
        }
        return PAN_PATTERN.matcher(pan).matches();
    }
}
