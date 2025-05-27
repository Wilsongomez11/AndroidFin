package com.example.proyectofinal.Screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.navigation.NavHostController
import com.example.proyectofinal.Model.Producto
import com.example.proyectofinal.ProductoViewModel
import com.example.proyectofinal.ViewModel.AppBackground


@Composable
    fun AgregarProductoScreen(navController: NavHostController,
                          viewModel: ProductoViewModel) {
    var nombre by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var cantidad by remember { mutableStateOf("") }
    var idProveedor by remember { mutableStateOf("") }
    var idAdministrador by remember { mutableStateOf("") }
    var mensaje by remember { mutableStateOf("") }

    AppBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Agregar Producto",
                color = Color.White,
                style = MaterialTheme.typography.headlineMedium
            )

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
                    cursorColor = Color.White,
                    disabledBorderColor = Color.Gray,
                    errorBorderColor = Color.Red
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = cantidad,
                onValueChange = { cantidad = it },
                label = { Text("Cantidad", color = Color.LightGray) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.LightGray,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.LightGray,
                    cursorColor = Color.White,
                    disabledBorderColor = Color.Gray,
                    errorBorderColor = Color.Red
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = precio,
                onValueChange = { precio = it },
                label = { Text("Precio", color = Color.LightGray) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.LightGray,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.LightGray,
                    cursorColor = Color.White,
                    disabledBorderColor = Color.Gray,
                    errorBorderColor = Color.Red
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = idProveedor,
                onValueChange = { idProveedor = it },
                label = { Text("ID Proveedor", color = Color.LightGray) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.LightGray,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.LightGray,
                    cursorColor = Color.White,
                    disabledBorderColor = Color.Gray,
                    errorBorderColor = Color.Red
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = idAdministrador,
                onValueChange = { idAdministrador = it },
                label = { Text("ID Administrador", color = Color.LightGray) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.LightGray,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.LightGray,
                    cursorColor = Color.White,
                    disabledBorderColor = Color.Gray,
                    errorBorderColor = Color.Red
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                val producto = Producto(
                    id = 0,
                    nombre = nombre,
                    precio = precio.toDoubleOrNull() ?: 0.0,
                    cantidad = cantidad.toIntOrNull() ?: 0,
                    idProveedor = (idProveedor.toIntOrNull() ?: 0).toLong(),
                    idAdministrador = (idAdministrador.toIntOrNull() ?: 0).toLong()
                )

                viewModel.agregarProducto(
                    nombre,
                    precio.toDoubleOrNull() ?: 0.0,
                    cantidad.toIntOrNull() ?: 0,
                    idProveedor.toIntOrNull() ?: 0,
                    idAdministrador.toIntOrNull() ?: 0
                ) { resultado ->
                    mensaje = resultado
                }


            },modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFF5722)
                )
            ) {
                Text("Agregar Producto", color = Color.White)
            }
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { navController.popBackStack()
                          },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFB2FF22)
                )
                ) {

                Text("Volver", color = Color.White)
            }

            if (mensaje.isNotEmpty()) {
                Text(mensaje)
                }
            }
        }
    }
