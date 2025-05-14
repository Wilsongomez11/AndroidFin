package com.example.proyectofinal.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.proyectofinal.AdministradorViewModel
import com.example.proyectofinal.Model.Administrador

@Composable
fun AdministradoresScreen(
    navController: NavHostController,
    viewModel: AdministradorViewModel = viewModel()
) {
    val administradores by viewModel.administradores.collectAsState()
    val error by viewModel.error.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Lista de Administradores",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (error != null) {
            Text(
                text = "Error al cargar administradores: $error",
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(8.dp)
            )
        }

        if (administradores.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Cargando administradores...")
            }
        } else {
            LazyColumn {
                items(administradores) { admin ->
                    AdministradorItem(administrador = admin)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            navController.popBackStack("adminHome", inclusive = false)
        }) {
            Text("Volver")
        }
    }
}
    @Composable
    fun AdministradorItem(administrador: Administrador) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp), // Separación entre tarjetas
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp) // Sombra de la tarjeta
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Cargo: ${administrador.cargo}")
                Text("Nombre: ${administrador.nombre}")
            }
        }
    }

