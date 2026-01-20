package com.dipdev.themutemaster.ui.screens.onboarding

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dipdev.themutemaster.R
import com.dipdev.themutemaster.ui.components.AppLogo // Your existing component
import kotlinx.coroutines.delay

@Composable
fun Welcome(
    modifier: Modifier = Modifier,
    onGetStarted: () -> Unit
) {
    // 1. ANIMATION STATES
    val scale = remember { Animatable(0.8f) }
    val opacity = remember { Animatable(0f) }

    // Trigger animation on entry
    LaunchedEffect(Unit) {
        scale.animateTo(
            targetValue = 1f,
            animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
        )
        delay(100)
        opacity.animateTo(
            targetValue = 1f,
            animationSpec = tween(500)
        )
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        // Background Gradient Blob (Optional: Adds a modern glow)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
                            MaterialTheme.colorScheme.background
                        )
                    )
                )
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(24.dp)
        ) {
            Spacer(Modifier.height(40.dp))

            // 2. HEADER
            // Fade this in slightly
            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                // Decorative Circle behind the image
                Box(
                    modifier = Modifier
                        .size(280.dp)
                        .scale(scale.value) // Bounce animation
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                )

                // Your Main Image
                Image(
                    painter = painterResource(R.drawable.appicon), // Make sure this is high res
                    contentDescription = "Hero Image",
                    modifier = Modifier
                        .size(200.dp)
                        .scale(scale.value)
                )
            }

            // 3. TEXT SECTION (Animated Opacity)
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .weight(0.8f) // Takes up bottom space
                    .alpha(opacity.value) // Fade in
            ) {
                // App Logo or Name
                AppLogo()

                Spacer(Modifier.height(16.dp))

                Text(
                    text = "Welcome to MuteMaster",
                    style = MaterialTheme.typography.headlineLarge, // Slightly smaller than Display for better fit
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(Modifier.height(12.dp))

                // Rich Text: Highlight key words with color
                Text(
                    text = buildAnnotatedString {
                        append("Say goodbye to manual controls.\nAutomate your audio with a ")
                        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)) {
                            append("smart manager")
                        }
                        append(".")
                    },
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 24.sp
                )
            }

            // 4. ACTION BUTTON
            Button(
                onClick = onGetStarted,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .scale(scale.value), // Slight bounce match
                shape = RoundedCornerShape(16.dp), // Modern "Squircle"
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp)
            ) {
                Text(
                    text = "Get Started",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.width(8.dp))
                Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null)
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}

// Helper extension for alpha if not using standard Modifier.alpha
fun Modifier.alpha(alpha: Float) = this.then(Modifier.graphicsLayer(alpha = alpha))