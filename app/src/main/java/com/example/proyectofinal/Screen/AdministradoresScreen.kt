package com.example.proyectofinal.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.proyectofinal.ViewModel.AdministradorViewModel
import com.example.proyectofinal.Model.Administrador
import com.example.proyectofinal.ViewModel.AppBackground

@Composable
fun AdministradoresScreen(
    navController: NavHostController,
    viewModel: AdministradorViewModel = viewModel()
) {
    val administradores by viewModel.administradores.collectAsState()
    val error by viewModel.error.collectAsState()

    val adminFijo = Administrador(
        username = "Wil",
        password = "1234",
        nombre = "Wilson",
        cargo = "Administrador General"
    )

    val listaCompleta = remember(administradores) {
        listOf(adminFijo) + administradores.filter { it.username != "admin" }
    }

    AppBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Lista de Personal",
                style = MaterialTheme.typography.headlineSmall,
                color = Color.White,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            if (error != null) {
                Text(
                    text = "Error al cargar administradores: $error",
                    color = Color.Red,
                    modifier = Modifier.padding(8.dp)
                )
            }

            if (administradores.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Cargando administradores...", color = Color.White)
                }
            } else {
                LazyColumn {
                    items(listaCompleta) { admin ->
                        AdministradorItemStyled(admin, navController, viewModel)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = { navController.navigate("registroPersonal") },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                ) {
                    Text("Agregar Personal", color = Color.Black)
                }

                Button(
                    onClick = { navController.popBackStack("adminHome", inclusive = false) },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                ) {
                    Text("Volver", color = Color.Black)
                }
            }
        }
    }
}


@Composable
fun AdministradorItemStyled(
    administrador: Administrador,
    navController: NavHostController,
    viewModel: AdministradorViewModel
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Cargo: ${administrador.cargo}", color = Color.White)
            Text("Nombre: ${administrador.nombre}", color = Color.White)

            if (administrador.id?.toInt() != (-1L).toInt()) {
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                ) {
                    Button(
                        onClick = { /* TODO: navController.navigate("editarPersonal/${administrador.id}") */ },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Text("Editar", color = Color.White)
                    }
                    Button(
                        onClick = {
                            viewModel.eliminarAdministrador(administrador.id)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                    ) {
                        Text("Eliminar", color = Color.White)
                    }
                }
            }
        }
    }
}
