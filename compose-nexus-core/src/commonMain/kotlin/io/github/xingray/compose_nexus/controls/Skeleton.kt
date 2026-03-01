package io.github.xingray.compose_nexus.controls

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.xingray.compose_nexus.theme.NexusTheme

/**
 * Element Plus Skeleton — a loading placeholder with shimmer animation.
 *
 * @param modifier Modifier.
 * @param loading Whether to show the skeleton. If false, [content] is displayed.
 * @param animated Whether to show shimmer animation.
 * @param rows Number of text-like rows to display.
 * @param avatar Whether to show an avatar circle placeholder.
 * @param content The real content shown when not loading.
 */
@Composable
fun NexusSkeleton(
    modifier: Modifier = Modifier,
    loading: Boolean = true,
    animated: Boolean = true,
    rows: Int = 3,
    avatar: Boolean = false,
    content: (@Composable () -> Unit)? = null,
) {
    if (!loading && content != null) {
        content()
        return
    }

    val baseColor = NexusTheme.colorScheme.fill.base
    val highlightColor = NexusTheme.colorScheme.fill.light

    val shimmerAlpha = if (animated) {
        val transition = rememberInfiniteTransition(label = "skeleton")
        val alpha by transition.animateFloat(
            initialValue = 1f,
            targetValue = 0.4f,
            animationSpec = infiniteRepeatable(
                animation = tween(1500, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse,
            ),
            label = "skeleton-alpha",
        )
        alpha
    } else {
        1f
    }

    val color = baseColor.copy(alpha = shimmerAlpha)

    Row(modifier = modifier.fillMaxWidth()) {
        if (avatar) {
            SkeletonBlock(
                modifier = Modifier.size(40.dp),
                color = color,
                shape = SkeletonShape.Circle,
            )
            Spacer(modifier = Modifier.width(16.dp))
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            // First row is shorter (title-like)
            SkeletonBlock(
                modifier = Modifier
                    .fillMaxWidth(0.4f)
                    .height(16.dp),
                color = color,
            )
            // Subsequent rows
            repeat(rows) { index ->
                val widthFraction = if (index == rows - 1) 0.6f else 1f
                SkeletonBlock(
                    modifier = Modifier
                        .fillMaxWidth(widthFraction)
                        .height(16.dp),
                    color = color,
                )
            }
        }
    }
}

private enum class SkeletonShape { Rect, Circle }

@Composable
private fun SkeletonBlock(
    modifier: Modifier = Modifier,
    color: Color,
    shape: SkeletonShape = SkeletonShape.Rect,
) {
    val clipShape = when (shape) {
        SkeletonShape.Circle -> CircleShape
        SkeletonShape.Rect -> NexusTheme.shapes.base
    }
    Box(
        modifier = modifier
            .clip(clipShape)
            .background(color),
    )
}
