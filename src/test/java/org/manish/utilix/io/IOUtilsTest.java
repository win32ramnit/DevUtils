package org.manish.utilix.io;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class IOUtilsTest {

    @Test
    void testToByteArray() throws Exception {
        byte[] data = "hello".getBytes(StandardCharsets.UTF_8);
        ByteArrayInputStream input = new ByteArrayInputStream(data);
        assertArrayEquals(data, IOUtils.toByteArray(input));
    }

    @Test
    void testToString() throws Exception {
        byte[] data = "world".getBytes(StandardCharsets.UTF_8);
        ByteArrayInputStream input = new ByteArrayInputStream(data);
        assertEquals("world", IOUtils.toString(input, StandardCharsets.UTF_8));
    }
}
