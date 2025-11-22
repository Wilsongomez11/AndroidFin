package com.example.proyectofinal.ViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectofinal.Api.AdministradorService
import com.example.proyectofinal.LoginRequest

import com.example.proyectofinal.admin.LoginState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState

    fun login(api: AdministradorService, username: String, password: String) {
        viewModelScope.launch {
            try {
                _loginState.value = LoginState.Loading

                val response = api.login(LoginRequest(username, password))

                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        _loginState.value = LoginState.Success(body)
                    } else {
                        _loginState.value = LoginState.Error("Respuesta vacía del servidor")
                    }
                } else {
                    _loginState.value = LoginState.Error("Credenciales incorrectas")
                }

            } catch (e: Exception) {
                _loginState.value = LoginState.Error("Error: ${e.localizedMessage}")
            }
        }
    }
}
