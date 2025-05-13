package com.example.proyectofinal


import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.proyectofinal.admin.AdminHomeScreen
import com.example.proyectofinal.admin.AdministradoresScreen
import com.example.proyectofinal.login.LoginScreen
import com.example.proyectofinal.login.RegisterScreen

@SuppressLint("ComposableDestinationInComposeScope")
@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login") {
            LoginScreen(
                onLoginSuccess = { rol ->
                    if (rol == "administrador") {
                        navController.navigate("adminHome")
                    }
                },
                onNavigateToRegister = {
                    navController.navigate("register")
                }
            )
        }

        composable("register") {
            RegisterScreen(onRegisterSuccess = {
                navController.popBackStack() // Vuelve al login al registrarse
            })
        }

        composable("adminHome") {
            AdminHomeScreen(navController)
        }

        composable("verPersonal") {
            AdministradoresScreen()
        }

        composable("agregarProducto") {
            AgregarProductoScreen()
        }
    }
}
