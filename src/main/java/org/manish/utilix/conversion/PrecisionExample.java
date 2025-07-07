package org.manish.utilix.conversion;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Recommended: Use <b>BigDecimal</b> for Precision and Rounding
 */
public class PrecisionExample {
    public static void main(String[] args) {
        double value = 10.34567;
        // convert to BigDecimal
        BigDecimal bd = BigDecimal.valueOf(value);

        // round to 2 decimal places
        bd = bd.setScale(2, RoundingMode.HALF_UP);

        // get the rounded value as double
        double roundedValue = bd.doubleValue();

        System.out.println("rounded Value = " + roundedValue);
    }
}
