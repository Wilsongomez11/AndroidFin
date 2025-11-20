package com.example.proyectofinal.Model

data class Pedido(
    val id: Long? = null,
    val fecha: String? = null,
    val estado: String,
    val total: Double,
    val mesa: Mesa?,               // ← CORREGIDO
    val cliente: Cliente?,
    val mesero: Mesero? = null,
    val detalles: List<DetallePedido>
)

data class DetallePedido(
    val id: Long? = null,
    val producto: Producto,
    val cantidad: Int,
    val precioUnitario: Double
)
