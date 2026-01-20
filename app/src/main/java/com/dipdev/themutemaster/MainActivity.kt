package com.dipdev.themutemaster

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.dipdev.themutemaster.ui.MainViewModel
import com.dipdev.themutemaster.ui.navigation.AppNavHost
import com.dipdev.themutemaster.ui.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        // 1. Wait for ViewModel decision
        splashScreen.setKeepOnScreenCondition {
            mainViewModel.isLoading.value
        }

        enableEdgeToEdge()
        setContent {
            AppTheme {
                val startDest by mainViewModel.startDestination.collectAsState()

                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    // 2. Launch only when ready
                    if (!mainViewModel.isLoading.collectAsState().value && startDest != null) {
                        AppNavHost(startDestination = startDest!!)
                    }
                }
            }
        }
    }
}

