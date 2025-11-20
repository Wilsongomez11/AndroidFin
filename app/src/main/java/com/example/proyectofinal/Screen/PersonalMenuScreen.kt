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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun PersonalMenuScreen(navController: NavHostController) {
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
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            Text(
                text = "\uD83D\uDC65 Gestión de Personal",
                color = Color.White,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.headlineSmall
            )

            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                PersonalMenuButton(
                    label = "\uD83D\uDC41\uFE0F Ver Personal",
                    onClick = { navController.navigate("verPersonal") }
                )
                PersonalMenuButton(
                    label = "\u2795 Agregar Personal",
                    onClick = { navController.navigate("agregarPersonal") }
                )
            }
        }
    }
}

@Composable
fun PersonalMenuButton(label: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(130.dp)
            .shadow(8.dp, RoundedCornerShape(20.dp))
            .background(Color(0xFF5E17EB), RoundedCornerShape(20.dp))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            label,
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 6.dp),
            textAlign = TextAlign.Center
        )
    }
}
