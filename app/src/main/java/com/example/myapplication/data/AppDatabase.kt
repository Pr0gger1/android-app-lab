package com.example.myapplication.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.myapplication.data.dao.ProductDao
import com.example.myapplication.data.dao.UserDao
import com.example.myapplication.data.models.Product
import com.example.myapplication.data.models.User

@Database(entities = [User::class, Product::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getProductDao(): ProductDao
    abstract fun getUserDao(): UserDao
}