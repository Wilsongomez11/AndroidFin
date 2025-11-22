package com.example.proyectofinal.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectofinal.Api.ApiClient
import com.example.proyectofinal.Model.Factura
import com.example.proyectofinal.Model.GenerarFacturaRequest
import com.example.proyectofinal.Model.ResumenReporte
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Response

class FacturaViewModel : ViewModel() {

    fun generarFactura(
        request: GenerarFacturaRequest,
        onResult: (Factura?, String?) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val response = ApiClient.facturaService.generarFactura(request)
                if (response.isSuccessful) {
                    onResult(response.body(), null)
                } else {
                    onResult(null, "Error generando factura ${response.code()}")
                }
            } catch (e: Exception) {
                onResult(null, "Error: ${e.localizedMessage}")
            }
        }
    }

    fun obtenerFactura(
        facturaId: Long,
        onResult: (Factura?, String?) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val response = ApiClient.facturaService.obtenerFactura(facturaId)
                if (response.isSuccessful) {
                    onResult(response.body(), null)
                } else {
                    onResult(null, "Factura no encontrada")
                }
            } catch (e: Exception) {
                onResult(null, e.localizedMessage)
            }
        }
    }

    fun obtenerFacturaPorPedido(
        pedidoId: Long,
        onResult: (Factura?, String?) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val response = ApiClient.facturaService.obtenerFacturaPorPedido(pedidoId)
                if (response.isSuccessful) {
                    onResult(response.body(), null)
                } else {
                    onResult(null, "El pedido no tiene factura")
                }
            } catch (e: Exception) {
                onResult(null, e.localizedMessage)
            }
        }
    }

    fun listarFacturas(
        onResult: (List<Factura>?, String?) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val response = ApiClient.facturaService.listarFacturas()
                if (response.isSuccessful) {
                    onResult(response.body(), null)
                } else {
                    onResult(null, "Error listando facturas")
                }
            } catch (e: Exception) {
                onResult(null, e.localizedMessage)
            }
        }
    }

    fun descargarPdf(
        facturaId: Long,
        onResult: (ByteArray?, String?) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val response: Response<ResponseBody> =
                    ApiClient.facturaService.descargarPdf(facturaId)

                if (response.isSuccessful) {
                    val bytes = response.body()?.bytes()
                    if (bytes != null && bytes.isNotEmpty()) {
                        onResult(bytes, null)
                    } else {
                        onResult(null, "PDF vacío o corrupto")
                    }
                } else {
                    onResult(null, "No se pudo descargar el PDF")
                }
            } catch (e: Exception) {
                onResult(null, "Error: ${e.localizedMessage}")
            }
        }
    }

    private val _reporteFacturas = MutableStateFlow<List<Factura>>(emptyList())
    val reporteFacturas: StateFlow<List<Factura>> = _reporteFacturas

    private val _resumenReporte = MutableStateFlow<ResumenReporte?>(null)
    val resumenReporte: StateFlow<ResumenReporte?> = _resumenReporte

    private val _loadingReporte = MutableStateFlow(false)
    val loadingReporte: StateFlow<Boolean> = _loadingReporte

    private val _errorReporte = MutableStateFlow<String?>(null)
    val errorReporte: StateFlow<String?> = _errorReporte

    fun cargarReporte(tipo: String) {
        viewModelScope.launch {
            _loadingReporte.value = true
            _errorReporte.value = null
            try {
                val response = when (tipo.lowercase()) {
                    "dia" -> ApiClient.apiService.reporteDia()
                    "semana" -> ApiClient.apiService.reporteSemana()
                    "mes" -> ApiClient.apiService.reporteMes()
                    else -> ApiClient.apiService.reporteDia()
                }

                if (response.isSuccessful) {
                    val lista = response.body().orEmpty()
                    _reporteFacturas.value = lista
                    _resumenReporte.value = calcularResumen(lista)
                } else {
                    _errorReporte.value = "Error cargando reporte: ${response.code()}"
                }
            } catch (e: Exception) {
                _errorReporte.value = "Error: ${e.localizedMessage}"
            } finally {
                _loadingReporte.value = false
            }
        }
    }

    private fun calcularResumen(lista: List<Factura>): ResumenReporte {
        val totalFacturas = lista.size
        val totalVentas = lista.sumOf { it.total ?: 0.0 }
        val totalPropinas = lista.sumOf { it.propina ?: 0.0 }

        val metodoPagoMasUsado = lista
            .mapNotNull { it.metodoPago }
            .groupBy { it }
            .maxByOrNull { it.value.size }
            ?.key

        val ticketPromedio = if (totalFacturas > 0) totalVentas / totalFacturas else 0.0

        return ResumenReporte(
            totalFacturas = totalFacturas,
            totalVentas = totalVentas,
            totalPropinas = totalPropinas,
            metodoPagoMasUsado = metodoPagoMasUsado,
            ticketPromedio = ticketPromedio
        )
    }

    fun descargarExcel(
        tipo: String,
        onResult: (ByteArray?, String?) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val response = when (tipo.lowercase()) {
                    "dia" -> ApiClient.apiService.excelDia()
                    "semana" -> ApiClient.apiService.excelSemana()
                    "mes" -> ApiClient.apiService.excelMes()
                    else -> ApiClient.apiService.excelDia()
                }

                if (response.isSuccessful) {
                    val bytes = response.body()?.bytes()
                    onResult(bytes, null)
                } else {
                    onResult(null, "Error descargando Excel: ${response.code()}")
                }
            } catch (e: Exception) {
                onResult(null, "Error: ${e.localizedMessage}")
            }
        }
    }
}
