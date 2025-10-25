package com.example.proyectofinal.Screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScaffold(
    navController: NavHostController,
    content: @Composable (PaddingValues) -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(navController = navController) {
                scope.launch { drawerState.close() }
            }
        }
    ) {
        Scaffold(
            topBar = {
                // Barra superior con fondo transparente
                TopAppBar(
                    title = {
                        Text(
                            text = "Panel Administrador",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch {
                                if (drawerState.isClosed) drawerState.open() else drawerState.close()
                            }
                        }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menú", tint = Color.White)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent, // ✅ quita el fondo negro
                        scrolledContainerColor = Color.Transparent
                    )
                )
            },
            containerColor = Color.Transparent, // ✅ hace que el Scaffold sea transparente
            content = content
        )
    }
}

@Composable
fun DrawerContent(
    navController: NavHostController,
    onItemClicked: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .background(Color(0xFF1E1E1E))
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Menú Principal",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(20.dp))

        DrawerItem("Inicio", "admin", navController, onItemClicked)
        DrawerItem("Inventario", "inventario", navController, onItemClicked)
        DrawerItem("Pedidos", "pedidos", navController, onItemClicked)
        DrawerItem("Personal", "personalMenu", navController, onItemClicked)
        DrawerItem("Cerrar Sesión", "login", navController, onItemClicked)
    }
}

@Composable
fun DrawerItem(
    label: String,
    route: String,
    navController: NavHostController,
    onItemClicked: () -> Unit
) {
    TextButton(
        onClick = {
            navController.navigate(route) {
                popUpTo("login") { inclusive = false }
                launchSingleTop = true
            }
            onItemClicked()
        },
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(label, color = Color.White, fontSize = 16.sp)
    }
}
