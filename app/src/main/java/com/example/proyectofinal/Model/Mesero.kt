package com.example.proyectofinal.Model

data class Mesero(
    val id: Long,
    val nombre: String,
    val administrador: Administrador?,
    val clientes: List<Cliente>? = emptyList()
)