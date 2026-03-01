package com.example.myapplication.features.home.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.myapplication.core.shared.CircleLoader
import com.example.myapplication.features.home.HomeViewModel
import com.example.myapplication.features.home.states.ProductState
import kotlinx.coroutines.launch

@Composable
fun HomeView(modifier: Modifier = Modifier, homeViewModel: HomeViewModel) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val lazyListState = rememberLazyListState()
    var isAscendingSort by remember { mutableStateOf(true) }
    val productState: ProductState by homeViewModel.productState.collectAsState()

    LaunchedEffect(true) {
        homeViewModel.loadProducts()
    }

    fun onSortClick() {
        isAscendingSort = !isAscendingSort
        homeViewModel.sortProducts(isAscendingSort)

        coroutineScope.launch {
            lazyListState.animateScrollToItem(0)
        }
    }

    fun onExitClick() {
        coroutineScope.launch {
            homeViewModel.logout(context)
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier.padding(horizontal = 12.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Товары",
                        style = TextStyle(
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )

                    IconButton(
                        onClick = ::onSortClick,
                        colors = IconButtonDefaults
                            .iconButtonColors(
                                containerColor = MaterialTheme
                                    .colorScheme
                                    .primary
                            )
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.Sort,
                            contentDescription = "Сортировать по алфавиту",
                            tint = Color.White
                        )
                    }
                }

                Button(
                    onClick = ::onExitClick,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Выйти", style = TextStyle(fontSize = 16.sp))
                }
            }

            if (productState.isLoading) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    CircleLoader()
                }
            } else
                LazyColumn(
                    state = lazyListState,
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(productState.products, key = { it.id }) { product ->
                        Card(
                            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp)
                            ) {
                                Column(
                                    verticalArrangement = Arrangement.spacedBy(24.dp)
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .aspectRatio(16f / 9f)
                                    ) {
                                        AsyncImage(
                                            model = product.image,
                                            contentDescription = product.title,
                                            modifier = Modifier.fillMaxSize(),
                                        )
                                    }

                                    Text(
                                        product.title,
                                        color = Color.Black,
                                        style = TextStyle(
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.Bold
                                        ),
                                    )

                                    Text(
                                        "$${product.price}",
                                        style = TextStyle(
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    )
                                }

                            }
                        }
                    }
                }
        }
    }
}