package com.example.proyectofinal.Model

data class ProductoDTO(
    val nombre: String,
    val precio: Double,
    val cantidad: Int,
    val idProveedor: Long,
    val idAdministrador: Long
)
fun Producto.toDTO(): ProductoDTO {
    return ProductoDTO(
        nombre = this.nombre,
        precio = this.precio,
        cantidad = this.cantidad,
        idProveedor = this.idProveedor,
        idAdministrador = this.idAdministrador
    )
}



