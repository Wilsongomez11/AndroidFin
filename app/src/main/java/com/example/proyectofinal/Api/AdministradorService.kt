package com.example.proyectofinal.Api

import com.example.proyectofinal.LoginRequest
import com.example.proyectofinal.Model.Administrador
import com.example.proyectofinal.Model.Mesero
import com.example.proyectofinal.Model.Pizzero
import com.example.proyectofinal.Model.Producto
import com.example.proyectofinal.Model.ProductoDTO
import com.example.proyectofinal.Model.Proveedor
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface AdministradorService {
    @POST("administrador/login")
    suspend fun login(@Body request: LoginRequest): Response<Administrador>

    @PUT("administrador/{id}")
    suspend fun actualizarAdministrador(@Path("id") id: Long, @Body administrador: Administrador): Response<Void>

    @GET("administrador")
    suspend fun getAdministradores(): Response<List<Administrador>>

    @DELETE("administrador/{id}")
    suspend fun eliminarAdministrador(@Path("id") id: Long): Response<Void>

    @GET("productos")
    suspend fun getProductos(): Response<List<Producto>>

    @POST("administrador")
    suspend fun insertarAdministrador(@Body admin: Administrador): Response<Administrador>

    @POST("meseros")
    suspend fun insertarMesero(@Body mesero: Mesero): Response<Mesero>

    @POST("pizzeros")
    suspend fun insertarPizzero(@Body pizzero: Pizzero): Response<Pizzero>

    @POST("productos")
    suspend fun agregarProducto(@Body producto: Producto): Response<Void>

    @POST("productos")
    suspend fun agregarProducto(@Body producto: ProductoDTO): Response<Producto>

    @GET("proveedores")
    suspend fun getProveedores(): Response<List<Proveedor>>

    @POST("proveedores")
    suspend fun agregarProveedor(@Body proveedor: Proveedor): Response<Proveedor>
}