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
                val response = ApiClient.apiService.getAdministradores()
                _administradores.value = response
            } catch (e: Exception) {
                _error.value = "Error al cargar administradores: ${e.localizedMessage}"
            }
        }
    }
    fun esAdminFijo(id: Long?): Boolean {
        return id == -1L
    }

    private fun getProductos() {
        viewModelScope.launch {
            try {
                val response = ApiClient.apiService.getProductos()
                _productos.value = response
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
}
