package org.manish.utilix.banking;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

/**
 * Provides precise banking-oriented calculations using {@link BigDecimal}.
 * <p>
 * These helpers avoid floating-point arithmetic for common financial operations such as
 * percentage-based charges, interest calculations, loan EMI computation, and textual amount
 * rendering.
 * </p>
 */
public final class BankingCalculations {
    /**
     * Default currency scale used by the helpers.
     */
    public static final int MONEY_SCALE = 2;

    private static final String[] SMALL_NUMBER_WORDS = {
        "Zero", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine",
        "Ten", "Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen", "Sixteen",
        "Seventeen", "Eighteen", "Nineteen"
    };
    private static final String[] TENS_WORDS = {
        "", "", "Twenty", "Thirty", "Forty", "Fifty", "Sixty", "Seventy", "Eighty", "Ninety"
    };
    private static final BigInteger[] SCALE_VALUES = {
        new BigInteger("100000000000000000"),
        new BigInteger("1000000000000000"),
        new BigInteger("10000000000000"),
        new BigInteger("100000000000"),
        new BigInteger("1000000000"),
        new BigInteger("10000000"),
        new BigInteger("100000"),
        new BigInteger("1000"),
        new BigInteger("100")
    };
    private static final String[] SCALE_WORDS = {
        "Shankh",
        "Padma",
        "Neel",
        "Kharab",
        "Arab",
        "Crore",
        "Lakh",
        "Thousand",
        "Hundred"
    };
    private static final BigDecimal ONE_HUNDRED = BigDecimal.valueOf(100);
    private static final BigDecimal MONTHS_IN_YEAR = BigDecimal.valueOf(12);
    private static final int INTERNAL_SCALE = 16;

    private BankingCalculations() {
    }

    /**
     * Rounds an amount to standard currency scale using {@link RoundingMode#HALF_UP}.
     *
     * @param amount the monetary amount to round
     * @return the rounded amount
     * @throws IllegalArgumentException if {@code amount} is {@code null}
     */
    public static BigDecimal toMoney(BigDecimal amount) {
        requireAmount(amount, "amount");
        return amount.setScale(MONEY_SCALE, RoundingMode.HALF_UP);
    }

    /**
     * Calculates a percentage of an amount using standard currency scale.
     *
     * @param amount the base amount
     * @param ratePercent the percentage rate, expressed as a percent value such as {@code 7.5}
     * @return the computed percentage amount rounded to currency scale
     * @throws IllegalArgumentException if any argument is {@code null}
     */
    public static BigDecimal percentageOf(BigDecimal amount, BigDecimal ratePercent) {
        return percentageOf(amount, ratePercent, MONEY_SCALE, RoundingMode.HALF_UP);
    }

    /**
     * Calculates a percentage of an amount.
     *
     * @param amount the base amount
     * @param ratePercent the percentage rate, expressed as a percent value such as {@code 7.5}
     * @param scale the result scale
     * @param mode the rounding mode to apply
     * @return the computed percentage amount
     * @throws IllegalArgumentException if any argument is invalid
     */
    public static BigDecimal percentageOf(
        BigDecimal amount,
        BigDecimal ratePercent,
        int scale,
        RoundingMode mode
    ) {
        requireAmount(amount, "amount");
        requireAmount(ratePercent, "ratePercent");
        requireScale(scale);
        requireMode(mode);
        return amount.multiply(ratePercent)
            .divide(ONE_HUNDRED, scale, mode);
    }

    /**
     * Adds a percentage-based charge or markup to an amount.
     *
     * @param amount the base amount
     * @param ratePercent the percentage rate to add
     * @return the amount after the percentage is added, rounded to currency scale
     * @throws IllegalArgumentException if any argument is {@code null}
     */
    public static BigDecimal addPercentage(BigDecimal amount, BigDecimal ratePercent) {
        requireAmount(amount, "amount");
        return toMoney(amount.add(percentageOf(amount, ratePercent)));
    }

    /**
     * Subtracts a percentage-based discount or deduction from an amount.
     *
     * @param amount the base amount
     * @param ratePercent the percentage rate to subtract
     * @return the amount after the percentage is subtracted, rounded to currency scale
     * @throws IllegalArgumentException if any argument is {@code null}
     */
    public static BigDecimal subtractPercentage(BigDecimal amount, BigDecimal ratePercent) {
        requireAmount(amount, "amount");
        return toMoney(amount.subtract(percentageOf(amount, ratePercent)));
    }

    /**
     * Calculates simple interest using the formula {@code principal * rate * time / 100}.
     *
     * @param principal the principal amount
     * @param annualRatePercent the annual interest rate as a percent value
     * @param timeInYears the time duration in years; fractional years are allowed
     * @return the simple interest amount rounded to currency scale
     * @throws IllegalArgumentException if any argument is invalid
     */
    public static BigDecimal simpleInterest(
        BigDecimal principal,
        BigDecimal annualRatePercent,
        BigDecimal timeInYears
    ) {
        requireNonNegative(principal, "principal");
        requireNonNegative(annualRatePercent, "annualRatePercent");
        requireNonNegative(timeInYears, "timeInYears");

        BigDecimal interest = principal.multiply(annualRatePercent)
            .multiply(timeInYears)
            .divide(ONE_HUNDRED, MONEY_SCALE, RoundingMode.HALF_UP);
        return toMoney(interest);
    }

    /**
     * Calculates compound maturity amount using
     * {@code principal * (1 + annualRate / (100 * compoundsPerYear))^(compoundsPerYear * years)}.
     *
     * @param principal the principal amount
     * @param annualRatePercent the annual interest rate as a percent value
     * @param compoundsPerYear the number of compounding periods in a year
     * @param years the number of years
     * @return the maturity amount rounded to currency scale
     * @throws IllegalArgumentException if any argument is invalid
     */
    public static BigDecimal compoundAmount(
        BigDecimal principal,
        BigDecimal annualRatePercent,
        int compoundsPerYear,
        int years
    ) {
        requireNonNegative(principal, "principal");
        requireNonNegative(annualRatePercent, "annualRatePercent");
        requirePositive(compoundsPerYear, "compoundsPerYear");
        requirePositive(years, "years");

        BigDecimal periodRate = annualRatePercent.divide(
            ONE_HUNDRED.multiply(BigDecimal.valueOf(compoundsPerYear)),
            INTERNAL_SCALE,
            RoundingMode.HALF_UP
        );
        BigDecimal growthFactor = BigDecimal.ONE.add(periodRate);
        BigDecimal maturityAmount = principal.multiply(growthFactor.pow(compoundsPerYear * years));
        return toMoney(maturityAmount);
    }

    /**
     * Calculates compound interest as maturity amount minus principal.
     *
     * @param principal the principal amount
     * @param annualRatePercent the annual interest rate as a percent value
     * @param compoundsPerYear the number of compounding periods in a year
     * @param years the number of years
     * @return the compound interest amount rounded to currency scale
     * @throws IllegalArgumentException if any argument is invalid
     */
    public static BigDecimal compoundInterest(
        BigDecimal principal,
        BigDecimal annualRatePercent,
        int compoundsPerYear,
        int years
    ) {
        return toMoney(compoundAmount(principal, annualRatePercent, compoundsPerYear, years)
            .subtract(principal));
    }

    /**
     * Calculates the Equated Monthly Installment (EMI) for a reducing-balance loan.
     *
     * @param principal the loan amount
     * @param annualRatePercent the annual interest rate as a percent value
     * @param tenureMonths the repayment tenure in months
     * @return the EMI rounded to currency scale
     * @throws IllegalArgumentException if any argument is invalid
     */
    public static BigDecimal emi(
        BigDecimal principal,
        BigDecimal annualRatePercent,
        int tenureMonths
    ) {
        requireNonNegative(principal, "principal");
        requireNonNegative(annualRatePercent, "annualRatePercent");
        requirePositive(tenureMonths, "tenureMonths");

        if (principal.signum() == 0) {
            return BigDecimal.ZERO.setScale(MONEY_SCALE, RoundingMode.HALF_UP);
        }

        BigDecimal monthlyRate = annualRatePercent.divide(
            ONE_HUNDRED.multiply(MONTHS_IN_YEAR),
            INTERNAL_SCALE,
            RoundingMode.HALF_UP
        );

        if (monthlyRate.signum() == 0) {
            return principal.divide(BigDecimal.valueOf(tenureMonths), MONEY_SCALE, RoundingMode.HALF_UP);
        }

        BigDecimal onePlusRatePower = BigDecimal.ONE.add(monthlyRate).pow(tenureMonths);
        BigDecimal numerator = principal.multiply(monthlyRate).multiply(onePlusRatePower);
        BigDecimal denominator = onePlusRatePower.subtract(BigDecimal.ONE);

        return numerator.divide(denominator, MONEY_SCALE, RoundingMode.HALF_UP);
    }

    /**
     * Calculates total repayment for a fixed EMI loan.
     *
     * @param emi the monthly installment amount
     * @param tenureMonths the repayment tenure in months
     * @return the total repayment rounded to currency scale
     * @throws IllegalArgumentException if any argument is invalid
     */
    public static BigDecimal totalRepayment(BigDecimal emi, int tenureMonths) {
        requireNonNegative(emi, "emi");
        requirePositive(tenureMonths, "tenureMonths");
        return toMoney(emi.multiply(BigDecimal.valueOf(tenureMonths)));
    }

    /**
     * Calculates total interest paid across the full loan tenure.
     *
     * @param principal the original loan principal
     * @param emi the monthly installment amount
     * @param tenureMonths the repayment tenure in months
     * @return the total interest paid rounded to currency scale
     * @throws IllegalArgumentException if any argument is invalid
     */
    public static BigDecimal totalInterest(BigDecimal principal, BigDecimal emi, int tenureMonths) {
        requireNonNegative(principal, "principal");
        return toMoney(totalRepayment(emi, tenureMonths).subtract(principal));
    }

    /**
     * Converts a decimal value to words using the Indian numbering system.
     * <p>
     * Fractional digits are rendered digit by digit after the word {@code Point}. For example,
     * {@code 123.45} becomes {@code One Hundred Twenty Three Point Four Five}.
     * </p>
     *
     * @param value the decimal value to convert
     * @return the textual representation of the supplied value
     * @throws IllegalArgumentException if {@code value} is {@code null}
     */
    public static String decimalToWords(BigDecimal value) {
        requireAmount(value, "value");

        BigDecimal normalized = value.stripTrailingZeros();
        boolean isNegative = normalized.signum() < 0;
        String plain = normalized.abs().toPlainString();
        String[] parts = plain.split("\\.");

        StringBuilder out = new StringBuilder();
        if (isNegative) {
            out.append("Minus ");
        }
        out.append(convertWholeNumber(new BigInteger(parts[0])));

        if (parts.length > 1) {
            out.append(" Point");
            for (int i = 0; i < parts[1].length(); i++) {
                int digit = parts[1].charAt(i) - '0';
                out.append(' ').append(SMALL_NUMBER_WORDS[digit]);
            }
        }
        return out.toString();
    }

    /**
     * Converts a monetary amount to words using rupee and paisa labels.
     * <p>
     * The amount is first rounded to standard currency scale using {@link #toMoney(BigDecimal)}.
     * For example, {@code 123.45} becomes
     * {@code One Hundred Twenty Three Rupees and Forty Five Paise Only}.
     * </p>
     *
     * @param amount the monetary amount to convert
     * @return the amount expressed in words
     * @throws IllegalArgumentException if {@code amount} is {@code null}
     */
    public static String amountToWords(BigDecimal amount) {
        requireAmount(amount, "amount");

        BigDecimal normalized = toMoney(amount.abs());
        BigInteger wholePart = normalized.toBigInteger();
        int fractionPart = normalized.remainder(BigDecimal.ONE)
            .movePointRight(MONEY_SCALE)
            .intValueExact();

        StringBuilder out = new StringBuilder();
        if (amount.signum() < 0) {
            out.append("Minus ");
        }

        out.append(convertWholeNumber(wholePart))
            .append(wholePart.equals(BigInteger.ONE) ? " Rupee" : " Rupees");

        if (fractionPart > 0) {
            out.append(" and ")
                .append(convertWholeNumber(BigInteger.valueOf(fractionPart)))
                .append(fractionPart == 1 ? " Paisa" : " Paise");
        }

        out.append(" Only");
        return out.toString();
    }

    private static void requireAmount(BigDecimal value, String name) {
        if (value == null) {
            throw new IllegalArgumentException(name + " must not be null");
        }
    }

    private static void requireNonNegative(BigDecimal value, String name) {
        requireAmount(value, name);
        if (value.signum() < 0) {
            throw new IllegalArgumentException(name + " must be >= 0");
        }
    }

    private static void requirePositive(int value, String name) {
        if (value <= 0) {
            throw new IllegalArgumentException(name + " must be > 0");
        }
    }

    private static void requireScale(int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("scale must be >= 0");
        }
    }

    private static void requireMode(RoundingMode mode) {
        if (mode == null) {
            throw new IllegalArgumentException("rounding mode must not be null");
        }
    }

    private static String convertWholeNumber(BigInteger value) {
        if (value.signum() == 0) {
            return SMALL_NUMBER_WORDS[0];
        }

        for (int i = 0; i < SCALE_VALUES.length; i++) {
            BigInteger scale = SCALE_VALUES[i];
            if (value.compareTo(scale) >= 0) {
                BigInteger[] division = value.divideAndRemainder(scale);
                StringBuilder out = new StringBuilder();
                out.append(convertWholeNumber(division[0])).append(' ').append(SCALE_WORDS[i]);
                if (division[1].signum() > 0) {
                    out.append(' ').append(convertWholeNumber(division[1]));
                }
                return out.toString();
            }
        }

        int intValue = value.intValue();
        if (intValue < 20) {
            return SMALL_NUMBER_WORDS[intValue];
        }

        int tens = intValue / 10;
        int units = intValue % 10;
        return units == 0 ? TENS_WORDS[tens] : TENS_WORDS[tens] + ' ' + SMALL_NUMBER_WORDS[units];
    }
}
