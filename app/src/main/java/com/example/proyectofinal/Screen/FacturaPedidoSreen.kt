@file:OptIn(ExperimentalMaterial3Api::class)
package com.example.proyectofinal.Screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import com.example.proyectofinal.Model.GenerarFacturaRequest
import com.example.proyectofinal.Model.Pedido
import com.example.proyectofinal.ViewModel.FacturaViewModel

@Composable
fun FacturarPedidoScreen(
    pedido: Pedido,
    navController: NavHostController,
    facturaViewModel: FacturaViewModel
) {
    var metodoPago by remember { mutableStateOf("EFECTIVO") }
    var propina by remember { mutableStateOf("") }
    var clienteNombre by remember { mutableStateOf("") }
    var clienteDocumento by remember { mutableStateOf("") }

    var mensaje by remember { mutableStateOf("") }
    var cargando by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Generar Factura",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
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
    ) { padding ->
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
                .padding(padding)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Text(
                "Pedido #${pedido.id}",
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                "Total: $${String.format("%.2f", pedido.total)}",
                color = Color(0xFF66BB6A),
                fontSize = 26.sp,
                fontWeight = FontWeight.ExtraBold
            )

            OutlinedTextField(
                value = clienteNombre,
                onValueChange = { clienteNombre = it },
                label = { Text("Nombre del cliente (opcional)") },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.LightGray,
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.LightGray,
                    cursorColor = Color.White
                ),
                textStyle = LocalTextStyle.current.copy(color = Color.White),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = clienteDocumento,
                onValueChange = { clienteDocumento = it },
                label = { Text("Documento (opcional)") },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.LightGray,
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.LightGray,
                    cursorColor = Color.White
                ),
                textStyle = LocalTextStyle.current.copy(color = Color.White),
                modifier = Modifier.fillMaxWidth()
            )

            // ---------------- PROPINA OPCIONAL ----------------
            OutlinedTextField(
                value = propina,
                onValueChange = { propina = it },
                label = { Text("Propina (opcional)") },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.LightGray,
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.LightGray,
                    cursorColor = Color.White
                ),
                textStyle = LocalTextStyle.current.copy(color = Color.White),
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                "Método de pago",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                MetodoPagoChip(
                    label = "Efectivo",
                    selected = metodoPago == "EFECTIVO"
                ) { metodoPago = "EFECTIVO" }

                MetodoPagoChip(
                    label = "Nequi",
                    selected = metodoPago == "NEQUI"
                ) { metodoPago = "NEQUI" }

                MetodoPagoChip(
                    label = "Datafono",
                    selected = metodoPago == "DATAFONO"
                ) { metodoPago = "DATAFONO" }
            }

            // ---------------- BOTÓN GENERAR ----------------
            Button(
                onClick = {
                    cargando = true
                    mensaje = ""

                    val request = GenerarFacturaRequest(
                        pedidoId = pedido.id!!,
                        metodoPago = metodoPago,
                        propina = propina.toDoubleOrNull() ?: 0.0,
                        clienteNombre = if (clienteNombre.isBlank()) null else clienteNombre,
                        clienteDocumento = if (clienteDocumento.isBlank()) null else clienteDocumento
                    )

                    facturaViewModel.generarFactura(request) { factura, error ->
                        cargando = false
                        mensaje = error ?: "Factura generada correctamente"

                        if (error == null) {
                            navController.popBackStack()
                        }
                    }
                },
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(Color(0xFF5E17EB)),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp)
            ) {
                Text(
                    if (cargando) "Generando..." else "Generar Factura",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            if (mensaje.isNotEmpty()) {
                Text(
                    mensaje,
                    color = if (mensaje.contains("correcta")) Color(0xFF00FF9D) else Color(0xFFFF6B6B),
                    fontSize = 16.sp
                )
            }
        }
    }
}

@Composable
fun MetodoPagoChip(label: String, selected: Boolean, onClick: () -> Unit) {
    Surface(
        color = if (selected) Color(0xFF5E17EB) else Color(0x33FFFFFF),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .height(40.dp)
            .clickable { onClick() }
            .padding(horizontal = 10.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.padding(horizontal = 12.dp)
        ) {
            Text(
                label,
                color = Color.White,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
            )
        }
    }
}
