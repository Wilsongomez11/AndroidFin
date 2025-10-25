package com.example.proyectofinal.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectofinal.Api.ApiClient
import com.example.proyectofinal.Model.Pedido
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class PedidoViewModel : ViewModel() {

    private val _pedidos = MutableStateFlow<List<Pedido>>(emptyList())
    val pedidos: StateFlow<List<Pedido>> = _pedidos

    private val _mensaje = MutableStateFlow("")
    val mensaje: StateFlow<String> = _mensaje

    // 🔹 Obtener todos los pedidos
    fun obtenerPedidos() {
        viewModelScope.launch {
            try {
                val response = ApiClient.apiService.getPedidos()
                if (response.isSuccessful) {
                    _pedidos.value = response.body() ?: emptyList()
                    _mensaje.value = ""
                } else {
                    _mensaje.value = "Error ${response.code()}: ${response.message()}"
                }
            } catch (e: IOException) {
                _mensaje.value = "Error de conexión al servidor"
            } catch (e: HttpException) {
                _mensaje.value = "Error HTTP: ${e.message}"
            } catch (e: Exception) {
                _mensaje.value = "Error desconocido: ${e.localizedMessage}"
            }
        }
    }

    // 🔹 Crear nuevo pedido
    fun crearPedido(pedido: Pedido, onResult: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val response = ApiClient.apiService.crearPedido(pedido)
                if (response.isSuccessful) {
                    onResult("✅ Pedido registrado correctamente")
                    obtenerPedidos()
                } else {
                    onResult("❌ Error al registrar pedido (${response.code()})")
                }
            } catch (e: IOException) {
                onResult("⚠️ Error de conexión al crear pedido")
            } catch (e: Exception) {
                onResult("⚠️ Error: ${e.localizedMessage}")
            }
        }
    }

    // 🔹 Eliminar pedido
    fun eliminarPedido(id: Long) {
        viewModelScope.launch {
            try {
                val response = ApiClient.apiService.eliminarPedido(id)
                if (response.isSuccessful) {
                    _mensaje.value = "🗑️ Pedido eliminado correctamente"
                    obtenerPedidos()
                } else {
                    _mensaje.value = "❌ Error al eliminar (${response.code()})"
                }
            } catch (e: Exception) {
                _mensaje.value = "⚠️ Error al eliminar pedido: ${e.localizedMessage}"
            }
        }
    }

    // 🔹 Actualizar estado (opcional)
    fun actualizarEstadoPedido(id: Long, nuevoEstado: String) {
        viewModelScope.launch {
            try {
                val response = ApiClient.apiService.actualizarEstadoPedido(id, nuevoEstado)
                if (response.isSuccessful) {
                    _mensaje.value = "✅ Estado actualizado a '$nuevoEstado'"
                    obtenerPedidos()
                } else {
                    _mensaje.value = "❌ Error al actualizar estado (${response.code()})"
                }
            } catch (e: Exception) {
                _mensaje.value = "⚠️ Error: ${e.localizedMessage}"
            }
        }
    }

    // Actualizar pedido completo (detalles, total, mesa, etc.)
    fun actualizarPedido(pedido: Pedido, onResult: (String) -> Unit = {}) {
        viewModelScope.launch {
            try {
                val id = pedido.id ?: run {
                    onResult("⚠️ ID de pedido inválido")
                    return@launch
                }

                val response = ApiClient.apiService.actualizarPedido(id, pedido)

                if (response.isSuccessful) {
                    _pedidos.value = _pedidos.value.map {
                        if (it.id == pedido.id) pedido else it
                    }
                    _mensaje.value = "✅ Pedido actualizado correctamente"
                    onResult("✅ Pedido actualizado correctamente")
                } else {
                    val msg = "❌ Error al actualizar pedido (${response.code()})"
                    _mensaje.value = msg
                    onResult(msg)
                }
            } catch (e: IOException) {
                val msg = "⚠️ Error de conexión al actualizar pedido"
                _mensaje.value = msg
                onResult(msg)
            } catch (e: HttpException) {
                val msg = "⚠️ Error HTTP: ${e.message}"
                _mensaje.value = msg
                onResult(msg)
            } catch (e: Exception) {
                val msg = "⚠️ Error desconocido: ${e.localizedMessage}"
                _mensaje.value = msg
                onResult(msg)
            }
        }
    }
}
