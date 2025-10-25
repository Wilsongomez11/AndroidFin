package com.example.proyectofinal.Model

import kotlinx.serialization.Serializable

@Serializable
data class Pizzero(
    val id: Long? = null,
    val nombre: String,
    val telefono: String,
    val direccion: String,
    val username: String,
    val password: String
)
