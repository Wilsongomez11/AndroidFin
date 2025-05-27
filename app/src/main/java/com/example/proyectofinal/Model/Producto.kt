package com.example.proyectofinal.Model

data class Producto(
    val id: Long? = null,
    val nombre: String,
    val precio: Double,
    val cantidad: Int,
    val idProveedor: Long,
    val idAdministrador: Long
)




