package com.example.proyectofinal.Model

data class Pedido(
    val id: Long? = null,
    val fecha: String? = null,
    val estado: String = "Pendiente",
    val total: Double = 0.0,
    val mesa: String? = null,
    val cliente: Cliente? = null,
    val mesero: Mesero? = null,
    val detalles: List<DetallePedido> = emptyList()
)

data class DetallePedido(
    val id: Long? = null,
    val producto: Producto,
    val cantidad: Int,
    val precioUnitario: Double
)
