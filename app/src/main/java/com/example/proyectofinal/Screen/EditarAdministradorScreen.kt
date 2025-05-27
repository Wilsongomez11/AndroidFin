package com.example.proyectofinal.Screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import com.example.proyectofinal.ViewModel.AdministradorViewModel
import com.example.proyectofinal.ViewModel.AppBackground

@Composable
fun EditarAdministradorScreen(
    navController: NavHostController,
    administradorId: Long,
    viewModel: AdministradorViewModel = viewModel()
) {
    val administrador = viewModel.administradores.collectAsState().value.find { it.id?.toLong() == administradorId }

    var username by remember { mutableStateOf("") }
    var nombre by remember { mutableStateOf("") }
    var cargo by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }


    var initialized by remember { mutableStateOf(false) }

    LaunchedEffect(administrador) {
        if (administrador != null && !initialized) {
            username = administrador.username ?: ""
            nombre = administrador.nombre ?: ""
            cargo = administrador.cargo ?: ""
            password = administrador.password ?: ""
            initialized = true
        }
    }


    AppBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Editar Administrador", color = Color.White, style = MaterialTheme.typography.h4)

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre", color = Color.LightGray) },
                modifier = Modifier.fillMaxWidth(),
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

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = cargo,
                onValueChange = { cargo = it },
                label = { Text("Cargo", color = Color.LightGray) },
                modifier = Modifier.fillMaxWidth(),
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

            Spacer(modifier = Modifier.height(8.dp))


            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password", color = Color.LightGray) },
                modifier = Modifier.fillMaxWidth(),
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

            Spacer(modifier = Modifier.height(8.dp))


            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("username", color = Color.LightGray) },
                modifier = Modifier.fillMaxWidth(),
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


            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                println("Guardando: $nombre, $cargo, $password, $username")
                viewModel.editarAdministrador(administradorId, nombre, cargo, password, username)
                navController.popBackStack()
            }) {
                Text("Guardar cambios")
            }


        }
    }
}
