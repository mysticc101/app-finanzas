package com.example.finanzas.ui;

import static org.junit.Assert.*;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.fragment.app.FragmentActivity;
import com.example.finanzas.R;
import com.example.finanzas.data.model.*;
import com.example.finanzas.data.repository.ITransactionRepository;
import com.example.finanzas.util.UiFormUtils;
import com.google.android.material.button.*;
import com.google.android.material.textfield.TextInputLayout;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import java.lang.reflect.*;
import java.util.*;

/** Executes the production save/delete/list consumer with an injected deferred repository.
 * Startup loading is omitted to isolate form behavior from accounts, cloud and category services.
 */
@RunWith(RobolectricTestRunner.class)
@Config(sdk = 28)
public class TransactionFormConsumerTest {
    public static class Form extends NuevaTransaccionFragment {
        @Override public void onViewCreated(View view, Bundle state) { /* bound explicitly below */ }
    }
    private Form form;
    private FakeRepository repository;
    private View root;

    @Before public void setUp() throws Exception {
        FragmentActivity activity = Robolectric.buildActivity(FragmentActivity.class).setup().get();
        activity.setTheme(R.style.Theme_Finanzas);
        FrameLayout container = new FrameLayout(activity);
        container.setId(View.generateViewId());
        activity.setContentView(container);
        form = new Form();
        repository = new FakeRepository();
        form.setTransactionRepository(repository);
        activity.getSupportFragmentManager().beginTransaction().add(container.getId(), form).commitNow();
        root = form.requireView();
        for (String name : new String[]{"etMonto", "etNota", "etFecha", "etHora", "actCategoria", "actMoneda",
                "actRecurrence", "tilFecha", "tilHora", "tilCategoria", "tilMoneda", "tvMontoError",
                "btnGuardar", "toggleTipo", "chipCustomRecurrenceDays"}) {
            int id = R.id.class.getField(name).getInt(null);
            set(name, root.findViewById(id));
        }
        set("visibles", Arrays.asList(new Categoria(7, "Alimentación", false)));
        edit("etMonto", "-50"); edit("etNota", "Prueba local");
        edit("etFecha", "11/10/2026"); edit("etHora", "14:30");
        edit("actCategoria", "Alimentación"); edit("actMoneda", "PEN");
        type(R.id.btnTipoGasto);
    }

    private void set(String name, Object value) throws Exception {
        Field field = NuevaTransaccionFragment.class.getDeclaredField(name);
        field.setAccessible(true); field.set(form, value);
    }
    private void edit(String name, String text) throws Exception {
        ((EditText) root.findViewById(R.id.class.getField(name).getInt(null))).setText(text);
    }
    private void type(int id) { ((MaterialButtonToggleGroup) root.findViewById(R.id.toggleTipo)).check(id); }
    private void invoke(String method) throws Exception {
        Method m = NuevaTransaccionFragment.class.getDeclaredMethod(method);
        m.setAccessible(true); m.invoke(form);
    }
    private void save() throws Exception {
        Method m = NuevaTransaccionFragment.class.getDeclaredMethod("onGuardar", View.class);
        m.setAccessible(true); m.invoke(form, root.findViewById(R.id.btnGuardar));
    }
    private TextInputLayout layout(int id) { return root.findViewById(id); }

    @Test public void createUsesInjectedRepositoryAndWaitsForCallback() throws Exception {
        save();
        assertSame(repository, form.getRepository());
        assertEquals("create", repository.operation);
        assertEquals(7, repository.categoryId);
        assertEquals(50, repository.amount, 0);
        assertEquals("PEN", repository.currency);
        assertEquals("CARD", repository.origin);
        assertEquals("Prueba local", repository.note);
        assertFalse(root.findViewById(R.id.btnGuardar).isEnabled());
        repository.createCallback.onError("Saldo insuficiente");
        assertTrue(root.findViewById(R.id.btnGuardar).isEnabled());
    }
    @Test public void editUsesInjectedRepositoryWithOriginalId() throws Exception {
        set("editingId", 42); save();
        assertEquals("update", repository.operation);
        assertEquals(42, repository.id);
        assertEquals(50, repository.amount, 0);
        repository.operationCallback.onError("No se pudo actualizar");
        assertTrue(root.findViewById(R.id.btnGuardar).isEnabled());
    }
    @Test public void createTransferUsesInjectedRepositoryWithoutCategory() throws Exception {
        type(R.id.btnTipoTransferencia); edit("actCategoria", ""); save();
        assertEquals("createTransfer", repository.operation);
        assertEquals("CARD", repository.origin); assertEquals("CASH", repository.destination);
        assertEquals(50, repository.amount, 0);
    }
    @Test public void editTransferUsesInjectedRepository() throws Exception {
        type(R.id.btnTipoTransferencia); set("editingId", 43); save();
        assertEquals("updateTransfer", repository.operation); assertEquals(43, repository.id);
    }
    @Test public void deleteUsesInjectedRepositoryAndErrorRestoresSave() throws Exception {
        set("editingId", 44); invoke("deleteEditingTransaction");
        assertEquals("delete", repository.operation); assertEquals(44, repository.id);
        repository.operationCallback.onError("No se pudo eliminar");
        assertTrue(root.findViewById(R.id.btnGuardar).isEnabled());
    }
    @Test public void usageListUsesInjectedRepository() throws Exception {
        invoke("loadCategoryUsageCounts"); assertEquals("list", repository.operation);
        repository.listCallback.onOk(Collections.emptyList());
    }
    @Test public void malformedAmountNeverReachesRepository() throws Exception {
        edit("etMonto", "1.2.3"); save(); assertNull(repository.operation);
        assertEquals(View.VISIBLE, root.findViewById(R.id.tvMontoError).getVisibility());
    }
    @Test public void sameAccountTransferNeverReachesRepository() throws Exception {
        type(R.id.btnTipoTransferencia); set("selectedDestinationAccountType", "CARD"); save();
        assertNull(repository.operation);
    }
    @Test public void correctingDateThenFailingTimeClearsPreviousError() throws Exception {
        edit("etFecha", "incorrecta"); save(); assertNotNull(layout(R.id.tilFecha).getError());
        edit("etFecha", "11/10/2026"); edit("etHora", "25:00"); save();
        assertNull(layout(R.id.tilFecha).getError()); assertNotNull(layout(R.id.tilHora).getError());
        assertNull(repository.operation);
    }
    @Test public void emptyAmountAlsoClearsErrorsFromPreviousAttempt() throws Exception {
        layout(R.id.tilFecha).setError("Anterior"); layout(R.id.tilMoneda).setError("Anterior");
        edit("etMonto", ""); save();
        assertNull(layout(R.id.tilFecha).getError()); assertNull(layout(R.id.tilMoneda).getError());
        assertEquals(View.VISIBLE, root.findViewById(R.id.tvMontoError).getVisibility());
        assertNull(repository.operation);
    }

    private static final class FakeRepository implements ITransactionRepository {
        String operation, currency, origin, destination, note;
        long id, categoryId; double amount;
        SimpleCb createCallback; VoidCb operationCallback; ListCb listCallback;
        public void list(int year, int month, TransaccionFiltro filter, ListCb cb) { operation="list"; listCallback=cb; }
        public void create(long category, boolean income, double value, String text, long date, String unit, String account, SimpleCb cb) {
            operation="create"; categoryId=category; amount=value; note=text; currency=unit; origin=account; createCallback=cb;
        }
        public void update(long key, long category, boolean income, double value, String text, long date, String unit, String account, VoidCb cb) {
            operation="update"; id=key; categoryId=category; amount=value; operationCallback=cb;
        }
        public void createTransfer(String source, String target, double value, String text, long date, String unit, SimpleCb cb) {
            operation="createTransfer"; origin=source; destination=target; amount=value; createCallback=cb;
        }
        public void updateTransfer(long key, String source, String target, double value, String text, long date, String unit, VoidCb cb) {
            operation="updateTransfer"; id=key; origin=source; destination=target; amount=value; operationCallback=cb;
        }
        public void delete(long key, VoidCb cb) { operation="delete"; id=key; operationCallback=cb; }
    }
}
