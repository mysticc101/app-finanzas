package com.example.finanzas.ui;

import static org.junit.Assert.*;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.fragment.app.FragmentActivity;
import com.example.finanzas.R;
import com.google.android.material.textfield.TextInputLayout;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import java.lang.reflect.*;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 28)
public class BudgetFormRegressionTest {
    public static class Form extends PresupuestoFragment {
        @Override public void onViewCreated(View view, Bundle state) { }
    }
    @Test public void malformedInputStopsBeforeViewModelSave() throws Exception {
        FragmentActivity activity = Robolectric.buildActivity(FragmentActivity.class).setup().get();
        activity.setTheme(R.style.Theme_Finanzas);
        FrameLayout container = new FrameLayout(activity); container.setId(View.generateViewId());
        activity.setContentView(container);
        Form form = new Form();
        activity.getSupportFragmentManager().beginTransaction().add(container.getId(), form).commitNow();
        EditText amount = form.requireView().findViewById(R.id.etPresupuesto);
        TextInputLayout error = form.requireView().findViewById(R.id.tilPresupuesto);
        for (String name : new String[]{"etPresupuesto", "tilPresupuesto"}) {
            Field field = PresupuestoFragment.class.getDeclaredField(name); field.setAccessible(true);
            field.set(form, "etPresupuesto".equals(name) ? amount : error);
        }
        amount.setText("1.2.3");
        Method save = PresupuestoFragment.class.getDeclaredMethod("guardarPresupuesto"); save.setAccessible(true);
        // No ViewModel is supplied: reaching saveBudget would fail this test.
        save.invoke(form);
        assertEquals(activity.getString(R.string.error_monto_invalido), error.getError().toString());
    }
    @Test public void fragmentParserPreservesNegativeSignForBudgetClamp() throws Exception {
        Method parse = PresupuestoFragment.class.getDeclaredMethod("parseMontoSeguro", String.class);
        parse.setAccessible(true);
        assertEquals(-50, (double) parse.invoke(new PresupuestoFragment(), "-50"), 0);
    }
    // Intercepta el destino real de la llamada del formulario sin acceder a la base de datos.
    @org.robolectric.annotation.Implements(
            value = com.example.finanzas.ui.viewmodel.BudgetViewModel.class,
            isInAndroidSdk = false)
    public static class CapturingBudgetViewModel {
        int calls;
        int year;
        int month;
        double amount = Double.NaN;
        String currency;

        @org.robolectric.annotation.Implementation
        protected void saveBudget(int year, int month, double amount, String currency) {
            this.calls++;
            this.year = year;
            this.month = month;
            this.amount = amount;
            this.currency = currency;
        }
    }

    @Test
    @Config(
            shadows = CapturingBudgetViewModel.class,
            instrumentedPackages = "com.example.finanzas.ui.viewmodel")
    public void negativeBudgetSavesAsZero() throws Exception {
        org.robolectric.android.controller.ActivityController<FragmentActivity> controller =
                Robolectric.buildActivity(FragmentActivity.class).setup();
        FragmentActivity activity = controller.get();
        activity.setTheme(R.style.Theme_Finanzas);
        try {
            FrameLayout container = new FrameLayout(activity);
            container.setId(View.generateViewId());
            activity.setContentView(container);
            Form form = new Form();
            activity.getSupportFragmentManager().beginTransaction()
                    .add(container.getId(), form).commitNow();

            EditText amount = form.requireView().findViewById(R.id.etPresupuesto);
            TextInputLayout error = form.requireView().findViewById(R.id.tilPresupuesto);
            setFormField(form, "etPresupuesto", amount);
            setFormField(form, "tilPresupuesto", error);
            setFormField(form, "btnGuardarPresupuesto",
                    form.requireView().findViewById(R.id.btnGuardarPresupuesto));
            setFormField(form, "anio", 2026);
            setFormField(form, "mes", 11);

            com.example.finanzas.ui.viewmodel.BudgetViewModel viewModel =
                    new com.example.finanzas.ui.viewmodel.BudgetViewModel(activity.getApplication());
            setFormField(form, "viewModel", viewModel);
            CapturingBudgetViewModel captured = org.robolectric.shadow.api.Shadow.extract(viewModel);
            String expectedCurrency =
                    com.example.finanzas.data.api.SettingsService.getCurrencyCode(activity);

            amount.setText("-50");
            Method save = PresupuestoFragment.class.getDeclaredMethod("guardarPresupuesto");
            save.setAccessible(true);
            save.invoke(form);

            // Comprueba la llamada real emitida por el formulario, no un parser aislado.
            assertEquals(1, captured.calls);
            assertEquals(0.0, captured.amount, 0.0);
            assertEquals(2026, captured.year);
            assertEquals(11, captured.month);
            assertEquals(expectedCurrency, captured.currency);
            assertNull(error.getError());
        } finally {
            controller.pause().stop().destroy();
        }
    }

    private static void setFormField(Form form, String name, Object value) throws Exception {
        Field field = PresupuestoFragment.class.getDeclaredField(name);
        field.setAccessible(true);
        field.set(form, value);
    }
}