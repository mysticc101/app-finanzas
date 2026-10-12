package com.example.finanzas.data.repository;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.finanzas.data.model.Transaccion;
import com.example.finanzas.data.model.TransaccionFiltro;
import java.util.List;

/**
 * Acceso asíncrono a movimientos y transferencias del formulario.
 * Las operaciones no bloquean al consumidor y notifican el resultado en el hilo
 * principal. Etiquetas, recurrencias y categorías conservan sus servicios propios.
 */
public interface ITransactionRepository {
    interface ListCb {
        void onOk(List<? extends Transaccion> result);
        void onError();
    }
    interface SimpleCb {
        void onOk(int newId);
        void onError(@Nullable String message);
    }
    interface VoidCb {
        void onOk();
        void onError(@Nullable String message);
    }

    void list(int anio, int mes, @Nullable TransaccionFiltro filtro, @NonNull ListCb cb);
    void create(long categoriaId, boolean esIngreso, double monto, @NonNull String nota,
                long fecha, @NonNull String moneda, @NonNull String accountType, @NonNull SimpleCb cb);
    void update(long id, long categoriaId, boolean esIngreso, double monto, @NonNull String nota,
                long fecha, @NonNull String moneda, @NonNull String accountType, @NonNull VoidCb cb);
    void createTransfer(@NonNull String origin, @NonNull String destination, double monto,
                        @NonNull String nota, long fecha, @NonNull String moneda, @NonNull SimpleCb cb);
    void updateTransfer(long id, @NonNull String origin, @NonNull String destination, double monto,
                        @NonNull String nota, long fecha, @NonNull String moneda, @NonNull VoidCb cb);
    void delete(long id, @NonNull VoidCb cb);
}
