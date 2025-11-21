package com.example.proyectofinal.ViewModel

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectofinal.Api.ApiClient
import com.example.proyectofinal.Model.Factura
import com.example.proyectofinal.Model.Pedido
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

class FacturaViewModel : ViewModel() {

    private val _facturaActual = MutableStateFlow<Factura?>(null)
    val facturaActual: StateFlow<Factura?> = _facturaActual

    private val _archivoPDF = MutableStateFlow<File?>(null)
    val archivoPDF: StateFlow<File?> = _archivoPDF

    private val _mensaje = MutableStateFlow("")
    val mensaje: StateFlow<String> = _mensaje

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun cargarFactura(id: Long) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = ApiClient.apiService.getFacturaById(id)
                if (response.isSuccessful) {
                    _facturaActual.value = response.body()
                } else {
                    _mensaje.value = "Error al cargar factura"
                }
            } catch (e: Exception) {
                _mensaje.value = "Error: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun generarFacturaDesdePedido(
        pedido: Pedido,
        metodoPago: String,
        propina: Double
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = ApiClient.apiService.generarFactura(
                    pedidoId = pedido.id!!,
                    metodoPago = metodoPago,
                    propina = propina
                )

                if (response.isSuccessful) {
                    _facturaActual.value = response.body()
                    _mensaje.value = "Factura generada"
                } else {
                    _mensaje.value = "Error generando factura"
                }
            } catch (e: Exception) {
                _mensaje.value = "Error: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun generarPDF(context: Context, factura: Factura) {
        viewModelScope.launch {
            try {
                val file = withContext(Dispatchers.IO) {
                    val pdfFile = File(context.cacheDir, "factura_${factura.id}.pdf")

                    FileOutputStream(pdfFile).use { out ->
                        out.write(generarContenidoPDF(factura).toByteArray())
                    }
                    pdfFile
                }

                _archivoPDF.value = file
                _mensaje.value = "PDF generado"
            } catch (e: Exception) {
                _mensaje.value = "Error creando PDF"
            }
        }
    }

    fun compartirPDF(context: Context) {
        val archivo = _archivoPDF.value ?: return

        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            archivo
        )

        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "application/pdf"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        context.startActivity(Intent.createChooser(shareIntent, "Compartir factura"))
    }

    fun verPDF(context: Context) {
        val archivo = _archivoPDF.value ?: return

        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            archivo
        )

        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "application/pdf")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        try {
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "No hay lector de PDF", Toast.LENGTH_SHORT).show()
        }
    }

    private fun generarContenidoPDF(f: Factura): String {
        return """
            FACTURA #${f.numeroFactura}
            ================================
            Fecha: ${f.fecha}
            Pedido: ${f.pedido.id}

            Subtotal: $${f.subtotal}
            IVA: $${f.iva}
            Propina: $${f.propina}

            TOTAL A PAGAR: $${f.total}

            Método de pago: ${f.metodoPago}

            ¡Gracias por su compra!
        """.trimIndent()
    }
}
