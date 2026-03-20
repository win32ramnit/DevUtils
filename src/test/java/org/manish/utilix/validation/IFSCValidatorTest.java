package org.manish.utilix.validation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class IFSCValidatorTest {

    @ParameterizedTest
    @ValueSource(strings = {
        "SBIN0001234",
        "HDFC0005678",
        "icic0ab1234"
    })
    void testValidIFSC(String ifsc) {
        assertTrue(IFSCValidator.isValidIFSC(ifsc));
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "",
        "SBIN001234",
        "SBI10001234",
        "SBIN1001234",
        "SBIN0@12345",
        " SBIN0001234",
        "SBIN0001234 "
    })
    void testInvalidIFSC(String ifsc) {
        assertFalse(IFSCValidator.isValidIFSC(ifsc));
    }

    @Test
    void testNullIFSC() {
        assertThrows(IllegalArgumentException.class, () -> IFSCValidator.isValidIFSC(null));
    }
}
