package com.example.finanzas.util.visuals;

import android.content.Context;
import androidx.core.content.ContextCompat;
import com.example.finanzas.R;
import com.example.finanzas.util.CategoryVisuals;

/** Reglas generales, en el orden original, para nombres sin coincidencia exacta. */
public final class DefaultCategoryVisualStrategy implements CategoryVisualStrategy {
    private static final class Rule {
        final int resource;
        final String[] words;
        Rule(int resource, String... words) { this.resource = resource; this.words = words; }
    }
    private static final Rule[] INCOME_ICON = {
        new Rule(R.drawable.ic_income_salary, "salario", "sueldo", "nomina"),
        new Rule(R.drawable.ic_income_freelance, "freelance", "independiente", "servicio"),
        new Rule(R.drawable.ic_income_business, "negocio", "business", "empresa"),
        new Rule(R.drawable.ic_income_investments, "inversion", "dividendo", "interes"),
        new Rule(R.drawable.ic_income_gift, "regalo", "gift"),
        new Rule(R.drawable.ic_income_refund, "reembolso", "refund", "devolucion"),
        new Rule(R.drawable.ic_income_sale, "venta", "sale")
    };
    private static final Rule[] EXPENSE_ICON = {
        new Rule(R.drawable.ic_category_health, "salud", "medic", "clinica", "farmacia"),
        new Rule(R.drawable.ic_category_education, "educacion", "curso", "universidad", "colegio"),
        new Rule(R.drawable.ic_category_services, "servicio", "luz", "agua", "internet", "telefono"),
        new Rule(R.drawable.ic_category_groceries, "super", "mercado", "alimentacion", "comida", "grocer"),
        new Rule(R.drawable.ic_category_shopping, "compra", "shopping", "tienda"),
        new Rule(R.drawable.ic_category_clothing, "ropa", "calzado", "vestimenta"),
        new Rule(R.drawable.ic_category_technology, "tecnologia", "software", "celular", "laptop"),
        new Rule(R.drawable.ic_category_travel, "viaje", "hotel", "vuelo", "turismo"),
        new Rule(R.drawable.ic_category_pets, "mascota", "pet", "veterin"),
        new Rule(R.drawable.ic_category_family, "familia", "hijo", "hogar", "vivienda", "alquiler", "casa"),
        new Rule(R.drawable.ic_category_debt, "deuda", "prestamo", "credito"),
        new Rule(R.drawable.ic_category_savings, "ahorro", "meta"),
        new Rule(R.drawable.ic_category_gifts, "regalo", "gift"),
        new Rule(R.drawable.ic_category_work, "trabajo", "oficina"),
        new Rule(R.drawable.ic_category_taxes, "impuesto", "tax")
    };
    private static final Rule[] INCOME_COLOR = {
        new Rule(R.color.income, "salario", "sueldo"),
        new Rule(R.color.chart_pie_3, "inversion"),
        new Rule(R.color.chart_pie_4, "regalo", "reembolso", "venta")
    };
    private static final Rule[] EXPENSE_COLOR = {
        new Rule(R.color.chart_pie_4, "vivienda", "alquiler", "casa", "familia"),
        new Rule(R.color.chart_pie_2, "entretenimiento", "ocio", "cine", "netflix"),
        new Rule(R.color.chart_pie_1, "super", "mercado", "alimentacion", "comida"),
        new Rule(R.color.chart_pie_6, "transporte", "viaje"),
        new Rule(R.color.expense, "salud", "deuda", "impuesto"),
        new Rule(R.color.chart_pie_3, "tecnologia", "servicio"),
        new Rule(R.color.chart_pie_5, "ropa", "compra", "regalo")
    };

    @Override public boolean matches(String name, boolean isIncome) { return true; }

    @Override public int getIconRes(String name, boolean isIncome) {
        return resolve(name, isIncome ? INCOME_ICON : EXPENSE_ICON,
                isIncome ? R.drawable.ic_income_other : R.drawable.ic_category_other);
    }

    @Override public int getColor(Context context, String name, boolean isIncome) {
        int resource = resolve(name, isIncome ? INCOME_COLOR : EXPENSE_COLOR,
                isIncome ? R.color.chart_pie_1 : R.color.chart_pie_8);
        return ContextCompat.getColor(context, resource);
    }

    private static int resolve(String name, Rule[] rules, int fallback) {
        for (Rule rule : rules) {
            if (CategoryVisuals.contains(name, rule.words)) return rule.resource;
        }
        return fallback;
    }
}
