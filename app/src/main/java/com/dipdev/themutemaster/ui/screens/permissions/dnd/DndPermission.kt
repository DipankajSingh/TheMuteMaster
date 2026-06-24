package com.dipdev.themutemaster.ui.screens.permissions.dnd

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DoNotDisturbOn
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
import com.dipdev.themutemaster.ui.components.AnimatedToggleInstruction
import com.dipdev.themutemaster.ui.components.IllustrationType
import com.dipdev.themutemaster.ui.components.PermissionIllustration
import com.dipdev.themutemaster.utils.hasDndPermission
import com.dipdev.themutemaster.utils.openDndSettings

@Composable
fun DndPermissionScreen(
    onPermissionGranted: () -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
    var isUnderstood by remember { mutableStateOf(false) }

    // --- Logic: Auto-check on return ---
    val currentOnPermissionGranted = rememberUpdatedState(onPermissionGranted)
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                if (context.hasDndPermission()) {
                    currentOnPermissionGranted.value()
                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
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
                            context.openDndSettings()
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
                        text = if (isUnderstood) "Grant Access" else "Understood",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "The app will update automatically when you return.",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
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
                    text = "Step 4 of 4",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = { 1f },
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
                    PermissionIllustration(type = IllustrationType.DND)
                }

                // HEADLINE
                Text(
                    text = "Modify Do Not Disturb",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "To automatically mute your phone, MuteMaster needs to toggle Android's native DND mode. This permission only controls volume states and does not read or intercept screen content.",
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
                Spacer(modifier = Modifier.height(32.dp))
                Text(
                    text = "Follow these steps in Settings:",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(16.dp))
                Text(
                    text = "1. Find 'MuteMaster' in the list\n2. Toggle the switch to 'On'\n3. Tap 'Allow' if prompted",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 28.sp,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(32.dp))
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    AnimatedToggleInstruction(
                        targetName = "MuteMaster",
                        icon = Icons.Default.DoNotDisturbOn
                    )
                }
            }
        }
    }
}
