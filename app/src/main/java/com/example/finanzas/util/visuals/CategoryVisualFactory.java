package com.example.finanzas.util.visuals;

import android.content.Context;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.finanzas.util.CategoryVisuals;

import java.util.ArrayList;
import java.util.List;

/**
 * Factoría y registro de estrategias visuales para categorías.
 * Implementa los patrones Strategy y Registry cumpliendo el Principio Abierto/Cerrado (OCP):
 * el conjunto de representaciones visuales está abierto a la extensión (mediante registerStrategy)
 * y cerrado a la modificación de lógica interna condicional.
 */
public final class CategoryVisualFactory {

    private static final List<CategoryVisualStrategy> STRATEGIES = new ArrayList<>();
    private static final CategoryVisualStrategy DEFAULT_STRATEGY = new DefaultCategoryVisualStrategy();

    static {
        STRATEGIES.add(new IncomeVisualStrategy());
        STRATEGIES.add(new FoodVisualStrategy());
        STRATEGIES.add(new BillsVisualStrategy());
        STRATEGIES.add(new TransportVisualStrategy());
        STRATEGIES.add(new EntertainmentVisualStrategy());
        STRATEGIES.add(new HealthVisualStrategy());
        STRATEGIES.add(new FinanceVisualStrategy());
    }

    private CategoryVisualFactory() {
    }

    /**
     * Permite registrar dinámicamente nuevas estrategias en tiempo de ejecución.
     * Fomenta la extensibilidad sin modificar el código existente (OCP).
     */
    public static synchronized void registerStrategy(@NonNull CategoryVisualStrategy strategy) {
        STRATEGIES.add(0, strategy);
    }

    public static synchronized boolean unregisterStrategy(@NonNull CategoryVisualStrategy strategy) {
        return STRATEGIES.remove(strategy);
    }

    /**
     * Resuelve la estrategia adecuada para el nombre de categoría y tipo de flujo.
     */
    @NonNull
    public static synchronized CategoryVisualStrategy getStrategy(@Nullable String categoryName, boolean isIncome) {
        String normalized = CategoryVisuals.normalize(categoryName);
        for (CategoryVisualStrategy strategy : STRATEGIES) {
            if (strategy.matches(normalized, isIncome)) {
                return strategy;
            }
        }
        return DEFAULT_STRATEGY;
    }

    /**
     * Resuelve el identificador del recurso drawable mediante la estrategia correspondiente.
     */
    @DrawableRes
    public static int getIcon(@Nullable String categoryName, boolean isIncome) {
        return getStrategy(categoryName, isIncome).getIconRes(CategoryVisuals.normalize(categoryName), isIncome);
    }

    /**
     * Resuelve el color int mediante la estrategia correspondiente.
     */
    @ColorInt
    public static int getColor(@NonNull Context context, @Nullable String categoryName, boolean isIncome) {
        return getStrategy(categoryName, isIncome).getColor(context, CategoryVisuals.normalize(categoryName), isIncome);
    }
}
