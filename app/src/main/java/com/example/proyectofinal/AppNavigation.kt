package com.example.proyectofinal

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.proyectofinal.Api.AdministradorService
import com.example.proyectofinal.Screen.*
import com.example.proyectofinal.ViewModel.*
import com.example.proyectofinal.admin.VerPersonalScreen
import com.example.proyectofinal.login.LoginScreen

@Composable
fun AppNavigation(
    navController: NavHostController,
    adminService: AdministradorService
) {
    val adminViewModel = remember { AdministradorViewModel() }
    val pedidoVM: PedidoViewModel = viewModel()

    NavHost(navController = navController, startDestination = "login") {

        // ============================
        //      FACTURA
        // ============================
        composable(
            route = "factura/{facturaId}",
            arguments = listOf(
                navArgument("facturaId") { type = NavType.LongType }
            )
        ) { entry ->

            val facturaId = entry.arguments?.getLong("facturaId") ?: 0L

            FacturaScreen(
                navController = navController,
                id = facturaId
            )
        }

        // ============================
        //      LOGIN
        // ============================
        composable("login") {
            LoginScreen(
                navController = navController,
                adminService = adminService,
                adminViewModel = adminViewModel,
                onLoginSuccess = { user ->
                    when (user.cargo.uppercase()) {
                        "ADMIN", "ADMINISTRADOR" ->
                            navController.navigate("admin") {
                                popUpTo("login") { inclusive = true }
                            }

                        "MESERO" ->
                            navController.navigate("mesero") {
                                popUpTo("login") { inclusive = true }
                            }

                        "PIZZERO" ->
                            navController.navigate("pizzero") {
                                popUpTo("login") { inclusive = true }
                            }

                        else ->
                            navController.navigate("login") {
                                popUpTo("login") { inclusive = true }
                            }
                    }
                }
            )
        }

        // ============================
        //      HOME SEGÚN ROL
        // ============================
        composable("admin") {
            AdminHomeScreen(navController, adminViewModel)
        }

        composable("mesero") {
            MeseroHomeScreen(navController)
        }

        composable("pizzero") {
            PizzeroHomeScreen(navController)
        }

        // ============================
        //      PEDIDOS
        // ============================
        composable("pedidos") {
            PedidosScreen(navController, pedidoVM)
        }

        composable(
            route = "detallePedido/{pedidoId}",
            arguments = listOf(navArgument("pedidoId") { type = NavType.LongType })
        ) { entry ->
            val pedidoId = entry.arguments?.getLong("pedidoId") ?: 0L
            val pedidos by pedidoVM.pedidos.collectAsState()

            val pedido = pedidos.find { it.id == pedidoId }

            if (pedido != null) {
                DetallePedidoScreen(
                    pedido = pedido,
                    navController = navController,
                    pedidoViewModel = pedidoVM
                )
            } else {
                Box(Modifier.fillMaxSize(), Alignment.Center) {
                    Text("Pedido no encontrado", color = Color.White)
                }
            }
        }

        // ============================
        //     MESAS / MESA DETALLE
        // ============================
        composable("mesas") {
            MesasScreen(navController, pedidoVM)
        }

        composable(
            route = "mesa/{mesaId}",
            arguments = listOf(navArgument("mesaId") { type = NavType.IntType })
        ) {
            val mesaId = it.arguments?.getInt("mesaId") ?: 0
            PedidoDeMesaScreen(navController, mesaId)
        }

        // ============================
        //     CREAR PEDIDO
        // ============================
        composable("crearPedidoLibre") {
            CrearPedidoScreen(navController, null, pedidoVM)
        }

        composable(
            route = "crearPedido/{mesaId}",
            arguments = listOf(navArgument("mesaId") { type = NavType.IntType })
        ) {
            val mesaId = it.arguments?.getInt("mesaId")
            CrearPedidoScreen(navController, mesaId, pedidoVM)
        }

        // ============================
        //     INSUMOS / INVENTARIO
        // ============================
        composable("inventario") { InventarioScreen(navController) }

        composable("verInventario") {
            val vm: ProductoViewModel = viewModel()
            VerProductoScreen(navController, vm)
        }

        composable(
            route = "insumos?returnTo={returnTo}",
            arguments = listOf(navArgument("returnTo") { defaultValue = "inventario" })
        ) { entry ->
            val returnTo = entry.arguments?.getString("returnTo") ?: "inventario"
            AppBackground { InsumosScreen(navController, returnTo) }
        }

        composable(
            route = "verInsumos?returnTo={returnTo}",
            arguments = listOf(navArgument("returnTo") { defaultValue = "inventario" })
        ) { entry ->
            val returnTo = entry.arguments?.getString("returnTo") ?: "inventario"
            AppBackground { VerInsumosScreen(navController, returnTo) }
        }

        // ============================
        //     PERSONAL
        // ============================
        composable("personalMenu") { PersonalMenuScreen(navController) }
        composable("agregarPersonal") { AgregarPersonalScreen(navController) }
        composable("verPersonal") { VerPersonalScreen(navController) }

        // ============================
        //     PROVEEDORES
        // ============================
        composable(
            route = "agregarProveedor?id={id}",
            arguments = listOf(
                navArgument("id") {
                    type = NavType.StringType
                    defaultValue = null
                    nullable = true
                }
            )
        ) {
            val proveedorId = it.arguments?.getString("id")
            AgregarProveedor(navController, proveedorId)
        }
    }
}
