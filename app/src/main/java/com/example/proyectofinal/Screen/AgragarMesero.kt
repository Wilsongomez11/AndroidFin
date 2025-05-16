@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.proyectofinal

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.proyectofinal.ViewModel.MeseroViewModel

@Composable
fun AgregarMeseroScreen(navController: NavHostController, viewModelScope: Nothing?){
    // Estado para los campos de texto
    var nombre = remember { "" }
    var correo = remember { "" }
    var telefono = remember { "" }
    var mensaje = remember { "" }

    // ViewModel para la lógica de negocio
    val viewModel: MeseroViewModel = viewModel()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Título
        Text("Agregar Nuevo Mesero", style = MaterialTheme.typography.headlineSmall)

        // Campos de texto para ingresar los datos
        TextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = correo,
            onValueChange = { correo = it },
            label = { Text("Correo") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = telefono,
            onValueChange = { telefono = it },
            label = { Text("Teléfono") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Botón para agregar el mesero
        Button(
            onClick = {
                // Llamada al ViewModel para agregar el mesero
                viewModel.agregarMesero(nombre, correo, telefono) { resultado ->
                    mensaje = resultado
                    Toast.makeText(navController.context, mensaje, Toast.LENGTH_SHORT).show()
                    if (resultado.contains("correctamente")) {
                        // Redirigir si el mesero fue agregado con éxito
                        navController.popBackStack()
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Registrar Mesero")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Mostrar mensaje de resultado
        Text(mensaje, style = MaterialTheme.typography.bodyMedium)
    }
}

annotation class
viewModelScope

@Preview(showBackground = true)
@Composable
fun AgregarMeseroPreview() {
    val viewModelScope = null
    AgregarMeseroScreen(navController = rememberNavController(), viewModelScope =
        viewModelScope)
}
