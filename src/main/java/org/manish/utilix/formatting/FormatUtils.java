package org.manish.utilix.formatting;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Provides presentation-oriented formatting helpers for numbers and date/time values.
 * <p>
 * This class is intended for application-layer output where callers need stable string
 * representations with explicit scale, pattern, and time-zone control.
 * </p>
 */
public final class FormatUtils {
    private FormatUtils() {
    }

    /**
     * Formats a decimal value with a fixed number of fraction digits using
     * {@link RoundingMode#HALF_UP}.
     *
     * @param value the numeric value to format
     * @param scale the number of digits to keep after the decimal point; must be zero or greater
     * @return the formatted decimal string
     * @throws IllegalArgumentException if {@code scale} is negative
     */
    public static String formatDecimal(double value, int scale) {
        return formatDecimal(value, scale, RoundingMode.HALF_UP);
    }

    /**
     * Formats a decimal value with a fixed number of fraction digits using the supplied
     * rounding mode.
     *
     * @param value the numeric value to format
     * @param scale the number of digits to keep after the decimal point; must be zero or greater
     * @param mode the rounding mode to apply
     * @return the formatted decimal string with trailing zeros preserved for the requested scale
     * @throws IllegalArgumentException if {@code scale} is negative or {@code mode} is {@code null}
     */
    public static String formatDecimal(double value, int scale, RoundingMode mode) {
        if (scale < 0) {
            throw new IllegalArgumentException("scale must be >= 0");
        }
        if (mode == null) {
            throw new IllegalArgumentException("rounding mode must not be null");
        }
        return BigDecimal.valueOf(value).setScale(scale, mode).toPlainString();
    }

    /**
     * Formats a {@link LocalDate} with the provided date pattern.
     *
     * @param date the date to format
     * @param pattern the {@link DateTimeFormatter} pattern to use
     * @return the formatted date string
     * @throws NullPointerException if {@code date} is {@code null}
     * @throws IllegalArgumentException if {@code pattern} is {@code null} or blank
     */
    public static String formatLocalDate(LocalDate date, String pattern) {
        Objects.requireNonNull(date, "date");
        return date.format(DateTimeFormatter.ofPattern(requirePattern(pattern)));
    }

    /**
     * Formats an {@link Instant} in a specific time zone using the provided date-time pattern.
     *
     * @param instant the instant to format
     * @param zoneId the time zone to apply when formatting the instant
     * @param pattern the {@link DateTimeFormatter} pattern to use
     * @return the formatted date-time string
     * @throws NullPointerException if {@code instant} or {@code zoneId} is {@code null}
     * @throws IllegalArgumentException if {@code pattern} is {@code null} or blank
     */
    public static String formatInstant(Instant instant, ZoneId zoneId, String pattern) {
        Objects.requireNonNull(instant, "instant");
        Objects.requireNonNull(zoneId, "zoneId");
        return DateTimeFormatter.ofPattern(requirePattern(pattern))
            .withZone(zoneId)
            .format(instant);
    }

    private static String requirePattern(String pattern) {
        if (pattern == null || pattern.trim().isEmpty()) {
            throw new IllegalArgumentException("pattern must not be blank");
        }
        return pattern;
    }
}
