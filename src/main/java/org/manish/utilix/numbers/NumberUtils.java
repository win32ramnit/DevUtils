package org.manish.utilix.numbers;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Provides numeric helpers for parsing, clamping, and rounding values.
 */
public final class NumberUtils {
    private NumberUtils() {
    }

    /**
     * Attempts to parse an integer from the supplied text.
     *
     * @param value the source text; leading and trailing whitespace is ignored
     * @param defaultValue the value to return when the text is {@code null} or cannot be parsed
     * @return the parsed integer or {@code defaultValue} when parsing is not possible
     */
    public static Integer parseInt(String value, Integer defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException ex) {
            return defaultValue;
        }
    }

    /**
     * Attempts to parse a long from the supplied text.
     *
     * @param value the source text; leading and trailing whitespace is ignored
     * @param defaultValue the value to return when the text is {@code null} or cannot be parsed
     * @return the parsed long or {@code defaultValue} when parsing is not possible
     */
    public static Long parseLong(String value, Long defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        try {
            return Long.parseLong(value.trim());
        } catch (NumberFormatException ex) {
            return defaultValue;
        }
    }

    /**
     * Attempts to parse a double from the supplied text.
     *
     * @param value the source text; leading and trailing whitespace is ignored
     * @param defaultValue the value to return when the text is {@code null} or cannot be parsed
     * @return the parsed double or {@code defaultValue} when parsing is not possible
     */
    public static Double parseDouble(String value, Double defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        try {
            return Double.parseDouble(value.trim());
        } catch (NumberFormatException ex) {
            return defaultValue;
        }
    }

    /**
     * Attempts to parse a {@link BigDecimal} from the supplied text.
     *
     * @param value the source text; leading and trailing whitespace is ignored
     * @param defaultValue the value to return when the text is {@code null} or cannot be parsed
     * @return the parsed decimal or {@code defaultValue} when parsing is not possible
     */
    public static BigDecimal parseBigDecimal(String value, BigDecimal defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        try {
            return new BigDecimal(value.trim());
        } catch (NumberFormatException ex) {
            return defaultValue;
        }
    }

    /**
     * Restricts an integer value to the inclusive range {@code [min, max]}.
     *
     * @param value the value to clamp
     * @param min the inclusive lower bound
     * @param max the inclusive upper bound
     * @return {@code value} when it lies within the bounds, otherwise the nearest bound
     * @throws IllegalArgumentException if {@code min} is greater than {@code max}
     */
    public static int clamp(int value, int min, int max) {
        if (min > max) {
            throw new IllegalArgumentException("min must be <= max");
        }
        return Math.max(min, Math.min(max, value));
    }

    /**
     * Restricts a long value to the inclusive range {@code [min, max]}.
     *
     * @param value the value to clamp
     * @param min the inclusive lower bound
     * @param max the inclusive upper bound
     * @return {@code value} when it lies within the bounds, otherwise the nearest bound
     * @throws IllegalArgumentException if {@code min} is greater than {@code max}
     */
    public static long clamp(long value, long min, long max) {
        if (min > max) {
            throw new IllegalArgumentException("min must be <= max");
        }
        return Math.max(min, Math.min(max, value));
    }

    /**
     * Restricts a double value to the inclusive range {@code [min, max]}.
     *
     * @param value the value to clamp
     * @param min the inclusive lower bound
     * @param max the inclusive upper bound
     * @return {@code value} when it lies within the bounds, the nearest bound otherwise, or
     *     {@link Double#NaN} when the supplied value is {@code NaN}
     * @throws IllegalArgumentException if {@code min} is greater than {@code max}
     */
    public static double clamp(double value, double min, double max) {
        if (min > max) {
            throw new IllegalArgumentException("min must be <= max");
        }
        if (Double.isNaN(value)) {
            return value;
        }
        return Math.max(min, Math.min(max, value));
    }

    /**
     * Rounds a decimal value using {@link RoundingMode#HALF_UP}.
     *
     * @param value the value to round
     * @param scale the number of digits to keep after the decimal point
     * @return the rounded value
     * @throws IllegalArgumentException if {@code scale} is negative
     */
    public static double round(double value, int scale) {
        return round(value, scale, RoundingMode.HALF_UP);
    }

    /**
     * Rounds a {@link BigDecimal} value using {@link RoundingMode#HALF_UP}.
     *
     * @param value the value to round
     * @param scale the number of digits to keep after the decimal point
     * @return the rounded value
     * @throws IllegalArgumentException if {@code value} is {@code null} or {@code scale} is negative
     */
    public static BigDecimal round(BigDecimal value, int scale) {
        return round(value, scale, RoundingMode.HALF_UP);
    }

    /**
     * Rounds a decimal value using {@link BigDecimal} for predictable precision.
     *
     * @param value the value to round
     * @param scale the number of digits to keep after the decimal point
     * @param mode the rounding mode to apply
     * @return the rounded value
     * @throws IllegalArgumentException if {@code scale} is negative or {@code mode} is {@code null}
     */
    public static double round(double value, int scale, RoundingMode mode) {
        if (scale < 0) {
            throw new IllegalArgumentException("scale must be >= 0");
        }
        if (mode == null) {
            throw new IllegalArgumentException("rounding mode must not be null");
        }
        return BigDecimal.valueOf(value).setScale(scale, mode).doubleValue();
    }

    /**
     * Rounds a {@link BigDecimal} value using the supplied rounding mode.
     *
     * @param value the value to round
     * @param scale the number of digits to keep after the decimal point
     * @param mode the rounding mode to apply
     * @return the rounded value
     * @throws IllegalArgumentException if {@code value} or {@code mode} is {@code null}, or
     *     {@code scale} is negative
     */
    public static BigDecimal round(BigDecimal value, int scale, RoundingMode mode) {
        if (value == null) {
            throw new IllegalArgumentException("value must not be null");
        }
        if (scale < 0) {
            throw new IllegalArgumentException("scale must be >= 0");
        }
        if (mode == null) {
            throw new IllegalArgumentException("rounding mode must not be null");
        }
        return value.setScale(scale, mode);
    }
}
