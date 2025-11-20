package com.example.proyectofinal.ViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectofinal.Api.ApiClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MesaViewModel : ViewModel() {
    private val _mesasOcupadas = MutableStateFlow<Set<Int>>(emptySet())
    val mesasOcupadas: StateFlow<Set<Int>> = _mesasOcupadas.asStateFlow()

    fun cargarMesasOcupadas() {
        viewModelScope.launch {
            try {
                val response = ApiClient.apiService.getMesasOcupadas()
                if (response.isSuccessful) {
                    _mesasOcupadas.value = response.body()?.toSet() ?: emptySet()
                }
            } catch (e: Exception) {
                Log.e("MesaViewModel", "Error", e)
            }
        }
    }
}