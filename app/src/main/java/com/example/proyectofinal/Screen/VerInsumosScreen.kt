package com.example.proyectofinal.Screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.proyectofinal.Model.Insumo
import com.example.proyectofinal.ViewModel.InsumoViewModel

@Composable
fun VerInsumosScreen(
    navController: NavHostController,
    returnTo: String,
    viewModel: InsumoViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val insumos by viewModel.insumos.collectAsState()
    var mensaje by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.obtenerInsumos()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF0B093B), Color(0xFF3A0CA3), Color(0xFF7209B7))
                )
            )
            .padding(20.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "📋 Lista de Insumos",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(top = 8.dp)
            ) {
                items(
                    items = insumos,
                    key = { it.id }
                ) { insumo ->
                    InsumoCardEditable(
                        insumo = insumo,
                        onGuardar = { actualizado ->

                            viewModel.actualizarInsumo(actualizado.id, actualizado) { resultado ->
                                mensaje = resultado

                                viewModel.obtenerInsumos()
                            }
                        },
                        onEliminar = { id ->
                            viewModel.eliminarInsumo(id) { resultado ->
                                mensaje = resultado
                                viewModel.obtenerInsumos()
                            }
                        }
                    )
                }
            }


            if (mensaje.isNotEmpty()) {
                Text(text = mensaje, color = Color.White, fontSize = 14.sp)
            }

            Button(
                onClick = {
                    if (!navController.navigateUp()) {
                        navController.navigate(returnTo) { launchSingleTop = true }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E1E1E)),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.height(60.dp).width(140.dp)
            ) {
                Text("↩️ Volver", color = Color.White, fontSize = 16.sp)
            }
        }
    }
}

@Composable
fun InsumoCardEditable(
    insumo: Insumo,
    onGuardar: (Insumo) -> Unit,
    onEliminar: (Long) -> Unit
) {
    var nombre by remember { mutableStateOf(insumo.nombre) }
    var unidad by remember { mutableStateOf(insumo.unidadMedida) }
    var cantidadActual by remember { mutableStateOf(insumo.cantidadActual.toString()) }
    var cantidadMinima by remember { mutableStateOf(insumo.cantidadMinima.toString()) }

    var modoEditar by remember { mutableStateOf(false) }
    var mostrarConfirmacion by remember { mutableStateOf(false) }

    val campoColor = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = Color(0xFF9C27B0),
        unfocusedBorderColor = Color.Gray,
        focusedTextColor = Color.White,
        unfocusedTextColor = Color.White
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .shadow(6.dp, RoundedCornerShape(16.dp))
            .background(Color(0xFF1E1E1E), RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                "📦 ${insumo.nombre}",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            if (modoEditar) {
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre") },
                    colors = campoColor,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = unidad,
                    onValueChange = { unidad = it },
                    label = { Text("Unidad de medida") },
                    colors = campoColor,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = cantidadActual,
                    onValueChange = { cantidadActual = it },
                    label = { Text("Cantidad actual") },
                    colors = campoColor,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = cantidadMinima,
                    onValueChange = { cantidadMinima = it },
                    label = { Text("Cantidad mínima") },
                    colors = campoColor,
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = {
                            val actualizado = insumo.copy(
                                nombre = nombre,
                                unidadMedida = unidad,
                                cantidadActual = cantidadActual.toDoubleOrNull() ?: insumo.cantidadActual,
                                cantidadMinima = cantidadMinima.toDoubleOrNull() ?: insumo.cantidadMinima
                            )
                            onGuardar(actualizado)
                            modoEditar = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7E57C2)),
                        modifier = Modifier.weight(1f)
                    ) { Text("Guardar", color = Color.White) }

                    OutlinedButton(
                        onClick = { modoEditar = false },
                        border = ButtonDefaults.outlinedButtonBorder.copy(width = 1.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                        modifier = Modifier.weight(1f)
                    ) { Text("Cancelar") }
                }
            } else {
                Text("Unidad: ${insumo.unidadMedida}", color = Color.LightGray, fontSize = 14.sp)
                Text("Cantidad actual: ${insumo.cantidadActual}", color = Color.LightGray, fontSize = 14.sp)
                Text("Cantidad mínima: ${insumo.cantidadMinima}", color = Color.LightGray, fontSize = 14.sp)

                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = { modoEditar = true },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9C27B0)),
                        modifier = Modifier.weight(1f)
                    ) { Text("Editar", color = Color.White) }

                    OutlinedButton(
                        onClick = { mostrarConfirmacion = true },
                        border = ButtonDefaults.outlinedButtonBorder.copy(width = 1.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red),
                        modifier = Modifier.weight(1f)
                    ) { Text("Eliminar") }
                }
            }
        }
    }

    // Diálogo de confirmación
    if (mostrarConfirmacion) {
        AlertDialog(
            onDismissRequest = { mostrarConfirmacion = false },
            title = {
                Text("Confirmar eliminación", color = Color.White)
            },
            text = {
                Text(
                    "¿Seguro que deseas eliminar el insumo \"${insumo.nombre}\"?",
                    color = Color.White.copy(alpha = 0.9f)
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onEliminar(insumo.id)
                        mostrarConfirmacion = false
                    }
                ) { Text("Sí, eliminar", color = Color.Red) }
            },
            dismissButton = {
                TextButton(onClick = { mostrarConfirmacion = false }) {
                    Text("Cancelar", color = Color.White)
                }
            },
            containerColor = Color(0xFF2C2C2C),
            shape = RoundedCornerShape(16.dp)
        )
    }
}

