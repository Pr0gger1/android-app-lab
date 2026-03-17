package com.example.myapplication.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.myapplication.utils.converters.ProductRatingConverter
import java.io.Serializable

data class ProductRating(
    val rate: Float,
    val count: Int
) : Serializable

@Entity(tableName = "products")
@TypeConverters(ProductRatingConverter::class)
data class Product(
    @PrimaryKey
    var id: Int,
    var title: String,
    var price: Double,
    var description: String,
    var category: String,
    var image: String,
    val rating: ProductRating,
    var updatedAt: Long?
)
