package com.example.myapplication.utils.converters

import androidx.room.TypeConverter
import com.example.myapplication.data.models.ProductRating
import com.google.gson.Gson

class ProductRatingConverter {
    private val gson: Gson
        get() = Gson()

    @TypeConverter
    fun fromProductRatingToJson(productRating: ProductRating): String {
        return gson.toJson(productRating)
    }

    @TypeConverter
    fun fromJsonToProductRating(json: String): ProductRating {
        return gson.fromJson(json, ProductRating::class.java)
    }
}