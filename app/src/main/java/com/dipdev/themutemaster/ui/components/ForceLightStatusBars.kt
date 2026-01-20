package com.dipdev.themutemaster.ui.components

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

@Composable
fun ForceLightStatusBars(useDarkIcons: Boolean) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            val insetsController = WindowCompat.getInsetsController(window, view)
            // true = Dark Icons (for Light Background)
            // false = Light Icons (for Dark Background)
            insetsController.isAppearanceLightStatusBars = useDarkIcons
        }
    }
}