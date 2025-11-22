package com.example.proyectofinal.Model

import androidx.room.Query
import okhttp3.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

data class Administrador(
    val id: Long? = 1,
    val username: String,
    val password: String,
    val nombre: String,
    val cargo: String

)



