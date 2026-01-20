package com.dipdev.themutemaster.ui.screens.generalSettings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.rounded.DarkMode
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Radar
import androidx.compose.material.icons.rounded.Security
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.dipdev.themutemaster.ui.components.CustomTopBar
import com.dipdev.themutemaster.ui.components.SettingsAction
import com.dipdev.themutemaster.ui.components.SettingsGroup
import com.dipdev.themutemaster.ui.components.SettingsSwitch

@Composable
fun GeneralSettingsScreen(
    onNavigateBack: () -> Unit,
    // Inject ViewModel state here
    isDarkTheme: Boolean=true,
    onThemeChanged: Boolean = true,
    isNotificationsEnabled: Boolean = true,
    onNotificationChanged: Boolean=true,
    defaultRadius: Float=10f,
    onRadiusChanged: Float = 20f,
    criticalError: Boolean
) {
    Column(modifier =
        if (criticalError){
            Modifier
                .fillMaxWidth()
        }
        else{
            Modifier
                .fillMaxWidth()
                .statusBarsPadding()
        }) {

        CustomTopBar(
            onBackClick = onNavigateBack,
            title = "General settings",

        )

        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {

            // --- SECTION 1: BEHAVIOR ---
            SettingsGroup("Behavior") {
                SettingsSwitch(
                    icon = Icons.Rounded.Notifications,
                    title = "Status Notification",
                    subtitle = "Show persistent icon when muted",
                    checked = isNotificationsEnabled,
                    onCheckedChange = {  }
                )
                HorizontalDivider(modifier = Modifier.padding(start = 56.dp), color = MaterialTheme.colorScheme.outlineVariant.copy(0.2f))

                SettingsSwitch(
                    icon = Icons.Rounded.DarkMode,
                    title = "Dark Mode",
                    subtitle = "Force dark theme",
                    checked = isDarkTheme,
                    onCheckedChange = {  }
                )
            }

            // --- SECTION 2: DEFAULTS ---
            SettingsGroup("Geofence Defaults") {
                // Example of a custom item (Slider) inserted seamlessly
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Rounded.Radar, null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(Modifier.width(16.dp))
                        Text("Default Radius: ${defaultRadius.toInt()}m")
                    }
                    Slider(
                        value = defaultRadius,
                        onValueChange = {  },
                        valueRange = 50f..500f,
                        steps = 9
                    )
                }
            }

            // --- SECTION 3: GENERAL ---
            SettingsGroup("General") {
                SettingsAction(
                    icon = Icons.Rounded.Security,
                    title = "Privacy Policy",
                    onClick = { /* Open URL */ }
                )
                HorizontalDivider(modifier = Modifier.padding(start = 56.dp), color = MaterialTheme.colorScheme.outlineVariant.copy(0.2f))

                SettingsAction(
                    icon = Icons.Rounded.Info,
                    title = "About MuteMaster",
                    subtitle = "Version 1.0.0",
                    onClick = { /* Show Dialog */ }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Subtle footer
            Text(
                text = "Made with ❤️ by Dipankaj",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.outline,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
    }
}