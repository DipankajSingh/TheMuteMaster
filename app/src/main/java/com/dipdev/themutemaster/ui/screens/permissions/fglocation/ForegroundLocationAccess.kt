package com.dipdev.themutemaster.ui.screens.permissions.fglocation

import android.Manifest
import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.outlined.MyLocation
import androidx.compose.material.icons.outlined.Security
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.dipdev.themutemaster.ui.components.PermissionDialog
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

    // --- Business Logic (Preserved) ---
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

    // --- Dialogs (Preserved) ---
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

    // --- Responsive UI ---
    Scaffold(
        containerColor = MaterialTheme.colorScheme.surface
    ) { padding ->
        // 1. Get screen height constraints
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            val minHeight = maxHeight

            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()) // Enable scrolling
                    .heightIn(min = minHeight)             // Force full height on tall screens
                    .padding(24.dp),                       // Consistent padding
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // 1. TOP BAR (T&C)
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.TopEnd) {
                    TextButton(onClick = { /* TODO: Open T&C */ }) {
                        Text(
                            text = "Terms & Privacy",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                // Push content slightly down, but allow shrinking on small screens
                Spacer(Modifier.height(24.dp))

                // 2. HERO IMAGE (Gradient Bubble)
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(120.dp)
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.primaryContainer,
                                    MaterialTheme.colorScheme.surfaceContainerHighest
                                )
                            ),
                            shape = CircleShape
                        )
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        modifier = Modifier.size(56.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }

                Spacer(Modifier.height(32.dp))

                // 3. HEADLINE
                Text(
                    text = "Where are you?",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(Modifier.height(12.dp))

                Text(
                    text = "To mute your phone automatically when you arrive at work or school, MuteMaster needs to know your location.",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(Modifier.height(40.dp))

                // 4. VALUE PROPOSITION LIST
                Column(
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    LocationBenefitItem(
                        icon = Icons.Outlined.MyLocation,
                        title = "Precise Triggers",
                        desc = "Ensures your phone silences exactly when you walk through the door."
                    )
                    LocationBenefitItem(
                        icon = Icons.Outlined.Security,
                        title = "Private & Secure",
                        desc = "Your location data stays on your device. We never share it."
                    )
                }

                // This spacer pushes the buttons to the bottom
                // On small screens (scrolling), it collapses. On tall screens, it expands.
                Spacer(Modifier.weight(1f))
                Spacer(Modifier.height(24.dp)) // Safety gap

                // 5. ACTION BUTTONS
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
                        .height(50.dp),
                    shape = RoundedCornerShape(25.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text(
                        text = "Allow While Using App",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(Modifier.height(12.dp))

                TextButton(
                    onClick = onSkip,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Maybe Later",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(Modifier.height(16.dp))
            }
        }
    }
}

// --- Local Helper Composable ---
@Composable
private fun LocationBenefitItem(
    icon: ImageVector,
    title: String,
    desc: String
) {
    Row(
        verticalAlignment = Alignment.Top,
        modifier = Modifier.fillMaxWidth()
    ) {
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.4f),
            modifier = Modifier.size(48.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSecondaryContainer,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = desc,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 20.sp
            )
        }
    }
}