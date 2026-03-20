package com.example.myapplication.features.auth

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.features.auth.presentation.AuthView
import com.example.myapplication.ui.theme.MyApplicationTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AuthActivity : ComponentActivity() {
    private val authViewModel: AuthViewModel by viewModels()

    suspend fun redirectIfNeed() {
        if (authViewModel.isAuthorized()) {
            authViewModel.redirect(this)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        var keepSplashScreen = true

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        installSplashScreen().apply {
            setKeepOnScreenCondition { keepSplashScreen }
        }

        lifecycleScope.launch {
            delay(1000)
            keepSplashScreen = false

            redirectIfNeed()
        }

        setContent {
            MyApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AuthView(authViewModel = authViewModel, padding = innerPadding)
                }
            }
        }
    }
}