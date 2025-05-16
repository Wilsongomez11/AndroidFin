package com.example.proyectofinal.Screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.lifecycle.viewmodel.compose.viewModel

import androidx.navigation.NavHostController
import com.example.proyectofinal.Model.Administrador
import com.example.proyectofinal.ViewModel.AdministradorViewModel

@Composable
fun AdminHomeScreen(
    navController: NavHostController,
    viewModel: AdministradorViewModel = viewModel(),
    onBack: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Bienvenido Administrador", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = { navController.navigate("agregarProducto") }) {
            Text("Agregar Producto")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = { navController.navigate("verPersonal") }) {
            Text("Ver Personal")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = { navController.navigate("inventario") }) {
            Text("Ver Inventario")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = { navController.navigate("agregarMesero") }) {
            Text("Agregar Mesero")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = { navController.navigate("agregarProveedor") }) {
            Text("Agregar Proveedor")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { navController.popBackStack() }) {
            Text("Volver")
        }
    }
}
