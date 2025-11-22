package com.example.proyectofinal.Model

data class ResumenReporte(
    val totalFacturas: Int,
    val totalVentas: Double,
    val totalPropinas: Double,
    val metodoPagoMasUsado: String?,
    val ticketPromedio: Double
)
