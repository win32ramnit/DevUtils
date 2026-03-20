package org.manish.utilix.text;

/**
 * Provides helpers for masking sensitive values before logging or displaying them.
 */
public final class MaskingUtils {
    private MaskingUtils() {
    }

    /**
     * Masks the middle portion of a string while preserving the requested prefix and suffix.
     *
     * @param value the value to mask
     * @param visiblePrefix the number of leading characters to keep visible
     * @param visibleSuffix the number of trailing characters to keep visible
     * @param maskChar the masking character to use
     * @return the masked value, or {@code null} when the input is {@code null}
     * @throws IllegalArgumentException if prefix or suffix lengths are negative
     */
    public static String mask(String value, int visiblePrefix, int visibleSuffix, char maskChar) {
        if (value == null) {
            return null;
        }
        if (visiblePrefix < 0 || visibleSuffix < 0) {
            throw new IllegalArgumentException("visible prefix and suffix must be >= 0");
        }
        if (visiblePrefix + visibleSuffix >= value.length()) {
            return value;
        }

        StringBuilder out = new StringBuilder(value.length());
        out.append(value, 0, visiblePrefix);
        for (int i = visiblePrefix; i < value.length() - visibleSuffix; i++) {
            out.append(maskChar);
        }
        out.append(value.substring(value.length() - visibleSuffix));
        return out.toString();
    }

    /**
     * Masks the local part of an email address while preserving the domain.
     *
     * @param email the email address to mask
     * @return the masked email, or {@code null} when the input is {@code null}
     */
    public static String maskEmail(String email) {
        if (email == null) {
            return null;
        }
        int atIndex = email.indexOf('@');
        if (atIndex <= 0 || atIndex != email.lastIndexOf('@')) {
            if (email.length() == 1) {
                return "*";
            }
            return mask(email, 1, 0, '*');
        }

        String localPart = email.substring(0, atIndex);
        String domain = email.substring(atIndex);
        if (localPart.length() == 1) {
            return "*" + domain;
        }
        if (localPart.length() == 2) {
            return localPart.charAt(0) + "*" + domain;
        }
        return localPart.charAt(0)
            + repeat('*', localPart.length() - 2)
            + localPart.charAt(localPart.length() - 1)
            + domain;
    }

    /**
     * Masks all digits of a mobile number except the last four.
     *
     * @param mobileNumber the mobile number to mask
     * @return the masked mobile number, or {@code null} when the input is {@code null}
     */
    public static String maskMobileNumber(String mobileNumber) {
        return maskTrailingDigits(mobileNumber, 4);
    }

    /**
     * Masks all digits of an account number except the last four.
     *
     * @param accountNumber the account number to mask
     * @return the masked account number, or {@code null} when the input is {@code null}
     */
    public static String maskAccountNumber(String accountNumber) {
        return maskTrailingDigits(accountNumber, 4);
    }

    private static String maskTrailingDigits(String value, int visibleDigits) {
        if (value == null) {
            return null;
        }

        StringBuilder out = new StringBuilder(value);
        int visibleCount = 0;
        for (int i = value.length() - 1; i >= 0; i--) {
            char ch = value.charAt(i);
            if (Character.isDigit(ch)) {
                if (visibleCount < visibleDigits) {
                    visibleCount++;
                } else {
                    out.setCharAt(i, '*');
                }
            }
        }
        return out.toString();
    }

    private static String repeat(char value, int count) {
        StringBuilder out = new StringBuilder(count);
        for (int i = 0; i < count; i++) {
            out.append(value);
        }
        return out.toString();
    }
}

