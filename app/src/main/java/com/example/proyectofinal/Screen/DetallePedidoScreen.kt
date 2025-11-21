package com.example.proyectofinal.Screen

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
import com.example.proyectofinal.Model.DetallePedido
import com.example.proyectofinal.Model.Pedido
import com.example.proyectofinal.ViewModel.PedidoViewModel
import kotlinx.coroutines.launch
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.proyectofinal.ViewModel.FacturaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetallePedidoScreen(
    pedido: Pedido,
    navController: NavHostController,
    pedidoViewModel: PedidoViewModel,
    facturaViewModel: FacturaViewModel = viewModel()
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

                // -------------------- INFO DEL PEDIDO --------------------
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
                        }

                        pedido.mesa?.let {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    "\uD83E\uDE91 Mesa",
                                    color = Color.LightGray,
                                    fontSize = 12.sp
                                )
                                Text(
                                    "${it.numero}",
                                    color = Color(0xFF9C27B0),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                // -------------------- TOTAL --------------------
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

                Text(
                    "\uD83C\uDF55 Productos del Pedido",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                // -------------------- LISTA DE PRODUCTOS --------------------
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

                // -------------------- BOTÓN GENERAR FACTURA --------------------
                Button(
                    onClick = {
                        navController.navigate("factura/${pedido.id}")
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4CAF50)
                    ),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                ) {
                    Text("📄 Generar Factura", color = Color.White, fontSize = 18.sp)
                }
            }
        }
    }
}

// -------------------- CARD DE DETALLE --------------------
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
                            Icon(Icons.Default.Remove, null, tint = Color.White)
                        }

                        Text(
                            "${detalle.cantidad}",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )

                        IconButton(
                            onClick = {
                                onCantidadCambiada(detalle.copy(cantidad = detalle.cantidad + 1))
                            },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(Icons.Default.Add, null, tint = Color.White)
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

                Text("Subtotal:", color = Color.LightGray, fontSize = 14.sp)

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


private fun FacturaViewModel.generarFacturaRemota(lng: Long) {

}
