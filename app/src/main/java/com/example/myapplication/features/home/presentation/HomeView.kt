package com.example.myapplication.features.home.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
//import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myapplication.features.home.HomeViewModel
import com.example.myapplication.features.home.states.ProductState

@Composable
fun HomeView(modifier: Modifier = Modifier, homeViewModel: HomeViewModel) {
    val productState: ProductState by homeViewModel.productState.collectAsState()

    LaunchedEffect(true) {
        homeViewModel.loadProducts()
    }

    Box(contentAlignment = Alignment.Center, modifier = modifier.fillMaxSize()) {
        Column() {
            Text(
                text = "Товары",
                style = TextStyle(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            )

            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(productState.products, key = { it.id }) { product ->
                    Card() {
                        Text(product.title)
                    }
                }
            }
        }
    }
}