package com.example.proyectofinal

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import com.example.proyectofinal.Model.Producto
import com.example.proyectofinal.Model.Pedido

@Composable
fun AppNavigation(
    navController: NavHostController,
    adminService: AdministradorService
) {
    val adminViewModel = remember { AdministradorViewModel() }
    val pedidoViewModelCompartido: PedidoViewModel = viewModel()

    NavHost(navController = navController, startDestination = "login") {

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

        composable("admin") { AdminHomeScreen(navController, adminViewModel) }
        composable("mesero") { MeseroHomeScreen(navController) }
        composable("pizzero") { PizzeroHomeScreen(navController) }

        composable("personalMenu") { PersonalMenuScreen(navController) }
        composable("agregarPersonal") { AgregarPersonalScreen(navController) }
        composable("verPersonal") { VerPersonalScreen(navController) }

        composable("inventario") { InventarioScreen(navController) }
        composable("caja") { CajaScreen(navController, adminViewModel, pedidoViewModelCompartido) }

        composable("agregarProducto") {
            val vm: ProductoViewModel = viewModel()
            AgregarProductoScreen(navController, vm, adminViewModel)
        }

        composable("verInventario") {
            val vm: ProductoViewModel = viewModel()
            VerProductoScreen(navController, vm)
        }

        composable("crearPedidoLibre") {
            CrearPedidoScreen(navController = navController, mesaId = null, pedidoViewModel = pedidoViewModelCompartido)
        }

        composable(
            route = "crearPedido/{mesaId}",
            arguments = listOf(navArgument("mesaId") { type = NavType.IntType })
        ) {
            val mesaId = it.arguments?.getInt("mesaId")
            CrearPedidoScreen(navController, mesaId, pedidoViewModelCompartido)
        }

        composable("mesas") {
            MesasScreen(navController, pedidoViewModelCompartido)
        }

        composable(
            route = "mesa/{mesaId}",
            arguments = listOf(navArgument("mesaId") { type = NavType.IntType })
        ) {
            val mesaId = it.arguments?.getInt("mesaId") ?: 0
            PedidoDeMesaScreen(navController, mesaId)
        }

        composable("pedidos") {
            PedidosScreen(navController, pedidoViewModelCompartido)
        }

        composable(
            route = "detallePedido/{pedidoId}",
            arguments = listOf(navArgument("pedidoId") { type = NavType.LongType })
        ) { entry ->
            val pedidoId = entry.arguments?.getLong("pedidoId") ?: 0L
            val pedidos by pedidoViewModelCompartido.pedidos.collectAsState()
            val pedido = remember(pedidos, pedidoId) { pedidos.find { it.id == pedidoId } }

            when {
                pedido == null && pedidos.isEmpty() -> {
                    LaunchedEffect(Unit) { pedidoViewModelCompartido.obtenerPedidos() }
                    Box(Modifier.fillMaxSize(), Alignment.Center) {
                        CircularProgressIndicator(color = Color.White)
                    }
                }
                pedido != null -> {
                    DetallePedidoScreen(pedido, navController, pedidoViewModelCompartido)
                }
                else -> {
                    Box(Modifier.fillMaxSize(), Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Pedido no encontrado", color = Color.White)
                            Spacer(Modifier.height(16.dp))
                            Button(onClick = { navController.popBackStack() }) {
                                Text("Volver")
                            }
                        }
                    }
                }
            }
        }

        composable(
            route = "devolverPedido/{pedidoId}",
            arguments = listOf(navArgument("pedidoId") { type = NavType.LongType })
        ) { entry ->
            val pedidoId = entry.arguments?.getLong("pedidoId") ?: 0
            val pedidos by pedidoViewModelCompartido.pedidos.collectAsState()
            val pedido = remember(pedidos, pedidoId) { pedidos.find { it.id == pedidoId } }

            when {
                pedido == null && pedidos.isEmpty() -> {
                    LaunchedEffect(Unit) { pedidoViewModelCompartido.obtenerPedidos() }
                    Box(Modifier.fillMaxSize(), Alignment.Center) {
                        CircularProgressIndicator(color = Color.White)
                    }
                }
                pedido != null -> {
                    DevolucionDetalleScreen(navController, pedido, pedidoViewModelCompartido)
                }
                else -> {
                    Box(Modifier.fillMaxSize(), Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Pedido no encontrado", color = Color.White)
                            Spacer(Modifier.height(16.dp))
                            Button(onClick = { navController.popBackStack() }) {
                                Text("Volver")
                            }
                        }
                    }
                }
            }
        }

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

        composable(
            route = "verReceta/{productoId}",
            arguments = listOf(navArgument("productoId") { type = NavType.LongType })
        ) { entry ->
            val productoId = entry.arguments?.getLong("productoId") ?: 0L
            val vm: ProductoViewModel = viewModel()
            val productos by vm.productos.collectAsState()

            LaunchedEffect(Unit) { vm.obtenerProductos() }

            val producto = productos.find { it.id == productoId }

            if (producto != null) {
                VerRecetaScreen(producto, navController)
            } else {
                Box(Modifier.fillMaxSize(), Alignment.Center) {
                    Text("Producto no encontrado", color = Color.White)
                }
            }
        }

        composable(
            route = "agregarProveedor?id={id}",
            arguments = listOf(navArgument("id") {
                type = NavType.StringType
                nullable = true
                defaultValue = null
            })
        ) { entry ->
            val id = entry.arguments?.getString("id")
            AgregarProveedor(navController, proveedorId = id)
        }


        composable(
            route = "facturar/{pedidoId}",
            arguments = listOf(navArgument("pedidoId") { type = NavType.LongType })
        ) { entry ->
            val pedidoId = entry.arguments?.getLong("pedidoId") ?: 0L
            val facturaViewModel: FacturaViewModel = viewModel()
            val pedidos by pedidoViewModelCompartido.pedidos.collectAsState()
            val pedido = remember(pedidos) { pedidos.find { it.id == pedidoId } }

            when (pedido) {
                null -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Color.White)
                    }
                }
                else -> {
                    FacturarPedidoScreen(
                        navController = navController,
                        pedido = pedido,
                        facturaViewModel = facturaViewModel
                    )
                }
            }
        }
        composable("facturas") {
            val facturaViewModel: FacturaViewModel = viewModel()

            ListarFacturasScreen(
                navController = navController,
                facturaViewModel = facturaViewModel
            )
        }

        composable("verFactura/{id}") { entry ->
            val facturaId = entry.arguments?.getString("id")!!.toLong()
            val context = LocalContext.current
            val viewModel: FacturaViewModel = viewModel()

            VerFacturaScreen(
                facturaId = facturaId,
                viewModel = viewModel,
                context = context,
                navController = navController
            )
        }

    }
}
