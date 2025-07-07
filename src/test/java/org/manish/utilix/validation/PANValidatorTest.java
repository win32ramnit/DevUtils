package org.manish.utilix.validation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class PANValidatorTest {

    @Test
    public void testValidPAN() {
        assertTrue(PANValidator.isValidPAN("ABCDE1234F")); // Standard valid PAN
        assertTrue(PANValidator.isValidPAN("ABCDE0001F")); // PAN with 0001 as digits
        assertTrue(PANValidator.isValidPAN("ABCDE9999F")); // PAN with 9999 as digits
    }

    @Test
    public void testInvalidPAN() {
        assertFalse(PANValidator.isValidPAN("abcde1234f")); // Lowercase letters
        assertFalse(PANValidator.isValidPAN("ABCDE12345")); // More than 10 characters
        assertFalse(PANValidator.isValidPAN("ABCDE1234g")); // Invalid last character
        assertFalse(PANValidator.isValidPAN("ABCDE1234"));  // Missing last character
        assertFalse(PANValidator.isValidPAN("ABCDE1234F ")); // Trailing space
        assertFalse(PANValidator.isValidPAN(" ABCDE1234F")); // Leading space
        assertFalse(PANValidator.isValidPAN("ABCDE 1234F")); // Space in the middle
        assertFalse(PANValidator.isValidPAN("ABCDE1234F!")); // Special character
        assertFalse(PANValidator.isValidPAN("ABCDE1234"));   // Missing last character
        assertFalse(PANValidator.isValidPAN("ABCDE1234F123")); // More than 10 characters
    }

    @Test
    public void testEdgeCases() {
//        assertFalse(PANValidator.isValidPAN("ABCDE0000F")); // Invalid digits (0000)
        assertFalse(PANValidator.isValidPAN("ABCDE1234F1")); // Extra character at the end
        assertFalse(PANValidator.isValidPAN("ABCDE1234F@")); // Special character
        assertFalse(PANValidator.isValidPAN("ABCDE1234F ")); // Trailing space
        assertFalse(PANValidator.isValidPAN("ABCDE1234F"));  // Valid case
    }

    @ParameterizedTest
    @CsvSource({
        "ABCDE1234F, true",
        "abcde1234f, false",
        "ABCDE12345, false",
        "ABCDE1234G, true",
        "ABCDE1234, false",
        "ABCDE1234Fd, false",
        " ABCDE1234F, false",
        "ABCDE 1234F, false",
        "ABCDE1234F!, false",
        "ABCDE1234F123, false"
    })
    void testPANValidation(String pan, boolean expected) {
        assertEquals(expected, PANValidator.isValidPAN(pan));
    }


}
