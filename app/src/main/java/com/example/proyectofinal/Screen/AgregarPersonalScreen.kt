@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.proyectofinal

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.proyectofinal.ViewModel.AppBackground
import com.example.proyectofinal.ViewModel.AgregarPersonalViewModel

@Composable
fun AgregarPersonalScreen(
    navController: NavHostController,
    viewModel: AgregarPersonalViewModel = viewModel(),
    it: String
) {
    var formularioActivo by remember { mutableStateOf("") }
    var mensaje by remember { mutableStateOf("") }

    val campoColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = Color.White,
        unfocusedBorderColor = Color.LightGray,
        focusedTextColor = Color.White,
        unfocusedTextColor = Color.White,
        focusedLabelColor = Color.White,
        unfocusedLabelColor = Color.LightGray,
        cursorColor = Color.White
    )

    AppBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Agregar Personal", color = Color.White, style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(24.dp))

            when (formularioActivo) {
                "" -> {
                    Button(
                        onClick = { formularioActivo = "Mesero" },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF5722))
                    ) {
                        Text("Agregar Mesero", color = Color.White)
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = { formularioActivo = "Pizzero" },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF5722))
                    ) {
                        Text("Agregar Pizzero", color = Color.White)
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = { formularioActivo = "Admin" },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF5722))
                    ) {
                        Text("Agregar Admin", color = Color.White)
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                    ) {
                        Text("Volver", color = Color.White)
                    }
                }

                "Mesero" -> {
                    var nombre by remember { mutableStateOf("") }
                    var telefono by remember { mutableStateOf("") }
                    var email by remember { mutableStateOf("") }

                    OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth(), colors = campoColors)
                    OutlinedTextField(value = telefono, onValueChange = { telefono = it }, label = { Text("Teléfono") }, modifier = Modifier.fillMaxWidth(), colors = campoColors)
                    OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Correo") }, modifier = Modifier.fillMaxWidth(), colors = campoColors)

                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            if (nombre.isNotBlank() && telefono.isNotBlank() && email.isNotBlank()) {
                                viewModel.guardarMesero(nombre, telefono, email) { mensaje = it }
                            } else {
                                mensaje = "Completa todos los campos"
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF5722))
                    ) {
                        Text("Guardar Mesero", color = Color.White)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = { formularioActivo = "" },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                    ) {
                        Text("Volver", color = Color.White)
                    }
                }

                "Pizzero" -> {
                    var nombre by remember { mutableStateOf("") }
                    var telefono by remember { mutableStateOf("") }
                    var direccion by remember { mutableStateOf("") }

                    OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth(), colors = campoColors)
                    OutlinedTextField(value = telefono, onValueChange = { telefono = it }, label = { Text("Teléfono") }, modifier = Modifier.fillMaxWidth(), colors = campoColors)
                    OutlinedTextField(value = direccion, onValueChange = { direccion = it }, label = { Text("Dirección") }, modifier = Modifier.fillMaxWidth(), colors = campoColors)

                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            if (nombre.isNotBlank() && telefono.isNotBlank() && direccion.isNotBlank()) {
                                viewModel.guardarPizzero(nombre, telefono, direccion) { mensaje = it }
                            } else {
                                mensaje = "Completa todos los campos"
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF5722))
                    ) {
                        Text("Guardar Pizzero", color = Color.White)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = { formularioActivo = "" },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                    ) {
                        Text("Volver", color = Color.White)
                    }
                }

                "Admin" -> {
                    var nombre by remember { mutableStateOf("") }
                    var username by remember { mutableStateOf("") }
                    var password by remember { mutableStateOf("") }
                    var cargo by remember { mutableStateOf("") }

                    OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth(), colors = campoColors)
                    OutlinedTextField(value = username, onValueChange = { username = it }, label = { Text("Username") }, modifier = Modifier.fillMaxWidth(), colors = campoColors)
                    OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("Password") }, modifier = Modifier.fillMaxWidth(), colors = campoColors)
                    OutlinedTextField(value = cargo, onValueChange = { cargo = it }, label = { Text("Cargo") }, modifier = Modifier.fillMaxWidth(), colors = campoColors)

                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            if (nombre.isNotBlank() && username.isNotBlank() && password.isNotBlank() && cargo.isNotBlank()) {
                                viewModel.guardarAdministrador(nombre, username, password, cargo) { mensaje = it }
                            } else {
                                mensaje = "Completa todos los campos"
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF5722))
                    ) {
                        Text("Guardar Admin", color = Color.White)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = {
                            if (!navController.popBackStack()) {
                                navController.navigate("admin")
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                    ) {
                        Text("Volver", color = Color.White)
                    }

                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (mensaje.isNotBlank()) {
                Text(mensaje, color = Color.White)
            }
        }
    }
}
