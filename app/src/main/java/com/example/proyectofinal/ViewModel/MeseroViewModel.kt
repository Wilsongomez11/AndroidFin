package com.example.proyectofinal.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectofinal.Api.ApiClient
import com.example.proyectofinal.Model.Mesero
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class MeseroViewModel : ViewModel() {

    fun agregarMesero(
        nombre: String,
        correo: String,
        telefono: String,
        onResult: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val nuevoMesero = Mesero(nombre, correo, telefono)
                val response = ApiClient.apiService.agregarMesero(nuevoMesero)

                if (response.isSuccessful) {
                    onResult("Mesero registrado correctamente")
                } else {
                    onResult("Error al registrar mesero: ${response.code()} - ${response.message()}")
                }
            } catch (e: IOException) {
                onResult("Error de red: ${e.localizedMessage}")
            } catch (e: HttpException) {
                onResult("Error HTTP: ${e.localizedMessage}")
            } catch (e: Exception) {
                onResult("Error inesperado: ${e.localizedMessage}")
            }
        }
    }
}
