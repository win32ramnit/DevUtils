package org.manish.utilix.validation;

import java.util.regex.Pattern;

/**
 * Validates email addresses using a pragmatic application-level rule set.
 * <p>
 * This validator is intentionally stricter than the full RFC surface area. It is designed for
 * typical business application input rather than every syntactically legal edge case.
 * </p>
 */
public final class EmailValidator {
    private static final int MAX_EMAIL_LENGTH = 254;
    private static final int MAX_LOCAL_PART_LENGTH = 64;
    private static final int MAX_DOMAIN_LENGTH = 253;
    private static final Pattern LOCAL_PART_PATTERN = Pattern.compile(
        "^[A-Z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[A-Z0-9!#$%&'*+/=?^_`{|}~-]+)*$",
        Pattern.CASE_INSENSITIVE
    );
    private static final Pattern DOMAIN_LABEL_PATTERN = Pattern.compile(
        "^[A-Z0-9](?:[A-Z0-9-]{0,61}[A-Z0-9])?$",
        Pattern.CASE_INSENSITIVE
    );

    private EmailValidator() {
    }

    /**
     * Determines whether the supplied value is a valid email address.
     *
     * @param email the email address to validate
     * @return {@code true} when the value matches the supported email format
     * @throws IllegalArgumentException if {@code email} is {@code null}
     */
    public static boolean isValidEmail(String email) {
        if (email == null) {
            throw new IllegalArgumentException("email must not be null");
        }
        if (email.isEmpty() || email.length() > MAX_EMAIL_LENGTH || !email.equals(email.trim())) {
            return false;
        }

        int atIndex = email.indexOf('@');
        if (atIndex <= 0 || atIndex != email.lastIndexOf('@') || atIndex == email.length() - 1) {
            return false;
        }

        String localPart = email.substring(0, atIndex);
        String domain = email.substring(atIndex + 1);

        if (localPart.length() > MAX_LOCAL_PART_LENGTH || domain.length() > MAX_DOMAIN_LENGTH) {
            return false;
        }
        if (localPart.startsWith(".") || localPart.endsWith(".") || localPart.contains("..")) {
            return false;
        }
        if (domain.startsWith(".") || domain.endsWith(".") || domain.contains("..")) {
            return false;
        }
        if (!LOCAL_PART_PATTERN.matcher(localPart).matches()) {
            return false;
        }

        String[] labels = domain.split("\\.");
        if (labels.length < 2) {
            return false;
        }

        for (String label : labels) {
            if (!DOMAIN_LABEL_PATTERN.matcher(label).matches()) {
                return false;
            }
        }

        return true;
    }
}
