package org.manish.utilix.conversion;

import org.manish.utilix.numbers.NumberUtils;

/**
 * Legacy rounding helper preserved for backward compatibility.
 *
 * @deprecated Use {@link NumberUtils#round(double, int)} instead.
 */
@Deprecated
public final class RoundDouble {
    private RoundDouble() {
    }

    /**
     * Runs a simple console demonstration of rounding behavior.
     *
     * @param args command-line arguments; not used
     */
    public static void main(String[] args) {
        double value = 10.34567;
        System.out.println("round(value, 2) = " + round(value, 2));
        System.out.println("round(value, 3) = " + round(value, 3));
    }

    /**
     * Rounds a double to the requested number of decimal places.
     *
     * @param value the value to round
     * @param places the number of digits to keep after the decimal point
     * @return the rounded value
     * @throws IllegalArgumentException if {@code places} is negative
     * @deprecated Use {@link NumberUtils#round(double, int)} instead.
     */
    @Deprecated
    public static double round(double value, int places) {
        return NumberUtils.round(value, places);
    }
}
