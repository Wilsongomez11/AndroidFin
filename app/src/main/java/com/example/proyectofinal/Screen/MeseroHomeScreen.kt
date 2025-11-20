package com.example.proyectofinal.Screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.proyectofinal.ViewModel.MeseroViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MeseroHomeScreen(
    navController: NavHostController,
    meseroViewModel: MeseroViewModel = viewModel()
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = true) {
        meseroViewModel.cargarDatos()
    }

    val meseroNombre by meseroViewModel.meseroNombre.collectAsState()
    val pedidosPendientes by meseroViewModel.pedidosPendientes.collectAsState()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = Color(0xFF1C1B29)
            ) {
                MeseroDrawerContent(
                    selected = "home",
                    onNavigate = { route ->
                        scope.launch {
                            drawerState.close()
                            navController.navigate(route)
                        }
                    },
                    onLogout = {
                        scope.launch {
                            drawerState.close()
                        }
                        navController.navigate("login") {
                            popUpTo("meseroHomeScreen") { inclusive = true }
                        }
                    }
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                "\uD83D\uDC68\u200D\uD83C\uDF73",
                                fontSize = 24.sp,
                                modifier = Modifier.padding(end = 8.dp)
                            )
                            Text("Panel del Mesero", color = Color.White)
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch {
                                drawerState.open()
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Filled.Menu,
                                contentDescription = "Abrir menú",
                                tint = Color.White
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(0xFF3A0CA3)
                    )
                )
            }
        ) { paddingValues ->
            MeseroHomeContent(
                modifier = Modifier.padding(paddingValues),
                meseroNombre = meseroNombre,
                pedidosPendientes = pedidosPendientes,
                onNavigateToMesas = {
                    navController.navigate("mesasScreen")
                },
                onNavigateToPedidos = {
                    navController.navigate("pedidos")
                }
            )
        }
    }
}

@Composable
fun MeseroHomeContent(
    modifier: Modifier = Modifier,
    meseroNombre: String,
    pedidosPendientes: Int,
    onNavigateToMesas: () -> Unit,
    onNavigateToPedidos: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF0B093B),
                        Color(0xFF3A0CA3),
                        Color(0xFF7209B7)
                    )
                )
            )
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(32.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(12.dp, RoundedCornerShape(24.dp)),
            colors = CardDefaults.cardColors(Color(0xFF1A144A)),
            shape = RoundedCornerShape(24.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "\uD83D\uDC4B",
                    fontSize = 48.sp
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    "Bienvenido",
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 16.sp
                )
                Text(
                    meseroNombre,
                    color = Color.White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(Modifier.height(24.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(10.dp, RoundedCornerShape(20.dp)),
            colors = CardDefaults.cardColors(Color(0xFF1C1B29)),
            shape = RoundedCornerShape(20.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        "Pedidos pendientes",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 14.sp
                    )
                    Text(
                        "$pedidosPendientes",
                        color = Color(0xFFFFC107),
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(
                    "\uD83D\uDCCB",
                    fontSize = 64.sp
                )
            }
        }

        Spacer(Modifier.height(32.dp))

        Button(
            onClick = onNavigateToMesas,
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            colors = ButtonDefaults.buttonColors(Color(0xFF7C4DFF)),
            shape = RoundedCornerShape(16.dp),
            elevation = ButtonDefaults.buttonElevation(8.dp)
        ) {
            Text(
                "\uD83C\uDF7D\uFE0F Gestionar Mesas",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = onNavigateToPedidos,
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            colors = ButtonDefaults.buttonColors(Color(0xFF00BCD4)),
            shape = RoundedCornerShape(16.dp),
            elevation = ButtonDefaults.buttonElevation(8.dp)
        ) {
            Text(
                "\uD83D\uDCDD Ver Pedidos",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(Modifier.weight(1f))

        Text(
            "Desliza desde la izquierda para el menú",
            color = Color.White.copy(alpha = 0.5f),
            fontSize = 12.sp
        )
    }
}

@Composable
private fun MeseroDrawerContent(
    selected: String,
    onNavigate: (String) -> Unit,
    onLogout: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1C1B29))
            .padding(24.dp)
    ) {
        Spacer(Modifier.height(32.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 24.dp)
        ) {
            Text(
                "\uD83D\uDC68\u200D\uD83C\uDF73",
                fontSize = 32.sp
            )
            Spacer(Modifier.width(12.dp))
            Text(
                "Menú Mesero",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Divider(color = Color.Gray.copy(alpha = 0.3f))

        Spacer(Modifier.height(24.dp))

        MeseroDrawerItem(
            label = "\uD83C\uDFE0 Inicio",
            route = "meseroHomeScreen",
            selected = selected,
            onNavigate = onNavigate
        )

        MeseroDrawerItem(
            label = "\uD83C\uDF7D\uFE0F Mesas",
            route = "mesasScreen",
            selected = selected,
            onNavigate = onNavigate
        )

        MeseroDrawerItem(
            label = "\uD83D\uDCDD Pedidos",
            route = "pedidos",
            selected = selected,
            onNavigate = onNavigate
        )

        Spacer(Modifier.weight(1f))

        Divider(color = Color.Gray.copy(alpha = 0.3f))

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = onLogout,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(Color(0xFFFF4B4B)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                "\uD83D\uDEAA Cerrar sesión",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun MeseroDrawerItem(
    label: String,
    route: String,
    selected: String,
    onNavigate: (String) -> Unit
) {
    val isSelected = route == selected

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(
            if (isSelected) Color(0xFF3A0CA3) else Color.Transparent
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        TextButton(
            onClick = { onNavigate(route) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = label,
                fontSize = 16.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
        }
    }
}