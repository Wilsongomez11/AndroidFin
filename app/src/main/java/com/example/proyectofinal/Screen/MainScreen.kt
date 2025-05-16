package com.example.proyectofinal.Screen

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.proyectofinal.ViewModel.MainViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavHostController,
    viewModel: MainViewModel) {
    val administradores by viewModel.administradores.collectAsState()
    val error by viewModel.error.collectAsState()

    Spacer(modifier = Modifier.height(16.dp))

    Button(onClick = { navController.popBackStack() }) {
        Text("Volver")
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Lista de Administradores") })
        }
    ) { paddingValues ->
        if (error != null) {
            Text(
                text = error!!,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(paddingValues)
            )
        } else {
            LazyColumn(modifier = Modifier.padding(paddingValues)) {
                items(administradores) { admin ->
                    Text(text = "${admin.nombre} ")
                }
            }
        }
    }
}

