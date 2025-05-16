package com.example.proyectofinal.Navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.proyectofinal.AgregarMeseroScreen
import com.example.proyectofinal.Screen.AdminHomeScreen
import com.example.proyectofinal.Screen.AgregarProductoScreen
import com.example.proyectofinal.Screen.InventarioScreen

import com.example.proyectofinal.admin.AdministradoresScreen
import com.example.proyectofinal.login.LoginScreen

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "login") {

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

        composable("admin") {
            AdminHomeScreen(
                navController,
                admin = TODO(),
                administrador = TODO(),
                onBack = TODO()
            )
        }

        composable("agregarProducto") {
            AgregarProductoScreen(navController)
        }

        composable("inventario") {
            InventarioScreen(navController)
        }

        composable("agregarMesero") {
            AgregarMeseroScreen(
                navController,
                viewModelScope = TODO()
            )
        }

        composable("agregarProveedor") {
            AgregarProveedorScreen(navController)
        }

        composable("verPersonal") {
            AdministradoresScreen(navController)
        }
    }
}

@Composable
fun AgregarProveedorScreen(x0: NavHostController) {
    TODO("Not yet implemented")
}

