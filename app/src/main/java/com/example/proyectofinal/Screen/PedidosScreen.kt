package com.example.proyectofinal.Screen

import androidx.compose.animation.*
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.proyectofinal.Model.Pedido
import com.example.proyectofinal.ViewModel.PedidoViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@Composable
fun PedidosScreen(
    navController: NavHostController,
    pedidoViewModel: PedidoViewModel = viewModel()
) {
    val pedidos by pedidoViewModel.pedidos.collectAsState()
    val mensaje by pedidoViewModel.mensaje.collectAsState()
    val isRefreshing by pedidoViewModel.isRefreshing.collectAsState()

    var filtroSeleccionado by remember { mutableStateOf("Todos") }

    val pedidosFiltrados = remember(pedidos, filtroSeleccionado) {
        when (filtroSeleccionado) {
            "Pendientes" -> pedidos.filter { it.estado == "Pendiente" }
            "Pagados" -> pedidos.filter { it.estado == "Pagado" }
            "Devueltos" -> pedidos.filter { it.estado == "Devuelto" }
            else -> pedidos
        }.sortedByDescending { it.id }
    }

    val pendientes = remember(pedidos) { pedidos.count { it.estado == "Pendiente" } }
    val pagados = remember(pedidos) { pedidos.count { it.estado == "Pagado" } }
    val devueltos = remember(pedidos) { pedidos.count { it.estado == "Devuelto" } }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF0B093B),
                        Color(0xFF3A0CA3),
                        Color(0xFF7209B7)
                    )
                )
            )
    ) {
        SwipeRefresh(
            state = rememberSwipeRefreshState(isRefreshing),
            onRefresh = { pedidoViewModel.refrescar() },
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp, vertical = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "\uD83D\uDCCB Gesti\u00F3n de Pedidos",
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )

                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IndicadorCardPedido(
                        emoji = "\u231B",
                        label = "Pendientes",
                        value = pendientes.toString(),
                        onClick = { filtroSeleccionado = "Pendientes" },
                        isSelected = filtroSeleccionado == "Pendientes"
                    )
                    IndicadorCardPedido(
                        emoji = "\u2705",
                        label = "Pagados",
                        value = pagados.toString(),
                        onClick = { filtroSeleccionado = "Pagados" },
                        isSelected = filtroSeleccionado == "Pagados"
                    )
                    IndicadorCardPedido(
                        emoji = "\u21A9",
                        label = "Devueltos",
                        value = devueltos.toString(),
                        onClick = { filtroSeleccionado = "Devueltos" },
                        isSelected = filtroSeleccionado == "Devueltos"
                    )
                }

                if (filtroSeleccionado != "Todos") {
                    Button(
                        onClick = { filtroSeleccionado = "Todos" },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF7E57C2)
                        ),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .height(45.dp)
                    ) {
                        Text(
                            text = "\uD83D\uDCCB Ver Todos (${pedidos.size})",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    onClick = { navController.navigate("mesas") },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF5E17EB)
                    ),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .height(60.dp)
                ) {
                    Text(
                        text = "\uD83C\uDF74 Gestionar Mesas",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                if (pedidosFiltrados.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "\uD83D\uDCE6",
                                fontSize = 64.sp
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "No hay pedidos",
                                color = Color.White,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = when (filtroSeleccionado) {
                                    "Pendientes" -> "en estado pendiente"
                                    "Pagados" -> "pagados"
                                    "Devueltos" -> "devueltos"
                                    else -> "registrados"
                                },
                                color = Color.White.copy(alpha = 0.7f),
                                fontSize = 14.sp
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(
                            items = pedidosFiltrados,
                            key = { it.id ?: 0 }
                        ) { pedido ->
                            PedidoCardAdmin(
                                pedido = pedido,
                                onClick = {
                                    navController.navigate("detallePedido/${pedido.id}")
                                },
                                onEliminar = {
                                    pedidoViewModel.eliminarPedido(pedido.id!!)
                                },
                                onDevolver = if (pedido.estado == "Pagado") {
                                    { navController.navigate("devolverPedido/${pedido.id}") }
                                } else null
                            )
                        }
                    }
                }

                if (mensaje.isNotEmpty()) {
                    Snackbar(
                        modifier = Modifier.padding(8.dp),
                        containerColor = Color(0xFF1E1E1E),
                        contentColor = Color.White
                    ) {
                        Text(mensaje)
                    }
                }
            }
        }
    }
}

@Composable
fun IndicadorCardPedido(
    emoji: String,
    label: String,
    value: String,
    onClick: () -> Unit,
    isSelected: Boolean
) {
    Card(
        modifier = Modifier
            .width(110.dp)
            .height(100.dp)
            .shadow(if (isSelected) 12.dp else 6.dp, RoundedCornerShape(20.dp))
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color(0xFF5E17EB) else Color(0xFF1E1E1E)
        ),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = emoji,
                fontSize = 28.sp
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = value,
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = label,
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 11.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun PedidoCardAdmin(
    pedido: Pedido,
    onClick: () -> Unit,
    onEliminar: () -> Unit,
    onDevolver: (() -> Unit)? = null
) {
    var mostrarDialogo by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(8.dp, RoundedCornerShape(20.dp))
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1E1E1E)
        ),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Pedido #${pedido.id}",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                val (estadoColor, estadoEmoji) = remember(pedido.estado) {
                    when (pedido.estado) {
                        "Pendiente" -> Color(0xFFFFA726) to "\u231B"
                        "Pagado" -> Color(0xFF66BB6A) to "\u2705"
                        "Devuelto" -> Color(0xFFEF5350) to "\u21A9"
                        else -> Color.White to "\u2753"
                    }
                }

                Surface(
                    color = estadoColor.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = estadoEmoji, fontSize = 14.sp)
                        Text(
                            text = pedido.estado,
                            color = estadoColor,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Divider(color = Color.White.copy(alpha = 0.1f))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "\uD83D\uDCB0 $${String.format("%.2f", pedido.total)}",
                        color = Color(0xFF66BB6A),
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "\uD83D\uDCC5 ${pedido.fecha ?: "Sin fecha"}",
                        color = Color.LightGray,
                        fontSize = 12.sp
                    )
                }

                pedido.cliente?.let {
                    Text(
                        text = "\uD83D\uDC64 ${it.nombre.ifBlank { "Cliente ${it.id}" }}",
                        color = Color.LightGray,
                        fontSize = 13.sp
                    )
                }
            }

            if (pedido.detalles.isNotEmpty()) {
                Surface(
                    color = Color.White.copy(alpha = 0.05f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "\uD83C\uDF55 ${pedido.detalles.size} producto(s)",
                            color = Color.White,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Tap para ver detalles",
                            color = Color.White.copy(alpha = 0.6f),
                            fontSize = 11.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                when (pedido.estado) {
                    "Pendiente" -> {
                        Button(
                            onClick = onClick,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF7E57C2)
                            ),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.weight(1f).height(42.dp)
                        ) {
                            Text("\uD83D\uDC41 Ver", color = Color.White, fontSize = 15.sp)
                        }

                        Button(
                            onClick = { mostrarDialogo = true },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFEF5350)
                            ),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.weight(1f).height(42.dp)
                        ) {
                            Text("\uD83D\uDDD1 Eliminar", color = Color.White, fontSize = 15.sp)
                        }
                    }
                    "Pagado" -> {
                        Button(
                            onClick = onClick,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF7E57C2)
                            ),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.weight(1f).height(42.dp)
                        ) {
                            Text("\uD83D\uDC41 Ver", color = Color.White, fontSize = 15.sp)
                        }

                        if (onDevolver != null) {
                            Button(
                                onClick = onDevolver,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF9C27B0)
                                ),
                                shape = RoundedCornerShape(16.dp),
                                modifier = Modifier.weight(1f).height(42.dp)
                            ) {
                                Text("\u21A9 Devolver", color = Color.White, fontSize = 15.sp)
                            }
                        }
                    }
                    else -> {
                        Button(
                            onClick = onClick,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF7E57C2)
                            ),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.fillMaxWidth().height(42.dp)
                        ) {
                            Text("\uD83D\uDC41 Ver Detalle", color = Color.White, fontSize = 15.sp)
                        }
                    }
                }
            }
        }
    }

    if (mostrarDialogo) {
        AlertDialog(
            onDismissRequest = { mostrarDialogo = false },
            confirmButton = {
                Button(
                    onClick = {
                        mostrarDialogo = false
                        onEliminar()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFEF5350)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Eliminar", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDialogo = false }) {
                    Text("Cancelar", color = Color.White)
                }
            },
            title = {
                Text(
                    "\u26A0 Confirmar",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    "\u00BFEliminar pedido #${pedido.id}?",
                    color = Color.White.copy(alpha = 0.8f)
                )
            },
            containerColor = Color(0xFF1E1E1E),
            shape = RoundedCornerShape(20.dp)
        )
    }
}