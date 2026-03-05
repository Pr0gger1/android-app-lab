package com.example.myapplication.features.home.widgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.myapplication.features.home.HomeViewModel
import kotlinx.coroutines.launch

@Composable
fun TopBar(homeViewModel: HomeViewModel, lazyGridState: LazyGridState) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    var isAscendingSort by remember { mutableStateOf(true) }

    fun onSortClick() {
        isAscendingSort = !isAscendingSort
        homeViewModel.sortProducts(isAscendingSort)

        coroutineScope.launch {
            lazyGridState.animateScrollToItem(0)
        }
    }

    fun onExitClick() {
        coroutineScope.launch {
            homeViewModel.logout(context)
        }
    }


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
}
