package com.example.myapplication.data.api

import com.example.myapplication.data.models.Product
import retrofit2.Response
import retrofit2.http.GET

interface ProductService {
    @GET("products")
    suspend fun getAllProducts(): Response<List<Product>>
}