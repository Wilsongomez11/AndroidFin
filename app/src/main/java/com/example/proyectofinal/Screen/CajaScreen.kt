package com.example.proyectofinal.Screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.example.proyectofinal.Model.MovimientoCaja
import com.example.proyectofinal.Model.Pedido
import com.example.proyectofinal.ViewModel.AdministradorViewModel
import com.example.proyectofinal.ViewModel.CajaViewModel
import com.example.proyectofinal.ViewModel.FacturaViewModel
import com.example.proyectofinal.ViewModel.PedidoViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun CajaScreen(
    navController: NavHostController,
    adminViewModel: AdministradorViewModel,
    pedidoViewModel: PedidoViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    cajaViewModel: CajaViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val pedidos by pedidoViewModel.pedidos.collectAsState()
    val movimientos by cajaViewModel.movimientos.collectAsState()
    val balance by cajaViewModel.balance.collectAsState()
    val resultadoPago by cajaViewModel.resultadoPago.collectAsState()
    val errorPago by cajaViewModel.error.collectAsState()

    var showPago by remember { mutableStateOf(false) }
    var showDevolucion by remember { mutableStateOf(false) }
    var showRecibo by remember { mutableStateOf(false) }

    var pedidoIdSel by remember { mutableStateOf<Long?>(null) }
    var totalSel by remember { mutableStateOf(0.0) }
    var montoPagado by remember { mutableStateOf(0.0) }

    val adminId = adminViewModel.administradorActual.value?.id ?: 1L
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        pedidoViewModel.obtenerPedidos(true)
        cajaViewModel.recargarTodo()
    }

    val pedidosPendientes = pedidos.filter {
        val estado = it.estado?.lowercase() ?: ""
        estado !in listOf("pagado", "devuelto", "parcialmente devuelto")
    }

    val hoy = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    val movimientosDeHoy = movimientos
        .filter { it.fecha?.take(10) == hoy }
        .sortedByDescending { it.id ?: 0 }

    Column(
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
            .padding(16.dp)
    ) {
        CajaEncabezado(balance = balance)
        Spacer(Modifier.height(24.dp))

        Button(
            onClick = { navController.navigate("facturas") },
            colors = ButtonDefaults.buttonColors(Color(0xFF5E17EB)),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp)
        ) {
            Text(
                text = "Ver Facturas",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(Modifier.height(16.dp))

        Text(
            "Pedidos pendientes (${pedidosPendientes.size})",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(Modifier.height(24.dp))

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 320.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(
                items = pedidosPendientes,
                key = { it.id ?: 0 }
            ) { pedido: Pedido ->
                PedidoCard(
                    pedido = pedido,
                    onPagar = { id, total ->
                        pedidoIdSel = id
                        totalSel = total
                        showPago = true
                    },
                    onDevolver = { id, total ->
                        pedidoIdSel = id
                        totalSel = total
                        showDevolucion = true
                    }
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        Text(
            "Movimientos del día (${movimientosDeHoy.size})",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(Modifier.height(8.dp))

        MovimientosListaMejorada(movimientosDeHoy)

        if (errorPago != null) {
            Spacer(Modifier.height(8.dp))
            Text(errorPago ?: "", color = Color(0xFFFF4B4B), fontSize = 14.sp)
        }
    }

    if (showPago) {
        PagoPopup(
            total = totalSel,
            onCerrar = { showPago = false },
            onRegistrar = { montoRecibido ->
                showPago = false
                montoPagado = montoRecibido

                scope.launch {
                    try {
                        cajaViewModel.registrarPago(
                            pedidoId = pedidoIdSel!!,
                            monto = montoRecibido,
                            adminId = adminId
                        )

                        delay(400)

                        pedidoViewModel.actualizarEstadoPedido(pedidoIdSel!!, "Pagado")

                        delay(600)

                        pedidoViewModel.obtenerPedidos(true)
                        pedidoViewModel.cargarEstadoMesas()
                        cajaViewModel.recargarTodo()

                        showRecibo = true

                    } catch (e: Exception) {
                        Log.e("CajaScreen", "Error en pago", e)
                    }
                }
            }
        )
    }

    if (showDevolucion) {
        DevolucionPopup(
            total = totalSel,
            onCerrar = { showDevolucion = false },
            onConfirmar = { montoDevolver ->
                showDevolucion = false

                scope.launch {
                    try {
                        pedidoViewModel.actualizarEstadoPedido(pedidoIdSel!!, "Devuelto")

                        delay(300)

                        cajaViewModel.registrarDevolucion(
                            pedidoId = pedidoIdSel!!,
                            monto = (montoDevolver * -1.0),
                            adminId = adminId,
                            cantidades = emptyMap()
                        )

                        delay(500)

                        cajaViewModel.recargarTodo()
                        pedidoViewModel.obtenerPedidos(true)

                    } catch (e: Exception) {
                        Log.e("CajaScreen", "Error devolucion", e)
                    }
                }
            }
        )
    }

    if (showRecibo && resultadoPago != null) {
        ReciboPopup(
            pedidoId = pedidoIdSel!!,
            total = totalSel,
            pagado = montoPagado,
            cambio = resultadoPago!!["cambio"]?.toString() ?: "0",
            onCerrar = {
                val id = pedidoIdSel ?: return@ReciboPopup
                showRecibo = false
                cajaViewModel.limpiarResultadoPago()

                val facturaVM = FacturaViewModel()
                facturaVM.obtenerFacturaPorPedido(id) { facturaExistente, err ->

                    if (facturaExistente != null) {
                        navController.navigate("verFactura/${facturaExistente.id}")
                    } else {
                        navController.navigate("facturar/$id")
                    }
                }

                pedidoIdSel = null
                totalSel = 0.0
                montoPagado = 0.0
            }

        )
    }
}


@Composable
fun CajaEncabezado(balance: Double) {
    val colorBalance = if (balance >= 0) Color(0xFF00FF9D) else Color(0xFFFF4B4B)

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
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Caja del Día",
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(8.dp))
            Text(
                "Balance actual",
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 14.sp
            )
            Text(
                "$${String.format(Locale.getDefault(), "%.2f", balance)}",
                color = colorBalance,
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold
            )
        }
    }
}

@Composable
fun PedidoCard(
    pedido: Pedido,
    onPagar: (Long, Double) -> Unit,
    onDevolver: (Long, Double) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(10.dp, RoundedCornerShape(20.dp)),
        colors = CardDefaults.cardColors(Color(0xFF1C1B29)),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Pedido #${pedido.id}",
                    color = Color(0xFFB388FF),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "$${String.format(Locale.getDefault(), "%.2f", pedido.total)}",
                    color = Color(0xFF76FF03),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "Mesa: ${pedido.mesa ?: "Sin mesa"}",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 14.sp
                )
                Text(
                    pedido.estado ?: "Pendiente",
                    color = Color(0xFFFFC107),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = { onPagar(pedido.id!!, pedido.total) },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(Color(0xFF7C4DFF)),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text("\uD83D\uDCB3 Pagar", color = Color.White)
                }

                Button(
                    onClick = { onDevolver(pedido.id!!, pedido.total) },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(Color(0xFFFFC107)),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text("\u21A9 Devolver", color = Color.Black)
                }

            }
        }
    }
}

@Composable
fun MovimientosListaMejorada(movs: List<MovimientoCaja>) {
    if (movs.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "No hay movimientos hoy",
                color = Color.White.copy(alpha = 0.6f),
                fontSize = 14.sp
            )
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 280.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(
                items = movs,
                key = { it.id ?: 0 }
            ) { mov ->
                MovimientoCard(mov)
            }
        }
    }
}

@Composable
fun MovimientoCard(mov: MovimientoCaja) {
    val esIngreso = mov.monto >= 0
    val colorMonto = if (esIngreso) Color(0xFF00FF9D) else Color(0xFFFF4B4B)
    val icono = if (esIngreso) "↑" else "↓"
    val tipo = mov.descripcion?.split(" - ")?.getOrNull(0) ?: "Movimiento"

    val hora = try {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val fecha = sdf.parse(mov.fecha ?: "")
        SimpleDateFormat("HH:mm", Locale.getDefault()).format(fecha ?: Date())
    } catch (e: Exception) {
        "00:00"
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(6.dp, RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(Color(0xFF1C1B29)),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            colorMonto.copy(alpha = 0.2f),
                            RoundedCornerShape(12.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        icono,
                        color = colorMonto,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(Modifier.width(12.dp))

                Column {
                    Text(
                        tipo,
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        hora,
                        color = Color.White.copy(alpha = 0.6f),
                        fontSize = 12.sp
                    )
                }
            }

            Text(
                "${if (esIngreso) "+" else ""}$${String.format(Locale.getDefault(), "%.2f", mov.monto)}",
                color = colorMonto,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.End
            )
        }
    }
}

@Composable
fun PagoPopup(
    total: Double,
    onCerrar: () -> Unit,
    onRegistrar: (Double) -> Unit
) {
    var monto by remember { mutableStateOf("") }

    BasePopup(
        titulo = "Registrar pago",
        contenido = {
            Text(
                "Monto total: $${String.format(Locale.getDefault(), "%.2f", total)}",
                color = Color(0xFFB388FF),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(12.dp))
            CajaInput(
                value = monto,
                label = "Monto recibido",
                onChange = { monto = it }
            )

            val montoRecibido = monto.toDoubleOrNull() ?: 0.0
            if (montoRecibido >= total) {
                Spacer(Modifier.height(8.dp))
                Text(
                    "Cambio: $${String.format(Locale.getDefault(), "%.2f", montoRecibido - total)}",
                    color = Color(0xFF00FF9D),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        confirmar = {
            val valor = monto.toDoubleOrNull()
            if (valor != null && valor >= total) {
                onRegistrar(valor)
            }
        },
        cancelar = onCerrar
    )
}

@Composable
fun DevolucionPopup(
    total: Double,
    onCerrar: () -> Unit,
    onConfirmar: (Double) -> Unit
) {
    BasePopup(
        titulo = "Devolver pedido",
        contenido = {
            Text(
                "¿Deseas devolver el pedido completo?",
                color = Color.White,
                fontSize = 16.sp
            )
            Spacer(Modifier.height(8.dp))
            Text(
                "Monto a devolver: $${String.format(Locale.getDefault(), "%.2f", total)}",
                color = Color(0xFFFF4B4B),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        },
        confirmarTexto = "Sí, devolver",
        confirmar = { onConfirmar(total) },
        cancelar = onCerrar
    )
}

@Composable
fun ReciboPopup(
    pedidoId: Long,
    total: Double,
    pagado: Double,
    cambio: String,
    onCerrar: () -> Unit
) {
    BasePopup(
        titulo = "Pago registrado",
        contenido = {
            Card(
                colors = CardDefaults.cardColors(Color(0xFF2A2844)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(Modifier.padding(16.dp)) {
                    ReciboPropiedades("Pedido #", pedidoId.toString())
                    ReciboPropiedades("Total", "$${String.format(Locale.getDefault(), "%.2f", total)}")
                    ReciboPropiedades("Pagado", "$${String.format(Locale.getDefault(), "%.2f", pagado)}")
                    Divider(
                        color = Color.Gray.copy(alpha = 0.3f),
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                    ReciboPropiedades(
                        "Cambio",
                        "$${String.format(Locale.getDefault(), "%.2f", cambio.toDoubleOrNull() ?: 0.0)}",
                        Color(0xFF00FF9D)
                    )
                }
            }
        },
        confirmarTexto = "Cerrar",
        confirmar = onCerrar,
        cancelar = null
    )
}

@Composable
fun ReciboPropiedades(label: String, valor: String, color: Color = Color.White) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, color = Color.White.copy(alpha = 0.7f), fontSize = 14.sp)
        Text(valor, color = color, fontSize = 16.sp, fontWeight = FontWeight.Bold)
    }
    Spacer(Modifier.height(4.dp))
}

@Composable
fun BasePopup(
    titulo: String,
    contenido: @Composable ColumnScope.() -> Unit,
    confirmarTexto: String = "Confirmar",
    confirmar: () -> Unit,
    cancelar: (() -> Unit)?
) {
    AlertDialog(
        onDismissRequest = cancelar ?: {},
        containerColor = Color(0xFF1C1B29),
        shape = RoundedCornerShape(22.dp),
        title = {
            Text(
                titulo,
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column { contenido() }
        },
        confirmButton = {
            Button(
                onClick = confirmar,
                colors = ButtonDefaults.buttonColors(Color(0xFF7C4DFF)),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text(confirmarTexto, color = Color.White)
            }
        },
        dismissButton = cancelar?.let {
            {
                TextButton(onClick = it) {
                    Text("Cancelar", color = Color.Gray)
                }
            }
        }
    )
}

@Composable
fun CajaInput(
    value: String,
    label: String,
    onChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onChange,
        label = { Text(label, color = Color(0xFFB388FF)) },
        singleLine = true,
        textStyle = LocalTextStyle.current.copy(
            color = Color.White,
            fontSize = 18.sp
        ),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFF7C4DFF),
            unfocusedBorderColor = Color.Gray,
            focusedLabelColor = Color(0xFFB388FF),
            unfocusedLabelColor = Color(0xFFB388FF),
            cursorColor = Color.White
        ),
        modifier = Modifier.fillMaxWidth()
    )
}
