package org.manish.utilix.conversion;

public class RoundDouble {
    public static void main(String[] args) {
        double value = 10.34567;

        // Rounding to two decimal places
        double roundedValue = Math.round(value * 100.0) / 100.0;
        System.out.println("roundedValue = " + roundedValue);
        System.out.println("round(value, 3) = " + round(value, 3));
    }

    // Rounding to any decimal places
    public static double round(double value, int places) {
        double scale = Math.pow(10, places);
        return Math.round(value * scale) / scale;
    }
}
