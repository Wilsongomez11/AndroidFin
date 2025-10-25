package com.example.proyectofinal.Model

data class Producto(
    val id: Long,
    val nombre: String,
    val precio: Double,
    val cantidad: Int,
    val idProveedor: Long,
    val idAdministrador: Long
)





