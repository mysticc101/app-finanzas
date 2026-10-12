package com.example.finanzas.util.visuals;

import com.example.finanzas.R;

public final class FoodVisualStrategy extends NamedCategoryVisualStrategy {
    public FoodVisualStrategy() {
        add("alimentacion", R.drawable.ic_restaurant, 0xFFD66529);
        add("delivery", R.drawable.ic_delivery_dining, 0xFFDA762F);
        add("restaurantes", R.drawable.ic_restaurant_menu, 0xFFBE4E2D);
    }
}
