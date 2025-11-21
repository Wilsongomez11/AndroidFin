package com.example.proyectofinal.ViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectofinal.Api.ApiClient
import com.example.proyectofinal.Model.Pedido
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

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

                val response = withContext(Dispatchers.IO) {
                    ApiClient.apiService.crearPedido(pedido)
                }

                if (response.isSuccessful) {
                    val msg = "Pedido creado"
                    _mensaje.value = msg
                    onResult(msg)

                    obtenerPedidos(true)
                    cargarEstadoMesas()
                } else {
                    val msg = extraerMensajeError(response.errorBody()?.string())
                    _errorDetallado.value = msg
                    onResult(msg)
                }
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun actualizarPedido(pedido: Pedido, onResult: (String) -> Unit) {
        val id = pedido.id ?: return
        val estado = pedido.estado ?: return

        actualizarEstadoPedido(id, estado)
        onResult("Pedido actualizado")
    }

    fun obtenerPedidos(forceRefresh: Boolean = false) {
        val now = System.currentTimeMillis()

        if (!forceRefresh && _pedidos.value.isNotEmpty() &&
            (now - lastFetchTime) < CACHE_DURATION) return

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
                }
            } finally {
                isFetching = false
                _isLoading.value = false
            }
        }
    }

    fun actualizarEstadoPedido(id: Long, nuevoEstado: String) {
        viewModelScope.launch {
            try {
                Log.d("PedidoVM", "Actualizando estado backend...")

                val response = withContext(Dispatchers.IO) {
                    ApiClient.apiService.actualizarEstadoPedido(id, nuevoEstado)
                }

                if (response.isSuccessful) {
                    _mensaje.value = "Estado actualizado"

                    delay(350)
                    obtenerPedidos(true)
                    cargarEstadoMesas()
                } else {
                    val msg = extraerMensajeError(response.errorBody()?.string())
                    _errorDetallado.value = msg
                }

            } catch (e: Exception) {
                _errorDetallado.value = e.localizedMessage
            }
        }
    }

    fun eliminarPedido(id: Long) {
        viewModelScope.launch {
            try {
                _isLoading.value = true

                val response = withContext(Dispatchers.IO) {
                    ApiClient.apiService.eliminarPedido(id)
                }

                if (response.isSuccessful) {
                    obtenerPedidos(true)
                    cargarEstadoMesas()
                } else {
                    _errorDetallado.value = extraerMensajeError(response.errorBody()?.string())
                }

            } finally {
                _isLoading.value = false
            }
        }
    }

    fun cargarEstadoMesas() {
        viewModelScope.launch {
            try {
                val response = ApiClient.apiService.getEstadoMesas()
                if (response.isSuccessful) {
                    _estadoMesas.value = response.body() ?: emptyMap()
                }
            } catch (e: Exception) {
                Log.e("PedidoVM", "Error cargando mesas", e)
            }
        }
    }

    private fun extraerMensajeError(errorBody: String?): String {
        return try {
            if (errorBody.isNullOrBlank()) return "Error desconocido"
            val json = JSONObject(errorBody)
            json.optString("message").ifBlank {
                json.optString("error", "Error del servidor")
            }
        } catch (e: Exception) {
            "Error procesando respuesta"
        }
    }
    fun limpiarError() {
        _mensaje.value = ""
        _errorDetallado.value = null
    }
    fun refrescar() {
        obtenerPedidos(true)
    }
    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing

    fun setRefreshing(value: Boolean) {
        _isRefreshing.value = value
    }

}
