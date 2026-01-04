package com.dipdev.themutemaster.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun LocationImage() {
    // 1. Material Theme Mapping
    // This automatically adapts to Dark Mode and your app's color palette.
    val outlineColor = MaterialTheme.colorScheme.primary
    // If primaryContainer is too dark in your specific theme, use: outlineColor.copy(alpha = 0.2f)
    val fillColor = MaterialTheme.colorScheme.primaryContainer
    val badgeColor = MaterialTheme.colorScheme.error
    val onBadgeColor = MaterialTheme.colorScheme.onError // Color for the (+) sign
    val holeColor = MaterialTheme.colorScheme.surface // Matches screen background
    val detailColor = MaterialTheme.colorScheme.tertiary // Secondary accents

    Canvas(
        modifier = Modifier
            .size(200.dp)
            .padding(24.dp)
    ) {
        val w = size.width
        val h = size.height
        val strokeWidth = w * 0.04f

        val pinTopRadius = w * 0.35f
        val pinCenterY = pinTopRadius
        val pinCenterX = w / 2

        val pinPath = Path().apply {
            moveTo(pinCenterX - pinTopRadius, pinCenterY)
            arcTo(
                rect = Rect(
                    center = Offset(pinCenterX, pinCenterY),
                    radius = pinTopRadius
                ),
                startAngleDegrees = 180f,
                sweepAngleDegrees = 180f,
                forceMoveTo = false
            )
            lineTo(pinCenterX, h * 0.9f)
            close()
        }

        drawPath(
            path = pinPath,
            color = fillColor,
            style = Fill
        )
        drawPath(
            path = pinPath,
            color = outlineColor,
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
        )

        // Center Hole
        drawCircle(
            color = holeColor,
            radius = pinTopRadius * 0.4f,
            center = Offset(pinCenterX, pinCenterY)
        )
        drawCircle(
            color = outlineColor.copy(alpha = 0.5f),
            radius = pinTopRadius * 0.4f,
            center = Offset(pinCenterX, pinCenterY),
            style = Stroke(width = strokeWidth * 0.5f)
        )

        val badgeRadius = w * 0.1f
        val badgeCenter = Offset(
            x = pinCenterX + pinTopRadius * 0.8f,
            y = pinCenterY - pinTopRadius * 0.8f
        )

        drawCircle(
            color = badgeColor,
            radius = badgeRadius,
            center = badgeCenter
        )

        val plusSize = badgeRadius * 0.6f
        val plusPath = Path().apply {
            moveTo(badgeCenter.x - plusSize, badgeCenter.y)
            lineTo(badgeCenter.x + plusSize, badgeCenter.y)
            moveTo(badgeCenter.x, badgeCenter.y - plusSize)
            lineTo(badgeCenter.x, badgeCenter.y + plusSize)
        }

        // Use onBadgeColor (usually white) for the plus sign
        drawPath(
            path = plusPath,
            color = onBadgeColor,
            style = Stroke(width = strokeWidth * 0.8f, cap = StrokeCap.Round)
        )

        drawCircle(
            color = detailColor,
            radius = w * 0.025f,
            center = Offset(w * 0.9f, h * 0.1f),
            style = Stroke(width = strokeWidth * 0.5f)
        )

        drawCircle(
            color = outlineColor,
            radius = w * 0.025f,
            center = Offset(w * 0.05f, h * 0.5f),
            style = Stroke(width = strokeWidth * 0.5f)
        )

        drawCircle(
            color = outlineColor,
            radius = w * 0.03f,
            center = Offset(w * 0.85f, h * 0.7f),
            style = Stroke(width = strokeWidth * 0.5f)
        )
    }
}