package com.example.myapplication.features.home.presentation

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.R
import com.example.myapplication.core.shared.CircleLoader
import com.example.myapplication.data.models.enums.FetchState
import com.example.myapplication.features.home.HomeViewModel
import com.example.myapplication.features.home.states.ProductState
import com.example.myapplication.features.home.widgets.ProductCard
import com.example.myapplication.features.home.widgets.TopBar
import kotlinx.coroutines.launch

@Composable
fun HomeView(modifier: Modifier = Modifier, homeViewModel: HomeViewModel) {
    val configuration = LocalConfiguration.current
    val coroutineScope = rememberCoroutineScope()
    val lazyGridState = rememberLazyGridState()
    val productState: ProductState by homeViewModel.productState.collectAsState()
    val isHorizontalOrientation = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    val columns by remember {
        derivedStateOf {
            when {
                isHorizontalOrientation -> 2
                else -> 1
            }
        }
    }

    LaunchedEffect(true) {
        homeViewModel.loadProducts()
    }

    fun onRefresh() {
        coroutineScope.launch { homeViewModel.loadProducts() }
    }

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            TopBar(homeViewModel, lazyGridState)

            when (productState.state) {
                FetchState.LOADING -> {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) { CircleLoader() }
                }

                FetchState.SUCCESS -> {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(columns),
                        state = lazyGridState,
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(productState.products, key = { it.id }) { product ->
                            ProductCard(
                                product = product,
                                isHorizontalOrientation = isHorizontalOrientation
                            )
                        }
                    }
                }

                FetchState.ERROR -> {
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = stringResource(R.string.product_error_message),
                                style = TextStyle(fontSize = 28.sp, fontWeight = FontWeight.Bold)
                            )

                            Button(
                                onClick = ::onRefresh,
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(stringResource(R.string.refresh_page_label))
                            }
                        }
                    }
                }

                else -> {}
            }
        }
    }
}