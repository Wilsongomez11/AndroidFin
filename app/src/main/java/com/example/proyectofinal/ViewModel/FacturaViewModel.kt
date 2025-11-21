package com.example.proyectofinal.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectofinal.Api.ApiClient
import com.example.proyectofinal.Model.Factura
import com.example.proyectofinal.Model.GenerarFacturaRequest
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
}
