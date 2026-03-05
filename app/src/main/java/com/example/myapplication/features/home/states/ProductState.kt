package com.example.myapplication.features.home.states

import com.example.myapplication.data.models.Product
import com.example.myapplication.data.models.enums.FetchState

data class ProductState(
    val products: List<Product>,
    val state: FetchState = FetchState.INITIAL
)
