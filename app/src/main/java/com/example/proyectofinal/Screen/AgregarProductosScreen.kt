package com.example.proyectofinal.Screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import com.example.proyectofinal.Model.Producto
import com.example.proyectofinal.ProductoViewModel



@Composable
    fun AgregarProductoScreen(navController: NavHostController,
                          viewModel: ProductoViewModel) {
    var nombre by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var cantidad by remember { mutableStateOf("") }
    var idProveedor by remember { mutableStateOf("") }
    var idAdministrador by remember { mutableStateOf("") }
    var mensaje by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Agregar Producto", style = MaterialTheme.typography.headlineSmall)


        TextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre del producto") })
        TextField(value = precio, onValueChange = { precio = it }, label = { Text("Precio") })
        TextField(value = cantidad, onValueChange = { cantidad = it }, label = { Text("Cantidad") })
        TextField(value = idProveedor, onValueChange = { idProveedor = it }, label = { Text("ID Proveedor") })
        TextField(value = idAdministrador, onValueChange = { idAdministrador = it }, label = { Text("ID Administrador") })

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            val producto = Producto(
                id = 0,
                nombre = nombre,
                precio = precio.toDoubleOrNull() ?: 0.0,
                cantidad = cantidad.toIntOrNull() ?: 0,
                idProveedor = idProveedor.toIntOrNull() ?: 0,
                idAdministrador = idAdministrador.toIntOrNull() ?: 0
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


        }) {
            Text("Agregar Producto")
        }
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { navController.popBackStack() }) {
            Text("Volver")
        }

        if (mensaje.isNotEmpty()) {
            Text(mensaje)
        }
    }
}
