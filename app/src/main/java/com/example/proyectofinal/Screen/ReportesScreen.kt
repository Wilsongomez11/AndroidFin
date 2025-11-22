package com.example.proyectofinal.Screen

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import androidx.navigation.NavController
import com.example.proyectofinal.Model.Factura
import com.example.proyectofinal.ViewModel.FacturaViewModel
import java.io.File
import java.io.FileOutputStream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportesScreen(
    viewModel: FacturaViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
) {
    val contexto = LocalContext.current

    val loading by viewModel.loadingReporte.collectAsState()
    val facturas by viewModel.reporteFacturas.collectAsState()
    val resumen by viewModel.resumenReporte.collectAsState()
    val error by viewModel.errorReporte.collectAsState()

    var tipoReporte by remember { mutableStateOf("dia") }

    LaunchedEffect(tipoReporte) {
        viewModel.cargarReporte(tipoReporte)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Reportes", color = Color.White)
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF0B093B))
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFF0B093B), Color(0xFF3A0CA3), Color(0xFF7209B7))
                    )
                )
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                BotonFiltro("Día", tipoReporte == "dia") { tipoReporte = "dia" }
                BotonFiltro("Semana", tipoReporte == "semana") { tipoReporte = "semana" }
                BotonFiltro("Mes", tipoReporte == "mes") { tipoReporte = "mes" }
            }

            Spacer(modifier = Modifier.height(20.dp))


            if (loading) {
                CircularProgressIndicator(color = Color.White)
                return@Column
            }


            error?.let {
                Text(it, color = Color.Red, fontSize = 16.sp)
                return@Column
            }


            resumen?.let {
                ReporteResumenCard(it)
            }

            Spacer(modifier = Modifier.height(20.dp))


            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                items(facturas) { factura ->
                    FacturaItem(factura)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    viewModel.descargarExcel(tipoReporte) { bytes, err ->
                        if (bytes != null) {
                            guardarExcel(contexto, bytes, tipoReporte)
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5E17EB))
            ) {
                Text("Descargar Excel", color = Color.White, fontSize = 18.sp)
            }
        }
    }
}

@Composable
fun BotonFiltro(texto: String, activo: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (activo) Color(0xFF9C27B0) else Color(0xFF3A0CA3)
        ),
        shape = RoundedCornerShape(20.dp)
    ) {
        Text(texto, color = Color.White)
    }
}

@Composable
fun FacturaItem(factura: Factura) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Factura: ${factura.numero}", color = Color.White, fontWeight = FontWeight.Bold)
            Text("Cliente: ${factura.clienteNombre}", color = Color.LightGray)
            Text("Total: $${factura.total}", color = Color(0xFFFFEB3B))
            Text("Método: ${factura.metodoPago}", color = Color.White)
        }
    }
}

@Composable
fun ReporteResumenCard(resumen: com.example.proyectofinal.Model.ResumenReporte) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text("Resumen del reporte", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Spacer(modifier = Modifier.height(10.dp))

            Text("Total facturas: ${resumen.totalFacturas}", color = Color.White)
            Text("Total ventas: $${resumen.totalVentas}", color = Color.White)
            Text("Propinas: $${resumen.totalPropinas}", color = Color.White)
            Text("Método más usado: ${resumen.metodoPagoMasUsado}", color = Color.White)
            Text("Ticket promedio: $${resumen.ticketPromedio}", color = Color.White)
        }
    }
}

fun guardarExcel(context: Context, bytes: ByteArray, tipo: String) {
    try {
        val file = File(context.getExternalFilesDir(null), "reporte-$tipo.xlsx")
        FileOutputStream(file).use { it.write(bytes) }

        val uri = FileProvider.getUriForFile(
            context,
            context.packageName + ".provider",
            file
        )

        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        context.startActivity(intent)

    } catch (e: Exception) {
        e.printStackTrace()
        Toast.makeText(context, "Error al abrir el archivo: ${e.message}", Toast.LENGTH_LONG).show()
    }
}
