package com.example.proyectofinal.Screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.proyectofinal.Model.Pedido
import com.example.proyectofinal.ViewModel.PedidoViewModel

@Composable
fun PedidosScreen(
    navController: NavHostController,
    pedidoViewModel: PedidoViewModel = viewModel()
) {
    val pedidos by pedidoViewModel.pedidos.collectAsState()
    val mensaje by pedidoViewModel.mensaje.collectAsState()

    // Cargar pedidos al abrir pantalla
    LaunchedEffect(Unit) { pedidoViewModel.obtenerPedidos() }

    // Detectar si hay refresh (cuando se crea un pedido nuevo)
    val refreshFlow = navController.currentBackStackEntry
        ?.savedStateHandle
        ?.getStateFlow("refreshPedidos", false)
    val refresh by refreshFlow?.collectAsState() ?: remember { mutableStateOf(false) }

    LaunchedEffect(refresh) {
        if (refresh) {
            pedidoViewModel.obtenerPedidos()
            navController.currentBackStackEntry
                ?.savedStateHandle
                ?.set("refreshPedidos", false)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF0B093B), Color(0xFF3A0CA3), Color(0xFF7209B7))
                )
            )
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "📋 Gestión de Pedidos",
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            // 🔹 Botón superior para crear pedido
            Button(
                onClick = { navController.navigate("crearPedido") },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5E17EB)),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp)
                    .padding(bottom = 16.dp)
            ) {
                Text("➕ Crear Nuevo Pedido", color = Color.White, fontSize = 17.sp)
            }

            Divider(color = Color.White.copy(alpha = 0.3f))
            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Pedidos Registrados",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (pedidos.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No hay pedidos registrados todavía",
                        color = Color.LightGray,
                        fontSize = 16.sp
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(pedidos) { pedido ->
                        PedidoCard(
                            pedido = pedido,
                            onEliminar = {
                                pedidoViewModel.eliminarPedido(pedido.id ?: 0L)
                            },
                            onVerDetalle = {
                                navController.navigate("detallePedido/${pedido.id}")
                            }
                        )
                    }
                }
            }

            if (mensaje.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = mensaje,
                    color = Color.Yellow,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}

@Composable
fun PedidoCard(
    pedido: Pedido,
    onEliminar: () -> Unit,
    onVerDetalle: () -> Unit
) {
    var mostrarDialogo by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onVerDetalle() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF3A0CA3))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text("🆔 Pedido N° ${pedido.id}", color = Color.White, fontWeight = FontWeight.Bold)
            Text("Estado: ${pedido.estado}", color = Color.LightGray)
            Text("Total: $${pedido.total}", color = Color(0xFFFFC107))
            Text("Fecha: ${pedido.fecha ?: "Sin fecha"}", color = Color.White.copy(0.8f))

            // 🔹 Mostrar mesa y cliente si existen
            pedido.mesa?.let {
                Text("Mesa: $it", color = Color.White.copy(0.9f))
            }
            pedido.cliente?.let {
                Text(
                    "Cliente: ${it.nombre.ifBlank { "ID ${it.id}" }}",
                    color = Color.White.copy(0.9f)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text("Detalles:", color = Color.White, fontWeight = FontWeight.Bold)
            if (pedido.detalles.isNotEmpty()) {
                pedido.detalles.forEach {
                    Text(
                        "- ${it.producto.nombre} x${it.cantidad} = $${it.cantidad * it.precioUnitario}",
                        color = Color.White.copy(0.9f),
                        fontSize = 14.sp
                    )
                }
            } else {
                Text("- Sin detalles registrados", color = Color.LightGray, fontSize = 14.sp)
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                OutlinedButton(
                    onClick = onVerDetalle,
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
                ) {
                    Text("Ver Detalle")
                }

                Button(
                    onClick = { mostrarDialogo = true },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F))
                ) {
                    Text("Eliminar", color = Color.White)
                }
            }
        }
    }

    //  Diálogo de confirmación
    if (mostrarDialogo) {
        AlertDialog(
            onDismissRequest = { mostrarDialogo = false },
            confirmButton = {
                Button(
                    onClick = {
                        mostrarDialogo = false
                        onEliminar()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F))
                ) {
                    Text("Sí, eliminar", color = Color.White)
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { mostrarDialogo = false }) {
                    Text("Cancelar", color = Color.White)
                }
            },
            title = { Text("Confirmar eliminación", color = Color.White) },
            text = { Text("¿Seguro que deseas eliminar este pedido?", color = Color.LightGray) },
            containerColor = Color(0xFF3A0CA3),
            shape = RoundedCornerShape(20.dp)
        )
    }
}
