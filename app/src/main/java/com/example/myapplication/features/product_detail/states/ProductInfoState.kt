package com.example.myapplication.features.product_detail.states

import com.example.myapplication.data.models.Product
import com.example.myapplication.data.models.enums.FetchState

data class ProductInfoState(
    val product: Product? = null,
    val fetchState: FetchState = FetchState.INITIAL
)