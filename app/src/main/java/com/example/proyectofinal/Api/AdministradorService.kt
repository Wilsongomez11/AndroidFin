package com.example.proyectofinal.Api

import com.example.proyectofinal.LoginRequest
import com.example.proyectofinal.LoginResponse
import com.example.proyectofinal.Model.*
import retrofit2.Response
import retrofit2.http.*

interface AdministradorService {

    // --- LOGIN ---
    @POST("administrador/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("meseros/login")
    suspend fun loginMesero(@Body request: LoginRequest): Response<Mesero>

    @POST("pizzeros/login")
    suspend fun loginPizzero(@Body request: LoginRequest): Response<Pizzero>

    // --- PEDIDOS ---
    @GET("pedidos")
    suspend fun getPedidos(): Response<List<Pedido>>

    @GET("pedidos/{id}")
    suspend fun getPedidoById(@Path("id") id: Long): Response<Pedido>

    @POST("pedidos")
    suspend fun crearPedido(@Body pedido: Pedido): Response<Pedido>

    @DELETE("pedidos/{id}")
    suspend fun eliminarPedido(@Path("id") id: Long): Response<Void>

    @PUT("pedidos/{id}/estado")
    suspend fun actualizarEstadoPedido(
        @Path("id") id: Long,
        @Query("estado") nuevoEstado: String
    ): Response<Void>


    // --- ADMINISTRADORES ---
    @GET("administrador")
    suspend fun getAdministradores(): Response<List<Administrador>>

    @POST("administrador")
    suspend fun insertarAdministrador(@Body admin: Administrador): Response<Administrador>

    @PUT("administrador/{id}")
    suspend fun actualizarAdministrador(
        @Path("id") id: Long,
        @Body administrador: Administrador
    ): Response<Administrador>

    @DELETE("administrador/{id}")
    suspend fun eliminarAdministrador(@Path("id") id: Long): Response<Void>


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

    @POST("producto-insumo")
    suspend fun agregarRelacion(@Body relacion: ProductoInsumoDTO): Response<Void>

    // --- PRODUCTOS ---
    @GET("productos")
    suspend fun getProductos(): Response<List<Producto>>

    @POST("productos")
    suspend fun agregarProducto(@Body producto: ProductoDTO): Response<Producto>

    @PUT("pedidos/{id}")
    suspend fun actualizarPedido(
        @Path("id") id: Long,
        @Body pedido: Pedido
    ): Response<Pedido>

    @GET("/productos/{id}")
    suspend fun getProductoById(@Path("id") id: Long): Response<Producto>

    @Headers("Content-Type: application/json")
    @PUT("productos/{id}")
    suspend fun actualizarProducto(
        @Path("id") id: Long,
        @Body producto: ProductoDTO
    ): Response<Producto>

    @PUT("productos/fabricar/{id}/{cantidad}")
    suspend fun fabricarProducto(
        @Path("id") id: Long,
        @Path("cantidad") cantidad: Int
    ): Response<Void>

    @DELETE("productos/{id}")
    suspend fun eliminarProducto(@Path("id") id: Long): Response<Void>

    // Obtener insumos asociados a un producto
    @GET("producto-insumos/{productoId}")
    suspend fun getInsumosByProducto(
        @Path("productoId") productoId: Long
    ): Response<List<ProductoInsumo>>

    // Crear una nueva relación producto-insumo
    @POST("producto-insumos")
    suspend fun crearProductoInsumo(
        @Body productoInsumo: ProductoInsumo
    ): Response<ProductoInsumo>

    // ---  PROVEEDORES ---
    @GET("proveedores")
    suspend fun getProveedores(): Response<List<Proveedor>>

    @POST("proveedores")
    suspend fun agregarProveedor(@Body proveedor: Proveedor): Response<Proveedor>

    // --- INSUMOS ---
    @GET("insumos")
    suspend fun getInsumos(): Response<List<Insumo>>

    @POST("insumos")
    suspend fun agregarInsumo(@Body insumo: InsumoDTO): Response<Insumo>

    @DELETE("insumos/{id}")
    suspend fun eliminarInsumo(@Path("id") id: Long): Response<Void>

    @PUT("insumos/{id}")
    suspend fun actualizarInsumo(
        @Path("id") id: Long,
        @Body insumo: InsumoDTO
    ): Response<Insumo>

    @GET("insumos/bajo-stock")
    suspend fun getInsumosBajoStock(): Response<List<Insumo>>
}
