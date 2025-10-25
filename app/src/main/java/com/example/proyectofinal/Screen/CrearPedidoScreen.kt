package com.example.proyectofinal.Screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.proyectofinal.Model.Cliente
import com.example.proyectofinal.Model.DetallePedido
import com.example.proyectofinal.Model.Pedido
import com.example.proyectofinal.Model.Producto
import com.example.proyectofinal.ProductoViewModel
import com.example.proyectofinal.ViewModel.PedidoViewModel

@Composable
fun CrearPedidoScreen(
    navController: NavHostController,
    pedidoViewModel: PedidoViewModel = viewModel(),
    productoViewModel: ProductoViewModel = viewModel()
) {
    val productos by productoViewModel.productos.collectAsState()
    val mensaje by pedidoViewModel.mensaje.collectAsState()

    val carrito = remember { mutableStateListOf<DetallePedido>() }
    var mesa by remember { mutableStateOf("") }
    var clienteIdText by remember { mutableStateOf("") }

    LaunchedEffect(Unit) { productoViewModel.obtenerProductos() }

    val total = remember { derivedStateOf { carrito.sumOf { it.cantidad * it.precioUnitario } } }

    Scaffold { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFF0B093B), Color(0xFF3A0CA3), Color(0xFF7209B7))
                    )
                )
                .padding(padding)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {

                Text(
                    text = "Nuevo Pedido",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Spacer(Modifier.height(10.dp))

                OutlinedTextField(
                    value = mesa,
                    onValueChange = { mesa = it },
                    label = { Text("Mesa (opcional)", color = Color(0xFFD1C4E9)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = Color.Gray,
                        cursorColor = Color.White,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )

                OutlinedTextField(
                    value = clienteIdText,
                    onValueChange = { clienteIdText = it },
                    label = { Text("ID Cliente (opcional)", color = Color(0xFFD1C4E9)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = Color.Gray,
                        cursorColor = Color.White,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(productos) { producto ->
                        ProductoCard(producto, carrito)
                    }
                }

                Spacer(Modifier.height(16.dp))

                if (carrito.isNotEmpty()) {
                    Text(
                        text = "Productos agregados:",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    val carritoListState = rememberLazyListState()
                    LaunchedEffect(carrito.size) {
                        if (carrito.isNotEmpty()) {
                            carritoListState.animateScrollToItem(carrito.size - 1)
                        }
                    }
                    LazyColumn(
                        state = carritoListState,
                        modifier = Modifier
                            .height(180.dp)
                            .padding(bottom = 10.dp)
                    ) {
                        items(carrito) { detalle ->
                            CarritoItem(detalle, onEliminar = { carrito.remove(detalle) })
                        }
                    }
                }

                Text(
                    text = "Total: $${String.format("%.2f", total.value)}",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Spacer(Modifier.height(10.dp))

                Button(
                    onClick = {
                        if (carrito.isNotEmpty()) {
                            val pedido = Pedido(
                                estado = "Pendiente",
                                total = total.value,
                                mesa = mesa.ifBlank { null },
                                cliente = clienteIdText.toLongOrNull()?.let { Cliente(id = it) },
                                detalles = carrito.toList()
                            )

                            pedidoViewModel.crearPedido(pedido) {
                                carrito.clear()
                                navController.navigate("pedidos") {
                                    popUpTo("pedidos") { inclusive = true }
                                }
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5E17EB)),
                    modifier = Modifier.fillMaxWidth().height(55.dp),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text("Registrar Pedido", color = Color.White, fontSize = 18.sp)
                }

                Spacer(Modifier.height(8.dp))

                Button(
                    onClick = { navController.popBackStack() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E1E1E)),
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text("Volver", color = Color.White)
                }

                if (mensaje.isNotEmpty()) {
                    Text(
                        mensaje,
                        color = Color.Yellow,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(top = 8.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ProductoCard(producto: Producto, carrito: MutableList<DetallePedido>) {
    var cantidad by remember { mutableStateOf("") }
    var advertencia by remember { mutableStateOf("") }

    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFF3A0CA3)),
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(producto.nombre, color = Color.White, fontWeight = FontWeight.Bold)
            Text("Precio: $${producto.precio}", color = Color.LightGray)
            Text(
                "Disponibles: ${producto.cantidad} unidades",
                color = Color(0xFFFFC107),
                fontSize = 13.sp
            )

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = cantidad,
                    onValueChange = { cantidad = it },
                    label = {
                        Text("Cantidad", color = Color(0xFFD1C4E9), fontSize = 13.sp)
                    },
                    textStyle = TextStyle(
                        color = Color.White,
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center,
                        lineHeight = 26.sp
                    ),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .width(110.dp)
                        .height(64.dp)
                        .padding(vertical = 2.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = Color.Gray,
                        cursorColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp)
                )

                Button(
                    onClick = {
                        val cant = cantidad.toIntOrNull()
                        if (cant != null && cant > 0) {
                            if (cant > producto.cantidad) {
                                advertencia = "No hay suficientes unidades disponibles"
                            } else {
                                val existente = carrito.find { it.producto.id == producto.id }
                                if (existente != null) {
                                    val index = carrito.indexOf(existente)
                                    carrito[index] = existente.copy(cantidad = existente.cantidad + cant)
                                } else {
                                    carrito.add(
                                        DetallePedido(
                                            producto = producto,
                                            cantidad = cant,
                                            precioUnitario = producto.precio
                                        )
                                    )
                                }
                                advertencia = ""
                                cantidad = ""
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5E17EB)),
                    modifier = Modifier.height(56.dp)
                ) {
                    Text("Agregar", color = Color.White, fontSize = 14.sp)
                }
            }

            if (advertencia.isNotEmpty()) {
                Text(
                    text = advertencia,
                    color = Color(0xFFFFC107),
                    fontSize = 13.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}

@Composable
fun CarritoItem(detalle: DetallePedido, onEliminar: () -> Unit) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFF4A148C)),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(detalle.producto.nombre, color = Color.White, fontWeight = FontWeight.Bold)
                Text(
                    "x${detalle.cantidad}  •  Subtotal: $${String.format("%.2f", detalle.cantidad * detalle.precioUnitario)}",
                    color = Color.LightGray,
                    fontSize = 13.sp
                )
            }
            IconButton(onClick = onEliminar) {
                Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = Color.Red)
            }
        }
    }
}


