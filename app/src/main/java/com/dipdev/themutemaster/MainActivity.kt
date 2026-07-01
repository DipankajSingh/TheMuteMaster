package com.dipdev.themutemaster

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.DisposableEffect
import android.graphics.Color
import androidx.compose.ui.Modifier
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import android.app.Activity
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
                    android.app.AlertDialog.Builder(this)
                        .setTitle("License Required")
                        .setMessage("DRM Authentication Failed. You need to purchase this app from the Huawei AppGallery to use it.")
                        .setCancelable(false)
                        .setPositiveButton("Exit") { _, _ ->
                            finishAffinity()
                        }
                        .show()
                }
            } else {
                mainViewModel.markDrmComplete()
            }
        }

        // 1. Wait for ViewModel decision (Splash logic)
        splashScreen.setKeepOnScreenCondition {
            mainViewModel.isLoading.value
        }

        setContent {
            // 2. Observe the Theme Preference
            val themeMode by preferencesManager.themeModeFlow
                .collectAsState(initial = AppThemeMode.SYSTEM)

            // 2b. Observe Analytics Preference and set Firebase configuration
            val enableAnalytics by preferencesManager.enableAnalyticsFlow
                .collectAsState(initial = true)

            val context = androidx.compose.ui.platform.LocalContext.current
            // Make sure Firebase respects user's preference
            androidx.compose.runtime.LaunchedEffect(enableAnalytics) {
                try {
                    // Firebase Analytics
                    val analyticsClass = Class.forName("com.google.firebase.analytics.FirebaseAnalytics")
                    val getInstanceMethod = analyticsClass.getMethod("getInstance", android.content.Context::class.java)
                    val analyticsInstance = getInstanceMethod.invoke(null, context)
                    val setCollectionMethod = analyticsClass.getMethod("setAnalyticsCollectionEnabled", Boolean::class.java)
                    setCollectionMethod.invoke(analyticsInstance, enableAnalytics)

                    // Firebase Crashlytics
                    val crashlyticsClass = Class.forName("com.google.firebase.crashlytics.FirebaseCrashlytics")
                    val getCrashlyticsInstanceMethod = crashlyticsClass.getMethod("getInstance")
                    val crashlyticsInstance = getCrashlyticsInstanceMethod.invoke(null)
                    val setCrashlyticsCollectionMethod = crashlyticsClass.getMethod("setCrashlyticsCollectionEnabled", Boolean::class.java)
                    setCrashlyticsCollectionMethod.invoke(crashlyticsInstance, enableAnalytics)
                } catch (e: Exception) {
                    // Firebase might not be available on HMS-only devices, ignore if it crashes
                }
            }

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
                val view = LocalView.current
                if (!view.isInEditMode) {
                    SideEffect {
                        val window = (view.context as Activity).window
                        
                        // 1. Enable edge-to-edge drawing
                        WindowCompat.setDecorFitsSystemWindows(window, false)
                        
                        // 2. Make backgrounds completely transparent
                        @Suppress("DEPRECATION")
                        window.statusBarColor = android.graphics.Color.TRANSPARENT
                        @Suppress("DEPRECATION")
                        window.navigationBarColor = android.graphics.Color.TRANSPARENT
                        
                        // 3. Disable Android 10+ contrast enforcement to prevent grey scrims
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                            @Suppress("DEPRECATION")
                            window.isNavigationBarContrastEnforced = false
                            @Suppress("DEPRECATION")
                            window.isStatusBarContrastEnforced = false
                        }
                        
                        // 4. Update the icon colors dynamically
                        val controller = WindowCompat.getInsetsController(window, view)
                        controller.isAppearanceLightStatusBars = !useDarkTheme
                        controller.isAppearanceLightNavigationBars = !useDarkTheme
                    }
                }

                val startDest by mainViewModel.startDestination.collectAsState()

                Surface(
                    modifier = Modifier.fillMaxSize(), 
                    color = MaterialTheme.colorScheme.background
                ) {
                    androidx.compose.foundation.layout.Box(
                        modifier = Modifier.fillMaxSize().systemBarsPadding()
                    ) {
                        if (!mainViewModel.isLoading.collectAsState().value && startDest != null) {
                            AppNavHost(startDestination = startDest!!)
                        }
                    }
                }
            }
        }
    }
}