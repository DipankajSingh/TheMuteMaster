package com.dipdev.themutemaster.ui.screens.permissions.fglocation

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dipdev.themutemaster.ui.components.LocationImage
import com.dipdev.themutemaster.ui.components.PermissionDialog
import com.dipdev.themutemaster.utils.openAppSettings

@Composable
fun ForegroundLocationAccess(
    modifier: Modifier = Modifier,
    viewModel: ForegroundLocationAccessViewModel = viewModel()
) {
    val context = LocalContext.current

    // --- Business Logic (Untouched) ---
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

    LaunchedEffect(Unit) {
        val hasPermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        println("launch effect ran $hasPermission")

        if (hasPermission) {
            viewModel.setPermissionGranted()
        }
    }

    // --- Dialogs (Untouched) ---
    if (!viewModel.shouldShowPermanentDeniedDialog && viewModel.shouldShowDialog) {
        PermissionDialog(
            title = "Location Permission Required",
            message = "We need location access to detect your location and automatically mute/unmute your device at specified locations.",
            onActionRequest = {
                println("denied in screen ran")
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
                println("perma denied in screen ran")
                openAppSettings(context)
                viewModel.onDismissRequest()
            },
            onDismissRequest = { viewModel.onDismissRequest() },
            actionName = "Open Settings"
        )
    }

    // --- UI Layout (Styled) ---
    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally // Fix: Center everything by default
    ) {
        // Top Bar Area
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.TopEnd
        ) {
            TextButton(onClick = {}) {
                Text(
                    text = "T&C",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }

        Spacer(Modifier.height(24.dp))

        // Hero Image
        // Assuming LocationImage() has a default size, but we ensure it doesn't stretch
        Box(contentAlignment = Alignment.Center) {
            LocationImage()
        }

        Spacer(Modifier.height(40.dp))

        // Title
        Text(
            text = "Allow Your Location",
            style = MaterialTheme.typography.headlineMedium, // Better than displaySmall for this context
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(Modifier.height(16.dp))

        // Description
        Text(
            text = "We need your location permission to manage your audio profiles automatically when you reach specified locations. Please see terms and conditions to know how we use this information.",
            style = MaterialTheme.typography.bodyLarge, // Fix: Readable size
            fontWeight = FontWeight.Normal, // Fix: 'Thin' is hard to read
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant, // Softer text color
            lineHeight = 24.sp
        )

        Spacer(Modifier.weight(1f)) // Push content up, buttons down

        // Disclaimer
        Text(
            text = "By continuing, you agree with T&C!",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.outline,
            textDecoration = TextDecoration.Underline,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(24.dp))

        // Main Action Button
        Button(
            onClick = {
                // Logic pasted from your code
                val hasPermission = ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED

                if (hasPermission) {
                    viewModel.setPermissionGranted()
                    return@Button
                }

                val canShowRationale = (context as Activity).shouldShowRequestPermissionRationale(
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
                if (!canShowRationale && viewModel.foregroundLocationState == ForegroundLocationAccessViewModel.PermissionState.PermanentDenied) {
                    viewModel.requestPermanentDeniedPermission()
                    return@Button
                }

                launcher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            },
            modifier = Modifier
                .fillMaxWidth() // Fill width looks better on modern mobile UI
                .height(50.dp), // Standard touch target height
            shape = MaterialTheme.shapes.medium
        ) {
            Text(
                text = "Allow Access",
                style = MaterialTheme.typography.titleMedium
            )
        }

        Spacer(Modifier.height(16.dp))

        // Secondary Action
        TextButton(
            onClick = {},
            modifier = Modifier.height(48.dp)
        ) {
            Text(
                text = "Not now",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }

        Spacer(Modifier.height(16.dp)) // Bottom safety margin
    }
}
