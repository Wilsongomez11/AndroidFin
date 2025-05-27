package com.example.proyectofinal.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectofinal.Api.ApiClient
import com.example.proyectofinal.Model.Administrador
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AdministradorViewModel : ViewModel() {

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
                _administradores.value = ApiClient.apiService.getAdministradores()
            } catch (e: Exception) {
                _error.value = e.localizedMessage
            }
        }
    }

    fun eliminarAdministrador(id: Long?) {
        if (id == null) return

        viewModelScope.launch {
            try {
                val response = ApiClient.apiService.eliminarAdministrador(id.toLong())
                if (response.isSuccessful) {
                    getAdministradores()
                } else {
                    _error.value = "Error al eliminar: ${response.code()} - ${response.message()}"
                }
            } catch (e: Exception) {
                _error.value = "Excepción: ${e.localizedMessage}"
            }
        }
    }

    fun editarAdministrador(id: Long, nuevoNombre: String, nuevoCargo: String) {
        viewModelScope.launch {
            try {
                val administradorActualizado = Administrador(
                    id = id,
                    nombre = nuevoNombre,
                    username = "Wil",
                    password = "1234",
                    cargo = nuevoCargo
                )

                val response =
                    ApiClient.apiService.actualizarAdministrador(id, administradorActualizado)

                if (response.isSuccessful) {
                    getAdministradores()
                } else {
                    _error.value = "Error al editar: ${response.code()} - ${response.message()}"
                }
            } catch (e: Exception) {
                _error.value = "Excepción: ${e.localizedMessage}"
            }
        }
    }
}
