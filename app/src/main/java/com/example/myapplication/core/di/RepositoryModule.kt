package com.example.myapplication.core.di

import com.example.myapplication.data.api.ProductService
import com.example.myapplication.data.repositories.ProductRepository
import com.example.myapplication.data.repositories.impl.ProductRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun providesProductRepository(service: ProductService): ProductRepository =
        ProductRepositoryImpl(service)
}