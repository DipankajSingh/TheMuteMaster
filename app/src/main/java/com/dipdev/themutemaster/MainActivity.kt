package com.dipdev.themutemaster

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.dipdev.themutemaster.ui.navigation.AppNavHost
import com.dipdev.themutemaster.ui.navigation.AppRoute
import com.dipdev.themutemaster.ui.screens.permissions.fglocation.ForegroundLocationAccess
import com.dipdev.themutemaster.ui.screens.permissions.fglocation.ForegroundLocationAccessViewModel
import com.dipdev.themutemaster.ui.screens.savedLocations.SavedLocationsScreen
import com.dipdev.themutemaster.ui.theme.AppTheme
import com.dipdev.themutemaster.utils.hasLocationPermission
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                val startDestination = remember {
                    if (hasLocationPermission()) {
                        AppRoute.MAIN_APP_CONTAINER
                    } else {
                        AppRoute.PERMISSION_FLOW
                    }
                }
                Surface(Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .statusBarsPadding()
                    .padding(10.dp)){
                    Scaffold {padVal->
                        AppNavHost(startDestination=startDestination,modifier = Modifier.padding(padVal))
                    }
                }
            }
        }
    }
}

