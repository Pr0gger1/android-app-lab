package com.example.myapplication.features.product_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.models.enums.FetchState
import com.example.myapplication.data.repositories.ProductRepository
import com.example.myapplication.features.product_detail.states.ProductInfoState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val repository: ProductRepository
) : ViewModel() {
    private val _productInfoState =
        MutableStateFlow(ProductInfoState())

    val productState = _productInfoState.asStateFlow()

    fun loadProduct(id: Int?) {
        viewModelScope.launch(Dispatchers.IO) {
            if (id == null) {
                return@launch
            }

            _productInfoState.update { it.copy(fetchState = FetchState.LOADING) }

            val response = repository.getProduct(id)

            _productInfoState.update {
                it.copy(
                    fetchState = FetchState.SUCCESS,
                    product = response
                )
            }
        }
    }
}