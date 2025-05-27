package com.example.proyectofinal

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.proyectofinal.Screen.AdminHomeScreen
import com.example.proyectofinal.Screen.AgregarProductoScreen
import com.example.proyectofinal.Screen.AgregarProveedor
import com.example.proyectofinal.Screen.EditarAdministradorScreen
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
            AdminHomeScreen(navController = navController)
        }


        composable("inventario") {
            InventarioScreen(navController = navController)
        }

        composable("AgregarPersonal") {
            AgregarPersonalScreen(navController = navController, it = it.toString())
        }


        composable("agregarProducto") {
            val viewModel: ProductoViewModel = viewModel()
            AgregarProductoScreen(
                navController = navController,
                viewModel = viewModel
            )
        }
        composable("editarAdministrador/{id}", arguments = listOf(navArgument("id") { type = NavType.LongType })) {
            val id = it.arguments?.getLong("id") ?: -1
            EditarAdministradorScreen(navController, id)
        }

        composable("verPersonal") {
            AdministradoresScreen(navController = navController)
        }

        composable(
            route = "agregarProveedor?id={id}",
            arguments = listOf(
                navArgument("id") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                }
            )
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")
            AgregarProveedor(navController = navController, proveedorId = id)
        }
    }
}
