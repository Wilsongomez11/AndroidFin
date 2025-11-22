package com.example.proyectofinal.Service

import android.content.Context
import com.example.proyectofinal.Api.ApiClient
import com.example.proyectofinal.Model.Factura
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class FacturaService(private val context: Context? = null) {

    // ============================================
    // 🔹 1. Generar factura en el backend
    // ============================================
    suspend fun generarFacturaRemota(
        pedidoId: Long,
        metodoPago: String,
        propina: Double
    ): Response<Factura> {
        return withContext(Dispatchers.IO) {
            ApiClient.apiService.generarFactura(
                pedidoId = pedidoId,
                metodoPago = metodoPago,
                propina = propina
            )
        }
    }

    // ============================================
    // 🔹 2. Consultar factura por ID
    // ============================================
    suspend fun obtenerFacturaPorId(id: Long): Response<Factura> {
        return withContext(Dispatchers.IO) {
            ApiClient.apiService.getFacturaById(id)
        }
    }

    // ============================================
    // 🔹 3. Generar texto para un PDF simple
    // ============================================
    fun generarContenidoPDF(f: Factura): String {
        return """
            FACTURA #${f.numeroFactura}
            =====================================

            Fecha: ${f.fecha}
            Pedido ID: ${f.pedido.id}

            -------------------------------------
            Subtotal: $${String.format("%.2f", f.subtotal)}
            IVA (19%): $${String.format("%.2f", f.iva)}
            Propina: $${String.format("%.2f", f.propina)}

            TOTAL A PAGAR: $${String.format("%.2f", f.total)}
            -------------------------------------

            Método de pago: ${f.metodoPago}

            ¡Gracias por su compra! 🍕
        """.trimIndent()
    }
}
