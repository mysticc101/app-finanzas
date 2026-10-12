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
}
