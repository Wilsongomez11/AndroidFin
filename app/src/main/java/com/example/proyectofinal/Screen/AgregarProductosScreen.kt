package com.example.proyectofinal.Screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.proyectofinal.Model.Producto
import com.example.proyectofinal.ProductoViewModel
import com.example.proyectofinal.ViewModel.AdministradorViewModel

@Composable
fun AgregarProductoScreen(
    navController: NavHostController,
    viewModel: ProductoViewModel,
    adminViewModel: AdministradorViewModel
) {
    var nombre by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var cantidad by remember { mutableStateOf("") }
    var mensaje by remember { mutableStateOf("") }

    val administradorActual by adminViewModel.administradorActual.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF0B093B), Color(0xFF3A0CA3), Color(0xFF7209B7))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                "📦 Agregar Producto",
                color = Color.White,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Administrador: ${administradorActual?.nombre ?: "Ninguno"}",
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 14.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            CampoTexto("Nombre", nombre) { nombre = it }
            Spacer(modifier = Modifier.height(12.dp))
            CampoTexto("Cantidad", cantidad, KeyboardType.Number) { cantidad = it }
            Spacer(modifier = Modifier.height(12.dp))
            CampoTexto("Precio", precio, KeyboardType.Number) { precio = it }

            Spacer(modifier = Modifier.height(30.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BotonTarjetaPequeno(
                    emoji = "↩️",
                    label = "Volver",
                    colorFondo = Color(0xFF1E1E1E),
                    modifier = Modifier.weight(1f)
                ) {

                    val popped = navController.popBackStack(route = "inventario", inclusive = false)
                    if (!popped) {

                        navController.navigate("inventario") {
                            popUpTo("inventario") { inclusive = true }
                        }
                    }
                }

                BotonTarjetaPequeno(
                    emoji = "➕",
                    label = "Agregar",
                    colorFondo = Color(0xFF1E1E1E),
                    modifier = Modifier.weight(1f)
                ) {
                    if (administradorActual == null) {
                        mensaje = "No se ha detectado un administrador activo."
                        return@BotonTarjetaPequeno
                    }

                    val producto = Producto(
                        id = 0,
                        nombre = nombre,
                        precio = precio.toDoubleOrNull() ?: 0.0,
                        cantidad = cantidad.toIntOrNull() ?: 0,
                        idProveedor = 0,
                        idAdministrador = administradorActual!!.id ?: 0
                    )

                    viewModel.agregarProducto(
                        nombre,
                        producto.precio,
                        producto.cantidad,
                        0,
                        producto.idAdministrador.toInt()
                    ) { resultado ->
                        mensaje = resultado
                        if (resultado.contains("éxito", true)) {
                            Toast.makeText(
                                navController.context,
                                "Producto agregado correctamente",
                                Toast.LENGTH_SHORT
                            ).show()
                            navController.popBackStack()
                        }
                    }
                }
            }

            if (mensaje.isNotEmpty()) {
                Spacer(modifier = Modifier.height(20.dp))
                Text(mensaje, color = Color.White, fontSize = 16.sp)
            }
        }
    }
}

@Composable
fun CampoTexto(
    label: String,
    value: String,
    tipo: KeyboardType = KeyboardType.Text,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, color = Color.LightGray) },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = tipo),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.White,
            unfocusedBorderColor = Color.LightGray,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            focusedLabelColor = Color.White,
            unfocusedLabelColor = Color.LightGray,
            cursorColor = Color.White
        )
    )
}

@Composable
fun BotonTarjetaPequeno(
    emoji: String,
    label: String,
    colorFondo: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .height(80.dp)
            .shadow(8.dp, RoundedCornerShape(18.dp))
            .background(colorFondo, RoundedCornerShape(18.dp))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(emoji, fontSize = 24.sp)
            Spacer(Modifier.height(4.dp))
            Text(label, color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}
