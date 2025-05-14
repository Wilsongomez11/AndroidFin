package com.example.proyectofinal

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun InventarioScreen(navController: NavHostController) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Inventario de Productos", style = MaterialTheme.typography.headlineSmall)

        // Aquí agregarías una LazyColumn para mostrar productos, por ahora solo texto:
        Spacer(Modifier.height(16.dp))
        Text("Aquí se mostrará el listado de productos...")

        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            navController.popBackStack("adminHome", inclusive = false)
        }) {
            Text("Volver")
        }
    }
}
