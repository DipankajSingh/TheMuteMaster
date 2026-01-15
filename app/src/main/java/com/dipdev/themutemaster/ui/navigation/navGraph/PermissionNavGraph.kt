package com.dipdev.themutemaster.ui.navigation.navGraph

import android.content.Context
import android.os.Build
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.dipdev.themutemaster.ui.navigation.PermissionRoute
import com.dipdev.themutemaster.ui.screens.permissions.bglocation.BackgroundLocationAccess
import com.dipdev.themutemaster.ui.screens.permissions.fglocation.ForegroundLocationAccess
import com.dipdev.themutemaster.ui.screens.permissions.fglocation.ForegroundLocationAccessViewModel
import com.dipdev.themutemaster.utils.openAppSettings

fun NavGraphBuilder.permissionGraph(
    navController: NavController,
    context: Context,
    modifier: Modifier,
    onPermissionsComplete: () -> Unit // Callback to exit to the Main App
) {
    navigation(
        startDestination = PermissionRoute.Foreground.route,
        route = "permission_graph_root" // The ID for this entire group
    ) {

        // --- SCREEN 1: FOREGROUND ---
        composable(PermissionRoute.Foreground.route) {
            // Get the specific VM for this screen
            val viewModel: ForegroundLocationAccessViewModel = viewModel()

            // OBSERVE STATE: This is the bridge between VM and Navigation
            val state = viewModel.foregroundLocationState

            // React to state changes
            LaunchedEffect(state) {
                if (state is ForegroundLocationAccessViewModel.PermissionState.Granted) {
                    // Logic: Where to go next?
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        // Android 10+: Needs Background step
                        navController.navigate(PermissionRoute.Background.route) {
                            // Clear Foreground screen from history so back button exits app
                            popUpTo(PermissionRoute.Foreground.route) { inclusive = true }
                        }
                    } else {
                        // Android 9-: Done! Signal completion.
                        onPermissionsComplete()
                    }
                }
            }

            // Render your UI
            ForegroundLocationAccess(
                viewModel = viewModel,
                modifier = modifier
                // We don't pass nav callbacks here because we are observing
                // the VM state directly above. This keeps the UI 'dumb'.
            )
        }

        // --- SCREEN 2: BACKGROUND ---
        composable(PermissionRoute.Background.route) {
            // You will need a similar VM setup for Background Location later
            BackgroundLocationAccess(
                onGoToSettingsClick = { openAppSettings(context) },
                onBackgroundGranted = {
                    // Success! The whole permission flow is finished.
                    onPermissionsComplete()
                },
                modifier = modifier
            )
        }
    }
}