package com.dipdev.themutemaster.ui.screens.savedLocations

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// 1. Data Model (Mock)
data class SavedLocation(
    val id: String,
    val title: String,
    val address: String,
    val isMuted: Boolean,
    val rangeRadius: Float, // in meters
    val icon: ImageVector = Icons.Filled.Home
)

@Composable
fun SavedLocationCard(
    location: SavedLocation,
    onToggleMute: (Boolean) -> Unit,
    onDelete: () -> Unit,
    onCopyAddress: () -> Unit,
    onRangeChanged: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    // State for expansion (Progressive Disclosure)
    var expanded by remember { mutableStateOf(false) }

    // Animation for the arrow rotation
    val rotationState by animateFloatAsState(if (expanded) 180f else 0f)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize(), // Smooth resize when expanding
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
    ) {
        Column(
            modifier = Modifier
                .clickable(
                    // 1. Create a source to track touches (required)
                    interactionSource = remember { MutableInteractionSource() },
                    // 2. Kill the visual effect
                    indication = null,
                    onClick = { expanded = !expanded }
                )
                .padding(16.dp)
        ) {
            // --- TOP ROW: Icon | Title | Mute Switch | Expand Arrow ---
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    // 1. Custom Icon Container
                    Surface(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = CircleShape,
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            imageVector = location.icon,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.padding(8.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    // 7. Title
                    Column {
                        Text(
                            text = location.title,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        // Status Text
                        Text(
                            text = if (location.isMuted) "Active" else "Inactive",
                            style = MaterialTheme.typography.labelSmall,
                            color = if (location.isMuted) MaterialTheme.colorScheme.primary else Color.Gray
                        )
                    }
                }

                // 3. Mute/Unmute Switch
                Switch(
                    checked = location.isMuted,
                    onCheckedChange = { onToggleMute(it) },
                    modifier = Modifier.scale(0.8f)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // --- MIDDLE ROW: Address ---
            // 2. Full Address
            Row(verticalAlignment = Alignment.Top) {
                Icon(
                    imageVector = Icons.Outlined.LocationOn,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp).offset(y = 2.dp),
                    tint = MaterialTheme.colorScheme.outline
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = location.address,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = if (expanded) Int.MAX_VALUE else 1, // Expandable text
                    overflow = TextOverflow.Ellipsis
                )
            }

            // --- EXPANDED SECTION (The Controls) ---
            AnimatedVisibility(visible = expanded) {
                Column(modifier = Modifier.padding(top = 16.dp)) {
                    Divider(color = MaterialTheme.colorScheme.outlineVariant)
                    Spacer(modifier = Modifier.height(16.dp))

                    // 5. Mute Range Slider
                    Text(
                        text = "Mute Radius: ${location.rangeRadius.toInt()}m",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Slider(
                        value = location.rangeRadius,
                        onValueChange = onRangeChanged,
                        valueRange = 50f..500f, // 50m to 500m
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Bottom Action Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // 4. Copy Address
                        TextButton(onClick = onCopyAddress) {
                            Icon(Icons.Outlined.ContentCopy, null, modifier = Modifier.size(18.dp))
                            Spacer(Modifier.width(8.dp))
                            Text("Copy Address")
                        }

                        // 6. Remove Button
                        TextButton(
                            onClick = onDelete,
                            colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
                        ) {
                            Icon(Icons.Outlined.Delete, null, modifier = Modifier.size(18.dp))
                            Spacer(Modifier.width(8.dp))
                            Text("Delete")
                        }
                    }
                }
            }

            // Visual Hint to expand (Centered Arrow)
            if (!expanded) {
                Box(
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "Expand",
                        tint = MaterialTheme.colorScheme.outlineVariant
                    )
                }
            }
        }
    }
}