package io.github.xingray.compose_nexus.controls

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.xingray.compose_nexus.theme.NexusTheme
import io.github.xingray.compose_nexus.theme.NexusType
import io.github.xingray.compose_nexus.theme.typeColor

/**
 * Progress display type.
 */
enum class ProgressType {
    Line,
    Circle,
}

/**
 * Element Plus Progress — a progress indicator (line or circle).
 *
 * @param percentage Current progress 0-100.
 * @param modifier Modifier.
 * @param type Line or Circle display.
 * @param status Semantic color status.
 * @param strokeWidth Track thickness.
 * @param showText Show percentage text.
 * @param circleSize Diameter for Circle type.
 */
@Composable
fun NexusProgress(
    percentage: Float,
    modifier: Modifier = Modifier,
    type: ProgressType = ProgressType.Line,
    status: NexusType = NexusType.Primary,
    strokeWidth: Dp = 6.dp,
    showText: Boolean = true,
    circleSize: Dp = 126.dp,
) {
    val colorScheme = NexusTheme.colorScheme
    val typography = NexusTheme.typography

    val clamped = percentage.coerceIn(0f, 100f)
    val animatedPercentage by animateFloatAsState(
        targetValue = clamped,
        animationSpec = NexusTheme.motion.tweenDefault(),
        label = "progress",
    )
    val fraction = animatedPercentage / 100f

    val activeColor = colorScheme.typeColor(status)?.base ?: colorScheme.primary.base
    val trackColor = colorScheme.border.lighter

    when (type) {
        ProgressType.Line -> LineProgress(
            fraction = fraction,
            percentage = clamped,
            activeColor = activeColor,
            trackColor = trackColor,
            strokeWidth = strokeWidth,
            showText = showText,
            modifier = modifier,
        )
        ProgressType.Circle -> CircleProgress(
            fraction = fraction,
            percentage = clamped,
            activeColor = activeColor,
            trackColor = trackColor,
            strokeWidth = strokeWidth,
            showText = showText,
            circleSize = circleSize,
            modifier = modifier,
        )
    }
}

@Composable
private fun LineProgress(
    fraction: Float,
    percentage: Float,
    activeColor: Color,
    trackColor: Color,
    strokeWidth: Dp,
    showText: Boolean,
    modifier: Modifier,
) {
    val typography = NexusTheme.typography
    val colorScheme = NexusTheme.colorScheme

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .height(strokeWidth)
                .clip(CircleShape)
                .background(trackColor),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(fraction)
                    .height(strokeWidth)
                    .clip(CircleShape)
                    .background(activeColor),
            )
        }
        if (showText) {
            NexusText(
                text = "${percentage.toInt()}%",
                modifier = Modifier.size(width = 40.dp, height = strokeWidth + 8.dp),
                color = colorScheme.text.regular,
                style = typography.extraSmall,
            )
        }
    }
}

@Composable
private fun CircleProgress(
    fraction: Float,
    percentage: Float,
    activeColor: Color,
    trackColor: Color,
    strokeWidth: Dp,
    showText: Boolean,
    circleSize: Dp,
    modifier: Modifier,
) {
    val typography = NexusTheme.typography
    val colorScheme = NexusTheme.colorScheme

    Box(
        modifier = modifier.size(circleSize),
        contentAlignment = Alignment.Center,
    ) {
        Canvas(modifier = Modifier.size(circleSize)) {
            val stroke = strokeWidth.toPx()
            val diameter = size.minDimension - stroke
            val topLeft = Offset(stroke / 2, stroke / 2)
            val arcSize = Size(diameter, diameter)

            // Track
            drawArc(
                color = trackColor,
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = stroke, cap = StrokeCap.Round),
            )
            // Active
            drawArc(
                color = activeColor,
                startAngle = -90f,
                sweepAngle = 360f * fraction,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = stroke, cap = StrokeCap.Round),
            )
        }
        if (showText) {
            NexusText(
                text = "${percentage.toInt()}%",
                color = colorScheme.text.primary,
                style = typography.large,
            )
        }
    }
}
