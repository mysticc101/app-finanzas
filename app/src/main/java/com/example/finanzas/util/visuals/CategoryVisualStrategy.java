package com.example.finanzas.util.visuals;

import android.content.Context;
import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;

/**
 * Contrato polimórfico para resolver recursos visuales (icono y color) de categorías.
 * Aplica el principio Abierto/Cerrado (OCP) formulado por Bertrand Meyer,
 * permitiendo incorporar nuevas familias de categorías sin alterar el código existente.
 */
public interface CategoryVisualStrategy {
    boolean matches(@NonNull String normalizedName, boolean isIncome);
    @DrawableRes int getIconRes(@NonNull String normalizedName, boolean isIncome);
    @ColorInt int getColor(@NonNull Context context, @NonNull String normalizedName, boolean isIncome);
}
