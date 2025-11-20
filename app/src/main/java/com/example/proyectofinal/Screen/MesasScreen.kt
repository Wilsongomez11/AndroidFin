package com.example.proyectofinal.Screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
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
import androidx.navigation.NavHostController
import com.example.proyectofinal.ViewModel.PedidoViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.delay

@Composable
fun MesasScreen(
    navController: NavHostController,
    pedidoViewModel: PedidoViewModel = viewModel()
) {
    val estadoMesas by pedidoViewModel.estadoMesas.collectAsState()
    val mesas = remember { (1..12).toList() }
    var isLoading by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        isLoading = true
        pedidoViewModel.cargarEstadoMesas()
        delay(200)
        isLoading = false
    }

    val refreshTrigger = navController.currentBackStackEntry
        ?.savedStateHandle
        ?.getStateFlow("refreshMesas", false)
        ?.collectAsState()

    LaunchedEffect(refreshTrigger?.value) {
        if (refreshTrigger?.value == true) {
            pedidoViewModel.cargarEstadoMesas()
            navController.currentBackStackEntry
                ?.savedStateHandle
                ?.set("refreshMesas", false)
        }
    }

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
            .padding(16.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Mesas",
                    color = Color.White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    }

                    IconButton(
                        onClick = {
                            pedidoViewModel.cargarEstadoMesas()
                        }
                    ) {
                        Text("\uD83D\uDD04", fontSize = 20.sp)
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        modifier = Modifier.size(16.dp),
                        shape = CircleShape,
                        color = Color(0xFF5E17EB)
                    ) {}
                    Spacer(Modifier.width(6.dp))
                    Text("Libre", color = Color.White, fontSize = 14.sp)
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        modifier = Modifier.size(16.dp),
                        shape = CircleShape,
                        color = Color(0xFFEF5350)
                    ) {}
                    Spacer(Modifier.width(6.dp))
                    Text("Ocupada", color = Color.White, fontSize = 14.sp)
                }
            }

            if (estadoMesas.isEmpty() && isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(Modifier.height(16.dp))
                        Text(
                            "Cargando mesas...",
                            color = Color.White,
                            fontSize = 16.sp
                        )
                    }
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    verticalArrangement = Arrangement.spacedBy(18.dp),
                    horizontalArrangement = Arrangement.spacedBy(18.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(
                        items = mesas,
                        key = { it }
                    ) { mesa ->
                        val ocupada = estadoMesas[mesa] == "Ocupada"

                        MesaButton(
                            mesaNumero = mesa,
                            ocupada = ocupada,
                            onClick = {
                                if (!ocupada) {
                                    navController.navigate("crearPedido/$mesa")
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MesaButton(
    mesaNumero: Int,
    ocupada: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (ocupada) Color(0xFFEF5350) else Color(0xFF5E17EB)
    val borderColor = if (ocupada) Color(0xFFFFCDD2) else Color.White

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Surface(
            modifier = Modifier
                .size(100.dp)
                .shadow(10.dp, shape = CircleShape)
                .clickable(enabled = !ocupada) { onClick() },
            shape = CircleShape,
            color = backgroundColor,
            border = BorderStroke(2.dp, borderColor)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = mesaNumero.toString(),
                    color = Color.White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = if (ocupada) "Ocupada" else "Libre",
            color = Color.White,
            fontSize = 14.sp,
            textAlign = TextAlign.Center
        )
    }
}