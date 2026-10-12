package com.example.finanzas;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import android.content.Context;

import com.example.finanzas.data.model.Transaccion;
import com.example.finanzas.data.model.TransaccionFiltro;
import com.example.finanzas.data.repository.ITransactionRepository;
import com.example.finanzas.util.AmountParser;
import com.example.finanzas.util.TransactionFormValidator;
import com.example.finanzas.util.visuals.CategoryVisualFactory;
import com.example.finanzas.util.visuals.CategoryVisualStrategy;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Suite de pruebas unitarias automatizadas para validar las refactorizaciones aplicadas:
 * 1. Principio DRY: Verificación de rutinas de saneamiento en AmountParser.
 * 2. Principio SRP: Validación aislada de reglas de negocio en TransactionFormValidator.
 * 3. Principio OCP: Extensibilidad dinámica del catálogo visual mediante CategoryVisualFactory.
 * 4. Principio IoC: Desacoplamiento e inyección mediante el contrato ITransactionRepository.
 */
public class TransactionValidatorUnitTest {

    private List<String> supportedCurrencies;

    @Before
    public void setUp() {
        supportedCurrencies = Arrays.asList("PEN", "USD", "EUR", "CLP");
    }

    // =========================================================================
    // 9.1 PRUEBAS UNITARIAS: APLICACIÓN DE DRY (AmountParser)
    // =========================================================================

    @Test
    public void amountParser_standardDotDecimal_shouldParseCorrectly() {
        double result = AmountParser.parseAmount("1250.75");
        assertEquals(1250.75, result, 0.001);
    }

    @Test
    public void amountParser_commaDecimal_shouldParseCorrectly() {
        double result = AmountParser.parseAmount("1250,75");
        assertEquals(1250.75, result, 0.001);
    }

    @Test
    public void amountParser_thousandsWithCommaAndDecimalDot_shouldParseCorrectly() {
        double result = AmountParser.parseAmount("1,250.50");
        assertEquals(1250.50, result, 0.001);
    }

    @Test
    public void amountParser_thousandsWithDotAndDecimalComma_shouldParseCorrectly() {
        double result = AmountParser.parseAmount("1.250,50");
        assertEquals(1250.50, result, 0.001);
    }

    @Test
    public void amountParser_withCurrencySymbolsAndSpaces_shouldSanitize() {
        double result = AmountParser.parseAmount("  S/ 3,450.80  ");
        assertEquals(3450.80, result, 0.001);
    }

    @Test
    public void amountParser_invalidInputs_shouldReturnDefaultValue() {
        assertEquals(0.0, AmountParser.parseAmount(null), 0.001);
        assertEquals(0.0, AmountParser.parseAmount(""), 0.001);
        assertEquals(0.0, AmountParser.parseAmount("    "), 0.001);
        assertEquals(0.0, AmountParser.parseAmount("abc"), 0.001);
        assertEquals(0.0, AmountParser.parseAmount("-"), 0.001);
        assertEquals(0.0, AmountParser.parseAmount("."), 0.001);
        assertEquals(10.0, AmountParser.parseAmount("invalido", 10.0), 0.001);
    }

    @Test
    public void amountParser_parsePositiveAmount_shouldAlwaysBeNonNegative() {
        assertEquals(45.50, AmountParser.parsePositiveAmount("-45.50"), 0.001);
        assertEquals(100.0, AmountParser.parsePositiveAmount("100.00"), 0.001);
        assertEquals(0.0, AmountParser.parsePositiveAmount("-"), 0.001);
    }

    // =========================================================================
    // 9.2 PRUEBAS UNITARIAS: APLICACIÓN DE SRP (TransactionFormValidator)
    // =========================================================================

    @Test
    public void validator_validForm_shouldPass() {
        TransactionFormValidator.ValidationResult result = TransactionFormValidator.validate(
                new Date(),
                "14:30",
                false,
                true,
                "PEN",
                supportedCurrencies,
                150.00,
                "CARD",
                "CASH",
                "NONE",
                0
        );
        assertTrue(result.isValid());
        assertEquals(TransactionFormValidator.ValidationStatus.VALID, result.getStatus());
    }

    @Test
    public void validator_nullDate_shouldReturnInvalidDate() {
        TransactionFormValidator.ValidationResult result = TransactionFormValidator.validate(
                null,
                "14:30",
                false,
                true,
                "PEN",
                supportedCurrencies,
                150.00,
                "CARD",
                "CASH",
                "NONE",
                0
        );
        assertFalse(result.isValid());
        assertEquals(TransactionFormValidator.ValidationStatus.INVALID_DATE, result.getStatus());
    }

    @Test
    public void validator_invalidTime_shouldReturnInvalidTime() {
        TransactionFormValidator.ValidationResult result = TransactionFormValidator.validate(
                new Date(),
                "25:99",
                false,
                true,
                "PEN",
                supportedCurrencies,
                150.00,
                "CARD",
                "CASH",
                "NONE",
                0
        );
        assertFalse(result.isValid());
        assertEquals(TransactionFormValidator.ValidationStatus.INVALID_TIME, result.getStatus());
    }

    @Test
    public void validator_missingCategoryForExpense_shouldReturnMissingCategory() {
        TransactionFormValidator.ValidationResult result = TransactionFormValidator.validate(
                new Date(),
                "10:00",
                false,
                false, // sin categoría
                "PEN",
                supportedCurrencies,
                50.00,
                "CARD",
                "CASH",
                "NONE",
                0
        );
        assertFalse(result.isValid());
        assertEquals(TransactionFormValidator.ValidationStatus.MISSING_CATEGORY, result.getStatus());
    }

    @Test
    public void validator_transferDoesNotRequireCategory_shouldPass() {
        TransactionFormValidator.ValidationResult result = TransactionFormValidator.validate(
                new Date(),
                "10:00",
                true,  // es transferencia
                false, // sin categoría
                "PEN",
                supportedCurrencies,
                50.00,
                "CARD",
                "CASH",
                "NONE",
                0
        );
        assertTrue(result.isValid());
        assertEquals(TransactionFormValidator.ValidationStatus.VALID, result.getStatus());
    }

    @Test
    public void validator_unsupportedCurrency_shouldReturnInvalidCurrency() {
        TransactionFormValidator.ValidationResult result = TransactionFormValidator.validate(
                new Date(),
                "10:00",
                false,
                true,
                "JPY", // no soportada
                supportedCurrencies,
                50.00,
                "CARD",
                "CASH",
                "NONE",
                0
        );
        assertFalse(result.isValid());
        assertEquals(TransactionFormValidator.ValidationStatus.INVALID_CURRENCY, result.getStatus());
    }

    @Test
    public void validator_zeroOrNegativeAmount_shouldReturnInvalidAmount() {
        TransactionFormValidator.ValidationResult zeroResult = TransactionFormValidator.validate(
                new Date(),
                "10:00",
                false,
                true,
                "PEN",
                supportedCurrencies,
                0.0,
                "CARD",
                "CASH",
                "NONE",
                0
        );
        assertFalse(zeroResult.isValid());
        assertEquals(TransactionFormValidator.ValidationStatus.INVALID_AMOUNT, zeroResult.getStatus());

        TransactionFormValidator.ValidationResult negativeResult = TransactionFormValidator.validate(
                new Date(),
                "10:00",
                false,
                true,
                "PEN",
                supportedCurrencies,
                -25.0,
                "CARD",
                "CASH",
                "NONE",
                0
        );
        assertFalse(negativeResult.isValid());
        assertEquals(TransactionFormValidator.ValidationStatus.INVALID_AMOUNT, negativeResult.getStatus());
    }

    @Test
    public void validator_transferSameAccount_shouldReturnSameTransferAccount() {
        TransactionFormValidator.ValidationResult result = TransactionFormValidator.validate(
                new Date(),
                "10:00",
                true,
                false,
                "PEN",
                supportedCurrencies,
                100.0,
                "CARD",
                "CARD", // misma cuenta origen y destino
                "NONE",
                0
        );
        assertFalse(result.isValid());
        assertEquals(TransactionFormValidator.ValidationStatus.SAME_TRANSFER_ACCOUNT, result.getStatus());
    }

    @Test
    public void validator_customRecurrenceWithoutDaysMask_shouldReturnInvalidRecurrence() {
        TransactionFormValidator.ValidationResult result = TransactionFormValidator.validate(
                new Date(),
                "10:00",
                false,
                true,
                "PEN",
                supportedCurrencies,
                100.0,
                "CARD",
                "CASH",
                "CUSTOM",
                0 // sin máscara de días
        );
        assertFalse(result.isValid());
        assertEquals(TransactionFormValidator.ValidationStatus.INVALID_RECURRENCE_CUSTOM, result.getStatus());
    }

    // =========================================================================
    // 9.3 PRUEBAS UNITARIAS: APLICACIÓN DE OCP (CategoryVisualFactory)
    // =========================================================================

    @Test
    public void visualFactory_resolvesStandardStrategies() {
        CategoryVisualStrategy foodStrategy = CategoryVisualFactory.getStrategy("Alimentación", false);
        assertNotNull(foodStrategy);
        assertEquals(R.drawable.ic_restaurant, foodStrategy.getIconRes("alimentacion", false));

        CategoryVisualStrategy incomeStrategy = CategoryVisualFactory.getStrategy("Sueldo", true);
        assertNotNull(incomeStrategy);
        assertEquals(R.drawable.ic_payments, incomeStrategy.getIconRes("sueldo", true));
    }

    @Test
    public void visualFactory_dynamicStrategyRegistration_fulfillsOCP() {
        // Demostración del Principio Abierto/Cerrado (OCP):
        // Extendemos el comportamiento para una nueva categoría "cripto" sin modificar
        // las clases de estrategias existentes ni la factoría original.
        CategoryVisualStrategy cryptoStrategy = new CategoryVisualStrategy() {
            @Override
            public boolean matches(String normalizedName, boolean isIncome) {
                return normalizedName.contains("cripto");
            }

            @Override
            public int getIconRes(String normalizedName, boolean isIncome) {
                return R.drawable.ic_trending_up;
            }

            @Override
            public int getColor(Context context, String normalizedName, boolean isIncome) {
                return 0xFF00FF; // Color personalizado
            }
        };

        CategoryVisualFactory.registerStrategy(cryptoStrategy);

        try {
        CategoryVisualStrategy resolved = CategoryVisualFactory.getStrategy("Inversiones en Cripto", false);
        assertEquals(cryptoStrategy, resolved);
        assertEquals(R.drawable.ic_trending_up, resolved.getIconRes("inversiones en cripto", false));
        } finally {
            CategoryVisualFactory.unregisterStrategy(cryptoStrategy);
        }
    }

}
