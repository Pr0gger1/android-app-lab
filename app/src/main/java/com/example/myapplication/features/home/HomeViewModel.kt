package com.example.myapplication.features.home

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.datastore.UserDataStore
import com.example.myapplication.data.models.Product
import com.example.myapplication.data.models.enums.FetchState
import com.example.myapplication.data.repositories.ProductRepository
import com.example.myapplication.features.auth.AuthActivity
import com.example.myapplication.features.home.states.ProductState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val userDataStore: UserDataStore
) : ViewModel() {
    private val _productState = MutableStateFlow(ProductState(products = emptyList()))
    val productState = _productState.asStateFlow()

    fun loadProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            _productState.update {
                it.copy(state = FetchState.LOADING)
            }

            try {
                val response = productRepository.getAllProducts()
                Log.d("HomeViewModel[loadProducts]", "Response: $response")
                _productState.update {
                    it.copy(
                        products = getSortedProducts(response, true),
                        state = FetchState.SUCCESS
                    )
                }
            } catch (e: Exception) {
                Log.e("HomeViewModel[loadProducts]", "${e.message}")
                _productState.update { it.copy(state = FetchState.ERROR) }
            }
        }
    }

    suspend fun logout(context: Context) {
        userDataStore.clear()
        val intent = Intent(context, AuthActivity::class.java)
        context.startActivity(intent)
    }

    private fun getSortedProducts(products: List<Product>, asc: Boolean): List<Product> {
        return if (asc) {
            products.sortedBy { it.title }
        } else {
            products.sortedByDescending { it.title }
        }
    }

    fun sortProducts(asc: Boolean) {
        val (products) = productState.value

        _productState.update {
            it.copy(products = getSortedProducts(products, asc))
        }
    }
}