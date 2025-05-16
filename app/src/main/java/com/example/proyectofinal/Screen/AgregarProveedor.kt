
package com.example.proyectofinal.Screen

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.proyectofinal.ProductoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgregarProductoScreen(navController: NavHostController) {
    val viewModel: ProductoViewModel = viewModel()
    val context = LocalContext.current

    var nombre by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var cantidad by remember { mutableStateOf("") }
    var idProveedor by remember { mutableStateOf("") }
    var idAdministrador by remember { mutableStateOf("") }
    var mensaje by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Agregar Producto", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre del Producto") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = precio,
            onValueChange = { precio = it },
            label = { Text("Precio") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = cantidad,
            onValueChange = { cantidad = it },
            label = { Text("Cantidad") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = idProveedor,
            onValueChange = { idProveedor = it },
            label = { Text("ID del Proveedor") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = idAdministrador,
            onValueChange = { idAdministrador = it },
            label = { Text("ID del Administrador") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                viewModel.agregarProducto(
                    nombre,
                    precio.toDoubleOrNull() ?: 0.0,
                    cantidad.toIntOrNull() ?: 0,
                    idProveedor.toIntOrNull() ?: 0,
                    idAdministrador.toIntOrNull() ?: 0,
                    onResult = { resultado ->
                        mensaje = resultado
                    }
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Agregar")
        }

        mensaje?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            mensaje = null
        }
    }
}
