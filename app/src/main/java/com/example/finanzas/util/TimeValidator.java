package com.example.finanzas.util;

import androidx.annotation.Nullable;

/** Regla de hora HH:mm independiente de vistas y recursos de Android. */
public final class TimeValidator {
    private TimeValidator() { }

    public static boolean isValidTime(@Nullable String raw) {
        if (raw == null || !raw.matches("^\\d{2}:\\d{2}$")) return false;
        int hour = Integer.parseInt(raw.substring(0, 2));
        int minute = Integer.parseInt(raw.substring(3, 5));
        return hour <= 23 && minute <= 59;
    }
}
