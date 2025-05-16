package com.example.proyectofinal.admin

import androidx.lifecycle.ViewModel
import com.example.proyectofinal.Model.Administrador

class AuthViewModel : ViewModel() {
    private val admin = Administrador(
        username = "admin",
        password = "1234",
        nombre = "Administrador",
        cargo = "Administrador General"
    )

    fun validateCredentials(user: String, pass: String): Boolean {
        return user == admin.username && pass == admin.password
    }

    fun getAdministrador(): Administrador = admin
}
