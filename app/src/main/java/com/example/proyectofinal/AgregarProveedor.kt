@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.proyectofinal

import android.R.attr.id
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.proyectofinal.Model.Proveedor

@Composable
fun AgregarProveedorScreen(navController: NavHostController) {
    var nombre by remember { mutableStateOf("") }
    var contacto by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Agregar Proveedor", style = MaterialTheme.typography.headlineSmall)

        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = contacto,
            onValueChange = { contacto = it },
            label = { Text("Contacto") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            val nuevoProveedor = Proveedor(nombre = nombre, id = id, telefono = telefono, direccion = direccion)
            viewModelScope.launch {
                try {
                    ApiClient.apiService.agregarProveedor(nuevoProveedor)
                    navController.popBackStack()
                } catch (e: Exception) {
                }
            }
        }) {
            Text("Guardar")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = { navController.popBackStack() }) {
            Text("Volver")
        }
    }
}
