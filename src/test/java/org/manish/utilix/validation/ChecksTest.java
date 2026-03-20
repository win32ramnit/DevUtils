package org.manish.utilix.validation;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ChecksTest {

    @Test
    void testNotNull() {
        Object value = new Object();
        assertNotNull(Checks.notNull(value, "value"));
        assertThrows(IllegalArgumentException.class, () -> Checks.notNull(null, "value"));
    }

    @Test
    void testNotBlank() {
        assertEquals("abc", Checks.notBlank("abc", "value"));
        assertThrows(IllegalArgumentException.class, () -> Checks.notBlank("   ", "value"));
        assertThrows(IllegalArgumentException.class, () -> Checks.notBlank(null, "value"));
    }

    @Test
    void testPositiveAndNonNegative() {
        assertEquals(5, Checks.positive(5, "value"));
        assertThrows(IllegalArgumentException.class, () -> Checks.positive(0, "value"));

        assertEquals(0L, Checks.nonNegative(0L, "value"));
        assertThrows(IllegalArgumentException.class, () -> Checks.nonNegative(-1L, "value"));
    }
}
