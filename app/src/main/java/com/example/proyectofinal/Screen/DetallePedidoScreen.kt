package com.example.proyectofinal.Screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.proyectofinal.Model.DetallePedido
import com.example.proyectofinal.Model.Pedido
import com.example.proyectofinal.ViewModel.PedidoViewModel
import kotlinx.coroutines.launch

@Composable
fun DetallePedidoScreen(
    pedido: Pedido,
    navController: NavHostController,
    pedidoViewModel: PedidoViewModel
) {
    var detallesEditados by remember { mutableStateOf(pedido.detalles.map { it.copy() }) }
    var mostrarConfirmacion by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val total = remember(detallesEditados) {
        detallesEditados.sumOf { it.cantidad * it.precioUnitario }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFF0B093B), Color(0xFF3A0CA3), Color(0xFF7209B7))
                    )
                )
                .padding(padding)
                .padding(16.dp)
        ) {
            Column {
                Text(
                    text = "📦 Detalle del Pedido N° ${pedido.id}",
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(8.dp))
                Text("Estado: ${pedido.estado}", color = Color.LightGray)
                Text("Total: $${String.format("%.2f", total)}", color = Color(0xFFFFC107))
                Text("Fecha: ${pedido.fecha ?: "Sin fecha"}", color = Color.White.copy(0.8f))
                Spacer(Modifier.height(12.dp))
                Divider(color = Color.White.copy(alpha = 0.3f))
                Spacer(Modifier.height(12.dp))

                if (detallesEditados.isNotEmpty()) {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        items(detallesEditados) { detalle ->
                            EditableDetalleCard(detalle) { nuevo ->
                                detallesEditados = detallesEditados.map {
                                    if (it.producto.id == nuevo.producto.id) nuevo else it
                                }
                            }
                        }
                    }
                } else {
                    Text("No hay productos en este pedido", color = Color.LightGray)
                }

                Spacer(Modifier.height(20.dp))

                // Botón Guardar cambios
                Button(
                    onClick = { mostrarConfirmacion = true },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5E17EB)),
                    shape = RoundedCornerShape(18.dp),
                    modifier = Modifier.fillMaxWidth().height(55.dp)
                ) {
                    Text("Guardar Cambios", color = Color.White, fontSize = 16.sp)
                }

                Spacer(Modifier.height(10.dp))

                // Botón Volver
                Button(
                    onClick = {
                        val popped = navController.popBackStack("pedidos", inclusive = false)
                        if (!popped) {
                            navController.navigate("pedidos") {
                                launchSingleTop = true
                                popUpTo("pedidos") { inclusive = false }
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E1E1E)),
                    shape = RoundedCornerShape(18.dp),
                    modifier = Modifier.fillMaxWidth().height(50.dp)
                ) {
                    Text("Volver", color = Color.White)
                }
            }
                //  Diálogo de confirmación antes de guardar
            if (mostrarConfirmacion) {
                AlertDialog(
                    onDismissRequest = { mostrarConfirmacion = false },
                    confirmButton = {
                        Button(
                            onClick = {
                                val actualizado = pedido.copy(
                                    detalles = detallesEditados,
                                    total = total
                                )
                                scope.launch {
                                    pedidoViewModel.actualizarPedido(actualizado) { msg ->
                                        scope.launch {
                                            snackbarHostState.showSnackbar(msg)
                                        }
                                    }
                                    mostrarConfirmacion = false
                                    navController.popBackStack("pedidos", inclusive = false)
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3A0CA3))
                        ) {
                            Text("Confirmar", color = Color.White)
                        }
                    },
                    dismissButton = {
                        OutlinedButton(onClick = { mostrarConfirmacion = false }) {
                            Text("Cancelar", color = Color.White)
                        }
                    },
                    title = { Text("Confirmar cambios", color = Color.White) },
                    text = { Text("¿Deseas guardar los cambios en este pedido?", color = Color.LightGray) },
                    containerColor = Color(0xFF4A148C),
                    shape = RoundedCornerShape(20.dp)
                )
            }
        }
    }
}

@Composable
fun EditableDetalleCard(detalle: DetallePedido, onCantidadCambiada: (DetallePedido) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(15.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF4A148C))
    ) {
        Column(Modifier.padding(12.dp)) {
            Text(detalle.producto.nombre, color = Color.White, fontWeight = FontWeight.Bold)
            Text("Precio unitario: $${detalle.precioUnitario}", color = Color.LightGray)
            Spacer(Modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = {
                            if (detalle.cantidad > 1)
                                onCantidadCambiada(detalle.copy(cantidad = detalle.cantidad - 1))
                        }
                    ) {
                        Icon(Icons.Default.Remove, contentDescription = "Disminuir", tint = Color.White)
                    }
                    Text(
                        text = "${detalle.cantidad}",
                        color = Color.White,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                    IconButton(
                        onClick = {
                            onCantidadCambiada(detalle.copy(cantidad = detalle.cantidad + 1))
                        }
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Aumentar", tint = Color.White)
                    }
                }

                Text(
                    "Subtotal: $${detalle.cantidad * detalle.precioUnitario}",
                    color = Color(0xFFFFEB3B),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

