package com.example.finanzas.data.repository

import android.content.Context
import android.os.Looper
import androidx.test.platform.app.InstrumentationRegistry
import com.example.finanzas.data.local.LocalRepository
import com.example.finanzas.data.model.Transaccion
import com.example.finanzas.util.Prefs
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.After
import org.junit.Test
import java.util.Calendar
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

/** Integration through the production adapter, TransService and Room on a test device. */
class TransactionRepositoryInstrumentedTest {
    private lateinit var context: Context
    private lateinit var local: LocalRepository
    private lateinit var repository: ITransactionRepository
    private var category = 0L
    private val date = Calendar.getInstance().apply { set(2026, 9, 11, 12, 0, 0) }.timeInMillis

    @Before fun setup() = runBlocking {
        context = InstrumentationRegistry.getInstrumentation().targetContext
        local = LocalRepository.getInstance(context)
        val id = IntArray(1)
        val email = "adapter-${System.nanoTime()}@example.test"
        assertTrue(local.registerUser("Adapter test", email, "test-only-123", id))
        Prefs.setUserSession(context, id[0].toLong(), email, "Adapter test")
        category = local.listCategorias().first { it.esIngreso }.id.toLong()
        repository = TransactionRepository(context)
    }

    @After fun cleanup() = runBlocking {
        local.deleteCurrentUserFinancialData()
        Prefs.clearAuth(context)
    }

    private class Result<T> {
        val latch = CountDownLatch(1)
        var value: T? = null
        var error: String? = null
        var main = false
        var returned = false
        var afterReturn = false
        fun finish(result: T?, failure: String?) {
            value = result; error = failure; main = Looper.myLooper() == Looper.getMainLooper()
            afterReturn = returned; latch.countDown()
        }
        fun await(): T {
            assertTrue("Callback timeout", latch.await(15, TimeUnit.SECONDS))
            assertTrue("Callback must run on main", main)
            assertTrue("Operation must return before completion", afterReturn)
            assertNull(error)
            return requireNotNull(value)
        }
    }
    private fun create(action: (ITransactionRepository.SimpleCb) -> Unit): Int {
        val result = Result<Int>()
        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            action(object : ITransactionRepository.SimpleCb {
                override fun onOk(newId: Int) = result.finish(newId, null)
                override fun onError(message: String?) = result.finish(null, message ?: "Failure")
            })
            result.returned = true
        }
        return result.await()
    }
    private fun change(action: (ITransactionRepository.VoidCb) -> Unit) {
        val result = Result<Boolean>()
        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            action(object : ITransactionRepository.VoidCb {
                override fun onOk() = result.finish(true, null)
                override fun onError(message: String?) = result.finish(null, message ?: "Failure")
            })
            result.returned = true
        }
        assertTrue(result.await())
    }
    private fun list(): List<Transaccion> {
        val result = Result<List<Transaccion>>()
        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            repository.list(2026, 10, null, object : ITransactionRepository.ListCb {
                override fun onOk(items: MutableList<out Transaccion>?) = result.finish(items?.toList(), null)
                override fun onError() = result.finish(null, "List failure")
            })
            result.returned = true
        }
        return result.await()
    }
    @Test fun createEditListAndDeletePersistThroughAsyncAdapter() {
        val id = create { repository.create(category, true, 100.0, "Ingreso", date, "PEN", "CARD", it) }
        assertTrue(id > 0)
        change { repository.update(id.toLong(), category, true, 150.0, "Ingreso editado", date, "PEN", "CARD", it) }
        val tx = list().single { it.id == id }
        assertEquals(150.0, tx.monto, 0.001)
        assertEquals("Ingreso editado", tx.nota)
        change { repository.delete(id.toLong(), it) }
        assertFalse(list().any { it.id == id })
    }
    @Test fun transferCreateEditDeletePreserveBalanceAndDestination() {
        create { repository.create(category, true, 200.0, "Fondos", date, "PEN", "CARD", it) }
        val id = create { repository.createTransfer("CARD", "CASH", 50.0, "Retiro", date, "PEN", it) }
        change { repository.updateTransfer(id.toLong(), "CARD", "CASH", 75.0, "Retiro editado", date, "PEN", it) }
        val tx = list().single { it.id == id }
        assertTrue(tx.isTransfer)
        assertEquals(75.0, tx.monto, 0.001)
        assertEquals("CASH", tx.transferDestinationAccountType)
        runBlocking {
            val summary = local.buildHomeSummary(2026, 10)
            assertEquals(200.0, summary.saldoActualTotal, 0.001)
            assertEquals(75.0, summary.efectivo, 0.001)
            assertEquals(125.0, summary.tarjetaCuenta, 0.001)
        }
        change { repository.delete(id.toLong(), it) }
        assertFalse(list().any { it.id == id })
    }
    @Test fun insufficientBalanceReturnsErrorWithoutCreatingTransfer() {
        val result = Result<String>()
        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            repository.createTransfer("CARD", "CASH", 999.0, "Sin fondos", date, "PEN", object : ITransactionRepository.SimpleCb {
                override fun onOk(newId: Int) = result.finish(null, "Unexpected success")
                override fun onError(message: String?) = result.finish(message, null)
            })
            result.returned = true
        }
        assertTrue(result.await().isNotBlank())
        assertTrue(list().isEmpty())
    }
}
