package com.example.proyectofinal

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.proyectofinal.Screen.AdminHomeScreen
import com.example.proyectofinal.Screen.AgregarProductoScreen
import com.example.proyectofinal.Screen.InventarioScreen
import com.example.proyectofinal.ViewModel.MeseroViewModel
import com.example.proyectofinal.admin.AdministradoresScreen
import com.example.proyectofinal.login.LoginScreen


@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "login") {

        // Pantalla de Login
        composable("login") {
            LoginScreen(
                navController = navController,
                onLoginSuccess = {
                    navController.navigate("admin") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }
        // Pantalla principal del administrador
        composable("admin") {
            AdminHomeScreen(navController = navController)
        }

        // Pantalla para agregar productos
        composable("agregarProducto") {
            AgregarProductoScreen(navController = navController)
        }

        // Pantalla de inventario
        composable("inventario") {
            InventarioScreen(navController = navController)
        }

        // Pantalla para agregar meseros
        composable("agregarMesero") {
            val viewModel: MeseroViewModel = viewModel()
            AgregarMeseroScreen(
                navController = navController,
                viewModel = viewModel
            )
        }

        composable("agregarProducto") {
            val viewModel: ProductoViewModel = viewModel()
            AgregarProductoScreen(
                navController = navController,
                viewModel = viewModel
            )
        }

        composable("verPersonal") {
            AdministradoresScreen(navController = navController)
        }
    }
}
