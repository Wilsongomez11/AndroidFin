package com.example.proyectofinal.Model

data class Cliente(
    val id: Long = 0,
    val nombre: String = "",
    val mesero: Mesero? = null
)
