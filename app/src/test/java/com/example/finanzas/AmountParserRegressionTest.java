package com.example.finanzas;

import static org.junit.Assert.*;
import com.example.finanzas.util.AmountParser;
import com.example.finanzas.util.TimeValidator;
import com.example.finanzas.util.TransactionFormValidator;
import java.util.*;
import org.junit.Test;

public class AmountParserRegressionTest {
    @Test public void negativePoliciesRemainDistinct() {
        assertEquals(-50, AmountParser.parseAmountOrThrow("-50"), 0);
        assertEquals(0, Math.max(0, AmountParser.parseAmountOrThrow("-50")), 0);
        assertEquals(50, AmountParser.parsePositiveAmount("-50"), 0);
        assertEquals(0, Math.max(0, AmountParser.parseAmount("-50")), 0);
    }
    @Test public void malformedBudgetNumberThrowsInsteadOfBecomingZero() {
        for (String value : new String[]{"1.2.3", "1,2,3", "--50", "1-2"}) {
            assertThrows(NumberFormatException.class, () -> AmountParser.parseAmountOrThrow(value));
            assertEquals(0, AmountParser.parsePositiveAmount(value), 0);
            assertEquals(9, AmountParser.parseAmount(value, 9), 0);
        }
    }
    @Test public void strictParserPreservesOriginalEmptyAndSeparatorPolicy() {
        for (String value : new String[]{null, "", "  ", "abc", "-", ".", "-.", ","})
            assertEquals(0, AmountParser.parseAmountOrThrow(value), 0);
        assertEquals(1234.56, AmountParser.parseAmountOrThrow("S/ 1.234,56"), 0.0001);
        assertEquals(1234.56, AmountParser.parseAmountOrThrow("S/ 1,234.56"), 0.0001);
    }
    @Test public void overflowCannotReachPersistenceAsInfinity() {
        String overflow = "9".repeat(400);
        assertThrows(NumberFormatException.class, () -> AmountParser.parseAmountOrThrow(overflow));
        assertEquals(0, AmountParser.parsePositiveAmount(overflow), 0);
    }
    @Test public void validatorRejectsNonFiniteAmounts() {
        for (double amount : new double[]{Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY})
            assertEquals(TransactionFormValidator.ValidationStatus.INVALID_AMOUNT,
                    TransactionFormValidator.validate(new Date(), "12:00", false, true, "PEN",
                            Arrays.asList("PEN"), amount, "CARD", "CASH", null, 0).getStatus());
    }
    @Test public void timeRulePreservesBoundariesWithoutAndroidViews() {
        for (String time : new String[]{"00:00", "23:59", "12:30"}) assertTrue(TimeValidator.isValidTime(time));
        for (String time : new String[]{null, "", "1:30", "24:00", "12:60", "-1:30", " 12:30 "})
            assertFalse(TimeValidator.isValidTime(time));
    }
}
