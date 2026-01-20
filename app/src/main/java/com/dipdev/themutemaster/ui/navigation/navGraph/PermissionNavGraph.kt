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
import com.dipdev.themutemaster.ui.navigation.AppRoute
import com.dipdev.themutemaster.ui.navigation.PermissionRoute
import com.dipdev.themutemaster.ui.screens.permissions.bglocation.BackgroundLocationAccess
import com.dipdev.themutemaster.ui.screens.permissions.dnd.DndPermissionScreen
import com.dipdev.themutemaster.ui.screens.permissions.fglocation.ForegroundLocationAccess
import com.dipdev.themutemaster.ui.screens.permissions.fglocation.ForegroundLocationAccessViewModel
import com.dipdev.themutemaster.ui.screens.permissions.notification.NotificationPermissionScreen
import com.dipdev.themutemaster.utils.hasDndPermission
import com.dipdev.themutemaster.utils.hasLocationPermission
import com.dipdev.themutemaster.utils.hasNotificationPermission
import com.dipdev.themutemaster.utils.openAppSettings

fun NavGraphBuilder.permissionGraph(
    navController: NavController,
    context: Context,
    modifier: Modifier,
    onPermissionsComplete: () -> Unit
) {
    val startRoute = if (!context.hasLocationPermission()) {
        PermissionRoute.Foreground.route
    } else if (!context.hasNotificationPermission()) {
        PermissionRoute.NotificationPermission.route
    } else if (!context.hasDndPermission()) {
        PermissionRoute.Dnd.route
    } else {
        "ALL_GRANTED_EXIT"
    }

    navigation(
        startDestination = startRoute,
        route = AppRoute.PERMISSION_FLOW
    ) {
        composable("ALL_GRANTED_EXIT") {
            LaunchedEffect(Unit) { onPermissionsComplete() }
        }
        composable(PermissionRoute.Foreground.route) {
            val viewModel: ForegroundLocationAccessViewModel = viewModel()
            val state = viewModel.foregroundLocationState

            LaunchedEffect(state) {
                if (state is ForegroundLocationAccessViewModel.PermissionState.Granted) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        navController.navigate(PermissionRoute.Background.route) {
                            popUpTo(PermissionRoute.Foreground.route) { inclusive = true }
                        }
                    } else {
                        navController.navigate(PermissionRoute.NotificationPermission.route) {
                            popUpTo(PermissionRoute.Foreground.route) { inclusive = true }
                        }
                    }
                }
            }

            ForegroundLocationAccess(
                viewModel = viewModel,
                modifier = modifier,
                onSkip = {
                    navController.navigate(PermissionRoute.NotificationPermission.route)
                }
            )
        }

        composable(PermissionRoute.Background.route) {
            BackgroundLocationAccess(
                onGoToSettingsClick = { context.openAppSettings() },
                onBackgroundGranted = {
                    navController.navigate(PermissionRoute.NotificationPermission.route) {
                        popUpTo(PermissionRoute.Background.route) { inclusive = true }
                    }
                },
                modifier = modifier,
                onSkip = {
                    navController.navigate(PermissionRoute.NotificationPermission.route)
                }
            )
        }

        // --- SCREEN 3: NOTIFICATION ---
        composable(PermissionRoute.NotificationPermission.route) {
            NotificationPermissionScreen(
                onPermissionGranted = {
                    navController.navigate(PermissionRoute.Dnd.route) {
                        popUpTo(PermissionRoute.NotificationPermission.route) { inclusive = true }
                    }
                },
                onSkip = {
                    navController.navigate(PermissionRoute.Dnd.route)
                }
            )
        }

        // --- SCREEN 4: DND (Last Step) ---
        composable(PermissionRoute.Dnd.route) {
            DndPermissionScreen(
                onPermissionGranted = {
                    onPermissionsComplete()
                }
            )
        }
    }
}