package com.dipdev.themutemaster.ui.components

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dipdev.themutemaster.R

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
        Text("17:46", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.DarkGray)
        Box(
            modifier = Modifier
                .width(80.dp)
                .height(20.dp)
                .background(Color(0xFF222222), RoundedCornerShape(10.dp))
        )
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(1.dp), verticalAlignment = Alignment.Bottom, modifier = Modifier.height(12.dp)) {
                Box(modifier = Modifier.width(3.dp).height(4.dp).background(Color.Gray))
                Box(modifier = Modifier.width(3.dp).height(6.dp).background(Color.Gray))
                Box(modifier = Modifier.width(3.dp).height(9.dp).background(Color.Gray))
                Box(modifier = Modifier.width(3.dp).height(12.dp).background(Color.Gray))
            }
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
fun MockSettingsScreen(
    modifier: Modifier = Modifier,
    title: String,
    content: @Composable () -> Unit
) {
    MockDeviceFrame(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(bottom = 16.dp)
        ) {
            MockStatusBar()

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.Black,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(title, fontSize = 16.sp, color = Color.Black)
            }

            Spacer(modifier = Modifier.height(12.dp))
            
            Box(modifier = Modifier.fillMaxWidth().weight(1f).clipToBounds()) {
                content()
            }
        }
    }
}

@Composable
fun AppListRow(
    name: String, 
    status: String, 
    isMuteMaster: Boolean = false, 
    highlightAlpha: Float = 0f,
    iconVector: androidx.compose.ui.graphics.vector.ImageVector? = null,
    iconBgColor: Color = Color.LightGray,
    iconTint: Color = Color.White
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.LightGray.copy(alpha = highlightAlpha))
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (isMuteMaster) {
            Image(
                painter = painterResource(id = R.drawable.appicon),
                contentDescription = null,
                modifier = Modifier.size(40.dp).clip(CircleShape)
            )
        } else if (iconVector != null) {
            Box(
                modifier = Modifier.size(40.dp).background(iconBgColor, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = iconVector, contentDescription = null, tint = iconTint, modifier = Modifier.size(24.dp))
            }
        } else {
            Box(
                modifier = Modifier.size(40.dp).background(Color.LightGray, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(name.first().toString(), fontSize = 20.sp, color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(name, fontSize = 16.sp, color = Color.Black)
            Text(status, fontSize = 12.sp, color = Color.Gray)
        }
    }
}

@Composable
fun AnimatedDndInstruction(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "dnd")

    val listScrollOffset by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 6000
                0f at 0
                0f at 1000
                -60f at 2000
                -60f at 6000
            }
        ), label = "scroll"
    )

    val finger1OffsetY by infiniteTransition.animateFloat(
        initialValue = -50f, targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 6000
                -50f at 0
                -50f at 2200
                0f at 2500
                0f at 3200
                0f at 6000
            }
        ), label = "f1_y"
    )

    val finger2OffsetY by infiniteTransition.animateFloat(
        initialValue = -50f, targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 6000
                -50f at 0
                -50f at 4000
                0f at 4300
                0f at 5200
                0f at 6000
            }
        ), label = "f2_y"
    )

    val screen1Alpha by infiniteTransition.animateFloat(
        initialValue = 1f, targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 6000
                1f at 0
                1f at 3400
                0f at 3600
                0f at 6000
            }
        ), label = "s1_alpha"
    )

    val screen2Alpha by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 6000
                0f at 0
                0f at 3400
                1f at 3600
                1f at 6000
            }
        ), label = "s2_alpha"
    )

    val finger1Alpha by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 6000
                0f at 0
                0f at 2200
                1f at 2500
                1f at 3200
                0f at 3400
                0f at 6000
            }
        ), label = "f1_a"
    )
    val finger1Scale by infiniteTransition.animateFloat(
        initialValue = 1.2f, targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 6000
                1.2f at 0
                1.2f at 2800
                0.9f at 3000
                1.2f at 3200
                1.2f at 6000
            }
        ), label = "f1_s"
    )
    
    val finger2Alpha by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 6000
                0f at 0
                0f at 4000
                1f at 4300
                1f at 5200
                0f at 5500
                0f at 6000
            }
        ), label = "f2_a"
    )
    val finger2Scale by infiniteTransition.animateFloat(
        initialValue = 1.2f, targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 6000
                1.2f at 0
                1.2f at 4800
                0.9f at 5000
                1.2f at 5200
                1.2f at 6000
            }
        ), label = "f2_s"
    )

    val rowHighlightAlpha by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 6000
                0f at 0
                0f at 2800
                0.6f at 3000
                0f at 3400
                0f at 6000
            }
        ), label = "row_hl"
    )

    val switchColor by infiniteTransition.animateColor(
        initialValue = Color.Gray.copy(alpha = 0.5f), targetValue = Color.Gray.copy(alpha = 0.5f),
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 6000
                Color.Gray.copy(alpha = 0.5f) at 0
                Color.Gray.copy(alpha = 0.5f) at 5000
                Color(0xFF1976D2) at 5200
                Color(0xFF1976D2) at 6000
            }
        ), label = "sw_c"
    )
    val switchOffset by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 6000
                0f at 0
                0f at 5000
                20f at 5200
                20f at 6000
            }
        ), label = "sw_o"
    )

    MockSettingsScreen(
        modifier = modifier,
        title = "\"Do Not Disturb\" access"
    ) {
        // SCREEN 1 (List)
        Box(modifier = Modifier.fillMaxSize().alpha(screen1Alpha)) {
            Column(modifier = Modifier.fillMaxWidth().offset(y = listScrollOffset.dp)) {
                AppListRow("Gmail", "Not allowed", iconVector = Icons.Default.Email, iconBgColor = Color.White, iconTint = Color(0xFFEA4335))
                AppListRow("Instagram", "Not allowed", iconVector = Icons.Default.CameraAlt, iconBgColor = Color(0xFFE1306C))
                AppListRow("Spotify", "Not allowed", iconVector = Icons.Default.PlayArrow, iconBgColor = Color(0xFF1DB954))
                Box(contentAlignment = Alignment.CenterEnd) {
                    AppListRow("Mute Master", "Not allowed", isMuteMaster = true, highlightAlpha = rowHighlightAlpha)
                    
                    Box(
                        modifier = Modifier
                            .padding(end = 40.dp, top = 20.dp)
                            .offset(y = finger1OffsetY.dp)
                            .size(40.dp)
                            .scale(finger1Scale)
                            .alpha(finger1Alpha)
                            .background(Color.Gray.copy(alpha = 0.4f), CircleShape)
                    )
                }
                AppListRow("WhatsApp", "Not allowed", iconVector = Icons.Default.Phone, iconBgColor = Color(0xFF25D366))
            }
        }

        // SCREEN 2 (Sub-screen)
        Box(modifier = Modifier.fillMaxSize().alpha(screen2Alpha).background(Color.White)) {
            Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
                Spacer(modifier = Modifier.height(16.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = R.drawable.appicon),
                        contentDescription = null,
                        modifier = Modifier.size(56.dp).clip(CircleShape)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text("Mute Master", fontSize = 18.sp, color = Color.Black)
                        Text("1.5", fontSize = 14.sp, color = Color.Gray)
                    }
                }
                Spacer(modifier = Modifier.height(32.dp))
                
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterStart) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Allow Do Not Disturb", fontSize = 16.sp, color = Color.Black)
                        
                        Box(contentAlignment = Alignment.CenterStart) {
                            Box(
                                modifier = Modifier
                                    .width(44.dp).height(24.dp)
                                    .background(switchColor, RoundedCornerShape(12.dp))
                            )
                            Box(
                                modifier = Modifier
                                    .padding(start = 2.dp)
                                    .offset(x = switchOffset.dp)
                                    .size(20.dp)
                                    .shadow(1.dp, CircleShape)
                                    .background(Color.White, CircleShape)
                            )
                            
                            Box(
                                modifier = Modifier
                                    .offset(x = 12.dp, y = finger2OffsetY.dp)
                                    .size(40.dp)
                                    .scale(finger2Scale)
                                    .alpha(finger2Alpha)
                                    .background(Color.Gray.copy(alpha = 0.4f), CircleShape)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AnimatedLocationInstruction(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "location")

    val fingerOffsetY by infiniteTransition.animateFloat(
        initialValue = -50f, targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 3000
                -50f at 0
                0f at 800
                0f at 1600
                100f at 2000
                100f at 3000
            }
        ), label = "finger_y"
    )

    val fingerAlpha by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 3000
                0f at 0
                1f at 600
                1f at 1600
                0f at 2000
                0f at 3000
            }
        ), label = "finger_a"
    )

    val fingerScale by infiniteTransition.animateFloat(
        initialValue = 1.2f, targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 3000
                1.2f at 0
                1.2f at 800
                0.9f at 1200
                1.2f at 1600
                1.2f at 3000
            }
        ), label = "finger_s"
    )

    val radioColor by infiniteTransition.animateColor(
        initialValue = Color.Transparent, targetValue = Color(0xFF6B67FD),
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 3000
                Color.Transparent at 0
                Color.Transparent at 1000
                Color(0xFF6B67FD) at 1200
                Color(0xFF6B67FD) at 3000
            }
        ), label = "radio_c"
    )

    val dummyRadioColor by infiniteTransition.animateColor(
        initialValue = Color(0xFF6B67FD), targetValue = Color.Transparent,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 3000
                Color(0xFF6B67FD) at 0
                Color(0xFF6B67FD) at 1000
                Color.Transparent at 1200
                Color.Transparent at 3000
            }
        ), label = "dummy_c"
    )

    MockSettingsScreen(
        modifier = modifier,
        title = "Location Permission"
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.appicon),
                    contentDescription = "App Icon",
                    modifier = Modifier.size(48.dp).clip(RoundedCornerShape(12.dp))
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("Mute Master", fontSize = 18.sp, color = Color.Black)
            }

            Spacer(modifier = Modifier.height(16.dp))

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

            Box(contentAlignment = Alignment.CenterStart) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier.size(22.dp).border(2.dp, Color.Black, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        if (radioColor != Color.Transparent) {
                            Box(modifier = Modifier.size(12.dp).background(radioColor, CircleShape))
                        }
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Text("Allow all the time", fontSize = 16.sp, color = Color.Black)
                }

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

            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier.size(22.dp).border(2.dp, Color.Black, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    if (dummyRadioColor != Color.Transparent) {
                        Box(modifier = Modifier.size(12.dp).background(dummyRadioColor, CircleShape))
                    }
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text("Allow only while using the app", fontSize = 16.sp, color = Color.Black)
            }

            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(modifier = Modifier.size(22.dp).border(2.dp, Color.Black, CircleShape))
                Spacer(modifier = Modifier.width(16.dp))
                Text("Deny", fontSize = 16.sp, color = Color.Black)
            }
        }
    }
}
