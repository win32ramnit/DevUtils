package org.manish.utilix.validation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EmailValidatorTest {

    @ParameterizedTest
    @ValueSource(strings = {
        "user@example.com",
        "user.name+tag@example.co.in",
        "USER_123@example.org",
        "customer-service@sub.domain.net"
    })
    void testValidEmails(String email) {
        assertTrue(EmailValidator.isValidEmail(email));
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "",
        "userexample.com",
        "user@@example.com",
        ".user@example.com",
        "user.@example.com",
        "user..name@example.com",
        "user@example",
        "user@-example.com",
        "user@example..com",
        " user@example.com",
        "user@example.com "
    })
    void testInvalidEmails(String email) {
        assertFalse(EmailValidator.isValidEmail(email));
    }

    @Test
    void testNullEmail() {
        assertThrows(IllegalArgumentException.class, () -> EmailValidator.isValidEmail(null));
    }
}
