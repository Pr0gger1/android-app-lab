package com.example.myapplication.features.home.states

import com.example.myapplication.data.models.Product

data class ProductState(
    val products: List<Product>,
    val isLoading: Boolean = true
)
