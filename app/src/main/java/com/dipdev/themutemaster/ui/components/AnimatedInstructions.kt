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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.Image
import androidx.compose.ui.viewinterop.AndroidView
import android.widget.ImageView
import com.dipdev.themutemaster.R
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn

@Composable
fun MockDeviceFrame(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
            .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
            .background(Color.Black)
            .padding(start = 8.dp, top = 8.dp, end = 8.dp)
            .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
            .background(Color.White)
            .pointerInput(Unit) {
                awaitPointerEventScope {
                    while (true) {
                        val event = awaitPointerEvent(pass = PointerEventPass.Initial)
                        event.changes.forEach { it.consume() }
                    }
                }
            }
    ) {
        content()
    }
}

@Composable
fun MockStatusBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Left: Time
        Text("17:46", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.DarkGray)
        
        // Center: Camera Pill (Dynamic Island)
        Box(
            modifier = Modifier
                .width(80.dp)
                .height(20.dp)
                .background(Color(0xFF222222), RoundedCornerShape(10.dp))
        )
        
        // Right: Status Icons
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            // Cellular bars
            Row(horizontalArrangement = Arrangement.spacedBy(1.dp), verticalAlignment = Alignment.Bottom, modifier = Modifier.height(12.dp)) {
                Box(modifier = Modifier.width(3.dp).height(4.dp).background(Color.Gray))
                Box(modifier = Modifier.width(3.dp).height(6.dp).background(Color.Gray))
                Box(modifier = Modifier.width(3.dp).height(9.dp).background(Color.Gray))
                Box(modifier = Modifier.width(3.dp).height(12.dp).background(Color.Gray))
            }
            // Battery
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier.size(width = 18.dp, height = 10.dp)
                        .border(1.dp, Color.Gray, RoundedCornerShape(2.dp))
                        .padding(2.dp)
                ) {
                    Box(modifier = Modifier.fillMaxSize().background(Color.Gray))
                }
                Box(modifier = Modifier.size(width = 2.dp, height = 4.dp).background(Color.Gray, RoundedCornerShape(topEnd = 1.dp, bottomEnd = 1.dp)))
            }
        }
    }
}

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

    MockDeviceFrame(modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
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
}



@Composable
fun AnimatedLocationInstruction(
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "location_instruction")

    // Finger drops down from top
    val fingerOffsetY by infiniteTransition.animateFloat(
        initialValue = -50f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 3000
                -50f at 0
                0f at 800
                0f at 1600
                100f at 2000
                100f at 3000
            },
            repeatMode = RepeatMode.Restart
        ),
        label = "finger_offset_y"
    )

    val fingerAlpha by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 3000
                0f at 0
                1f at 600
                1f at 1600
                0f at 2000
                0f at 3000
            },
            repeatMode = RepeatMode.Restart
        ),
        label = "finger_alpha_location"
    )

    val fingerScale by infiniteTransition.animateFloat(
        initialValue = 1.2f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 3000
                1.2f at 0
                1.2f at 800
                0.9f at 1200
                1.2f at 1600
                1.2f at 3000
            },
            repeatMode = RepeatMode.Restart
        ),
        label = "finger_scale_location"
    )

    val radioColor by infiniteTransition.animateColor(
        initialValue = Color.Transparent,
        targetValue = Color(0xFF6B67FD),
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 3000
                Color.Transparent at 0
                Color.Transparent at 1000
                Color(0xFF6B67FD) at 1200
                Color(0xFF6B67FD) at 3000
            },
            repeatMode = RepeatMode.Restart
        ),
        label = "radio_color_location"
    )

    val dummyRadioColor by infiniteTransition.animateColor(
        initialValue = Color(0xFF6B67FD),
        targetValue = Color.Transparent,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 3000
                Color(0xFF6B67FD) at 0
                Color(0xFF6B67FD) at 1000
                Color.Transparent at 1200
                Color.Transparent at 3000
            },
            repeatMode = RepeatMode.Restart
        ),
        label = "dummy_radio_color_location"
    )

    MockDeviceFrame(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(bottom = 16.dp)
        ) {
            MockStatusBar()

            // Top Bar
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color(0xFF00FF00), // Neon green arrow
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text("Location Permission", fontSize = 16.sp, color = Color.Black)
            }

            Spacer(modifier = Modifier.height(12.dp))

            // App Icon & Name
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.appicon),
                    contentDescription = "App Icon",
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("Mute Master", fontSize = 18.sp, color = Color.Black)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Divider
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Color(0xFFC0D2F3))
                    .padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "LOCATION ACCESS FOR THIS APP",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black.copy(alpha = 0.6f),
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Target Row ("Allow all the time")
            Box(contentAlignment = Alignment.CenterStart) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Radio button
                    Box(
                        modifier = Modifier
                            .size(22.dp)
                            .border(2.dp, Color.Black, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        if (radioColor != Color.Transparent) {
                            Box(
                                modifier = Modifier
                                    .size(12.dp)
                                    .background(radioColor, CircleShape)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        "Allow all the time",
                        fontSize = 16.sp,
                        color = Color.Black
                    )
                }

                // Finger tap
                Box(
                    modifier = Modifier
                        .padding(start = 20.dp)
                        .offset(y = fingerOffsetY.dp)
                        .size(40.dp)
                        .scale(fingerScale)
                        .alpha(fingerAlpha)
                        .background(Color.Gray.copy(alpha = 0.4f), CircleShape)
                )
            }

            // Dummy Row ("Allow only while using the app")
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(22.dp)
                        .border(2.dp, Color.Black, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    if (dummyRadioColor != Color.Transparent) {
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .background(dummyRadioColor, CircleShape)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    "Allow only while using the app",
                    fontSize = 16.sp,
                    color = Color.Black
                )
            }

            // Dummy Row ("Deny")
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(22.dp)
                        .border(2.dp, Color.Black, CircleShape)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    "Deny",
                    fontSize = 16.sp,
                    color = Color.Black
                )
            }
        }
    }
}

