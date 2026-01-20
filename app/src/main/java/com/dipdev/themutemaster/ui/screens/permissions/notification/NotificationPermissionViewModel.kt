package com.dipdev.themutemaster.ui.screens.permissions.notification


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NotificationPermissionViewModel @Inject constructor() : ViewModel() {

    sealed class PermissionState {
        object NotRequested : PermissionState()
        object Granted : PermissionState()
        object DeniedOnce : PermissionState() // Soft denial (show rationale)
        object PermanentDenied : PermissionState() // Hard denial (show settings)
    }

    var permissionState by mutableStateOf<PermissionState>(PermissionState.NotRequested)
        private set

    // Dialog Control
    var showRationaleDialog by mutableStateOf(false)
        private set
    var showSettingsDialog by mutableStateOf(false)
        private set

    /**
     * Called when the System Permission Launcher returns a result.
     */
    fun onPermissionResult(isGranted: Boolean, isPermanentDenied: Boolean) {
        if (isGranted) {
            permissionState = PermissionState.Granted
            dismissDialogs()
        } else if (isPermanentDenied) {
            permissionState = PermissionState.PermanentDenied
            showSettingsDialog = true
        } else {
            permissionState = PermissionState.DeniedOnce
            // We don't necessarily show a dialog immediately on simple denial,
            // but we store the state so the next button click handles it correctly.
        }
    }

    /**
     * Called when the user clicks the "Enable" button.
     * We decide whether to show a Rationale Dialog or launch the System Prompt.
     */
    fun onEnableClicked(isSystemRationaleNeeded: Boolean, launchSystemPrompt: () -> Unit) {
        if (isSystemRationaleNeeded) {
            // Android says: "User denied this before, please explain why you need it."
            showRationaleDialog = true
        } else if (permissionState is PermissionState.PermanentDenied) {
            // We know they blocked it, so force the Settings dialog
            showSettingsDialog = true
        } else {
            // Fresh request
            launchSystemPrompt()
        }
    }

    fun dismissDialogs() {
        showRationaleDialog = false
        showSettingsDialog = false
    }

    // Helper to manually trigger the settings dialog if logic gets complex
    fun triggerSettingsDialog() {
        showSettingsDialog = true
    }
}