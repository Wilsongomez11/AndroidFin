package com.example.proyectofinal.ViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectofinal.Api.ApiClient
import com.example.proyectofinal.Model.Mesero
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MeseroViewModel : ViewModel() {

    private val _meseroNombre = MutableStateFlow("Mesero")
    val meseroNombre: StateFlow<String> = _meseroNombre

    private val _pedidosPendientes = MutableStateFlow(0)
    val pedidosPendientes: StateFlow<Int> = _pedidosPendientes

    private val _meseroActual = MutableStateFlow<Mesero?>(null)
    val meseroActual: StateFlow<Mesero?> = _meseroActual.asStateFlow()

    fun establecerMeseroActual(id: Long, nombre: String) {
        val mesero = Mesero(
            id = id,
            nombre = nombre,
            correo = "",
            telefono = "",
            username = "",
            password = ""
        )
        _meseroActual.value = mesero
        _meseroNombre.value = nombre
    }

    fun cargarDatos() {
        viewModelScope.launch {
            try {
                Log.d("MESERO_VM", "Cargando datos del mesero...")

                val response = ApiClient.apiService.getPedidos()

                if (response.isSuccessful) {
                    val pedidos = response.body() ?: emptyList()

                    val pendientes = pedidos.count { pedido ->
                        val estado = pedido.estado?.lowercase() ?: ""
                        estado !in listOf("pagado", "devuelto", "parcialmente devuelto")
                    }

                    _pedidosPendientes.value = pendientes
                    Log.d("MESERO_VM", "Pedidos pendientes: $pendientes")
                } else {
                    Log.e("MESERO_VM", "Error al cargar pedidos: ${response.code()}")
                }

            } catch (e: Exception) {
                Log.e("MESERO_VM", "Excepción al cargar datos", e)
            }
        }
    }

    fun actualizarPedidosPendientes() {
        cargarDatos()
    }
}