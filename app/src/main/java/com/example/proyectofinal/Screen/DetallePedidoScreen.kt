package com.example.proyectofinal.Screen

import com.example.proyectofinal.Model.DetallePedido
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Remove
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
import com.example.proyectofinal.Model.Pedido
import com.example.proyectofinal.ViewModel.PedidoViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
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

    val (estadoColor, estadoEmoji) = remember(pedido.estado) {
        when (pedido.estado) {
            "Pendiente" -> Color(0xFFFFA726) to "\u231B"
            "Pagado" -> Color(0xFF66BB6A) to "\u2705"
            "Devuelto" -> Color(0xFFEF5350) to "\u21A9"
            else -> Color.White to "\u2753"
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Detalle del Pedido #${pedido.id}",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = Color.Transparent
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color(0xFF0B093B),
                            Color(0xFF3A0CA3),
                            Color(0xFF7209B7)
                        )
                    )
                )
                .padding(padding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp, vertical = 10.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(6.dp, RoundedCornerShape(20.dp)),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF1E1E1E)
                    ),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "Pedido #${pedido.id}",
                                color = Color.White,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Surface(
                                color = estadoColor.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(
                                        horizontal = 12.dp,
                                        vertical = 6.dp
                                    ),
                                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(text = estadoEmoji, fontSize = 14.sp)
                                    Text(
                                        text = pedido.estado,
                                        color = estadoColor,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }

                        Divider(color = Color.White.copy(alpha = 0.1f))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    "\uD83D\uDCC5 Fecha",
                                    color = Color.LightGray,
                                    fontSize = 12.sp
                                )
                                Text(
                                    pedido.fecha ?: "Sin fecha",
                                    color = Color.White,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }

                            pedido.mesa?.let {
                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        "\uD83E\uDE91 Mesa",
                                        color = Color.LightGray,
                                        fontSize = 12.sp
                                    )
                                    Text(
                                        "$it",
                                        color = Color(0xFF9C27B0),
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }

                        pedido.cliente?.let {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    "\uD83D\uDC64 Cliente",
                                    color = Color.LightGray,
                                    fontSize = 12.sp
                                )
                                Text(
                                    it.nombre.ifBlank { "Cliente ${it.id}" },
                                    color = Color.White,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(6.dp, RoundedCornerShape(20.dp)),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF1E1E1E)
                    ),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "\uD83D\uDCB0 Total del Pedido",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "$${String.format("%.2f", total)}",
                            color = Color(0xFF66BB6A),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(
                            onClick = {
                                navController.navigate("facturar/${pedido.id}")
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF5E17EB)
                            ),
                            shape = RoundedCornerShape(20.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp)
                                .padding(end = 8.dp)
                        ) {
                            Text(
                                "\uD83D\uDCC4 Ver Factura",
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Text(
                        "\uD83C\uDF55 Productos del Pedido",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    if (detallesEditados.isNotEmpty()) {
                        LazyColumn(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            items(detallesEditados) { detalle ->
                                EditableDetalleCardAdmin(detalle) { nuevo ->
                                    detallesEditados = detallesEditados.map {
                                        if (it.producto.id == nuevo.producto.id) nuevo else it
                                    }
                                }
                            }
                        }
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "No hay productos en este pedido",
                                color = Color.LightGray,
                                fontSize = 16.sp
                            )
                        }
                    }

                    Button(
                        onClick = { mostrarConfirmacion = true },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF5E17EB)
                        ),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp)
                    ) {
                        Text(
                            "\u2714 Guardar Cambios",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                if (mostrarConfirmacion) {
                    AlertDialog(
                        onDismissRequest = { mostrarConfirmacion = false },
                        confirmButton = {
                            Button(
                                onClick = {
                                    scope.launch {
                                        val actualizado = pedido.copy(
                                            detalles = detallesEditados,
                                            total = total
                                        )

                                        pedidoViewModel.actualizarPedido(actualizado) { msg ->
                                            scope.launch {
                                                snackbarHostState.showSnackbar(msg)
                                            }
                                        }

                                        mostrarConfirmacion = false
                                        pedidoViewModel.obtenerPedidos(forceRefresh = true)
                                        navController.popBackStack()
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF5E17EB)
                                ),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("Confirmar", color = Color.White, fontWeight = FontWeight.Bold)
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = { mostrarConfirmacion = false }) {
                                Text("Cancelar", color = Color.White)
                            }
                        },
                        icon = {
                            Surface(
                                color = Color(0xFF5E17EB).copy(alpha = 0.2f),
                                shape = androidx.compose.foundation.shape.CircleShape,
                                modifier = Modifier.size(56.dp)
                            ) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    Text(text = "\u2714", fontSize = 28.sp)
                                }
                            }
                        },
                        title = {
                            Text(
                                "Confirmar cambios",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            )
                        },
                        text = {
                            Text(
                                "\u00BFDeseas guardar los cambios en este pedido?",
                                color = Color.White.copy(alpha = 0.8f),
                                fontSize = 16.sp
                            )
                        },
                        containerColor = Color(0xFF1E1E1E),
                        shape = RoundedCornerShape(20.dp)
                    )
                }
            }
        }
    }

    @Composable
    fun EditableDetalleCardAdmin(
        detalle: DetallePedido,
        onCantidadCambiada: (DetallePedido) -> Unit
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(4.dp, RoundedCornerShape(16.dp)),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF1E1E1E)
            )
        ) {
            Column(
                modifier = Modifier.padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    detalle.producto.nombre,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Precio: $${String.format("%.2f", detalle.precioUnitario)}",
                        color = Color.LightGray,
                        fontSize = 13.sp
                    )

                    Surface(
                        color = Color(0xFF5E17EB).copy(alpha = 0.2f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(
                                onClick = {
                                    if (detalle.cantidad > 1) {
                                        onCantidadCambiada(detalle.copy(cantidad = detalle.cantidad - 1))
                                    }
                                },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    Icons.Default.Remove,
                                    contentDescription = "Disminuir",
                                    tint = Color.White
                                )
                            }

                            Text(
                                text = "${detalle.cantidad}",
                                color = Color.White,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )

                            IconButton(
                                onClick = {
                                    onCantidadCambiada(detalle.copy(cantidad = detalle.cantidad + 1))
                                },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    Icons.Default.Add,
                                    contentDescription = "Aumentar",
                                    tint = Color.White
                                )
                            }
                        }
                    }
                }

                Divider(color = Color.White.copy(alpha = 0.1f))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Subtotal:",
                        color = Color.LightGray,
                        fontSize = 14.sp
                    )
                    Text(
                        "$${String.format("%.2f", detalle.cantidad * detalle.precioUnitario)}",
                        color = Color(0xFF66BB6A),
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }
            }
        }
    }