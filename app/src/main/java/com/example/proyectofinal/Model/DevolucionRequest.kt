package com.example.proyectofinal.Model

data class DevolucionRequest(
    val monto: Double,
    val reponerStock: Boolean,
    val cantidades: Map<Long, Int>
)

