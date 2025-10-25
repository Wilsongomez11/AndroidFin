package com.example.proyectofinal.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectofinal.Api.ApiClient
import com.example.proyectofinal.Model.Administrador
import com.example.proyectofinal.Model.Mesero
import com.example.proyectofinal.Model.Pizzero
import kotlinx.coroutines.launch

class AgregarPersonalViewModel : ViewModel() {

    fun guardarMesero(
        nombre: String,
        telefono: String,
        correo: String,
        username: String,
        password: String,
        onResult: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val response = ApiClient.apiService.insertarMesero(
                    Mesero(
                        id = null,
                        nombre = nombre,
                        telefono = telefono,
                        correo = correo,
                        username = username,
                        password = password
                    )
                )

                if (response.isSuccessful) {
                    onResult(" Mesero guardado con éxito")
                } else {
                    onResult(" Error al guardar mesero: ${response.code()} - ${response.message()}")
                }
            } catch (e: Exception) {
                onResult(" Excepción al guardar mesero: ${e.localizedMessage}")
            }
        }
    }

    fun guardarPizzero(
        nombre: String,
        telefono: String,
        direccion: String,
        username: String,
        password: String,
        onResult: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val response = ApiClient.apiService.insertarPizzero(
                    Pizzero(
                        id = null,
                        nombre = nombre,
                        telefono = telefono,
                        direccion = direccion,
                        username = username,
                        password = password
                    )
                )

                if (response.isSuccessful) {
                    onResult("Pizzero guardado con éxito")
                } else {
                    onResult("Error al guardar pizzero: ${response.code()} - ${response.message()}")
                }
            } catch (e: Exception) {
                onResult("Excepción al guardar pizzero: ${e.localizedMessage}")
            }
        }
    }

    fun guardarAdministrador(
        nombre: String,
        username: String,
        password: String,
        cargo: String,
        onResult: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val response = ApiClient.apiService.insertarAdministrador(
                    Administrador(
                        id = null,
                        nombre = nombre,
                        username = username,
                        password = password,
                        cargo = cargo
                    )
                )

                if (response.isSuccessful) {
                    onResult(" Administrador guardado con éxito")
                } else {
                    onResult(" Error al guardar administrador: ${response.code()} - ${response.message()}")
                }
            } catch (e: Exception) {
                onResult(" Excepción al guardar administrador: ${e.localizedMessage}")
            }
        }
    }
}





