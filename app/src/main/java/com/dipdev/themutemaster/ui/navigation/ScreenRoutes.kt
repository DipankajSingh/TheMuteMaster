package com.dipdev.themutemaster.ui.navigation

// PermissionRoutes.kt
sealed class PermissionRoute(val route: String) {
    object Foreground : PermissionRoute("perm_foreground")
    object Background : PermissionRoute("perm_background")
}