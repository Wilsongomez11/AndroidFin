package com.example.proyectofinal.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectofinal.Api.AdministradorService
import com.example.proyectofinal.Api.ApiClient
import com.example.proyectofinal.Model.Administrador
import com.example.proyectofinal.Model.Mesero
import com.example.proyectofinal.Model.Pizzero
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AdministradorViewModel(
    val service: AdministradorService = ApiClient.apiService
) : ViewModel() {

    // 📦 Listas
    private val _administradores = MutableStateFlow<List<Administrador>>(emptyList())
    val administradores: StateFlow<List<Administrador>> = _administradores

    private val _meseros = MutableStateFlow<List<Mesero>>(emptyList())
    val meseros: StateFlow<List<Mesero>> = _meseros

    private val _pizzeros = MutableStateFlow<List<Pizzero>>(emptyList())
    val pizzeros: StateFlow<List<Pizzero>> = _pizzeros

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _administradorActual = MutableStateFlow<Administrador?>(null)
    val administradorActual = _administradorActual

    fun establecerAdministradorActual(admin: Administrador) {
        _administradorActual.value = admin
    }

    // -----------------------------------------------------
    // Cerrar sesión
    // -----------------------------------------------------
    fun cerrarSesion() {
        _administradorActual.value = null
    }

    // -----------------------------------------------------
    // Funciones de carga
    // -----------------------------------------------------

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
                _error.value = "Excepción al cargar: ${e.localizedMessage}"
            }
        }
    }

    fun cargarMeseros() {
        viewModelScope.launch {
            try {
                val response = service.getMeseros()
                if (response.isSuccessful) {
                    _meseros.value = response.body() ?: emptyList()
                } else {
                    _error.value = "Error al cargar meseros: ${response.code()}"
                }
            } catch (e: Exception) {
                _error.value = "Excepción al cargar: ${e.localizedMessage}"
            }
        }
    }

    fun cargarPizzeros() {
        viewModelScope.launch {
            try {
                val response = service.getPizzeros()
                if (response.isSuccessful) {
                    _pizzeros.value = response.body() ?: emptyList()
                } else {
                    _error.value = "Error al cargar pizzeros: ${response.code()}"
                }
            } catch (e: Exception) {
                _error.value = "Excepción al cargar: ${e.localizedMessage}"
            }
        }
    }

    // -----------------------------------------------------
    // Funciones de edición (optimizadas)
    // -----------------------------------------------------

    fun editarAdministrador(
        id: Long,
        nombre: String,
        cargo: String,
        password: String,
        username: String,
        onResult: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val adminActualizado = Administrador(id, username, password, nombre, cargo)
                val response = service.actualizarAdministrador(id, adminActualizado)
                if (response.isSuccessful) {
                    cargarAdministradores()
                    onResult("Administrador actualizado correctamente ✅")
                } else {
                    onResult("Error al actualizar administrador: ${response.code()}")
                }
            } catch (e: Exception) {
                onResult("Error al actualizar: ${e.localizedMessage}")
            }
        }
    }

    fun editarMesero(
        id: Long,
        nombre: String,
        telefono: String,
        correo: String,
        username: String,
        password: String,
        onResult: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val meseroActualizado = Mesero(id, nombre, correo, telefono, username, password)
                val response = service.actualizarMesero(id, meseroActualizado)
                if (response.isSuccessful) {
                    cargarMeseros()
                    onResult("Mesero actualizado correctamente ✅")
                } else {
                    onResult("Error al actualizar mesero: ${response.code()}")
                }
            } catch (e: Exception) {
                onResult("Error al actualizar mesero: ${e.localizedMessage}")
            }
        }
    }

    fun editarPizzero(
        id: Long,
        nombre: String,
        telefono: String,
        direccion: String,
        username: String,
        password: String,
        onResult: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val pizzeroActualizado = Pizzero(id, nombre, telefono, direccion, username, password)
                val response = service.actualizarPizzero(id, pizzeroActualizado)
                if (response.isSuccessful) {
                    cargarPizzeros()
                    onResult("Pizzero actualizado correctamente ✅")
                } else {
                    onResult("Error al actualizar pizzero: ${response.code()}")
                }
            } catch (e: Exception) {
                onResult("Error al actualizar pizzero: ${e.localizedMessage}")
            }
        }
    }


    // -----------------------------------------------------
    // Funciones de eliminación
    // -----------------------------------------------------

    fun eliminarAdministrador(id: Long?) {
        if (id == null) return
        viewModelScope.launch {
            try {
                val response = service.eliminarAdministrador(id)
                if (response.isSuccessful) cargarAdministradores()
                else _error.value = "Error al eliminar administrador: ${response.code()}"
            } catch (e: Exception) {
                _error.value = "Excepción al eliminar administrador: ${e.localizedMessage}"
            }
        }
    }

    fun eliminarMesero(id: Long?) {
        if (id == null) return
        viewModelScope.launch {
            try {
                val response = service.eliminarMesero(id)
                if (response.isSuccessful) cargarMeseros()
                else _error.value = "Error al eliminar mesero: ${response.code()}"
            } catch (e: Exception) {
                _error.value = "Excepción al eliminar mesero: ${e.localizedMessage}"
            }
        }
    }

    fun eliminarPizzero(id: Long?) {
        if (id == null) return
        viewModelScope.launch {
            try {
                val response = service.eliminarPizzero(id)
                if (response.isSuccessful) cargarPizzeros()
                else _error.value = "Error al eliminar pizzero: ${response.code()}"
            } catch (e: Exception) {
                _error.value = "Excepción al eliminar pizzero: ${e.localizedMessage}"
            }
        }
    }
}
