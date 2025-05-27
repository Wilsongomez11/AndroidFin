package com.example.proyectofinal.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectofinal.Api.AdministradorService
import com.example.proyectofinal.Api.ApiClient
import com.example.proyectofinal.Model.Administrador
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class AdministradorViewModel(
    private val service: AdministradorService = ApiClient.apiService
) : ViewModel() {

    private val _administradores = MutableStateFlow<List<Administrador>>(emptyList())
    val administradores: StateFlow<List<Administrador>> = _administradores

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        cargarAdministradores()
    }

    fun cargarAdministradores() {
        viewModelScope.launch {
            try {
                val response = service.getAdministradores()
                if (response.isSuccessful) {
                    _administradores.value = response.body() ?: emptyList()
                } else {
                    _error.value = "Error al cargar administradores: ${response.code()}"
                }
            } catch (e: Exception) {
                _error.value = "Excepción: ${e.localizedMessage}"
            }
        }
    }

    fun eliminarAdministrador(id: Long?) {
        if (id == null) return

        viewModelScope.launch {
            try {
                val response = service.eliminarAdministrador(id)
                if (response.isSuccessful) {
                    cargarAdministradores()
                } else {
                    _error.value = "Error al eliminar: ${response.code()} - ${response.message()}"
                }
            } catch (e: Exception) {
                _error.value = "Excepción al eliminar: ${e.localizedMessage}"
            }
        }
    }

    fun editarAdministrador(
        id: Long,
        nombre: String,
        cargo: String,
        password: String,
        username: String
    ) {
        viewModelScope.launch {
            try {
                val adminActualizado = Administrador(id = id, username =username, password = password, nombre = nombre, cargo = cargo)

                val response = service.actualizarAdministrador(id, adminActualizado)

                if (response.isSuccessful) {
                    cargarAdministradores()
                } else {
                    _error.value = "Error al actualizar: ${response.code()}"
                }
            } catch (e: Exception) {
                _error.value = "Excepción al actualizar: ${e.localizedMessage}"
            }
        }
    }
}
