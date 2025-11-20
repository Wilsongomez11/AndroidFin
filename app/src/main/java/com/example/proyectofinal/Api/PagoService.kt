/**package com.example.proyectofinal.Api

import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Query

interface PagoService {

    @POST("pagos/registrar")
    suspend fun registrarPago(
        @Query("pedidoId") pedidoId: Long,
        @Query("montoPagado") montoPagado: Double,
        @Query("adminId") adminId: Long
    ): Response<Map<String, Any>>
}
**/