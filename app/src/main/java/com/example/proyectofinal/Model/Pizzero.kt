package com.example.proyectofinal.Model

import kotlinx.serialization.Serializable

@Serializable
data class Pizzero(
    val id: Int,
    val nombre: String,
    val telefono: String,
    val direccion: String
)
