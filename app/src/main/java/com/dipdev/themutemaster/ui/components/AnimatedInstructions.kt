package com.dipdev.themutemaster.ui.components

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AnimatedToggleInstruction(
    modifier: Modifier = Modifier,
    targetName: String,
    icon: ImageVector
) {
    val infiniteTransition = rememberInfiniteTransition(label = "toggle_instruction")

    val fingerAlpha by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 3000
                0f at 0
                0f at 800
                0.5f at 1200
                0f at 1600
                0f at 3000
            },
            repeatMode = RepeatMode.Restart
        ),
        label = "finger_alpha"
    )

    val fingerScale by infiniteTransition.animateFloat(
        initialValue = 1.5f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 3000
                1.5f at 0
                1.5f at 800
                1f at 1200
                1.5f at 1600
                1.5f at 3000
            },
            repeatMode = RepeatMode.Restart
        ),
        label = "finger_scale"
    )

    val switchOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 20f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 3000
                0f at 0
                0f at 1200
                20f at 1500
                20f at 3000
            },
            repeatMode = RepeatMode.Restart
        ),
        label = "switch_offset"
    )

    val surfaceVariant = MaterialTheme.colorScheme.surfaceVariant
    val primary = MaterialTheme.colorScheme.primary

    val switchColor by infiniteTransition.animateColor(
        initialValue = surfaceVariant,
        targetValue = primary,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 3000
                surfaceVariant at 0
                surfaceVariant at 1200
                primary at 1500
                primary at 3000
            },
            repeatMode = RepeatMode.Restart
        ),
        label = "switch_color"
    )

    // The mock screen outline
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(140.dp)
            .border(2.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(24.dp))
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(24.dp))
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceContainerHighest, RoundedCornerShape(12.dp))
                .padding(horizontal = 16.dp, vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = targetName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Box(contentAlignment = Alignment.Center) {
                // Track
                Box(
                    modifier = Modifier
                        .width(44.dp)
                        .height(24.dp)
                        .background(switchColor, RoundedCornerShape(12.dp))
                )
                // Thumb
                Box(
                    modifier = Modifier
                        .padding(start = 2.dp, end = 22.dp)
                        .offset(x = switchOffset.dp)
                        .size(20.dp)
                        .background(MaterialTheme.colorScheme.surface, CircleShape)
                )

                // Animated Finger Tap
                Box(
                    modifier = Modifier
                        .offset(x = 10.dp, y = 10.dp)
                        .size(40.dp)
                        .scale(fingerScale)
                        .alpha(fingerAlpha)
                        .background(MaterialTheme.colorScheme.tertiary.copy(alpha = 0.4f), CircleShape)
                )
            }
        }
    }
}

@Composable
fun AnimatedListInstruction(
    modifier: Modifier = Modifier,
    targetOption: String
) {
    val infiniteTransition = rememberInfiniteTransition(label = "list_instruction")

    val fingerAlpha by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 3000
                0f at 0
                0f at 800
                0.5f at 1200
                0f at 1600
                0f at 3000
            },
            repeatMode = RepeatMode.Restart
        ),
        label = "finger_alpha_list"
    )

    val fingerScale by infiniteTransition.animateFloat(
        initialValue = 1.5f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 3000
                1.5f at 0
                1.5f at 800
                1f at 1200
                1.5f at 1600
                1.5f at 3000
            },
            repeatMode = RepeatMode.Restart
        ),
        label = "finger_scale_list"
    )

    val transparent = Color.Transparent
    val primaryContainer = MaterialTheme.colorScheme.primaryContainer

    val rowHighlight by infiniteTransition.animateColor(
        initialValue = transparent,
        targetValue = primaryContainer,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 3000
                transparent at 0
                transparent at 1200
                primaryContainer at 1300
                primaryContainer at 3000
            },
            repeatMode = RepeatMode.Restart
        ),
        label = "row_highlight"
    )

    val outline = MaterialTheme.colorScheme.outline
    val primary = MaterialTheme.colorScheme.primary

    val radioColor by infiniteTransition.animateColor(
        initialValue = outline,
        targetValue = primary,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 3000
                outline at 0
                outline at 1200
                primary at 1300
                primary at 3000
            },
            repeatMode = RepeatMode.Restart
        ),
        label = "radio_color"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(180.dp)
            .border(2.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(24.dp))
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(24.dp))
            .padding(vertical = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Location permission",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
            )
            
            // Dummy Row 1
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(modifier = Modifier.size(20.dp).border(2.dp, MaterialTheme.colorScheme.outline, CircleShape))
                Spacer(modifier = Modifier.width(16.dp))
                Text("Allow only while using the app", style = MaterialTheme.typography.bodyMedium)
            }

            // Target Row
            Box(contentAlignment = Alignment.CenterEnd) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(rowHighlight)
                        .padding(horizontal = 20.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Radio button
                    Box(
                        modifier = Modifier.size(20.dp).border(2.dp, radioColor, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        if (radioColor == MaterialTheme.colorScheme.primary) {
                            Box(modifier = Modifier.size(10.dp).background(MaterialTheme.colorScheme.primary, CircleShape))
                        }
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(targetOption, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                }

                // Finger tap
                Box(
                    modifier = Modifier
                        .padding(end = 40.dp)
                        .size(40.dp)
                        .scale(fingerScale)
                        .alpha(fingerAlpha)
                        .background(MaterialTheme.colorScheme.tertiary.copy(alpha = 0.4f), CircleShape)
                )
            }
        }
    }
}
