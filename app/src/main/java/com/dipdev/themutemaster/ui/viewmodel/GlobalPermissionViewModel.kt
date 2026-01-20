package com.dipdev.themutemaster.ui.viewmodel

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.compose.runtime.mutableStateListOf
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import com.dipdev.themutemaster.utils.hasDndPermission
import com.dipdev.themutemaster.utils.hasLocationPermission
import com.dipdev.themutemaster.utils.hasNotificationPermission
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

// Define the errors
enum class AppError {
    DND_MISSING,
    LOCATION_BG_MISSING,
    LOCATION_FG_MISSING,
    NOTIFICATION_MISSING
}

@HiltViewModel
class GlobalPermissionViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {

    // Observable list of errors
    var activeErrors = mutableStateListOf<AppError>()
        private set

    fun checkPermissions() {
        activeErrors.clear()

        // 1. Critical: DND (App crashes without this)
        if (!context.hasDndPermission()) {
            activeErrors.add(AppError.DND_MISSING)
        }

        // 2. Critical: Location (Automation fails without this)
        if (!context.hasLocationPermission()) {
            activeErrors.add(AppError.LOCATION_FG_MISSING)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q &&
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            activeErrors.add(AppError.LOCATION_BG_MISSING)
        }

        // 3. Warning: Notifications (UX suffers)
        if (!context.hasNotificationPermission()) {
            activeErrors.add(AppError.NOTIFICATION_MISSING)
        }
    }
}