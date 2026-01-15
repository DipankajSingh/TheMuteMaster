package com.dipdev.themutemaster.ui.screens.savedLocations

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector



@Composable
fun SavedLocationsScreen(modifier: Modifier = Modifier){
    Column {
        SavedLocationCard(
            location = SavedLocation(
                id = "1",
                title = "Home",
                address = "17-a, New India Centre, Cooperage Road, Mumbai",
                isMuted = true,
                rangeRadius = 150f
            ),
            onToggleMute = {},
            onDelete = {},
            onCopyAddress = {},
            onRangeChanged = {}
        )
        SavedLocationCard(
            location = SavedLocation(
                id = "1",
                title = "Home",
                address = "17-a, New India Centre, Cooperage Road, Mumbai",
                isMuted = true,
                rangeRadius = 150f
            ),
            onToggleMute = {},
            onDelete = {},
            onCopyAddress = {},
            onRangeChanged = {}
        )

    }
}




