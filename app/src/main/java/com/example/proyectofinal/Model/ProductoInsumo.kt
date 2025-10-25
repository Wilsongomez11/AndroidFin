package com.example.proyectofinal.Model

data class ProductoInsumo(
    val id: Long? = null,
    val producto: Producto? = null,
    val insumo: Insumo? = null,
    val cantidadUsada: Double
)

data class ProductoInsumoDTO(
    val productoId: Long,
    val insumoId: Long,
    val cantidadUsada: Double
)