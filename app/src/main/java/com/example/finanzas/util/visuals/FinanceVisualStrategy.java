package com.example.finanzas.util.visuals;

import com.example.finanzas.R;

public final class FinanceVisualStrategy extends NamedCategoryVisualStrategy {
    public FinanceVisualStrategy() {
        add("deudas", R.drawable.ic_receipt_long, 0xFFAB414B);
        add("impuestos", R.drawable.ic_account_balance, 0xFF606470);
    }
}
