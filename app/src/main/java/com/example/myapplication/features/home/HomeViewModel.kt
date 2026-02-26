package com.example.myapplication.features.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.repositories.ProductRepository
import com.example.myapplication.features.home.states.ProductState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : ViewModel() {
    private val _productState = MutableStateFlow(ProductState(products = emptyList()))
    val productState = _productState.asStateFlow()

    fun loadProducts() {
        viewModelScope.launch {
            val response = productRepository.getAllProducts()

            Log.d("HomeViewModel[loadProducts]", "Response: $response")

            _productState.update {
                it.copy(products = response)
            }
        }
    }
}