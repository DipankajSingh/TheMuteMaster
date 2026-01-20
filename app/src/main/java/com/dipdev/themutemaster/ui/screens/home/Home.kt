package com.dipdev.themutemaster.ui.screens.home

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddLocation
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.LocationOff
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.PinDrop
import androidx.compose.material.icons.rounded.NotificationsOff
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.dipdev.themutemaster.ui.navigation.AppRoute
import com.dipdev.themutemaster.ui.navigation.LocalRootNavController
import com.dipdev.themutemaster.ui.screens.onboarding.alpha
import com.dipdev.themutemaster.utils.copyToClipboard

@Composable
fun Home(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
    onNavigateToManage: (String) -> Unit
) {
    val context = LocalContext.current
    val rootNavController = LocalRootNavController.current

    // --- Permissions ---
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val isGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        if (isGranted) {
            viewModel.fetchLocation()
        } else {
            viewModel.onPermissionDenied()
        }
    }

    LaunchedEffect(Unit) {
        val hasFine = ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (hasFine) {
            viewModel.fetchLocation()
        } else {
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            ModernHeader(
                onSettingsClick = { rootNavController.navigate(AppRoute.PERMISSION_FLOW) }
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(0.5f))

            // --- PULSE INDICATOR ---
            // Re-enabled the alpha animation you commented out
            PulseIndicator(
                isActive = viewModel.isLocationMuted,
                statusText = if (viewModel.isLocationMuted) "Auto-Muting Active" else "System Paused"
            )

            Spacer(modifier = Modifier.weight(0.5f))

            // --- STATUS CARD ---
            LocationStatusCard(
                locationText = viewModel.locationText,
                isSaved = viewModel.isLocationSaved,
                isActive = viewModel.isLocationMuted,
                onCopy = {
                    viewModel.locationText?.let { text ->
                        context.copyToClipboard(text, "Location Address")
                    }
                },
                onPrimaryAction = {
                    if (!viewModel.isLocationSaved) {
                        // CRITICAL FIX: Changed !== (reference check) to != (value check)
                        if (viewModel.currentLatitude != null && viewModel.currentLongitude != null) {
                            viewModel.saveLocation()
                        }
                    } else {
                        onNavigateToManage(viewModel.locationId)
                    }
                }
            )

            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

// --- 1. MODERN HEADER ---
@Composable
fun ModernHeader(onSettingsClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 24.dp, vertical = 24.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(verticalArrangement = Arrangement.Center) {
            // This calls the component defined below
            AppLogo()

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Silence is Golden",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary,
                letterSpacing = 0.5.sp
                // Removed manual padding; alignment handles it now
            )
        }

        IconButton(
            onClick = onSettingsClick,
            colors = IconButtonDefaults.filledTonalIconButtonColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerHighest.copy(alpha = 0.5f)
            )
        ) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = "Settings",
                modifier = Modifier.size(22.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

// --- 1.1 APP LOGO COMPONENT (Was missing in your code) ---
@Composable
fun AppLogo() {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = Icons.Default.GraphicEq, // Sound-wave style icon
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(28.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "MuteMaster",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

// --- 2. PULSE INDICATOR ---
@Composable
fun PulseIndicator(
    isActive: Boolean,
    statusText: String
) {
    // 1. SMOOTH COLOR TRANSITIONS
    // Instead of snapping, we animate the color over 500ms
    val coreColor by animateColorAsState(
        targetValue = if (isActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceContainerHighest,
        animationSpec = tween(500),
        label = "color"
    )

    val iconColor by animateColorAsState(
        targetValue = if (isActive) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
        animationSpec = tween(500),
        label = "iconColor"
    )

    // 2. THE PULSE ANIMATION (Optimized)
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")

    // We only run the pulse animations if active, but we handle the "off" state gracefully
    val pulseScale by if (isActive) {
        infiniteTransition.animateFloat(
            initialValue = 1f,
            targetValue = 1.2f, // Reduced growth slightly for smoothness
            animationSpec = infiniteRepeatable(
                animation = tween(1200, easing = LinearOutSlowInEasing),
                repeatMode = RepeatMode.Reverse // Reverse makes it breathe (In-Out) instead of Snap-Reset
            ),
            label = "pulseScale"
        )
    } else {
        remember { mutableFloatStateOf(1f) }
    }

    val rippleScale by if (isActive) {
        infiniteTransition.animateFloat(
            initialValue = 1f,
            targetValue = 1.6f, // Larger ripple
            animationSpec = infiniteRepeatable(
                animation = tween(1500, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            ),
            label = "rippleScale"
        )
    } else {
        remember { mutableFloatStateOf(0f) }
    }

    val rippleAlpha by if (isActive) {
        infiniteTransition.animateFloat(
            initialValue = 0.4f,
            targetValue = 0f,
            animationSpec = infiniteRepeatable(
                animation = tween(1500, easing = LinearEasing), // Match duration with rippleScale
                repeatMode = RepeatMode.Restart
            ),
            label = "rippleAlpha"
        )
    } else {
        remember { mutableFloatStateOf(0f) }
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(contentAlignment = Alignment.Center) {

            // RIPPLE LAYER (Only visible when active)
            // We gently fade this in/out using AnimatedVisibility to avoid the "Pop"
            androidx.compose.animation.AnimatedVisibility(
                visible = isActive,
                enter = fadeIn(animationSpec = tween(500)),
                exit = fadeOut(animationSpec = tween(500))
            ) {
                Box(
                    modifier = Modifier
                        .size(180.dp) // Match core size
                        .scale(rippleScale)
                        .alpha(rippleAlpha)
                        .background(coreColor, CircleShape)
                )
            }

            // CORE CIRCLE
            Surface(
                modifier = Modifier
                    .size(180.dp)
                    .scale(pulseScale), // The "Breathing" effect
                shape = CircleShape,
                color = coreColor,
                shadowElevation = 10.dp
            ) {
                Box(contentAlignment = Alignment.Center) {
                    // 3. SMOOTH ICON SWAP
                    // AnimatedContent slides the new icon in while the old one fades out
                    AnimatedContent (
                        targetState = isActive,
                        transitionSpec = {
                            fadeIn(animationSpec = tween(300)) + scaleIn() togetherWith
                                    fadeOut(animationSpec = tween(300)) + scaleOut()
                        },
                        label = "iconSwap"
                    ) { active ->
                        Icon(
                            imageVector = if (active) Icons.Rounded.NotificationsOff else Icons.Rounded.Pause,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = iconColor
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // STATUS TEXT (Crossfade this too for extra polish)
        AnimatedContent(
            targetState = statusText,
            transitionSpec = {
                fadeIn(tween(300)) togetherWith fadeOut(tween(300))
            },
            label = "textSwap"
        ) { text ->
            Text(
                text = text,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = if (isActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

// --- 3. STATUS CARD ---
@Composable
fun LocationStatusCard(
    locationText: String?,
    isSaved: Boolean,
    isActive: Boolean,
    onCopy: () -> Unit,
    onPrimaryAction: () -> Unit
) {
    val (statusColor, statusText, icon) = when {
        !isSaved -> Triple(MaterialTheme.colorScheme.secondary, "Not Saved", Icons.Outlined.PinDrop)
        isActive -> Triple(MaterialTheme.colorScheme.primary, "Active Zone", Icons.Default.LocationOn)
        else -> Triple(MaterialTheme.colorScheme.tertiary, "Inactive Zone", Icons.Default.LocationOff)
    }

    ElevatedCard(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.Top) {
                Surface(
                    shape = CircleShape,
                    color = statusColor.copy(alpha = 0.1f),
                    modifier = Modifier.size(48.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = statusColor,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = statusText.uppercase(),
                        style = MaterialTheme.typography.labelSmall,
                        color = statusColor,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = locationText ?: "Locating...",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.2f))
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(onClick = onCopy) {
                    Icon(Icons.Default.ContentCopy, null, Modifier.size(16.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Copy", style = MaterialTheme.typography.labelLarge)
                }

                FilledTonalButton(
                    onClick = onPrimaryAction,
                    contentPadding = PaddingValues(horizontal = 24.dp)
                ) {
                    Icon(
                        imageVector = if (isSaved) Icons.Default.Edit else Icons.Default.AddLocation,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(if (isSaved) "Manage" else "Save Zone")
                }
            }
        }
    }
}