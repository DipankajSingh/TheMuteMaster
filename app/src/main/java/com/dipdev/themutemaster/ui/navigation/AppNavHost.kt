package com.dipdev.themutemaster.ui.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.dipdev.themutemaster.ui.navigation.navGraph.permissionGraph
import com.dipdev.themutemaster.ui.screens.home.HomeScreen

// AppNavHost.kt
@Composable
fun AppNavHost(
    modifier: Modifier= Modifier,
    navController: NavHostController = rememberNavController(),
    // Logic to decide where to start (e.g., check sharedPrefs or Permissions)
    startDestination: String = "permission_graph_root",
) {
    val ctx=LocalContext.current
    NavHost(navController = navController, startDestination = startDestination) {

        // 1. The Permission Graph (We plug it in here)
        permissionGraph(
            navController = navController,
            onPermissionsComplete = {
                // When the entire graph finishes, navigate to the Main App
                navController.navigate("main_app_graph") {
                    popUpTo("permission_graph_root") { inclusive = true }
                }
            },
            context = ctx,
            modifier = modifier
        )

        // 2. The Main App Graph (Your actual app)
        navigation(startDestination = "home", route = "main_app_graph") {
            composable("home") {
                HomeScreen()
            }
            // ... other app screens ...
        }
    }
}