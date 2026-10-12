package com.example.finanzas.util.visuals;

import android.content.Context;
import java.util.LinkedHashMap;
import java.util.Map;

/** Catálogo de coincidencias exactas; sus valores originales no dependen del tipo de movimiento. */
public abstract class NamedCategoryVisualStrategy implements CategoryVisualStrategy {
    private final Map<String, int[]> visuals = new LinkedHashMap<>();

    protected final void add(String name, int icon, int color) {
        visuals.put(name, new int[]{icon, color});
    }

    @Override public final boolean matches(String name, boolean isIncome) {
        return visuals.containsKey(name);
    }
    @Override public final int getIconRes(String name, boolean isIncome) {
        return visuals.get(name)[0];
    }
    @Override public final int getColor(Context context, String name, boolean isIncome) {
        return visuals.get(name)[1];
    }
}
