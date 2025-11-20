/**package com.example.proyectofinal.Api

import com.example.proyectofinal.Model.Insumo
import com.example.proyectofinal.Model.InsumoDTO
import retrofit2.Response
import retrofit2.http.*

interface InsumoService {

    @GET("insumos")
    suspend fun getInsumos(): Response<List<Insumo>>

    @POST("insumos")
    suspend fun agregarInsumo(@Body dto: InsumoDTO): Response<Insumo>

    @GET("insumos/bajo-stock")
    suspend fun getInsumosBajoStock(): Response<List<Insumo>>

    @PUT("insumos/{id}")
    suspend fun actualizarInsumo(@Path("id") id: Long, @Body dto: InsumoDTO): Response<Void>


    @GET("insumos/{id}")
    suspend fun getInsumoById(@Path("id") id: Long): Response<Insumo>

    @POST("insumos")
    suspend fun agregarInsumo(@Body insumo: Insumo): Response<Insumo>

    @PUT("insumos/{id}")
    suspend fun actualizarInsumo(
        @Path("id") id: Long,
        @Body insumo: Insumo
    ): Response<Insumo>

    @DELETE("insumos/{id}")
    suspend fun eliminarInsumo(@Path("id") id: Long): Response<Void>

}
**/