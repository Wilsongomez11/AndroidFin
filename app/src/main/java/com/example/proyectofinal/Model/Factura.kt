package com.example.proyectofinal.Model

data class Factura(
    val id: Long?,
    val numero: String?,
    val fechaEmision: String?,
    val clienteNombre: String?,
    val clienteDocumento: String?,
    val metodoPago: String?,
    val subtotal: Double?,
    val propina: Double?,
    val impuestos: Double?,
    val total: Double?,
    val estado: String?,
    val pedido: Pedido?
)
