package com.example.finanzas.util.visuals;

import com.example.finanzas.R;

public final class BillsVisualStrategy extends NamedCategoryVisualStrategy {
    public BillsVisualStrategy() {
        add("servicios", R.drawable.ic_home_repair_service, 0xFF5E7586);
        add("internet / telefono", R.drawable.ic_wifi_calling, 0xFF3770B9);
        add("internet telefono", R.drawable.ic_wifi_calling, 0xFF3770B9);
        add("luz", R.drawable.ic_bolt, 0xFFDAA623);
        add("agua", R.drawable.ic_water_drop, 0xFF298EBD);
        add("gas", R.drawable.ic_local_fire_department, 0xFFC7522A);
        add("reparaciones", R.drawable.ic_build, 0xFF5E7586);
        add("tecnologia", R.drawable.ic_devices, 0xFF3770B9);
    }
}
