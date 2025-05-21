package com.example.proyectofinal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectofinal.Api.ApiClient
import com.example.proyectofinal.Model.Producto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException



class ProductoViewModel : ViewModel() {

    private val _productos = MutableStateFlow<List<Producto>>(emptyList())
    val productos: StateFlow<List<Producto>> = _productos

    private val _mensaje = MutableStateFlow("")
    val mensaje: StateFlow<String> = _mensaje

    fun obtenerProductos() {
        viewModelScope.launch {
            try {
                val response = ApiClient.apiService.getProductos()
                _productos.value = response
            } catch (e: Exception) {
                _mensaje.value = "Error al obtener productos: ${e.localizedMessage}"
            }
        }
    }

    fun agregarProducto(
        nombre: String,
        precio: Double,
        cantidad: Int,
        idProveedor: Int,
        idAdministrador: Int,
        onResult: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val producto = Producto(
                    id = 0, // o null, si el backend lo permite
                    nombre = nombre,
                    precio = precio,
                    cantidad = cantidad,
                    idProveedor = idProveedor,
                    idAdministrador = idAdministrador
                )

                val response = ApiClient.apiService.agregarProducto(producto)

                if (response.isSuccessful) {
                    onResult("Producto agregado correctamente")
                    obtenerProductos() // Actualiza la lista
                } else {
                    onResult("Error: ${response.code()}")
                }
            } catch (e: HttpException) {
                onResult("Error HTTP: ${e.code()}")
            } catch (e: Exception) {
                onResult("Error al agregar producto: ${e.localizedMessage}")
            }
        }
    }
}


