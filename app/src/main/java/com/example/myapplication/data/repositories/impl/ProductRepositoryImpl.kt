package com.example.myapplication.data.repositories.impl

import com.example.myapplication.data.api.ProductService
import com.example.myapplication.data.models.Product
import com.example.myapplication.data.repositories.ProductRepository
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val productService: ProductService
) : ProductRepository {
    override suspend fun getAllProducts(): List<Product> {
        val response = productService.getAllProducts()

        if (response.isSuccessful) {
            return response.body() ?: emptyList()
        }

        return emptyList()
    }
}