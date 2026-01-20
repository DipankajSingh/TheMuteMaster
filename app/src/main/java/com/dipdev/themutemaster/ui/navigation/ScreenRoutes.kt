package com.dipdev.themutemaster.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOff
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

object AppRoute {
    const val WELCOME = "welcome_screen"
    const val PERMISSION_FLOW = "permission_flow_root"
    const val MAIN_APP_CONTAINER = "main_app_container"
}

sealed class PermissionRoute(val route: String) {
    object Foreground : PermissionRoute("perm_foreground")
    object Background : PermissionRoute("perm_background")
    object NotificationPermission : PermissionRoute("perm_notification")
    object Dnd : PermissionRoute("perm_dnd")
}

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    data object Home : Screen("home_tab", "Home", Icons.Default.Home)
    data object MutedLocations : Screen("muted_locations", "Locations", Icons.Default.LocationOff)
    data object MutedContacts : Screen("muted_contacts", "Contacts", Icons.Default.Person)
    data object GeneralSettings : Screen("general_settings", "Settings", Icons.Default.Settings)

    object ManageLocation : Screen("manage_location?id={id}", "Manage", Icons.Default.Settings) {
        fun createRoute(id: String ): String {
            return "manage_location?id=${id}"
        }
    }
}