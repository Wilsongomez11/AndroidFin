package com.example.proyectofinal.Screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.proyectofinal.Api.ApiClient
import com.example.proyectofinal.Model.DevolucionRequest
import com.example.proyectofinal.Model.Pedido
import com.example.proyectofinal.ViewModel.PedidoViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DevolucionDetalleScreen(
    navController: NavHostController,
    pedido: Pedido,
    pedidoViewModel: PedidoViewModel = viewModel()
) {
    val scope = rememberCoroutineScope()

    val cantidades = remember {
        mutableStateMapOf<Long, Int>().apply {
            pedido.detalles.forEach { put(it.producto.id!!, 0) }
        }
    }

    var reponerStock by remember { mutableStateOf(true) }
    var devolviendo by remember { mutableStateOf(false) }
    var mensajeError by remember { mutableStateOf<String?>(null) }
    var mostrarConfirmacion by remember { mutableStateOf(false) }

    val montoDevolucion = remember(cantidades.values.toList()) {
        pedido.detalles.sumOf { det ->
            val cantidadDevuelta = cantidades[det.producto.id] ?: 0
            cantidadDevuelta * det.precioUnitario
        }
    }

    val totalProductosDevueltos = remember(cantidades.values.toList()) {
        cantidades.values.sum()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Devoluci\u00F3n Pedido #${pedido.id}",
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
        containerColor = Color.Transparent
    ) { innerPadding ->
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
                .padding(innerPadding)
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
                            Column {
                                Text(
                                    "\u21A9 Devoluci\u00F3n",
                                    color = Color(0xFFEF5350),
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    "Selecciona productos a devolver",
                                    color = Color.LightGray,
                                    fontSize = 13.sp
                                )
                            }

                            Surface(
                                color = Color(0xFFEF5350).copy(alpha = 0.2f),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(
                                    text = "$totalProductosDevueltos items",
                                    color = Color(0xFFEF5350),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(
                                        horizontal = 12.dp,
                                        vertical = 6.dp
                                    )
                                )
                            }
                        }

                        Divider(color = Color.White.copy(alpha = 0.1f))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "\uD83D\uDCB0 Monto a devolver:",
                                color = Color.White,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                "$${String.format("%.2f", montoDevolucion)}",
                                color = Color(0xFFEF5350),
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Text(
                    "\uD83C\uDF55 Productos del Pedido",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(pedido.detalles) { det ->
                        val prodId = det.producto.id!!
                        val cantidadDevolver = cantidades[prodId] ?: 0

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
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            det.producto.nombre,
                                            color = Color.White,
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            "Comprado: ${det.cantidad} unidades",
                                            color = Color.LightGray,
                                            fontSize = 13.sp
                                        )
                                        Text(
                                            "Precio: $${String.format("%.2f", det.precioUnitario)}",
                                            color = Color.LightGray,
                                            fontSize = 12.sp
                                        )
                                    }

                                    if (cantidadDevolver > 0) {
                                        Surface(
                                            color = Color(0xFFEF5350).copy(alpha = 0.2f),
                                            shape = RoundedCornerShape(8.dp)
                                        ) {
                                            Text(
                                                "-$${String.format("%.2f", cantidadDevolver * det.precioUnitario)}",
                                                color = Color(0xFFEF5350),
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.padding(8.dp)
                                            )
                                        }
                                    }
                                }

                                Divider(color = Color.White.copy(alpha = 0.1f))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        "Cantidad a devolver:",
                                        color = Color.White,
                                        fontSize = 13.sp
                                    )

                                    Surface(
                                        color = Color(0xFF5E17EB).copy(alpha = 0.2f),
                                        shape = RoundedCornerShape(12.dp)
                                    ) {
                                        Row(
                                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier.padding(
                                                horizontal = 8.dp,
                                                vertical = 4.dp
                                            )
                                        ) {
                                            IconButton(
                                                onClick = {
                                                    val actual = cantidades[prodId] ?: 0
                                                    if (actual > 0) {
                                                        cantidades[prodId] = actual - 1
                                                    }
                                                },
                                                modifier = Modifier.size(32.dp)
                                            ) {
                                                Text(
                                                    "-",
                                                    color = Color.White,
                                                    fontSize = 20.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }

                                            Text(
                                                cantidadDevolver.toString(),
                                                color = Color.White,
                                                fontSize = 18.sp,
                                                fontWeight = FontWeight.Bold
                                            )

                                            IconButton(
                                                onClick = {
                                                    val actual = cantidades[prodId] ?: 0
                                                    if (actual < det.cantidad) {
                                                        cantidades[prodId] = actual + 1
                                                    }
                                                },
                                                modifier = Modifier.size(32.dp)
                                            ) {
                                                Text(
                                                    "+",
                                                    color = Color.White,
                                                    fontSize = 20.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(4.dp, RoundedCornerShape(16.dp)),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF1E1E1E)
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp)
                    ) {
                        Checkbox(
                            checked = reponerStock,
                            onCheckedChange = { reponerStock = it },
                            colors = CheckboxDefaults.colors(
                                checkedColor = Color(0xFF5E17EB),
                                uncheckedColor = Color.LightGray
                            )
                        )
                        Column(modifier = Modifier.padding(start = 8.dp)) {
                            Text(
                                "\uD83D\uDCE6 Reponer stock de insumos",
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                "Los insumos volver\u00E1n al inventario",
                                color = Color.LightGray,
                                fontSize = 12.sp
                            )
                        }
                    }
                }

                if (mensajeError != null) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFEF5350).copy(alpha = 0.2f)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "\u26A0 $mensajeError",
                            color = Color(0xFFEF5350),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }

                Button(
                    enabled = !devolviendo,
                    onClick = {
                        val cantidadesFiltradas = cantidades.filter { it.value > 0 }
                        if (cantidadesFiltradas.isEmpty()) {
                            mensajeError = "Selecciona al menos 1 producto para devolver"
                        } else {
                            mensajeError = null
                            mostrarConfirmacion = true
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFEF5350),
                        disabledContainerColor = Color.Gray
                    ),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                ) {
                    Text(
                        if (devolviendo) "Procesando..." else "\u21A9 Confirmar Devoluci\u00F3n",
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
                                mostrarConfirmacion = false
                                devolviendo = true

                                scope.launch {
                                    try {
                                        val cantidadesFiltradas = cantidades.filter { it.value > 0 }

                                        val request = DevolucionRequest(
                                            monto = montoDevolucion,
                                            reponerStock = reponerStock,
                                            cantidades = cantidadesFiltradas
                                        )

                                        val response = ApiClient.apiService.devolverPedido(
                                            pedido.id!!,
                                            request
                                        )

                                        if (response.isSuccessful) {
                                            pedidoViewModel.obtenerPedidos(forceRefresh = true)
                                            navController.popBackStack()
                                        } else {
                                            mensajeError = "Error: C\u00F3digo ${response.code()}"
                                        }
                                    } catch (e: Exception) {
                                        mensajeError = "No se pudo conectar con el servidor"
                                        Log.e("DevolucionScreen", "Error", e)
                                    } finally {
                                        devolviendo = false
                                    }
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFEF5350)
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
                            color = Color(0xFFEF5350).copy(alpha = 0.2f),
                            shape = CircleShape,
                            modifier = Modifier.size(56.dp)
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.fillMaxSize()
                            ) {
                                Text(text = "\u21A9", fontSize = 28.sp)
                            }
                        }
                    },
                    title = {
                        Text(
                            "Confirmar Devoluci\u00F3n",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                    },
                    text = {
                        Column {
                            Text(
                                "\u00BFDevolver $totalProductosDevueltos producto(s)?",
                                color = Color.White.copy(alpha = 0.9f),
                                fontSize = 16.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                "Monto: $${String.format("%.2f", montoDevolucion)}",
                                color = Color(0xFFEF5350),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                            if (reponerStock) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    "\u2714 Se repondr\u00E1 el stock",
                                    color = Color(0xFF66BB6A),
                                    fontSize = 13.sp
                                )
                            }
                        }
                    },
                    containerColor = Color(0xFF1E1E1E),
                    shape = RoundedCornerShape(20.dp)
                )
            }
        }
    }
}