package org.manish.utilix.config;

/**
 * Provides helpers for reading configuration from environment variables and system properties.
 */
public final class ConfigUtils {
    private ConfigUtils() {
    }

    /**
     * Returns a value from the environment first and system properties second.
     *
     * @param key the configuration key to look up
     * @return the resolved value, or {@code null} when no value is found
     * @throws IllegalArgumentException if {@code key} is {@code null} or blank
     */
    public static String getEnvOrProperty(String key) {
        requireKey(key);
        String envValue = System.getenv(key);
        if (envValue != null && !envValue.trim().isEmpty()) {
            return envValue;
        }
        return System.getProperty(key);
    }

    /**
     * Returns a value from the environment first and system properties second, falling back to a
     * default when missing.
     *
     * @param key the configuration key to look up
     * @param defaultValue the value to return when the key is not configured
     * @return the resolved value or {@code defaultValue}
     * @throws IllegalArgumentException if {@code key} is {@code null} or blank
     */
    public static String getEnvOrProperty(String key, String defaultValue) {
        String value = getEnvOrProperty(key);
        return value == null ? defaultValue : value;
    }

    /**
     * Returns a required value from the environment first and system properties second.
     *
     * @param key the configuration key to look up
     * @return the resolved non-blank value
     * @throws IllegalArgumentException if {@code key} is {@code null} or blank
     * @throws IllegalStateException if no value is configured
     */
    public static String getRequiredEnvOrProperty(String key) {
        String value = getEnvOrProperty(key);
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalStateException(key + " is not configured");
        }
        return value;
    }

    /**
     * Reads a boolean configuration value from the environment first and system properties second.
     * Accepted true values are {@code true}, {@code 1}, {@code yes}, and {@code on}. Accepted
     * false values are {@code false}, {@code 0}, {@code no}, and {@code off}.
     *
     * @param key the configuration key to look up
     * @param defaultValue the value to return when the key is not configured
     * @return the resolved boolean value
     * @throws IllegalArgumentException if the key is blank or the configured value cannot be parsed
     */
    public static boolean getBooleanEnvOrProperty(String key, boolean defaultValue) {
        String value = getEnvOrProperty(key);
        if (value == null || value.trim().isEmpty()) {
            return defaultValue;
        }

        String normalized = value.trim().toLowerCase();
        if ("true".equals(normalized) || "1".equals(normalized)
            || "yes".equals(normalized) || "on".equals(normalized)) {
            return true;
        }
        if ("false".equals(normalized) || "0".equals(normalized)
            || "no".equals(normalized) || "off".equals(normalized)) {
            return false;
        }
        throw new IllegalArgumentException(key + " must be a valid boolean value");
    }

    /**
     * Reads an integer configuration value from the environment first and system properties second.
     *
     * @param key the configuration key to look up
     * @param defaultValue the value to return when the key is not configured
     * @return the resolved integer value
     * @throws IllegalArgumentException if the key is blank or the configured value cannot be parsed
     */
    public static int getIntEnvOrProperty(String key, int defaultValue) {
        String value = getEnvOrProperty(key);
        if (value == null || value.trim().isEmpty()) {
            return defaultValue;
        }

        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(key + " must be a valid integer", ex);
        }
    }

    private static void requireKey(String key) {
        if (key == null || key.trim().isEmpty()) {
            throw new IllegalArgumentException("key must not be blank");
        }
    }
}
