package com.example.proyectofinal.admin

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectofinal.Api.AdministradorService
import com.example.proyectofinal.LoginRequest
import com.example.proyectofinal.Model.Administrador
import kotlinx.coroutines.launch

class AuthViewModel(
    private val adminService: AdministradorService
) : ViewModel() {

    var loginState by mutableStateOf<LoginState>(LoginState.Idle)
        private set

    fun login(username: String, password: String) {
        viewModelScope.launch {
            loginState = LoginState.Loading
            try {
                val request = LoginRequest(username = username, password = password)
                val response = adminService.login(request)

                if (response.isSuccessful) {
                    val admin = response.body()
                    if (admin != null) {
                        loginState = LoginState.Success(admin)
                    } else {
                        loginState = LoginState.Error("Respuesta vacía del servidor.")
                    }
                } else {
                    val errorMsg = response.errorBody()?.string() ?: "Error desconocido"
                    loginState = LoginState.Error("Error ${response.code()}: $errorMsg")
                }
            } catch (e: Exception) {
                loginState = LoginState.Error("Excepción: ${e.localizedMessage}")
            }
        }
    }
}

sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    data class Success(val admin: Administrador) : LoginState()
    data class Error(val message: String) : LoginState()
}

