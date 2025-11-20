package com.example.proyectofinal.Screen
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.proyectofinal.ViewModel.AdministradorViewModel
import androidx.compose.ui.text.style.TextAlign

@Composable
fun InventarioScreen(
    navController: NavHostController,
    viewModel: AdministradorViewModel = viewModel()
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF0B093B), Color(0xFF3A0CA3), Color(0xFF7209B7))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(40.dp)
        ) {
            Text(
                text = "\uD83D\uDCCB Gestión de Existencias",
                color = Color.White,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.headlineSmall
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(24.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    InventarioMenuButton(
                        label = "\uD83D\uDC41\uFE0F Ver Inventario",
                        onClick = { navController.navigate("verInventario") }
                    )
                    InventarioMenuButton(
                        label = "\u2795 Agregar Producto",
                        onClick = { navController.navigate("agregarProducto") }
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(24.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    InventarioMenuButton(
                        label = "\uD83E\uDDC2 Gestionar Insumos",
                        onClick = { navController.navigate("insumos?returnTo=inventario") }
                    )
                    InventarioMenuButton(
                        label = "\uD83D\uDCCB Ver Insumos",
                        onClick = { navController.navigate("verInsumos?returnTo=inventario") }
                    )
                }
            }
        }
    }
}

@Composable
fun InventarioMenuButton(label: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(110.dp)
            .shadow(8.dp, RoundedCornerShape(18.dp))
            .background(Color(0xFF5E17EB), RoundedCornerShape(18.dp))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            label,
            color = Color.White,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            lineHeight = 15.sp,
            modifier = Modifier.padding(horizontal = 8.dp),
            textAlign = TextAlign.Center
        )
    }
}
