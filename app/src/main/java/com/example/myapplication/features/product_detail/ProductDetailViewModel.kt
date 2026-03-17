package com.example.myapplication.features.product_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.models.Product
import com.example.myapplication.data.models.enums.FetchState
import com.example.myapplication.data.repositories.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProductState(
    val product: Product? = null,
    val fetchState: FetchState = FetchState.INITIAL
)

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val repository: ProductRepository
) : ViewModel() {
    private val _productState =
        MutableStateFlow(ProductState())

    val productState = _productState.asStateFlow()

    fun loadProduct(id: Int?) {
        viewModelScope.launch(Dispatchers.IO) {
            if (id == null) {
                return@launch
            }

            _productState.update { it.copy(fetchState = FetchState.LOADING) }

            val response = repository.getProduct(id)

            _productState.update { it.copy(fetchState = FetchState.SUCCESS, product = response) }
        }
    }
}