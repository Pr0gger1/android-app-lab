package com.example.myapplication.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.myapplication.data.models.Product

@Dao
interface ProductDao {
    @Query("SELECT * FROM products")
    fun getAll(): List<Product>

    @Update
    fun updateProduct(product: Product)

    @Query("SELECT * FROM products WHERE id = :id")
    fun findById(id: Int): Product?

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateAll(products: List<Product>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(product: Product)

    @Insert
    fun insertAll(products: List<Product>)

    @Delete
    fun deleteAll(product: Product)
}