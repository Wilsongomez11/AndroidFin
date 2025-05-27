package com.example.proyectofinal

import com.example.proyectofinal.Model.Administrador
import com.example.proyectofinal.Model.Cliente
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


interface ApiService {

    @GET("administrador")
    suspend fun getAdministradores(): List<Administrador>

    @GET("clientes")
    suspend fun getClientes(): List<Cliente>

    @GET("meseros")
    suspend fun getMeseros(): List<Mesero>

    @GET("pizeros")
    suspend fun getPizeros(): List<Pizzero>

    @GET("productos")
    suspend fun getProductos(): List<Producto>

    @POST("productos")
    suspend fun agregarProducto(@Body productoDTO: ProductoDTO): Response<Producto>

    @POST("meseros")
    suspend fun insertarMesero(@Body mesero: Mesero): Response<Mesero>

    @POST("pizzeros")
    suspend fun insertarPizzero(@Body pizzero: Pizzero): Response<Pizzero>

    @POST("administrador")
    suspend fun insertarAdministrador(@Body admin: Administrador): Response<Administrador>

    @GET("proveedores")
    suspend fun getProveedores(): Response<List<Proveedor>>

    @POST("proveedores")
    suspend fun agregarProveedor(@Body proveedor: Proveedor): Response<Proveedor>

    @DELETE("administrador/{id}")
    suspend fun eliminarAdministrador(@Path("id") id: Long): Response<Void>

    @PUT("administrador/{id}")
    suspend fun actualizarAdministrador(@Path("id") id: Long, @Body administrador: Administrador): Response<Administrador>
}
