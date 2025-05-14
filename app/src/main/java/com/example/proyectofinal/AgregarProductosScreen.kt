package com.example.proyectofinal

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController


@Composable
fun AgregarProductoScreen(
    navController: NavHostController,
    viewModel: ProductoViewModel = viewModel()) {
    var nombre by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var cantidad by remember { mutableStateOf("") }
    var idProveedor by remember { mutableStateOf("") }
    var idAdministrador by remember { mutableStateOf("") }
    var mensaje by remember { mutableStateOf("") }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        Text("Agregar Producto", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre") })
        OutlinedTextField(value = precio, onValueChange = { precio = it }, label = { Text("Precio") })
        OutlinedTextField(value = cantidad, onValueChange = { cantidad = it }, label = { Text("Cantidad") })
        OutlinedTextField(value = idProveedor, onValueChange = { idProveedor = it }, label = { Text("ID Proveedor") })
        OutlinedTextField(value = idAdministrador, onValueChange = { idAdministrador = it }, label = { Text("ID Administrador") })

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            viewModel.agregarProducto(
                nombre,
                precio.toDoubleOrNull() ?: 0.0,
                cantidad.toIntOrNull() ?: 0,
                idProveedor.toIntOrNull() ?: 0,
                idAdministrador.toIntOrNull() ?: 0,
                onResult = { resultado -> mensaje = resultado }
            )
        }) {
            Text("Guardar Producto")
        }

        if (mensaje.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(mensaje, color = MaterialTheme.colorScheme.primary)
        }
        Button(onClick = {
            navController.popBackStack()
        }) {
            Text("Volver")
        }
    }
}
