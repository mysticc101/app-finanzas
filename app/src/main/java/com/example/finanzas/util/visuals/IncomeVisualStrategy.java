package com.example.finanzas.util.visuals;

import com.example.finanzas.R;

public final class IncomeVisualStrategy extends NamedCategoryVisualStrategy {
    public IncomeVisualStrategy() {
        add("sueldo", R.drawable.ic_payments, 0xFF1F7A55);
        add("freelance", R.drawable.ic_work_history, 0xFF2D76A9);
        add("inversiones", R.drawable.ic_trending_up, 0xFF368B4B);
        add("negocio", R.drawable.ic_store, 0xFF796934);
        add("otros ingresos", R.drawable.ic_add_card, 0xFF5472AC);
        add("reembolso", R.drawable.ic_assignment_return, 0xFF23978C);
        add("regalo recibido", R.drawable.ic_redeem, 0xFFB95680);
        add("venta", R.drawable.ic_sell, 0xFFB06D28);
    }
}
