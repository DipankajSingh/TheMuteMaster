package com.dipdev.themutemaster.ui.screens.onboarding

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import com.dipdev.themutemaster.R

@Composable
fun Welcome(
    onGetStarted: () -> Unit,
    onPrivacyClick: () -> Unit={},
    onTermsClick: () -> Unit={}
) {
    var showContent by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        showContent = true
    }

    val brush = Brush.radialGradient(
        colors = listOf(
            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f),
            MaterialTheme.colorScheme.background
        ),
        radius = 900f
    )

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        // 1. Get Screen Height for Responsive Layout
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .background(brush)
                .padding(padding)
        ) {
            val screenHeight = maxHeight

            // 2. SCROLLABLE COLUMN
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()) // Enable scrolling
                    .heightIn(min = screenHeight),         // Force full height for spacing
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween // Spreads items Top/Center/Bottom
            ) {

                // --- TOP SECTION (Logo) ---
                Box(
                    modifier = Modifier
                        .padding(top = 60.dp)
                        .padding(bottom = 24.dp) // Safety padding for small screens
                ) {
                    AnimatedVisibilityBlock(visible = showContent, delay = 100) {
                        AppLogo()
                    }
                }

                // --- MIDDLE SECTION (Hero + Text) ---
                // We group these so they stay together in the center
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(horizontal = 24.dp)
                ) {
                    // Hero Image
                    Box(contentAlignment = Alignment.Center) {
                        PulseEffect()
                        Surface(
                            shape = CircleShape,
                            shadowElevation = 24.dp,
                            tonalElevation = 8.dp,
                            modifier = Modifier.size(140.dp)
                        ) {
                            Image(
                                painter = painterResource(R.drawable.appicon),
                                contentDescription = "Logo",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(24.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(40.dp))

                    // Title
                    AnimatedVisibilityBlock(visible = showContent, delay = 300) {
                        Text(
                            text = "Master Your Silence",
                            style = MaterialTheme.typography.displaySmall,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Description
                    AnimatedVisibilityBlock(visible = showContent, delay = 400) {
                        Text(
                            text = buildAnnotatedString {
                                append("Your phone should know when to be quiet.\nAutomate your audio with ")
                                withStyle(SpanStyle(color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)) {
                                    append("smart geofences")
                                }
                                append(".")
                            },
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = 26.sp
                        )
                    }
                }

                // --- BOTTOM SECTION (Button + Footer) ---
                Column(
                    modifier = Modifier
                        .padding(24.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(24.dp)) // Extra space before button

                    // Button
                    AnimatedVisibilityBlock(visible = showContent, delay = 500) {
                        Button(
                            onClick = onGetStarted,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(58.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            ),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
                        ) {
                            Text(
                                text = "Get Started",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(Modifier.width(12.dp))
                            Icon(Icons.AutoMirrored.Rounded.ArrowForward, null)
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Legal Footer
                    AnimatedVisibilityBlock(visible = showContent, delay = 600) {
                        LegalFooter(onPrivacyClick, onTermsClick)
                    }
                }
            }
        }
    }
}

// ==========================================
//        HELPER COMPONENTS
// ==========================================

@Composable
fun LegalFooter(
    onPrivacyClick: () -> Unit,
    onTermsClick: () -> Unit
) {
    val annotatedString = buildAnnotatedString {
        append("By continuing, you agree to our ")

        // Tag: TERMS
        pushStringAnnotation(tag = "TERMS", annotation = "terms")
        withStyle(
            style = SpanStyle(
                color = MaterialTheme.colorScheme.primary,
                textDecoration = TextDecoration.Underline,
                fontWeight = FontWeight.SemiBold
            )
        ) {
            append("Terms of Service")
        }
        pop()

        append(" and ")

        // Tag: PRIVACY
        pushStringAnnotation(tag = "PRIVACY", annotation = "privacy")
        withStyle(
            style = SpanStyle(
                color = MaterialTheme.colorScheme.primary,
                textDecoration = TextDecoration.Underline,
                fontWeight = FontWeight.SemiBold
            )
        ) {
            append("Privacy Policy")
        }
        pop()

        append(".")
    }

    ClickableText(
        text = annotatedString,
        style = MaterialTheme.typography.labelMedium.copy(
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
            lineHeight = 18.sp
        ),
        onClick = { offset ->
            // Check if user clicked the "TERMS" tag
            annotatedString.getStringAnnotations(tag = "TERMS", start = offset, end = offset)
                .firstOrNull()?.let {
                    onTermsClick()
                }

            // Check if user clicked the "PRIVACY" tag
            annotatedString.getStringAnnotations(tag = "PRIVACY", start = offset, end = offset)
                .firstOrNull()?.let {
                    onPrivacyClick()
                }
        },
        modifier = Modifier.padding(horizontal = 16.dp)
    )
}

@Composable
fun AppLogo() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            imageVector = Icons.Default.GraphicEq,
            contentDescription = null,
            modifier = Modifier.size(28.dp),
            tint = MaterialTheme.colorScheme.primary,
        )
        Row(verticalAlignment = Alignment.Bottom) {
            Text(
                text = "MuteMaster",
                style = androidx.compose.ui.text.TextStyle(
                    fontFamily = FontFamily(Font(R.font.sekuya_regular)),
                    fontSize = 22.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            )
            Spacer(modifier = Modifier.width(2.dp))
            Box(
                modifier = Modifier
                    .padding(bottom = 5.dp)
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
            )
        }
    }
}

@Composable
fun PulseEffect() {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    PulseRing(infiniteTransition, delay = 0)
    PulseRing(infiniteTransition, delay = 700)
    PulseRing(infiniteTransition, delay = 1400)
}

@Composable
fun PulseRing(transition: InfiniteTransition, delay: Int) {
    val scale by transition.animateFloat(
        initialValue = 1f,
        targetValue = 2.5f,
        animationSpec = infiniteRepeatable(
            animation = tween(2500, delayMillis = delay, easing = LinearOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "scale"
    )

    val alpha by transition.animateFloat(
        initialValue = 0.6f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(2500, delayMillis = delay, easing = LinearOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "alpha"
    )

    Box(
        modifier = Modifier
            .size(140.dp)
            .scale(scale)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.primary.copy(alpha = alpha),
                shape = CircleShape
            )
    )
}

@Composable
fun AnimatedVisibilityBlock(
    visible: Boolean,
    delay: Int,
    content: @Composable () -> Unit
) {
    val alpha = remember { Animatable(0f) }
    val translationY = remember { Animatable(50f) }

    LaunchedEffect(visible) {
        if (visible) {
            delay(delay.toLong())
            alpha.animateTo(1f, tween(500))
        }
    }
    LaunchedEffect(visible) {
        if (visible) {
            delay(delay.toLong())
            translationY.animateTo(0f, spring(dampingRatio = 0.6f, stiffness = 100f))
        }
    }

    Box(
        modifier = Modifier
            .graphicsLayer {
                this.alpha = alpha.value
                this.translationY = translationY.value
            }
    ) {
        content()
    }
}