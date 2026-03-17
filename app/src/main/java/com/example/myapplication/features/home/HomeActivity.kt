package com.example.myapplication.features.home

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.myapplication.Route
import com.example.myapplication.features.home.presentation.HomeView
import com.example.myapplication.features.product_detail.ProductDetailView
import com.example.myapplication.ui.theme.MyApplicationTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : ComponentActivity() {
    private val homeViewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MyApplicationTheme {
                val navController = rememberNavController()

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(navController = navController, startDestination = Route.HOME) {
                        composable(route = Route.HOME) {
                            HomeView(
                                modifier = Modifier.padding(innerPadding),
                                homeViewModel = homeViewModel,
                                navController = navController
                            )
                        }

                        composable(
                            route = "${Route.PRODUCT}/{id}",
                            arguments = listOf(navArgument("id") { NavType.StringType })
                        ) { entry ->
                            val arg: String? = entry.arguments?.getString("id")
                            val id =
                                arg?.runCatching { arg.toInt() }?.getOrNull()

                            ProductDetailView(
                                modifier = Modifier.padding(innerPadding),
                                id = id,
                                navController = navController
                            )
                        }
                    }
                }
            }
        }
    }
}