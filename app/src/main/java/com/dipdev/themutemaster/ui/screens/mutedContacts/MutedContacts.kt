package com.dipdev.themutemaster.ui.screens.mutedContacts

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat

@Composable
fun ComingSoonScreen(onClose: () -> Unit) {
    val view = LocalView.current
    val darkTheme = isSystemInDarkTheme()

    DisposableEffect(view) {
        val window = (view.context as Activity).window
        val controller = WindowCompat.getInsetsController(window, view)

        controller.isAppearanceLightStatusBars = false

        onDispose {
            controller.isAppearanceLightStatusBars = !darkTheme
        }
    }

    val premiumGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF1A1A2E), // Deep Navy
            Color(0xFF16213E), // Dark Blue
            Color(0xFF0F3460)  // Rich Indigo
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(premiumGradient)
    ) {
        // Decorative background glow (Behind the card)
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .offset(y = (-50).dp)
                .size(300.dp)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            Color(0xFFE94560).copy(alpha = 0.2f), // Subtle Red/Pink Glow
                            Color.Transparent
                        )
                    ),
                    CircleShape
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp)) // Status bar spacing
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End // Push to Right
            ) {
                // Semi-transparent Close Button
                IconButton(
                    onClick = onClose,
                    modifier = Modifier
                        .background(Color.White.copy(0.1f), CircleShape)
                        .size(40.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Close,
                        contentDescription = "Close",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
            // --- 1. TOP BADGE ---
            Surface(
                color = Color(0xFFE94560).copy(alpha = 0.1f),
                shape = RoundedCornerShape(50),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE94560).copy(alpha = 0.5f))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Rounded.Star,
                        contentDescription = null,
                        tint = Color(0xFFE94560),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "PREMIUM FEATURE",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFFE94560),
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // --- 2. HERO TITLE ---
            Text(
                text = "Priority\nGatekeeper",
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.Black,
                color = Color.White,
                textAlign = TextAlign.Center,
                lineHeight = 44.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Don't miss the calls that matter.\nWhitelist VIP contacts to bypass silence.",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White.copy(alpha = 0.7f),
                textAlign = TextAlign.Center,
                lineHeight = 24.sp
            )

            Spacer(modifier = Modifier.height(48.dp))

            // --- 3. THE "GLASS" CARD (Mock UI) ---
            // Simulates a frosted glass effect
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White.copy(alpha = 0.08f) // Translucent
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    // "Active Call" Simulation Header
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(Color(0xFF4CAF50), CircleShape) // Green dot
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "WHITELIST ACTIVE",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White.copy(alpha = 0.5f),
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Mock Rows
                    GlassContactRow("Mom", "Mobile • +91 98...", isActive = true)
                    Spacer(modifier = Modifier.height(16.dp))
                    GlassContactRow("Boss", "Work • +91 11...", isActive = true)
                    Spacer(modifier = Modifier.height(16.dp))
                    GlassContactRow("Spam / Others", "Blocked by MuteMaster", isActive = false, isDimmed = true)
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            // --- 4. BOTTOM INFO ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                PremiumFeatureItem(Icons.Rounded.Lock, "Secure", "Local Data Only")
                PremiumFeatureItem(Icons.Rounded.Bolt, "Instant", "Zero Latency")
            }

            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = "COMING SOON IN V2.0",
                style = MaterialTheme.typography.labelMedium,
                color = Color.White.copy(alpha = 0.3f),
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp
            )

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

// --- HELPER COMPONENTS ---

@Composable
fun GlassContactRow(name: String, detail: String, isActive: Boolean, isDimmed: Boolean = false) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            // Gradient Avatar
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(
                        brush = if (isDimmed) {
                            SolidColor(Color.White.copy(0.1f)) // Treat this Color as a Brush
                        } else {
                            Brush.linearGradient(listOf(Color(0xFFE94560), Color(0xFF0F3460)))
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = name.take(1),
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = if (isDimmed) Color.White.copy(0.3f) else Color.White
                )
                Text(
                    text = detail,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.5f)
                )
            }
        }

        // Custom Switch Look
        Icon(
            imageVector = if (isActive) Icons.Rounded.ToggleOn else Icons.Rounded.ToggleOff,
            contentDescription = null,
            tint = if (isActive) Color(0xFFE94560) else Color.White.copy(0.2f),
            modifier = Modifier.size(36.dp)
        )
    }
}

@Composable
fun PremiumFeatureItem(icon: androidx.compose.ui.graphics.vector.ImageVector, title: String, subtitle: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color(0xFFE94560),
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodySmall,
            color = Color.White.copy(alpha = 0.5f)
        )
    }
}