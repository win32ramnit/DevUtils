package org.manish.utilix.formatting;

import org.junit.jupiter.api.Test;

import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FormatUtilsTest {

    @Test
    void testFormatDecimal() {
        assertEquals("10.35", FormatUtils.formatDecimal(10.345, 2));
        assertEquals("10.34", FormatUtils.formatDecimal(10.349, 2, RoundingMode.DOWN));
    }

    @Test
    void testFormatLocalDate() {
        assertEquals("15/01/2024", FormatUtils.formatLocalDate(LocalDate.of(2024, 1, 15), "dd/MM/yyyy"));
    }

    @Test
    void testFormatInstant() {
        Instant instant = Instant.parse("2024-01-15T10:30:00Z");
        assertEquals("2024-01-15 10:30", FormatUtils.formatInstant(instant, ZoneOffset.UTC, "yyyy-MM-dd HH:mm"));
    }
}
