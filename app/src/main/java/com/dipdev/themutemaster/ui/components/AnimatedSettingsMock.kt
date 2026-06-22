package com.dipdev.themutemaster.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

enum class SettingsMockType {
    BACKGROUND_LOCATION,
    DND_ACCESS
}

@Composable
fun AnimatedSettingsMock(
    type: SettingsMockType,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(280.dp),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceContainerHighest,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Screen Header
            Column(modifier = Modifier.fillMaxSize()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp),
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = if (type == SettingsMockType.BACKGROUND_LOCATION) "Location permission" else "Do Not Disturb access",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                
                // Screen Content
                when (type) {
                    SettingsMockType.BACKGROUND_LOCATION -> BackgroundLocationContent()
                    SettingsMockType.DND_ACCESS -> DndAccessContent()
                }
            }
        }
    }
}

@Composable
private fun BackgroundLocationContent() {
    val infiniteTransition = rememberInfiniteTransition(label = "bg_loc_anim")
    
    // Animation phases
    // 0f -> 1f (Wait)
    // 1f -> 2f (Move Finger)
    // 2f -> 3f (Tap & Change state)
    // 3f -> 4f (Wait & Reset)
    val progress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 4f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "progress"
    )

    val isTapped = progress in 2f..3.8f
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp)
    ) {
        Text("Location access for MuteMaster", fontSize = 12.sp, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        
        RadioOption(text = "Allow all the time", selected = isTapped)
        RadioOption(text = "Allow only while using the app", selected = !isTapped)
        RadioOption(text = "Ask every time", selected = false)
        RadioOption(text = "Don't allow", selected = false)
    }

    // Finger Animation
    Box(modifier = Modifier.fillMaxSize()) {
        val yOffset = if (progress < 1f) 100 else if (progress < 2f) 100 - ((progress - 1f) * 60).toInt() else 40
        val scale = if (progress in 2.0f..2.2f) 0.8f else 1f
        val alpha = if (progress > 3.8f) 0f else if (progress < 0.5f) (progress * 2f) else 1f
        
        Icon(
            imageVector = Icons.Default.TouchApp,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .offset { IntOffset(x = 300, y = yOffset.dp.roundToPx()) }
                .scale(scale)
                .alpha(alpha)
                .size(48.dp)
        )
    }
}

@Composable
private fun DndAccessContent() {
    val infiniteTransition = rememberInfiniteTransition(label = "dnd_anim")
    val progress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 4f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "progress"
    )

    val isTapped = progress in 2f..3.8f

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp)
    ) {
        Text("Allowed apps", fontSize = 12.sp, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        
        SwitchOption("Digital Wellbeing", false)
        SwitchOption("Google Play Services", true)
        SwitchOption("MuteMaster", isTapped, highlight = true)
        SwitchOption("System UI", true)
    }

    // Finger Animation
    Box(modifier = Modifier.fillMaxSize()) {
        val yOffset = if (progress < 1f) 200 else if (progress < 2f) 200 - ((progress - 1f) * 60).toInt() else 140
        val scale = if (progress in 2.0f..2.2f) 0.8f else 1f
        val alpha = if (progress > 3.8f) 0f else if (progress < 0.5f) (progress * 2f) else 1f
        
        Icon(
            imageVector = Icons.Default.TouchApp,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .offset { IntOffset(x = 700, y = yOffset.dp.roundToPx()) }
                .scale(scale)
                .alpha(alpha)
                .size(48.dp)
        )
    }
}

@Composable
private fun RadioOption(text: String, selected: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(20.dp)
                .border(2.dp, if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant, CircleShape)
                .padding(4.dp)
        ) {
            if (selected) {
                Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.primary, CircleShape))
            }
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text(text, fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurface)
    }
}

@Composable
private fun SwitchOption(text: String, checked: Boolean, highlight: Boolean = false) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(if (highlight) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f) else Color.Transparent, RoundedCornerShape(8.dp))
            .padding(vertical = 12.dp, horizontal = if(highlight) 8.dp else 0.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text, fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurface, fontWeight = if(highlight) FontWeight.Bold else FontWeight.Normal)
        Switch(checked = checked, onCheckedChange = null, modifier = Modifier.scale(0.8f))
    }
}
