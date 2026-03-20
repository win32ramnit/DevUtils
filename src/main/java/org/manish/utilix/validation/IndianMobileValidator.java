package org.manish.utilix.validation;

import java.util.regex.Pattern;

/**
 * Validates Indian mobile numbers.
 * <p>
 * Supported inputs include plain 10-digit mobile numbers as well as common country-code forms
 * such as {@code +91XXXXXXXXXX}, {@code 91XXXXXXXXXX}, or {@code 0XXXXXXXXXX}. Embedded spaces
 * and hyphens are ignored.
 * </p>
 */
public final class IndianMobileValidator {
    private static final Pattern MOBILE_PATTERN = Pattern.compile(
        "^(?:\\+91|91|0)?[6-9][0-9]{9}$"
    );

    private IndianMobileValidator() {
    }

    /**
     * Determines whether the supplied value is a valid Indian mobile number.
     *
     * @param mobileNumber the mobile number to validate
     * @return {@code true} when the value matches the supported Indian mobile number format
     * @throws IllegalArgumentException if {@code mobileNumber} is {@code null}
     */
    public static boolean isValidMobileNumber(String mobileNumber) {
        if (mobileNumber == null) {
            throw new IllegalArgumentException("mobileNumber must not be null");
        }
        String normalized = mobileNumber.replace(" ", "").replace("-", "");
        return MOBILE_PATTERN.matcher(normalized).matches();
    }
}
