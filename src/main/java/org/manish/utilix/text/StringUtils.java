package org.manish.utilix.text;

import java.util.Objects;

/**
 * Provides common string helpers for null safety, cleanup, comparison, and simple case
 * transformations.
 */
public final class StringUtils {
    private StringUtils() {
    }

    /**
     * Determines whether a string is {@code null} or empty.
     *
     * @param value the string to inspect
     * @return {@code true} when the value is {@code null} or has zero length
     */
    public static boolean isEmpty(String value) {
        return value == null || value.isEmpty();
    }

    /**
     * Determines whether a string is {@code null}, empty, or contains only whitespace.
     *
     * @param value the string to inspect
     * @return {@code true} when the value is blank
     */
    public static boolean isBlank(String value) {
        if (value == null) {
            return true;
        }
        for (int i = 0; i < value.length(); i++) {
            if (!Character.isWhitespace(value.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Trims a string and converts the result to {@code null} when it becomes empty.
     *
     * @param value the string to normalize
     * @return the trimmed string or {@code null} when the trimmed result is empty
     */
    public static String trimToNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    /**
     * Trims a string and converts {@code null} to an empty string.
     *
     * @param value the string to normalize
     * @return the trimmed string or an empty string when the input is {@code null}
     */
    public static String trimToEmpty(String value) {
        if (value == null) {
            return "";
        }
        return value.trim();
    }

    /**
     * Converts a {@code null} reference to an empty string.
     *
     * @param value the string to normalize
     * @return the original string or an empty string when the input is {@code null}
     */
    public static String nullToEmpty(String value) {
        return value == null ? "" : value;
    }

    /**
     * Converts an empty string to {@code null}.
     *
     * @param value the string to normalize
     * @return {@code null} when the value is {@code null} or empty, otherwise the original string
     */
    public static String emptyToNull(String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        return value;
    }

    /**
     * Compares two strings with null safety.
     *
     * @param left the first string
     * @param right the second string
     * @return {@code true} when both values are equal or both are {@code null}
     */
    public static boolean safeEquals(String left, String right) {
        return Objects.equals(left, right);
    }

    /**
     * Compares two strings ignoring case with null safety.
     *
     * @param left the first string
     * @param right the second string
     * @return {@code true} when both values are equal ignoring case or both are {@code null}
     */
    public static boolean safeEqualsIgnoreCase(String left, String right) {
        if (left == null || right == null) {
            return left == right;
        }
        return left.equalsIgnoreCase(right);
    }

    /**
     * Shortens a string to the requested maximum length using {@code ...} as the suffix.
     *
     * @param value the source string
     * @param maxLen the maximum output length; must be at least 4
     * @return the abbreviated string, or the original value when it already fits within the limit
     * @throws IllegalArgumentException if {@code maxLen} is smaller than 4
     */
    public static String abbreviate(String value, int maxLen) {
        if (value == null) {
            return null;
        }
        if (maxLen < 4) {
            throw new IllegalArgumentException("maxLen must be >= 4");
        }
        if (value.length() <= maxLen) {
            return value;
        }
        return value.substring(0, maxLen - 3) + "...";
    }

    /**
     * Converts free-form text or camel-case input to snake case.
     *
     * @param input the source text
     * @return the snake-case representation, or {@code null} when the input is {@code null}
     */
    public static String toSnakeCase(String input) {
        return toDelimitedCase(input, '_');
    }

    /**
     * Converts free-form text or camel-case input to kebab case.
     *
     * @param input the source text
     * @return the kebab-case representation, or {@code null} when the input is {@code null}
     */
    public static String toKebabCase(String input) {
        return toDelimitedCase(input, '-');
    }

    private static String toDelimitedCase(String input, char delimiter) {
        if (input == null) {
            return null;
        }
        StringBuilder out = new StringBuilder();
        boolean prevWasDelimiter = true;
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (!Character.isLetterOrDigit(c)) {
                if (!prevWasDelimiter && out.length() > 0) {
                    out.append(delimiter);
                    prevWasDelimiter = true;
                }
                continue;
            }

            if (Character.isUpperCase(c)) {
                boolean prevIsLowerOrDigit = i > 0 && (Character.isLowerCase(input.charAt(i - 1))
                    || Character.isDigit(input.charAt(i - 1)));
                boolean prevIsUpper = i > 0 && Character.isUpperCase(input.charAt(i - 1));
                boolean nextIsLower = i + 1 < input.length()
                    && Character.isLowerCase(input.charAt(i + 1));
                if (!prevWasDelimiter && (prevIsLowerOrDigit || (prevIsUpper && nextIsLower))) {
                    out.append(delimiter);
                }
                out.append(Character.toLowerCase(c));
            } else {
                out.append(c);
            }
            prevWasDelimiter = false;
        }

        int len = out.length();
        if (len > 0 && out.charAt(len - 1) == delimiter) {
            out.setLength(len - 1);
        }
        return out.toString();
    }
}
