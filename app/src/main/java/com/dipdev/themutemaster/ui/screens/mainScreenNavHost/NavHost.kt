package com.dipdev.themutemaster.ui.screens.mainScreenNavHost

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.dipdev.themutemaster.ui.navigation.Screen
import com.dipdev.themutemaster.ui.screens.home.Home
import com.dipdev.themutemaster.ui.screens.mutedContacts.MutedContacts
import com.dipdev.themutemaster.ui.screens.savedLocations.SavedLocationsScreen

@Composable
fun MainScreenNavHost() {
    val navController = rememberNavController()

    // Define the items to be displayed in the Bottom Bar
    val items = listOf(
        Screen.MutedLocations,
        Screen.Home,
        Screen.MutedContacts
    )

    Scaffold(
        bottomBar = {
            NavigationBar (
                containerColor = Color.Transparent
            ){
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                items.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = screen.title) },
                        label = { Text(screen.title) },
                        // Check if the current destination matches the screen route
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navController.navigate(screen.route) {
                                // Pop up to the start destination of the graph to
                                // avoid building up a large stack of destinations
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                // Avoid multiple copies of the same destination when
                                // reselecting the same item
                                launchSingleTop = true
                                // Restore state when reselecting a previously selected item
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        // The NavHost takes the remaining space
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier
                .padding(innerPadding) // Apply Scaffold padding strictly
        ) {
            composable(Screen.Home.route) { Home() }
            composable(Screen.MutedLocations.route) { SavedLocationsScreen() }
            composable(Screen.MutedContacts.route) { MutedContacts() }
        }
    }
}