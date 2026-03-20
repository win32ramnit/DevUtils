package org.manish.utilix.crypto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class HashUtilsTest {

    @Test
    void testSha256() {
        assertEquals(
            "2cf24dba5fb0a30e26e83b2ac5b9e29e1b161e5c1fa7425e73043362938b9824",
            HashUtils.sha256Hex("hello")
        );
    }

    @Test
    void testSha512() {
        assertEquals(
            "9b71d224bd62f3785d96d46ad3ea3d73319bfbc2890caadae2dff72519673ca72323c3d99ba5c11d7c7acc6e14b8c5da0c4663475c2e5c3adef46f73bcdec043",
            HashUtils.sha512Hex("hello")
        );
    }

    @Test
    void testHmacSha256() {
        assertEquals(
            "f7bc83f430538424b13298e6aa6fb143ef4d59a14946175997479dbc2d1a3cd8",
            HashUtils.hmacSha256Hex("key", "The quick brown fox jumps over the lazy dog")
        );
    }

    @Test
    void testNullInputs() {
        assertThrows(IllegalArgumentException.class, () -> HashUtils.sha256Hex(null));
        assertThrows(IllegalArgumentException.class, () -> HashUtils.hmacSha256Hex(null, "x"));
        assertThrows(IllegalArgumentException.class, () -> HashUtils.hmacSha256Hex("x", null));
    }
}
