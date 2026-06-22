package com.dipdev.themutemaster.ui.screens.onboarding

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material.icons.rounded.AccessTime
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.NotificationsOff
import androidx.compose.material.icons.rounded.Security
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dipdev.themutemaster.ui.components.AppLogo

@Composable
fun Welcome(onGetStarted: () -> Unit) {

    // --- Entrance animations ---
    val enterAlpha = remember { Animatable(0f) }
    val enterSlide = remember { Animatable(32f) }

    LaunchedEffect(Unit) {
        enterAlpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 700, easing = EaseOutCubic)
        )
    }
    LaunchedEffect(Unit) {
        enterSlide.animateTo(
            targetValue = 0f,
            animationSpec = tween(durationMillis = 700, easing = EaseOutCubic)
        )
    }

    // --- Infinite breathing glow behind hero ---
    val infiniteTransition = rememberInfiniteTransition(label = "glow")
    val heroFloat by infiniteTransition.animateFloat(
        initialValue = -8f,
        targetValue = 8f,
        animationSpec = infiniteRepeatable(
            animation = tween(3200, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "heroFloat"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    alpha = enterAlpha.value
                    translationY = enterSlide.value
                },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // ── Top Logo ──────────────────────────────────────────────────
            Spacer(Modifier.height(56.dp))
            AppLogo()

            // ── Hero Illustration area ────────────────────────────────────
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                // Hero illustration — built in Compose, no PNG needed
                HeroIllustration(
                    modifier = Modifier
                        .size(260.dp)
                        .graphicsLayer { translationY = heroFloat }
                )
            }

            // ── Bottom Content Card ───────────────────────────────────────
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(topStart = 36.dp, topEnd = 36.dp),
                color = MaterialTheme.colorScheme.surfaceContainer,
                tonalElevation = 0.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 28.dp)
                        .padding(top = 36.dp, bottom = 40.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    // Tagline chip
                    Surface(
                        shape = RoundedCornerShape(50),
                        color = MaterialTheme.colorScheme.primaryContainer,
                    ) {
                        Text(
                            text = "✦  Silence is Golden  ✦",
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            letterSpacing = 0.5.sp
                        )
                    }

                    Spacer(Modifier.height(18.dp))

                    // Main Headline
                    Text(
                        text = "Your phone,\nauto-silenced.",
                        style = MaterialTheme.typography.displaySmall,
                        fontWeight = FontWeight.ExtraBold,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurface,
                        lineHeight = 40.sp
                    )

                    Spacer(Modifier.height(14.dp))

                    // Sub-copy
                    Text(
                        text = "Set a location or a time. Walk in — phone goes silent. Walk out — it's back. Zero effort.",
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 24.sp
                    )

                    Spacer(Modifier.height(32.dp))

                    // CTA Button
                    Button(
                        onClick = onGetStarted,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp)
                    ) {
                        Text(
                            text = "Get Started",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.width(10.dp))
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.ArrowForward,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(Modifier.height(16.dp))

                    // Privacy & Legal
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Security,
                                contentDescription = null,
                                modifier = Modifier.size(14.dp),
                                tint = MaterialTheme.colorScheme.outline
                            )
                            Spacer(Modifier.width(5.dp))
                            Text(
                                text = "100% local · No data leaves your device",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.outline
                            )
                        }
                        
                        Spacer(Modifier.height(16.dp))
                        
                        Text(
                            text = "By getting started, you agree to our",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.outline
                        )
                        Spacer(Modifier.height(2.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "Privacy Policy",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = " & ",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.outline
                            )
                            Text(
                                text = "Terms of Service",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Hero Illustration — drawn entirely in Compose using M3 tokens
// ─────────────────────────────────────────────────────────────────────────────
@Composable
private fun HeroIllustration(modifier: Modifier = Modifier) {
    val primary = MaterialTheme.colorScheme.primary
    val primaryContainer = MaterialTheme.colorScheme.primaryContainer
    val surfaceVariant = MaterialTheme.colorScheme.surfaceContainerHighest
    val onSurface = MaterialTheme.colorScheme.onSurface

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {

        // ── Central phone frame ───────────────────────────────────────────
        Box(
            modifier = Modifier
                .width(120.dp)
                .height(200.dp)
                .clip(RoundedCornerShape(22.dp))
                .background(primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            // Phone screen inner
            Box(
                modifier = Modifier
                    .width(104.dp)
                    .height(184.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(MaterialTheme.colorScheme.surfaceContainerLow),
                contentAlignment = Alignment.Center
            ) {
                // Muted bell icon — the star of the show
                Icon(
                    imageVector = Icons.Rounded.NotificationsOff,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    tint = primary
                )
            }

            // Notch
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 8.dp)
                    .width(30.dp)
                    .height(8.dp)
                    .clip(RoundedCornerShape(50))
                    .background(primaryContainer)
            )
        }

        // ── Floating card: Location (top-left) ───────────────────────────
        FloatingBadge(
            modifier = Modifier
                .align(Alignment.TopStart)
                .offset(x = 6.dp, y = 42.dp),
            icon = {
                Icon(
                    imageVector = Icons.Rounded.LocationOn,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                    tint = primary
                )
            },
            label = "Location",
            surfaceVariant = surfaceVariant,
            onSurface = onSurface
        )

        // ── Floating card: Schedule (bottom-right) ────────────────────────
        FloatingBadge(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(x = (-6).dp, y = (-42).dp),
            icon = {
                Icon(
                    imageVector = Icons.Rounded.AccessTime,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                    tint = primary
                )
            },
            label = "Schedule",
            surfaceVariant = surfaceVariant,
            onSurface = onSurface
        )
    }
}

@Composable
private fun FloatingBadge(
    modifier: Modifier = Modifier,
    icon: @Composable () -> Unit,
    label: String,
    surfaceVariant: androidx.compose.ui.graphics.Color,
    onSurface: androidx.compose.ui.graphics.Color
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        color = surfaceVariant,
        shadowElevation = 8.dp,
        tonalElevation = 2.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            icon()
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.SemiBold,
                color = onSurface
            )
        }
    }
}
