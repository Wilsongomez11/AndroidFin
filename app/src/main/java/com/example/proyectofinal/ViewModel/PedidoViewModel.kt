package com.example.proyectofinal.ViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.proyectofinal.Api.ApiClient
import com.example.proyectofinal.Model.Pedido
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException

class PedidoViewModel : ViewModel() {

    private val _pedidos = MutableStateFlow<List<Pedido>>(emptyList())
    val pedidos: StateFlow<List<Pedido>> = _pedidos.asStateFlow()

    private val _mensaje = MutableStateFlow("")
    val mensaje: StateFlow<String> = _mensaje.asStateFlow()

    private val _errorDetallado = MutableStateFlow<String?>(null)
    val errorDetallado: StateFlow<String?> = _errorDetallado.asStateFlow()

    private val _estadoMesas = MutableStateFlow<Map<Int, String>>(emptyMap())
    val estadoMesas: StateFlow<Map<Int, String>> = _estadoMesas.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    private var lastFetchTime = 0L
    private val CACHE_DURATION = 3000L
    private var isFetching = false

    init {
        viewModelScope.launch {
            obtenerPedidos()
            cargarEstadoMesas()
        }
    }

    fun crearPedido(pedido: Pedido, onResult: (String) -> Unit) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _errorDetallado.value = null

                val response = withContext(Dispatchers.IO) {
                    ApiClient.apiService.crearPedido(pedido)
                }

                if (response.isSuccessful) {
                    val msg = "\u2705 Pedido registrado correctamente"
                    _mensaje.value = msg
                    onResult(msg)
                    cargarEstadoMesas()
                    obtenerPedidos(true)
                } else {
                    val mensajeLimpio = extraerMensajeError(response.errorBody()?.string())
                    _mensaje.value = mensajeLimpio
                    _errorDetallado.value = mensajeLimpio
                    onResult(mensajeLimpio)
                }
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun obtenerPedidos(forceRefresh: Boolean = false) {
        val now = System.currentTimeMillis()

        if (!forceRefresh &&
            _pedidos.value.isNotEmpty() &&
            (now - lastFetchTime) < CACHE_DURATION
        ) return

        if (isFetching) return

        isFetching = true
        _isLoading.value = true

        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    ApiClient.apiService.getPedidos()
                }

                if (response.isSuccessful) {
                    _pedidos.value = response.body() ?: emptyList()
                    lastFetchTime = now
                    _mensaje.value = ""
                } else {
                    _mensaje.value = "Error ${response.code()}"
                }
            } catch (e: IOException) {
                _mensaje.value = "\u26A0 Error de conexi\u00F3n"
            } catch (e: HttpException) {
                _mensaje.value = "\u26A0 Error HTTP"
            } catch (e: Exception) {
                _mensaje.value = "\u26A0 Error desconocido"
            } finally {
                isFetching = false
                _isLoading.value = false
                _isRefreshing.value = false
            }
        }
    }

    fun refrescar() {
        _isRefreshing.value = true
        obtenerPedidos(true)
    }

    fun actualizarPedido(pedido: Pedido, onResult: (String) -> Unit) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _errorDetallado.value = null

                val id = pedido.id ?: return@launch

                _pedidos.value = _pedidos.value.map { if (it.id == id) pedido else it }

                val response = withContext(Dispatchers.IO) {
                    ApiClient.apiService.actualizarPedido(id, pedido)
                }

                if (response.isSuccessful) {
                    val msg = "\u2705 Pedido actualizado correctamente"
                    _mensaje.value = msg
                    onResult(msg)
                    cargarEstadoMesas()
                    obtenerPedidos(true)
                } else {
                    val mensajeLimpio = extraerMensajeError(response.errorBody()?.string())
                    _mensaje.value = mensajeLimpio
                    _errorDetallado.value = mensajeLimpio
                    onResult(mensajeLimpio)
                    obtenerPedidos(true)
                }
            } catch (e: Exception) {
                val msg = "\u26A0 ${e.localizedMessage ?: "Error desconocido"}"
                _mensaje.value = msg
                _errorDetallado.value = msg
                onResult(msg)
                obtenerPedidos(true)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun eliminarPedido(id: Long) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _errorDetallado.value = null

                val respaldo = _pedidos.value.find { it.id == id }
                _pedidos.value = _pedidos.value.filterNot { it.id == id }

                val response = withContext(Dispatchers.IO) {
                    ApiClient.apiService.eliminarPedido(id)
                }

                if (response.isSuccessful) {
                    _mensaje.value = "\u2705 Pedido eliminado correctamente"
                    cargarEstadoMesas()
                } else {
                    if (respaldo != null) _pedidos.value = _pedidos.value + respaldo
                    val mensajeLimpio = extraerMensajeError(response.errorBody()?.string())
                    _mensaje.value = mensajeLimpio
                    _errorDetallado.value = mensajeLimpio
                }
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun marcarPedidoComoPagado(id: Long) {
        actualizarEstadoPedido(id, "Pagado")
    }

    fun marcarPedidoComoDevuelto(id: Long) {
        actualizarEstadoPedido(id, "Devuelto")
    }

    fun actualizarEstadoPedido(id: Long, nuevoEstado: String) {
        viewModelScope.launch {
            try {
                _pedidos.value = _pedidos.value.map {
                    if (it.id == id) it.copy(estado = nuevoEstado) else it
                }

                val response = withContext(Dispatchers.IO) {
                    ApiClient.apiService.actualizarEstadoPedido(id, nuevoEstado)
                }

                if (response.isSuccessful) {
                    _mensaje.value = "\u2705 Estado actualizado correctamente"
                    cargarEstadoMesas()
                    obtenerPedidos(true)
                } else {
                    obtenerPedidos(true)
                    _mensaje.value = "\u274C Error al actualizar estado (${response.code()})"
                }
            } catch (e: Exception) {
                obtenerPedidos(true)
                _mensaje.value = "\u26A0 Error: ${e.localizedMessage}"
            }
        }
    }

    fun cargarEstadoMesas() {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    ApiClient.apiService.getPedidos()
                }

                if (response.isSuccessful) {
                    val info = response.body() ?: emptyList()
                    val mesas = mutableMapOf<Int, String>()

                    info.forEach { pedido ->
                        val mesa = pedido.mesa?.id?.toInt()
                        if (mesa != null) {
                            val ocupado = pedido.estado != "Devuelto" && pedido.estado != "Pagado"
                            mesas[mesa] = if (ocupado) "Ocupada" else "Libre"
                        }
                    }

                    _estadoMesas.value = mesas
                }

            } catch (e: Exception) {
                Log.e("PedidoVM", "Error mesas", e)
            }
        }
    }


    private fun extraerMensajeError(errorBody: String?): String {
        return try {
            if (errorBody.isNullOrBlank()) return "\u26A0 Error desconocido"

            val json = JSONObject(errorBody)
            val msg = json.optString("message")
            if (msg.isNotBlank()) return "\u274C $msg"

            val err = json.optString("error")
            if (err.isNotBlank()) return "\u274C $err"

            "\u26A0 Error del servidor"
        } catch (e: Exception) {
            "\u26A0 Error procesando respuesta"
        }
    }

    fun limpiarError() {
        _mensaje.value = ""
        _errorDetallado.value = null
    }


    fun generarFactura(
        pedidoId: Long,
        metodoPago: String,
        propina: Double,
        navController: NavHostController
    ) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _mensaje.value = ""

                val response = ApiClient.apiService.generarFactura(
                    pedidoId = pedidoId,
                    metodoPago = metodoPago,
                    propina = propina
                )

                if (response.isSuccessful) {
                    val factura = response.body()!!
                    navController.navigate("factura/${factura.id}")
                } else {
                    _mensaje.value = "❌ Error generando factura: ${response.code()}"
                }

            } catch (e: Exception) {
                _mensaje.value = "⚠ Error: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
