package com.example.proyectofinal.Api

import com.example.proyectofinal.Model.Pedido
import com.example.proyectofinal.Model.ProductoInsumo
import retrofit2.Response
import retrofit2.http.*

interface ProductoInsumoService {

    // Obtiene todos los insumos asociados a un producto
    @GET("producto-insumos/{productoId}")
    suspend fun getRelacionesPorProducto(
        @Path("productoId") productoId: Long
    ): Response<List<ProductoInsumo>>

    // Crea una nueva relación entre producto e insumo
    @POST("producto-insumos")
    suspend fun agregarRelacion(
        @Body productoInsumo: ProductoInsumo
    ): Response<ProductoInsumo>

    @PUT("pedidos/{id}")
    suspend fun actualizarPedido(
        @Path("id") id: Long,
        @Body pedido: Pedido
    ): Response<Pedido>

}
