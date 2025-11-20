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
import com.example.proyectofinal.ViewModel.PedidoViewModel

@Composable
fun PedidoDeMesaScreen(
    navController: NavHostController,
    mesaId: Int,
    pedidoViewModel: PedidoViewModel = viewModel()
) {
    val pedidos by pedidoViewModel.pedidos.collectAsState()

    LaunchedEffect(Unit) {
        pedidoViewModel.obtenerPedidos()
    }

    val pedidoMesa = pedidos.find {
        it.mesa?.id?.toInt() == mesaId && it.estado != "Pagado"
    }


    Column(
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
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            "Mesa $mesaId",
            color = Color.White,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(20.dp))

        //SI LA MESA ESTÁ LIBRE
        if (pedidoMesa == null) {
            Text("Mesa disponible", color = Color.White)

            Spacer(Modifier.height(20.dp))

            Button(
                onClick = {
                    navController.navigate("crearPedido/$mesaId")
                },
                colors = ButtonDefaults.buttonColors(Color(0xFF7C4DFF))
            ) {
                Text("Crear Pedido", color = Color.White)
            }
        }

        else {

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(Color(0xFF1C1B29)),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {

                    Text("Pedido #${pedidoMesa.id}", color = Color(0xFFB388FF), fontSize = 20.sp)
                    Text("Estado: ${pedidoMesa.estado}", color = Color.White)
                    Text("Total: ${pedidoMesa.total}", color = Color(0xFF76FF03))

                    Spacer(Modifier.height(20.dp))

                    Button(
                        onClick = {
                            navController.navigate("detallePedido/${pedidoMesa.id}")
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(Color(0xFF7C4DFF))
                    ) {
                        Text("Ver pedido", color = Color.White)
                    }

                    Spacer(Modifier.height(10.dp))

                    Button(
                        onClick = {
                            navController.navigate("crearPedido/$mesaId")
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(Color(0xFF03DAC5))
                    ) {
                        Text("Agregar productos", color = Color.Black)
                    }

                    Spacer(Modifier.height(10.dp))

                    Button(
                        onClick = { navController.navigate("caja") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(Color(0xFF76FF03))
                    ) {
                        Text("Pagar", color = Color.Black)
                    }
                }
            }
        }
    }
}
