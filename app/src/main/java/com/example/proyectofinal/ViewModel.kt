package com.example.proyectofinal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectofinal.Model.Administrador
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
}
