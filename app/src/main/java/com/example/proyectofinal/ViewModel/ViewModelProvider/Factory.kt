package com.example.proyectofinal.ViewModel.ViewModelProvider

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.proyectofinal.Api.AdministradorService
import com.example.proyectofinal.admin.AuthViewModel

class AuthViewModelFactory(
    private val adminService: AdministradorService
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AuthViewModel(adminService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
