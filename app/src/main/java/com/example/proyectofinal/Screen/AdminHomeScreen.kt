package com.example.proyectofinal.Screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.proyectofinal.Model.Insumo
import com.example.proyectofinal.ViewModel.AdministradorViewModel
import com.example.proyectofinal.ViewModel.InsumoViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminHomeScreen(
    navController: NavHostController,
    adminViewModel: AdministradorViewModel,
    insumoViewModel: InsumoViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var selectedScreen by remember { mutableStateOf("dashboard") }
    var insumosBajoStock by remember { mutableStateOf<List<Insumo>>(emptyList()) }

    LaunchedEffect(Unit) {
        insumoViewModel.verificarBajoStock { lista -> insumosBajoStock = lista }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(
                selected = selectedScreen,
                onNavigate = { route ->
                    scope.launch { drawerState.close() }
                    selectedScreen = route
                },
                onLogout = {
                    adminViewModel.cerrarSesion()
                    navController.navigate("login") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            when (selectedScreen) {
                                "dashboard" -> "Panel Administrador"
                                "personal" -> "Gestión de Personal"
                                "reportes" -> "Reportes Power BI"
                                "inventario" -> "Gestión de Inventario"
                                else -> "Panel"
                            },
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(
                                Icons.Default.Menu,
                                contentDescription = "Menu",
                                tint = Color.White
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
                )
            },
            containerColor = Color.Transparent
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            listOf(Color(0xFF0B093B), Color(0xFF3A0CA3), Color(0xFF7209B7))
                        )
                    )
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                when (selectedScreen) {
                    "dashboard" -> DashboardContent(adminViewModel, navController)
                    "reportes" -> PowerBIPlaceholder()
                    "personal" -> PersonalMenuScreen(navController)
                    "inventario" -> InventarioScreen(navController)
                    "pedidos" -> PedidosScreen(navController)
                    "caja" -> CajaScreen(navController, adminViewModel)
                }
            }
        }
    }
}

@Composable
fun DrawerContent(
    selected: String,
    onNavigate: (String) -> Unit,
    onLogout: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .width(280.dp)
            .background(Color(0xFF121212))
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Menú principal", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Divider(color = Color.Gray)

        DrawerItem("\uD83D\uDCCA Dashboard", "dashboard", selected, onNavigate)
        DrawerItem("\uD83D\uDCC8 Reportes Power BI", "reportes", selected, onNavigate)
        DrawerItem("\uD83D\uDC65 Personal", "personal", selected, onNavigate)
        DrawerItem("\uD83D\uDCE6 Inventario", "inventario", selected, onNavigate)
        DrawerItem("\uD83D\uDCCB Pedidos", "pedidos", selected, onNavigate)
        DrawerItem("\uD83D\uDCB3 Caja", "caja", selected, onNavigate)

        Spacer(modifier = Modifier.weight(1f))
        Divider(color = Color.Gray)

        TextButton(
            onClick = { onLogout() },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.textButtonColors(contentColor = Color.Red)
        ) {
            Text("\uD83D\uDEAA Cerrar sesión", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun DrawerItem(text: String, route: String, selected: String, onNavigate: (String) -> Unit) {
    val isSelected = selected == route
    TextButton(
        onClick = { onNavigate(route) },
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.textButtonColors(
            contentColor = if (isSelected) Color(0xFFFF9800) else Color.White
        )
    ) {
        Text(text, fontSize = 18.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal)
    }
}

@Composable
fun DashboardContent(
    adminViewModel: com.example.proyectofinal.ViewModel.AdministradorViewModel,
    navController: NavHostController,
    viewModel: com.example.proyectofinal.ViewModel.InsumoViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    var insumosBajoStock by remember { mutableStateOf<List<com.example.proyectofinal.Model.Insumo>>(emptyList()) }
    var mensaje by remember { mutableStateOf("") }

    val adminActual = adminViewModel.administradorActual.value?.nombre ?: "Administrador"
    val adminRol = adminViewModel.administradorActual.value?.cargo ?: "Administrador"

    val pedidoViewModel: com.example.proyectofinal.ViewModel.PedidoViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
    val productoViewModel: com.example.proyectofinal.ProductoViewModel = androidx.lifecycle.viewmodel.compose.viewModel()

    val pedidos by pedidoViewModel.pedidos.collectAsState()
    val productos by productoViewModel.productos.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.verificarBajoStock { lista -> insumosBajoStock = lista }
        pedidoViewModel.obtenerPedidos()
        productoViewModel.obtenerProductos()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text("\uD83D\uDCCB Bienvenido al panel principal",
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(6.dp, RoundedCornerShape(20.dp)),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)),
            shape = RoundedCornerShape(20.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text("\uD83D\uDC4B Bienvenido, $adminActual",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text("Rol: $adminRol",
                    color = Color.LightGray,
                    fontSize = 14.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { navController.navigate("caja") },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5E17EB)),
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .height(60.dp)
        ) {
            Text("\uD83D\uDCB3 Ir a Caja",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(24.dp))


        Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
            IndicadorCard("\uD83D\uDCCB Pedidos", pedidos.size.toString())
            IndicadorCard("\uD83D\uDCE6 Productos", productos.size.toString())
            IndicadorCard("\u26A0\uFE0F Bajo Stock", insumosBajoStock.size.toString())
        }

        if (insumosBajoStock.isNotEmpty()) {
            Spacer(modifier = Modifier.height(20.dp))
            Text("\u26A0\uFE0F Insumos con bajo stock:", color = Color(0xFFFFEB3B), fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(10.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)
            ) {
                items(insumosBajoStock, key = { it.id }) { insumo ->
                    InsumoAlertaCard(
                        insumo = insumo,
                        onActualizar = { actualizado ->
                            viewModel.actualizarInsumo(actualizado.id, actualizado) { resultado ->
                                mensaje = resultado
                                viewModel.verificarBajoStock { lista -> insumosBajoStock = lista }
                            }
                        }
                    )
                }
            }
        }

        if (mensaje.isNotEmpty()) {
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = mensaje, color = Color.White, fontSize = 14.sp)
        }
    }
}

@Composable
fun InsumoAlertaCard(
    insumo: com.example.proyectofinal.Model.Insumo,
    onActualizar: (com.example.proyectofinal.Model.Insumo) -> Unit
) {
    var modoAjuste by remember { mutableStateOf(false) }
    var cantidad by remember { mutableStateOf(insumo.cantidadActual.toInt()) }

    val fondo = Brush.verticalGradient(colors = listOf(Color(0xFF6A11CB), Color(0xFF2575FC)))

    Box(
        modifier = Modifier.width(180.dp).shadow(8.dp, RoundedCornerShape(20.dp))
            .background(fondo, RoundedCornerShape(20.dp)).padding(16.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
            if (modoAjuste) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    Text("\u2716", color = Color.White.copy(alpha = 0.8f), fontSize = 18.sp, modifier = Modifier.clickable { modoAjuste = false })
                }
            }

            Text(insumo.nombre, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Text("Stock: $cantidad ${insumo.unidadMedida}", color = Color.White.copy(alpha = 0.9f), fontSize = 15.sp)

            if (!modoAjuste) {
                Button(
                    onClick = { modoAjuste = true },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9C27B0)),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth(0.8f).height(42.dp)
                ) {
                    Text("Ajustar", color = Color.White, fontSize = 15.sp)
                }
            } else {
                Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                    IconButton(
                        onClick = { if (cantidad > 0) cantidad-- },
                        modifier = Modifier.size(40.dp).background(Color.White.copy(alpha = 0.15f), RoundedCornerShape(50))
                    ) {
                        Text("-", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                    }

                    IconButton(
                        onClick = { cantidad++ },
                        modifier = Modifier.size(40.dp).background(Color.White.copy(alpha = 0.15f), RoundedCornerShape(50))
                    ) {
                        Text("+", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                    }

                    IconButton(
                        onClick = { cantidad += 5 },
                        modifier = Modifier.size(40.dp).background(Color.White.copy(alpha = 0.15f), RoundedCornerShape(50))
                    ) {
                        Text("+5", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    onClick = {
                        val actualizado = insumo.copy(cantidadActual = cantidad.toDouble())
                        onActualizar(actualizado)
                        modoAjuste = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7E57C2)),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth(0.8f).height(42.dp)
                ) {
                    Text("Aceptar", color = Color.White, fontSize = 15.sp)
                }
            }
        }
    }
}

@Composable
fun IndicadorCard(label: String, value: String) {
    val fondo = Brush.verticalGradient(colors = listOf(Color(0xFF6A11CB), Color(0xFF2575FC)))
    Box(
        modifier = Modifier.width(100.dp).height(80.dp).shadow(8.dp, RoundedCornerShape(16.dp))
            .background(fondo, RoundedCornerShape(16.dp)),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(label, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            Text(value, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun PowerBIPlaceholder() {
    Text("\uD83D\uDCCA Aquí se integrarán los reportes Power BI", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
}
