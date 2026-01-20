package com.dipdev.themutemaster.ui.navigation


import androidx.compose.runtime.compositionLocalOf
import androidx.navigation.NavController

val LocalRootNavController = compositionLocalOf<NavController> {
    error("No Root NavController found!")
}