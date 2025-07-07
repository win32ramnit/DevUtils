package org.manish.utilix.validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The {@code PANValidator} class provides utility methods to validate Indian Permanent Account
 * Numbers (PANs).
 * <p>
 * A valid PAN consists of 10 characters:
 * <ul>
 *     <li>First five characters: Uppercase English letters (A-Z)</li>
 *     <li>Next four characters: Digits (0-9)</li>
 *     <li>Last character: Uppercase English letter (A-Z)</li>
 * </ul>
 * </p>
 * <p>
 * This class offers a method to verify if a given PAN matches the specified pattern.
 * </p>
 * <p>
 * Example usage:
 * <pre>
 *     boolean isValid = PANValidator.isValidPAN("ABCDE1234F");
 * </pre>
 * </p>
 * <p>
 * Note: This validation checks the format but does not verify the authenticity of the PAN.
 * </p>
 *
 * @version 1.0
 * @since 1.0
 */
public class PANValidator {

    /**
     * Regular expression pattern to match a valid PAN format.
     * <p>
     * The pattern ensures the PAN consists of:
     * <ul>
     *     <li>5 uppercase letters</li>
     *     <li>4 digits</li>
     *     <li>1 uppercase letter</li>
     * </ul>
     * </p>
     */
    private static final Pattern PAN_PATTERN = Pattern.compile("^[A-Z]{5}[0-9]{4}[A-Z]{1}$");

    /**
     * Validates the format of the provided PAN.
     * <p>
     * This method checks if the given PAN matches the standard format of 5 uppercase letters,
     * followed by 4 digits, and ending with an uppercase letter.
     * </p>
     *
     * @param pan the PAN to be validated; may be {@code null}
     * @return {@code true} if the PAN matches the valid format; {@code false} otherwise
     * @throws IllegalArgumentException if the PAN is {@code null}
     * @since 1.0
     */
    public static boolean isValidPAN(String pan) {
        if (pan == null) {
            throw new IllegalArgumentException("PAN cannot be null");
        }
        return PAN_PATTERN.matcher(pan).matches();
    }
}
