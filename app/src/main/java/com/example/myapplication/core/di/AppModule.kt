package com.example.myapplication.core.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module(
    includes = [
        RetrofitModule::class,
        ApiModule::class,
        RepositoryModule::class,
        DataStoreModule::class
    ]
)
@InstallIn(SingletonComponent::class)
object AppModule