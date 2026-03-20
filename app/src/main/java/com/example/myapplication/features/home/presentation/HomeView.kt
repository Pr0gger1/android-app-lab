package com.example.myapplication.features.home.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.myapplication.R
import com.example.myapplication.Route
import com.example.myapplication.core.shared.CircleLoader
import com.example.myapplication.data.models.Product
import com.example.myapplication.data.models.enums.FetchState
import com.example.myapplication.features.home.HomeViewModel
import com.example.myapplication.features.home.states.ProductState
import com.example.myapplication.features.home.widgets.ProductBottomSheet
import com.example.myapplication.features.home.widgets.ProductCard
import com.example.myapplication.features.home.widgets.TopBar
import com.example.myapplication.utils.hooks.rememberLandscapeOrientationState
import kotlinx.coroutines.launch

@Composable
fun HomeView(
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel,
    navController: NavHostController
) {
    val coroutineScope = rememberCoroutineScope()
    val lazyGridState = rememberLazyGridState()
    val productState: ProductState by homeViewModel.productState.collectAsState()
    val isHorizontalOrientation = rememberLandscapeOrientationState()

    val columns by remember {
        derivedStateOf {
            when {
                isHorizontalOrientation -> 2
                else -> 1
            }
        }
    }

    val isFloatingButtonVisible by remember {
        derivedStateOf { lazyGridState.firstVisibleItemIndex > 0 }
    }

    var activeProduct by remember { mutableStateOf<Product?>(null) }

    LaunchedEffect(true) {
        homeViewModel.loadProducts()
    }

    fun scrollToTop() {
        coroutineScope.launch { lazyGridState.animateScrollToItem(0) }
    }

    fun onCardClick(product: Product) {
        navController.navigate("${Route.PRODUCT}/${product.id}")
    }

    Scaffold(modifier = modifier.fillMaxSize(), floatingActionButton = {
        AnimatedVisibility(
            visible = isFloatingButtonVisible,
            enter = scaleIn(),
            exit = scaleOut()
        ) {
            FloatingActionButton(onClick = ::scrollToTop, shape = CircleShape) {
                Icon(Icons.Filled.KeyboardArrowUp, contentDescription = "Refresh")
            }
        }
    }) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = 12.dp,
                    end = 12.dp,
                    top = padding.calculateTopPadding(),
                ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            TopBar(homeViewModel, lazyGridState)
            ProductBottomSheet(
                product = activeProduct,
                showBottomSheet = activeProduct != null,
                onDismiss = { activeProduct = null }
            )

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
                        itemsIndexed(
                            productState.products,
                            key = { _, item -> item.id }) { index, product ->
                            ProductCard(
                                onClick = { onCardClick(product) },
                                onLongPress = { activeProduct = product },
                                product = product,
                                isHorizontalOrientation = isHorizontalOrientation,
                                modifier = Modifier.padding(
                                    bottom = if (index == productState.products.size - 1) 12.dp
                                    else 0.dp
                                )
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
                                onClick = { coroutineScope.launch { homeViewModel.loadProducts() } },
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