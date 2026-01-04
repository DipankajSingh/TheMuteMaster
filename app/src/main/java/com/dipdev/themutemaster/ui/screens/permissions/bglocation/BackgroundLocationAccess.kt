package com.dipdev.themutemaster.ui.screens.permissions.bglocation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dipdev.themutemaster.ui.components.LocationImage

@Composable
fun BackgroundLocationAccess(
    modifier: Modifier = Modifier,
    viewModel: BackgroundLocationAccessViewModel = viewModel(),
    onGoToSettingsClick: () -> Unit,
    onBackgroundGranted: ()->Unit
) {



    // Inside BackgroundLocationAccess composable
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                // Refresh state when app comes to foreground
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

    // UI Structure: Instructional. We need to tell the user what to do.

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 1. Top Bar
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.TopEnd
        ) {
            TextButton(onClick = { /* Handle 'Skip' if optional */ }) {
                Text(
                    text = "Later",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }

        Spacer(Modifier.height(24.dp))

        // 2. Visual Visual: Reusing your location image, but you might want
        // to overlay a clock or 'infinite' symbol to imply "Always"
        Box(contentAlignment = Alignment.Center) {
            LocationImage()
            // Optional: Add a "Badge" here indicating "Always" logic
        }

        Spacer(Modifier.height(40.dp))

        // 3. The Required Disclosure Title
        Text(
            text = "Always-On Location",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(Modifier.height(16.dp))

        // 4. The "Prominent Disclosure" (CRITICAL FOR PLAY STORE)
        // You must explain strictly: "This feature uses location in the background to [feature name]."
        Text(
            text = "To automatically mute your device when you arrive at work, this app needs location access even when the app is closed.",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            lineHeight = 24.sp
        )

        Spacer(Modifier.height(16.dp))

        // 5. Instructional Text (Crucial for Android 11+)
        // Since the system dialog might just show "Change in Settings", guide them.
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            ),
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "How to enable:",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(Modifier.height(8.dp))
                InstructionRow(step = "1", text = "Tap 'Allow in Settings' below")
                InstructionRow(step = "2", text = "Select 'Permissions' > 'Location'")
                InstructionRow(step = "3", text = "Choose 'Allow all the time'")
            }
        }

        Spacer(Modifier.weight(1f))

        // 6. Action Button
        Button(
            onClick = {
                // LOGIC NOTE:
                // On Android 11 (API 30+), requesting ACCESS_BACKGROUND_LOCATION
                // often redirects the user to the App Info page automatically.
                // You usually don't need a specific 'launcher' here if you strictly
                // follow the incremental request flow, but passing a callback
                // is safer to keep logic in the ViewModel/Activity.
                onGoToSettingsClick()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = MaterialTheme.shapes.medium
        ) {
            Text(
                text = "Allow in Settings", // Be honest about where this button goes
                style = MaterialTheme.typography.titleMedium
            )
        }

        Spacer(Modifier.height(16.dp))
    }
}

@Composable
fun InstructionRow(step: String, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .background(MaterialTheme.colorScheme.primary, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = step,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
        Spacer(Modifier.width(12.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}