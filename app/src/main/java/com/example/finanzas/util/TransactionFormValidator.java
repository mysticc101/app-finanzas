package com.example.finanzas.util;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Date;
import java.util.List;

/**
 * Validador de dominio para el formulario de transacciones.
 * Implementa el Principio de Responsabilidad Única (SRP) aislando las reglas
 * de validación de negocio del controlador de interfaz NuevaTransaccionFragment.
 */
public final class TransactionFormValidator {

    public enum ValidationStatus {
        VALID,
        INVALID_DATE,
        INVALID_TIME,
        MISSING_CATEGORY,
        INVALID_CURRENCY,
        INVALID_AMOUNT,
        SAME_TRANSFER_ACCOUNT,
        INVALID_RECURRENCE_CUSTOM
    }

    public static class ValidationResult {
        private final ValidationStatus status;
        private final String errorMessage;

        private ValidationResult(ValidationStatus status, @Nullable String errorMessage) {
            this.status = status;
            this.errorMessage = errorMessage;
        }

        public static ValidationResult valid() {
            return new ValidationResult(ValidationStatus.VALID, null);
        }

        public static ValidationResult error(ValidationStatus status, String errorMessage) {
            return new ValidationResult(status, errorMessage);
        }

        public boolean isValid() {
            return status == ValidationStatus.VALID;
        }

        public ValidationStatus getStatus() {
            return status;
        }

        @Nullable
        public String getErrorMessage() {
            return errorMessage;
        }
    }

    private TransactionFormValidator() {
    }

    /**
     * Valida integralmente los datos requeridos para registrar o actualizar una transacción.
     */
    @NonNull
    public static ValidationResult validate(
            @Nullable Date fecha,
            @Nullable String hora,
            boolean isTransfer,
            boolean hasCategory,
            @Nullable String moneda,
            @Nullable List<String> supportedCurrencies,
            double monto,
            @Nullable String originAccount,
            @Nullable String destinationAccount,
            @Nullable String recurrenceCode,
            int recurrenceDaysMask
    ) {
        if (fecha == null) {
            return ValidationResult.error(ValidationStatus.INVALID_DATE, "Formato de fecha inválido.");
        }

        if (!TimeValidator.isValidTime(hora == null ? "" : hora.trim())) {
            return ValidationResult.error(ValidationStatus.INVALID_TIME, "Formato de hora inválido.");
        }

        if (!isTransfer && !hasCategory) {
            return ValidationResult.error(ValidationStatus.MISSING_CATEGORY, "Selecciona una categoría.");
        }

        if (moneda == null || supportedCurrencies == null || !supportedCurrencies.contains(moneda)) {
            return ValidationResult.error(ValidationStatus.INVALID_CURRENCY, "Moneda no soportada.");
        }

        if (!Double.isFinite(monto) || monto <= 0) {
            return ValidationResult.error(ValidationStatus.INVALID_AMOUNT, "El monto debe ser mayor a 0.");
        }

        if (isTransfer) {
            String origin = originAccount == null ? "" : originAccount.trim();
            String dest = destinationAccount == null ? "" : destinationAccount.trim();
            if (!origin.isEmpty() && origin.equalsIgnoreCase(dest)) {
                return ValidationResult.error(ValidationStatus.SAME_TRANSFER_ACCOUNT, "La cuenta origen y destino deben ser distintas.");
            }
        }

        if ("CUSTOM".equalsIgnoreCase(recurrenceCode) && recurrenceDaysMask == 0) {
            return ValidationResult.error(ValidationStatus.INVALID_RECURRENCE_CUSTOM, "Selecciona al menos un día para la recurrencia personalizada.");
        }

        return ValidationResult.valid();
    }
}
