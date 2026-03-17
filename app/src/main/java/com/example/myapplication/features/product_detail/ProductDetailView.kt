package com.example.myapplication.features.product_detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import com.example.myapplication.features.product_detail.widgets.StarRatingBar
import com.example.myapplication.utils.hooks.rememberLandscapeOrientationState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailView(modifier: Modifier = Modifier, navController: NavHostController, id: Int?) {
    val viewModel: ProductDetailViewModel = hiltViewModel()
    val state by viewModel.productState.collectAsState()
    val product = state.product
    val scrollState = rememberScrollState()
    val isHorizontalOrientation = rememberLandscapeOrientationState()

    LaunchedEffect(true) {
        viewModel.loadProduct(id)
    }

    Scaffold(modifier = modifier.fillMaxSize(), topBar = {
        TopAppBar(
            navigationIcon = {
                IconButton(onClick = navController::popBackStack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Localized description"
                    )
                }
            },
            title = { Text(product?.title ?: "Товар", fontSize = 16.sp) }
        )
    }) { padding ->
        if (product == null) {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Нет информации")
            }
        } else
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        top = padding.calculateTopPadding(),
                        bottom = padding.calculateBottomPadding(),
                        start = 12.dp,
                        end = 12.dp
                    )
            ) {
                Column(
                    modifier = Modifier
                        .padding(top = 10.dp)
                        .verticalScroll(scrollState),
                    verticalArrangement = Arrangement.spacedBy(24.dp),
                ) {
                    AsyncImage(
                        model = product.image, contentDescription = product.title,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(if (isHorizontalOrientation) 150.dp else 300.dp),
                        contentScale = ContentScale.Fit
                    )

                    Text(
                        "${product.price}$",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.End,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Column(
                        modifier = Modifier,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text("Категория: ${product.category}")
                        Text(
                            product.description,
                            fontSize = 18.sp,
                            textAlign = TextAlign.Justify
                        )

                        Column(modifier = Modifier.fillMaxWidth()) {
                            Text("Отзывы: ${product.rating.count}")

                            StarRatingBar(rating = product.rating.rate)
                        }
                    }
                }
            }
    }
}