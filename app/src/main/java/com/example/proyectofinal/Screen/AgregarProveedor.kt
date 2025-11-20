package com.example.proyectofinal.Screen

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.proyectofinal.ViewModel.AppBackground
import com.example.proyectofinal.ViewModel.ProveedorViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgregarProveedor(
    navController: NavHostController,
    proveedorId: String? = null,
    viewModel: ProveedorViewModel = viewModel()
) {
    var nombre by remember { mutableStateOf("") }
    var contacto by remember { mutableStateOf("") }
    val context = LocalContext.current

    AppBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "\uD83D\uDC65 Agregar Proveedor",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre del Proveedor") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.LightGray,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.LightGray,
                    cursorColor = Color.White,
                    disabledBorderColor = Color.Gray
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = contacto,
                onValueChange = { contacto = it },
                label = { Text("Contacto del Proveedor") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.LightGray,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.LightGray,
                    cursorColor = Color.White,
                    disabledBorderColor = Color.Gray
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (nombre.isNotBlank() && contacto.isNotBlank()) {
                        viewModel.agregarProveedor(nombre, contacto) {
                            Toast.makeText(context, "Proveedor agregado correctamente", Toast.LENGTH_SHORT).show()
                            navController.popBackStack()
                        }
                    } else {
                        Toast.makeText(context, "Complete todos los campos", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF5722))
            ) {
                Text("\uD83D\uDCBE Guardar", color = Color.White)
            }
        }
    }
}
