package com.dipdev.themutemaster.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dipdev.themutemaster.ui.components.AppLogo

@Composable
fun Home(modifier: Modifier= Modifier) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
           // Matches image background
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 1. Top Bar
        TopHeader()

        Spacer(modifier = Modifier.height(32.dp))

        // 2. The Switch (Placeholder for your custom canvas work)
        Box(
            modifier = Modifier
                .weight(1f) // Takes up available vertical space to center itself
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            // REPLACE THIS with your Custom Canvas Switch
            // I put a placeholder circle here just to hold the space
            Box(
                modifier = Modifier
                    .size(250.dp)
                    .background(MaterialTheme.colorScheme.surfaceContainer, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Settings, // Power icon usually
                    contentDescription = "Switch",
                    modifier = Modifier.size(64.dp),
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // 3. Location Status Card
        LocationStatusCard()

        Spacer(modifier = Modifier.height(24.dp))

        // 4. Bottom Action Buttons
    }
}

// --- Component 1: Top Header ---
@Composable
fun TopHeader() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            AppLogo()
        }

        IconButton(onClick = { /* Open Settings */ }) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = "Settings",
                modifier = Modifier.size(28.dp),
            )
        }
    }
}

// --- Component 2: The Gray Status Card ---
@Composable
fun LocationStatusCard() {
    Card(
        shape = RoundedCornerShape(4.dp), // Sharp corners as per image
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            // Address Row
            Row(verticalAlignment = Alignment.Top) {
                Icon(
                    imageVector = Icons.Outlined.Home,
                    contentDescription = null,
                    modifier = Modifier.size(32.dp),
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = "17-a, New India Centre, Cooperage Road, Opp Oval Maidan, Council Hall",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        lineHeight = 20.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "This location is not in your mutes list",
                        fontSize = 14.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Actions Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ActionLabel(icon = Icons.Default.ContentCopy, text = "copy address")
                ActionLabel(icon = Icons.Default.Edit, text = "Manage this\nLocation")
            }
        }
    }
}

@Composable
fun ActionLabel(icon: ImageVector, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clickable { /* Handle action */ }
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(24.dp),
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            lineHeight = 16.sp
        )
    }
}

// --- Component 3: Bottom Buttons ---


@Composable
fun BigActionButton(modifier: Modifier = Modifier, icon: ImageVector, text: String) {
    Button(
        onClick = {},
        modifier = modifier.fillMaxHeight(),
        shape = RoundedCornerShape(4.dp),
        contentPadding = PaddingValues(horizontal = 12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start, // Align left inside button
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = text,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                lineHeight = 18.sp,
                textAlign = TextAlign.Start
            )
        }
    }
}

