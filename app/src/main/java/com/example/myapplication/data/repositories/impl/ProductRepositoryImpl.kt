package com.example.myapplication.data.repositories.impl

import com.example.myapplication.data.AppDatabase
import com.example.myapplication.data.api.ProductService
import com.example.myapplication.data.models.Product
import com.example.myapplication.data.repositories.ProductRepository
import java.util.Date
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val productService: ProductService,
    database: AppDatabase
) : ProductRepository {
    private val productDao = database.getProductDao()
    private val CACHE_TIME = 1000 * 60 * 1
    private var lastUpdateTime: Date? = null

    override suspend fun getAllProducts(): List<Product> {
        val cachedData = productDao.getAll()

        if (isCacheValid()) {
            return cachedData
        } else {
            val response = productService.getAllProducts()

            if (response.isSuccessful) {
                productDao.updateAll(response.body() ?: emptyList())
                lastUpdateTime = Date()

                return response.body() ?: emptyList()
            } else return cachedData
        }
    }

    override suspend fun getProduct(id: Int): Product? {
        val response = productService.getProduct(id)

        if (response.isSuccessful) {
            productDao.update(response.body() ?: return null)
            lastUpdateTime = Date()

            return response.body()
        }

        return null
    }

    private fun isCacheValid(): Boolean {
        return lastUpdateTime?.let { time ->
            val currentTime = Date().time

            return currentTime - time.time < CACHE_TIME
        } ?: false
    }
}