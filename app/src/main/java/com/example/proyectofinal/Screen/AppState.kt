package com.example.proyectofinal.Navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@Stable
class AppState(
    val navController: NavHostController
) {

    fun navigate(route: String) {
        try {
            navController.navigate(route) {
                launchSingleTop = true
                restoreState = true
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun popBackStack() {
        navController.popBackStack()
    }
}

@Composable
fun rememberAppState(
    navController: NavHostController = rememberNavController()
): AppState {
    return remember(navController) {
        AppState(navController)
    }
}