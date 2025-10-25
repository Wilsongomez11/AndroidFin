package com.example.proyectofinal.Screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.foundation.lazy.items

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

    // Lista de insumos en bajo stock
    var insumosBajoStock by remember { mutableStateOf<List<Insumo>>(emptyList()) }

    // Cargar los insumos con bajo stock al iniciar
    LaunchedEffect(Unit) {
        insumoViewModel.verificarBajoStock { lista ->
            insumosBajoStock = lista
        }
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
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent
                    )
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
                    "dashboard" -> DashboardContent()
                    "reportes" -> PowerBIPlaceholder()
                    "personal" -> PersonalMenuScreen(navController)
                    "inventario" -> InventarioScreen(navController)
                    "pedidos" -> PedidosScreen(navController)

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

        DrawerItem("📊 Dashboard", "dashboard", selected, onNavigate)
        DrawerItem("📈 Reportes Power BI", "reportes", selected, onNavigate)
        DrawerItem("👥 Personal", "personal", selected, onNavigate)
        DrawerItem("📦 Inventario", "inventario", selected, onNavigate)
        DrawerItem("📋 Pedidos", "pedidos", selected, onNavigate)

        Spacer(modifier = Modifier.weight(1f))
        Divider(color = Color.Gray)

        TextButton(
            onClick = { onLogout() },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.textButtonColors(contentColor = Color.Red)
        ) {
            Text("🚪 Cerrar sesión", fontSize = 18.sp, fontWeight = FontWeight.Bold)
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
fun DashboardContent(viewModel: com.example.proyectofinal.ViewModel.InsumoViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    var insumosBajoStock by remember { mutableStateOf<List<com.example.proyectofinal.Model.Insumo>>(emptyList()) }
    var mensaje by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.verificarBajoStock { lista ->
            insumosBajoStock = lista
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .padding(horizontal = 20.dp, vertical = 30.dp)
            .fillMaxWidth()
    ) {
        Text(
            "📋 Bienvenido al panel principal",
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            "Selecciona una opción del menú",
            color = Color.White.copy(0.8f),
            fontSize = 14.sp
        )

        if (insumosBajoStock.isNotEmpty()) {
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "⚠️ Insumos con bajo stock:",
                color = Color(0xFFFFEB3B),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Spacer(modifier = Modifier.height(10.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            ) {
                items(insumosBajoStock, key = { it.id }) { insumo ->
                    InsumoAlertaCard(
                        insumo = insumo,
                        onActualizar = { actualizado ->
                            viewModel.actualizarInsumo(actualizado.id, actualizado) { resultado ->
                                mensaje = resultado
                                viewModel.verificarBajoStock { lista ->
                                    insumosBajoStock = lista
                                }
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

    val fondo = Brush.verticalGradient(
        colors = listOf(Color(0xFF6A11CB), Color(0xFF2575FC))
    )

    Box(
        modifier = Modifier
            .width(180.dp)
            .shadow(8.dp, RoundedCornerShape(20.dp))
            .background(fondo, RoundedCornerShape(20.dp))
            .padding(16.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            if (modoAjuste) {
                // Botón para cerrar modo ajuste
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        text = "✖",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 18.sp,
                        modifier = Modifier
                            .clickable { modoAjuste = false }
                    )
                }
            }

            // Nombre centrado del insumo
            Text(
                text = insumo.nombre,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )

            // Cantidad actual centrada
            Text(
                text = "Stock: $cantidad ${insumo.unidadMedida}",
                color = Color.White.copy(alpha = 0.9f),
                fontSize = 15.sp
            )

            if (!modoAjuste) {
                Button(
                    onClick = { modoAjuste = true },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9C27B0)),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(42.dp)
                ) {
                    Text("Ajustar", color = Color.White, fontSize = 15.sp)
                }
            } else {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Disminuir 1
                    IconButton(
                        onClick = { if (cantidad > 0) cantidad-- },
                        modifier = Modifier
                            .size(40.dp)
                            .background(Color.White.copy(alpha = 0.15f), RoundedCornerShape(50))
                    ) {
                        Text("-", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                    }

                    // Aumentar 1
                    IconButton(
                        onClick = { cantidad++ },
                        modifier = Modifier
                            .size(40.dp)
                            .background(Color.White.copy(alpha = 0.15f), RoundedCornerShape(50))
                    ) {
                        Text("+", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                    }

                    // Aumentar +5
                    IconButton(
                        onClick = { cantidad += 5 },
                        modifier = Modifier
                            .size(40.dp)
                            .background(Color.White.copy(alpha = 0.15f), RoundedCornerShape(50))
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
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(42.dp)
                ) {
                    Text("Aceptar", color = Color.White, fontSize = 15.sp)
                }
            }
        }
    }
}


@Composable
fun PowerBIPlaceholder() {
    Text(
        text = "📊 Aquí se integrarán los reportes Power BI",
        color = Color.White,
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold
    )
}
