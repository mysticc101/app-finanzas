package com.example.finanzas.util;

import androidx.annotation.Nullable;

/**
 * Utilitario centralizado para saneamiento y análisis numérico de montos monetarios.
 * Implementa el principio DRY (Don't Repeat Yourself) para eliminar la duplicación
 * de rutinas de parseo decimal dispersas en controladores de interfaz.
 */
public final class AmountParser {

    private AmountParser() {
    }

    /**
     * Parsea de manera segura un valor numérico ingresado por el usuario.
     * Soporta formatos internacionales con coma o punto decimal y elimina símbolos de moneda.
     */
    public static double parseAmount(@Nullable String raw) {
        return parseAmount(raw, 0.0);
    }

    /**
     * Parsea un monto asegurando que el valor retornado no sea negativo.
     */
    public static double parsePositiveAmount(@Nullable String raw) {
        return Math.max(0.0, Math.abs(parseAmount(raw, 0.0)));
    }

    /**
     * Parsea el monto crudo retornando un valor por defecto en caso de formato inválido o vacío.
     */
    public static double parseAmount(@Nullable String raw, double defaultValue) {
        try {
            return parseAmountOrThrow(raw, defaultValue);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * Conserva la política del presupuesto: vacío equivale a cero, pero un
     * número mal formado se rechaza antes de guardar. No convierte negativos
     * a su valor absoluto; cada pantalla decide cómo tratar el signo.
     */
    public static double parseAmountOrThrow(@Nullable String raw) {
        return parseAmountOrThrow(raw, 0.0);
    }

    private static double parseAmountOrThrow(@Nullable String raw, double defaultValue) {
        if (raw == null) {
            return defaultValue;
        }
        String limpio = raw.trim();
        if (limpio.isEmpty()) {
            return defaultValue;
        }

        // Remover caracteres ajenos a dígitos y separadores decimales
        limpio = limpio.replaceAll("[^0-9,.-]", "");
        if (limpio.isEmpty()) {
            return defaultValue;
        }

        int lastComma = limpio.lastIndexOf(',');
        int lastDot = limpio.lastIndexOf('.');
        if (lastComma >= 0 && lastDot >= 0) {
            if (lastComma > lastDot) {
                limpio = limpio.replace(".", "");
                limpio = limpio.replace(',', '.');
            } else {
                limpio = limpio.replace(",", "");
            }
        } else if (lastComma >= 0) {
            limpio = limpio.replace(',', '.');
        }

        if ("-".equals(limpio) || ".".equals(limpio) || "-.".equals(limpio) || ",".equals(limpio)) {
            return defaultValue;
        }

        double amount = Double.parseDouble(limpio);
        if (!Double.isFinite(amount)) {
            throw new NumberFormatException("Monto fuera de rango");
        }
        return amount;
    }
}
