package com.example.proyectofinal.Screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.proyectofinal.ViewModel.InsumoViewModel

@Composable
fun InsumosScreen(
    navController: NavHostController,
    returnTo: String,
    viewModel: InsumoViewModel = viewModel()
) {
    var nombre by remember { mutableStateOf("") }
    var unidad by remember { mutableStateOf("") }
    var cantidadActual by remember { mutableStateOf("") }
    var cantidadMinima by remember { mutableStateOf("") }
    var mensaje by remember { mutableStateOf("") }

    val campoColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = Color.White,
        unfocusedBorderColor = Color.LightGray,
        focusedTextColor = Color.White,
        unfocusedTextColor = Color.White,
        cursorColor = Color.White
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF0B093B), Color(0xFF3A0CA3), Color(0xFF7209B7))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text(
                text = "📦 Gestión de Insumos",
                fontSize = 26.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )

            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre", color = Color.LightGray) },
                colors = campoColors,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = unidad,
                onValueChange = { unidad = it },
                label = { Text("Unidad de medida", color = Color.LightGray) },
                colors = campoColors,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = cantidadActual,
                onValueChange = { cantidadActual = it },
                label = { Text("Cantidad actual", color = Color.LightGray) },
                colors = campoColors,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = cantidadMinima,
                onValueChange = { cantidadMinima = it },
                label = { Text("Cantidad mínima", color = Color.LightGray) },
                colors = campoColors,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(30.dp))

            // Fila con los botones Volver y Agregar
            Row(
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = {
                        if (!navController.navigateUp()) {
                            navController.navigate(returnTo) { launchSingleTop = true }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E1E1E)),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier
                        .height(60.dp)
                        .width(140.dp)
                ) {
                    Text("↩️ Volver", color = Color.White, fontSize = 16.sp)
                }

                Button(
                    onClick = {
                        if (nombre.isBlank() || unidad.isBlank() || cantidadActual.isBlank() || cantidadMinima.isBlank()) {
                            mensaje = "⚠️ Completa todos los campos"
                            return@Button
                        }

                        viewModel.agregarInsumo(
                            nombre,
                            unidad,
                            cantidadActual.toInt(),
                            cantidadMinima.toInt()
                        ) {
                            mensaje = it
                            if (it.contains("correctamente")) {
                                nombre = ""
                                unidad = ""
                                cantidadActual = ""
                                cantidadMinima = ""
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E1E1E)),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier
                        .height(60.dp)
                        .width(140.dp)
                ) {
                    Text("➕ Agregar", color = Color.White, fontSize = 16.sp)
                }
            }

            if (mensaje.isNotEmpty()) {
                Text(mensaje, color = Color.White, fontSize = 14.sp)
            }
        }
    }
}
