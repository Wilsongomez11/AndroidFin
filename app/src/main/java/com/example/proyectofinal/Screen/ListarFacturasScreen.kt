package com.example.proyectofinal.Screen

import android.os.Environment
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.proyectofinal.Model.Factura
import com.example.proyectofinal.ViewModel.FacturaViewModel
import java.io.File
import java.io.FileOutputStream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListarFacturasScreen(
    navController: NavHostController,
    facturaViewModel: FacturaViewModel
) {
    var facturas by remember { mutableStateOf<List<Factura>>(emptyList()) }
    var error by remember { mutableStateOf<String?>(null) }
    var loading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        facturaViewModel.listarFacturas { lista, err ->
            loading = false
            if (lista != null) facturas = lista
            if (err != null) error = err
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Facturas",
                        color = Color.White,
                        fontSize = 20.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        },
        containerColor = Color.Transparent
    ) { padding ->

        Box(
            modifier = Modifier
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
                .padding(padding)
                .padding(16.dp)
        ) {

            when {
                loading -> {
                    Box(Modifier.fillMaxSize(), Alignment.Center) {
                        CircularProgressIndicator(color = Color.White)
                    }
                }

                error != null -> {
                    Box(Modifier.fillMaxSize(), Alignment.Center) {
                        Text(error ?: "", color = Color.White)
                    }
                }

                facturas.isEmpty() -> {
                    Box(Modifier.fillMaxSize(), Alignment.Center) {
                        Text("No hay facturas", color = Color.White)
                    }
                }

                else -> {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(facturas) { factura ->
                            FacturaCard(
                                factura = factura,
                                facturaViewModel = facturaViewModel,
                                navController = navController
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FacturaCard(
    factura: Factura,
    facturaViewModel: FacturaViewModel,
    navController: NavHostController

) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(8.dp, RoundedCornerShape(20.dp)),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(Color(0xFF1E1E1E))
    ) {
        Column(Modifier.padding(16.dp)) {

            Text(
                "Factura #${factura.id ?: "-"}",
                color = Color.White,
                fontSize = 20.sp
            )

            Spacer(Modifier.height(4.dp))

            Text(
                factura.fechaEmision ?: "Sin fecha",
                color = Color.LightGray,
                fontSize = 14.sp
            )

            Spacer(Modifier.height(6.dp))

            Text(
                "Total: $${String.format("%.2f", factura.total)}",
                color = Color(0xFF00FF9D),
                fontSize = 18.sp
            )

            Spacer(Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = {
                        factura.id?.let { navController.navigate("verFactura/$it") }
                    },
                    colors = ButtonDefaults.buttonColors(Color(0xFF7C4DFF)),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Ver PDF", color = Color.White)
                }

                Button(
                    onClick = {
                        factura.id?.let { idNoNull ->
                            facturaViewModel.descargarPdf(idNoNull) { bytes, _ ->
                                if (bytes != null) guardarPdfLocal(idNoNull, bytes)
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(Color(0xFF5E17EB)),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Descargar", color = Color.White)
                }
            }

        }
    }
}

fun guardarPdfLocal(id: Long, bytes: ByteArray) {
    val dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
    val file = File(dir, "factura_$id.pdf")
    FileOutputStream(file).use { it.write(bytes) }
}
