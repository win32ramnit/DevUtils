package org.manish.utilix.numbers;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class NumberUtilsTest {

    @Test
    void testParseNumbers() {
        assertEquals(12, NumberUtils.parseInt(" 12 ", 0));
        assertEquals(5, NumberUtils.parseInt("x", 5));
        assertNull(NumberUtils.parseInt(null, null));

        assertEquals(12L, NumberUtils.parseLong("12", 0L));
        assertEquals(7L, NumberUtils.parseLong("abc", 7L));

        assertEquals(10.5, NumberUtils.parseDouble("10.5", 0.0), 0.0001);
        assertEquals(1.25, NumberUtils.parseDouble("bad", 1.25), 0.0001);

        assertEquals(new BigDecimal("123.45"), NumberUtils.parseBigDecimal(" 123.45 ", BigDecimal.ZERO));
        assertEquals(new BigDecimal("9.99"), NumberUtils.parseBigDecimal("bad", new BigDecimal("9.99")));
    }

    @Test
    void testClamp() {
        assertEquals(5, NumberUtils.clamp(5, 0, 10));
        assertEquals(0, NumberUtils.clamp(-1, 0, 10));
        assertEquals(10, NumberUtils.clamp(11, 0, 10));
        assertThrows(IllegalArgumentException.class, () -> NumberUtils.clamp(1, 5, 2));
    }

    @Test
    void testRound() {
        assertEquals(10.35, NumberUtils.round(10.345, 2), 0.000001);
        assertEquals(10.34, NumberUtils.round(10.345, 2, RoundingMode.DOWN), 0.000001);
        assertEquals(new BigDecimal("10.35"), NumberUtils.round(new BigDecimal("10.345"), 2));
        assertEquals(
            new BigDecimal("10.34"),
            NumberUtils.round(new BigDecimal("10.349"), 2, RoundingMode.DOWN)
        );
    }
}
