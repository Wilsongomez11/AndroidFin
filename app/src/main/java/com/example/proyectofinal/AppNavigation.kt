package com.example.proyectofinal


import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.proyectofinal.Model.Administrador
import com.example.proyectofinal.admin.AdminHomeScreen
import com.example.proyectofinal.admin.AdministradoresScreen
import com.example.proyectofinal.login.LoginScreen


@SuppressLint("ComposableDestinationInComposeScope")
@Composable
fun AppNavigation(navController: NavHostController) {
    val adminProfileState = remember { mutableStateOf<Administrador?>(null) }

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen { adminProfile ->
                adminProfileState.value = adminProfile
                navController.navigate("adminHome")
            }
        }

        composable("adminHome") {
            adminProfileState.value?.let { profile ->
                AdminHomeScreen(navController, profile)
            }
        }

        composable("verPersonal") {
            AdministradoresScreen(navController)
        }

        composable("agregarProducto") {
            AgregarProductoScreen(navController)
        }
        composable("inventario") {
            InventarioScreen(navController)
        }
        composable("agregarMesero") {
            AgregarMeseroScreen(navController, viewModelScope)
        }
        composable("agregarProveedor") {
            AgregarProveedorScreen(navController)
        }

    }
}

