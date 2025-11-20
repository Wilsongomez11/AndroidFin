package com.example.proyectofinal.admin

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectofinal.Api.AdministradorService
import com.example.proyectofinal.LoginRequest
import com.example.proyectofinal.LoginResponse
import com.example.proyectofinal.Model.Administrador
import com.example.proyectofinal.ViewModel.AdministradorViewModel
import kotlinx.coroutines.launch

class AuthViewModel(
    private val adminService: AdministradorService,
    private val adminViewModel: AdministradorViewModel? = null
) : ViewModel() {

    var loginState by mutableStateOf<LoginState>(LoginState.Idle)
        private set

    fun login(username: String, password: String) {
        if (username.isBlank() || password.isBlank()) {
            loginState = LoginState.Error("Usuario o contraseña vacíos")
            return
        }

        viewModelScope.launch {
            loginState = LoginState.Loading
            try {
                val adminResponse = adminService.login(LoginRequest(username, password))
                if (adminResponse.isSuccessful && adminResponse.body() != null) {
                    val user = adminResponse.body()!!

                    adminViewModel?.establecerAdministradorActual(
                        Administrador(
                            id = user.id,
                            username = user.username,
                            password = "",
                            nombre = user.nombre,
                            cargo = user.cargo
                        )
                    )

                    loginState = LoginState.Success(user)
                    return@launch
                }
                val meseroResponse = adminService.loginMesero(LoginRequest(username, password))
                if (meseroResponse.isSuccessful && meseroResponse.body() != null) {
                    val mesero = meseroResponse.body()!!
                    val user = LoginResponse(
                        id = mesero.id ?: 0L,
                        nombre = mesero.nombre,
                        cargo = "MESERO",
                        username = mesero.username
                    )
                    loginState = LoginState.Success(user)
                    return@launch
                }

                val pizzeroResponse = adminService.loginPizzero(LoginRequest(username, password))
                if (pizzeroResponse.isSuccessful && pizzeroResponse.body() != null) {
                    val pizzero = pizzeroResponse.body()!!
                    val user = LoginResponse(
                        id = pizzero.id ?: 0L,
                        nombre = pizzero.nombre,
                        cargo = "PIZZERO",
                        username = pizzero.username
                    )
                    loginState = LoginState.Success(user)
                    return@launch
                }

                loginState = LoginState.Error("Credenciales incorrectas o usuario no encontrado.")

            } catch (e: Exception) {
                loginState = LoginState.Error("Error de conexión: ${e.localizedMessage}")
            }
        }
    }
}


sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    data class Success(val user: LoginResponse) : LoginState()
    data class Error(val message: String) : LoginState()
}
