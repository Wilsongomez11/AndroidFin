package com.example.proyectofinal.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectofinal.Api.ApiClient
import com.example.proyectofinal.Model.Insumo
import com.example.proyectofinal.Model.InsumoDTO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException

class InsumoViewModel : ViewModel() {

    private val _insumos = MutableStateFlow<List<Insumo>>(emptyList())
    val insumos: StateFlow<List<Insumo>> = _insumos

    private val _mensaje = MutableStateFlow("")
    val mensaje: StateFlow<String> = _mensaje

    private val api = ApiClient.apiService

    fun obtenerInsumos() {
        viewModelScope.launch {
            try {
                val response = api.getInsumos()
                if (response.isSuccessful) {
                    _insumos.value = response.body() ?: emptyList()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun agregarInsumo(
        nombre: String,
        unidad: String,
        cantidadActual: Int,
        cantidadMinima: Int,
        onResult: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val dto = InsumoDTO(nombre, unidad, cantidadActual, cantidadMinima)
                val response = api.agregarInsumo(dto)

                if (response.isSuccessful) {
                    val nuevo = response.body()
                    if (nuevo != null) {
                        _insumos.value = _insumos.value + nuevo
                    }
                    onResult(" Insumo agregado correctamente")
                } else {
                    onResult(" Error: ${response.code()}")
                }
            } catch (e: HttpException) {
                onResult(" Error HTTP: ${e.code()}")
            } catch (e: Exception) {
                onResult("Error: ${e.localizedMessage}")
            }
        }
    }

    fun actualizarInsumo(id: Long, insumo: Insumo, onResult: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val dto = InsumoDTO(
                    nombre = insumo.nombre,
                    unidadMedida = insumo.unidadMedida,
                    cantidadActual = insumo.cantidadActual.toInt(),
                    cantidadMinima = insumo.cantidadMinima.toInt()
                )
                val response = api.actualizarInsumo(id, dto)
                if (response.isSuccessful) {
                    // Actualiza el insumo en la lista local sin recargar todo
                    _insumos.value = _insumos.value.map {
                        if (it.id == id) insumo else it
                    }
                    onResult("Insumo actualizado correctamente ")
                } else {
                    onResult("Error ${response.code()}")
                }
            } catch (e: Exception) {
                onResult("Error: ${e.localizedMessage}")
            }
        }
    }

    fun eliminarInsumo(id: Long, onResult: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val response = api.eliminarInsumo(id)
                if (response.isSuccessful) {
                    // Elimina localmente sin recargar toda la lista
                    _insumos.value = _insumos.value.filterNot { it.id == id }
                    onResult("Insumo eliminado correctamente ")
                } else {
                    onResult("Error ${response.code()}")
                }
            } catch (e: Exception) {
                onResult("Error: ${e.localizedMessage}")
            }
        }
    }

    fun verificarBajoStock(onResult: (List<Insumo>) -> Unit) {
        viewModelScope.launch {
            try {
                val response = api.getInsumosBajoStock()
                if (response.isSuccessful) {
                    onResult(response.body() ?: emptyList())
                } else {
                    onResult(emptyList())
                }
            } catch (e: Exception) {
                onResult(emptyList())
            }
        }
    }
}