package com.dipdev.themutemaster

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.dipdev.themutemaster.data.local.AppThemeMode
import com.dipdev.themutemaster.data.local.PreferencesManager
import com.dipdev.themutemaster.ui.MainViewModel
import com.dipdev.themutemaster.ui.navigation.AppNavHost
import com.dipdev.themutemaster.ui.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint
import com.dipdev.themutemaster.utils.DrmManager
import android.widget.Toast
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    // Inject the preferences manager directly
    @Inject lateinit var preferencesManager: PreferencesManager

    @Inject lateinit var drmManager: DrmManager

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        
        drmManager.checkDrm(this) { success ->
            if (!success) {
                runOnUiThread {
                    Toast.makeText(this, "DRM Authentication Failed. Please purchase the app from AppGallery.", Toast.LENGTH_LONG).show()
                    finishAffinity()
                }
            }
        }

        // 1. Wait for ViewModel decision (Splash logic)
        splashScreen.setKeepOnScreenCondition {
            mainViewModel.isLoading.value
        }

        enableEdgeToEdge()
        setContent {
            // 2. Observe the Theme Preference
            val themeMode by preferencesManager.themeModeFlow
                .collectAsState(initial = AppThemeMode.SYSTEM)

            // 3. Calculate the actual boolean for Dark Mode
            // isSystemInDarkTheme() checks the global Android setting
            val useDarkTheme = when (themeMode) {
                AppThemeMode.LIGHT -> false
                AppThemeMode.DARK -> true
                AppThemeMode.SYSTEM -> isSystemInDarkTheme()
            }

            // 4. Pass the decision to your Theme wrapper
            AppTheme(
                darkTheme = useDarkTheme // <--- This applies the switch!
            ) {
                val startDest by mainViewModel.startDestination.collectAsState()

                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    if (!mainViewModel.isLoading.collectAsState().value && startDest != null) {
                        AppNavHost(startDestination = startDest!!)
                    }
                }
            }
        }
    }
}