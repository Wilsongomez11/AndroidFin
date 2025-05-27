package com.example.proyectofinal

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.proyectofinal.Model.Administrador
import com.example.proyectofinal.ViewModel.AdministradorViewModel

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
        colors = CardDefaults.cardColors(containerColor = Color.DarkGray),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Cargo: ${administrador.cargo}", color = Color.White)
            Text("Nombre: ${administrador.nombre}", color = Color.White)

            if (administrador.username != "admin") {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Button(
                        onClick = {
                            navController.navigate("editarAdministrador/${administrador.id}")
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                    ) {
                        Text("Editar", color = Color.Black)
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
            } else {
                Text("Administrador por defecto", color = Color.LightGray, modifier = Modifier.padding(top = 8.dp))
            }
        }
    }
}
