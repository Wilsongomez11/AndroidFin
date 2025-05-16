package com.example.proyectofinal.ViewModel

import com.example.proyectofinal.Api.RetrofitClient

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectofinal.Apia.ApiClient
import com.example.proyectofinal.Model.Proveedor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Response

class ProveedorViewModel : ViewModel() {

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> get() = _error

    fun clearError() {
        _error.value = null
    }

    fun agregarProveedor(proveedor: Proveedor) {
        viewModelScope.launch {
            try {
                val response = ApiClient.apiService.agregarProveedor(proveedor)
                if (response.isSuccessful) {
                    _error.value = "Proveedor agregado correctamente"
                } else {
                    _error.value = "Error al agregar proveedor"
                }
            } catch (e: Exception) {
                _error.value = "Excepción: ${e.localizedMessage}"
            }
        }
    }
}
