package org.manish.utilix.conversion;

import org.manish.utilix.numbers.NumberUtils;

/**
 * Small example program showing the recommended numeric rounding approach.
 */
public final class PrecisionExample {
    private PrecisionExample() {
    }

    /**
     * Runs a console example of the shared rounding utility.
     *
     * @param args command-line arguments; not used
     */
    public static void main(String[] args) {
        double value = 10.34567;
        double roundedValue = NumberUtils.round(value, 2);
        System.out.println("roundedValue = " + roundedValue);
    }
}
