package com.example.proyectofinal.Screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.navigation.NavHostController
import com.example.proyectofinal.Model.PersonalItem
import com.example.proyectofinal.ViewModel.AdministradorViewModel

@Composable
fun PersonalItemCard(
    persona: PersonalItem,
    navController: NavHostController,
    viewModel: AdministradorViewModel
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF5E17EB)),
        elevation = CardDefaults.cardElevation(6.dp),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("\uD83D\uDC64 ${persona.nombre}", color = Color.White, style = MaterialTheme.typography.titleMedium)
            Text("\uD83D\uDCBC Cargo: ${persona.cargo}", color = Color(0xFF5E17EB))

            Spacer(Modifier.height(10.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = {
                        when (persona.cargo) {
                            "Administrador" -> navController.navigate("editarAdministrador/${persona.id}")
                            "Mesero" -> navController.navigate("editarMesero/${persona.id}")
                            "Pizzero" -> navController.navigate("editarPizzero/${persona.id}")
                        }
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                ) {
                    Text("Editar", color = Color.Black)
                }

                Button(
                    onClick = {
                        when (persona.cargo) {
                            "Administrador" -> viewModel.eliminarAdministrador(persona.id)
                            "Mesero" -> viewModel.eliminarMesero(persona.id)
                            "Pizzero" -> viewModel.eliminarPizzero(persona.id)
                        }
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5E17EB))
                ) {
                    Text("Eliminar", color = Color.White)
                }
            }
        }
    }
}


