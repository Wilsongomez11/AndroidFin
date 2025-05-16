package com.example.proyectofinal.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectofinal.Api.ApiClient
import com.example.proyectofinal.Api.RetrofitClient
import com.example.proyectofinal.Model.Administrador
import com.example.proyectofinal.Model.Producto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val _administradores = MutableStateFlow<List<Administrador>>(emptyList())
    val administradores: StateFlow<List<Administrador>> = _administradores

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        getAdministradores()
    }

    private fun getAdministradores() {
        viewModelScope.launch {
            try {
                val response = ApiClient.apiService.getAdministradores()
                _administradores.value = response
            } catch (e: Exception) {
                _error.value = "Error al cargar administradores: ${e.localizedMessage}"
            }
        }
    }

    class MainViewModel : ViewModel() {

        private val _productos = MutableStateFlow<List<Producto>>(emptyList())
        val productos: StateFlow<List<Producto>> = _productos

        private val _error = MutableStateFlow<String?>(null)
        val error: StateFlow<String?> = _error

        init {
            // Llamamos a getProductos dentro de una corrutina en el init block
            getProductos()
        }

        private fun getProductos() {
            // Llamamos a la función suspendida dentro de una corrutina
            viewModelScope.launch {
                try {
                    val response = RetrofitClient.api.getProductos()
                    _productos.value = response
                } catch (e: Exception) {
                    _error.value = "Error al cargar productos: ${e.localizedMessage}"
                }
            }
        }

        fun agregarProducto(producto: Producto) {
            viewModelScope.launch {
                try {
                    val response = RetrofitClient.api.agregarProducto(producto)
                    // Actualizamos la lista de productos
                    _productos.value = (_productos.value + response) as List<Producto>
                } catch (e: Exception) {
                    _error.value = "Error al agregar el producto: ${e.localizedMessage}"
                }
            }
        }
    }

}
