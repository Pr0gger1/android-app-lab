package com.example.myapplication.data.repositories

import com.example.myapplication.data.models.Product

interface ProductRepository {
    suspend fun getAllProducts(): List<Product>
    suspend fun getProduct(id: Int): Product?
}