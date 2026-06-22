package com.dipdev.themutemaster.ui.screens.permissions.fglocation

import android.Manifest
import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.LocationOn
import com.dipdev.themutemaster.ui.components.IllustrationType
import com.dipdev.themutemaster.ui.components.PermissionDialog
import com.dipdev.themutemaster.ui.components.PermissionIllustration
import com.dipdev.themutemaster.utils.hasForegroundLocationPermission
import com.dipdev.themutemaster.utils.openAppSettings

@Composable
fun ForegroundLocationAccess(
    modifier: Modifier = Modifier,
    viewModel: ForegroundLocationAccessViewModel = hiltViewModel(),
    onSkip: () -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    // --- Business Logic ---
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        viewModel.onPermissionResult(
            isGranted = isGranted,
            isPermanentDenied = !isGranted && !(context as Activity).shouldShowRequestPermissionRationale(
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        )
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                if (context.hasForegroundLocationPermission()) {
                    viewModel.setPermissionGranted()
                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    // --- Dialogs ---
    if (!viewModel.shouldShowPermanentDeniedDialog && viewModel.shouldShowDialog) {
        PermissionDialog(
            title = "Location Permission Required",
            message = "We need location access to detect when you enter or leave your saved zones (like Home or Work).",
            onActionRequest = {
                launcher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            },
            actionName = "Allow Location",
            onDismissRequest = { viewModel.onDismissRequest() }
        )
    }

    if (viewModel.shouldShowPermanentDeniedDialog && viewModel.shouldShowDialog) {
        PermissionDialog(
            title = "Permission Denied",
            message = "This app needs location access to function properly. Please go to Settings → Permissions → Location and enable it.",
            onActionRequest = {
                openAppSettings(context)
                viewModel.onDismissRequest()
            },
            onDismissRequest = { viewModel.onDismissRequest() },
            actionName = "Open Settings"
        )
    }

    // --- Modern Abstract UI ---
    Scaffold(
        containerColor = MaterialTheme.colorScheme.surface,
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = {
                        if (context.hasForegroundLocationPermission()) {
                            viewModel.setPermissionGranted()
                        } else {
                            val canShowRationale = (context as Activity).shouldShowRequestPermissionRationale(
                                Manifest.permission.ACCESS_FINE_LOCATION
                            )
                            if (!canShowRationale && viewModel.foregroundLocationState == ForegroundLocationAccessViewModel.PermissionState.PermanentDenied) {
                                viewModel.requestPermanentDeniedPermission()
                            } else {
                                launcher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text(
                        text = "Allow While Using App",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(Modifier.height(8.dp))

                TextButton(
                    onClick = onSkip,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Maybe Later",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    ) { padding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(24.dp))
            
            // TOP BAR & PROGRESS
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Step 1 of 4",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = { 0.25f },
                modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(4.dp)),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.primaryContainer
            )

            // ILLUSTRATION AREA
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                PermissionIllustration(type = IllustrationType.LOCATION_FOREGROUND)
            }

            // TEXT CONTENT
            Text(
                text = "Location Access",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(16.dp))

            Text(
                text = "To mute your phone automatically when you arrive at work or school, MuteMaster needs to know your location. This ensures your phone silences exactly when you walk through the door of a saved zone.\n\nYour location data stays on your device. We never share it.",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 24.sp
            )

            Spacer(Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Privacy Policy",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = "Terms of Service",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}