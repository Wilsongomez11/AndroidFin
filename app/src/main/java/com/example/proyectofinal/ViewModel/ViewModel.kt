package com.example.proyectofinal.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectofinal.Api.ApiClient
import com.example.proyectofinal.Model.Administrador
import com.example.proyectofinal.Model.Producto
import com.example.proyectofinal.Model.toDTO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val _administradores = MutableStateFlow<List<Administrador>>(emptyList())
    val administradores: StateFlow<List<Administrador>> = _administradores

    private val _productos = MutableStateFlow<List<Producto>>(emptyList())
    val productos: StateFlow<List<Producto>> = _productos

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        getAdministradores()
        getProductos()
    }

    private fun getAdministradores() {
        viewModelScope.launch {
            try {
                val response = ApiClient.administradorService.getAdministradores()
                if (response.isSuccessful) {
                    _administradores.value = response.body() ?: emptyList()
                } else {
                    _error.value = "Error al cargar administradores: ${response.code()}"
                }
            } catch (e: Exception) {
                _error.value = "Error al cargar administradores: ${e.localizedMessage}"
            }
        }
    }

    private fun getProductos() {
        viewModelScope.launch {
            try {
                val response = ApiClient.administradorService.getProductos()
                if (response.isSuccessful) {
                    _productos.value = response.body() ?: emptyList()
                } else {
                    _error.value = "Error al cargar productos: ${response.code()}"
                }
            } catch (e: Exception) {
                _error.value = "Error al cargar productos: ${e.localizedMessage}"
            }
        }
    }

    fun agregarProducto(producto: Producto) {
        viewModelScope.launch {
            try {
                val response = ApiClient.apiService.agregarProducto(producto.toDTO())
                if (response.isSuccessful) {
                    getProductos()
                } else {
                    _error.value = "Error al agregar el producto: ${response.code()}"
                }
            } catch (e: Exception) {
                _error.value = "Error al agregar el producto: ${e.localizedMessage}"
            }
        }
    }


    fun editarAdministrador(id: Long, nuevoNombre: String, nuevoCargo: String, nuevoPassword: String, nuevoUsername: String) {
        viewModelScope.launch {
            try {
                val adminActual = _administradores.value.find { it.id == id }
                if (adminActual != null) {
                    val adminEditado = adminActual.copy(nombre = nuevoNombre, cargo = nuevoCargo, password = nuevoPassword, username = nuevoUsername)
                    val response = ApiClient.apiService.actualizarAdministrador(id, adminEditado)
                    if (response.isSuccessful) {
                        getAdministradores()
                    } else {
                        _error.value = "Error al actualizar administrador: ${response.code()}"
                    }
                }
            } catch (e: Exception) {
                _error.value = "Error al actualizar administrador: ${e.localizedMessage}"
            }
        }
    }
}

