package com.example.myapplication.core.di

import com.example.myapplication.data.api.ProductService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {
    @Provides
    @Singleton
    fun providesProductService(retrofit: Retrofit) =
        retrofit.create(ProductService::class.java)
}