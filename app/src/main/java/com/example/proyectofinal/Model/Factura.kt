package com.example.proyectofinal.Model

data class Factura(
    val id: Long?,
    val numeroFactura: String,
    val fecha: String,
    val pedido: Pedido,
    val subtotal: Double,
    val iva: Double,
    val propina: Double,
    val total: Double,
    val metodoPago: String
)
