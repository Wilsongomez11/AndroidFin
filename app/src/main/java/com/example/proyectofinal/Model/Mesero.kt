package com.example.proyectofinal.Model

import kotlinx.serialization.Serializable

@Serializable
data class Mesero(
    val nombre: String,
    val correo: String,
    val telefono: String
)
