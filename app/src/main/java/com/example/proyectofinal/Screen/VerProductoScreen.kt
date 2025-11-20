package com.example.proyectofinal.Screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.proyectofinal.Model.Producto
import com.example.proyectofinal.ProductoViewModel
import kotlinx.coroutines.launch

@Composable
fun VerProductoScreen(
    navController: NavHostController,
    viewModel: ProductoViewModel
) {
    val productos by viewModel.productos.collectAsState()
    val listState = rememberLazyListState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.obtenerProductos()
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text(
                    text = "\uD83D\uDCCB Inventario de Productos",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(productos) { producto ->
                        ProductoCardItem(
                            producto = producto,
                            onEliminar = {
                                if (producto.id != null) {
                                    viewModel.eliminarProducto(producto.id) {
                                        scope.launch {
                                            snackbarHostState.showSnackbar("\uD83D\uDDD1\uFE0F Producto eliminado")
                                        }
                                    }
                                }
                            },
                            onActualizado = {
                                scope.launch {
                                    snackbarHostState.showSnackbar("\u2705 Producto actualizado correctamente")
                                }
                            },
                            navController = navController
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ProductoCardItem(
    producto: Producto,
    onEliminar: () -> Unit,
    onActualizado: () -> Unit,
    viewModel: ProductoViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    navController: NavHostController
) {
    var enEdicion by rememberSaveable(producto.id) { mutableStateOf(false) }
    var nombre by rememberSaveable(producto.id) { mutableStateOf(producto.nombre) }
    var precio by rememberSaveable(producto.id) { mutableStateOf(producto.precio.toString()) }
    var cantidad by rememberSaveable(producto.id) { mutableStateOf(producto.cantidad.toString()) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF5E17EB)),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            if (enEdicion) {
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = Color.Gray,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.LightGray
                    )
                )

                OutlinedTextField(
                    value = precio,
                    onValueChange = { precio = it },
                    label = { Text("Precio") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = Color.Gray,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.LightGray
                    )
                )

                OutlinedTextField(
                    value = cantidad,
                    onValueChange = { cantidad = it },
                    label = { Text("Cantidad") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = Color.Gray,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.LightGray
                    )
                )
            } else {
                Text(producto.nombre, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text("\uD83D\uDCB0 Precio: ${producto.precio}", color = Color.LightGray)
                Text("Cantidad: ${producto.cantidad}", color = Color.LightGray)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                if (enEdicion) {
                    Button(
                        onClick = {
                            try {
                                val dto = com.example.proyectofinal.Model.ProductoDTO(
                                    nombre = nombre,
                                    precio = precio.toDoubleOrNull() ?: producto.precio,
                                    cantidad = cantidad.toIntOrNull() ?: producto.cantidad,
                                    idProveedor = producto.idProveedor,
                                    idAdministrador = producto.idAdministrador
                                )
                                viewModel.actualizarProducto(producto.id ?: 0L, dto) {
                                    onActualizado()
                                }
                                enEdicion = false
                            } catch (e: Exception) {
                                println("Error al actualizar producto: ${e.message}")
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3A0CA3))
                    ) {
                        Text("Guardar", color = Color.White)
                    }
                } else {
                    Button(
                        onClick = { enEdicion = true },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3A0CA3))
                    ) {
                        Text("Editar", color = Color.White)
                    }
                }

                Button(
                    onClick = onEliminar,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("Eliminar", color = Color.White)
                }

                Button(
                    onClick = { navController.navigate("verReceta/${producto.id}") },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3A0CA3)),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.height(45.dp)
                ) {
                    Text("Ver Receta", color = Color.White)
                }
            }
        }
    }
}
