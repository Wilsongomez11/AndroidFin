package com.example.proyectofinal

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.proyectofinal.Api.AdministradorService
import com.example.proyectofinal.Screen.*
import com.example.proyectofinal.ViewModel.AdministradorViewModel
import com.example.proyectofinal.ViewModel.AppBackground
import com.example.proyectofinal.admin.VerPersonalScreen
import com.example.proyectofinal.login.LoginScreen
import com.example.proyectofinal.Model.Producto
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import com.example.proyectofinal.ViewModel.PedidoViewModel
import com.example.proyectofinal.Screen.DetallePedidoScreen


@Composable
fun AppNavigation(navController: NavHostController, adminService: AdministradorService) {

    val adminViewModel = remember { AdministradorViewModel() }

    NavHost(navController, startDestination = "login") {

        // LOGIN
        composable("login") {
            LoginScreen(
                navController = navController,
                onLoginSuccess = { user ->
                    when (user.cargo.uppercase()) {
                        "ADMIN", "ADMINISTRADOR" -> navController.navigate("admin") {
                            popUpTo("login") { inclusive = true }
                        }
                        "MESERO" -> navController.navigate("mesero") {
                            popUpTo("login") { inclusive = true }
                        }
                        "PIZZERO" -> navController.navigate("pizzero") {
                            popUpTo("login") { inclusive = true }
                        }
                        else -> navController.navigate("login") {
                            popUpTo("login") { inclusive = true }
                        }
                    }
                },
                adminService = adminService,
                adminViewModel = adminViewModel
            )
        }

        // ADMIN
        composable("admin") {
            AdminHomeScreen(navController = navController, adminViewModel = adminViewModel)
        }

        // PERSONAL
        composable("personalMenu") {
            PersonalMenuScreen(navController = navController)
        }

        composable("agregarPersonal") {
            AgregarPersonalScreen(navController = navController)
        }

        composable("verPersonal") {
            VerPersonalScreen(navController = navController)
        }

        // MESERO / PIZZERO
        composable("mesero") { MeseroHomeScreen(navController = navController) }
        composable("pizzero") { PizzeroHomeScreen(navController = navController) }

        // INVENTARIO
        composable("inventario") { InventarioScreen(navController = navController) }

        // AGREGAR PRODUCTO
        composable("agregarProducto") {
            val productoViewModel: ProductoViewModel = viewModel()
            AgregarProductoScreen(
                navController = navController,
                viewModel = productoViewModel,
                adminViewModel = adminViewModel
            )
        }

        // VER INVENTARIO
        composable("verInventario") {
            val productoViewModel: ProductoViewModel = viewModel()
            VerProductoScreen(navController = navController, viewModel = productoViewModel)
        }

        // Pantalla de Pedidos
        composable("pedidos") {
            val pedidoViewModel: PedidoViewModel = viewModel()
            PedidosScreen(navController = navController, pedidoViewModel = pedidoViewModel)
        }

        composable("crearPedido") {
            CrearPedidoScreen(navController = navController)
        }

        // INSUMOS
        composable(
            route = "insumos?returnTo={returnTo}",
            arguments = listOf(navArgument("returnTo") { defaultValue = "inventario" })
        ) {
            val returnTo = it.arguments?.getString("returnTo") ?: "inventario"
            AppBackground { InsumosScreen(navController, returnTo) }
        }

        composable(
            route = "verInsumos?returnTo={returnTo}",
            arguments = listOf(navArgument("returnTo") { defaultValue = "inventario" })
        ) {
            val returnTo = it.arguments?.getString("returnTo") ?: "inventario"
            AppBackground { VerInsumosScreen(navController, returnTo) }
        }

        // ✅ VER RECETA (corregido)
        composable(
            route = "verReceta/{productoId}",
            arguments = listOf(navArgument("productoId") { type = NavType.LongType })
        ) { backStackEntry ->
            val productoId = backStackEntry.arguments?.getLong("productoId") ?: 0L
            val productoViewModel: ProductoViewModel = viewModel()
            val productos by productoViewModel.productos.collectAsState()

            // Cargar productos si aún no se han obtenido
            LaunchedEffect(Unit) {
                if (productos.isEmpty()) {
                    productoViewModel.obtenerProductos()
                }
            }

            if (productos.isEmpty()) {
                // Muestra spinner mientras carga
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFF5E17EB))
                }
            } else {
                val producto: Producto? = productos.find { it.id == productoId }
                if (producto != null) {
                    VerRecetaScreen(producto = producto, navController = navController)
                } else {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Producto no encontrado",
                            color = Color.White,
                            fontSize = 18.sp
                        )
                    }
                }
            }
        }
        // AGREGAR PROVEEDOR
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

        // Pantalla para ver el detalle de un pedido
        composable(
            route = "detallePedido/{pedidoId}",
            arguments = listOf(navArgument("pedidoId") { type = NavType.LongType })
        ) { backStackEntry ->
            val pedidoId = backStackEntry.arguments?.getLong("pedidoId") ?: 0L
            val pedidoViewModel: PedidoViewModel = viewModel()
            val pedidos by pedidoViewModel.pedidos.collectAsState()

            // Cargar pedidos si aún no están cargados
            LaunchedEffect(Unit) { pedidoViewModel.obtenerPedidos() }

            val pedido = pedidos.find { it.id == pedidoId }

            if (pedido != null) {
                DetallePedidoScreen(
                    pedido = pedido,
                    navController = navController,
                    pedidoViewModel = pedidoViewModel
                )
            } else {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Pedido no encontrado", color = Color.White)
                }
            }
        }
    }
}

