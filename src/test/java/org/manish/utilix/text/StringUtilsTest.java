package org.manish.utilix.text;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StringUtilsTest {

    @Test
    void testIsBlank() {
        assertTrue(StringUtils.isBlank(null));
        assertTrue(StringUtils.isBlank(""));
        assertTrue(StringUtils.isBlank("   "));
        assertFalse(StringUtils.isBlank("a"));
    }

    @Test
    void testTrimHelpers() {
        assertNull(StringUtils.trimToNull("   "));
        assertEquals("hello", StringUtils.trimToNull("  hello "));
        assertEquals("", StringUtils.trimToEmpty(null));
        assertEquals("hi", StringUtils.trimToEmpty(" hi "));
    }

    @Test
    void testAbbreviate() {
        assertEquals("Hello", StringUtils.abbreviate("Hello", 10));
        assertEquals("Hel...", StringUtils.abbreviate("HelloWorld", 6));
        assertThrows(IllegalArgumentException.class, () -> StringUtils.abbreviate("Hello", 3));
    }

    @Test
    void testDelimitedCase() {
        assertEquals("hello_world", StringUtils.toSnakeCase("HelloWorld"));
        assertEquals("hello_world", StringUtils.toSnakeCase("hello world"));
        assertEquals("http_server", StringUtils.toSnakeCase("HTTPServer"));
        assertEquals("xml2_json_parser", StringUtils.toSnakeCase("XML2JSONParser"));
        assertEquals("hello-world", StringUtils.toKebabCase("hello_world"));
        assertEquals("hello-world", StringUtils.toKebabCase("__Hello__World__"));
    }

    @Test
    void testSafeEqualsIgnoreCase() {
        assertTrue(StringUtils.safeEqualsIgnoreCase(null, null));
        assertFalse(StringUtils.safeEqualsIgnoreCase("Test", null));
        assertTrue(StringUtils.safeEqualsIgnoreCase("Test", "test"));
    }
}
