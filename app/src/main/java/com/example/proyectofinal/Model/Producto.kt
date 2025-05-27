package com.example.proyectofinal.Model

data class Producto(
    val id: Int = 0,
    val nombre: String,
    val precio: Double,
    val cantidad: Int,
    val idProveedor: Int,
    val idAdministrador: Int
)

fun Producto.toDTO(): ProductoDTO {
    return ProductoDTO(
        nombre = nombre,
        precio = precio,
        cantidad = cantidad,
        idProveedor = idProveedor,
        idAdministrador = idAdministrador
    )
}



