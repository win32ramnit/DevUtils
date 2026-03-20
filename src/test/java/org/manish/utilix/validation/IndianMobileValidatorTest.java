package org.manish.utilix.validation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class IndianMobileValidatorTest {

    @ParameterizedTest
    @ValueSource(strings = {
        "9876543210",
        "+919876543210",
        "919876543210",
        "09876543210",
        "+91 98765 43210",
        "98765-43210"
    })
    void testValidMobileNumbers(String mobileNumber) {
        assertTrue(IndianMobileValidator.isValidMobileNumber(mobileNumber));
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "",
        "5876543210",
        "987654321",
        "98765432101",
        "+119876543210",
        "98765A3210",
        "12345-67890"
    })
    void testInvalidMobileNumbers(String mobileNumber) {
        assertFalse(IndianMobileValidator.isValidMobileNumber(mobileNumber));
    }

    @Test
    void testNullMobileNumber() {
        assertThrows(
            IllegalArgumentException.class,
            () -> IndianMobileValidator.isValidMobileNumber(null)
        );
    }
}
