package com.example.proyectofinal

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.proyectofinal.admin.AdministradoresScreen


@Composable
fun MainScreen() {
    val viewModel: AdministradorViewModel = viewModel()
    AdministradoresScreen(viewModel = viewModel)
}
