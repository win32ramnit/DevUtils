package org.manish.utilix.time;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DateTimeUtilsTest {

    @Test
    void testInstantRoundTrip() {
        Instant instant = Instant.parse("2024-01-02T03:04:05Z");
        String formatted = DateTimeUtils.formatInstant(instant);
        Instant parsed = DateTimeUtils.parseInstant(formatted);
        assertEquals(instant, parsed);
    }

    @Test
    void testEpochMillis() {
        long millis = 1700000000000L;
        Instant instant = DateTimeUtils.fromEpochMillis(millis);
        assertEquals(millis, DateTimeUtils.toEpochMillis(instant));
    }

    @Test
    void testLocalDateRoundTrip() {
        LocalDate date = LocalDate.of(2024, 1, 15);
        String formatted = DateTimeUtils.formatLocalDate(date, null);
        LocalDate parsed = DateTimeUtils.parseLocalDate(formatted, null);
        assertEquals(date, parsed);
    }
}
