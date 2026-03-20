package org.manish.utilix.banking;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BankingCalculationsTest {

    @Test
    void testPercentageHelpers() {
        BigDecimal amount = new BigDecimal("25000.00");
        BigDecimal rate = new BigDecimal("1.25");

        assertEquals(new BigDecimal("312.50"), BankingCalculations.percentageOf(amount, rate));
        assertEquals(new BigDecimal("25312.50"), BankingCalculations.addPercentage(amount, rate));
        assertEquals(new BigDecimal("24687.50"), BankingCalculations.subtractPercentage(amount, rate));
    }

    @Test
    void testSimpleInterest() {
        BigDecimal interest = BankingCalculations.simpleInterest(
            new BigDecimal("100000.00"),
            new BigDecimal("7.5"),
            new BigDecimal("1.5")
        );

        assertEquals(new BigDecimal("11250.00"), interest);
    }

    @Test
    void testCompoundCalculations() {
        BigDecimal amount = BankingCalculations.compoundAmount(
            new BigDecimal("100000.00"),
            new BigDecimal("8.0"),
            4,
            2
        );
        BigDecimal interest = BankingCalculations.compoundInterest(
            new BigDecimal("100000.00"),
            new BigDecimal("8.0"),
            4,
            2
        );

        assertEquals(new BigDecimal("117165.94"), amount);
        assertEquals(new BigDecimal("17165.94"), interest);
    }

    @Test
    void testEmiAndLoanSummary() {
        BigDecimal emi = BankingCalculations.emi(
            new BigDecimal("100000.00"),
            new BigDecimal("10.0"),
            12
        );

        assertEquals(new BigDecimal("8791.59"), emi);
        assertEquals(new BigDecimal("105499.08"), BankingCalculations.totalRepayment(emi, 12));
        assertEquals(
            new BigDecimal("5499.08"),
            BankingCalculations.totalInterest(new BigDecimal("100000.00"), emi, 12)
        );
    }

    @Test
    void testDecimalToWords() {
        assertEquals(
            "One Hundred Twenty Three Point Four Five",
            BankingCalculations.decimalToWords(new BigDecimal("123.45"))
        );
        assertEquals(
            "Zero Point Zero Five",
            BankingCalculations.decimalToWords(new BigDecimal("0.05"))
        );
        assertEquals(
            "Minus Twelve Point Five",
            BankingCalculations.decimalToWords(new BigDecimal("-12.5"))
        );
    }

    @Test
    void testAmountToWords() {
        assertEquals(
            "Twelve Lakh Thirty Four Thousand Five Hundred Sixty Seven Rupees and Eighty Nine Paise Only",
            BankingCalculations.amountToWords(new BigDecimal("1234567.89"))
        );
        assertEquals(
            "One Rupee Only",
            BankingCalculations.amountToWords(new BigDecimal("1.00"))
        );
        assertEquals(
            "Zero Rupees and Seventy Five Paise Only",
            BankingCalculations.amountToWords(new BigDecimal("0.75"))
        );
    }

    @Test
    void testZeroInterestEmi() {
        BigDecimal emi = BankingCalculations.emi(
            new BigDecimal("12000.00"),
            BigDecimal.ZERO,
            12
        );

        assertEquals(new BigDecimal("1000.00"), emi);
    }

    @Test
    void testValidation() {
        assertThrows(
            IllegalArgumentException.class,
            () -> BankingCalculations.simpleInterest(new BigDecimal("-1"), BigDecimal.ONE, BigDecimal.ONE)
        );
        assertThrows(
            IllegalArgumentException.class,
            () -> BankingCalculations.emi(BigDecimal.ONE, BigDecimal.ONE, 0)
        );
        assertThrows(
            IllegalArgumentException.class,
            () -> BankingCalculations.amountToWords(null)
        );
    }
}
