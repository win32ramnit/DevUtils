package org.manish.utilix.text;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class MaskingUtilsTest {

    @Test
    void testMask() {
        assertEquals("AB*****YZ", MaskingUtils.mask("ABCDEFGYZ", 2, 2, '*'));
        assertEquals("ABC", MaskingUtils.mask("ABC", 2, 1, '*'));
        assertNull(MaskingUtils.mask(null, 1, 1, '*'));
    }

    @Test
    void testMaskEmail() {
        assertEquals("u**r@example.com", MaskingUtils.maskEmail("user@example.com"));
        assertEquals("a*@example.com", MaskingUtils.maskEmail("ab@example.com"));
        assertEquals("*", MaskingUtils.maskEmail("a"));
    }

    @Test
    void testMaskPhoneAndAccount() {
        assertEquals("******3210", MaskingUtils.maskMobileNumber("9876543210"));
        assertEquals("************3456", MaskingUtils.maskAccountNumber("1234567890123456"));
        assertEquals("+** ***** *3210", MaskingUtils.maskMobileNumber("+91 98765 43210"));
    }
}


