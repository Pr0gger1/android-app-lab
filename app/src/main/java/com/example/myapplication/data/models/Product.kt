package com.example.myapplication.data.models

data class ProductRating(
    val rate: Float,
    val count: Int
)

data class Product(
    val id: Int,
    val title: String,
    val price: Double,
    val category: String,
    val image: String,
    val rating: ProductRating
)
