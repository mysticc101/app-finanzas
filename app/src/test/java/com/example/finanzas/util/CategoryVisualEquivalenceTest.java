package com.example.finanzas.util;

import static org.junit.Assert.*;
import android.content.Context;
import com.example.finanzas.R;
import com.example.finanzas.data.model.Categoria;
import com.example.finanzas.util.visuals.*;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 28)
public class CategoryVisualEquivalenceTest {
    private final Context context = RuntimeEnvironment.getApplication();
    private static final String[] NAMES = {
            "agua",
            "ahorro",
            "alimentacion",
            "alquiler",
            "business",
            "calzado",
            "casa",
            "celular",
            "cine",
            "clinica",
            "colegio",
            "comida",
            "compra",
            "credito",
            "cuidado personal",
            "curso",
            "delivery",
            "deuda",
            "deudas",
            "devolucion",
            "dividendo",
            "educacion",
            "empresa",
            "entretenimiento",
            "familia",
            "farmacia",
            "freelance",
            "gas",
            "gift",
            "grocer",
            "hijo",
            "hogar",
            "hotel",
            "impuesto",
            "impuestos",
            "independiente",
            "interes",
            "internet",
            "internet / telefono",
            "internet telefono",
            "inversion",
            "inversiones",
            "laptop",
            "luz",
            "mascota",
            "medic",
            "mercado",
            "meta",
            "negocio",
            "netflix",
            "nomina",
            "ocio",
            "oficina",
            "otros ingresos",
            "pet",
            "prestamo",
            "reembolso",
            "refund",
            "regalo",
            "regalo recibido",
            "reparaciones",
            "restaurantes",
            "ropa",
            "salario",
            "sale",
            "salud",
            "servicio",
            "servicios",
            "shopping",
            "software",
            "sueldo",
            "super",
            "suscripciones",
            "tax",
            "tecnologia",
            "telefono",
            "tienda",
            "trabajo",
            "transporte",
            "turismo",
            "universidad",
            "venta",
            "vestimenta",
            "veterin",
            "viaje",
            "vivienda",
            "vuelo"
    };

    private void equivalent(String name, boolean income) {
        assertEquals("icon: " + name + "/" + income,
                LegacyCategoryVisuals.iconFor(name, income), CategoryVisuals.iconFor(name, income));
        assertEquals("color: " + name + "/" + income,
                LegacyCategoryVisuals.colorFor(context, name, income), CategoryVisuals.colorFor(context, name, income));
    }

    @Test public void exactNamesKeywordsAndOverlappingRulesMatchOriginal() {
        for (boolean income : new boolean[]{false, true}) {
            equivalent(null, income);
            equivalent("", income);
            equivalent("sin coincidencia", income);
            for (String name : NAMES) {
                equivalent(name, income);
                equivalent("  " + name.toUpperCase(java.util.Locale.ROOT) + "  ", income);
                equivalent("mi " + name, income);
                for (String other : NAMES) equivalent(name + " " + other, income);
            }
            equivalent("ALIMENTACIÓN", income);
            equivalent("Internet / Teléfono", income);
        }
    }

    @Test @Config(qualifiers = "night") public void nightThemeMatchesOriginal() {
        for (String name : NAMES) {
            equivalent(name, false);
            equivalent(name, true);
            equivalent("mi " + name, false);
        }
    }

    @Test public void categoryWithoutCustomizationRetainsOriginalDefaults() {
        CategoryPrefs.importSyncMeta(context, new JSONObject());
        for (String name : NAMES) {
            Categoria category = new Categoria(501, name, false);
            assertEquals(LegacyCategoryVisuals.iconFor(context, category), CategoryVisuals.iconFor(context, category));
            assertEquals(LegacyCategoryVisuals.colorFor(context, category), CategoryVisuals.colorFor(context, category));
        }
    }

    @Test public void customizationOverridesRegistryAndInvalidColorFallsBack() throws Exception {
        Categoria category = new Categoria(502, "Delivery", false);
        JSONObject meta = new JSONObject().put("502", new JSONObject()
                .put("icon", CategoryPrefs.ICON_BOLT).put("color", "#123456"));
        CategoryPrefs.importSyncMeta(context, meta);
        assertEquals(R.drawable.ic_bolt, CategoryVisuals.iconFor(context, category));
        assertEquals(0xFF123456, CategoryVisuals.colorFor(context, category));
        meta.getJSONObject("502").put("color", "invalid");
        CategoryPrefs.importSyncMeta(context, meta);
        assertEquals(0xFFDA762F, CategoryVisuals.colorFor(context, category));
    }

    @Test public void registeredStrategyCanOverrideIconAndColorAndBeRemoved() {
        CategoryVisualStrategy extension = new CategoryVisualStrategy() {
            public boolean matches(String name, boolean income) { return "delivery".equals(name); }
            public int getIconRes(String name, boolean income) { return R.drawable.ic_bolt; }
            public int getColor(Context ctx, String name, boolean income) { return 0xFF123456; }
        };
        CategoryVisualFactory.registerStrategy(extension);
        try {
            assertEquals(R.drawable.ic_bolt, CategoryVisuals.iconFor("Delivery", false));
            assertEquals(0xFF123456, CategoryVisuals.colorFor(context, "Delivery", false));
        } finally { CategoryVisualFactory.unregisterStrategy(extension); }
        equivalent("Delivery", false);
    }
}
