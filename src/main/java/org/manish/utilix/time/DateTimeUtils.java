package org.manish.utilix.time;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Provides helpers for working with {@code java.time} types in a consistent UTC-first way.
 */
public final class DateTimeUtils {
    private static final ZoneId UTC = ZoneOffset.UTC;

    private DateTimeUtils() {
    }

    /**
     * Returns the current instant from the system clock.
     *
     * @return the current {@link Instant}
     */
    public static Instant nowUtc() {
        return Instant.now();
    }

    /**
     * Returns the current date-time in the UTC time zone.
     *
     * @return the current UTC {@link ZonedDateTime}
     */
    public static ZonedDateTime nowUtcZoned() {
        return ZonedDateTime.now(UTC);
    }

    /**
     * Formats an instant using the ISO-8601 instant representation.
     *
     * @param instant the instant to format
     * @return the ISO-8601 string representation
     * @throws NullPointerException if {@code instant} is {@code null}
     */
    public static String formatInstant(Instant instant) {
        Objects.requireNonNull(instant, "instant");
        return DateTimeFormatter.ISO_INSTANT.format(instant);
    }

    /**
     * Parses an ISO-8601 instant string.
     *
     * @param isoInstant the ISO-8601 text to parse
     * @return the parsed {@link Instant}
     * @throws NullPointerException if {@code isoInstant} is {@code null}
     */
    public static Instant parseInstant(String isoInstant) {
        Objects.requireNonNull(isoInstant, "isoInstant");
        return Instant.parse(isoInstant);
    }

    /**
     * Converts an instant to epoch milliseconds.
     *
     * @param instant the instant to convert
     * @return the epoch millisecond value
     * @throws NullPointerException if {@code instant} is {@code null}
     */
    public static long toEpochMillis(Instant instant) {
        Objects.requireNonNull(instant, "instant");
        return instant.toEpochMilli();
    }

    /**
     * Creates an instant from an epoch millisecond value.
     *
     * @param epochMillis the epoch millisecond value
     * @return the corresponding {@link Instant}
     */
    public static Instant fromEpochMillis(long epochMillis) {
        return Instant.ofEpochMilli(epochMillis);
    }

    /**
     * Formats a local date using the supplied formatter or ISO local date when no formatter is
     * provided.
     *
     * @param date the date to format
     * @param formatter the formatter to apply; defaults to ISO local date when {@code null}
     * @return the formatted date string
     * @throws NullPointerException if {@code date} is {@code null}
     */
    public static String formatLocalDate(LocalDate date, DateTimeFormatter formatter) {
        Objects.requireNonNull(date, "date");
        DateTimeFormatter useFormatter = formatter == null
            ? DateTimeFormatter.ISO_LOCAL_DATE
            : formatter;
        return date.format(useFormatter);
    }

    /**
     * Parses a local date using the supplied formatter or ISO local date when no formatter is
     * provided.
     *
     * @param value the text to parse
     * @param formatter the formatter to apply; defaults to ISO local date when {@code null}
     * @return the parsed {@link LocalDate}
     * @throws NullPointerException if {@code value} is {@code null}
     */
    public static LocalDate parseLocalDate(String value, DateTimeFormatter formatter) {
        Objects.requireNonNull(value, "value");
        DateTimeFormatter useFormatter = formatter == null
            ? DateTimeFormatter.ISO_LOCAL_DATE
            : formatter;
        return LocalDate.parse(value, useFormatter);
    }
}
