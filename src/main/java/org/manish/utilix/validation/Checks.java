package org.manish.utilix.validation;

import org.manish.utilix.text.StringUtils;

/**
 * Provides argument validation helpers for defensive programming.
 */
public final class Checks {
    private Checks() {
    }

    /**
     * Ensures that a value is not {@code null}.
     *
     * @param value the value to validate
     * @param name the logical argument name used in the exception message
     * @param <T> the value type
     * @return the original value when validation succeeds
     * @throws IllegalArgumentException if {@code value} is {@code null}
     */
    public static <T> T notNull(T value, String name) {
        if (value == null) {
            throw new IllegalArgumentException(name + " must not be null");
        }
        return value;
    }

    /**
     * Ensures that a string is neither {@code null} nor blank.
     *
     * @param value the string to validate
     * @param name the logical argument name used in the exception message
     * @return the original value when validation succeeds
     * @throws IllegalArgumentException if {@code value} is {@code null} or blank
     */
    public static String notBlank(String value, String name) {
        if (value == null) {
            throw new IllegalArgumentException(name + " must not be null");
        }
        if (StringUtils.isBlank(value)) {
            throw new IllegalArgumentException(name + " must not be blank");
        }
        return value;
    }

    /**
     * Ensures that an integer is strictly positive.
     *
     * @param value the numeric value to validate
     * @param name the logical argument name used in the exception message
     * @return the original value when validation succeeds
     * @throws IllegalArgumentException if {@code value} is less than or equal to zero
     */
    public static int positive(int value, String name) {
        if (value <= 0) {
            throw new IllegalArgumentException(name + " must be > 0");
        }
        return value;
    }

    /**
     * Ensures that a long value is zero or greater.
     *
     * @param value the numeric value to validate
     * @param name the logical argument name used in the exception message
     * @return the original value when validation succeeds
     * @throws IllegalArgumentException if {@code value} is negative
     */
    public static long nonNegative(long value, String name) {
        if (value < 0) {
            throw new IllegalArgumentException(name + " must be >= 0");
        }
        return value;
    }
}
