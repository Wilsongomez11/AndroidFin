package com.example.proyectofinal.Model

import kotlinx.serialization.Serializable

@Serializable
data class Mesero(
    val id: Long? = null,
    val nombre: String,
    val correo: String,
    val telefono: String,
    val username: String,
    val password: String
)