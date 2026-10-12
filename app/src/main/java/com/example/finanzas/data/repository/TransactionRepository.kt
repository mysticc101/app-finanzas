package com.example.finanzas.data.repository

import android.content.Context
import com.example.finanzas.data.api.TransService
import com.example.finanzas.data.model.Transaccion
import com.example.finanzas.data.model.TransaccionFiltro

/** Adaptador asíncrono: conserva el despacho IO/Main y la persistencia de TransService. */
class TransactionRepository(context: Context) : ITransactionRepository {
    private val context = context.applicationContext

    override fun list(anio: Int, mes: Int, filtro: TransaccionFiltro?, cb: ITransactionRepository.ListCb) {
        TransService.list(context, anio, mes, filtro, object : TransService.ListCb {
            override fun onOk(res: List<Transaccion>) = cb.onOk(res)
            override fun onError() = cb.onError()
        })
    }

    override fun create(categoriaId: Long, esIngreso: Boolean, monto: Double, nota: String,
                        fecha: Long, moneda: String, accountType: String, cb: ITransactionRepository.SimpleCb) {
        TransService.create(context, categoriaId, esIngreso, monto, nota, fecha, moneda, accountType, cb.toService())
    }

    override fun update(id: Long, categoriaId: Long, esIngreso: Boolean, monto: Double, nota: String,
                        fecha: Long, moneda: String, accountType: String, cb: ITransactionRepository.VoidCb) {
        TransService.update(context, id, categoriaId, esIngreso, monto, nota, fecha, moneda, accountType, cb.toService())
    }

    override fun createTransfer(origin: String, destination: String, monto: Double, nota: String,
                                fecha: Long, moneda: String, cb: ITransactionRepository.SimpleCb) {
        TransService.createTransfer(context, origin, destination, monto, nota, fecha, moneda, cb.toService())
    }

    override fun updateTransfer(id: Long, origin: String, destination: String, monto: Double, nota: String,
                                fecha: Long, moneda: String, cb: ITransactionRepository.VoidCb) {
        TransService.updateTransfer(context, id, origin, destination, monto, nota, fecha, moneda, cb.toService())
    }

    override fun delete(id: Long, cb: ITransactionRepository.VoidCb) {
        TransService.delete(context, id, cb.toService())
    }

    private fun ITransactionRepository.SimpleCb.toService() = object : TransService.SimpleCb {
        override fun onOk(newId: Int) = this@toService.onOk(newId)
        override fun onError(message: String?) = this@toService.onError(message)
    }

    private fun ITransactionRepository.VoidCb.toService() = object : TransService.VoidCb {
        override fun onOk() = this@toService.onOk()
        override fun onError(message: String?) = this@toService.onError(message)
    }
}
