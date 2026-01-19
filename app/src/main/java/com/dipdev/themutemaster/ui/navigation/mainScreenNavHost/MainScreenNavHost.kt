package com.dipdev.themutemaster.ui.navigation.mainScreenNavHost

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.dipdev.themutemaster.ui.navigation.Screen
import com.dipdev.themutemaster.ui.screens.home.Home
import com.dipdev.themutemaster.ui.screens.manageLocation.ManageLocationScreen
import com.dipdev.themutemaster.ui.screens.mutedContacts.MutedContacts
import com.dipdev.themutemaster.ui.screens.savedLocations.SavedLocationsScreen

@Composable
fun MainScreenNavHost() {
    val navController = rememberNavController()

    // Observe current screen to toggle Bottom Bar visibility
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val bottomBarItems = listOf(Screen.MutedLocations, Screen.Home, Screen.MutedContacts)

    Scaffold(
        // We use a transparent container color so the floating bar stands out against the background content
        containerColor = MaterialTheme.colorScheme.background,

        bottomBar = {
            // HIDE LOGIC: Only show bottom bar if the current route is one of the main tabs
            val isMainTab = bottomBarItems.any { it.route == currentRoute }

            // Wrap in AnimatedVisibility so the bar slides up/down smoothly when navigating to "Manage"
            AnimatedVisibility(
                visible = isMainTab,
                enter = slideInVertically { it } + fadeIn(),
                exit = slideOutVertically { it } + fadeOut()
            ) {
                FloatingBottomBar(
                    navController = navController,
                    items = bottomBarItems
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding),

            // --- GLOBAL ANIMATIONS (Slide Left/Right) ---
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
            // 1. HOME SCREEN
            composable(Screen.Home.route) {
                Home(
                    onNavigateToManage = { id ->
                        navController.navigate(Screen.ManageLocation.createRoute(id = id))
                    }
                )
            }

            // 2. SAVED LOCATIONS SCREEN
            composable(Screen.MutedLocations.route) {
                SavedLocationsScreen(
                    onNavigateToEdit = { id ->
                        navController.navigate(Screen.ManageLocation.createRoute(id = id))
                    }
                )
            }

            // 3. CONTACTS
            composable(Screen.MutedContacts.route) { MutedContacts() }

            // 4. THE MANAGE SCREEN
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
            ) { backStackEntry ->
                val id = backStackEntry.arguments?.getString("id")

                ManageLocationScreen(
                    onBack = { navController.popBackStack() },
                    onSave = { navController.popBackStack() },
                    onDelete = { navController.popBackStack() }
                )
            }
        }
    }
}

// --- NEW MODERN COMPONENT ---
@Composable
fun FloatingBottomBar(
    navController: NavHostController,
    items: List<Screen>
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    // 1. Container Surface (The "Pill")
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 5.dp, vertical = 24.dp) // Float off the edges
            .height(64.dp) // Compact height
            .shadow(
                elevation = 12.dp,
                shape = RoundedCornerShape(32.dp),
                spotColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f) // Colored shadow
            )
            .clip(RoundedCornerShape(32.dp)), // Fully rounded corners
        color = MaterialTheme.colorScheme.surfaceContainer, // Modern grey-ish surface
        tonalElevation = 8.dp
    ) {
        // 2. The Row of Items
        NavigationBar(
            containerColor = Color.Transparent, // Transparent so Surface color shows
            tonalElevation = 0.dp,
            windowInsets =  WindowInsets(0) // Prevent extra system padding inside the floating pill
        ) {
            items.forEach { screen ->
                val isSelected = currentDestination?.hierarchy?.any { it.route == screen.route } == true

                NavigationBarItem(
                    icon = {
                        // Icon switch (Filled when selected, Outlined when not - optional)
                        Icon(
                            imageVector = screen.icon,
                            contentDescription = screen.title,
                            modifier = Modifier.padding(8.dp)

                        )
                    },
                    // CLEAN LOOK: Only show text when selected (or remove this label block entirely)

                    selected = isSelected,
                    onClick = {
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    // CUSTOM COLORS
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = MaterialTheme.colorScheme.secondaryContainer,
                        selectedIconColor = MaterialTheme.colorScheme.onSecondaryContainer,
                        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                        selectedTextColor = MaterialTheme.colorScheme.onSurface
                    ),
                    alwaysShowLabel = false // This creates the "Expand on select" animation
                )
            }
        }
    }
}