package com.example.proyectofinal.admin

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.proyectofinal.ViewModel.AdministradorViewModel

@Composable
fun VerPersonalScreen(
    navController: NavHostController,
    viewModel: AdministradorViewModel = viewModel()
) {
    val administradores by viewModel.administradores.collectAsState()
    val meseros by viewModel.meseros.collectAsState()
    val pizzeros by viewModel.pizzeros.collectAsState()
    val error by viewModel.error.collectAsState()
    var mensaje by remember { mutableStateOf("") }

    // Cargar datos
    LaunchedEffect(Unit) {
        viewModel.cargarAdministradores()
        viewModel.cargarMeseros()
        viewModel.cargarPizzeros()
    }

    val personal = remember(administradores, meseros, pizzeros) {
        buildList {
            addAll(administradores.map { Triple(it.id, it.nombre, "Administrador") })
            addAll(meseros.map { Triple(it.id, it.nombre, "Mesero") })
            addAll(pizzeros.map { Triple(it.id, it.nombre, "Pizzero") })
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF0B093B), Color(0xFF3A0CA3), Color(0xFF7209B7))
                )
            )
            .padding(16.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "👥 Lista de Personal",
                fontSize = 24.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )

            if (error != null) {
                Text("Error: $error", color = Color.Red)
            }

            if (mensaje.isNotEmpty()) {
                Text(mensaje, color = Color.White, fontSize = 14.sp)
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (personal.isEmpty()) {
                    item {
                        Text("Cargando personal...", color = Color.White)
                    }
                } else {
                    items(
                        items = personal,
                        key = { "${it.first ?: 0}_${it.third}" }
                    ) { persona ->
                        Box(
                            modifier = Modifier
                                .animateContentSize(animationSpec = spring(dampingRatio = 0.8f, stiffness = 200f))
                        ) {
                            PersonalEditableCard(
                                id = persona.first,
                                nombre = persona.second,
                                cargo = persona.third,
                                viewModel = viewModel,
                                onResult = { mensaje = it }
                            )
                        }
                    }
                }
            }


            Button(
                onClick = { navController.navigate("personalMenu") },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E1E1E)),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .height(60.dp)
                    .width(140.dp)
            ) {
                Text("↩️ Volver", color = Color.White, fontSize = 16.sp)
            }
        }
    }
}

@Composable
fun PersonalEditableCard(
    id: Long?,
    nombre: String,
    cargo: String,
    viewModel: AdministradorViewModel,
    onResult: (String) -> Unit
) {
    var enEdicion by rememberSaveable(id) { mutableStateOf(false) }
    var nombreEditado by rememberSaveable(id) { mutableStateOf(nombre) }
    var username by rememberSaveable(id) { mutableStateOf("") }
    var password by rememberSaveable(id) { mutableStateOf("") }
    var telefono by rememberSaveable(id) { mutableStateOf("") }
    var correo by rememberSaveable(id) { mutableStateOf("") }
    var direccion by rememberSaveable(id) { mutableStateOf("") }
    var visible by remember { mutableStateOf(true) }

    val fondoDegradado = Brush.verticalGradient(
        listOf(Color(0xFF3A0CA3), Color(0xFF7209B7))
    )
    AnimatedVisibility(visible = visible) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(10.dp, RoundedCornerShape(20.dp))
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF3A0CA3), // morado oscuro
                            Color(0xFF7209B7)  // morado brillante
                        )
                    ),
                    shape = RoundedCornerShape(20.dp)
                )
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (!enEdicion) {
                    Text(
                        text = nombre,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Text(
                        text = cargo,
                        color = Color(0xFFD1C4E9),
                        fontSize = 14.sp
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        Button(
                            onClick = { enEdicion = true },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF5E17EB) // violeta puro
                            ),
                            shape = RoundedCornerShape(20.dp),
                            modifier = Modifier
                                .height(42.dp)
                                .width(100.dp)
                        ) {
                            Text("Editar", color = Color.White, fontSize = 14.sp)
                        }

                        Button(
                            onClick = {
                                if (id != null) {
                                    when (cargo) {
                                        "Administrador" -> viewModel.eliminarAdministrador(id)
                                        "Mesero" -> viewModel.eliminarMesero(id)
                                        "Pizzero" -> viewModel.eliminarPizzero(id)
                                    }
                                    onResult("Eliminado correctamente")
                                    visible = false
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFD32F2F) // rojo intenso
                            ),
                            shape = RoundedCornerShape(20.dp),
                            modifier = Modifier
                                .height(42.dp)
                                .width(100.dp)
                        ) {
                            Text("Eliminar", color = Color.White, fontSize = 14.sp)
                        }
                    }
                } else {
                    Text(text = "Editando $cargo", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)

                        OutlinedTextField(
                            value = nombreEditado,
                            onValueChange = { nombreEditado = it },
                            label = { Text("Nombre", color = Color.LightGray) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color.White,
                                unfocusedBorderColor = Color.Gray,
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )

                        when (cargo) {
                            "Administrador" -> {
                                OutlinedTextField(
                                    value = username,
                                    onValueChange = { username = it },
                                    label = { Text("Usuario", color = Color.LightGray) },
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = Color.White,
                                        unfocusedBorderColor = Color.Gray,
                                        focusedTextColor = Color.White,
                                        unfocusedTextColor = Color.White
                                    ),
                                    modifier = Modifier.fillMaxWidth()
                                )
                                OutlinedTextField(
                                    value = password,
                                    onValueChange = { password = it },
                                    label = { Text("Contraseña", color = Color.LightGray) },
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = Color.White,
                                        unfocusedBorderColor = Color.Gray,
                                        focusedTextColor = Color.White,
                                        unfocusedTextColor = Color.White
                                    ),
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }

                            "Mesero" -> {
                                OutlinedTextField(
                                    value = telefono,
                                    onValueChange = { telefono = it },
                                    label = { Text("Teléfono", color = Color.LightGray) },
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = Color.White,
                                        unfocusedBorderColor = Color.Gray,
                                        focusedTextColor = Color.White,
                                        unfocusedTextColor = Color.White
                                    ),
                                    modifier = Modifier.fillMaxWidth()
                                )
                                OutlinedTextField(
                                    value = correo,
                                    onValueChange = { correo = it },
                                    label = { Text("Correo", color = Color.LightGray) },
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = Color.White,
                                        unfocusedBorderColor = Color.Gray,
                                        focusedTextColor = Color.White,
                                        unfocusedTextColor = Color.White
                                    ),
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }

                            "Pizzero" -> {
                                OutlinedTextField(
                                    value = telefono,
                                    onValueChange = { telefono = it },
                                    label = { Text("Teléfono", color = Color.LightGray) },
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = Color.White,
                                        unfocusedBorderColor = Color.Gray,
                                        focusedTextColor = Color.White,
                                        unfocusedTextColor = Color.White
                                    ),
                                    modifier = Modifier.fillMaxWidth()
                                )
                                OutlinedTextField(
                                    value = direccion,
                                    onValueChange = { direccion = it },
                                    label = { Text("Dirección", color = Color.LightGray) },
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = Color.White,
                                        unfocusedBorderColor = Color.Gray,
                                        focusedTextColor = Color.White,
                                        unfocusedTextColor = Color.White
                                    ),
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }

                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            Button(
                                onClick = {
                                    if (id != null) {
                                        when (cargo) {
                                            "Administrador" -> viewModel.editarAdministrador(
                                                id,
                                                nombreEditado,
                                                cargo,
                                                password,
                                                username,
                                                onResult
                                            )

                                            "Mesero" -> viewModel.editarMesero(
                                                id,
                                                nombreEditado,
                                                telefono,
                                                correo,
                                                username,
                                                password,
                                                onResult
                                            )

                                            "Pizzero" -> viewModel.editarPizzero(
                                                id,
                                                nombreEditado,
                                                telefono,
                                                direccion,
                                                username,
                                                password,
                                                onResult
                                            )
                                        }
                                        enEdicion = false
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(
                                        0xFF1E88E5
                                    )
                                ),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Text("Guardar", color = Color.White)
                            }

                            Button(
                                onClick = { enEdicion = false },
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Text("Cancelar", color = Color.White)
                            }
                        }
                    }
                }
            }
        }
    }
