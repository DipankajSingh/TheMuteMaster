package com.dipdev.themutemaster.ui.screens.generalSettings

import android.content.Intent
import android.net.Uri
import com.dipdev.themutemaster.BuildConfig
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.VolumeOff
import androidx.compose.material.icons.rounded.DarkMode
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Radar
import androidx.compose.material.icons.rounded.Security
import androidx.compose.material.icons.rounded.Mail
import androidx.compose.material.icons.rounded.VolumeOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dipdev.themutemaster.data.local.AppThemeMode
import com.dipdev.themutemaster.ui.components.CustomTopBar
import com.dipdev.themutemaster.ui.components.SettingsAction
import com.dipdev.themutemaster.ui.components.SettingsGroup
import com.dipdev.themutemaster.ui.components.SettingsSwitch

@Composable
fun GeneralSettingsScreen(
    onNavigateBack: () -> Unit,
    viewModel: GeneralSettingsViewModel = hiltViewModel(),
    criticalError: Boolean = false
) {
    // 1. Observe State from ViewModel
    val themeMode by viewModel.themeMode.collectAsStateWithLifecycle()
    val defaultRadius by viewModel.defaultRadius.collectAsStateWithLifecycle()
    val muteMediaVolume by viewModel.muteMediaVolume.collectAsStateWithLifecycle()

    // 2. Local State for Dialogs
    var showThemeDialog by remember { mutableStateOf(false) }

    val uriHandler = LocalUriHandler.current
    val context = LocalContext.current

    Column(modifier =
        if (criticalError) {
            Modifier.fillMaxWidth()
        } else {
            Modifier
                .fillMaxWidth()
                .statusBarsPadding()
        }
    ) {

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
                // Determine subtitle based on current mode
                val themeSubtitle = when (themeMode) {
                    AppThemeMode.LIGHT -> "Light Mode"
                    AppThemeMode.DARK -> "Dark Mode"
                    AppThemeMode.SYSTEM -> "System Default"
                }

                // Clickable Action to open Dialog
                SettingsAction(
                    icon = Icons.Rounded.DarkMode,
                    title = "App Theme",
                    subtitle = themeSubtitle,
                    onClick = { showThemeDialog = true }
                )

                HorizontalDivider(modifier = Modifier.padding(start = 56.dp), color = MaterialTheme.colorScheme.outlineVariant.copy(0.2f))

                SettingsSwitch(
                    icon = Icons.AutoMirrored.Rounded.VolumeOff,
                    title = "Mute Media Volume",
                    subtitle = "Also silence music, videos, and games",
                    checked = muteMediaVolume,
                    onCheckedChange = { viewModel.setMuteMediaVolume(it) }
                )
            }

            // --- SECTION 2: DEFAULTS ---
            SettingsGroup("Geofence Defaults") {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Rounded.Radar, null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(Modifier.width(16.dp))
                        Text(
                            text = "Default Radius: ${defaultRadius.toInt()}m",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                    Slider(
                        value = defaultRadius,
                        onValueChange = { viewModel.updateRadius(it) },
                        valueRange = 100f..500f,
                        steps = 3 // Snaps to 50m increments
                    )
                }
            }

            // --- SECTION 3: GENERAL ---
            SettingsGroup("General") {
                SettingsAction(
                    icon = Icons.Rounded.Security,
                    title = "Privacy Policy",
                    onClick = {
                        uriHandler.openUri("https://dipankajsingh.github.io/MuteMaster/")
                    }
                )
                HorizontalDivider(modifier = Modifier.padding(start = 56.dp), color = MaterialTheme.colorScheme.outlineVariant.copy(0.2f))
                SettingsAction(
                    icon = Icons.Rounded.Mail,
                    title = "Send Feedback",
                    subtitle = "Report bugs or suggest features",
                    onClick = {
                        val intent = Intent(Intent.ACTION_SENDTO).apply {
                            data = Uri.parse("mailto:dipankajsingh25@gmail.com")
                            putExtra(Intent.EXTRA_SUBJECT, "Feedback for The Mute Master")
                            putExtra(Intent.EXTRA_TEXT, "\n\n--- Device Info ---\nApp Version: ${BuildConfig.VERSION_NAME}\nAndroid Version: ${android.os.Build.VERSION.RELEASE}")
                        }
                        context.startActivity(intent)
                    }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Footer
            Text(
                text = "Made with ❤️ by DipDev Labs",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.outline,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    // --- THEME SELECTION DIALOG ---
    if (showThemeDialog) {
        AlertDialog(
            onDismissRequest = { showThemeDialog = false },
            title = { Text("Choose Theme") },
            text = {
                Column {
                    ThemeRadioButton(
                        text = "System Default",
                        selected = themeMode == AppThemeMode.SYSTEM,
                        onClick = {
                            viewModel.setThemeMode(AppThemeMode.SYSTEM)
                            showThemeDialog = false
                        }
                    )
                    ThemeRadioButton(
                        text = "Light Mode",
                        selected = themeMode == AppThemeMode.LIGHT,
                        onClick = {
                            viewModel.setThemeMode(AppThemeMode.LIGHT)
                            showThemeDialog = false
                        }
                    )
                    ThemeRadioButton(
                        text = "Dark Mode",
                        selected = themeMode == AppThemeMode.DARK,
                        onClick = {
                            viewModel.setThemeMode(AppThemeMode.DARK)
                            showThemeDialog = false
                        }
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { showThemeDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

// --- HELPER COMPOSABLE ---
@Composable
private fun ThemeRadioButton(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = onClick
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}