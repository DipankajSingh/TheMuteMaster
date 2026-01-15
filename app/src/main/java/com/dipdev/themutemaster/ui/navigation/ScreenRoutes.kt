package com.dipdev.themutemaster.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOff
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector


// PermissionRoutes.kt
sealed class PermissionRoute(val route: String) {
    object Foreground : PermissionRoute("perm_foreground")
    object Background : PermissionRoute("perm_background")
}

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    data object Home : Screen("home", "Home", Icons.Default.Home)
    data object MutedLocations : Screen("MutedLocations", "Muted Locations", Icons.Default.LocationOff)
    data object MutedContacts : Screen("MutedContacts", "Muted Contacts", Icons.Default.Person)
}