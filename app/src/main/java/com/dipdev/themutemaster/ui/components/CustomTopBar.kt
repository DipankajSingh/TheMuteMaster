package com.dipdev.themutemaster.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun CustomTopBar(
    title: String? = null, // Optional title
    onBackClick: () -> Unit,
    // Make these nullable. If null, we show nothing.
    actionIcon: ImageVector? = null,
    actionText: String? = null,
    onActionClick: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp) // Standard Material TopBar height
            .padding(horizontal = 4.dp), // Safe breathing room
        horizontalArrangement = Arrangement.SpaceBetween, // Pushes items to edges
        verticalAlignment = Alignment.CenterVertically // Centers them vertically
    ) {
        // --- 1. LEFT SIDE (Back Button) ---
        IconButton(onClick = onBackClick) {
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.ArrowBack, // Standard back icon
                contentDescription = "Back"
            )
        }

        // --- 2. CENTER (Optional Title) ---
        // If you want a title in the middle:
        if (title != null) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
        } else {
            Spacer(Modifier.width(48.dp)) // Balance the layout if no title
        }

        // --- 3. RIGHT SIDE (Action) ---
        // Logic: Prioritize Text. If no Text, check for Icon. If neither, show empty space.
        Box(
            modifier = Modifier.sizeIn(minWidth = 48.dp, minHeight = 48.dp), // Ensure touch target size
            contentAlignment = Alignment.CenterEnd
        ) {
            when {
                actionText != null && onActionClick != null -> {
                    TextButton(onClick = onActionClick) {
                        Text(
                            text = actionText,
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                actionIcon != null && onActionClick != null -> {
                    IconButton(onClick = onActionClick) {
                        Icon(
                            imageVector = actionIcon,
                            contentDescription = "Action",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
                else -> {
                    // Empty Spacer to keep the left icon pushed to the edge if needed
                    Spacer(Modifier.width(48.dp))
                }
            }
        }
    }
}