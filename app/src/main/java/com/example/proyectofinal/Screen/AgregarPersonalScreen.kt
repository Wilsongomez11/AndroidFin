@file:OptIn(ExperimentalMaterial3Api::class)
package com.example.proyectofinal

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.proyectofinal.ViewModel.AgregarPersonalViewModel

@Composable
fun AgregarPersonalScreen(
    navController: NavHostController,
    viewModel: AgregarPersonalViewModel = viewModel()
) {
    var formularioActivo by remember { mutableStateOf("") }
    var mensaje by remember { mutableStateOf("") }

    val campoColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = Color(0xFFFF9800),
        unfocusedBorderColor = Color.Gray,
        focusedTextColor = Color.White,
        unfocusedTextColor = Color.White,
        focusedLabelColor = Color(0xFFFF9800),
        unfocusedLabelColor = Color.LightGray,
        cursorColor = Color(0xFFFF9800)
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
                "👥 Agregar Personal",
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
                    Spacer(modifier = Modifier.height(40.dp))
                    ButtonVolver {
                        navController.navigate("personalMenu") {
                            popUpTo("personalMenu") { inclusive = true }
                        }
                    }
                }

                "Mesero" -> {
                    FormularioMesero(
                        campoColors = campoColors,
                        onGuardar = { nombre, telefono, correo, username, password ->
                            viewModel.guardarMesero(nombre, telefono, correo, username, password) {
                                mensaje = it
                            }
                        },
                        onVolver = { formularioActivo = "" }
                    )
                }

                "Pizzero" -> {
                    FormularioPizzero(
                        campoColors = campoColors,
                        onGuardar = { nombre, telefono, direccion, username, password ->
                            viewModel.guardarPizzero(nombre, telefono, direccion, username, password) {
                                mensaje = it
                            }
                        },
                        onVolver = { formularioActivo = "" }
                    )
                }

                "Admin" -> {
                    FormularioAdmin(
                        campoColors = campoColors,
                        onGuardar = { nombre, username, password, cargo ->
                            viewModel.guardarAdministrador(nombre, username, password, cargo) {
                                mensaje = it
                            }
                        },
                        onVolver = { formularioActivo = "" }
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
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1C1B1F))
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            val icono = when {
                texto.contains("Mesero", ignoreCase = true) -> "🍽️"
                texto.contains("Pizzero", ignoreCase = true) -> "🍕"
                texto.contains("Admin", ignoreCase = true) -> "🧑‍💼"
                texto.contains("Volver", ignoreCase = true) -> "↩️"
                else -> "➕"
            }
            Text(icono, fontSize = 28.sp)
            Spacer(modifier = Modifier.height(6.dp))
            Text(texto, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
fun ButtonVolver(onClick: () -> Unit = {}) {
    val context = LocalContext.current

    Button(
        onClick = {
            try {
                onClick()
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(context, "No se pudo volver", Toast.LENGTH_SHORT).show()
            }
        },
        modifier = Modifier
            .padding(8.dp)
            .size(120.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1C1B1F))
    ) {
        Spacer(Modifier.height(20.dp))

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("↩️", fontSize = 28.sp)
            Spacer(modifier = Modifier.height(6.dp))
            Text("Volver", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
fun FormularioMesero(
    campoColors: TextFieldColors,
    onGuardar: (String, String, String, String, String) -> Unit,
    onVolver: () -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Text("🍽️ Nuevo Mesero", color = Color.White, fontSize = 22.sp)
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
    Spacer(modifier = Modifier.height(10.dp))
    ButtonVolver(onVolver)
}

@Composable
fun FormularioPizzero(
    campoColors: TextFieldColors,
    onGuardar: (String, String, String, String, String) -> Unit,
    onVolver: () -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Text("🍕 Nuevo Pizzero", color = Color.White, fontSize = 22.sp)
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
    Spacer(modifier = Modifier.height(10.dp))
    ButtonVolver(onVolver)
}

@Composable
fun FormularioAdmin(
    campoColors: TextFieldColors,
    onGuardar: (String, String, String, String) -> Unit,
    onVolver: () -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var cargo by remember { mutableStateOf("") }

    Text(" Nuevo Administrador", color = Color.White, fontSize = 22.sp)
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
    Spacer(modifier = Modifier.height(10.dp))
    ButtonVolver(onVolver)
}
@Composable
fun BotonCuadro(
    texto: String,
    icono: androidx.compose.ui.graphics.vector.ImageVector,
    colorFondo: Color = Color(0xFF1E1E1E),
    onClick: () -> Unit
) {
    ElevatedButton(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth(0.85f)
            .height(70.dp),
        shape = RoundedCornerShape(18.dp),
        colors = ButtonDefaults.elevatedButtonColors(containerColor = colorFondo),
        elevation = ButtonDefaults.elevatedButtonElevation(8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icono,
                contentDescription = texto,
                tint = Color(0xFFFF9800),
                modifier = Modifier.size(30.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                texto,
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

