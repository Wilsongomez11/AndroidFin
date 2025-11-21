package com.example.proyectofinal.Screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.proyectofinal.Model.Mesa
import com.example.proyectofinal.Model.Pedido
import com.example.proyectofinal.Model.Producto
import com.example.proyectofinal.ProductoViewModel
import com.example.proyectofinal.ViewModel.PedidoViewModel

@Composable
fun CrearPedidoScreen(
    navController: NavHostController,
    mesaId: Int? = null,
    pedidoViewModel: PedidoViewModel = viewModel(),
    productoViewModel: ProductoViewModel = viewModel()
) {
    val productos by productoViewModel.productos.collectAsState()
    val mensaje by pedidoViewModel.mensaje.collectAsState()
    val errorDetallado by pedidoViewModel.errorDetallado.collectAsState()
    val pedidos by pedidoViewModel.pedidos.collectAsState()

    var mesaTexto by remember { mutableStateOf(mesaId?.toString() ?: "") }
    val mesaEsFija = mesaId != null

    val carrito = remember { mutableStateListOf<DetallePedido>() }
    var clienteIdText by remember { mutableStateOf("") }

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        productoViewModel.obtenerProductos()
        pedidoViewModel.obtenerPedidos()
    }

    LaunchedEffect(errorDetallado) {
        errorDetallado?.let { error ->
            snackbarHostState.showSnackbar(
                message = error,
                duration = SnackbarDuration.Long
            )
            pedidoViewModel.limpiarError()
        }
    }

    val total = remember {
        derivedStateOf { carrito.sumOf { it.cantidad * it.precioUnitario } }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(snackbarHostState) { data ->
                Snackbar(
                    snackbarData = data,
                    containerColor = if (data.visuals.message.contains("✅")) {
                        Color(0xFF4CAF50)
                    } else {
                        Color(0xFFD32F2F)
                    },
                    contentColor = Color.White,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    ) { padding ->
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
                    text = "🛒 Nuevo Pedido",
                    color = Color.White,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Spacer(Modifier.height(10.dp))

                // Campo Mesa
                OutlinedTextField(
                    value = mesaTexto,
                    onValueChange = { if (!mesaEsFija) mesaTexto = it },
                    label = { Text("Mesa", color = Color(0xFFD1C4E9)) },
                    enabled = !mesaEsFija,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = Color.Gray,
                        disabledBorderColor = Color.LightGray,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        disabledTextColor = Color.Gray,
                        cursorColor = Color.White
                    )
                )

                // Cliente opcional
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

                // Lista de productos
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(productos) { producto ->
                        ProductoCard(producto, carrito)
                    }
                }

                Spacer(Modifier.height(16.dp))

                // Carrito
                if (carrito.isNotEmpty()) {
                    Text(
                        text = "📦 Productos agregados:",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    LazyColumn(
                        modifier = Modifier
                            .height(180.dp)
                            .padding(bottom = 10.dp)
                    ) {
                        items(carrito) { detalle ->
                            CarritoItem(detalle) { carrito.remove(detalle) }
                        }
                    }
                }

                // Total
                Text(
                    text = "💰 Total: $${String.format("%.2f", total.value)}",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Spacer(Modifier.height(12.dp))

                // BOTÓN REGISTRAR PEDIDO
                Button(
                    onClick = {
                        if (carrito.isEmpty()) {
                            return@Button
                        }

                        val clienteObj = clienteIdText.toLongOrNull()?.let { Cliente(id = it) }

                        //  CREAR OBJETO MESA CORRECTAMENTE
                        val mesaNumero = mesaId ?: mesaTexto.toIntOrNull()
                        val mesaObj = if (mesaNumero != null) {
                            Mesa(id = mesaNumero.toLong(), numero = mesaNumero, estado = "Ocupada")
                        } else {
                            null
                        }

                        // Verificar si existe pedido pendiente en esa mesa
                        val pedidoExistente = pedidos.find { pedido ->
                            pedido.mesa?.numero == mesaNumero &&
                                    pedido.estado !in listOf("Pagado", "Devuelto")
                        }

                        if (pedidoExistente != null) {
                            // AGREGAR PRODUCTOS AL PEDIDO EXISTENTE
                            val detallesActualizados = pedidoExistente.detalles.toMutableList()

                            carrito.forEach { nuevo ->
                                val viejo = detallesActualizados.find {
                                    it.producto.id == nuevo.producto.id
                                }

                                if (viejo != null) {
                                    val index = detallesActualizados.indexOf(viejo)
                                    detallesActualizados[index] =
                                        viejo.copy(cantidad = viejo.cantidad + nuevo.cantidad)
                                } else {
                                    detallesActualizados.add(nuevo)
                                }
                            }

                            val nuevoTotal = detallesActualizados.sumOf {
                                it.cantidad * it.precioUnitario
                            }

                            val pedidoActualizado = pedidoExistente.copy(
                                mesa = pedidoExistente.mesa ?: mesaObj,
                                total = nuevoTotal,
                                cliente = clienteObj ?: pedidoExistente.cliente,
                                detalles = detallesActualizados
                            )

                            pedidoViewModel.actualizarPedido(pedidoActualizado) { resultado ->
                                if (resultado.contains("✅")) {
                                    carrito.clear()
                                    navController.popBackStack()
                                }
                            }

                        } else {
                            // CREAR PEDIDO NUEVO
                            val pedidoNuevo = Pedido(
                                estado = "Pendiente",
                                total = total.value,
                                mesa = mesaObj,
                                cliente = clienteObj,
                                mesero = null,
                                detalles = carrito.toList()
                            )

                            pedidoViewModel.crearPedido(pedidoNuevo) { resultado ->
                                if (resultado.contains("✅")) {
                                    carrito.clear()
                                    pedidoViewModel.obtenerPedidos(forceRefresh = true)
                                    pedidoViewModel.cargarEstadoMesas()

                                    navController.navigate("mesas") {
                                        popUpTo("mesas") { inclusive = true }
                                    }
                                }
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5E17EB)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text("✅ Registrar Pedido", color = Color.White, fontSize = 18.sp)
                }

                if (mensaje.isNotEmpty() && !mensaje.contains("Stock insuficiente")) {
                    Text(
                        mensaje,
                        color = Color.Yellow,
                        modifier = Modifier.padding(top = 8.dp)
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
            Text("Disponibles: ${producto.cantidad}", color = Color(0xFFFFC107), fontSize = 13.sp)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = cantidad,
                    onValueChange = { cantidad = it },
                    label = { Text("Cant.", color = Color(0xFFD1C4E9)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    textStyle = TextStyle(
                        color = Color.White,
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier
                        .width(110.dp)
                        .height(60.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = Color.Gray,
                        cursorColor = Color.White
                    )
                )

                Button(
                    onClick = {
                        val cant = cantidad.toIntOrNull()
                        if (cant == null || cant <= 0) return@Button

                        val existente = carrito.find { it.producto.id == producto.id }
                        if (existente != null) {
                            val index = carrito.indexOf(existente)
                            carrito[index] =
                                existente.copy(cantidad = existente.cantidad + cant)
                        } else {
                            carrito.add(
                                DetallePedido(
                                    producto = producto,
                                    cantidad = cant,
                                    precioUnitario = producto.precio
                                )
                            )
                        }
                        cantidad = ""
                        advertencia = ""
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5E17EB)),
                    modifier = Modifier.height(56.dp)
                ) {
                    Text("Agregar", color = Color.White)
                }
            }

            if (advertencia.isNotEmpty()) {
                Text(advertencia, color = Color(0xFFFFC107), fontSize = 13.sp)
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
                    "x${detalle.cantidad}  -  $${detalle.cantidad * detalle.precioUnitario}",
                    color = Color.LightGray,
                    fontSize = 13.sp
                )
            }

            IconButton(onClick = onEliminar) {
                Icon(Icons.Default.Delete, contentDescription = null, tint = Color.Red)
            }
        }
    }
}