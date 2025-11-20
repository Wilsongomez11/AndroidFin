package com.example.proyectofinal

import com.example.proyectofinal.Model.*
import retrofit2.Response
import retrofit2.http.*
import retrofit2.http.Path

interface ApiService {

    // --- ADMINISTRADORES ---
    @GET("administrador")
    suspend fun getAdministradores(): List<Administrador>

    @POST("administrador")
    suspend fun insertarAdministrador(@Body admin: Administrador): Response<Administrador>

    @PUT("administrador/{id}")
    suspend fun actualizarAdministrador(
        @Path("id") id: Long,
        @Body administrador: Administrador
    ): Response<Administrador>

    @DELETE("administrador/{id}")
    suspend fun eliminarAdministrador(@Path("id") id: Long): Response<Void>


    // --- CLIENTES ---
    @GET("clientes")
    suspend fun getClientes(): List<Cliente>


    // --- PRODUCTOS ---
    @GET("productos")
    suspend fun getProductos(): List<Producto>

    @POST("productos")
    suspend fun agregarProducto(@Body productoDTO: ProductoDTO): Response<Producto>

    @PUT("productos/{id}")
    suspend fun actualizarProducto(
        @Path("id") id: Long,
        @Body producto: ProductoDTO
    ): Response<Void>


    // --- PROVEEDORES ---
    @GET("proveedores")
    suspend fun getProveedores(): Response<List<Proveedor>>

    @POST("proveedores")
    suspend fun agregarProveedor(@Body proveedor: Proveedor): Response<Proveedor>


    // --- PEDIDOS ---
    @GET("pedidos")
    suspend fun getPedidos(): Response<List<Pedido>>

    @GET("pedidos/{id}")
    suspend fun getPedidoById(@Path("id") id: Long): Response<Pedido>

    @POST("pedidos")
    suspend fun crearPedido(@Body pedido: Pedido): Response<Pedido>

    @PUT("pedidos/{id}")
    suspend fun actualizarPedido(
        @Path("id") id: Long,
        @Body pedido: Pedido
    ): Response<Pedido>

    @PUT("pedidos/{id}/estado")
    suspend fun actualizarEstadoPedido(
        @Path("id") id: Long,
        @Query("estado") estado: String
    ): Response<Void>

    @DELETE("pedidos/{id}")
    suspend fun eliminarPedido(@Path("id") id: Long): Response<Void>

    @PUT("pedidos/{id}/devolver")
    suspend fun devolverPedido(
        @Path("id") id: Long,
        @Body devolucion: DevolucionRequest
    ): Response<Void>


    @GET("pedidos/mesas/estado")
    suspend fun getEstadoMesas(): Response<Map<Int, String>>


    @GET("pedidos/mesas/ocupadas")
    suspend fun getMesasOcupadas(): Response<List<Int>>

    // --- CAJA ---
    @GET("caja/hoy")
    suspend fun getCajaDelDia(): Response<Caja>

    @GET("movimientos")
    suspend fun getMovimientos(): Response<List<MovimientoCaja>>


    // --- PAGOS ---
    @POST("pagos/registrar")
    suspend fun registrarPago(
        @Query("pedidoId") pedidoId: Long,
        @Query("montoPagado") montoPagado: Double,
        @Query("adminId") adminId: Long
    ): Map<String, Any>

    // ---INSUMOS---
    @GET("insumos")
    suspend fun getInsumos(): Response<List<Insumo>>

    @GET("insumos/{id}")
    suspend fun getInsumoById(@Path("id") id: Long): Response<Insumo>

    @GET("insumos/bajo-stock")
    suspend fun getInsumosBajoStock(): Response<List<Insumo>>

    @POST("insumos")
    suspend fun agregarInsumo(@Body dto: InsumoDTO): Response<Insumo>

    @PUT("insumos/{id}")
    suspend fun actualizarInsumo(@Path("id") id: Long, @Body dto: InsumoDTO): Response<Void>  // ✅ Solo uno

    @DELETE("insumos/{id}")
    suspend fun eliminarInsumo(@Path("id") id: Long): Response<Void>

    // ---PRODUCTOINSUMO---
    @GET("producto-insumos/{productoId}")
    suspend fun getRelacionesPorProducto(
        @Path("productoId") productoId: Long
    ): Response<List<ProductoInsumo>>

    @POST("producto-insumos")
    suspend fun agregarRelacion(
        @Body productoInsumo: ProductoInsumo
    ): Response<ProductoInsumo>

    // --- MESEROS ---
    @GET("meseros")
    suspend fun getMeseros(): Response<List<Mesero>>

    @POST("meseros/registrar")
    suspend fun insertarMesero(@Body mesero: Mesero): Response<Mesero>

    @PUT("meseros/{id}")
    suspend fun actualizarMesero(
        @Path("id") id: Long,
        @Body mesero: Mesero
    ): Response<Mesero>

    @DELETE("meseros/{id}")
    suspend fun eliminarMesero(@Path("id") id: Long): Response<Void>


    // --- PIZZEROS ---
    @GET("pizzeros")
    suspend fun getPizzeros(): Response<List<Pizzero>>

    @POST("pizzeros/registrar")
    suspend fun insertarPizzero(@Body pizzero: Pizzero): Response<Pizzero>

    @PUT("pizzeros/{id}")
    suspend fun actualizarPizzero(
        @Path("id") id: Long,
        @Body pizzero: Pizzero
    ): Response<Pizzero>

    @DELETE("pizzeros/{id}")
    suspend fun eliminarPizzero(@Path("id") id: Long): Response<Void>
}
