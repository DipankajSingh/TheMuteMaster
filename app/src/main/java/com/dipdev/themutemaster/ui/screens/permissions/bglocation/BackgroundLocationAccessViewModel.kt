package com.dipdev.themutemaster.ui.screens.permissions.bglocation


import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel

class BackgroundLocationAccessViewModel : ViewModel() {

    sealed class PermissionState {
        object NotRequested : PermissionState()
        object Granted : PermissionState()
        object Denied : PermissionState()
        object PermanentDenied : PermissionState() // Relevant if they select "While using app" in settings
    }

    var backgroundLocationState by mutableStateOf<PermissionState>(PermissionState.NotRequested)
        private set

    // We reuse the dialog logic, though for Background Location,
    // the "Dialog" is often the entire screen itself.
    var shouldShowDialog by mutableStateOf(false)
        private set

    // Logic: If true, we might show a specific "Go to Settings" manual guide
    var shouldShowPermanentDeniedDialog by mutableStateOf(false)
        private set

    /**
     * Standard callback from the Permission Launcher
     */
    fun onPermissionResult(isGranted: Boolean, isPermanentDenied: Boolean) {
        backgroundLocationState = when {
            isGranted -> PermissionState.Granted
            isPermanentDenied -> {
                shouldShowDialog = true
                shouldShowPermanentDeniedDialog = true
                PermissionState.PermanentDenied
            }
            else -> {
                shouldShowPermanentDeniedDialog = false
                shouldShowDialog = true // Show rationale again
                PermissionState.Denied
            }
        }
    }

    /**
     * CRITICAL for Background Location:
     * Call this from onResume() to update state when user returns from Settings.
     */
    fun checkPermission(context: Context) {
        // Background permission is only relevant for Android 10 (Q) and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val hasPermission = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            ) == PackageManager.PERMISSION_GRANTED

            if (hasPermission) {
                backgroundLocationState = PermissionState.Granted
                // Reset dialogs as we are done
                shouldShowDialog = false
                shouldShowPermanentDeniedDialog = false
            } else {
                // If checking manually and it's missing, it's technically "Denied"
                // But we don't necessarily want to trigger dialogs immediately unless requested
                if (backgroundLocationState == PermissionState.Granted) {
                    backgroundLocationState = PermissionState.Denied
                }
            }
        } else {
            // Android 9 and below implies background if foreground is granted
            backgroundLocationState = PermissionState.Granted
        }
    }

    fun setPermissionGranted() {
        backgroundLocationState = PermissionState.Granted
    }

    fun onDismissRequest() {
        shouldShowDialog = false
        shouldShowPermanentDeniedDialog = false
    }
}