@file:OptIn(ExperimentalMaterial3Api::class)
package com.example.proyectofinal

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
import com.example.proyectofinal.ViewModel.AgregarPersonalViewModel

@Composable
fun AgregarPersonalScreen(
    navController: NavHostController,
    viewModel: AgregarPersonalViewModel = viewModel()
) {
    var formularioActivo by remember { mutableStateOf("") }
    var mensaje by remember { mutableStateOf("") }

    val campoColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = Color(0xFF7209B7),
        unfocusedBorderColor = Color.Gray,
        focusedTextColor = Color.White,
        unfocusedTextColor = Color.White,
        focusedLabelColor = Color(0xFF7209B7),
        unfocusedLabelColor = Color.LightGray,
        cursorColor = Color(0xFF7209B7)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF0B093B), Color(0xFF3A0CA3), Color(0xFF7209B7))
                )
            )
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "\uD83D\uDC65 Agregar Personal",
                color = Color.White,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(24.dp))

            when (formularioActivo) {
                "" -> {
                    ButtonElegante("Mesero", onClick = { formularioActivo = "Mesero" })
                    Spacer(modifier = Modifier.height(16.dp))
                    ButtonElegante("Pizzero", onClick = { formularioActivo = "Pizzero" })
                    Spacer(modifier = Modifier.height(16.dp))
                    ButtonElegante("Admin", onClick = { formularioActivo = "Admin" })
                }

                "Mesero" -> {
                    FormularioMesero(
                        campoColors = campoColors,
                        onGuardar = { nombre, telefono, correo, username, password ->
                            viewModel.guardarMesero(nombre, telefono, correo, username, password) {
                                mensaje = it
                            }
                        }
                    )
                }

                "Pizzero" -> {
                    FormularioPizzero(
                        campoColors = campoColors,
                        onGuardar = { nombre, telefono, direccion, username, password ->
                            viewModel.guardarPizzero(nombre, telefono, direccion, username, password) {
                                mensaje = it
                            }
                        }
                    )
                }

                "Admin" -> {
                    FormularioAdmin(
                        campoColors = campoColors,
                        onGuardar = { nombre, username, password, cargo ->
                            viewModel.guardarAdministrador(nombre, username, password, cargo) {
                                mensaje = it
                            }
                        }
                    )
                }
            }

            if (mensaje.isNotBlank()) {
                Spacer(modifier = Modifier.height(20.dp))
                Text(mensaje, color = Color.White, fontSize = 16.sp)
            }
        }
    }
}

@Composable
fun ButtonElegante(texto: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .padding(8.dp)
            .size(120.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5E17EB))
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            val icono = when {
                texto.contains("Mesero", ignoreCase = true) -> "\uD83C\uDF7D\uFE0F"
                texto.contains("Pizzero", ignoreCase = true) -> "\uD83C\uDF55"
                texto.contains("Admin", ignoreCase = true) -> "\uD83D\uDCBC"
                else -> "\u2795"
            }
            Text(icono, fontSize = 28.sp)
            Spacer(modifier = Modifier.height(6.dp))
            Text(texto, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
fun FormularioMesero(
    campoColors: TextFieldColors,
    onGuardar: (String, String, String, String, String) -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Text("\uD83C\uDF7D\uFE0F Nuevo Mesero", color = Color.White, fontSize = 22.sp)
    Spacer(modifier = Modifier.height(16.dp))

    OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth(), colors = campoColors)
    OutlinedTextField(value = telefono, onValueChange = { telefono = it }, label = { Text("Teléfono") }, modifier = Modifier.fillMaxWidth(), colors = campoColors)
    OutlinedTextField(value = correo, onValueChange = { correo = it }, label = { Text("Correo") }, modifier = Modifier.fillMaxWidth(), colors = campoColors)
    OutlinedTextField(value = username, onValueChange = { username = it }, label = { Text("Usuario") }, modifier = Modifier.fillMaxWidth(), colors = campoColors)
    OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("Contraseña") }, modifier = Modifier.fillMaxWidth(), colors = campoColors)

    Spacer(modifier = Modifier.height(20.dp))
    ButtonElegante("Guardar") {
        if (nombre.isNotBlank() && telefono.isNotBlank() && correo.isNotBlank() && username.isNotBlank() && password.isNotBlank()) {
            onGuardar(nombre, telefono, correo, username, password)
        }
    }
}

@Composable
fun FormularioPizzero(
    campoColors: TextFieldColors,
    onGuardar: (String, String, String, String, String) -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Text("\uD83C\uDF55 Nuevo Pizzero", color = Color.White, fontSize = 22.sp)
    Spacer(modifier = Modifier.height(16.dp))

    OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth(), colors = campoColors)
    OutlinedTextField(value = telefono, onValueChange = { telefono = it }, label = { Text("Teléfono") }, modifier = Modifier.fillMaxWidth(), colors = campoColors)
    OutlinedTextField(value = direccion, onValueChange = { direccion = it }, label = { Text("Dirección") }, modifier = Modifier.fillMaxWidth(), colors = campoColors)
    OutlinedTextField(value = username, onValueChange = { username = it }, label = { Text("Usuario") }, modifier = Modifier.fillMaxWidth(), colors = campoColors)
    OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("Contraseña") }, modifier = Modifier.fillMaxWidth(), colors = campoColors)

    Spacer(modifier = Modifier.height(20.dp))
    ButtonElegante("Guardar") {
        if (nombre.isNotBlank() && telefono.isNotBlank() && direccion.isNotBlank() && username.isNotBlank() && password.isNotBlank()) {
            onGuardar(nombre, telefono, direccion, username, password)
        }
    }
}

@Composable
fun FormularioAdmin(
    campoColors: TextFieldColors,
    onGuardar: (String, String, String, String) -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var cargo by remember { mutableStateOf("") }

    Text("\uD83D\uDCBC Nuevo Administrador", color = Color.White, fontSize = 22.sp)
    Spacer(modifier = Modifier.height(16.dp))

    OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth(), colors = campoColors)
    OutlinedTextField(value = username, onValueChange = { username = it }, label = { Text("Usuario") }, modifier = Modifier.fillMaxWidth(), colors = campoColors)
    OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("Contraseña") }, modifier = Modifier.fillMaxWidth(), colors = campoColors)
    OutlinedTextField(value = cargo, onValueChange = { cargo = it }, label = { Text("Cargo") }, modifier = Modifier.fillMaxWidth(), colors = campoColors)

    Spacer(modifier = Modifier.height(20.dp))
    ButtonElegante("Guardar") {
        if (nombre.isNotBlank() && username.isNotBlank() && password.isNotBlank() && cargo.isNotBlank()) {
            onGuardar(nombre, username, password, cargo)
        }
    }
}
