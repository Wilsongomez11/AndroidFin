package com.example.proyectofinal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectofinal.Api.ApiClient
import com.example.proyectofinal.Api.ProductoInsumoService
import com.example.proyectofinal.Model.*
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException

class ProductoViewModel : ViewModel() {

    private val productoInsumoService: ProductoInsumoService = ApiClient.productoInsumoService

    val relaciones = MutableStateFlow<List<ProductoInsumo>>(emptyList())
    private val _productos = MutableStateFlow<List<Producto>>(emptyList())
    val productos: StateFlow<List<Producto>> = _productos

    private val _mensaje = MutableStateFlow("")
    val mensaje: StateFlow<String> = _mensaje

    fun obtenerProductos() {
        viewModelScope.launch {
            try {
                val response = ApiClient.apiService.getProductos()
                if (response.isSuccessful) {
                    _productos.value = response.body() ?: emptyList()
                } else {
                    _mensaje.value = "Error del servidor: ${response.code()}"
                }
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
                val productoDTO = ProductoDTO(
                    nombre = nombre,
                    precio = precio,
                    cantidad = cantidad,
                    idProveedor = idProveedor.toLong(),
                    idAdministrador = idAdministrador.toLong()
                )

                val response = ApiClient.apiService.agregarProducto(productoDTO)

                if (response.isSuccessful) {
                    obtenerProductos()
                    onResult("✅ Producto agregado correctamente")
                } else {
                    val errorBody = response.errorBody()?.string()
                    onResult("❌ Error al agregar producto: ${response.code()} - $errorBody")
                }
            } catch (e: HttpException) {
                onResult("Error HTTP: ${e.code()}")
            } catch (e: Exception) {
                onResult("⚠️ Error al agregar producto: ${e.localizedMessage}")
            }
        }
    }

    fun eliminarProducto(id: Long?, onResult: (String) -> Unit = {}) {
        viewModelScope.launch {
            try {
                if (id == null) {
                    onResult("ID de producto inválido")
                    return@launch
                }

                val response = ApiClient.apiService.eliminarProducto(id)
                if (response.isSuccessful) {
                    // Elimina directamente el producto de la lista local
                    _productos.value = _productos.value.filterNot { it.id == id }
                    onResult("✅ Producto eliminado correctamente")
                } else {
                    onResult("❌ Error al eliminar: ${response.code()}")
                }
            } catch (e: Exception) {
                onResult("⚠️ Error al eliminar: ${e.localizedMessage}")
            }
        }
    }

    fun fabricarProducto(id: Long, cantidad: Int, onResult: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val response = ApiClient.apiService.fabricarProducto(id, cantidad)
                if (response.isSuccessful) {
                    onResult("Producto fabricado correctamente")
                    obtenerProductos()
                } else {
                    onResult("Error: ${response.code()}")
                }
            } catch (e: Exception) {
                onResult("Error al fabricar: ${e.localizedMessage}")
            }
        }
    }

    // ------------------------------------------------------------------
    // Relaciones producto - insumo
    // ------------------------------------------------------------------
    fun obtenerRelacionesPorProducto(idProducto: Long) {
        viewModelScope.launch {
            try {
                val response = productoInsumoService.getRelacionesPorProducto(idProducto)
                if (response.isSuccessful) {
                    relaciones.value = response.body() ?: emptyList()
                } else {
                    _mensaje.value = "Error al obtener relaciones: ${response.code()}"
                }
            } catch (e: Exception) {
                _mensaje.value = "Error al obtener relaciones: ${e.localizedMessage}"
            }
        }
    }

    fun agregarRelacion(relacion: ProductoInsumo) {
        viewModelScope.launch {
            try {
                val productoId = relacion.producto?.id
                val insumoId = relacion.insumo?.id

                if (productoId == null || insumoId == null) {
                    _mensaje.value = "Error: ID de producto o insumo no definido"
                    return@launch
                }

                // Creamos el objeto que el backend espera (con producto e insumo anidados)
                val relacionEnviar = ProductoInsumo(
                    id = null,
                    producto = Producto(
                        id = productoId,
                        nombre = "",
                        precio = 0.0,
                        cantidad = 0,
                        idProveedor = 0L,
                        idAdministrador = 0L
                    ),
                    insumo = Insumo(
                        id = insumoId,
                        nombre = "",
                        unidadMedida = "",
                        cantidadActual = 0.0,
                        cantidadMinima = 0.0
                    ),
                    cantidadUsada = relacion.cantidadUsada
                )

                val response = productoInsumoService.agregarRelacion(relacionEnviar)

                if (response.isSuccessful) {
                    obtenerRelacionesPorProducto(productoId)
                    _mensaje.value = "✅ Relación agregada correctamente"
                } else {
                    val errorBody = response.errorBody()?.string()
                    _mensaje.value = "❌ Error al agregar relación: ${response.code()} - $errorBody"
                }
            } catch (e: Exception) {
                _mensaje.value = "⚠️ Error al agregar relación: ${e.localizedMessage}"
                e.printStackTrace()
            }
        }
    }


    fun actualizarProducto(id: Long, producto: ProductoDTO, onResult: (String) -> Unit = {}) {
        viewModelScope.launch {
            try {
                val productoDTO = ProductoDTO(
                    nombre = producto.nombre,
                    precio = producto.precio,
                    cantidad = producto.cantidad,
                    idProveedor = producto.idProveedor.takeIf { it != 0L } ?: 1L,
                    idAdministrador = producto.idAdministrador.takeIf { it != 0L } ?: 1L
                )

                println("➡️ Enviando PUT /productos/$id BODY: ${Gson().toJson(productoDTO)}")

                val response = ApiClient.apiService.actualizarProducto(id, productoDTO)

                if (response.isSuccessful) {
                    val updatedList = _productos.value.map {
                        if (it.id == id) it.copy(
                            nombre = producto.nombre,
                            precio = producto.precio,
                            cantidad = producto.cantidad
                        ) else it
                    }
                    _productos.value = updatedList

                    onResult("✅ Producto actualizado correctamente")
                    obtenerProductos()
                } else {
                    val error = response.errorBody()?.string()
                    println("❌ Error ${response.code()}: $error")
                    onResult("Error ${response.code()}: $error")
                }
            } catch (e: Exception) {
                onResult("⚠️ Error al conectar con el servidor: ${e.localizedMessage}")
                e.printStackTrace()
            }
        }
    }
}
