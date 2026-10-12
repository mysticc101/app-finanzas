package com.example.finanzas.util;

import android.content.Context;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.finanzas.data.model.Categoria;
import com.example.finanzas.util.visuals.CategoryVisualFactory;

import java.text.Normalizer;
import java.util.Locale;

/**
 * Fachada para la resolución visual de categorías.
 * Refactorizada bajo el Principio Abierto/Cerrado (OCP) para delegar la resolución
 * en el registro polimórfico CategoryVisualFactory.
 */
public final class CategoryVisuals {

    private CategoryVisuals() {
    }

    @DrawableRes
    public static int iconFor(@Nullable String name, boolean income) {
        return CategoryVisualFactory.getIcon(name, income);
    }

    @DrawableRes
    public static int iconFor(@NonNull Context context, @NonNull Categoria categoria) {
        return CategoryPrefs.iconFor(CategoryPrefs.meta(context, categoria).iconKey);
    }

    @ColorInt
    public static int colorFor(@NonNull Context context, @Nullable String name, boolean income) {
        return CategoryVisualFactory.getColor(context, name, income);
    }

    @ColorInt
    public static int colorFor(@NonNull Context context, @NonNull Categoria categoria) {
        return CategoryPrefs.meta(context, categoria).color;
    }

    @NonNull
    public static String normalize(@Nullable String raw) {
        if (raw == null || raw.trim().isEmpty()) return "";
        String clean = Normalizer.normalize(raw.toLowerCase(Locale.ROOT), Normalizer.Form.NFD);
        return clean.replaceAll("\\p{InCombiningDiacriticalMarks}+", "").trim();
    }

    public static boolean contains(@NonNull String text, @NonNull String... parts) {
        for (String part : parts) {
            if (text.contains(part)) return true;
        }
        return false;
    }
}
