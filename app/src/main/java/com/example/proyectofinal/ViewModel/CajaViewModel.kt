package com.example.proyectofinal.ViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectofinal.Api.ApiClient
import com.example.proyectofinal.Model.Caja
import com.example.proyectofinal.Model.DevolucionRequest
import com.example.proyectofinal.Model.MovimientoCaja
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CajaViewModel : ViewModel() {

    private val _movimientos = MutableStateFlow<List<MovimientoCaja>>(emptyList())
    val movimientos: StateFlow<List<MovimientoCaja>> = _movimientos

    private val _caja = MutableStateFlow<Caja?>(null)
    val caja = _caja.asStateFlow()

    private val _balance = MutableStateFlow(0.0)
    val balance: StateFlow<Double> = _balance

    private val _resultadoPago = MutableStateFlow<Map<String, Any>?>(null)
    val resultadoPago: StateFlow<Map<String, Any>?> = _resultadoPago

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading


    fun cargarMovimientos() {
        viewModelScope.launch {
            try {
                _isLoading.value = true

                val response = ApiClient.apiService.getMovimientos()

                if (response.isSuccessful) {
                    _movimientos.value = response.body() ?: emptyList()
                    delay(50)
                    calcularBalanceLocal()
                } else {
                    _error.value = "Error: ${response.code()}"
                }

            } catch (e: Exception) {
                _error.value = "Error: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }


    fun registrarPago(pedidoId: Long, monto: Double, adminId: Long) {
        viewModelScope.launch {
            try {
                _isLoading.value = true

                val result = ApiClient.apiService.registrarPago(
                    pedidoId = pedidoId,
                    montoPagado = monto,
                    adminId = adminId
                )

                _resultadoPago.value = result

                cargarMovimientos()
                cargarCajaDelDia()

            } catch (e: Exception) {
                _error.value = "Error: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun registrarDevolucion(
        pedidoId: Long,
        monto: Double,
        adminId: Long,
        cantidades: Map<Long, Int>
    ) {
        viewModelScope.launch {
            try {
                _isLoading.value = true

                val request = DevolucionRequest(
                    monto = monto,
                    reponerStock = true,
                    cantidades = cantidades
                )

                val response = ApiClient.apiService.devolverPedido(
                    id = pedidoId,
                    devolucion = request
                )

                if (response.isSuccessful) {
                    cargarMovimientos()
                    cargarCajaDelDia()
                    _error.value = null
                } else {
                    _error.value = "Error: ${response.code()}"
                }

            } catch (e: Exception) {
                _error.value = "Error: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun calcularBalanceLocal() {
        try {
            val hoy = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
            val movimientosHoy = _movimientos.value.filter {
                it.fecha?.take(10) == hoy
            }

            _balance.value = movimientosHoy.sumOf { it.monto }

        } catch (e: Exception) {
            _error.value = "Error calculando balance"
        }
    }

    fun cargarCajaDelDia() {
        viewModelScope.launch {
            try {
                val response = ApiClient.apiService.getCajaDelDia()
                if (response.isSuccessful) {
                    val cajaResp = response.body()
                    _caja.value = cajaResp
                    _balance.value = cajaResp?.balance ?: 0.0
                } else {
                    calcularBalanceLocal()
                }
            } catch (e: Exception) {
                calcularBalanceLocal()
            }
        }
    }

    fun limpiarResultadoPago() {
        _resultadoPago.value = null
    }

    fun limpiarError() {
        _error.value = null
    }

    fun recargarTodo() {
        viewModelScope.launch {
            cargarMovimientos()
            cargarCajaDelDia()
        }
    }
}
