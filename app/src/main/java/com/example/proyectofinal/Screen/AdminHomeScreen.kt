package com.example.proyectofinal.Screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel

import androidx.navigation.NavHostController
import com.example.proyectofinal.Model.Administrador
import com.example.proyectofinal.ViewModel.AdministradorViewModel
import com.example.proyectofinal.ViewModel.AppBackground

@Composable
fun AdminHomeScreen(
    navController: NavHostController,
    viewModel: AdministradorViewModel = viewModel(),
    onBack: () -> Unit = {}
) {

    AppBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Bienvenido Administrador",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(24.dp))

            val buttonColors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF5722))

            Button(
                onClick = { navController.navigate("agregarProducto") },
                modifier = Modifier.fillMaxWidth(),
                colors = buttonColors
            ) {
                Text("Agregar Producto", color = Color.White)
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = { navController.navigate("verPersonal") },
                modifier = Modifier.fillMaxWidth(),
                colors = buttonColors
            ) {
                Text("Ver Personal", color = Color.White)
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = { navController.navigate("inventario") },
                modifier = Modifier.fillMaxWidth(),
                colors = buttonColors
            ) {
                Text("Ver Inventario", color = Color.White)
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = { navController.navigate("agregarMesero") },
                modifier = Modifier.fillMaxWidth(),
                colors = buttonColors
            ) {
                Text("Agregar Mesero", color = Color.White)
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = { navController.navigate("agregarProveedor") },
                modifier = Modifier.fillMaxWidth(),
                colors = buttonColors
            ) {
                Text("Agregar Proveedor", color = Color.White)
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
    }
}
