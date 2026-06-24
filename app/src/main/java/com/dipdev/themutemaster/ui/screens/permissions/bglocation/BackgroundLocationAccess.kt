package com.dipdev.themutemaster.ui.screens.permissions.bglocation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dipdev.themutemaster.ui.components.AnimatedLocationInstruction
import com.dipdev.themutemaster.ui.components.IllustrationType
import com.dipdev.themutemaster.ui.components.PermissionIllustration

@Composable
fun BackgroundLocationAccess(
    modifier: Modifier = Modifier,
    viewModel: BackgroundLocationAccessViewModel = viewModel(),
    onGoToSettingsClick: () -> Unit,
    onBackgroundGranted: () -> Unit,
    onSkip: () -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    var isUnderstood by remember { mutableStateOf(false) }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.checkPermission(context)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    LaunchedEffect(viewModel.backgroundLocationState) {
        if (viewModel.backgroundLocationState is BackgroundLocationAccessViewModel.PermissionState.Granted) {
            onBackgroundGranted()
        }
    }

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
                        if (!isUnderstood) {
                            isUnderstood = true
                        } else {
                            onGoToSettingsClick()
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
                        text = if (isUnderstood) "Open Settings" else "Understood",
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
            
            // TOP BAR
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Step 2 of 4",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = { 0.5f },
                modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(4.dp)),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.primaryContainer
            )

            if (!isUnderstood) {
                // ILLUSTRATION AREA
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    PermissionIllustration(type = IllustrationType.LOCATION_BACKGROUND)
                }

                // TEXT CONTENT
                Text(
                    text = "Always-On Location",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(Modifier.height(16.dp))

                Text(
                    text = "To mute your device automatically when you arrive at work, MuteMaster needs location access even when the app is closed. This enables detecting your arrival at a saved place, even if your phone is in your pocket. Location data is processed strictly locally and never shared, uploaded, or sold to third parties.",
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
            } else {
                Spacer(Modifier.height(24.dp))
                // MIDDLE VISUAL (DYNAMIC SCALE)
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    AnimatedLocationInstruction(
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}
