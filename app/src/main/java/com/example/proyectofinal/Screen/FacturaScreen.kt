package com.example.proyectofinal.Screen

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.proyectofinal.ViewModel.FacturaViewModel

@Composable
fun FacturaScreen(
    navController: NavHostController,
    id: Long,
    facturaViewModel: FacturaViewModel = viewModel()
) {

    val context = LocalContext.current

    val factura by facturaViewModel.facturaActual.collectAsState()
    val pdfFile by facturaViewModel.archivoPDF.collectAsState()
    val isLoading by facturaViewModel.isLoading.collectAsState()
    val mensaje by facturaViewModel.mensaje.collectAsState()

    // 🔹 Cargar la factura al entrar
    LaunchedEffect(id) {
        facturaViewModel.cargarFactura(id)
    }

    // 🔹 Pantalla de carga
    if (isLoading || factura == null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF0B093B)),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = Color.White)
        }
        return
    }

    val f = factura!!

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
            .padding(20.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Factura #${f.id}",
            fontSize = 28.sp,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF1E1E1E)
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {

                Text("📅 Fecha: ${f.fecha}", color = Color.White)
                Text("🧾 N° Factura: ${f.numeroFactura}", color = Color.White)
                Text("🛎 Pedido: ${f.pedido.id}", color = Color.White)
                Text("💳 Método de pago: ${f.metodoPago}", color = Color.White)

                Divider(Modifier.padding(vertical = 12.dp))

                // 🔹 Detalles del pedido
                f.pedido.detalles.forEach { det ->
                    Text(
                        "${det.cantidad} x ${det.producto.nombre} — $${det.cantidad * det.precioUnitario}",
                        color = Color.White
                    )
                }

                Divider(Modifier.padding(vertical = 12.dp))

                Text("Subtotal: $${"%.2f".format(f.subtotal)}", color = Color.White)
                Text("IVA: $${"%.2f".format(f.iva)}", color = Color.White)
                Text("Propina: $${"%.2f".format(f.propina)}", color = Color.White)

                Text(
                    "TOTAL: $${"%.2f".format(f.total)}",
                    fontSize = 22.sp,
                    color = Color(0xFFFF5252),
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ---------------------------
        // BOTÓN: GENERAR PDF
        // ---------------------------
        Button(
            onClick = { facturaViewModel.generarPDF(context, f) },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(Color(0xFF00C853)),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text("📄 Generar PDF", fontSize = 18.sp)
        }

        // ---------------------------
        // BOTONES PARA VER Y COMPARTIR
        // ---------------------------
        if (pdfFile != null) {
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {

                Button(
                    onClick = { facturaViewModel.verPDF(context) },
                    colors = ButtonDefaults.buttonColors(Color(0xFFFF9800)),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("👁 Ver", fontSize = 16.sp)
                }

                Button(
                    onClick = { facturaViewModel.compartirPDF(context) },
                    colors = ButtonDefaults.buttonColors(Color(0xFF2196F3)),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("📤 Compartir", fontSize = 16.sp)
                }
            }
        }

        if (mensaje.isNotEmpty()) {
            Spacer(modifier = Modifier.height(10.dp))
            Text(mensaje, color = Color.White)
        }
    }
}

