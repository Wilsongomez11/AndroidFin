package com.example.proyectofinal.Model


data class Caja(
    val id: Long?,
    val fecha: String,
    val ingresos: Double,
    val egresos: Double,
    val balance: Double,
    val tipoCierre: String?,
    val administrador: Administrador?
)
