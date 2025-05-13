package com.example.proyectofinal.Model

data class Cliente(
    val id: Long,
    val nombre: String,
    val mesero: Mesero?
)
