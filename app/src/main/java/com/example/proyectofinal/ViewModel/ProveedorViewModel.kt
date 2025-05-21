package com.example.proyectofinal.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectofinal.Api.ApiClient
import com.example.proyectofinal.Model.Proveedor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProveedorViewModel : ViewModel() {

    private val _proveedores = MutableStateFlow<List<Proveedor>>(emptyList())
    val proveedores: StateFlow<List<Proveedor>> = _proveedores

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        obtenerProveedores()
    }

    private fun obtenerProveedores() {
        viewModelScope.launch {
            try {
                val response = ApiClient.apiService.getProveedores()
                if (response.isSuccessful && response.body() != null) {
                    _proveedores.value = response.body()!!
                } else {
                    _error.value = "Error al obtener proveedores: ${response.code()}"
                }
            } catch (e: Exception) {
                _error.value = "Error de red: ${e.localizedMessage}"
            }
        }
    }

    fun agregarProveedor(nombre: String, contacto: String) {
        viewModelScope.launch {
            try {
                val proveedor = Proveedor(
                    nombre = nombre,
                    contacto = contacto
                )
                val response = ApiClient.apiService.agregarProveedor(proveedor)
                if (response.isSuccessful && response.body() != null) {
                    _proveedores.value = _proveedores.value + response.body()!!
                } else {
                    _error.value = "Error al agregar proveedor: ${response.code()}"
                }
            } catch (e: Exception) {
                _error.value = "Error al agregar proveedor: ${e.localizedMessage}"
            }
        }
    }
}