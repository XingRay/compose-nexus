package io.github.xingray.compose_nexus.controls

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import kotlin.math.max

enum class ProgressType {
    Line,
    Circle,
    Dashboard,
}

enum class ProgressStrokeLinecap {
    Butt,
    Round,
    Square,
}

data class ProgressColorStop(
    val color: Color,
    val percentage: Float,
)

@Composable
fun NexusProgress(
    percentage: Float,
    modifier: Modifier = Modifier,
    type: ProgressType = ProgressType.Line,
    status: NexusType = NexusType.Primary,
    strokeWidth: Dp = 6.dp,
    textInside: Boolean = false,
    indeterminate: Boolean = false,
    duration: Float = 3f,
    color: Color? = null,
    colorFunction: ((Float) -> Color)? = null,
    colorStops: List<ProgressColorStop> = emptyList(),
    showText: Boolean = true,
    strokeLinecap: ProgressStrokeLinecap = ProgressStrokeLinecap.Round,
    format: ((Float) -> String)? = null,
    striped: Boolean = false,
    stripedFlow: Boolean = false,
    circleSize: Dp = 126.dp,
    width: Dp? = null,
    content: (@Composable (percentage: Float) -> Unit)? = null,
) {
    val colorScheme = NexusTheme.colorScheme
    val clamped = percentage.coerceIn(0f, 100f)
    val animatedPercentage by animateFloatAsState(
        targetValue = clamped,
        animationSpec = NexusTheme.motion.tweenDefault(),
        label = "progress",
    )

    val flowTransition = rememberInfiniteTransition(label = "progressFlow")
    val indeterminateTransition = rememberInfiniteTransition(label = "progressIndeterminate")
    val stripePhase by flowTransition.animateFloat(
        initialValue = 0f,
        targetValue = 24f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = (duration * 1000f).toInt().coerceAtLeast(300),
                easing = LinearEasing,
            ),
            repeatMode = RepeatMode.Restart,
        ),
        label = "stripePhase",
    )
    val indeterminateStart by indeterminateTransition.animateFloat(
        initialValue = -0.35f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = (duration * 1000f).toInt().coerceAtLeast(500),
                easing = LinearEasing,
            ),
            repeatMode = RepeatMode.Restart,
        ),
        label = "indeterminateStart",
    )

    val displayPercent = if (indeterminate) ((indeterminateStart * 100f).coerceIn(0f, 100f)) else animatedPercentage
    val fraction = if (indeterminate) 0.3f else (animatedPercentage / 100f)
    val activeColor = resolveProgressColor(
        percentage = animatedPercentage,
        color = color,
        colorFunction = colorFunction,
        colorStops = colorStops,
        defaultColor = colorScheme.typeColor(status)?.base ?: colorScheme.primary.base,
    )
    val trackColor = colorScheme.border.lighter
    val text = format?.invoke(animatedPercentage) ?: "${animatedPercentage.toInt()}%"
    val cap = when (strokeLinecap) {
        ProgressStrokeLinecap.Butt -> StrokeCap.Butt
        ProgressStrokeLinecap.Round -> StrokeCap.Round
        ProgressStrokeLinecap.Square -> StrokeCap.Square
    }

    when (type) {
        ProgressType.Line -> LineProgress(
            modifier = modifier,
            fraction = fraction,
            indeterminate = indeterminate,
            indeterminateStart = indeterminateStart,
            activeColor = activeColor,
            trackColor = trackColor,
            strokeWidth = strokeWidth,
            striped = striped,
            stripedFlow = stripedFlow,
            stripePhase = stripePhase,
            showText = showText,
            textInside = textInside,
            text = text,
            content = content,
        )

        ProgressType.Circle, ProgressType.Dashboard -> CircleLikeProgress(
            modifier = modifier,
            type = type,
            fraction = fraction,
            indeterminate = indeterminate,
            indeterminateStart = indeterminateStart,
            activeColor = activeColor,
            trackColor = trackColor,
            strokeWidth = strokeWidth,
            showText = showText,
            text = text,
            content = content,
            size = width ?: circleSize,
            cap = cap,
        )
    }
}

@Composable
private fun LineProgress(
    modifier: Modifier,
    fraction: Float,
    indeterminate: Boolean,
    indeterminateStart: Float,
    activeColor: Color,
    trackColor: Color,
    strokeWidth: Dp,
    striped: Boolean,
    stripedFlow: Boolean,
    stripePhase: Float,
    showText: Boolean,
    textInside: Boolean,
    text: String,
    content: (@Composable (Float) -> Unit)?,
) {
    val colorScheme = NexusTheme.colorScheme
    val typography = NexusTheme.typography
    val barHeight = max(strokeWidth.value, if (textInside) 18f else strokeWidth.value).dp

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .height(barHeight)
                .clip(NexusTheme.shapes.base)
                .background(trackColor),
        ) {
            Canvas(modifier = Modifier.fillMaxWidth().height(barHeight)) {
                val progressWidth = size.width * fraction.coerceIn(0f, 1f)
                val startX = if (indeterminate) size.width * indeterminateStart else 0f
                val drawWidth = if (indeterminate) size.width * 0.35f else progressWidth
                val finalWidth = drawWidth.coerceAtLeast(0f).coerceAtMost(size.width)
                val x = startX.coerceAtLeast(0f).coerceAtMost(size.width)
                if (finalWidth > 0f) {
                    drawRect(
                        color = activeColor,
                        topLeft = Offset(x, 0f),
                        size = Size(finalWidth.coerceAtMost(size.width - x), size.height),
                    )
                    if (striped) {
                        val step = 12f
                        val shift = if (stripedFlow) stripePhase else 0f
                        var stripeX = x - size.height + shift
                        val stripeColor = Color.White.copy(alpha = 0.25f)
                        while (stripeX < x + finalWidth + size.height) {
                            drawLine(
                                color = stripeColor,
                                start = Offset(stripeX, size.height),
                                end = Offset(stripeX + size.height, 0f),
                                strokeWidth = 3f,
                            )
                            stripeX += step
                        }
                    }
                }
            }
            if (showText && textInside && !indeterminate) {
                Box(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                    contentAlignment = Alignment.CenterEnd,
                ) {
                    if (content != null) {
                        content(fraction * 100f)
                    } else {
                        NexusText(
                            text = text,
                            color = colorScheme.white,
                            style = typography.extraSmall,
                        )
                    }
                }
            }
        }

        if (showText && !textInside) {
            if (content != null) {
                content(fraction * 100f)
            } else {
                NexusText(
                    text = if (indeterminate) "..." else text,
                    color = colorScheme.text.regular,
                    style = typography.extraSmall,
                )
            }
        }
    }
}

@Composable
private fun CircleLikeProgress(
    modifier: Modifier,
    type: ProgressType,
    fraction: Float,
    indeterminate: Boolean,
    indeterminateStart: Float,
    activeColor: Color,
    trackColor: Color,
    strokeWidth: Dp,
    showText: Boolean,
    text: String,
    content: (@Composable (Float) -> Unit)?,
    size: Dp,
    cap: StrokeCap,
) {
    val colorScheme = NexusTheme.colorScheme
    val typography = NexusTheme.typography
    val baseSweep = if (type == ProgressType.Dashboard) 270f else 360f
    val startAngle = if (type == ProgressType.Dashboard) 135f else -90f
    val drawFraction = if (indeterminate) 0.25f else fraction.coerceIn(0f, 1f)
    val dynamicStart = if (indeterminate) startAngle + indeterminateStart * baseSweep else startAngle

    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center,
    ) {
        Canvas(modifier = Modifier.size(size)) {
            val strokePx = strokeWidth.toPx()
            val diameter = this.size.minDimension - strokePx
            val topLeft = Offset(strokePx / 2, strokePx / 2)
            val arcSize = Size(diameter, diameter)

            drawArc(
                color = trackColor,
                startAngle = startAngle,
                sweepAngle = baseSweep,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = strokePx, cap = cap),
            )
            drawArc(
                color = activeColor,
                startAngle = dynamicStart,
                sweepAngle = baseSweep * drawFraction,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = strokePx, cap = cap),
            )
        }
        if (showText) {
            if (content != null) {
                content(drawFraction * 100f)
            } else {
                NexusText(
                    text = if (indeterminate) "..." else text,
                    color = colorScheme.text.primary,
                    style = typography.base,
                )
            }
        }
    }
}

private fun resolveProgressColor(
    percentage: Float,
    color: Color?,
    colorFunction: ((Float) -> Color)?,
    colorStops: List<ProgressColorStop>,
    defaultColor: Color,
): Color {
    if (color != null) return color
    if (colorFunction != null) return colorFunction(percentage)
    if (colorStops.isNotEmpty()) {
        val sorted = colorStops.sortedBy { it.percentage }
        for (stop in sorted) {
            if (percentage <= stop.percentage) return stop.color
        }
        return sorted.last().color
    }
    return defaultColor
}
