package com.example.proyectofinal.ViewModel
import android.R.attr.id
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectofinal.Api.ApiClient
import com.example.proyectofinal.Model.Administrador
import com.example.proyectofinal.Model.Mesero
import com.example.proyectofinal.Model.Pizzero
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

class AgregarPersonalViewModel : ViewModel() {

    private val baseUrl = "http://10.0.2.2:8080"
    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                prettyPrint = true
                isLenient = true
            })
        }
    }

    private val jsonFormat = Json { ignoreUnknownKeys = true }

    private fun postToBackend(endpoint: String, body: String, onResult: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val response = client.post("$baseUrl/$endpoint") {
                    contentType(ContentType.Application.Json)
                    setBody(body)
                }

                if (response.status.isSuccess()) {
                    onResult("Guardado correctamente en $endpoint")
                } else {
                    onResult("Error: ${response.status}")
                }

            } catch (e: Exception) {
                onResult("Error de red: ${e.message}")
            }
        }
    }

    fun guardarMesero(nombre: String, telefono: String, email: String, onResult: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val response = ApiClient.apiService.insertarMesero(
                    Mesero(nombre = nombre, telefono = telefono, correo = email)
                )
                onResult("Mesero guardado con éxito")
            } catch (e: Exception) {
                onResult("Error al guardar: ${e.localizedMessage}")
            }
        }
    }


    fun guardarPizzero(
        nombre: String,
        telefono: String,
        direccion: String,
        onResult: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val response = ApiClient.apiService.insertarPizzero(
                    Pizzero(id = id, nombre = nombre, telefono = telefono, direccion = direccion)
                )
                if (response.isSuccessful) {
                    onResult("Pizzero guardado con éxito")
                } else {
                    onResult("Error: ${response.code()} - ${response.message()}")
                }
            } catch (e: Exception) {
                onResult("Excepción: ${e.localizedMessage}")
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
                    onResult("Administrador guardado con éxito")
                } else {
                    onResult("Error: ${response.code()} - ${response.message()}")
                }
            } catch (e: Exception) {
                onResult("Excepción: ${e.localizedMessage}")
            }
        }
    }
}





