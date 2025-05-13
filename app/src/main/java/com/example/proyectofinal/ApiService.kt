package com.example.proyectofinal

import com.example.proyectofinal.Model.Administrador
import com.example.proyectofinal.Model.Cliente
import com.example.proyectofinal.Model.Mesero
import com.example.proyectofinal.Model.Pizzero
import com.example.proyectofinal.Model.Producto
import com.example.proyectofinal.Model.Proveedor
import retrofit2.http.GET

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

    @GET("proveedores")
    suspend fun getProveedores(): List<Proveedor>

}