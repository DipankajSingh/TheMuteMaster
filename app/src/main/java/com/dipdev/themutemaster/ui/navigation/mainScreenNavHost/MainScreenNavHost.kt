package com.dipdev.themutemaster.ui.navigation.mainScreenNavHost

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.dipdev.themutemaster.ui.components.GlobalErrorBanner
import com.dipdev.themutemaster.ui.navigation.Screen
import com.dipdev.themutemaster.ui.screens.generalSettings.GeneralSettingsScreen
import com.dipdev.themutemaster.ui.screens.home.Home
import com.dipdev.themutemaster.ui.screens.manageLocation.ManageLocationScreen
import com.dipdev.themutemaster.ui.screens.savedLocations.SavedLocationsScreen
import com.dipdev.themutemaster.ui.viewmodel.AppError
import com.dipdev.themutemaster.ui.viewmodel.GlobalPermissionViewModel
import com.dipdev.themutemaster.utils.openAppSettings
import com.dipdev.themutemaster.utils.openDndSettings

@Composable
fun MainScreenNavHost(
    globalViewModel: GlobalPermissionViewModel = hiltViewModel()
) {
    val navController = rememberNavController()
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val bottomBarItems = listOf(Screen.MutedLocations, Screen.Home, Screen.Schedules)

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                globalViewModel.checkPermissions()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

   Box (
       modifier = Modifier.background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            val criticalError = globalViewModel.activeErrors.firstOrNull()

            if (criticalError != null) {
                GlobalErrorBanner(
                    error = criticalError,
                    onFixClick = {
                        when (criticalError) {
                            AppError.DND_MISSING -> context.openDndSettings()
                            AppError.LOCATION_FG_MISSING,
                            AppError.LOCATION_BG_MISSING,
                            AppError.NOTIFICATION_MISSING -> context.openAppSettings()
                        }
                    }
                )
            }

            NavHost(
                navController = navController,
                startDestination = Screen.Home.route,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                enterTransition = {
                    slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Start, tween(300))
                },
                exitTransition = {
                    slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Start, tween(300))
                },
                popEnterTransition = {
                    slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.End, tween(300)) +
                            fadeIn(tween(300))
                },
                popExitTransition = {
                    slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.End, tween(300)) +
                            fadeOut(tween(300))
                }
            ) {
                composable(Screen.Home.route) {
                    Home(
                        onNavigateToManage = { id ->
                            navController.navigate(Screen.ManageLocation.createRoute(id = id))
                        },
                        criticalError=criticalError!=null,
                        onSettingsClick = {
                            navController.navigate(Screen.GeneralSettings.route)
                        }
                    )
                }

                composable(Screen.MutedLocations.route) {
                    SavedLocationsScreen (
                        onNavigateToEdit = { id ->
                            navController.navigate(Screen.ManageLocation.createRoute(id = id))
                        },
                        criticalError=criticalError!=null
                    )
                }

                composable(Screen.Schedules.route) {
                    com.dipdev.themutemaster.ui.screens.schedules.SchedulesScreen(
                        onNavigateToEdit = { id ->
                            navController.navigate(Screen.ManageSchedule.createRoute(id))
                        },
                        criticalError = criticalError != null
                    )
                }

                composable(Screen.GeneralSettings.route){ GeneralSettingsScreen(onNavigateBack = {
                    navController.popBackStack()
                },
                    criticalError=criticalError!=null
                ) }
                composable(
                    route = Screen.ManageLocation.route,
                    arguments = listOf(
                        navArgument("id") { type = NavType.StringType; nullable = true; defaultValue = null }
                    ),
                    enterTransition = {
                        slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Up, tween(400))
                    },
                    popExitTransition = {
                        slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Down, tween(400))
                    }
                ) {
                    ManageLocationScreen(
                        onBack = { navController.popBackStack() },
                        onSave = { navController.popBackStack() },
                        onDelete = { navController.popBackStack() },
                        criticalError=criticalError!=null
                    )
                }

                composable(
                    route = Screen.ManageSchedule.route,
                    arguments = listOf(
                        navArgument("id") { type = NavType.StringType; nullable = true; defaultValue = null }
                    ),
                    enterTransition = {
                        slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Up, tween(400))
                    },
                    popExitTransition = {
                        slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Down, tween(400))
                    }
                ) {
                    com.dipdev.themutemaster.ui.screens.schedules.AddScheduleScreen(
                        onBack = { navController.popBackStack() },
                        onSave = { navController.popBackStack() },
                        onDelete = { navController.popBackStack() },
                        criticalError = criticalError != null
                    )
                }
            }

            /*...............................
            val isMainTab = bottomBarItems.any { it.route == currentRoute }

            AnimatedVisibility(
                visible = isMainTab,
                enter = slideInVertically { it } + fadeIn(),
                exit = slideOutVertically { it } + fadeOut()
            ) {
                FloatingBottomBar(
                    navController = navController,
                    items = bottomBarItems
                )
            }*/
// 3. UPDATED LOGIC: Hide bar if we are on MutedContacts
            val showBottomBar = bottomBarItems.any { it.route == currentRoute }

            AnimatedVisibility(
                visible = showBottomBar, // Use the new boolean
                enter = slideInVertically { it } + fadeIn(),
                exit = slideOutVertically { it } + fadeOut()
            ) {
                FloatingBottomBar(
                    navController = navController,
                    items = bottomBarItems
                )
            }
        }
    }
}

@Composable
fun FloatingBottomBar(
    navController: NavHostController,
    items: List<Screen>
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 20.dp)
            .height(68.dp)
            .clip(RoundedCornerShape(34.dp))
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.surfaceContainerHigh.copy(alpha = 0.92f),
                        MaterialTheme.colorScheme.surfaceContainer.copy(alpha = 0.88f)
                    )
                )
            )
            .border(
                width = 0.5.dp,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f),
                        MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.08f)
                    )
                ),
                shape = RoundedCornerShape(34.dp)
            ),
        containerColor = Color.Transparent,
        tonalElevation = 0.dp,
        windowInsets = WindowInsets(0)
    ) {
        items.forEach { screen ->
            val isSelected = currentDestination?.hierarchy?.any { it.route == screen.route } == true

            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = screen.icon,
                        contentDescription = screen.title,
                        modifier = Modifier.size(24.dp)
                    )
                },
                selected = isSelected,
                onClick = {
                    if (!isSelected) {
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = MaterialTheme.colorScheme.secondaryContainer,
                    selectedIconColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                    selectedTextColor = MaterialTheme.colorScheme.onSurface
                ),
                alwaysShowLabel = false
            )
        }
    }
}
