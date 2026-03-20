package org.manish.utilix.validation;

import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Validates Indian Financial System Code (IFSC) values.
 * <p>
 * An IFSC code contains 11 characters in the format {@code AAAA0XXXXXX}, where the first four
 * characters are alphabetic, the fifth is always {@code 0}, and the final six are alphanumeric.
 * </p>
 */
public final class IFSCValidator {
    private static final Pattern IFSC_PATTERN = Pattern.compile("^[A-Z]{4}0[A-Z0-9]{6}$");

    private IFSCValidator() {
    }

    /**
     * Determines whether the supplied value is a valid IFSC code.
     *
     * @param ifsc the IFSC code to validate
     * @return {@code true} when the value matches the IFSC format
     * @throws IllegalArgumentException if {@code ifsc} is {@code null}
     */
    public static boolean isValidIFSC(String ifsc) {
        if (ifsc == null) {
            throw new IllegalArgumentException("ifsc must not be null");
        }
        if (!ifsc.equals(ifsc.trim())) {
            return false;
        }
        return IFSC_PATTERN.matcher(ifsc.toUpperCase(Locale.ROOT)).matches();
    }
}
