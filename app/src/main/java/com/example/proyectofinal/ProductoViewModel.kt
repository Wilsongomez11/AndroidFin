package com.example.proyectofinal

import androidx.lifecycle.ViewModel

class ProductoViewModel : ViewModel() {
    fun agregarProducto(
        nombre: String,
        precio: Double,
        cantidad: Int,
        idProveedor: Int,
        idAdministrador: Int,
        onResult: (String) -> Unit
    ) {
        onResult("Producto registrado exitosamente")
    }
}
