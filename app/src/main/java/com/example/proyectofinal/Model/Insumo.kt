package com.example.proyectofinal.Model

data class Insumo(
    val id: Long = 0,
    val nombre: String,
    val unidadMedida: String,
    val cantidadActual: Double,
    val cantidadMinima: Double
)
data class InsumoDTO(
    val nombre: String,
    val unidadMedida: String,
    val cantidadActual: Int,
    val cantidadMinima: Int
)
