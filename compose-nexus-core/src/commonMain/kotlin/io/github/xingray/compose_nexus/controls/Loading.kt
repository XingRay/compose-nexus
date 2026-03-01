package io.github.xingray.compose_nexus.controls

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.xingray.compose_nexus.theme.NexusTheme

/**
 * Element Plus Loading — a loading overlay or inline spinner.
 *
 * When [fullscreen] is true, the loading overlay covers the entire parent.
 * Otherwise it displays inline as a spinner with optional text.
 *
 * @param modifier Modifier.
 * @param loading Whether to show the loading state.
 * @param text Optional loading text below the spinner.
 * @param fullscreen Whether to overlay the full parent area.
 * @param spinnerSize Spinner diameter.
 * @param content Content to overlay when [fullscreen] and [loading].
 */
@Composable
fun NexusLoading(
    modifier: Modifier = Modifier,
    loading: Boolean = true,
    text: String? = null,
    fullscreen: Boolean = false,
    spinnerSize: Dp = 40.dp,
    content: (@Composable () -> Unit)? = null,
) {
    if (fullscreen && content != null) {
        Box(modifier = modifier) {
            content()
            if (loading) {
                LoadingOverlay(text = text, spinnerSize = spinnerSize)
            }
        }
    } else {
        if (loading) {
            LoadingSpinner(
                modifier = modifier,
                text = text,
                spinnerSize = spinnerSize,
            )
        } else {
            content?.invoke()
        }
    }
}

@Composable
private fun LoadingOverlay(
    text: String?,
    spinnerSize: Dp,
) {
    val colorScheme = NexusTheme.colorScheme

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorScheme.mask.base)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
            ) { /* consume clicks */ },
        contentAlignment = Alignment.Center,
    ) {
        LoadingSpinner(text = text, spinnerSize = spinnerSize)
    }
}

@Composable
private fun LoadingSpinner(
    modifier: Modifier = Modifier,
    text: String?,
    spinnerSize: Dp,
) {
    val colorScheme = NexusTheme.colorScheme
    val typography = NexusTheme.typography

    val infiniteTransition = rememberInfiniteTransition(label = "loading-spin")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "loading-rotation",
    )

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Canvas(
            modifier = Modifier
                .size(spinnerSize)
                .graphicsLayer { rotationZ = rotation },
        ) {
            val strokeWidth = size.minDimension * 0.1f
            val arcSize = size.minDimension - strokeWidth
            drawArc(
                color = colorScheme.primary.base,
                startAngle = 0f,
                sweepAngle = 270f,
                useCenter = false,
                topLeft = androidx.compose.ui.geometry.Offset(strokeWidth / 2, strokeWidth / 2),
                size = androidx.compose.ui.geometry.Size(arcSize, arcSize),
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
            )
        }

        if (text != null) {
            Spacer(modifier = Modifier.height(12.dp))
            NexusText(
                text = text,
                color = colorScheme.primary.base,
                style = typography.small,
            )
        }
    }
}
