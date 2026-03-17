package com.example.myapplication.utils.converters

import androidx.room.TypeConverter
import com.example.myapplication.data.models.ProductRating
import com.google.gson.Gson

class ProductRatingConverter : Converter<ProductRating> {
    private val gson: Gson
        get() = Gson()

    @TypeConverter
    override fun toString(data: ProductRating): String {
        return gson.toJson(data)
    }

    @TypeConverter
    override fun fromString(data: String): ProductRating {
        return gson.fromJson(data, ProductRating::class.java)
    }
}