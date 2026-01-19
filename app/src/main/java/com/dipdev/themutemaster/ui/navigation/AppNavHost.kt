package com.dipdev.themutemaster.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dipdev.themutemaster.ui.navigation.mainScreenNavHost.MainScreenNavHost
import com.dipdev.themutemaster.ui.navigation.navGraph.permissionGraph

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String,
) {
    val ctx = LocalContext.current

    NavHost(
        navController = navController,
        startDestination = startDestination,
        // Global Animations for Parent Switches
        enterTransition = { fadeIn(tween(300)) },
        exitTransition = { fadeOut(tween(300)) }
    ) {

        // 1. THE PERMISSION FLOW
        permissionGraph(
            navController = navController,
            context = ctx,
            modifier = modifier,
            onPermissionsComplete = {
                // When done, SWAP to the Main App Container
                navController.navigate(AppRoute.MAIN_APP_CONTAINER) {
                    popUpTo(AppRoute.PERMISSION_FLOW) { inclusive = true }
                }
            }
        )

        // 2. THE MAIN APP CONTAINER
        // This is a single composable that holds your MainScreenNavHost
        composable(
            route = AppRoute.MAIN_APP_CONTAINER,
            enterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Up, tween(500))
            }
        ) {
            // This composable has its OWN internal NavController and Scaffold
            MainScreenNavHost()
        }
    }
}