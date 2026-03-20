package org.manish.utilix.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ConfigUtilsTest {

    @Test
    void testPropertyLookup() {
        System.setProperty("devutils.test.key", "value");
        try {
            assertEquals("value", ConfigUtils.getEnvOrProperty("devutils.test.key"));
            assertEquals("fallback", ConfigUtils.getEnvOrProperty("devutils.missing.key", "fallback"));
            assertEquals("value", ConfigUtils.getRequiredEnvOrProperty("devutils.test.key"));
        } finally {
            System.clearProperty("devutils.test.key");
        }
    }

    @Test
    void testBooleanParsing() {
        System.setProperty("devutils.bool.key", "yes");
        try {
            assertTrue(ConfigUtils.getBooleanEnvOrProperty("devutils.bool.key", false));
            assertFalse(ConfigUtils.getBooleanEnvOrProperty("devutils.bool.missing", false));
        } finally {
            System.clearProperty("devutils.bool.key");
        }
    }

    @Test
    void testIntParsing() {
        System.setProperty("devutils.int.key", "42");
        try {
            assertEquals(42, ConfigUtils.getIntEnvOrProperty("devutils.int.key", 0));
            assertEquals(7, ConfigUtils.getIntEnvOrProperty("devutils.int.missing", 7));
        } finally {
            System.clearProperty("devutils.int.key");
        }
    }

    @Test
    void testInvalidConfig() {
        System.setProperty("devutils.bad.bool", "maybe");
        System.setProperty("devutils.bad.int", "abc");
        try {
            assertThrows(IllegalArgumentException.class, () -> ConfigUtils.getBooleanEnvOrProperty("devutils.bad.bool", false));
            assertThrows(IllegalArgumentException.class, () -> ConfigUtils.getIntEnvOrProperty("devutils.bad.int", 0));
            assertThrows(IllegalStateException.class, () -> ConfigUtils.getRequiredEnvOrProperty("devutils.required.missing"));
        } finally {
            System.clearProperty("devutils.bad.bool");
            System.clearProperty("devutils.bad.int");
        }
    }
}
