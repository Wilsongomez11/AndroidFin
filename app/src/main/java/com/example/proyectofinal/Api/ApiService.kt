package com.example.proyectofinal

import com.example.proyectofinal.Model.Administrador
import com.example.proyectofinal.Model.Cliente
import com.example.proyectofinal.Model.Mesero
import com.example.proyectofinal.Model.Pizzero
import com.example.proyectofinal.Model.Producto
import com.example.proyectofinal.Model.Proveedor
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    @GET("administradores")
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
    suspend fun agregarProducto(@Body producto: Producto): Response<Void>

    @POST("/meseros")
    suspend fun agregarMesero(@Body mesero: Mesero): Response<Void>




    @POST("proveedores")
    suspend fun agregarProveedor(@Body proveedor: Proveedor): Response<Proveedor>

    @GET("proveedores")
    suspend fun getProveedores(): Response<List<Proveedor>>






}
