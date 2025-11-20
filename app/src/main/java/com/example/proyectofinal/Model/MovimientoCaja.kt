package com.example.proyectofinal.Model

data class MovimientoCaja(
    val id: Long?,
    val tipo: String,
    val monto: Double,
    val descripcion: String,
    val fecha: String?,
    val administrador: Administrador?,
    val caja: Caja?
)
