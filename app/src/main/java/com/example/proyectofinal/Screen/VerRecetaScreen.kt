package com.example.proyectofinal.Screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
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
import com.example.proyectofinal.Model.Insumo
import com.example.proyectofinal.Model.Producto
import com.example.proyectofinal.Model.ProductoInsumo
import com.example.proyectofinal.ProductoViewModel
import com.example.proyectofinal.ViewModel.InsumoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerRecetaScreen(
    producto: Producto,
    navController: NavHostController,
    viewModel: ProductoViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    insumoViewModel: InsumoViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val relaciones by viewModel.relaciones.collectAsState()
    val listaInsumos by insumoViewModel.insumos.collectAsState()

    var expanded by remember { mutableStateOf(false) }
    var insumoSeleccionado by remember { mutableStateOf<Insumo?>(null) }
    var cantidadUsada by remember { mutableStateOf("") }

    var showDeleteDialog by remember { mutableStateOf(false) }
    var relacionAEliminar by remember { mutableStateOf<ProductoInsumo?>(null) }

    LaunchedEffect(producto.id) {
        viewModel.obtenerRelacionesPorProducto(producto.id!!)
        insumoViewModel.obtenerInsumos()
    }

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

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "\uD83D\uDCC3 Receta de ${producto.nombre}",
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "ID: ${producto.id}",
                    color = Color.LightGray,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // ------------------ SELECCIÓN DE INSUMOS ------------------
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        readOnly = true,
                        value = insumoSeleccionado?.nombre ?: "Selecciona un insumo",
                        onValueChange = {},
                        label = { Text("Insumo", color = Color.LightGray) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.White,
                            unfocusedBorderColor = Color.Gray,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                        },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        listaInsumos.forEach { insumo ->
                            DropdownMenuItem(
                                text = { Text(insumo.nombre) },
                                onClick = {
                                    insumoSeleccionado = insumo
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = cantidadUsada,
                    onValueChange = { cantidadUsada = it },
                    label = { Text("Cantidad usada", color = Color.LightGray) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = Color.Gray,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    onClick = {
                        if (insumoSeleccionado != null && cantidadUsada.isNotEmpty()) {
                            viewModel.agregarRelacion(
                                ProductoInsumo(
                                    producto = producto,
                                    insumo = insumoSeleccionado,
                                    cantidadUsada = cantidadUsada.toDouble()
                                )
                            )
                            cantidadUsada = ""
                            insumoSeleccionado = null
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5E17EB)),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text("\u2795 Agregar Insumo", color = Color.White)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "\uD83D\uDCCB Insumos asociados:",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                if (relaciones.isEmpty()) {
                    Text("No hay insumos asociados aún", color = Color.LightGray)
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(vertical = 8.dp)
                    ) {
                        items(relaciones) { rel ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .shadow(4.dp, RoundedCornerShape(20.dp)),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFF3A0CA3))
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {

                                    Column {
                                        Text("ID Insumo: ${rel.insumo?.id}", color = Color.White)
                                        Text("Nombre: ${rel.insumo?.nombre}", color = Color.White)
                                        Text("Cantidad usada: ${rel.cantidadUsada}", color = Color.LightGray)
                                    }

                                    IconButton(
                                        onClick = {
                                            relacionAEliminar = rel
                                            showDeleteDialog = true
                                        }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Filled.Delete,
                                            contentDescription = "Eliminar",
                                            tint = Color(0xFFFF4B4B)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // ------------------------ POPUP DE CONFIRMACIÓN ------------------------
    if (showDeleteDialog && relacionAEliminar != null) {

        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = {
                Text("Eliminar insumo", color = Color.White)
            },
            text = {
                Text(
                    "¿Seguro que deseas eliminar este insumo de la receta?",
                    color = Color.LightGray
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.eliminarRelacion(
                            relacionId = relacionAEliminar!!.id!!,
                            productoId = producto.id!!
                        )
                        showDeleteDialog = false
                        relacionAEliminar = null
                    },
                    colors = ButtonDefaults.buttonColors(Color(0xFFEF5350))
                ) {
                    Text("Eliminar", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancelar", color = Color.LightGray)
                }
            },
            containerColor = Color(0xFF1C1B29),
            shape = RoundedCornerShape(22.dp)
        )
    }
}

