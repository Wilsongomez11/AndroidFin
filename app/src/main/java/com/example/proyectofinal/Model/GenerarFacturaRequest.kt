package com.example.proyectofinal.Model

data class GenerarFacturaRequest(
    val pedidoId: Long,
    val metodoPago: String,
    val propina: Double,
    val clienteNombre: String? = null,
    val clienteDocumento: String? = null
)
