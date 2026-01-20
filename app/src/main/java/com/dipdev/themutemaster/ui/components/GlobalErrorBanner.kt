package com.dipdev.themutemaster.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.NotificationsOff
import androidx.compose.material.icons.rounded.PriorityHigh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dipdev.themutemaster.ui.viewmodel.AppError

@Composable
fun GlobalErrorBanner(
    error: AppError,
    onFixClick: () -> Unit
) {
    // Determine Severity
    val isCritical = error != AppError.NOTIFICATION_MISSING

    // 1. Dynamic Colors & Text
    val containerColor = if (isCritical) MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.tertiaryContainer
    val contentColor = if (isCritical) MaterialTheme.colorScheme.onErrorContainer else MaterialTheme.colorScheme.onTertiaryContainer

    val (title, desc) = when (error) {
        AppError.DND_MISSING -> "Permission Revoked" to "MuteMaster cannot silence calls."
        AppError.LOCATION_FG_MISSING,
        AppError.LOCATION_BG_MISSING -> "Automation Stopped" to "Location access is required."
        AppError.NOTIFICATION_MISSING -> "Updates Paused" to "Status alerts are disabled."
    }

    val iconVector = if (isCritical) Icons.Rounded.PriorityHigh else Icons.Outlined.NotificationsOff

    // 2. Animated Appearance
    AnimatedVisibility(
        visible = true,
        enter = expandVertically() + fadeIn(),
        exit = shrinkVertically() + fadeOut(),
        modifier = Modifier.statusBarsPadding().padding(horizontal = 16.dp).padding(top = 16.dp, bottom = 0.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = containerColor,
                    contentColor = contentColor
                ),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onFixClick() } // Make whole card tappable
            ) {
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 3. Icon Bubble (Adds Polish)
                    Surface(
                        shape = CircleShape,
                        color = contentColor.copy(alpha = 0.2f), // Semi-transparent circle
                        modifier = Modifier.size(40.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = iconVector,
                                contentDescription = null,
                                tint = contentColor,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    // 4. Text Content
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = desc,
                            style = MaterialTheme.typography.bodySmall,
                            color = contentColor.copy(alpha = 0.9f)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    // 5. High-Contrast "Pill" Button
                    // We invert the colors here: Background is Dark, Text is Light
                    Surface(
                        shape = RoundedCornerShape(50),
                        color = contentColor,
                        modifier = Modifier.height(32.dp)
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        ) {
                            Text(
                                text = "FIX",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = containerColor // Inverted Text Color
                            )
                        }
                    }
                }
            }
        }
    }
}