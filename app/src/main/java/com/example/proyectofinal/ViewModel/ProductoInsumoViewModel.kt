package com.example.proyectofinal.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectofinal.Api.ApiClient
import com.example.proyectofinal.Model.ProductoInsumo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProductoInsumoViewModel : ViewModel() {

    private val _relaciones = MutableStateFlow<List<ProductoInsumo>>(emptyList())
    val relaciones: StateFlow<List<ProductoInsumo>> = _relaciones

    private val _mensaje = MutableStateFlow("")
    val mensaje: StateFlow<String> = _mensaje

    fun obtenerPorProducto(productoId: Long) {
        viewModelScope.launch {
            try {
                val response = ApiClient.administradorService.getInsumosByProducto(productoId)
                if (response.isSuccessful) {
                    _relaciones.value = response.body() ?: emptyList()
                } else {
                    _mensaje.value = "Error: ${response.code()}"
                }
            } catch (e: Exception) {
                _mensaje.value = "Error al cargar insumos: ${e.localizedMessage}"
            }
        }
    }

    fun agregarRelacion(nuevaRelacion: ProductoInsumo, onResult: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val response = ApiClient.administradorService.crearProductoInsumo(nuevaRelacion)
                if (response.isSuccessful) {
                    obtenerPorProducto(nuevaRelacion.producto?.id ?: 0)
                    onResult("Relación agregada correctamente")
                } else {
                    onResult("Error: ${response.code()}")
                }
            } catch (e: Exception) {
                onResult("Error al crear relación: ${e.localizedMessage}")
            }
        }
    }
}
